/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.particle;

import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.PlayerListPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.utils.Binary;
import cn.nukkit.utils.SerializedImage;
import cn.nukkit.utils.Utils;
import com.google.common.base.Strings;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

public class FloatingTextParticle
extends Particle {
    private static final Skin b = new Skin();
    private static final SerializedImage a = SerializedImage.fromLegacy(new byte[8192]);
    private static final UUID c = UUID.nameUUIDFromBytes(Binary.appendBytes(Skin.GEOMETRY_CUSTOM.getBytes(StandardCharsets.UTF_8), (byte[][])new byte[][]{FloatingTextParticle.a.data}));
    protected UUID uuid = UUID.randomUUID();
    protected final Level level;
    protected long entityId = -1L;
    protected boolean invisible = false;
    protected EntityMetadata metadata = new EntityMetadata();

    public FloatingTextParticle(Location location, String string) {
        this(location, string, null);
    }

    public FloatingTextParticle(Location location, String string, String string2) {
        this(location.getLevel(), location, string, string2);
    }

    public FloatingTextParticle(Vector3 vector3, String string) {
        this(vector3, string, null);
    }

    public FloatingTextParticle(Vector3 vector3, String string, String string2) {
        this(null, vector3, string, string2);
    }

    private FloatingTextParticle(Level level, Vector3 vector3, String string, String string2) {
        super(vector3.x, vector3.y, vector3.z);
        this.level = level;
        this.metadata.putLong(0, 65536L).putLong(37, -1L).putFloat(38, 0.01f).putFloat(54, 0.01f).putFloat(53, 0.01f);
        if (!Strings.isNullOrEmpty(string)) {
            this.metadata.putString(4, string);
        }
        if (!Strings.isNullOrEmpty(string2)) {
            this.metadata.putString(84, string2);
        }
    }

    public String getText() {
        return this.metadata.getString(84);
    }

    public void setText(String string) {
        this.metadata.putString(84, string);
        this.a();
    }

    public String getTitle() {
        return this.metadata.getString(4);
    }

    public void setTitle(String string) {
        this.metadata.putString(4, string);
        this.a();
    }

    private void a() {
        if (this.level != null) {
            SetEntityDataPacket setEntityDataPacket = new SetEntityDataPacket();
            setEntityDataPacket.eid = this.entityId;
            setEntityDataPacket.metadata = this.metadata;
            this.level.addChunkPacket(this.getChunkX(), this.getChunkZ(), setEntityDataPacket);
        }
    }

    public boolean isInvisible() {
        return this.invisible;
    }

    public void setInvisible(boolean bl) {
        this.invisible = bl;
    }

    public void setInvisible() {
        this.setInvisible(true);
    }

    public long getEntityId() {
        return this.entityId;
    }

    @Override
    public DataPacket[] mvEncode(int n) {
        PlayerListPacket.Entry[] entryArray;
        ArrayList<Object> arrayList = new ArrayList<Object>();
        if (this.entityId == -1L) {
            this.entityId = 0xFF00000000L + Utils.random.nextLong(0L, Integer.MAX_VALUE);
        } else {
            entryArray = new RemoveEntityPacket();
            entryArray.eid = this.entityId;
            entryArray.protocol = n;
            entryArray.tryEncode();
            arrayList.add(entryArray);
        }
        if (!this.invisible) {
            entryArray = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(this.uuid, this.entityId, this.metadata.getString(4), b)};
            PlayerListPacket playerListPacket = new PlayerListPacket();
            playerListPacket.entries = entryArray;
            playerListPacket.type = 0;
            playerListPacket.protocol = n;
            playerListPacket.tryEncode();
            arrayList.add(playerListPacket);
            AddPlayerPacket addPlayerPacket = new AddPlayerPacket();
            addPlayerPacket.uuid = this.uuid;
            addPlayerPacket.username = "";
            addPlayerPacket.entityUniqueId = this.entityId;
            addPlayerPacket.entityRuntimeId = this.entityId;
            addPlayerPacket.x = (float)this.x;
            addPlayerPacket.y = (float)(this.y - 0.75);
            addPlayerPacket.z = (float)this.z;
            addPlayerPacket.speedX = 0.0f;
            addPlayerPacket.speedY = 0.0f;
            addPlayerPacket.speedZ = 0.0f;
            addPlayerPacket.yaw = 0.0f;
            addPlayerPacket.pitch = 0.0f;
            addPlayerPacket.metadata = this.metadata;
            addPlayerPacket.item = Item.get(0);
            addPlayerPacket.protocol = n;
            addPlayerPacket.tryEncode();
            arrayList.add(addPlayerPacket);
            PlayerListPacket playerListPacket2 = new PlayerListPacket();
            playerListPacket2.entries = entryArray;
            playerListPacket2.type = 1;
            playerListPacket2.protocol = n;
            playerListPacket2.tryEncode();
            arrayList.add(playerListPacket2);
        }
        return arrayList.toArray(new DataPacket[0]);
    }

    static {
        b.setSkinData(a);
        b.setSkinResourcePatch(Skin.GEOMETRY_CUSTOM);
        b.setSkinId(c + ".FloatingText");
        b.setCapeData(SerializedImage.EMPTY);
        b.setCapeId("");
    }
}

