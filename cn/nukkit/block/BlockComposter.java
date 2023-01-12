/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemDye;
import cn.nukkit.item.ItemID;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.Utils;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

public class BlockComposter
extends BlockTransparentMeta
implements ItemID {
    private static final Int2IntOpenHashMap d = new Int2IntOpenHashMap();

    public BlockComposter() {
        this(0);
    }

    public BlockComposter(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 468;
    }

    @Override
    public String getName() {
        return "Composter";
    }

    @Override
    public double getHardness() {
        return 0.6;
    }

    @Override
    public double getResistance() {
        return 0.6;
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride() {
        return this.getDamage();
    }

    public boolean incrementLevel() {
        int n = this.getDamage() + 1;
        this.setDamage(n);
        this.level.setBlock(this, this, true, true);
        return n == 8;
    }

    public boolean isFull() {
        return this.getDamage() == 8;
    }

    public boolean isEmpty() {
        return this.getDamage() == 0;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        boolean bl;
        if (item.getCount() <= 0 || item.getId() == 0) {
            return false;
        }
        if (this.isFull()) {
            this.setDamage(0);
            this.level.setBlock(this, this, true, true);
            this.level.dropItem(this.add(0.5, 0.85, 0.5), new ItemDye(DyeColor.WHITE));
            this.level.addSound((Vector3)this.add(0.5, 0.5, 0.5), Sound.BLOCK_COMPOSTER_EMPTY);
            return true;
        }
        int n = BlockComposter.getChance(item);
        if (n <= 0) {
            return false;
        }
        boolean bl2 = bl = Utils.random.nextInt(100) < n;
        if (player != null && !player.isCreative()) {
            item.setCount(item.getCount() - 1);
        }
        if (bl) {
            if (this.incrementLevel()) {
                this.level.addSound((Vector3)this.add(0.5, 0.5, 0.5), Sound.BLOCK_COMPOSTER_READY);
            } else {
                this.level.addSound((Vector3)this.add(0.5, 0.5, 0.5), Sound.BLOCK_COMPOSTER_FILL_SUCCESS);
            }
        } else {
            this.level.addSound((Vector3)this.add(0.5, 0.5, 0.5), Sound.BLOCK_COMPOSTER_FILL);
        }
        return true;
    }

    public Item empty() {
        return this.empty(null);
    }

    public Item empty(Player player) {
        if (this.isFull()) {
            this.setDamage(0);
            this.level.setBlock(this, this, true, true);
            this.level.addSound((Vector3)this.add(0.5, 0.5, 0.5), Sound.BLOCK_COMPOSTER_EMPTY);
            return new ItemDye(DyeColor.WHITE);
        }
        return null;
    }

    public static void registerItem(int n, int n2) {
        BlockComposter.registerItem(n, n2, 0);
    }

    public static void registerItem(int n, int n2, int n3) {
        d.put(n2 << 6 | n3 & 0x3F, n);
    }

    public static void registerItems(int n, int ... nArray) {
        for (int n2 : nArray) {
            BlockComposter.registerItem(n, n2, 0);
        }
    }

    public static void registerBlocks(int n, int ... nArray) {
        for (int n2 : nArray) {
            BlockComposter.registerBlock(n, n2, 0);
        }
    }

    public static void registerBlock(int n, int n2) {
        BlockComposter.registerBlock(n, n2, 0);
    }

    public static void registerBlock(int n, int n2, int n3) {
        if (n2 > 255) {
            n2 = 255 - n2;
        }
        BlockComposter.registerItem(n, n2, n3);
    }

    public static void register(int n, Item item) {
        BlockComposter.registerItem(n, item.getId(), item.getDamage());
    }

    public static int getChance(Item item) {
        int n = d.get(item.getId() << 6 | item.getDamage());
        if (n == 0) {
            n = d.get(item.getId() << 6);
        }
        return n;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    static {
        BlockComposter.registerItems(30, 335, 458, 464, 362, 361, 477, 295);
        BlockComposter.registerItems(50, 360, 338);
        BlockComposter.registerItems(65, 260, 457, 391, 127, 392, 296);
        BlockComposter.registerItems(85, 393, 297, 357);
        BlockComposter.registerItems(100, 354, 400);
        BlockComposter.registerBlocks(30, 393, 18, 161, 6, 385, 462);
        BlockComposter.registerBlocks(50, 2, 81, 394, 106);
        BlockComposter.registerBlocks(65, 37, 38, 175, 471, 111, 103, 86, 410, 411, 39, 40);
        BlockComposter.registerBlocks(85, 170, 99, 100, 282);
        BlockComposter.registerBlocks(100, 92);
        BlockComposter.registerBlock(50, 31, 0);
        BlockComposter.registerBlock(50, 31, 1);
        BlockComposter.registerBlock(65, 31, 2);
        BlockComposter.registerBlock(65, 31, 3);
    }
}

