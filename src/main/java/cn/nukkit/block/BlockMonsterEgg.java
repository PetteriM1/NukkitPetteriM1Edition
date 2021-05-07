package cn.nukkit.block;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.EntitySilverfish;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.utils.Utils;

public class BlockMonsterEgg extends BlockSolidMeta {

    public static final int STONE = 0;
    public static final int COBBLESTONE = 1;
    public static final int STONE_BRICK = 2;
    public static final int MOSSY_BRICK = 3;
    public static final int CRACKED_BRICK = 4;
    public static final int CHISELED_BRICK = 5;

    private static final String[] NAMES = new String[]{
            "Stone",
            "Cobblestone",
            "Stone Brick",
            "Mossy Stone Brick",
            "Cracked Stone Brick",
            "Chiseled Stone Brick"
    };

    public BlockMonsterEgg() {
        this(0);
    }

    public BlockMonsterEgg(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return MONSTER_EGG;
    }

    @Override
    public double getHardness() {
        return 0.75;
    }

    @Override
    public double getResistance() {
        return 3.75;
    }

    @Override
    public String getName() {
        return NAMES[this.getDamage() > 5 ? 0 : this.getDamage()] + " Monster Egg";
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[0];
    }

    @Override
    public boolean onBreak(Item item) {
        if (Server.getInstance().mobsFromBlocks) {
            if (Utils.rand(1, 5) == 1 && !item.hasEnchantment(Enchantment.ID_SILK_TOUCH) && this.getLevel().getBlockLightAt((int) this.x, (int) this.y, (int) this.z) < 12) {
                EntitySilverfish entity = (EntitySilverfish) Entity.createEntity("Silverfish", this.add(0.5, 0, 0.5));
                entity.spawnToAll();
                EntityEventPacket pk = new EntityEventPacket();
                pk.eid = entity.getId();
                pk.event = EntityEventPacket.SILVERFISH_SPAWN_ANIMATION;
                entity.getLevel().addChunkPacket(entity.getChunkX() >> 2, entity.getChunkZ() >> 2, pk);
            }
        }
        return this.getLevel().setBlock(this, Block.get(BlockID.AIR), true);
    }
}
