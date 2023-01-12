/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.Chunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.IntTag;

public class BlockEntityLectern
extends BlockEntitySpawnable {
    private int b;

    public BlockEntityLectern(Chunk chunk, CompoundTag compoundTag) {
        super(chunk, compoundTag);
    }

    @Override
    protected void initBlockEntity() {
        if (!(this.namedTag.get("book") instanceof CompoundTag)) {
            this.namedTag.remove("book");
        }
        if (!(this.namedTag.get("page") instanceof IntTag)) {
            this.namedTag.remove("page");
        }
        this.a(false);
    }

    @Override
    public CompoundTag getSpawnCompound() {
        CompoundTag compoundTag = new CompoundTag().putString("id", "Lectern").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z).putBoolean("isMovable", this.movable);
        Item item = this.getBook();
        if (item.getId() != 0) {
            compoundTag.putCompound("book", NBTIO.putItemHelper(item));
            compoundTag.putBoolean("hasBook", true);
            compoundTag.putInt("page", this.getRawPage());
            compoundTag.putInt("totalPages", this.b);
        } else {
            compoundTag.putBoolean("hasBook", false);
        }
        return compoundTag;
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.level.getBlockIdAt(this.chunk, (int)this.x, (int)this.y, (int)this.z) == 449;
    }

    @Override
    public void onBreak() {
        Item item = this.getBook();
        if (item.getId() != 0) {
            this.level.dropItem(this, item);
        }
    }

    public boolean hasBook() {
        return this.namedTag.contains("book") && this.namedTag.get("book") instanceof CompoundTag;
    }

    public Item getBook() {
        if (!this.hasBook()) {
            return Item.get(0, 0, 0);
        }
        return NBTIO.getItemHelper(this.namedTag.getCompound("book"));
    }

    public void setBook(Item item) {
        if (item.getId() == 387 || item.getId() == 386) {
            this.namedTag.putCompound("book", NBTIO.putItemHelper(item));
        } else {
            this.namedTag.remove("book");
            this.namedTag.remove("page");
        }
        this.a(true);
    }

    public int getLeftPage() {
        return this.getRawPage() * 2 + 1;
    }

    public int getRightPage() {
        return this.getLeftPage() + 1;
    }

    public void setLeftPage(int n) {
        this.setRawPage((n - 1) / 2);
    }

    public void setRightPage(int n) {
        this.setLeftPage(n - 1);
    }

    public void setRawPage(int n) {
        this.namedTag.putInt("page", Math.min(n, this.b));
        this.getLevel().updateAround(this);
    }

    public int getRawPage() {
        return this.namedTag.getInt("page");
    }

    public int getTotalPages() {
        return this.b;
    }

    private void a(boolean bl) {
        Item item = this.getBook();
        this.b = item.getId() == 0 || !item.hasCompoundTag() ? 0 : item.getNamedTag().getList("pages", CompoundTag.class).size();
        if (bl) {
            this.getLevel().updateAroundRedstone(this, null);
        }
    }
}

