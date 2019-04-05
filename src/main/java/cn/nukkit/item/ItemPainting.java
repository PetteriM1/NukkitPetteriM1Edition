package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.item.EntityPainting;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.sound.ItemFramePlacedSound;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class ItemPainting extends Item {

    public ItemPainting() {
        this(0, 1);
    }

    public ItemPainting(Integer meta) {
        this(meta, 1);
    }

    public ItemPainting(Integer meta, int count) {
        super(PAINTING, 0, count, "Painting");
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Level level, Player player, Block block, Block target, BlockFace face, double fx, double fy, double fz) {
        FullChunk chunk = level.getChunk((int) block.getX() >> 4, (int) block.getZ() >> 4);

        if (chunk == null) {
            return false;
        }

        if (!target.isTransparent() && face.getIndex() > 1 && !block.isSolid()) {
            int[] direction = {3, 0, 2, 1};
            int[] right = {4, 5, 3, 2};

            List<EntityPainting.Motive> validMotives = new ArrayList<>();
            for (EntityPainting.Motive motive : EntityPainting.motives) {
                boolean valid = true;
                for (int x = 0; x < motive.width && valid; x++) {
                    for (int z = 0; z < motive.height && valid; z++) {
                        if (target.getSide(BlockFace.fromIndex(right[face.getIndex() - 2]), x).isTransparent() ||
                                target.up(z).isTransparent() ||
                                block.getSide(BlockFace.fromIndex(right[face.getIndex() - 2]), x).isSolid() ||
                                block.up(z).isSolid()) {
                            valid = false;
                        }
                    }
                }

                if (valid) {
                    validMotives.add(motive);
                }
            }

            int rot = direction[face.getHorizontalIndex()] * 90;
            if (direction[face.getHorizontalIndex()] * 90 == 0) rot = 360;

            CompoundTag nbt = new CompoundTag()
                    .putByte("Direction", direction[face.getHorizontalIndex()])
                    .putString("Motive", validMotives.get(ThreadLocalRandom.current().nextInt(validMotives.size())).title)
                    .putList(new ListTag<DoubleTag>("Pos")
                            .add(new DoubleTag("0", target.x))
                            .add(new DoubleTag("1", target.y))
                            .add(new DoubleTag("2", target.z)))
                    .putList(new ListTag<DoubleTag>("Motion")
                            .add(new DoubleTag("0", 0))
                            .add(new DoubleTag("1", 0))
                            .add(new DoubleTag("2", 0)))
                    .putList(new ListTag<FloatTag>("Rotation")
                            .add(new FloatTag("0", rot))
                            .add(new FloatTag("1", 0)));

            EntityPainting entity = new EntityPainting(chunk, nbt);

            if (player.isSurvival()) {
                Item item = player.getInventory().getItemInHand();
                item.setCount(item.getCount() - 1);
                player.getInventory().setItemInHand(item);
            }
            entity.spawnToAll();
            entity.getLevel().addSound(new ItemFramePlacedSound(entity));

            return true;
        }

        return false;
    }
}
