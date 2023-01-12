/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.block.BlockRedstoneDiode;
import cn.nukkit.event.block.BlockRedstoneEvent;
import cn.nukkit.event.redstone.RedstoneUpdateEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import java.util.EnumSet;

public class BlockRedstoneWire
extends BlockFlowable {
    private boolean d = true;

    public BlockRedstoneWire() {
        this(0);
    }

    public BlockRedstoneWire(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Redstone Wire";
    }

    @Override
    public int getId() {
        return 55;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (!BlockRedstoneWire.canStayOnFullSolid(block.down())) {
            return false;
        }
        this.getLevel().setBlock(block, this, true, false);
        this.a(true);
        Location location = this.getLocation();
        for (BlockFace blockFace2 : BlockFace.Plane.VERTICAL) {
            this.level.updateAroundRedstone(location.getSideVec(blockFace2), blockFace2.getOpposite());
        }
        for (BlockFace blockFace2 : BlockFace.Plane.VERTICAL) {
            this.c(location.getSideVec(blockFace2), blockFace2.getOpposite());
        }
        for (BlockFace blockFace2 : BlockFace.Plane.HORIZONTAL) {
            Vector3 vector3 = location.getSideVec(blockFace2);
            if (this.level.getBlock(vector3).isNormalBlock()) {
                this.c(vector3.getSideVec(BlockFace.UP), BlockFace.DOWN);
                continue;
            }
            this.c(vector3.getSideVec(BlockFace.DOWN), BlockFace.UP);
        }
        return true;
    }

    private void c(Vector3 vector3, BlockFace blockFace) {
        if (this.level.getBlockIdAt((int)vector3.x, (int)vector3.y, (int)vector3.z) == 55) {
            this.level.updateAroundRedstone(vector3, blockFace);
            for (BlockFace blockFace2 : BlockFace.values()) {
                this.level.updateAroundRedstone(vector3.getSideVec(blockFace2), blockFace2.getOpposite());
            }
        }
    }

    private void a(boolean bl) {
        block9: {
            Location location;
            block8: {
                int n;
                location = this.getLocation();
                int n2 = n = this.getDamage();
                this.d = false;
                int n3 = this.a();
                this.d = true;
                if (n3 > 0 && n3 > n2 - 1) {
                    n2 = n3;
                }
                int n4 = 0;
                for (BlockFace blockFace : BlockFace.Plane.HORIZONTAL) {
                    Vector3 vector3 = location.getSideVec(blockFace);
                    if (vector3.getX() == this.getX() && vector3.getZ() == this.getZ()) continue;
                    n4 = this.a(vector3, n4);
                    boolean bl2 = this.level.getBlock(vector3).isNormalBlock();
                    if (bl2 && !this.level.getBlock(location.getSideVec(BlockFace.UP)).isNormalBlock()) {
                        n4 = this.a(vector3.getSideVec(BlockFace.UP), n4);
                        continue;
                    }
                    if (bl2) continue;
                    n4 = this.a(vector3.getSideVec(BlockFace.DOWN), n4);
                }
                n2 = n4 > n2 ? n4 - 1 : (n2 > 0 ? --n2 : 0);
                if (n3 > n2 - 1) {
                    n2 = n3;
                } else if (n3 < n2 && n4 <= n2) {
                    n2 = Math.max(n3, n4 - 1);
                }
                if (n == n2) break block8;
                this.level.getServer().getPluginManager().callEvent(new BlockRedstoneEvent(this, n, n2));
                this.setDamage(n2);
                this.level.setBlock(this, this, false, false);
                this.level.updateAroundRedstone(this, null);
                for (BlockFace blockFace : BlockFace.values()) {
                    this.level.updateAroundRedstone(location.getSideVec(blockFace), blockFace.getOpposite());
                }
                break block9;
            }
            if (!bl) break block9;
            for (BlockFace blockFace : BlockFace.values()) {
                this.level.updateAroundRedstone(location.getSideVec(blockFace), blockFace.getOpposite());
            }
        }
    }

    private int a(Vector3 vector3, int n) {
        if (this.level.getBlockIdAt(vector3.getFloorX(), vector3.getFloorY(), vector3.getFloorZ()) != 55) {
            return n;
        }
        int n2 = this.level.getBlockDataAt(vector3.getFloorX(), vector3.getFloorY(), vector3.getFloorZ());
        return Math.max(n2, n);
    }

    @Override
    public boolean onBreak(Item item) {
        this.getLevel().setBlock(this, Block.get(0), true, true);
        Location location = this.getLocation();
        this.level.updateAroundRedstone(location, null);
        for (BlockFace blockFace : BlockFace.values()) {
            this.level.updateAroundRedstone(location.getSideVec(blockFace), null);
        }
        for (BlockFace blockFace : BlockFace.Plane.HORIZONTAL) {
            Vector3 vector3 = location.getSideVec(blockFace);
            if (this.level.getBlock(vector3).isNormalBlock()) {
                this.c(vector3.getSideVec(BlockFace.UP), BlockFace.DOWN);
                continue;
            }
            this.c(vector3.getSideVec(BlockFace.DOWN), BlockFace.UP);
        }
        return true;
    }

    @Override
    public Item toItem() {
        return Item.get(331);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    @Override
    public int onUpdate(int n) {
        if (n != 1 && n != 6) {
            return 0;
        }
        if (n == 1 && !BlockRedstoneWire.canStayOnFullSolid(this.down())) {
            this.getLevel().useBreakOn(this);
            return 1;
        }
        RedstoneUpdateEvent redstoneUpdateEvent = new RedstoneUpdateEvent(this);
        this.getLevel().getServer().getPluginManager().callEvent(redstoneUpdateEvent);
        if (redstoneUpdateEvent.isCancelled()) {
            return 0;
        }
        if (this.level.getBlockIdAt((int)this.x, (int)this.y, (int)this.z) != this.getId()) {
            return 0;
        }
        this.a(false);
        return 6;
    }

    public boolean canBePlacedOn(Vector3 vector3) {
        return BlockRedstoneWire.canStayOnFullSolid(this.level.getBlock(vector3));
    }

    @Override
    public int getStrongPower(BlockFace blockFace) {
        return !this.d ? 0 : this.getWeakPower(blockFace);
    }

    @Override
    public int getWeakPower(BlockFace blockFace) {
        if (!this.d) {
            return 0;
        }
        int n = this.getDamage();
        if (n == 0) {
            return 0;
        }
        if (blockFace == BlockFace.UP) {
            return n;
        }
        EnumSet<BlockFace> enumSet = EnumSet.noneOf(BlockFace.class);
        for (BlockFace blockFace2 : BlockFace.Plane.HORIZONTAL) {
            if (!this.a(blockFace2)) continue;
            enumSet.add(blockFace2);
        }
        if (blockFace.getAxis().isHorizontal() && enumSet.isEmpty()) {
            return n;
        }
        if (enumSet.contains((Object)blockFace) && !enumSet.contains((Object)blockFace.rotateYCCW()) && !enumSet.contains((Object)blockFace.rotateY())) {
            return n;
        }
        return 0;
    }

    private boolean a(BlockFace blockFace) {
        Block block = this.getSide(blockFace);
        boolean bl = block.isNormalBlock();
        return bl && !this.up().isNormalBlock() && BlockRedstoneWire.canConnectUpwardsTo(block.up()) || BlockRedstoneWire.canConnectTo(block, blockFace) || !bl && BlockRedstoneWire.canConnectUpwardsTo(block.down());
    }

    protected static boolean canConnectUpwardsTo(Level level, Vector3 vector3) {
        return BlockRedstoneWire.canConnectUpwardsTo(level.getBlock(vector3));
    }

    protected static boolean canConnectUpwardsTo(Block block) {
        return BlockRedstoneWire.canConnectTo(block, null);
    }

    protected static boolean canConnectTo(Block block, BlockFace blockFace) {
        if (block.getId() == 55) {
            return true;
        }
        if (BlockRedstoneDiode.isDiode(block)) {
            BlockFace blockFace2 = ((BlockRedstoneDiode)block).getFacing();
            return blockFace2 == blockFace || blockFace2.getOpposite() == blockFace;
        }
        return block.isPowerSource() && blockFace != null;
    }

    @Override
    public boolean isPowerSource() {
        return this.d;
    }

    private int a() {
        int n = 0;
        Location location = this.getLocation();
        for (BlockFace blockFace : BlockFace.values()) {
            int n2 = this.a(location.getSideVec(blockFace), blockFace);
            if (n2 >= 15) {
                return 15;
            }
            if (n2 <= n) continue;
            n = n2;
        }
        return n;
    }

    private int a(Vector3 vector3, BlockFace blockFace) {
        Block block = this.level.getBlock(vector3);
        if (block.getId() == 55) {
            return 0;
        }
        return block.isNormalBlock() ? this.b(vector3.getSideVec(blockFace), blockFace) : block.getWeakPower(blockFace);
    }

    private int b(Vector3 vector3, BlockFace blockFace) {
        Block block = this.level.getBlock(vector3);
        if (block.getId() == 55) {
            return 0;
        }
        return block.getStrongPower(blockFace);
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

