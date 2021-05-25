package cn.nukkit.block;

@Deprecated //compatible plugins
public class BlockOreGoldNether extends BlockSolid {

    @Override
    public int getId() {
        return NETHER_GOLD_ORE;
    }

    @Override
    public String getName() {
        return "Nether Gold Ore";
    }

}
