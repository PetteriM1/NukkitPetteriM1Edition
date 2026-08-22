# 実 Java クラスによる検証ハーネス

リポジトリ内の**無改変**の実ソース

* `cn/nukkit/math/NukkitRandom.java`
* `cn/nukkit/level/generator/populator/type/Populator.java`
* `cn/nukkit/level/generator/populator/impl/PopulatorBedrock.java`

をそのままコピーし、依存だけを最小スタブ (`BlockID`, `ChunkManager`, `FullChunk`) で埋めて
コンパイルできるようにしたもの。`Normal.generateChunk()` のシード導出式を再現し、
実際の `PopulatorBedrock` に岩盤を生成させて y=2,3,4 の 768bit を出力する。

```bash
javac $(find . -name '*.java')
java Verify <seed> <chunkX> <chunkZ>
```

C++/Python 実装との突き合わせ:

```bash
java Verify 12345 0 1
python3 ../nukkit_seedcrack.py --generate 12345   # 同じ値が出る
```

スタブは `setBlockId` / `getHighestBlockAt` のシグネチャを満たすだけで、
`PopulatorBedrock` のロジックには一切手を入れていない。
