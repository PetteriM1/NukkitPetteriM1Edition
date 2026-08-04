package cn.nukkit.block;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.entity.projectile.EntityThrownTrident;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockTarget extends BlockSolid {

    public BlockTarget() {
        super();
    }

    @Override
    public int getBurnAbility() {
        return 15;
    }

    @Override
    public int getBurnChance() {
        return 5;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.QUARTZ_BLOCK_COLOR;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public int getId() {
        return TARGET;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_16_0;
    }

    @Override
    public String getName() {
        return "Target";
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_HOE;
    }

    @Override
    public boolean isPowerSource() {
        return true;
    }

    private int calculatePower(double hitPosX, double hitPosY, double hitPosZ) {
        // Project entity position onto the block surface. In Nukkit, projectiles stop at the face
        // (outside the block), so we clamp to get the actual face contact point.
        double hx = Math.min(this.x + 1, Math.max(this.x, hitPosX));
        double hy = Math.min(this.y + 1, Math.max(this.y, hitPosY));
        double hz = Math.min(this.z + 1, Math.max(this.z, hitPosZ));

        // 3D distance from face point to block center.
        // For a flat face: d3² = 0.25 (face normal component) + (2D face offset)²
        double dx = hx - (this.x + 0.5);
        double dy = hy - (this.y + 0.5);
        double dz = hz - (this.z + 0.5);
        double d3sq = dx * dx + dy * dy + dz * dz;

        // 2D perpendicular offset from face center: 0 = bullseye, 0.5 = edge
        double faceOffset = Math.sqrt(Math.max(0, d3sq - 0.25));

        double normalized = 1.0 - (faceOffset / 0.5);
        if (normalized <= 0) return 1;
        int power = (int) Math.ceil(normalized * 15.0);
        return Math.min(15, Math.max(1, power));
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.HAY_BALE;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{new ItemBlock(Block.get(TARGET))};
    }

    @Override
    public int getWeakPower(BlockFace face) {
        for (Entity e : level.getCollidingEntities(new SimpleAxisAlignedBB(x - 0.000001, y - 0.000001, z - 0.000001, x + 1.000001, y + 1.000001, z + 1.000001))) {
            if (e instanceof EntityProjectile) {
                EntityProjectile projectile = (EntityProjectile) e;
                boolean isArrowTrident = e instanceof EntityArrow || e instanceof EntityThrownTrident;
                if (projectile.getCollidedTick() > 0 && level.getServer().getTick() - projectile.getCollidedTick() < (isArrowTrident ? 20 : 8)) {
                    return calculatePower(e.x, e.y, e.z);
                }
            }
        }

        return 0;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        if (entity instanceof EntityProjectile) {
            this.level.updateAroundRedstone(this, null);
            boolean isArrowTrident = entity instanceof EntityArrow || entity instanceof EntityThrownTrident;
            this.level.scheduleUpdate(this, isArrowTrident ? 20 : 8);
        }
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            this.level.updateAroundRedstone(this, null);
            return Level.BLOCK_UPDATE_SCHEDULED;
        }
        return 0;
    }
}
