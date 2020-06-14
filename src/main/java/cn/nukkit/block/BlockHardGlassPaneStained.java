package cn.nukkit.block;

import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;

/**
 * Created by PetteriM1
 */
public class BlockHardGlassPaneStained extends BlockHardGlassPane {

    private int meta;

    public BlockHardGlassPaneStained() {
        this(0);
    }

    public BlockHardGlassPaneStained(int meta) {
        this.meta = meta;
    }

    @Override
    public int getFullId() {
        return (3056) + meta;
    }

    @Override
    public int getId() {
        return HARD_STAINED_GLASS_PANE;
    }

    @Override
    public String getName() {
        return getDyeColor().getName() + " Hardened Stained Glass Pane";
    }

    @Override
    public BlockColor getColor() {
        return getDyeColor().getColor();
    }

    public DyeColor getDyeColor() {
        return DyeColor.getByWoolData(meta);
    }

    @Override
    public final int getDamage() {
        return this.meta;
    }

    @Override
    public final void setDamage(int meta) {
        this.meta = meta;
    }
}
