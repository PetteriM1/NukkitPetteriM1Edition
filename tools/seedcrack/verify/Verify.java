import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.impl.PopulatorBedrock;
import cn.nukkit.math.NukkitRandom;
import java.util.SplittableRandom;

/** Runs the REAL PopulatorBedrock with the REAL NukkitRandom and dumps y=2,3,4. */
public class Verify {
    static class Rec implements FullChunk {
        boolean[][] b = new boolean[256][5];
        public void setBlockId(int x, int y, int z, int id) { if (y < 5) b[x*16+z][y] = true; }
        public int getHighestBlockAt(int x, int z) { return 0; }
    }
    public static void main(String[] a) {
        long seed = Long.parseLong(a[0]);
        int cx = Integer.parseInt(a[1]), cz = Integer.parseInt(a[2]);
        SplittableRandom sr = new SplittableRandom(seed);
        long l1 = sr.nextLong(), l2 = sr.nextLong();
        NukkitRandom r = new NukkitRandom(0);
        r.setSeed(cx * l1 ^ cz * l2 ^ seed);          // Normal.generateChunk() verbatim
        Rec c = new Rec();
        new PopulatorBedrock().populate(null, cx, cz, r, c);   // REAL populator
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 256; i++) for (int y = 2; y <= 4; y++) sb.append(c.b[i][y] ? '1' : '0');
        System.out.println(sb);
        // sanity: y=0 and y=1 must always be bedrock
        boolean all01 = true;
        for (int i = 0; i < 256; i++) if (!c.b[i][0] || !c.b[i][1]) all01 = false;
        System.out.println("y0_y1_always_bedrock=" + all01);
    }
}
