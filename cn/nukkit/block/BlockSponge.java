/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.block.BlockWater;
import cn.nukkit.block.b;
import cn.nukkit.item.Item;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.level.Sound;
import cn.nukkit.level.particle.ExplodeParticle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import java.util.ArrayDeque;

public class BlockSponge
extends BlockSolidMeta {
    public static final int DRY = 0;
    public static final int WET = 1;
    private static final String[] d = new String[]{"Sponge", "Wet sponge"};

    public BlockSponge() {
        this(0);
    }

    public BlockSponge(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 19;
    }

    @Override
    public double getHardness() {
        return 0.6;
    }

    @Override
    public double getResistance() {
        return 3.0;
    }

    @Override
    public String getName() {
        return d[this.getDamage() & 1];
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.CLOTH_BLOCK_COLOR;
    }

    @Override
    public int getToolType() {
        return 6;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (this.getDamage() == 1 && this.level.getDimension() == 1) {
            this.level.setBlock(block, Block.get(19, 0), true, true);
            this.level.addSound((Vector3)block.getLocation(), Sound.RANDOM_FIZZ);
            this.level.addParticle(new ExplodeParticle(block.add(0.5, 1.0, 0.5)));
            return true;
        }
        if (this.getDamage() == 0 && block instanceof BlockWater && this.a(block)) {
            this.level.setBlock(block, Block.get(19, 1), true, true);
            for (int k = 0; k < 4; ++k) {
                LevelEventPacket levelEventPacket = new LevelEventPacket();
                levelEventPacket.evid = 2001;
                levelEventPacket.x = (float)block.getX() + 0.5f;
                levelEventPacket.y = (float)block.getY() + 1.0f;
                levelEventPacket.z = (float)block.getZ() + 0.5f;
                levelEventPacket.data = GlobalBlockPalette.getOrCreateRuntimeId(ProtocolInfo.CURRENT_PROTOCOL, 8, 0);
                this.level.addChunkPacket(this.getChunkX(), this.getChunkZ(), levelEventPacket);
            }
            return true;
        }
        return super.place(item, block, block2, blockFace, d2, d3, d4, player);
    }

    private boolean a(Block block) {
        b b2;
        ArrayDeque<b> arrayDeque = new ArrayDeque<b>();
        arrayDeque.add(new b(block, 0));
        int n = 0;
        while (n < 64 && (b2 = (b)arrayDeque.poll()) != null) {
            for (BlockFace blockFace : BlockFace.values()) {
                Block block2 = b.a(b2).getSide(blockFace);
                if (Block.hasWater(block2.getId())) {
                    this.level.setBlock(block2, Block.get(0));
                    ++n;
                    if (b.b(b2) >= 6) continue;
                    arrayDeque.add(new b(block2, b.b(b2) + 1));
                    continue;
                }
                if (block2.getId() != 0 || b.b(b2) >= 6) continue;
                arrayDeque.add(new b(block2, b.b(b2) + 1));
            }
        }
        return n > 0;
    }
}

