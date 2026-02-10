package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.properties.OxidizationLevel;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockCopperDoor extends BlockDoor implements Oxidizable, Waxable {

    public BlockCopperDoor() {
        this(0);
    }

    public BlockCopperDoor(int meta) {
        super(meta);
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.IRON_DOOR_BLOCK;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.ORANGE_BLOCK_COLOR;
    }

    protected int getCopperId(boolean waxed, OxidizationLevel oxidizationLevel) {
        if (oxidizationLevel == null) {
            return this.getId();
        }
        switch (oxidizationLevel) {
            case UNAFFECTED:
                return waxed ? WAXED_COPPER_DOOR_BLOCK : COPPER_DOOR_BLOCK;
            case EXPOSED:
                return waxed ? WAXED_EXPOSED_COPPER_DOOR_BLOCK : EXPOSED_COPPER_DOOR_BLOCK;
            case WEATHERED:
                return waxed ? WAXED_WEATHERED_COPPER_DOOR_BLOCK : WEATHERED_COPPER_DOOR_BLOCK;
            case OXIDIZED:
                return waxed ? WAXED_OXIDIZED_COPPER_DOOR_BLOCK : OXIDIZED_COPPER_DOOR_BLOCK;
            default:
                return this.getId();
        }
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{
                    toItem()
            };
        } else {
            return new Item[0];
        }
    }

    @Override
    public double getHardness() {
        return 3;
    }

    @Override
    public int getId() {
        return COPPER_DOOR_BLOCK;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_21_0;
    }

    @Override
    public String getName() {
        return "Copper Door";
    }

    @Override
    public OxidizationLevel getOxidizationLevel() {
        return OxidizationLevel.UNAFFECTED;
    }

    @Override
    public double getResistance() {
        return 30;
    }

    @Override
    public Block getStateWithOxidizationLevel(OxidizationLevel oxidizationLevel) {
        return Block.get(this.getCopperId(this.isWaxed(), oxidizationLevel), this.getDamage());
    }

    @Override
    public int getToolTier() {
        return ItemTool.TIER_STONE;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public boolean isWaxed() {
        return false;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        return Waxable.super.onActivate(item, player)
                || Oxidizable.super.onActivate(item, player) || super.onActivate(item, player);
    }

    @Override
    public int onUpdate(int type) {
        return Oxidizable.super.onUpdate(type) == 0 ? super.onUpdate(type) : 0;
    }

    @Override
    public boolean setOxidizationLevel(OxidizationLevel oxidizationLevel) {
        if (this.getOxidizationLevel().equals(oxidizationLevel)) {
            return true;
        }
        return this.level.setBlock(this, Block.get(this.getCopperId(this.isWaxed(), oxidizationLevel)));
    }

    @Override
    public boolean setWaxed(boolean waxed) {
        if (this.isWaxed() == waxed) {
            return true;
        }
        return this.level.setBlock(this, Block.get(getCopperId(waxed, getOxidizationLevel())));
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(this.getId()), 0, 1);
    }
}
