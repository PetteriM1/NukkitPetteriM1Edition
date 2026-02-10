package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockAzaleaLeaves extends BlockLeaves {

    public BlockAzaleaLeaves() {
        this(0);
    }

    public BlockAzaleaLeaves(int meta) {
        super(meta);
    }

    @Override
    protected boolean canDropApple() {
        return false;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.LEAVES;
    }

    @Override
    public int getId() {
        return AZALEA_LEAVES;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public String getName() {
        return "Azalea Leaves";
    }

    @Override
    protected Item getSapling() {
        return new ItemBlock(Block.get(Block.AZALEA, 0));
    }

    @Override
    public boolean isCheckDecay() {
        return (this.getDamage() & 1) == 1;
    }

    @Override
    public boolean isPersistent() {
        return (this.getDamage() & 2) >>> 1 == 1;
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        this.setPersistent(true);
        return super.place(item, block, target, face, fx, fy, fz, player);
    }

    @Override
    public void setCheckDecay(boolean updateBit) {
        this.setDamage(this.getDamage() & ~1 | ((updateBit ? 1 : 0) & 1));
    }

    @Override
    public void setPersistent(boolean persistent) {
        int value = (persistent ? 1 : 0) << 1;
        this.setDamage(this.getDamage() & ~1 | (value & 2));
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(this.getId(), 0), 0);
    }
}
