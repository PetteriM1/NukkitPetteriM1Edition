package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockPitcherPlant extends BlockFlowable {

    public BlockPitcherPlant() {
        this(0);
    }

    public BlockPitcherPlant(int meta) {
        super(meta);
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.FLOWER;
    }

    @Override
    public int getAlternateMeta(int protocol) {
        return protocol >= ProtocolInfo.v1_9_0 ? BlockFlower.TYPE_CORNFLOWER : BlockFlower.TYPE_BLUE_ORCHID;
    }

    @Override
    public int getId() {
        return PITCHER_PLANT;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_20_0_23;
    }

    @Override
    public String getName() {
        return "Pitcher Plant";
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (this.down().getId() == Item.AIR) {
                this.getLevel().useBreakOn(this);

                return Level.BLOCK_UPDATE_NORMAL;
            }
        }

        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        Block down = this.down();
        int id = down.getId();
        if (id == Block.GRASS || id == Block.DIRT || id == Block.FARMLAND || id == Block.PODZOL || id == MYCELIUM || id == MOSS_BLOCK || id == MUD || id == MUDDY_MANGROVE_ROOTS) {
            this.getLevel().setBlock(this, this, true, true);
            return true;
        }
        return false;
    }
}
