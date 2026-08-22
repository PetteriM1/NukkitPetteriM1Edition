# Nukkit / PM1E ワールドシード逆算 (Seed Cracking) 解析レポート

対象リポジトリのソース (`cn/nukkit/level/generator/`, `cn/nukkit/math/NukkitRandom.java`) を
静的解析し、ゲーム内で観測できる地形データから 64bit ワールドシードを復元する手法と
ツールをまとめたもの。

**結論から言うと、このサーバのワールドシードは実用上ほぼ無防備で、
岩盤 (BEDROCK) 4 チャンク分の目視データと 4 コア CPU で 43 秒で完全復元できる。**

---

## 1. ワールド生成ロジックの特定

### 1-1. PRNG: `cn.nukkit.math.NukkitRandom`

```java
public void setSeed(long seeds) {
    CRC32 crc32 = new CRC32();
    ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
    buffer.putInt((int) seeds);            // ★ 上位 32bit を捨てている
    crc32.update(buffer.array());
    this.seed = crc32.getValue();
}
public int nextSignedInt() {
    int t = (((int) ((this.seed * 65535) + 31337) >> 8) + 1337);
    this.seed ^= t;
    return t;
}
public int nextInt()               { return this.nextSignedInt() & 0x7fffffff; }
public int nextBoundedInt(int b)   { return b == 0 ? 0 : this.nextInt() % b; }
```

抽出した数式:

| 項目 | 数式 |
|---|---|
| 状態初期化 | `state = CRC32( BE4((int)seed) )` |
| 状態遷移 | `t = ((int32)(state*65535 + 31337) >>a 8) + 1337` , `state ^= t` |
| 出力 | `nextInt() = t & 0x7fffffff` , `nextBoundedInt(b) = nextInt() % b` |

**致命的な弱点が 3 つある。**

1. **上位 32bit の破棄** — `(int) seeds` により、`setSeed()` を通る全ての生成物は
   ワールドシードの**下位 32bit にしか依存しない**。探索空間が 2^64 → 2^32 に落ちる。
2. **CRC32 が全単射** — 固定長 4 バイト入力に対する CRC32 は GF(2) 上のアフィン全単射
   (`crc(x) = A·x ^ c`, A は正則)。よって **逆算可能**。観測から PRNG 状態を割り出せば、
   `setSeed()` の引数がそのまま復元できる (総当たり不要)。
3. **実効状態が 32bit** — `seed` は long だが、`(int)` キャストで下位 32bit しか使われず、
   `seed ^= t` の上位ビットは次の出力に一切影響しない。状態空間は 2^32。

さらに実測すると PRNG の品質自体が壊れている:

```
ランダムな初期状態 2000 個のサイクル長分布 (実測)
  周期  2 :  1.4%      周期 4 : 35.2%      周期 8 : 14.6%
  → 約 51% の状態が「周期 8 以下」の閉路に落ちる
  平均テール長 = 3845 ステップ
```

つまり半数の初期状態は、数千回引いたあと **長さ 4 前後の同じ値を延々と繰り返す**。
(実際に 1 チャンク分の岩盤を出力させると、途中からパターンが完全にループする)

### 1-2. チャンクシードの導出: `Normal.java`

```java
// init()
SplittableRandom random1 = new SplittableRandom(this.level.getSeed());
this.localSeed1 = random1.nextLong();
this.localSeed2 = random1.nextLong();

// generateChunk()  ... 岩盤・地形はこちら
this.nukkitRandom.setSeed(chunkX * localSeed1 ^ chunkZ * localSeed2 ^ this.level.getSeed());

// populateChunk()  ... 鉱石・洞窟・植生はこちら
this.nukkitRandom.setSeed(0xdeadbeef ^ (chunkX << 8) ^ chunkZ ^ this.level.getSeed());
```

`SplittableRandom` は JDK 標準なので完全に再現できる (JDK21 で実測検証済み):

```
GOLDEN_GAMMA = 0x9e3779b97f4a7c15
mix64(z): z=(z^(z>>>30))*0xbf58476d1ce4e5b9; z=(z^(z>>>27))*0x94d049bb133111eb; return z^(z>>>31)
localSeed1 = mix64(levelSeed + 1*GOLDEN_GAMMA)
localSeed2 = mix64(levelSeed + 2*GOLDEN_GAMMA)
```

64bit 積の下位 32bit は各因子の下位 32bit のみで決まるので、`S=low32(levelSeed)`,
`L1=low32(localSeed1)`, `L2=low32(localSeed2)` と置くと **mod 2^32 で閉じた式**になる:

```
arg32(cx,cz) = (cx * L1) ^ (cz * L2) ^ S           ... generateChunk
arg32(0,0) = S            → チャンク(0,0)の引数はシード下位32bitそのもの
arg32(1,0) = L1 ^ S       → L1 = arg32(1,0) ^ arg32(0,0)
arg32(0,1) = L2 ^ S       → L2 = arg32(0,1) ^ arg32(0,0)

arg32_pop(cx,cz) = 0xdeadbeef ^ (cx<<8) ^ cz ^ S   ... populateChunk (上位ビット非依存)
```

Java 実装での実測値 (seed = 1234567890123456789 = `0x112210f47de98115`):

```
arg32(0,0) = 0x7de98115  = low32(seed)                 ✓
arg32(1,0) = 0x03ca9ca7  = 0x7e231db2 ^ 0x7de98115     ✓
arg32(0,1) = 0x4cd748ad  = 0x313ec9b8 ^ 0x7de98115     ✓
```

> **上位 32bit はどこに効くか**
> `populateChunk()` は上位ビットに全く依存しない。上位 32bit が影響するのは
> `generateChunk()` の `chunkX*localSeed1 ^ chunkZ*localSeed2` の項だけ。
> したがって **チャンク(0,0) だけを見ても上位 32bit は原理的に復元不能**で、
> X 方向・Z 方向にずれたチャンクが最低 1 つずつ必要になる。

### 1-3. ノイズ生成は乱数を消費しない (重要)

`NoiseGeneratorOctavesF` / `SimplexF` / `BiomeSelector` は **コンストラクタ内 (= `init()` 時) でのみ**
`NukkitRandom` を消費し、置換表 (`permutations[512]`) を作る。生成時の `generateNoiseOctaves()` /
`pickBiome()` は置換表を引くだけで乱数を進めない。

よって `generateChunk()` の中では

```
setSeed(...) → ノイズ評価(消費0) → バイオーム選択(消費0) → ブロック配置(消費0)
             → generationPopulators[0] = PopulatorBedrock   ★ここが最初の消費者
```

となり、**岩盤生成が `setSeed()` 直後の手つかずの乱数列を丸ごと受け取る**。
オフセット推定が一切不要という、逆算にとって理想的な条件。

---

## 2. バックトラック対象の選定

シード特定に使う特徴量を、汚染されにくさ (purity) で比較した。

| 候補 | 乱数列上の位置 | 純度 | 判定 |
|---|---|---|---|
| **岩盤 (PopulatorBedrock)** | `generateChunk` の**先頭**、オフセット 0 | 地形・バイオーム・設定に非依存。1 柱ごとに固定 4 回消費 | **◎ 採用** |
| 鉱石 (PopulatorOre) | `populateChunk` の先頭 | 石ブロックにしか置かれず、地形形状に依存。`legacy` フラグで鉱石表が変わる | △ |
| 洞窟 (PopulatorCaves) | 鉱石の後 | 消費回数が地形依存で可変。オフセット不定 | × |
| 地形ノイズ | `init()` 時 | 置換表経由で間接的。float 誤差、逆算が非線形で困難 | × |
| バイオーム分布 | `init()` 時 | 同上。解像度が粗い | × |
| 植生・構造物 | `populateChunk` 末尾 | プラグイン/成長/プレイヤー改変で汚染されやすい | × |

### なぜ岩盤が最適か

```java
for (int x = 0; x < 16; x++)
    for (int z = 0; z < 16; z++) {
        chunk.setBlockId(x, 0, z, BEDROCK);          // 無条件、乱数消費なし
        for (int i = 1; i < 5; i++)
            if (random.nextBoundedInt(i) == 0)       // i は「境界値」かつ「Y座標」
                chunk.setBlockId(x, i, z, BEDROCK);
    }
```

* **消費回数が完全固定** — 1 柱あたり必ず 4 回。地形・バイオーム・設定に一切左右されない。
  他の populator のように「条件を満たしたときだけ引く」構造ではないため、
  観測できない分岐によるオフセットずれが起きない。
* **列挙順が単純** — x 外側 / z 内側。乱数列と観測データが 1:1 で対応する。
* **プラグイン汚染に強い** — 岩盤層はサバイバルで破壊不能、装飾建築の対象にもならない。
* **Y=0, Y=1 は常に岩盤** — `nextBoundedInt(1)` は必ず 0 を返すため。
  情報量はゼロだが「**この 2 層が欠けていたら人為的改変**」という検証子になる。
  (実測で `y0_y1_always_bedrock=true` を確認済み)
* **情報量** — 1 柱で `H(1/2)+H(1/3)+H(1/4) = 2.73 bit`。32bit 状態には理論上 12 柱で足りる。

### 唯一の汚染源: 洞窟

`PopulatorCaves` は `yFrom` を 1 で下限クランプし、carve ループが `yy > yFrom` なので
**Y=2 以上の岩盤を溶岩に置換しうる** (`if (yy - 1 < 10) setBlock(LAVA)`)。
Y=0 と Y=1 は絶対に削られない。

→ 対策: 溶岩/空洞が見える柱は入力で `?` にする。ツールは欠測を許容する。
なお `PopulatorOre` は `getBlockIdAt(x,y,z) == replaceBlockId` (= STONE) のときしか
置換しないため、**鉱石が岩盤を壊すことはない** (ソース確認済み)。

---

## 3. 高速特定スクリプト

| ファイル | 内容 |
|---|---|
| `nukkit_seedcrack.cpp` | 本命。マルチスレッド C++。**4 チャンクを 43 秒** (4 コア) |
| `nukkit_seedcrack.py`  | 同アルゴリズムの NumPy ベクトル化 Python 版 (同条件 約 12 分) |
| `sample_example.txt`   | 入力ファイルの例 |

### アルゴリズム

```
Phase A : 各チャンクについて 2^32 の PRNG 状態を全探索し、岩盤パターンに一致する状態を列挙
          (早期棄却が効くので候補 1 個あたり平均 4〜6 ステップで落ちる)
          → CRC32 を GF(2) 上で逆行列化し、状態から setSeed() の引数 arg32 を復元
Phase B : arg32(cx,cz) = (cx*L1)^(cz*L2)^S をビット下位から DFS で解き (S,L1,L2) を確定
          → 上位 32bit を SplittableRandom の逆算で決定
             tier1: h=0 / h=0xFFFFFFFF / h=0x100..0x400 を即座に試す
             tier2: 外れたら 2^32 全探索
検証    : 求めたシードから岩盤を再生成し、全サンプルと一致するか確認
```

**Phase B の tier1 が実用上ほぼ必ず当たる理由** — `Server.java` のシード決定は

```java
String seedString = String.valueOf(this.getProperty("level-seed", System.currentTimeMillis()));
try { seed = Long.parseLong(seedString); }
catch (NumberFormatException e) { seed = seedString.hashCode(); }   // ★ 32bit
```

* 文字列シード → `String.hashCode()` は int なので上位は `0x00000000` か `0xFFFFFFFF` の 2 択
* 未設定 → `System.currentTimeMillis()` は上位 32bit が 0x100〜0x400 程度 (西暦2008〜2116)
* 数値シード → 人間が打つ値は大抵 int に収まる

### 短周期への対処

PRNG が短周期に落ちると、1 チャンクの観測だけでは状態が一意に決まらない
(実測で 1 チャンクあたり候補 1〜5 個)。そこで

1. Phase A では**一致する状態を全部**集める
2. 4 チャンク分の候補の直積を取り、`arg32` の関係式で整合しない組み合わせを Phase B の DFS で除去
3. 残った `(S,L1,L2)` を SplittableRandom の 64bit 制約でさらに絞る

実測: seed=12345 で候補 2×1×4×2 = 16 通り → **整合解はちょうど 1 個**に収束。

### 使い方

```bash
g++ -O3 -march=native -pthread -o nukkit_seedcrack nukkit_seedcrack.cpp
./nukkit_seedcrack sample_example.txt          # 実データから復元
./nukkit_seedcrack --selftest                  # 既知シード5種で往復検証

python3 nukkit_seedcrack.py --generate 12345 > s.txt   # テスト用サンプル生成
python3 nukkit_seedcrack.py s.txt
```

### ゲーム内サンプル採取手順

1. **採取対象は岩盤の Y=2 / Y=3 / Y=4 の 3 層だけ。**
   Y=0・Y=1 は常に岩盤なので掘る意味はない (整合性チェックにのみ使える)。
   > ワールド高さが -64..319 の環境でも、このジェネレータは
   > `DimensionData.getSectionOffset()` の仕様により岩盤を **ワールド座標 Y=0..4** に置く。
   > Y=-1 以下は空洞になる。「Y=0〜4 を見る」で正しい。
2. **チャンク (0,0) / (1,0) / (0,1) / (1,1) の 4 つ**へ行く
   (ワールド座標 X,Z = 0..15 / 16..31 / 0..15,16..31 / 16..31,16..31)。
   X 方向と Z 方向にずれたチャンクを必ず含めること (上位 32bit の復元に必須)。
3. スペクテイター等で Y=2 に降り、16×16 の各マスが岩盤かを記録。Y=3, Y=4 も同様。
4. **行 = X (0..15)、行内の文字 = Z (0..15)** の順で書く。
   `PopulatorBedrock` のループ順 (x 外側 / z 内側) と一致させるため、ここを間違えると解けない。
5. 溶岩や空洞で削れている柱は `?` にする。

**必要サンプル数**

| 目的 | 必要量 |
|---|---|
| 理論下限 | 12 柱 (2.73 bit × 12 ≒ 32bit) |
| 下位 32bit のみ | 任意の 1 チャンク (16×16 推奨、最低 32 柱) |
| **完全な 64bit** | **4 チャンク × 16×16 を推奨** (短周期対策の冗長性込み) |

最低限は 1 チャンクあたり X=0,1 の 2 行 (32 柱) でも動くが、短周期に落ちた場合に
候補が絞りきれないことがあるため、16×16 全部を埋めるのが安全。

### 入力フォーマット

```
chunk 0 0
y2
0010110111000001      <- X=0 の行 (Z=0..15)
1011101100011110      <- X=1 の行
...
y3
...
y4
...
chunk 1 0
...
```
文字: `1` `#` `B` = 岩盤 / `0` `.` `-` = 岩盤でない / `?` = 不明

---

## 4. 検証結果

**リポジトリ内の実際のクラスを無改変でコンパイルし**、C++/Python 実装と突き合わせた。

* 使用した実ソース: `cn/nukkit/math/NukkitRandom.java`,
  `cn/nukkit/level/generator/populator/type/Populator.java`,
  `cn/nukkit/level/generator/populator/impl/PopulatorBedrock.java`
  (最小スタブで依存だけ埋め、ロジックは一切変更していない)

```
実 Java 出力 vs Python モデル (岩盤 y=2,3,4 の 768bit 完全一致)
  seed=1234567890123456789 chunk=(0,0)   MATCH
  seed=1234567890123456789 chunk=(1,0)   MATCH
  seed=12345               chunk=(0,1)   MATCH
  seed=-42                 chunk=(3,-7)  MATCH     ← 負のチャンク座標
  seed=1755820000000       chunk=(-12,25) MATCH
  y0_y1_always_bedrock = true
```

エンドツーエンド (4 チャンク × 16×16 から 64bit シードを復元):

| 対象シード | 由来の想定 | 結果 |
|---|---|---|
| `1755820000000` | `System.currentTimeMillis()` | ✅ 復元 (tier1) |
| `-42` | 負の数値シード | ✅ 復元 (tier1) |
| `12345` | 小さい数値シード | ✅ 復元 (tier1) |
| `-1249781873` | `String.hashCode()` | ✅ 復元 (tier1) |
| `1234567890123456789` | 完全な 64bit | ✅ 復元 (tier2 全探索) |

全ケースで整合解は 1 個に収束し、再生成した岩盤が全サンプルと一致した (`--selftest` で再現可能)。

---

## 5. 防御側への示唆

シードを秘匿したい場合、この解析が示す対策は以下:

1. **`NukkitRandom` を使う限りシードは 32bit しか守れない。** `setSeed()` の
   `(int) seeds` を `long` 全体を使う実装に変えない限り、上位ビットは意味を持たない。
2. **岩盤層をランダム化しない方が安全という逆説** — 岩盤が乱数依存であること自体が
   オフセット 0 の理想的な観測窓を与えている。フラット化すれば情報源が 1 つ消える。
3. 根本的には `java.util.Random` 相当以上の PRNG (最低でも 64bit 状態・長周期) への置換が必要。
   現状は周期 4 の閉路に半数の状態が落ちるため、生成物の統計的品質も低い。

> 本資料は自サーバの構成解析・セキュリティ評価を目的とした技術文書。
