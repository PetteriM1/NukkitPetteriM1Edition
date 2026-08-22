// =============================================================================
//  nukkit_seedcrack.cpp  --  Nukkit / PM1E ワールドシード逆算ツール
// =============================================================================
//
//  対象: cn.nukkit.level.generator.Normal + cn.nukkit.math.NukkitRandom
//        (PopulatorBedrock が生成する岩盤パターンから 64bit シードを復元する)
//
// -----------------------------------------------------------------------------
//  ■ ゲーム内でのサンプル採取手順
// -----------------------------------------------------------------------------
//
//  【採取対象】岩盤 (BEDROCK) の Y=2 / Y=3 / Y=4 の分布。
//
//   このジェネレータは Y=0 と Y=1 を必ず岩盤で埋め、Y=2,3,4 をそれぞれ
//   確率 1/2, 1/3, 1/4 で岩盤にする。この 3 層だけが乱数依存＝情報源である。
//   (Y=0,Y=1 は常に岩盤なので情報量ゼロ。掘っても意味は無い)
//
//   ※ 重要: このジェネレータはワールド高さが -64..319 の環境でも、岩盤を
//     ワールド座標 Y=0..4 に置く (DimensionData.getSectionOffset() の仕様)。
//     Y=-1 以下は空洞(空気)になる。したがって「Y=0〜4」を見ること。
//
//  【手順】
//   1. 対象チャンクへ行く。推奨は チャンク(0,0) / (1,0) / (0,1) の 3 つ
//      (= ワールド座標 X,Z = 0..15 / 16..31,0..15 / 0..15,16..31)。
//      ワールド座標を 16 で割った商がチャンク座標。負数は切り捨てではなく
//      算術シフト (例: X=-3 -> チャンク -1)。
//   2. スペクテイター/クリエイティブで Y=2 に降り、16x16 の各マスについて
//      「岩盤かどうか」を記録する。続けて Y=3, Y=4 も記録する。
//   3. 記録は下記フォーマットのテキストファイルにする。
//      行 = X (0..15 の順)、各行の文字 = Z (0..15 の順) であることに注意。
//      これは PopulatorBedrock のループ順 (x 外側, z 内側) と一致する。
//   4. 溶岩や空洞で岩盤が削られている柱は必ず '?' にする。
//      (洞窟生成 PopulatorCaves は Y>=2 の岩盤を溶岩に置換しうる。
//       Y=0,Y=1 は絶対に削られないので、そこが欠けていたら人為的改変を疑う)
//
//  【必要サンプル数】
//   ・岩盤 1 柱 = 情報量 約 2.73 bit (H(1/2)+H(1/3)+H(1/4))。
//   ・PRNG の実効状態は 32bit しかないので、理論上は 12 柱で一意に定まる。
//   ・安全マージン込みで 1 チャンクあたり 32 柱 (= X=0 と X=1 の 2 行) を推奨。
//     この 2 行だけあれば十分で、16x16 全部を埋める必要は無い。
//   ・下位 32bit のみで良い場合  : チャンク 1 個 (32 柱)
//   ・完全な 64bit シードが必要  : チャンク 3 個 ((0,0),(1,0),(0,1) 各 32 柱)
//     ※ 上位 32bit は generateChunk() の chunkX/chunkZ 項にしか効かないため、
//        チャンク(0,0) だけでは原理的に復元不能。必ず X 方向と Z 方向に
//        ずれたチャンクを 1 つずつ含めること。
//
//  【入力ファイル形式】
//      chunk 0 0
//      y2
//      1010110100101101      <- X=0 の行 (Z=0..15)
//      0110...               <- X=1 の行   (16 行未満なら残りは不明扱い)
//      y3
//      ...
//      y4
//      ...
//      chunk 1 0
//      ...
//   文字: '1' '#' 'B' = 岩盤 / '0' '.' '-' = 岩盤でない / '?' = 不明
//
// -----------------------------------------------------------------------------
//  ■ ビルド / 実行
// -----------------------------------------------------------------------------
//      g++ -O3 -march=native -pthread -o nukkit_seedcrack nukkit_seedcrack.cpp
//      ./nukkit_seedcrack samples.txt [--threads N]
//      ./nukkit_seedcrack --selftest            # 既知シードで往復検証
//
// -----------------------------------------------------------------------------
//  ■ 数式 (ソース抽出結果)
// -----------------------------------------------------------------------------
//   NukkitRandom.setSeed(long s):
//        state = CRC32( big-endian 4 bytes of (int)s )      <- 上位32bitは破棄
//   NukkitRandom.nextSignedInt():
//        t     = ((int32)(state*65535 + 31337) >> 8) + 1337  (算術シフト)
//        state ^= t ;  return t
//   nextInt()          = t & 0x7fffffff
//   nextBoundedInt(b)  = b==0 ? 0 : nextInt() % b
//
//   Normal.init():
//        SplittableRandom r(levelSeed)
//        localSeed1 = r.nextLong() = mix64(levelSeed + 1*GOLDEN_GAMMA)
//        localSeed2 = r.nextLong() = mix64(levelSeed + 2*GOLDEN_GAMMA)
//   Normal.generateChunk(cx,cz):
//        setSeed( cx*localSeed1 ^ cz*localSeed2 ^ levelSeed )   <- 岩盤はここ
//   Normal.populateChunk(cx,cz):
//        setSeed( 0xdeadbeef ^ (cx<<8) ^ cz ^ levelSeed )       <- 鉱石はここ
//
//   下位32bitだけを見ると (64bit 積の下位32bitは各下位32bitのみに依存):
//        arg32(cx,cz) = (cx * L1) ^ (cz * L2) ^ S      (mod 2^32, L1,L2,S は下位32bit)
//   よって
//        arg32(0,0) = S
//        arg32(1,0) = L1 ^ S      ->  L1 = arg32(1,0) ^ arg32(0,0)
//        arg32(0,1) = L2 ^ S      ->  L2 = arg32(0,1) ^ arg32(0,0)
// =============================================================================

#include <cstdio>
#include <cstdint>
#include <cstring>
#include <cstdlib>
#include <string>
#include <vector>
#include <thread>
#include <mutex>
#include <atomic>
#include <algorithm>
#include <fstream>
#include <sstream>

static_assert((-256 >> 8) == -1, "arithmetic right shift required");

// ---------------------------------------------------------------- CRC32 (zlib)
static uint32_t CRC_TAB[256];
static void crc_init() {
    for (uint32_t i = 0; i < 256; i++) {
        uint32_t c = i;
        for (int k = 0; k < 8; k++) c = (c & 1) ? (0xEDB88320u ^ (c >> 1)) : (c >> 1);
        CRC_TAB[i] = c;
    }
}
// java: ByteBuffer.allocate(4).order(BIG_ENDIAN).putInt(v) -> CRC32.update
static inline uint32_t crc32_be4(uint32_t v) {
    uint32_t c = 0xFFFFFFFFu;
    c = CRC_TAB[(c ^ (uint8_t)(v >> 24)) & 0xFF] ^ (c >> 8);
    c = CRC_TAB[(c ^ (uint8_t)(v >> 16)) & 0xFF] ^ (c >> 8);
    c = CRC_TAB[(c ^ (uint8_t)(v >>  8)) & 0xFF] ^ (c >> 8);
    c = CRC_TAB[(c ^ (uint8_t)(v      )) & 0xFF] ^ (c >> 8);
    return c ^ 0xFFFFFFFFu;
}

// CRC32 over a fixed 4-byte message is an affine bijection over GF(2)^32:
//   crc(x) = A(x) ^ c0.   Invert with an XOR basis.
static uint32_t CRC0;
static uint32_t BAS_V[32], BAS_T[32];
static void crc_inv_init() {
    CRC0 = crc32_be4(0);
    for (int i = 0; i < 32; i++) {
        uint32_t v = crc32_be4(1u << i) ^ CRC0;   // A(e_i)
        uint32_t t = 1u << i;
        for (int b = 31; b >= 0; b--) {
            if (!((v >> b) & 1)) continue;
            if (!BAS_V[b]) { BAS_V[b] = v; BAS_T[b] = t; v = 0; break; }
            v ^= BAS_V[b]; t ^= BAS_T[b];
        }
    }
}
// returns x with crc32_be4(x) == target
static uint32_t crc32_be4_inverse(uint32_t target) {
    uint32_t y = target ^ CRC0, x = 0;
    for (int b = 31; b >= 0; b--) {
        if (!((y >> b) & 1)) continue;
        y ^= BAS_V[b]; x ^= BAS_T[b];
    }
    return y ? 0xDEADDEADu : x;   // y!=0 would mean non-bijective (cannot happen)
}

// ---------------------------------------------------------- NukkitRandom (32b)
struct NRand {
    uint32_t s;
    explicit NRand(uint32_t st) : s(st) {}
    inline int32_t nextSignedInt() {
        int32_t t = (int32_t)((int32_t)(s * 65535u + 31337u) >> 8) + 1337;
        s ^= (uint32_t)t;
        return t;
    }
    inline int32_t nextInt() { return nextSignedInt() & 0x7fffffff; }
    inline int32_t nextBoundedInt(int32_t b) { return b == 0 ? 0 : nextInt() % b; }
};

// ------------------------------------------------------------ SplittableRandom
static const uint64_t GOLDEN_GAMMA = 0x9e3779b97f4a7c15ULL;
static inline uint64_t mix64(uint64_t z) {
    z = (z ^ (z >> 30)) * 0xbf58476d1ce4e5b9ULL;
    z = (z ^ (z >> 27)) * 0x94d049bb133111ebULL;
    return z ^ (z >> 31);
}
static inline void split_two(uint64_t seed, uint64_t &l1, uint64_t &l2) {
    l1 = mix64(seed + GOLDEN_GAMMA);
    l2 = mix64(seed + 2 * GOLDEN_GAMMA);
}

// -------------------------------------------------------------- sample storage
// per column (x*16+z): three tri-state observations for y=2,3,4  (-1 unknown)
struct ChunkSample {
    int cx = 0, cz = 0;
    int8_t obs[256][3];
    int known = 0;
    ChunkSample() { memset(obs, -1, sizeof(obs)); }
    int lastConstraintIdx() const {          // last PRNG call index we must reach
        int last = -1;
        for (int c = 0; c < 256; c++)
            for (int i = 0; i < 3; i++)
                if (obs[c][i] >= 0) last = c * 4 + (i + 1);
        return last;
    }
};

// replay PopulatorBedrock from state `st`, compare with sample; true = match
static inline bool matches(uint32_t st, const ChunkSample &smp, int lastIdx) {
    NRand r(st);
    for (int c = 0; c <= lastIdx / 4; c++) {
        for (int i = 1; i < 5; i++) {
            int32_t v = r.nextBoundedInt(i);
            if (i == 1) continue;                       // bound 1 -> always 0
            int8_t want = smp.obs[c][i - 2];
            if (want < 0) continue;
            if ((v == 0) != (want != 0)) return false;
        }
    }
    return true;
}

// -------------------------------------------------------------------- parsing
static int charToObs(char ch) {
    switch (ch) {
        case '1': case '#': case 'B': case 'b': return 1;
        case '0': case '.': case '-': return 0;
        default: return -1;
    }
}
static bool parseSamples(const std::string &path, std::vector<ChunkSample> &out) {
    std::ifstream f(path);
    if (!f) { fprintf(stderr, "cannot open %s\n", path.c_str()); return false; }
    std::string line; ChunkSample cur; bool have = false; int layer = -1, row = 0;
    auto flush = [&]() { if (have) out.push_back(cur); };
    while (std::getline(f, line)) {
        while (!line.empty() && (line.back() == '\r' || line.back() == ' ')) line.pop_back();
        if (line.empty() || line[0] == '#' || line[0] == ';') continue;
        std::istringstream is(line); std::string tok; is >> tok;
        if (tok == "chunk") {
            flush(); cur = ChunkSample(); have = true; layer = -1; row = 0;
            if (!(is >> cur.cx >> cur.cz)) { fprintf(stderr, "bad chunk line: %s\n", line.c_str()); return false; }
        } else if (tok == "y2" || tok == "y3" || tok == "y4") {
            layer = tok[1] - '2'; row = 0;
        } else {
            if (!have || layer < 0) { fprintf(stderr, "data before chunk/y header: %s\n", line.c_str()); return false; }
            if (row >= 16) { fprintf(stderr, "too many rows in layer\n"); return false; }
            for (int z = 0; z < 16 && z < (int)line.size(); z++) {
                int o = charToObs(line[z]);
                if (o >= 0) { cur.obs[row * 16 + z][layer] = (int8_t)o; cur.known++; }
            }
            row++;
        }
    }
    flush();
    return !out.empty();
}

// --------------------------------------------------------------- phase A crack
static std::vector<uint32_t> crackStates(const ChunkSample &smp, int nthreads) {
    int lastIdx = smp.lastConstraintIdx();
    if (lastIdx < 0) { fprintf(stderr, "chunk(%d,%d): no usable observations\n", smp.cx, smp.cz); return {}; }
    std::vector<uint32_t> found;
    std::mutex mu;
    std::atomic<uint32_t> progress{0};
    auto worker = [&](uint32_t lo, uint64_t hi) {
        std::vector<uint32_t> local;
        for (uint64_t s = lo; s < hi; s++)
            if (matches((uint32_t)s, smp, lastIdx)) local.push_back((uint32_t)s);
        if (!local.empty()) { std::lock_guard<std::mutex> g(mu); found.insert(found.end(), local.begin(), local.end()); }
        progress++;
    };
    std::vector<std::thread> th;
    uint64_t total = 1ULL << 32, span = total / nthreads;
    for (int i = 0; i < nthreads; i++) {
        uint64_t lo = (uint64_t)i * span, hi = (i == nthreads - 1) ? total : lo + span;
        th.emplace_back(worker, (uint32_t)lo, hi);
    }
    for (auto &t : th) t.join();
    std::sort(found.begin(), found.end());
    return found;
}

// ------------------------------------------------- solve (S,L1,L2) bit by bit
// arg32(cx,cz) = (cx*L1) ^ (cz*L2) ^ S   over Z/2^32 ; all ops bit-triangular.
struct Triple { uint32_t S, L1, L2; };
static std::vector<Triple> solveTriples(const std::vector<ChunkSample> &smps,
                                        const std::vector<uint32_t> &args) {
    std::vector<Triple> cand{{0, 0, 0}};
    for (int bit = 0; bit < 32 && !cand.empty(); bit++) {
        std::vector<Triple> next;
        for (const Triple &t : cand) {
            for (int m = 0; m < 8; m++) {
                Triple n = t;
                if (m & 1) n.S  |= 1u << bit;
                if (m & 2) n.L1 |= 1u << bit;
                if (m & 4) n.L2 |= 1u << bit;
                bool ok = true;
                for (size_t k = 0; k < smps.size(); k++) {
                    uint32_t lhs = ((uint32_t)smps[k].cx * n.L1) ^ ((uint32_t)smps[k].cz * n.L2) ^ n.S;
                    if (((lhs >> bit) & 1) != ((args[k] >> bit) & 1)) { ok = false; break; }
                }
                if (ok) next.push_back(n);
            }
        }
        cand.swap(next);
        if (cand.size() > 200000) { fprintf(stderr, "under-constrained: add a chunk that differs on both axes\n"); return {}; }
    }
    return cand;
}

// ------------------------------------------------ forward / helper generators
// Regenerate a chunk's bedrock straight from a candidate world seed (ground truth).
static void buildSample(uint64_t levelSeed, int cx, int cz, int rows, ChunkSample &smp) {
    uint64_t l1, l2; split_two(levelSeed, l1, l2);
    uint64_t arg = (uint64_t)((int64_t)cx * (int64_t)l1)
                 ^ (uint64_t)((int64_t)cz * (int64_t)l2) ^ levelSeed;
    NRand r(crc32_be4((uint32_t)arg));
    smp = ChunkSample(); smp.cx = cx; smp.cz = cz;
    for (int x = 0; x < 16; x++)
        for (int z = 0; z < 16; z++)
            for (int i = 1; i < 5; i++) {
                bool placed = r.nextBoundedInt(i) == 0;
                if (i >= 2 && x < rows) { smp.obs[x * 16 + z][i - 2] = placed; smp.known++; }
            }
}

// --------------------------------------------------------------- phase B (hi32)
// Recover the upper 32 bits by inverting SplittableRandom(levelSeed).
// Constraint is 64 bits wide (L1 and L2), so at most one high half survives.
static bool findHighMulti(const std::vector<Triple> &trs, int nthreads,
                          uint64_t &outSeed, Triple &outT) {
    auto test = [&](uint32_t h, const Triple &t) {
        uint64_t seed = ((uint64_t)h << 32) | t.S;
        if ((uint32_t)mix64(seed + GOLDEN_GAMMA) != t.L1) return false;
        return (uint32_t)mix64(seed + 2 * GOLDEN_GAMMA) == t.L2;
    };
    // tier 1: the cases real servers actually produce.
    //   h=0          -> positive int seed / small decimal seed
    //   h=0xFFFFFFFF -> negative int seed (String.hashCode() sign extension)
    //   h=0x100..400 -> System.currentTimeMillis() default (approx. 2008-2116)
    std::vector<uint32_t> quick{0u, 0xFFFFFFFFu};
    for (uint32_t h = 0x100; h <= 0x400; h++) quick.push_back(h);
    for (const Triple &t : trs)
        for (uint32_t h : quick)
            if (test(h, t)) { outSeed = ((uint64_t)h << 32) | t.S; outT = t; return true; }
    // tier 2: exhaustive 2^32 over the high half, all triples fused into one sweep.
    printf("    tier 1 miss -> exhaustive 2^32 sweep over the high half\n"); fflush(stdout);
    std::atomic<bool> hit{false};
    std::atomic<uint64_t> res{0};
    std::atomic<size_t> which{0};
    std::vector<std::thread> th;
    uint64_t total = 1ULL << 32, span = total / nthreads;
    for (int i = 0; i < nthreads; i++) {
        uint64_t lo = (uint64_t)i * span, hi = (i == nthreads - 1) ? total : lo + span;
        th.emplace_back([&, lo, hi]() {
            for (uint64_t h = lo; h < hi; h++) {
                if ((h & 0xFFFFF) == 0 && hit.load(std::memory_order_relaxed)) return;
                for (size_t k = 0; k < trs.size(); k++) {
                    uint64_t seed = ((uint64_t)h << 32) | trs[k].S;
                    if ((uint32_t)mix64(seed + GOLDEN_GAMMA) != trs[k].L1) continue;
                    if ((uint32_t)mix64(seed + 2 * GOLDEN_GAMMA) != trs[k].L2) continue;
                    res.store(seed); which.store(k); hit.store(true); return;
                }
            }
        });
    }
    for (auto &t : th) t.join();
    if (hit.load()) { outSeed = res.load(); outT = trs[which.load()]; return true; }
    return false;
}

// ------------------------------------------------------------------ pipeline
static int runCrack(std::vector<ChunkSample> &smps, int nthreads) {
    // ---- phase A: per-chunk candidate setSeed() arguments
    std::vector<std::vector<uint32_t>> cands;
    for (auto &smp : smps) {
        printf("  chunk(%d,%d): %d observations -> phase A sweep (2^32)...\n",
               smp.cx, smp.cz, smp.known);
        fflush(stdout);
        std::vector<uint32_t> states = crackStates(smp, nthreads);
        if (states.empty()) {
            fprintf(stderr, "    no PRNG state reproduces this pattern.\n"
                            "    -> a '1'/'0' is probably wrong, the rows are transposed\n"
                            "       (rows must be X, characters must be Z), or the chunk\n"
                            "       coordinate is off. Mark doubtful columns '?'.\n");
            return 1;
        }
        std::vector<uint32_t> as;
        for (uint32_t st : states) as.push_back(crc32_be4_inverse(st));
        std::sort(as.begin(), as.end());
        as.erase(std::unique(as.begin(), as.end()), as.end());
        printf("    %zu matching state(s) -> %zu candidate arg(s):", states.size(), as.size());
        for (size_t i = 0; i < as.size() && i < 8; i++) printf(" 0x%08x", as[i]);
        printf("%s\n", as.size() > 8 ? " ..." : "");
        cands.push_back(as);
    }
    // ---- combine: every combination of candidate args, filtered by the solver
    size_t combos = 1;
    for (auto &c : cands) combos *= c.size();
    if (combos > 500000) { fprintf(stderr, "  too many combinations (%zu); sample more columns\n", combos); return 1; }
    printf("  testing %zu arg combination(s) against arg32(cx,cz)=(cx*L1)^(cz*L2)^S\n", combos);
    std::vector<Triple> triples;
    std::vector<size_t> idx(cands.size(), 0);
    for (size_t n = 0; n < combos; n++) {
        std::vector<uint32_t> pick(cands.size());
        for (size_t k = 0; k < cands.size(); k++) pick[k] = cands[k][idx[k]];
        for (const Triple &t : solveTriples(smps, pick)) triples.push_back(t);
        for (size_t k = 0; k < cands.size(); k++) {           // odometer increment
            if (++idx[k] < cands[k].size()) break;
            idx[k] = 0;
        }
    }
    std::sort(triples.begin(), triples.end(), [](const Triple &a, const Triple &b) {
        return a.S != b.S ? a.S < b.S : (a.L1 != b.L1 ? a.L1 < b.L1 : a.L2 < b.L2); });
    triples.erase(std::unique(triples.begin(), triples.end(), [](const Triple &a, const Triple &b) {
        return a.S == b.S && a.L1 == b.L1 && a.L2 == b.L2; }), triples.end());
    if (triples.empty()) {
        fprintf(stderr, "  no consistent (S,L1,L2). Check the chunk coordinates.\n");
        return 1;
    }
    printf("  %zu consistent (S,L1,L2) candidate(s)\n", triples.size());
    {
        std::vector<uint32_t> lows;
        for (auto &t : triples) lows.push_back(t.S);
        std::sort(lows.begin(), lows.end());
        lows.erase(std::unique(lows.begin(), lows.end()), lows.end());
        printf("  low 32 bits of the world seed:");
        for (size_t i = 0; i < lows.size() && i < 8; i++)
            printf(" 0x%08x(%d)", lows[i], (int32_t)lows[i]);
        printf("%s\n", lows.size() > 8 ? " ..." : "");
    }
    // ---- phase B
    printf("  phase B: recovering the high 32 bits via SplittableRandom...\n"); fflush(stdout);
    uint64_t seed; Triple win;
    if (!findHighMulti(triples, nthreads, seed, win)) {
        fprintf(stderr, "  high 32 bits not found (inconsistent low-half candidates)\n");
        return 1;
    }
    // ---- final verification: regenerate every sampled chunk from the recovered seed
    bool ok = true;
    for (auto &smp : smps) {
        ChunkSample chk;
        buildSample(seed, smp.cx, smp.cz, 16, chk);
        for (int c = 0; c < 256 && ok; c++)
            for (int i = 0; i < 3; i++)
                if (smp.obs[c][i] >= 0 && smp.obs[c][i] != chk.obs[c][i]) { ok = false; break; }
    }
    printf("\n  ==> WORLD SEED = %lld   (0x%016llx)\n", (long long)seed, (unsigned long long)seed);
    printf("      low32=0x%08x  localSeed1_low32=0x%08x  localSeed2_low32=0x%08x\n",
           win.S, win.L1, win.L2);
    printf("      re-generated bedrock matches every sample: %s\n", ok ? "YES" : "NO");
    return ok ? 0 : 1;
}

// ------------------------------------------------------------------- selftest
static int selftest(int nthreads) {
    uint64_t l1, l2; split_two(1234567890123456789ULL, l1, l2);
    printf("[selftest] SplittableRandom vs JDK: L1=0x%016llx L2=0x%016llx %s\n",
           (unsigned long long)l1, (unsigned long long)l2,
           (l1 == 0x9904eee77e231db2ULL && l2 == 0x70ee7eb0313ec9b8ULL) ? "OK" : "MISMATCH");
    if (l1 != 0x9904eee77e231db2ULL || l2 != 0x70ee7eb0313ec9b8ULL) return 1;
    for (uint32_t t : {0u, 1u, 0x7de98115u, 0xdeadbeefu, 0xFFFFFFFFu})
        if (crc32_be4_inverse(crc32_be4(t)) != t) { printf("[selftest] CRC inverse FAILED %08x\n", t); return 1; }
    printf("[selftest] CRC32 4-byte inversion: OK\n");
    // chunk(0,0) arg must equal the low 32 bits of the seed
    uint64_t s0 = 1234567890123456789ULL;
    ChunkSample probe; buildSample(s0, 0, 0, 16, probe);
    printf("[selftest] arg32(0,0)==low32(seed): %s\n",
           ((uint32_t)s0 == 0x7de98115u) ? "OK" : "MISMATCH");

    const uint64_t seeds[] = {
        (uint64_t)(int64_t)1755820000000LL,   // System.currentTimeMillis() default
        (uint64_t)(int64_t)-42,               // negative int seed
        12345ULL,                             // small decimal seed
        (uint64_t)(int64_t)(int32_t)std::hash<std::string>{}("x"), // int-sized
        1234567890123456789ULL                // full 64-bit (exercises tier 2)
    };
    for (uint64_t s : seeds) {
        printf("\n[selftest] target seed = %lld\n", (long long)s);
        std::vector<ChunkSample> smps(4);
        buildSample(s, 0, 0, 16, smps[0]);
        buildSample(s, 1, 0, 16, smps[1]);
        buildSample(s, 0, 1, 16, smps[2]);
        buildSample(s, 1, 1, 16, smps[3]);
        if (runCrack(smps, nthreads) != 0) { printf("[selftest] FAILED\n"); return 1; }
    }
    printf("\n[selftest] ALL PASSED\n");
    return 0;
}

// ----------------------------------------------------------------------- main
int main(int argc, char **argv) {
    crc_init(); crc_inv_init();
    int nthreads = (int)std::thread::hardware_concurrency();
    if (nthreads <= 0) nthreads = 4;
    std::string path;
    for (int i = 1; i < argc; i++) {
        std::string a = argv[i];
        if (a == "--threads" && i + 1 < argc) nthreads = atoi(argv[++i]);
        else if (a == "--selftest") { printf("threads=%d\n", nthreads); return selftest(nthreads); }
        else path = a;
    }
    if (path.empty()) {
        fprintf(stderr,
            "Nukkit/PM1E world-seed cracker (bedrock backtracking)\n"
            "usage: %s samples.txt [--threads N]\n"
            "       %s --selftest\n"
            "See the header of this file for the in-game sampling procedure.\n", argv[0], argv[0]);
        return 2;
    }
    std::vector<ChunkSample> smps;
    if (!parseSamples(path, smps)) return 2;
    printf("threads=%d, chunks=%zu\n", nthreads, smps.size());
    bool hasX = false, hasZ = false;
    for (auto &s : smps) { if (s.cx) hasX = true; if (s.cz) hasZ = true; }
    if (!hasX || !hasZ)
        printf("NOTE: samples do not vary on both axes -> only the low 32 bits are recoverable.\n"
               "      Add a chunk with cx!=0 and one with cz!=0 for the full 64-bit seed.\n");
    return runCrack(smps, nthreads);
}
