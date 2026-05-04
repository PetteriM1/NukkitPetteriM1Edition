package cn.nukkit.block;

public class BlockStairsMossyStoneBrick extends BlockStairsStoneBrick {

    public BlockStairsMossyStoneBrick() {
        this(0);
    }

    public BlockStairsMossyStoneBrick(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return MOSSY_STONE_BRICK_STAIRS;
    }

    @Override
    public String getName() {
        return "Mossy Stone Brick Stairs";
    }
}
