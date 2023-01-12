/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.block.BlockTripWire;
import cn.nukkit.event.block.BlockRedstoneEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.Location;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;

public class BlockTripWireHook
extends BlockFlowable {
    public BlockTripWireHook() {
        this(0);
    }

    public BlockTripWireHook(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Tripwire Hook";
    }

    @Override
    public int getId() {
        return 131;
    }

    public BlockFace getFacing() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 3);
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            if (!this.getSide(this.getFacing().getOpposite()).isNormalBlock()) {
                this.level.useBreakOn(this);
            }
            return n;
        }
        if (n == 3) {
            this.calculateState(false, true, -1, null);
            return n;
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (!this.getSide(blockFace.getOpposite()).isNormalBlock() || blockFace == BlockFace.DOWN || blockFace == BlockFace.UP) {
            return false;
        }
        if (blockFace.getAxis().isHorizontal()) {
            this.setFace(blockFace);
        }
        this.level.setBlock(this, this);
        if (player != null) {
            this.calculateState(false, false, -1, null);
        }
        return true;
    }

    @Override
    public boolean onBreak(Item item) {
        super.onBreak(item);
        boolean bl = this.isAttached();
        boolean bl2 = this.isPowered();
        if (bl || bl2) {
            this.calculateState(true, false, -1, null);
        }
        if (bl2) {
            this.level.updateAroundRedstone(this, null);
            this.level.updateAroundRedstone(this.getLocation().getSide(this.getFacing().getOpposite()), null);
        }
        return true;
    }

    public void calculateState(boolean bl, boolean bl2, int n, Block block) {
        Object object;
        Vector3 vector3;
        BlockFace blockFace = this.getFacing();
        Location location = this.getLocation();
        boolean bl3 = this.isAttached();
        boolean bl4 = this.isPowered();
        boolean bl5 = !bl;
        boolean bl6 = false;
        int n2 = 0;
        Block[] blockArray = new Block[42];
        for (int k = 1; k < 42; ++k) {
            vector3 = ((Vector3)location).getSide(blockFace, k);
            object = this.level.getBlock(vector3);
            if (object instanceof BlockTripWireHook) {
                if (((BlockTripWireHook)object).getFacing() != blockFace.getOpposite()) break;
                n2 = k;
                break;
            }
            if (((Block)object).getId() != 132 && k != n) {
                blockArray[k] = null;
                bl5 = false;
                continue;
            }
            if (k == n) {
                Object object2 = object = block != null ? block : object;
            }
            if (object instanceof BlockTripWire) {
                boolean bl7 = !((BlockTripWire)object).isDisarmed();
                boolean bl8 = ((BlockTripWire)object).isPowered();
                bl6 |= bl7 && bl8;
                if (k == n) {
                    this.level.scheduleUpdate(this, 10);
                    bl5 &= bl7;
                }
            }
            blockArray[k] = object;
        }
        boolean bl9 = n2 > 1;
        BlockTripWireHook blockTripWireHook = (BlockTripWireHook)Block.get(131);
        blockTripWireHook.setAttached(bl5);
        blockTripWireHook.setPowered(bl6 &= (bl5 &= bl9));
        if (n2 > 0) {
            vector3 = ((Vector3)location).getSide(blockFace, n2);
            object = blockFace.getOpposite();
            blockTripWireHook.setFace((BlockFace)((Object)object));
            this.level.setBlock(vector3, blockTripWireHook, true, false);
            this.level.updateAroundRedstone(vector3, null);
            this.level.updateAroundRedstone(vector3.getSide(((BlockFace)((Object)object)).getOpposite()), null);
            this.a(vector3, bl5, bl6, bl3, bl4);
        }
        this.a(location, bl5, bl6, bl3, bl4);
        if (!bl) {
            blockTripWireHook.setFace(blockFace);
            this.level.setBlock(location, blockTripWireHook, true, false);
            if (bl2) {
                this.level.updateAroundRedstone(location, null);
                this.level.updateAroundRedstone(((Vector3)location).getSide(blockFace.getOpposite()), null);
            }
        }
        if (bl3 != bl5) {
            for (int k = 1; k < n2; ++k) {
                object = ((Vector3)location).getSide(blockFace, k);
                block = blockArray[k];
                if (block == null || this.level.getBlockIdAt(((Vector3)object).getFloorX(), ((Vector3)object).getFloorY(), ((Vector3)object).getFloorZ()) == 0) continue;
                if (bl5 ^ (block.getDamage() & 4) > 0) {
                    block.setDamage(block.getDamage() ^ 4);
                }
                this.level.setBlock((Vector3)object, block, true, false);
            }
        }
    }

    private void a(Vector3 vector3, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        if (bl2 && !bl4) {
            this.level.addLevelSoundEvent(vector3, 73);
            this.level.getServer().getPluginManager().callEvent(new BlockRedstoneEvent(this, 0, 15));
        } else if (!bl2 && bl4) {
            this.level.addLevelSoundEvent(vector3, 74);
            this.level.getServer().getPluginManager().callEvent(new BlockRedstoneEvent(this, 15, 0));
        } else if (bl && !bl3) {
            this.level.addLevelSoundEvent(vector3, 75);
        } else if (!bl && bl3) {
            this.level.addLevelSoundEvent(vector3, 76);
        }
    }

    public boolean isAttached() {
        return (this.getDamage() & 4) > 0;
    }

    public boolean isPowered() {
        return (this.getDamage() & 8) > 0;
    }

    public void setPowered(boolean bl) {
        if (bl ^ this.isPowered()) {
            this.setDamage(this.getDamage() ^ 8);
        }
    }

    public void setAttached(boolean bl) {
        if (bl ^ this.isAttached()) {
            this.setDamage(this.getDamage() ^ 4);
        }
    }

    public void setFace(BlockFace blockFace) {
        this.setDamage(this.getDamage() - this.getDamage() % 4);
        this.setDamage(this.getDamage() | blockFace.getHorizontalIndex());
    }

    @Override
    public boolean isPowerSource() {
        return true;
    }

    @Override
    public int getWeakPower(BlockFace blockFace) {
        return this.isPowered() ? 15 : 0;
    }

    @Override
    public int getStrongPower(BlockFace blockFace) {
        return !this.isPowered() ? 0 : (this.getFacing() == blockFace ? 15 : 0);
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

