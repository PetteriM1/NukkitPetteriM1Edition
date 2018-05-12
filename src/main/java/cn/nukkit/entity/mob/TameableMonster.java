package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.LongEntityData;
import cn.nukkit.entity.EntityTameable;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public abstract class TameableMonster extends WalkingMonster implements EntityTameable {

    private Server          server          = null;

    private Player          owner           = null;

    private String          ownerUUID       = "";

    private boolean         sitting         = false;

    public TameableMonster(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.server = Server.getInstance();
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        if (this.namedTag != null) {
            String ownerName = namedTag.getString(NAMED_TAG_OWNER);
            if (ownerName != null && ownerName.length() > 0) {
                Player player = server.getPlayer(ownerName);
                this.setOwner(player);
                this.setSitting(namedTag.getBoolean(NAMED_TAG_SITTING));
            }
        }

    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        namedTag.putBoolean(NAMED_TAG_SITTING, this.sitting);
        if (this.owner != null) {
            namedTag.putString(NAMED_TAG_OWNER, this.owner.getName());
            namedTag.putString(NAMED_TAG_OWNER_UUID, owner.getUniqueId().toString());
        } else {
            namedTag.putString(NAMED_TAG_OWNER, "");
            namedTag.putString(NAMED_TAG_OWNER_UUID, "");
        }
    }

    @Override
    public Player getOwner() {
        return owner;
    }

    public boolean hasOwner(){
        return owner!=null;
    }

    public void setOwner(Player player) {
        this.owner = player;
        setDataProperty(new LongEntityData(DATA_OWNER_EID, player.getId()));
        setTamed(true);
    }

    @Override
    public String getName() {
        return getNameTag();
    }

    public boolean isSitting() {
        return sitting;
    }

    public void setSitting(boolean flag) {
        this.sitting = flag;
        setSittingDataProperty(flag);
    }


    private void setTamed (boolean tamed) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_TAMED, tamed);
    }

    private void setSittingDataProperty(boolean sit) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_SITTING, sit);
    }

    @Override
    public String getOwnerUUID() {
        return ownerUUID;
    }

    @Override
    public void setOwnerUUID(String ownerUUID) {
        this.ownerUUID = ownerUUID;
    }
}
