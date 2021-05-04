package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityVillagerV2 extends EntityVillager {

    public static final int NETWORK_ID = 115;
    
    //Profession
    // 0 普通
    // 1 农民
    // 2 渔夫
    // 3 牧羊人
    // 4 制箭师
    // 5 图书馆管理员
    // 6 制图师
    // 7 牧师
    // 8 盔甲匠
    // 9 武器匠
    // 10 工具匠
    // 11 屠夫
    // 12 皮匠
    // 13 石匠
    // 14 傻子

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
