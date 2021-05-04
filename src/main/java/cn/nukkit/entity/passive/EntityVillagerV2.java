package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityVillagerV2 extends EntityVillager {

    public static final int NETWORK_ID = 115;
    
    // 0 普通
    public static final int PROFESSION_UNEMPLOYED = 0;
    // 1 农民
    public static final int PROFESSION_FARMER = 1;
    // 2 渔夫
    public static final int PROFESSION_FISHERMAN = 2;
    // 3 牧羊人
    public static final int PROFESSION_SHEPHERD = 3;
    // 4 制箭师
    public static final int PROFESSION_FLETCHER = 4;
    // 5 图书馆管理员
    public static final int PROFESSION_LIBRARIAN = 5;
    // 6 制图师
    public static final int PROFESSION_CARTOGRAPHER = 6;
    // 7 牧师
    public static final int PROFESSION_CLERIC = 7;
    // 8 盔甲匠
    public static final int PROFESSION_ARMORER = 8;
    // 9 武器匠
    public static final int PROFESSION_WEAPONSMITH = 9;
    // 10 工具匠
    public static final int PROFESSION_TOOLSMITH = 10;
    // 11 屠夫
    public static final int PROFESSION_BUTCHER = 11;
    // 12 皮匠
    public static final int PROFESSION_LEATHERWORKER = 12;
    // 13 石匠
    public static final int PROFESSION_STONEMASON = 13;
    // 14 傻子
    public static final int PROFESSION_NITWIT = 14;

    public EntityVillagerV2(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Villager";
    }
    
    @Override
    public boolean onInteract(Player player, Item item) {
        if (this.getProfession() == PROFESSION_NITWIT) {
            return false;
        }
        return super.onInteract(player, item);
    }
    
    @Override
    public void setProfession(int profession) {
        super.setProfession(profession);
    
        //int skinID = Utils.random.nextInt(6);
        
        this.dataProperties.putInt(DATA_VARIANT, profession);
        //this.dataProperties.putInt(DATA_SKIN_ID, skinID);
    
        this.sendData(this.getViewers().values().toArray(new Player[0]),
                new EntityMetadata()
                        .putInt(DATA_VARIANT, profession)
                        /*.putInt(DATA_SKIN_ID, skinID)*/);
    }
    
}
