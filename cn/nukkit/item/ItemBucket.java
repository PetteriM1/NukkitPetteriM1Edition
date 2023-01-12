/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockLava;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.player.PlayerBucketEmptyEvent;
import cn.nukkit.event.player.PlayerBucketFillEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.level.particle.ExplodeParticle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;

public class ItemBucket
extends Item {
    public ItemBucket() {
        this((Integer)0, 1);
    }

    public ItemBucket(Integer n) {
        this(n, 1);
    }

    public ItemBucket(Integer n, int n2) {
        super(325, n, n2, ItemBucket.getName(n));
    }

    protected static String getName(int n) {
        switch (n) {
            case 1: {
                return "Milk";
            }
            case 2: {
                return "Bucket of Cod";
            }
            case 3: {
                return "Bucket of Salmon";
            }
            case 4: {
                return "Bucket of Tropical Fish";
            }
            case 5: {
                return "Bucket of Pufferfish";
            }
            case 8: {
                return "Water Bucket";
            }
            case 10: {
                return "Lava Bucket";
            }
            case 12: {
                return "Bucket of Axolotl";
            }
            case 13: {
                return "Bucket of Tadpoles";
            }
        }
        return "Bucket";
    }

    public static int getDamageByTarget(int n) {
        switch (n) {
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 8: 
            case 9: 
            case 12: 
            case 13: {
                return 8;
            }
            case 10: 
            case 11: {
                return 10;
            }
        }
        return 0;
    }

    @Override
    public int getMaxStackSize() {
        return this.meta == 0 ? 16 : 1;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Level level, Player player, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4) {
        if (player.isAdventure()) {
            return false;
        }
        Block block3 = Block.get(ItemBucket.getDamageByTarget(this.meta));
        if (block3 instanceof BlockAir) {
            if (block2 instanceof BlockLiquid && block2.getDamage() == 0) {
                Item item = Item.get(325, ItemBucket.getDamageByTarget(block2.getId()), 1);
                PlayerBucketFillEvent playerBucketFillEvent = new PlayerBucketFillEvent(player, block, blockFace, this, item);
                player.getServer().getPluginManager().callEvent(playerBucketFillEvent);
                if (!playerBucketFillEvent.isCancelled()) {
                    player.getLevel().setBlock(block2, Block.get(0), true, true);
                    for (BlockFace blockFace2 : BlockFace.Plane.HORIZONTAL) {
                        Block block4 = block2.getSide(blockFace2);
                        if (block4.getId() != 9) continue;
                        level.setBlock(block4, Block.get(8));
                    }
                    if (player.isSurvival()) {
                        if (this.getCount() - 1 <= 0) {
                            player.getInventory().setItemInHand(playerBucketFillEvent.getItem());
                        } else {
                            Item item2 = this.clone();
                            item2.setCount(this.getCount() - 1);
                            player.getInventory().setItemInHand(item2);
                            if (player.getInventory().canAddItem(playerBucketFillEvent.getItem())) {
                                player.getInventory().addItem(playerBucketFillEvent.getItem());
                            } else {
                                player.dropItem(playerBucketFillEvent.getItem());
                            }
                        }
                    }
                    if (block2 instanceof BlockLava) {
                        level.addLevelSoundEvent(block, 91);
                    } else {
                        level.addLevelSoundEvent(block, 90);
                    }
                    return true;
                }
                player.getInventory().sendContents(player);
            }
        } else if (block3 instanceof BlockLiquid) {
            Item item = Item.get(325, 0, 1);
            PlayerBucketEmptyEvent playerBucketEmptyEvent = new PlayerBucketEmptyEvent(player, block, blockFace, this, item, true);
            if (!block.canBeFlowedInto()) {
                playerBucketEmptyEvent.setCancelled(true);
            }
            boolean bl = false;
            if (player.getLevel().getDimension() == 1 && this.getDamage() != 10) {
                playerBucketEmptyEvent.setCancelled(true);
                bl = true;
            }
            player.getServer().getPluginManager().callEvent(playerBucketEmptyEvent);
            if (!playerBucketEmptyEvent.isCancelled()) {
                Cloneable cloneable;
                player.getLevel().setBlock(block, block3, true, true);
                if (player.isSurvival()) {
                    if (this.getCount() - 1 <= 0) {
                        player.getInventory().setItemInHand(playerBucketEmptyEvent.getItem());
                    } else {
                        cloneable = this.clone();
                        ((Item)cloneable).setCount(this.getCount() - 1);
                        player.getInventory().setItemInHand((Item)cloneable);
                        if (player.getInventory().canAddItem(playerBucketEmptyEvent.getItem())) {
                            player.getInventory().addItem(playerBucketEmptyEvent.getItem());
                        } else {
                            player.dropItem(playerBucketEmptyEvent.getItem());
                        }
                    }
                }
                if (this.getDamage() == 10) {
                    level.addLevelSoundEvent(block, 93);
                } else {
                    level.addLevelSoundEvent(block, 92);
                }
                if (Server.getInstance().mobsFromBlocks && playerBucketEmptyEvent.isMobSpawningAllowed()) {
                    switch (this.getDamage()) {
                        case 2: {
                            cloneable = Entity.createEntity("Cod", (Position)block, new Object[0]);
                            if (cloneable == null) break;
                            ((Entity)cloneable).spawnToAll();
                            break;
                        }
                        case 3: {
                            Entity entity = Entity.createEntity("Salmon", (Position)block, new Object[0]);
                            if (entity == null) break;
                            entity.spawnToAll();
                            break;
                        }
                        case 4: {
                            Entity entity = Entity.createEntity("TropicalFish", (Position)block, new Object[0]);
                            if (entity == null) break;
                            entity.spawnToAll();
                            break;
                        }
                        case 5: {
                            Entity entity = Entity.createEntity("Pufferfish", (Position)block, new Object[0]);
                            if (entity == null) break;
                            entity.spawnToAll();
                            break;
                        }
                        case 12: {
                            Entity entity = Entity.createEntity("Axolotl", (Position)block, new Object[0]);
                            if (entity == null) break;
                            entity.spawnToAll();
                            break;
                        }
                        case 13: {
                            Entity entity = Entity.createEntity("Tadpole", (Position)block, new Object[0]);
                            if (entity == null) break;
                            entity.spawnToAll();
                        }
                    }
                }
                return true;
            }
            if (bl) {
                if (!player.isCreative()) {
                    this.setDamage(0);
                    player.getInventory().setItemInHand(this);
                }
                player.getLevel().addSound((Vector3)block2, Sound.RANDOM_FIZZ);
                player.getLevel().addParticle(new ExplodeParticle(block2.add(0.5, 1.0, 0.5)));
            } else {
                player.getInventory().sendContents(player);
            }
        }
        return false;
    }

    @Override
    public boolean onClickAir(Player player, Vector3 vector3) {
        return this.getDamage() == 1;
    }

    @Override
    public boolean onUse(Player player, int n) {
        if (player.isSpectator() || this.getDamage() != 1) {
            return false;
        }
        PlayerItemConsumeEvent playerItemConsumeEvent = new PlayerItemConsumeEvent(player, this);
        player.getServer().getPluginManager().callEvent(playerItemConsumeEvent);
        if (playerItemConsumeEvent.isCancelled()) {
            player.getInventory().sendContents(player);
            return false;
        }
        if (!player.isCreative()) {
            player.getInventory().setItemInHand(Item.get(325));
        }
        player.removeAllEffects();
        return true;
    }
}

