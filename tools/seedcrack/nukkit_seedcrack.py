#!/usr/bin/env python3
# =============================================================================
#  nukkit_seedcrack.py -- Nukkit / PM1E ワールドシード逆算ツール (Python版)
# =============================================================================
#
#  C++版 (nukkit_seedcrack.cpp) と同じアルゴリズムの参照実装。
#  NumPy でベクトル化しているため 2^32 の全探索が現実的な時間で終わる。
#
# -----------------------------------------------------------------------------
#  ■ ゲーム内でのサンプル採取手順  (詳細は README.md / .cpp ヘッダ参照)
# -----------------------------------------------------------------------------
#   採取対象 : 岩盤 (BEDROCK) の Y=2 / Y=3 / Y=4 の分布
#              Y=0,Y=1 は常に岩盤なので情報量ゼロ (掘る必要なし)
#              ワールド高さが -64..319 の環境でも岩盤は Y=0..4 に置かれる
#
#   手順 :
#     1. チャンク (0,0) / (1,0) / (0,1) / (1,1) の 4 つへ行く
#        (ワールド座標 X,Z = 0..15 / 16..31 / 0..15,16..31 / 16..31,16..31)
#     2. Y=2 に降りて 16x16 の各マスが岩盤かどうかを記録。Y=3, Y=4 も同様。
#     3. 行 = X(0..15)、行内の文字 = Z(0..15) の順で書く
#        (PopulatorBedrock のループ順 x 外側 / z 内側 と一致させる)
#     4. 溶岩や空洞で削れている柱は必ず '?' にする
#        (PopulatorCaves は Y>=2 の岩盤を溶岩に置換しうる。Y=0,1 は削られない)
#
#   必要サンプル数 :
#     ・岩盤 1 柱 = 約 2.73 bit、PRNG 実効状態は 32bit -> 理論最小 12 柱
#     ・実用推奨 : 1 チャンクあたり 16x16 全部 (短周期に落ちた場合の保険)
#       最低でも X=0..1 の 2 行 (32 柱) は必要
#     ・下位32bitのみ  : チャンク 1 個
#     ・完全な64bit    : X方向にずれたチャンクと Z方向にずれたチャンクが必須
#
#   入力形式 :
#       chunk 0 0
#       y2
#       1010110100101101     <- X=0 の行 (Z=0..15)
#       ...
#       y3
#       ...
#       y4
#       ...
#   文字: '1' '#' 'B' = 岩盤 / '0' '.' '-' = 岩盤でない / '?' = 不明
#
# -----------------------------------------------------------------------------
#  ■ 使い方
# -----------------------------------------------------------------------------
#     python3 nukkit_seedcrack.py samples.txt
#     python3 nukkit_seedcrack.py --generate 1234567890123456789 > samples.txt
#     python3 nukkit_seedcrack.py --selftest
# =============================================================================

import sys, zlib, argparse, itertools
import numpy as np

MASK32 = 0xFFFFFFFF
GOLDEN_GAMMA = 0x9E3779B97F4A7C15


# ------------------------------------------------------------ CRC32 (4-byte BE)
def crc32_be4(v: int) -> int:
    return zlib.crc32(int(v & MASK32).to_bytes(4, "big")) & MASK32


# CRC32 over a fixed 4-byte message is an affine bijection over GF(2)^32.
_CRC0 = crc32_be4(0)
_BAS_V = [0] * 32
_BAS_T = [0] * 32
for _i in range(32):
    _v = crc32_be4(1 << _i) ^ _CRC0
    _t = 1 << _i
    for _b in range(31, -1, -1):
        if not (_v >> _b) & 1:
            continue
        if not _BAS_V[_b]:
            _BAS_V[_b], _BAS_T[_b] = _v, _t
            _v = 0
            break
        _v ^= _BAS_V[_b]
        _t ^= _BAS_T[_b]


def crc32_be4_inverse(target: int) -> int:
    """x such that crc32_be4(x) == target"""
    y, x = target ^ _CRC0, 0
    for b in range(31, -1, -1):
        if not (y >> b) & 1:
            continue
        y ^= _BAS_V[b]
        x ^= _BAS_T[b]
    assert y == 0
    return x


# ------------------------------------------------------------- NukkitRandom
class NukkitRandom:
    """cn.nukkit.math.NukkitRandom -- effective state is 32 bits."""

    def __init__(self, state: int):
        self.s = state & MASK32

    @classmethod
    def from_seed(cls, seed: int):
        return cls(crc32_be4(seed & MASK32))

    def next_signed_int(self) -> int:
        prod = (self.s * 65535 + 31337) & MASK32
        t = ((prod - (1 << 32)) if prod >> 31 else prod) >> 8   # int32 arithmetic shift
        t += 1337
        self.s ^= t & MASK32
        return t

    def next_int(self) -> int:
        return self.next_signed_int() & 0x7FFFFFFF

    def next_bounded_int(self, bound: int) -> int:
        return 0 if bound == 0 else self.next_int() % bound


# --------------------------------------------------------- SplittableRandom
def mix64(z: int) -> int:
    z &= 0xFFFFFFFFFFFFFFFF
    z = ((z ^ (z >> 30)) * 0xBF58476D1CE4E5B9) & 0xFFFFFFFFFFFFFFFF
    z = ((z ^ (z >> 27)) * 0x94D049BB133111EB) & 0xFFFFFFFFFFFFFFFF
    return z ^ (z >> 31)


def split_two(seed: int):
    """Normal.init(): localSeed1, localSeed2 = SplittableRandom(seed).nextLong() x2"""
    s = seed & 0xFFFFFFFFFFFFFFFF
    return mix64(s + GOLDEN_GAMMA), mix64(s + 2 * GOLDEN_GAMMA)


# ------------------------------------------------------------------ sampling
def chunk_arg(seed: int, cx: int, cz: int) -> int:
    """Normal.generateChunk(): setSeed(cx*localSeed1 ^ cz*localSeed2 ^ levelSeed)"""
    l1, l2 = split_two(seed)
    return (cx * l1 ^ cz * l2 ^ seed) & MASK32


def populate_arg(seed: int, cx: int, cz: int) -> int:
    """Normal.populateChunk(): setSeed(0xdeadbeef ^ (cx<<8) ^ cz ^ levelSeed)"""
    return (0xDEADBEEF ^ ((cx << 8) & MASK32) ^ (cz & MASK32) ^ seed) & MASK32


def generate_bedrock(seed: int, cx: int, cz: int):
    """PopulatorBedrock.populate() -> obs[256][3] for y=2,3,4"""
    r = NukkitRandom.from_seed(chunk_arg(seed, cx, cz))
    obs = [[0, 0, 0] for _ in range(256)]
    for x in range(16):
        for z in range(16):
            for i in range(1, 5):
                placed = r.next_bounded_int(i) == 0
                if i >= 2:
                    obs[x * 16 + z][i - 2] = 1 if placed else 0
    return obs


# ------------------------------------------------- vectorized 2^32 phase A
def _step(s):
    """One NukkitRandom.nextSignedInt() over a uint32 array; returns t as uint32."""
    prod = ((s.astype(np.uint64) * np.uint64(65535) + np.uint64(31337))
            & np.uint64(MASK32)).astype(np.uint32)
    t = (prod.view(np.int32) >> np.int32(8)) + np.int32(1337)   # cannot overflow int32
    tu = t.view(np.uint32)
    s ^= tu
    return tu


def crack_states(obs, block_bits=22, verbose=True):
    """All 32-bit PRNG states whose PopulatorBedrock replay matches obs."""
    # constraint stream in PRNG call order: (bound, want|None)
    cons = []
    last = -1
    for c in range(256):
        for i in range(1, 5):
            want = None if i == 1 else obs[c][i - 2]
            cons.append((i, want))
            if want is not None:
                last = len(cons) - 1
    if last < 0:
        return []
    cons = cons[: last + 1]

    found = []
    block = 1 << block_bits
    for base in range(0, 1 << 32, block):
        s = np.arange(base, base + block, dtype=np.uint32)
        orig = s.copy()
        for bound, want in cons:
            tu = _step(s)
            if want is None:
                continue
            v = (tu & np.uint32(0x7FFFFFFF)) % np.uint32(bound)
            keep = (v == 0) if want else (v != 0)
            if not keep.all():
                s, orig = s[keep], orig[keep]
            if s.size == 0:
                break
        found.extend(int(v) for v in orig)
        if verbose and (base // block) % 128 == 127:
            print(f"    ... {100*(base+block)/2**32:.0f}%", file=sys.stderr)
    return sorted(found)


# ------------------------------------------- solve (S,L1,L2) bit by bit
def solve_triples(chunks, args):
    """arg32(cx,cz) = (cx*L1) ^ (cz*L2) ^ S over Z/2^32 (bit-triangular)."""
    cand = [(0, 0, 0)]
    for bit in range(32):
        nxt = []
        for S, L1, L2 in cand:
            for m in range(8):
                nS = S | ((m & 1) << bit)
                nL1 = L1 | (((m >> 1) & 1) << bit)
                nL2 = L2 | (((m >> 2) & 1) << bit)
                ok = True
                for (cx, cz), a in zip(chunks, args):
                    lhs = (cx * nL1 ^ cz * nL2 ^ nS) & MASK32
                    if ((lhs >> bit) & 1) != ((a >> bit) & 1):
                        ok = False
                        break
                if ok:
                    nxt.append((nS, nL1, nL2))
        cand = nxt
        if not cand:
            return []
        if len(cand) > 200000:
            print("under-constrained: add a chunk differing on both axes", file=sys.stderr)
            return []
    return cand


# ------------------------------------------------------------ phase B (hi32)
def find_high(triples, verbose=True):
    """Recover the upper 32 bits by inverting SplittableRandom."""
    quick = [0, MASK32] + list(range(0x100, 0x401))
    for S, L1, L2 in triples:
        for h in quick:
            seed = (h << 32) | S
            if mix64(seed + GOLDEN_GAMMA) & MASK32 == L1 and \
               mix64(seed + 2 * GOLDEN_GAMMA) & MASK32 == L2:
                return seed, (S, L1, L2)
    if verbose:
        print("    tier 1 miss -> exhaustive 2^32 sweep (vectorized)", file=sys.stderr)
    C1, C2, C3 = np.uint64(0xBF58476D1CE4E5B9), np.uint64(0x94D049BB133111EB), np.uint64(GOLDEN_GAMMA)

    def vmix(z):
        z = (z ^ (z >> np.uint64(30))) * C1
        z = (z ^ (z >> np.uint64(27))) * C2
        return z ^ (z >> np.uint64(31))

    block = 1 << 22
    for S, L1, L2 in triples:
        for base in range(0, 1 << 32, block):
            h = np.arange(base, base + block, dtype=np.uint64)
            seed = (h << np.uint64(32)) | np.uint64(S)
            m1 = vmix(seed + C3).astype(np.uint32)
            hit = np.nonzero(m1 == np.uint32(L1))[0]
            if hit.size:
                m2 = vmix(seed[hit] + np.uint64(2) * C3).astype(np.uint32)
                good = hit[m2 == np.uint32(L2)]
                if good.size:
                    return int(seed[good[0]]), (S, L1, L2)
    return None, None


# ------------------------------------------------------------------- parsing
_CHARMAP = {"1": 1, "#": 1, "B": 1, "b": 1, "0": 0, ".": 0, "-": 0}


def parse_samples(path):
    chunks, cur, layer, row = [], None, -1, 0
    with open(path) as f:
        for raw in f:
            line = raw.rstrip("\r\n").rstrip()
            if not line or line[0] in "#;":
                continue
            tok = line.split()
            if tok[0] == "chunk":
                if cur:
                    chunks.append(cur)
                cur = {"cx": int(tok[1]), "cz": int(tok[2]),
                       "obs": [[None, None, None] for _ in range(256)], "known": 0}
                layer, row = -1, 0
            elif tok[0] in ("y2", "y3", "y4"):
                layer, row = int(tok[0][1]) - 2, 0
            else:
                if cur is None or layer < 0:
                    raise SystemExit(f"data before chunk/y header: {line}")
                for z, ch in enumerate(line[:16]):
                    if ch in _CHARMAP:
                        cur["obs"][row * 16 + z][layer] = _CHARMAP[ch]
                        cur["known"] += 1
                row += 1
    if cur:
        chunks.append(cur)
    if not chunks:
        raise SystemExit("no chunks parsed")
    return chunks


def emit_samples(seed, coords=((0, 0), (1, 0), (0, 1), (1, 1))):
    out = [f"# generated from world seed {seed}"]
    for cx, cz in coords:
        obs = generate_bedrock(seed, cx, cz)
        out.append(f"chunk {cx} {cz}")
        for layer, name in enumerate(("y2", "y3", "y4")):
            out.append(name)
            for x in range(16):
                out.append("".join(str(obs[x * 16 + z][layer]) for z in range(16)))
    return "\n".join(out)


# ------------------------------------------------------------------ pipeline
def run_crack(chunks):
    cand_args = []
    for c in chunks:
        print(f"  chunk({c['cx']},{c['cz']}): {c['known']} observations -> phase A sweep (2^32)...")
        sys.stdout.flush()
        states = crack_states(c["obs"])
        if not states:
            print("    no PRNG state reproduces this pattern -- check rows(X)/chars(Z) "
                  "orientation, chunk coords, and mark carved columns '?'", file=sys.stderr)
            return 1
        args = sorted({crc32_be4_inverse(st) for st in states})
        print(f"    {len(states)} state(s) -> {len(args)} candidate arg(s): "
              + " ".join(f"0x{a:08x}" for a in args[:8]) + (" ..." if len(args) > 8 else ""))
        cand_args.append(args)

    coords = [(c["cx"], c["cz"]) for c in chunks]
    triples = set()
    total = 1
    for a in cand_args:
        total *= len(a)
    if total > 500000:
        print(f"  too many combinations ({total}); sample more columns", file=sys.stderr)
        return 1
    print(f"  testing {total} arg combination(s)")
    for combo in itertools.product(*cand_args):
        triples.update(solve_triples(coords, combo))
    if not triples:
        print("  no consistent (S,L1,L2) -- check chunk coordinates", file=sys.stderr)
        return 1
    triples = sorted(triples)
    print(f"  {len(triples)} consistent (S,L1,L2) candidate(s)")
    lows = sorted({t[0] for t in triples})
    print("  low 32 bits of the world seed: "
          + " ".join(f"0x{v:08x}({np.int32(v)})" for v in lows[:8]))

    print("  phase B: recovering the high 32 bits via SplittableRandom...")
    sys.stdout.flush()
    seed, win = find_high(triples)
    if seed is None:
        print("  high 32 bits not found", file=sys.stderr)
        return 1

    ok = True
    for c in chunks:
        ref = generate_bedrock(seed, c["cx"], c["cz"])
        for i in range(256):
            for k in range(3):
                if c["obs"][i][k] is not None and c["obs"][i][k] != ref[i][k]:
                    ok = False
    signed = seed - (1 << 64) if seed >> 63 else seed
    print(f"\n  ==> WORLD SEED = {signed}   (0x{seed:016x})")
    print(f"      low32=0x{win[0]:08x}  localSeed1_low32=0x{win[1]:08x}  "
          f"localSeed2_low32=0x{win[2]:08x}")
    print(f"      re-generated bedrock matches every sample: {'YES' if ok else 'NO'}")
    return 0 if ok else 1


def selftest():
    l1, l2 = split_two(1234567890123456789)
    assert l1 == 0x9904EEE77E231DB2 and l2 == 0x70EE7EB0313EC9B8, "SplittableRandom mismatch"
    print("[selftest] SplittableRandom vs JDK: OK")
    for t in (0, 1, 0x7DE98115, 0xDEADBEEF, MASK32):
        assert crc32_be4_inverse(crc32_be4(t)) == t
    print("[selftest] CRC32 4-byte inversion: OK")
    assert chunk_arg(1234567890123456789, 0, 0) == 0x7DE98115
    assert chunk_arg(1234567890123456789, 1, 0) == 0x03CA9CA7
    assert chunk_arg(1234567890123456789, 0, 1) == 0x4CD748AD
    print("[selftest] chunk arg formulas vs Java ground truth: OK")
    for seed in (1755820000000, (-42) & 0xFFFFFFFFFFFFFFFF, 12345):
        signed = seed - (1 << 64) if seed >> 63 else seed
        print(f"\n[selftest] target seed = {signed}")
        chunks = []
        for cx, cz in ((0, 0), (1, 0), (0, 1), (1, 1)):
            obs = generate_bedrock(seed, cx, cz)
            chunks.append({"cx": cx, "cz": cz, "obs": obs, "known": 768})
        if run_crack(chunks) != 0:
            print("[selftest] FAILED")
            return 1
    print("\n[selftest] ALL PASSED")
    return 0


def main():
    ap = argparse.ArgumentParser(description="Nukkit/PM1E world-seed cracker (bedrock backtracking)")
    ap.add_argument("samples", nargs="?", help="sample file (see header for the format)")
    ap.add_argument("--generate", type=int, metavar="SEED", help="emit a sample file for SEED")
    ap.add_argument("--selftest", action="store_true")
    a = ap.parse_args()
    if a.selftest:
        return selftest()
    if a.generate is not None:
        print(emit_samples(a.generate & 0xFFFFFFFFFFFFFFFF))
        return 0
    if not a.samples:
        ap.print_help()
        return 2
    chunks = parse_samples(a.samples)
    print(f"chunks={len(chunks)}")
    if not any(c["cx"] for c in chunks) or not any(c["cz"] for c in chunks):
        print("NOTE: samples do not vary on both axes -> only the low 32 bits are recoverable.")
    return run_crack(chunks)


if __name__ == "__main__":
    sys.exit(main())
