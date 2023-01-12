/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemDurable;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.ByteTag;
import cn.nukkit.nbt.tag.Tag;

public abstract class ItemArmor
extends Item
implements ItemDurable {
    public static final int TIER_LEATHER = 1;
    public static final int TIER_IRON = 2;
    public static final int TIER_CHAIN = 3;
    public static final int TIER_GOLD = 4;
    public static final int TIER_DIAMOND = 5;
    public static final int TIER_NETHERITE = 6;
    public static final int TIER_OTHER = 7;

    public ItemArmor(int n) {
        super(n);
    }

    public ItemArmor(int n, Integer n2) {
        super(n, n2);
    }

    public ItemArmor(int n, Integer n2, int n3) {
        super(n, n2, n3);
    }

    public ItemArmor(int n, Integer n2, int n3, String string) {
        super(n, n2, n3, string);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean isArmor() {
        return true;
    }

    @Override
    public boolean onClickAir(Player player, Vector3 vector3) {
        boolean bl = false;
        Item item = Item.get(0);
        if (this.isHelmet()) {
            item = player.getInventory().getHelmet();
            if (player.getInventory().setHelmet(this)) {
                bl = true;
            }
        } else if (this.isChestplate()) {
            item = player.getInventory().getChestplate();
            if (player.getInventory().setChestplate(this)) {
                bl = true;
            }
        } else if (this.isLeggings()) {
            item = player.getInventory().getLeggings();
            if (player.getInventory().setLeggings(this)) {
                bl = true;
            }
        } else if (this.isBoots()) {
            item = player.getInventory().getBoots();
            if (player.getInventory().setBoots(this)) {
                bl = true;
            }
        }
        if (bl) {
            player.getInventory().setItem(player.getInventory().getHeldItemIndex(), item);
            switch (this.getTier()) {
                case 3: {
                    player.getLevel().addLevelSoundEvent(player, 94);
                    break;
                }
                case 5: {
                    player.getLevel().addLevelSoundEvent(player, 95);
                    break;
                }
                case 4: {
                    player.getLevel().addLevelSoundEvent(player, 97);
                    break;
                }
                case 2: {
                    player.getLevel().addLevelSoundEvent(player, 98);
                    break;
                }
                case 1: {
                    player.getLevel().addLevelSoundEvent(player, 99);
                    break;
                }
                case 6: {
                    player.getLevel().addLevelSoundEvent(player, 317);
                    break;
                }
                default: {
                    player.getLevel().addLevelSoundEvent(player, 96);
                }
            }
        }
        return this.getCount() == 0;
    }

    @Override
    public int getEnchantAbility() {
        switch (this.getTier()) {
            case 3: {
                return 12;
            }
            case 1: {
                return 15;
            }
            case 5: {
                return 10;
            }
            case 4: {
                return 25;
            }
            case 2: {
                return 9;
            }
            case 6: {
                return 10;
            }
        }
        return 0;
    }

    @Override
    public boolean isUnbreakable() {
        Tag tag = this.getNamedTagEntry("Unbreakable");
        return tag instanceof ByteTag && ((ByteTag)tag).data > 0;
    }

    @Override
    public boolean canBePutInHelmetSlot() {
        return this.isHelmet();
    }
}

