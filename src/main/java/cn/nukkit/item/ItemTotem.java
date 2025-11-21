package cn.nukkit.item;

public class ItemTotem extends Item {

    public ItemTotem(Integer meta) {
        this(meta, 1);
    }

    public ItemTotem(Integer meta, int count) {
        super(TOTEM, meta, count, "Totem of Undying");
    }

    @Override
    public boolean allowOffhand() {
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
