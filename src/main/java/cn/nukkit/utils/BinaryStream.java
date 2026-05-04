package cn.nukkit.utils;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.*;
import cn.nukkit.item.RuntimeItemMapping.LegacyEntry;
import cn.nukkit.item.RuntimeItemMapping.RuntimeEntry;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.GameRules;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector2f;
import cn.nukkit.math.Vector3f;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.network.LittleEndianByteBufInputStream;
import cn.nukkit.network.LittleEndianByteBufOutputStream;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.network.protocol.types.EntityLink;
import cn.nukkit.network.protocol.types.ExperimentData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import it.unimi.dsi.fastutil.io.FastByteArrayInputStream;
import org.cloudburstmc.nbt.NBTOutputStream;
import org.cloudburstmc.nbt.NbtUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * BinaryStream
 *
 * @author MagicDroidX
 * Nukkit Project
 */
public class BinaryStream {

    public int offset;
    private byte[] buffer;
    protected int count;

    private static final int MAX_ARRAY_SIZE = 2147483639;

    public BinaryStream() {
        this.buffer = new byte[32];
        this.offset = 0;
        this.count = 0;
    }

    public BinaryStream(byte[] buffer) {
        this(buffer, 0);
    }

    public BinaryStream(byte[] buffer, int offset) {
        this.buffer = buffer;
        this.offset = offset;
        this.count = buffer.length;
    }
    private static byte[] steveSkinDecoded;

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
        this.count = buffer == null ? -1 : buffer.length;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Reads a list of Attributes from the stream.
     *
     * @return Attribute[]
     */
    public Attribute[] getAttributeList() throws Exception {
        List<Attribute> list = new ArrayList<>();
        long count = this.getUnsignedVarInt();

        for (int i = 0; i < count; ++i) {
            String name = this.getString();
            Attribute attr = Attribute.getAttributeByName(name);
            if (attr != null) {
                attr.setMinValue(this.getLFloat());
                attr.setValue(this.getLFloat());
                attr.setMaxValue(this.getLFloat());
                list.add(attr);
            } else {
                throw new Exception("Unknown attribute type \"" + name + '"');
            }
        }

        return list.toArray(new Attribute[0]);
    }

    public BlockFace getBlockFace() {
        return BlockFace.fromIndex(this.getVarInt());
    }

    public BlockVector3 getBlockVector3() {
        Server.mvw("BinaryStream#getBlockVector3()");
        return getBlockVector3(ProtocolInfo.CURRENT_PROTOCOL);
    }

    public boolean getBoolean() {
        return this.getByte() == 0x01;
    }

    public byte[] getBuffer() {
        return Arrays.copyOf(buffer, count);
    }

    public int getByte() {
        return this.buffer[this.offset++] & 0xff;
    }

    public byte[] getByteArray() {
        return this.get((int) this.getUnsignedVarInt());
    }

    public int getCount() {
        return count;
    }

    public EntityLink getEntityLink() {
        Server.mvw("BinaryStream#getEntityLink()");
        return new EntityLink(
                getEntityUniqueId(),
                getEntityUniqueId(),
                (byte) getByte(),
                getBoolean(),
                getBoolean(), //1.16+
                getLFloat() // v1_21_20
        );
    }

    /**
     * Reads and returns an EntityRuntimeID
     */
    public long getEntityRuntimeId() {
        return this.getUnsignedVarLong();
    }

    /**
     * Reads and returns an EntityUniqueID
     *
     * @return int
     */
    public long getEntityUniqueId() {
        return this.getVarLong();
    }

    public float getFloat() {
        return getFloat(-1);
    }

    public SerializedImage getImage() {
        int width = this.getLInt();
        int height = this.getLInt();
        byte[] data = this.getByteArray();
        return new SerializedImage(width, height, data);
    }

    public int getInt() {
        return Binary.readInt(this.get(4));
    }

    public float getLFloat() {
        return getLFloat(-1);
    }

    public int getLInt() {
        return Binary.readLInt(this.get(4));
    }

    public long getLLong() {
        return Binary.readLLong(this.get(8));
    }

    public int getLShort() {
        return Binary.readLShort(this.get(2));
    }

    public int getLTriad() {
        return Binary.readLTriad(this.get(3));
    }

    public long getLong() {
        return Binary.readLong(this.get(8));
    }

    public int getOffset() {
        return offset;
    }

    public byte[] getRawBuffer() {
        return buffer;
    }

    public int getShort() {
        return Binary.readShort(this.get(2));
    }

    public BlockVector3 getSignedBlockPosition() {
        return new BlockVector3(getVarInt(), getVarInt(), getVarInt());
    }

    public Skin getSkin() {
        Server.mvw("BinaryStream#getSkin()");
        return getSkin(ProtocolInfo.CURRENT_PROTOCOL);
    }

    public Item getSlot() {
        Server.mvw("BinaryStream#getSlot()");
        return this.getSlot(ProtocolInfo.CURRENT_PROTOCOL);
    }

    public String getString() {
        return new String(this.getByteArray(), StandardCharsets.UTF_8);
    }

    public int getTriad() {
        return Binary.readTriad(this.get(3));
    }

    public UUID getUUID() {
        return Binary.readUUID(this.get(16));
    }

    public long getUnsignedVarInt() {
        return VarInt.readUnsignedVarInt(this);
    }

    public long getUnsignedVarLong() {
        return VarInt.readUnsignedVarLong(this);
    }

    public int getVarInt() {
        return VarInt.readVarInt(this);
    }

    public long getVarLong() {
        return VarInt.readVarLong(this);
    }

    public Vector2f getVector2f() {
        return new Vector2f(this.getLFloat(), this.getLFloat());
    }

    public Vector3f getVector3f() {
        return new Vector3f(this.getLFloat(), this.getLFloat(), this.getLFloat());
    }

    private void ensureCapacity(int minCapacity) {
        // overflow-conscious code
        if (minCapacity - buffer.length > 0) {
            grow(minCapacity);
        }
    }

    private static List<String> extractStringList(Item item, String tagName) {
        CompoundTag namedTag = item.getNamedTag();
        if (namedTag == null) {
            return Collections.emptyList();
        }

        ListTag<StringTag> listTag = namedTag.getList(tagName, StringTag.class);
        if (listTag == null) {
            return Collections.emptyList();
        }

        int size = listTag.size();
        List<String> values = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            StringTag stringTag = listTag.get(i);
            if (stringTag != null) {
                values.add(stringTag.data);
            }
        }

        return values;
    }

    public boolean feof() {
        return this.offset < 0 || this.offset >= this.buffer.length;
    }

    public byte[] get() {
        return this.get(this.count - this.offset);
    }

    public byte[] get(int len) {
        if (len < 0) {
            this.offset = this.count - 1;
            return new byte[0];
        }
        len = Math.min(len, this.count - this.offset);
        this.offset += len;
        return Arrays.copyOfRange(this.buffer, this.offset - len, this.offset);
    }

    @SuppressWarnings("unchecked")
    public <T> T[] getArray(Class<T> clazz, Function<BinaryStream, T> function) {
        ArrayDeque<T> deque = new ArrayDeque<>();
        int count = (int) getUnsignedVarInt();
        for (int i = 0; i < count; i++) {
            deque.add(function.apply(this));
        }
        return deque.toArray((T[]) Array.newInstance(clazz, 0));
    }

    public BlockVector3 getBlockVector3(int protocol) {
        return new BlockVector3(this.getVarInt(), protocol >= ProtocolInfo.v1_26_10 ? this.getVarInt() : (int) this.getUnsignedVarInt(), this.getVarInt());
    }

    protected void getDummyNetworkItemStackDescriptor(int protocol) {
        if (protocol < ProtocolInfo.v1_26_20_26) {
            getDummySlot(protocol);
            return;
        }

        this.getLShort(); // runtimeId
        this.getLShort(); // count
        this.getUnsignedVarInt(); // damage

        if (this.getBoolean()) { // hasNetId
            this.getUnsignedVarInt(); // netIdVariant
            this.getVarInt(); // netId
        }

        this.getUnsignedVarInt(); // blockRuntimeId

        this.getByteArray(); // bytes
    }

    protected void getDummySlot(int protocol) {
        int runtimeId = this.getVarInt();
        if (runtimeId == 0) {
            return;
        }

        if (protocol < ProtocolInfo.v1_16_220) {
            this.getVarInt(); // auxValue

            int id;
            if (protocol < ProtocolInfo.v1_16_100) {
                id = runtimeId;
            } else {
                LegacyEntry legacyEntry = RuntimeItems.getMapping(protocol).fromRuntime(runtimeId);
                id = legacyEntry.getLegacyId();
            }

            int nbtLen = this.getLShort();
            if (nbtLen < Short.MAX_VALUE) {
                this.get(nbtLen);
            } else if (nbtLen == 65535) {
                int nbtTagCount = (int) getUnsignedVarInt();
                int offset = this.offset;
                FastByteArrayInputStream stream = new FastByteArrayInputStream(get());
                for (int i = 0; i < nbtTagCount; i++) {
                    try {
                        NBTIO.read(stream, ByteOrder.LITTLE_ENDIAN, true);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                setOffset(offset + (int) stream.position());
            }

            int canPlaceOnCount = this.getVarInt();
            if (canPlaceOnCount > 4096) {
                throw new RuntimeException("Too many CanPlaceOn blocks: " + canPlaceOnCount);
            }

            for (int i = 0; i < canPlaceOnCount; ++i) {
                this.getString();
            }

            int canDestroyCount = this.getVarInt();
            if (canDestroyCount > 4096) {
                throw new RuntimeException("Too many CanDestroy blocks: " + canDestroyCount);
            }

            for (int i = 0; i < canDestroyCount; ++i) {
                this.getString();
            }

            if (id == ItemID.SHIELD && protocol >= ProtocolInfo.v1_11_0) {
                this.getVarLong();
            }

            return;
        }

        this.getLShort(); // count
        this.getUnsignedVarInt(); // damage

        if (this.getBoolean()) { // hasNetId
            this.getVarInt(); // netId
        }

        this.getVarInt(); // blockRuntimeId

        this.getByteArray(); // data
    }

    public float getFloat(int accuracy) {
        return Binary.readFloat(this.get(4), accuracy);
    }

    public float getLFloat(int accuracy) {
        return Binary.readLFloat(this.get(4), accuracy);
    }

    public Item getNetworkItemStackDescriptor(int protocol) {
        if (protocol < ProtocolInfo.v1_26_20_26) {
            return getSlot(protocol);
        }

        int id = 0;
        short runtimeId = (short) this.getLShort(); // signed short
        int count = this.getLShort();
        int damage = (int) this.getUnsignedVarInt();

        LegacyEntry legacyEntry = null;

        if (runtimeId != 0) {
            legacyEntry = RuntimeItems.getMapping(protocol).fromRuntime(runtimeId);

            id = legacyEntry.getLegacyId();

            if (legacyEntry.isHasDamage()) {
                damage = legacyEntry.getDamage();
            }
        }

        if (this.getBoolean()) { // hasNetId
            this.getUnsignedVarInt(); // netIdVariant
            this.getVarInt(); // netId
        }

        int blockRuntimeId = (int) this.getUnsignedVarInt();

        if (id != Item.AIR && id < 256 && id != 166 && !(id == -212 && legacyEntry.getDamage() == 0) && !legacyEntry.isHasDamage() && (id == BlockID.RED_MUSHROOM_BLOCK || id == BlockID.BROWN_MUSHROOM_BLOCK)) { // ItemBlock
            int fullId = GlobalBlockPalette.getLegacyFullId(protocol, blockRuntimeId);
            if (fullId != -1) {
                damage = fullId & 0x3f;
            }
        }

        byte[] nbt = new byte[0];
        String[] canPlace = null;
        String[] canBreak = null;

        byte[] bytes = this.getByteArray();

        if (bytes.length != 0) {
            ByteBuf buf = ByteBufAllocator.DEFAULT.ioBuffer(bytes.length);
            buf.writeBytes(bytes);

            try (LittleEndianByteBufInputStream stream = new LittleEndianByteBufInputStream(buf)) {
                int nbtSize = stream.readShort();

                CompoundTag compoundTag = null;
                if (nbtSize > 0) {
                    compoundTag = NBTIO.read(stream, ByteOrder.LITTLE_ENDIAN);
                } else if (nbtSize == -1) {
                    int tagCount = stream.readUnsignedByte();
                    if (tagCount != 1) throw new IllegalArgumentException("Expected 1 tag but got " + tagCount);
                    compoundTag = NBTIO.read(stream, ByteOrder.LITTLE_ENDIAN);
                }

                if (compoundTag != null && !compoundTag.getAllTags().isEmpty()) {
                    if (!legacyEntry.isHasDamage() && compoundTag.contains("Damage")) {
                        damage = compoundTag.getInt("Damage");
                        compoundTag.remove("Damage");
                    }
                    if (compoundTag.contains("__DamageConflict__")) {
                        compoundTag.put("Damage", compoundTag.removeAndGet("__DamageConflict__"));
                    }
                    if (!compoundTag.isEmpty()) {
                        nbt = NBTIO.write(compoundTag, ByteOrder.LITTLE_ENDIAN);
                    }
                }

                int canPlaceCount = stream.readInt();
                if (canPlaceCount > 4096) {
                    throw new RuntimeException("Too many CanPlaceOn blocks: " + canPlaceCount);
                }

                canPlace = new String[canPlaceCount];
                for (int i = 0; i < canPlace.length; i++) {
                    canPlace[i] = stream.readUTF(1024);
                }

                int canBreakCount = stream.readInt();
                if (canBreakCount > 4096) {
                    throw new RuntimeException("Too many CanDestroy blocks: " + canBreakCount);
                }

                canBreak = new String[canBreakCount];
                for (int i = 0; i < canBreak.length; i++) {
                    canBreak[i] = stream.readUTF(1024);
                }

                if (id == ItemID.SHIELD) {
                    stream.readLong();
                }

                if (compoundTag != null && compoundTag.contains("mv_origin_id") && compoundTag.contains("mv_origin_meta")) {
                    Item item = Item.get(compoundTag.getInt("mv_origin_id"), compoundTag.getInt("mv_origin_meta"), count);
                    if (compoundTag.contains("mv_origin_nbt")) {
                        item.setNamedTag(compoundTag.getCompound("mv_origin_nbt"));
                    }
                    return item;
                }
            } catch (IOException e) {
                throw new IllegalStateException("Unable to read item user data", e);
            } finally {
                buf.release();
            }
        }

        Item item = Item.get(id, damage, count, nbt);

        if ((canBreak != null && canBreak.length > 0) || (canPlace != null && canPlace.length > 0)) {
            CompoundTag namedTag = item.getNamedTag();
            if (namedTag == null) {
                namedTag = new CompoundTag();
            }

            if (canBreak != null && canBreak.length > 0) {
                ListTag<StringTag> listTag = new ListTag<>("CanDestroy");
                for (String blockName : canBreak) {
                    listTag.add(new StringTag("", blockName));
                }
                namedTag.put("CanDestroy", listTag);
            }

            if (canPlace != null && canPlace.length > 0) {
                ListTag<StringTag> listTag = new ListTag<>("CanPlaceOn");
                for (String blockName : canPlace) {
                    listTag.add(new StringTag("", blockName));
                }
                namedTag.put("CanPlaceOn", listTag);
            }

            item.setNamedTag(namedTag);
        }

        return item;
    }

    public Item getRecipeIngredient(int protocol) {
        int runtimeId = this.getVarInt();
        if (runtimeId == 0) {
            return Item.get(Item.AIR, 0, 0);
        }

        int damage = this.getVarInt();
        if (damage == 0x7fff) {
            damage = -1;
        }

        int id;
        if (protocol < ProtocolInfo.v1_16_100) {
            id = runtimeId;
        } else {
            RuntimeItemMapping mapping = RuntimeItems.getMapping(protocol);
            LegacyEntry legacyEntry = mapping.fromRuntime(runtimeId);
            id = legacyEntry.getLegacyId();
            if (legacyEntry.isHasDamage()) {
                damage = legacyEntry.getDamage();
            }
        }

        int count = this.getVarInt();
        return Item.get(id, damage, count);
    }

    public Skin getSkin(int protocol) { // Can be used only with protocol >= 388
        Skin skin = new Skin();
        skin.setSkinId(this.getString());
        if (protocol >= ProtocolInfo.v1_16_210) {
            skin.setPlayFabId(this.getString());
        }
        skin.setSkinResourcePatch(this.getString());
        skin.setSkinData(this.getImage());

        int animationCount = this.getLInt();
        for (int i = 0; i < Math.min(animationCount, 1024); i++) {
            SerializedImage image = this.getImage();
            int type = this.getLInt();
            float frames = this.getLFloat();
            int expression = protocol >= ProtocolInfo.v1_16_100 ? this.getLInt() : 0;
            skin.getAnimations().add(new SkinAnimation(image, type, frames, expression));
        }

        skin.setCapeData(this.getImage());
        skin.setGeometryData(this.getString());
        if (protocol >= ProtocolInfo.v1_17_30) {
            skin.setGeometryDataEngineVersion(this.getString());
        }
        skin.setAnimationData(this.getString());
        if (protocol < ProtocolInfo.v1_17_30) {
            skin.setPremium(this.getBoolean());
            skin.setPersona(this.getBoolean());
            skin.setCapeOnClassic(this.getBoolean());
        }
        skin.setCapeId(this.getString());
        skin.setFullSkinId(this.getString());
        if (protocol >= ProtocolInfo.v1_14_60) {
            skin.setArmSize(this.getString());
            skin.setSkinColor(this.getString());

            int piecesLength = this.getLInt();
            for (int i = 0; i < Math.min(piecesLength, 1024); i++) {
                String pieceId = this.getString();
                String pieceType = this.getString();
                String packId = this.getString();
                boolean isDefault = this.getBoolean();
                String productId = this.getString();
                skin.getPersonaPieces().add(new PersonaPiece(pieceId, pieceType, packId, isDefault, productId));
            }

            int tintsLength = this.getLInt();
            for (int i = 0; i < Math.min(tintsLength, 1024); i++) {
                String pieceType = this.getString();
                List<String> colors = new ArrayList<>();
                int colorsLength = this.getLInt();
                for (int i2 = 0; i2 < Math.min(colorsLength, 1024); i2++) {
                    colors.add(this.getString());
                }
                skin.getTintColors().add(new PersonaPieceTint(pieceType, colors));
            }

            if (protocol >= ProtocolInfo.v1_17_30) {
                skin.setPremium(this.getBoolean());
                skin.setPersona(this.getBoolean());
                skin.setCapeOnClassic(this.getBoolean());
                skin.setPrimaryUser(this.getBoolean());
                if (protocol >= ProtocolInfo.v1_19_63) {
                    this.getBoolean(); //skin.setOverridingPlayerAppearance(this.getBoolean());
                }
            }
        }
        return skin;
    }

    public Item getSlot(int protocol) {
        if (protocol >= ProtocolInfo.v1_16_220) {
            return this.getSlotNew(protocol);
        }

        int runtimeId = this.getVarInt();
        if (runtimeId == 0) {
            return Item.get(Item.AIR, 0, 0);
        }

        int auxValue = this.getVarInt();
        int damage = auxValue >> 8;
        if (damage == Short.MAX_VALUE) {
            damage = -1;
        }

        int id;
        if (protocol < ProtocolInfo.v1_16_100) {
            id = runtimeId;
        } else {
            RuntimeItemMapping mapping = RuntimeItems.getMapping(protocol);
            LegacyEntry legacyEntry = mapping.fromRuntime(runtimeId);
            id = legacyEntry.getLegacyId();
            if (legacyEntry.isHasDamage()) {
                damage = legacyEntry.getDamage();
            }
        }

        int cnt = auxValue & 0xff;

        int nbtLen = this.getLShort();
        byte[] nbt = new byte[0];
        if (nbtLen < Short.MAX_VALUE) {
            nbt = this.get(nbtLen);
        } else if (nbtLen == 65535) {
            int nbtTagCount = (int) getUnsignedVarInt();
            int offset = this.offset;
            FastByteArrayInputStream stream = new FastByteArrayInputStream(get());
            for (int i = 0; i < nbtTagCount; i++) {
                try {
                    // TODO: 05/02/2019 This hack is necessary because we keep the raw NBT tag. Try to remove it.
                    CompoundTag tag = NBTIO.read(stream, ByteOrder.LITTLE_ENDIAN, true);
                    // Hack for tool damage
                    if (tag.contains("Damage")) {
                        damage = tag.getInt("Damage");
                        tag.remove("Damage");
                    }
                    if (tag.contains("__DamageConflict__")) {
                        tag.put("Damage", tag.removeAndGet("__DamageConflict__"));
                    }
                    if (!tag.getAllTags().isEmpty()) {
                        nbt = NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN, false);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            setOffset(offset + (int) stream.position());
        }

        int canPlaceOnCount = this.getVarInt();
        if (canPlaceOnCount > 4096) {
            throw new RuntimeException("Too many CanPlaceOn blocks: " + canPlaceOnCount);
        }

        String[] canPlaceOn = new String[canPlaceOnCount];
        for (int i = 0; i < canPlaceOn.length; ++i) {
            canPlaceOn[i] = this.getString();
        }

        int canDestroyCount = this.getVarInt();
        if (canDestroyCount > 4096) {
            throw new RuntimeException("Too many CanDestroy blocks: " + canDestroyCount);
        }

        String[] canDestroy = new String[canDestroyCount];
        for (int i = 0; i < canDestroy.length; ++i) {
            canDestroy[i] = this.getString();
        }

        if (id == ItemID.SHIELD && protocol >= ProtocolInfo.v1_11_0) {
            this.getVarLong();
        }

        try {
            CompoundTag compoundTag;
            if (nbt.length > 0 && (compoundTag = NBTIO.read(nbt, ByteOrder.LITTLE_ENDIAN)).contains("mv_origin_id") && compoundTag.contains("mv_origin_meta")) {
                Item item = Item.get(compoundTag.getInt("mv_origin_id"), compoundTag.getInt("mv_origin_meta"), cnt);
                if (compoundTag.contains("mv_origin_nbt")) {
                    item.setNamedTag(compoundTag.getCompound("mv_origin_nbt"));
                }
                return item;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        Item item = Item.get(id, damage, cnt, nbt);

        if (canDestroy.length > 0 || canPlaceOn.length > 0) {
            CompoundTag namedTag = item.getNamedTag();
            if (namedTag == null) {
                namedTag = new CompoundTag();
            }

            if (canDestroy.length > 0) {
                ListTag<StringTag> listTag = new ListTag<>("CanDestroy");
                for (String blockName : canDestroy) {
                    listTag.add(new StringTag("", blockName));
                }
                namedTag.put("CanDestroy", listTag);
            }

            if (canPlaceOn.length > 0) {
                ListTag<StringTag> listTag = new ListTag<>("CanPlaceOn");
                for (String blockName : canPlaceOn) {
                    listTag.add(new StringTag("", blockName));
                }
                namedTag.put("CanPlaceOn", listTag);
            }

            item.setNamedTag(namedTag);
        }

        return item;
    }

    private Item getSlotNew(int protocol) {
        int runtimeId = this.getVarInt();
        if (runtimeId == 0) {
            return Item.get(Item.AIR, 0, 0);
        }

        int count = this.getLShort();
        int damage = (int) this.getUnsignedVarInt();

        RuntimeItemMapping mapping = RuntimeItems.getMapping(protocol);
        LegacyEntry legacyEntry = mapping.fromRuntime(runtimeId);

        int id = legacyEntry.getLegacyId();
        if (legacyEntry.isHasDamage()) {
            damage = legacyEntry.getDamage();
        }

        if (this.getBoolean()) { // hasNetId
            this.getVarInt(); // netId
        }

        int blockRuntimeId = this.getVarInt();
        if (protocol >= ProtocolInfo.v1_19_0_31 && id < 256 && id != 166 && !(id == -212 && legacyEntry.getDamage() == 0) && !legacyEntry.isHasDamage() && (protocol < ProtocolInfo.v1_21_30 || id == BlockID.RED_MUSHROOM_BLOCK || id == BlockID.BROWN_MUSHROOM_BLOCK)) { // ItemBlock
            int fullId = GlobalBlockPalette.getLegacyFullId(protocol, blockRuntimeId);
            if (fullId != -1) {
                damage = fullId & 0x3f;
            }
        }

        byte[] bytes = this.getByteArray();
        ByteBuf buf = ByteBufAllocator.DEFAULT.ioBuffer(bytes.length);
        buf.writeBytes(bytes);

        byte[] nbt = new byte[0];
        String[] canPlace;
        String[] canBreak;

        try (LittleEndianByteBufInputStream stream = new LittleEndianByteBufInputStream(buf)) {
            int nbtSize = stream.readShort();

            CompoundTag compoundTag = null;
            if (nbtSize > 0) {
                compoundTag = NBTIO.read(stream, ByteOrder.LITTLE_ENDIAN);
            } else if (nbtSize == -1) {
                int tagCount = stream.readUnsignedByte();
                if (tagCount != 1) throw new IllegalArgumentException("Expected 1 tag but got " + tagCount);
                compoundTag = NBTIO.read(stream, ByteOrder.LITTLE_ENDIAN);
            }

            if (compoundTag != null && !compoundTag.getAllTags().isEmpty()) {
                if (!legacyEntry.isHasDamage() && compoundTag.contains("Damage")) {
                    damage = compoundTag.getInt("Damage");
                    compoundTag.remove("Damage");
                }
                if (compoundTag.contains("__DamageConflict__")) {
                    compoundTag.put("Damage", compoundTag.removeAndGet("__DamageConflict__"));
                }
                if (!compoundTag.isEmpty()) {
                    nbt = NBTIO.write(compoundTag, ByteOrder.LITTLE_ENDIAN);
                }
            }

            int canPlaceCount = stream.readInt();
            if (canPlaceCount > 4096) {
                throw new RuntimeException("Too many CanPlaceOn blocks: " + canPlaceCount);
            }

            canPlace = new String[canPlaceCount];
            for (int i = 0; i < canPlace.length; i++) {
                canPlace[i] = stream.readUTF(1024);
            }

            int canBreakCount = stream.readInt();
            if (canBreakCount > 4096) {
                throw new RuntimeException("Too many CanDestroy blocks: " + canBreakCount);
            }

            canBreak = new String[canBreakCount];
            for (int i = 0; i < canBreak.length; i++) {
                canBreak[i] = stream.readUTF(1024);
            }

            if (id == ItemID.SHIELD) {
                stream.readLong();
            }

            if (compoundTag != null && compoundTag.contains("mv_origin_id") && compoundTag.contains("mv_origin_meta")) {
                Item item = Item.get(compoundTag.getInt("mv_origin_id"), compoundTag.getInt("mv_origin_meta"), count);
                if (compoundTag.contains("mv_origin_nbt")) {
                    item.setNamedTag(compoundTag.getCompound("mv_origin_nbt"));
                }
                return item;
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read item user data", e);
        } finally {
            buf.release();
        }

        Item item = Item.get(id, damage, count, nbt);

        if (canBreak.length > 0 || canPlace.length > 0) {
            CompoundTag namedTag = item.getNamedTag();
            if (namedTag == null) {
                namedTag = new CompoundTag();
            }

            if (canBreak.length > 0) {
                ListTag<StringTag> listTag = new ListTag<>("CanDestroy");
                for (String blockName : canBreak) {
                    listTag.add(new StringTag("", blockName));
                }
                namedTag.put("CanDestroy", listTag);
            }

            if (canPlace.length > 0) {
                ListTag<StringTag> listTag = new ListTag<>("CanPlaceOn");
                for (String blockName : canPlace) {
                    listTag.add(new StringTag("", blockName));
                }
                namedTag.put("CanPlaceOn", listTag);
            }

            item.setNamedTag(namedTag);
        }

        return item;
    }

    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = buffer.length;
        int newCapacity = oldCapacity << 1;

        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }

        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            newCapacity = hugeCapacity(minCapacity);
        }
        this.buffer = Arrays.copyOf(buffer, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) { // overflow
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }

    public void put(byte[] bytes) {
        this.ensureCapacity(this.count + bytes.length);

        System.arraycopy(bytes, 0, this.buffer, this.count, bytes.length);
        this.count += bytes.length;
    }

    public <T> void putArray(Collection<T> array, BiConsumer<BinaryStream, T> biConsumer) {
        this.putUnsignedVarInt(array.size());
        for (T val : array) {
            biConsumer.accept(this, val);
        }
    }

    /**
     * Writes a list of Attributes to the packet buffer using the standard format.
     */
    public void putAttributeList(Attribute[] attributes) {
        this.putUnsignedVarInt(attributes.length);
        for (Attribute attribute : attributes) {
            this.putString(attribute.getName());
            this.putLFloat(attribute.getMinValue());
            this.putLFloat(attribute.getValue());
            this.putLFloat(attribute.getMaxValue());
        }
    }

    public void putBlockFace(BlockFace face) {
        this.putVarInt(face.getIndex());
    }

    public void putBlockVector3(BlockVector3 v) {
        Server.mvw("BinaryStream#putBlockVector3(BlockVector3)");
        putBlockVector3(ProtocolInfo.CURRENT_PROTOCOL, v.x, v.y, v.z);
    }

    public void putBlockVector3(int protocol, BlockVector3 v) {
        putBlockVector3(protocol, v.x, v.y, v.z);
    }

    public void putBlockVector3(int x, int y, int z) {
        Server.mvw("BinaryStream#putBlockVector3(int, int, int)");
        putBlockVector3(ProtocolInfo.CURRENT_PROTOCOL, x, y, z);
    }

    public void putBlockVector3(int protocol, int x, int y, int z) {
        putVarInt(x);
        if (protocol >= ProtocolInfo.v1_26_10) {
            putVarInt(y);
        } else {
            putUnsignedVarInt(Integer.toUnsignedLong(y));
        }
        putVarInt(z);
    }

    public void putBoolean(boolean bool) {
        this.putByte((byte) (bool ? 1 : 0));
    }

    public void putByte(byte b) {
        this.put(new byte[]{b});
    }

    public void putByteArray(byte[] b) {
        this.putUnsignedVarInt(b.length);
        this.put(b);
    }

    public void putEntityLink(EntityLink link) {
        Server.mvw("BinaryStream#putEntityLink(EntityLink)");
        this.putEntityLink(ProtocolInfo.CURRENT_PROTOCOL, link);
    }

    public void putEntityLink(int protocol, EntityLink link) {
        putEntityUniqueId(link.fromEntityUniquieId);
        putEntityUniqueId(link.toEntityUniquieId);
        putByte(link.type);
        putBoolean(link.immediate);
        if (protocol >= 407) {
            putBoolean(link.riderInitiated);
            if (protocol >= ProtocolInfo.v1_21_20) {
                putLFloat(link.vehicleAngularVelocity);
            }
        }
    }

    /**
     * Writes an EntityUniqueID
     */
    public void putEntityRuntimeId(long eid) {
        this.putUnsignedVarLong(eid);
    }

    /**
     * Writes an EntityUniqueID
     */
    public void putEntityUniqueId(long eid) {
        this.putVarLong(eid);
    }

    public void putExperiments(Collection<ExperimentData> experiments) {
        this.putLInt(experiments.size());
        for (ExperimentData experimentData : experiments) {
            this.putString(experimentData.getName());
            this.putBoolean(experimentData.isEnabled());
        }
        this.putBoolean(!experiments.isEmpty());
    }

    public void putFloat(float v) {
        this.put(Binary.writeFloat(v));
    }

    public void putGameRules(GameRules gameRules, boolean startGame) {
        Server.mvw("BinaryStream#putGameRules(GameRules, boolean)");
        this.putGameRules(ProtocolInfo.CURRENT_PROTOCOL, gameRules, startGame);
    }

    public void putGameRules(int protocol, GameRules gameRules, boolean startGame) {
        Map<GameRule, GameRules.Value> allGameRules = gameRules.getGameRules();
        Map<GameRule, GameRules.Value> rulesToSend = new HashMap<>();
        allGameRules.forEach((gameRule, value) -> {
            if (protocol > value.getMinProtocol()) {
                rulesToSend.put(gameRule, value);
            }
        });
        this.putUnsignedVarInt(rulesToSend.size());
        rulesToSend.forEach((gameRule, value) -> {
            putString(gameRule.getName().toLowerCase(Locale.ROOT));
            value.write(protocol, this, startGame);
        });
    }

    public void putGameRulesMap(int protocol, Map<GameRule, cn.nukkit.level.GameRules.Value> allGameRules, boolean startGame) {
        Map<GameRule, GameRules.Value> rulesToSend = new HashMap<>();
        allGameRules.forEach((gameRule, value) -> {
            if (protocol > value.getMinProtocol()) {
                if (gameRule == GameRule.NATURAL_REGENERATION) {
                    rulesToSend.put(gameRule, new GameRules.Value<>(GameRules.Type.BOOLEAN, false)); // Fix client-side desync?
                } else {
                    rulesToSend.put(gameRule, value);
                }
            }
        });
        this.putUnsignedVarInt(rulesToSend.size());
        rulesToSend.forEach((gameRule, value) -> {
            putString(gameRule.getName().toLowerCase(Locale.ROOT));
            value.write(protocol, this, startGame);
        });
    }

    public void putImage(SerializedImage image) {
        this.putLInt(image.width);
        this.putLInt(image.height);
        this.putByteArray(image.data);
    }

    public void putInt(int i) {
        this.put(Binary.writeInt(i));
    }

    public void putLFloat(float v) {
        this.put(Binary.writeLFloat(v));
    }

    public void putLInt(int i) {
        this.put(Binary.writeLInt(i));
    }

    public void putLLong(long l) {
        this.put(Binary.writeLLong(l));
    }

    public void putLShort(int s) {
        this.put(Binary.writeLShort(s));
    }

    public void putLTriad(int triad) {
        this.put(Binary.writeLTriad(triad));
    }

    public void putLong(long l) {
        this.put(Binary.writeLong(l));
    }

    public <T> void putNbtTag(T tag) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try (NBTOutputStream writer = NbtUtils.createNetworkWriter(stream)) {
            writer.writeTag(tag);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.put(stream.toByteArray());
    }

    public void putNetworkItemStackDescriptor(int protocol, Item item) {
        if (protocol < ProtocolInfo.v1_26_20_26) {
            putSlot(protocol, item);
            return;
        }

        if (item == null) {
            item = Item.get(Item.AIR);
        }

        Item alternate = null;
        if (item.getId() != Item.AIR) {
            if (item instanceof ItemBlock && protocol < item.getBlockUnsafe().getMinimumVersion()) {
                alternate = Item.get(item.getBlockUnsafe().getAlternateBlock(protocol), item.getBlockUnsafe().getAlternateMeta(protocol), item.getCount());
            } else if (!item.isSupportedOn(protocol)) {
                alternate = Item.get(Item.INFO_UPDATE, 0, item.getCount());
            }

            if (alternate != null) {
                Item original = item;
                item = alternate;

                CompoundTag originalNBT = original.getNamedTag();
                if (originalNBT != null) {
                    item.setNamedTag(new CompoundTag().putCompound("mv_origin_nbt", originalNBT));
                }
                item.setCustomName("§r§f" + original.getName());
                item.setNamedTag(item.getNamedTag().putInt("mv_origin_id", original.getId()).putInt("mv_origin_meta", original.getDamage()));
            }
        }

        int id = item.getId();
        int meta = item.getDamage();
        boolean isBlock = item instanceof ItemBlock;
        boolean isDurable = item instanceof ItemDurable;

        RuntimeEntry runtimeEntry = null;
        if (id != Item.AIR) {
            runtimeEntry = RuntimeItems.getMapping(protocol).toRuntime(id, meta);
        }

        int runtimeId = runtimeEntry == null ? 0 : runtimeEntry.getRuntimeId();
        int damage = isBlock || isDurable || runtimeEntry == null || runtimeEntry.isHasDamage() ? 0 : meta;

        this.putLShort(runtimeId);
        this.putLShort(item.getCount());
        this.putUnsignedVarInt(damage);

        boolean hasNetId = id != Item.AIR;
        this.putBoolean(hasNetId); // hasNetId
        if (hasNetId) {
            this.putUnsignedVarInt(0); // netIdVariant ItemStackNetId
            this.putVarInt(1); // netId 1 = Item is present
        }

        Block block = isBlock && id != Item.AIR ? item.getBlockUnsafe() : null;
        int blockRuntimeId = block == null ? 0 : GlobalBlockPalette.getOrCreateRuntimeId(protocol, block.getId(), block.getDamage());
        this.putUnsignedVarInt(blockRuntimeId);

        if (id == Item.AIR) {
            this.putUnsignedVarInt(0); // No user date
            return;
        }

        ByteBuf userDataBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        try (LittleEndianByteBufOutputStream stream = new LittleEndianByteBufOutputStream(userDataBuf)) {
            if (isDurable && runtimeEntry != null && !runtimeEntry.isHasDamage()) {
                byte[] nbt = item.getCompoundTag();
                CompoundTag tag;
                if (nbt == null || nbt.length == 0) {
                    tag = new CompoundTag();
                } else {
                    tag = NBTIO.read(nbt, ByteOrder.LITTLE_ENDIAN);
                }
                if (tag.contains("Damage")) {
                    tag.put("__DamageConflict__", tag.removeAndGet("Damage"));
                }
                tag.putInt("Damage", meta);
                stream.writeShort(-1);
                stream.writeByte(1); // Hardcoded in current version
                stream.write(NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN));
            } else if (item.hasCompoundTag()) {
                stream.writeShort(-1);
                stream.writeByte(1); // Hardcoded in current version
                stream.write(item.getCompoundTag());
            } else {
                userDataBuf.writeShortLE(0);
            }

            List<String> canPlaceOn = extractStringList(item, "CanPlaceOn");
            stream.writeInt(canPlaceOn.size());
            for (String string : canPlaceOn) {
                stream.writeUTF(string);
            }

            List<String> canDestroy = extractStringList(item, "CanDestroy");
            stream.writeInt(canDestroy.size());
            for (String string : canDestroy) {
                stream.writeUTF(string);
            }

            if (id == ItemID.SHIELD) {
                stream.writeLong(0);
            }

            byte[] bytes = new byte[userDataBuf.readableBytes()];
            userDataBuf.readBytes(bytes);
            putByteArray(bytes);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to write item user data", e);
        } finally {
            userDataBuf.release();
        }
    }

    public <T> void putOptionalNull(T object, BiConsumer<BinaryStream, T> consumer) {
        if (object != null) {
            this.putBoolean(true);
            consumer.accept(this, object);
        } else {
            this.putBoolean(false);
        }
    }

    public void putRecipeIngredient(int protocol, Item item) {
        if (item == null || item.getId() == Item.AIR) {
            if (protocol >= ProtocolInfo.v1_19_30_23) {
                this.putBoolean(false); // isValid? - false
                this.putVarInt(0); // item == null ? 0 : item.getCount()
            } else {
                this.putVarInt(0);
            }
            return;
        }

        if (protocol >= ProtocolInfo.v1_19_30_23) {
            this.putBoolean(true); // isValid? - true
        }

        int runtimeId = item.getId();
        int damage = item.hasMeta() ? item.getDamage() : 0x7fff;

        if (protocol >= ProtocolInfo.v1_16_100) {
            RuntimeItemMapping mapping = RuntimeItems.getMapping(protocol);
            if (!item.hasMeta()) {
                RuntimeEntry runtimeEntry = mapping.toRuntime(item.getId(), 0);
                runtimeId = runtimeEntry.getRuntimeId();
                damage = 0x7fff;
            } else {
                RuntimeEntry runtimeEntry = mapping.toRuntime(item.getId(), item.getDamage());
                runtimeId = runtimeEntry.getRuntimeId();
                damage = runtimeEntry.isHasDamage() ? 0 : item.getDamage();
            }
        }

        if (protocol >= ProtocolInfo.v1_19_30_23) {
            this.putLShort(runtimeId);
            this.putLShort(damage);
        } else {
            this.putVarInt(runtimeId);
            this.putVarInt(damage);
        }
        this.putVarInt(item.getCount());
    }

    public void putShort(int s) {
        this.put(Binary.writeShort(s));
    }

    public void putSignedBlockPosition(BlockVector3 v) {
        putVarInt(v.x);
        putVarInt(v.y);
        putVarInt(v.z);
    }

    public void putSkin(Skin skin) {
        Server.mvw("BinaryStream#putSkin(Skin)");
        this.putSkin(ProtocolInfo.CURRENT_PROTOCOL, skin);
    }

    public void putSkin(int protocol, Skin skin) {
        this.putString(skin.getSkinId());

        if (protocol < ProtocolInfo.v1_13_0) {
            if (skin.isPersona()) { // Hack: Replace persona skins with steve skins for < 1.13 players to avoid invisible skins
                this.putByteArray(steveSkinDecoded != null ? steveSkinDecoded : (steveSkinDecoded = Base64.getDecoder().decode(Skin.STEVE_SKIN)));
                if (protocol >= ProtocolInfo.v1_2_13) {
                    this.putByteArray(skin.getCapeData().data);
                }
                this.putString("geometry.humanoid.custom");
                this.putString(Skin.STEVE_GEOMETRY);
            } else {
                this.putByteArray(skin.getSkinData().data);
                if (protocol >= ProtocolInfo.v1_2_13) {
                    this.putByteArray(skin.getCapeData().data);
                }
                this.putString(skin.isLegacySlim ? "geometry.humanoid.customSlim" : "geometry.humanoid.custom");
                this.putString(skin.getGeometryData());
            }
        } else {
            if (protocol >= ProtocolInfo.v1_16_210) {
                this.putString(skin.getPlayFabId());
            }
            this.putString(skin.getSkinResourcePatch());
            this.putImage(skin.getSkinData());

            List<SkinAnimation> animations = skin.getAnimations();
            this.putLInt(animations.size());
            for (SkinAnimation animation : animations) {
                this.putImage(animation.image);
                this.putLInt(animation.type);
                this.putLFloat(animation.frames);
                if (protocol >= ProtocolInfo.v1_16_100) {
                    this.putLInt(animation.expression);
                }
            }

            this.putImage(skin.getCapeData());
            this.putString(skin.getGeometryData());
            if (protocol >= ProtocolInfo.v1_17_30) {
                this.putString(skin.getGeometryDataEngineVersion());
            }
            this.putString(skin.getAnimationData());
            if (protocol < ProtocolInfo.v1_17_30) {
                this.putBoolean(skin.isPremium());
                this.putBoolean(skin.isPersona());
                this.putBoolean(skin.isCapeOnClassic());
            }
            this.putString(skin.getCapeId());
            this.putString(skin.getFullSkinId());
            if (protocol >= ProtocolInfo.v1_14_60) {
                this.putString(skin.getArmSize());
                this.putString(skin.getSkinColor());

                List<PersonaPiece> pieces = skin.getPersonaPieces();
                this.putLInt(pieces.size());
                for (PersonaPiece piece : pieces) {
                    this.putString(piece.id);
                    this.putString(piece.type);
                    this.putString(piece.packId);
                    this.putBoolean(piece.isDefault);
                    this.putString(piece.productId);
                }

                List<PersonaPieceTint> tints = skin.getTintColors();
                this.putLInt(tints.size());
                for (PersonaPieceTint tint : tints) {
                    this.putString(tint.pieceType);
                    List<String> colors = tint.colors;
                    this.putLInt(colors.size());
                    for (String color : colors) {
                        this.putString(color);
                    }
                }

                if (protocol >= ProtocolInfo.v1_17_30) {
                    this.putBoolean(skin.isPremium());
                    this.putBoolean(skin.isPersona());
                    this.putBoolean(skin.isCapeOnClassic());
                    this.putBoolean(skin.isPrimaryUser());
                    if (protocol >= ProtocolInfo.v1_19_63) {
                        this.putBoolean(skin.isOverridingPlayerAppearance());
                    }
                }
            }
        }
    }

    public void putSlot(Item item) {
        Server.mvw("BinaryStream#putSlot(Item)");
        this.putSlot(ProtocolInfo.CURRENT_PROTOCOL, item);
    }

    public void putSlot(int protocol, Item item) {
        this.putSlot(protocol, item, false);
    }

    public void putSlot(int protocol, Item item, boolean instanceItem) {
        if (protocol >= ProtocolInfo.v1_19_0_31) {
            this.putSlot_1_19_0(protocol, item, instanceItem);
        } else if (protocol >= ProtocolInfo.v1_16_220) {
            this.putSlot_1_16_220(protocol, item, instanceItem);
        } else {
            this.putSlotLegacy(protocol, item, instanceItem);
        }
    }

    private void putSlotLegacy(int protocol, Item item, boolean instanceItem) {
        if (item == null || item.getId() == Item.AIR) {
            this.putVarInt(0);
            return;
        }

        Item alternate = null;
        if (item instanceof ItemBlock && protocol < item.getBlockUnsafe().getMinimumVersion()) {
            alternate = Item.get(item.getBlockUnsafe().getAlternateBlock(protocol), item.getBlockUnsafe().getAlternateMeta(protocol), item.getCount());
        } else if (!item.isSupportedOn(protocol)) {
            alternate = Item.get(Item.INFO_UPDATE, 0, item.getCount());
        }

        if (alternate != null) {
            Item original = item;
            item = alternate;

            CompoundTag originalNBT = original.getNamedTag();
            if (originalNBT != null) {
                item.setNamedTag(new CompoundTag().putCompound("mv_origin_nbt", originalNBT));
            }
            item.setCustomName("§r§f" + original.getName());
            item.setNamedTag(item.getNamedTag().putInt("mv_origin_id", original.getId()).putInt("mv_origin_meta", original.getDamage()));
        }

        int runtimeId = item.getId();
        int damage = item.hasMeta() ? item.getDamage() : -1;

        if (protocol >= ProtocolInfo.v1_16_100) {
            RuntimeItemMapping mapping = RuntimeItems.getMapping(protocol);
            RuntimeEntry runtimeEntry = mapping.toRuntime(item.getId(), item.getDamage());
            runtimeId = runtimeEntry.getRuntimeId();
            damage = runtimeEntry.isHasDamage() ? 0 : item.getDamage();
        }

        this.putVarInt(runtimeId);

        int auxValue;
        boolean isDurable = item instanceof ItemDurable;

        if (protocol >= ProtocolInfo.v1_12_0) {
            auxValue = item.getCount();
            if (!isDurable) {
                int meta;
                if (protocol < ProtocolInfo.v1_16_100) {
                    meta = item.hasMeta() ? item.getDamage() : -1;
                } else {
                    meta = damage;
                }
                auxValue |= ((meta & 0x7fff) << 8);
            }
        } else {
            auxValue = (((item.hasMeta() ? item.getDamage() : -1) & 0x7fff) << 8) | item.getCount();
        }

        this.putVarInt(auxValue);

        // Hack: fix recipe list not displaying some items
        if (instanceItem) {
            this.putLShort(0);
            this.putVarInt(0);
            this.putVarInt(0);
            if (item.getId() == ItemID.SHIELD && protocol >= ProtocolInfo.v1_11_0) {
                this.putVarLong(0);
            }
            return;
        }

        if (item.hasCompoundTag() || (isDurable && protocol >= ProtocolInfo.v1_12_0)) {
            if (protocol < ProtocolInfo.v1_12_0) {
                byte[] nbt = item.getCompoundTag();
                this.putLShort(nbt.length);
                this.put(nbt);
            } else {
                try {
                    // Hack for tool damage
                    byte[] nbt = item.getCompoundTag();
                    CompoundTag tag;
                    if (nbt == null || nbt.length == 0) {
                        tag = new CompoundTag();
                    } else {
                        tag = NBTIO.read(nbt, ByteOrder.LITTLE_ENDIAN, false);
                    }
                    if (tag.contains("Damage")) {
                        tag.put("__DamageConflict__", tag.removeAndGet("Damage"));
                    }
                    if (isDurable) {
                        tag.putInt("Damage", item.getDamage());
                    }

                    this.putLShort(0xffff);
                    this.putByte((byte) 1);
                    this.put(NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN, true));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            this.putLShort(0);
        }
        List<String> canPlaceOn = extractStringList(item, "CanPlaceOn");
        List<String> canDestroy = extractStringList(item, "CanDestroy");
        this.putVarInt(canPlaceOn.size());
        for (String block : canPlaceOn) {
            this.putString(block);
        }
        this.putVarInt(canDestroy.size());
        for (String block : canDestroy) {
            this.putString(block);
        }

        if (item.getId() == ItemID.SHIELD && protocol >= ProtocolInfo.v1_11_0) {
            this.putVarLong(0); //"blocking tick" (ffs mojang)
        }
    }

    private void putSlot_1_16_220(int protocol, Item item, boolean instanceItem) {
        if (item == null || item.getId() == Item.AIR) {
            this.putByte((byte) 0);
            return;
        }

        Item alternate = null;
        if (item instanceof ItemBlock && protocol < item.getBlockUnsafe().getMinimumVersion()) {
            alternate = Item.get(item.getBlockUnsafe().getAlternateBlock(protocol), item.getBlockUnsafe().getAlternateMeta(protocol), item.getCount());
        } else if (!item.isSupportedOn(protocol)) {
            alternate = Item.get(Item.INFO_UPDATE, 0, item.getCount());
        }

        if (alternate != null) {
            Item original = item;
            item = alternate;

            CompoundTag originalNBT = original.getNamedTag();
            if (originalNBT != null) {
                item.setNamedTag(new CompoundTag().putCompound("mv_origin_nbt", originalNBT));
            }
            item.setCustomName("§r§f" + original.getName());
            item.setNamedTag(item.getNamedTag().putInt("mv_origin_id", original.getId()).putInt("mv_origin_meta", original.getDamage()));
        }

        RuntimeItemMapping mapping = RuntimeItems.getMapping(protocol);
        RuntimeEntry runtimeEntry = mapping.toRuntime(item.getId(), item.getDamage());
        int runtimeId = runtimeEntry.getRuntimeId();
        int damage = runtimeEntry.isHasDamage() ? 0 : item.getDamage();

        this.putVarInt(runtimeId);
        this.putLShort(item.getCount());
        this.putUnsignedVarInt(damage);

        if (!instanceItem) {
            this.putBoolean(true);
            this.putVarInt(1); // Item is present
        }

        Block block = item.getBlockUnsafe();
        int blockRuntimeId = block == null ? 0 : GlobalBlockPalette.getOrCreateRuntimeId(protocol, block.getId(), block.getDamage());
        this.putVarInt(blockRuntimeId);

        ByteBuf userDataBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        try (LittleEndianByteBufOutputStream stream = new LittleEndianByteBufOutputStream(userDataBuf)) {
            if (((item instanceof ItemDurable && item.getDamage() > 0) || block != null && block.getDamage() > 0) && !runtimeEntry.isHasDamage()) {
                byte[] nbt = item.getCompoundTag();
                CompoundTag tag;
                if (nbt == null || nbt.length == 0) {
                    tag = new CompoundTag();
                } else {
                    tag = NBTIO.read(nbt, ByteOrder.LITTLE_ENDIAN);
                }
                if (tag.contains("Damage")) {
                    tag.put("__DamageConflict__", tag.removeAndGet("Damage"));
                }
                tag.putInt("Damage", item.getDamage());
                stream.writeShort(-1);
                stream.writeByte(1); // Hardcoded in current version
                stream.write(NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN));
            } else if (item.hasCompoundTag()) {
                stream.writeShort(-1);
                stream.writeByte(1); // Hardcoded in current version
                stream.write(item.getCompoundTag());
            } else {
                userDataBuf.writeShortLE(0);
            }

            List<String> canPlaceOn = extractStringList(item, "CanPlaceOn");
            stream.writeInt(canPlaceOn.size());
            for (String string : canPlaceOn) {
                stream.writeUTF(string);
            }

            List<String> canDestroy = extractStringList(item, "CanDestroy");
            stream.writeInt(canDestroy.size());
            for (String string : canDestroy) {
                stream.writeUTF(string);
            }

            if (item.getId() == ItemID.SHIELD) {
                stream.writeLong(0);
            }

            byte[] bytes = new byte[userDataBuf.readableBytes()];
            userDataBuf.readBytes(bytes);
            putByteArray(bytes);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to write item user data", e);
        } finally {
            userDataBuf.release();
        }
    }

    private void putSlot_1_19_0(int protocol, Item item, boolean instanceItem) {
        if (item == null || item.getId() == Item.AIR) {
            this.putByte((byte) 0);
            return;
        }

        Item alternate = null;
        if (item instanceof ItemBlock && protocol < item.getBlockUnsafe().getMinimumVersion()) {
            alternate = Item.get(item.getBlockUnsafe().getAlternateBlock(protocol), item.getBlockUnsafe().getAlternateMeta(protocol), item.getCount());
        } else if (!item.isSupportedOn(protocol)) {
            alternate = Item.get(Item.INFO_UPDATE, 0, item.getCount());
        }

        if (alternate != null) {
            Item original = item;
            item = alternate;

            CompoundTag originalNBT = original.getNamedTag();
            if (originalNBT != null) {
                item.setNamedTag(new CompoundTag().putCompound("mv_origin_nbt", originalNBT));
            }
            item.setCustomName("§r§f" + original.getName());
            item.setNamedTag(item.getNamedTag().putInt("mv_origin_id", original.getId()).putInt("mv_origin_meta", original.getDamage()));
        }

        int id = item.getId();
        int meta = item.getDamage();
        boolean isBlock = item instanceof ItemBlock;
        boolean isDurable = item instanceof ItemDurable;

        RuntimeItemMapping mapping = RuntimeItems.getMapping(protocol);
        RuntimeEntry runtimeEntry = mapping.toRuntime(id, meta);
        int runtimeId = runtimeEntry.getRuntimeId();
        int damage = isBlock || isDurable || runtimeEntry.isHasDamage() ? 0 : meta;

        this.putVarInt(runtimeId);
        this.putLShort(item.getCount());
        this.putUnsignedVarInt(damage);

        if (!instanceItem) {
            this.putBoolean(true);
            this.putVarInt(1); // Item is present
        }

        Block block = isBlock ? item.getBlockUnsafe() : null;
        int blockRuntimeId = block == null ? 0 : GlobalBlockPalette.getOrCreateRuntimeId(protocol, block.getId(), block.getDamage());
        this.putVarInt(blockRuntimeId);

        ByteBuf userDataBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        try (LittleEndianByteBufOutputStream stream = new LittleEndianByteBufOutputStream(userDataBuf)) {
            if (!instanceItem && isDurable && !runtimeEntry.isHasDamage()) {
                byte[] nbt = item.getCompoundTag();
                CompoundTag tag;
                if (nbt == null || nbt.length == 0) {
                    tag = new CompoundTag();
                } else {
                    tag = NBTIO.read(nbt, ByteOrder.LITTLE_ENDIAN);
                }
                if (tag.contains("Damage")) {
                    tag.put("__DamageConflict__", tag.removeAndGet("Damage"));
                }
                tag.putInt("Damage", meta);
                stream.writeShort(-1);
                stream.writeByte(1); // Hardcoded in current version
                stream.write(NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN));
            } else if (item.hasCompoundTag()) {
                stream.writeShort(-1);
                stream.writeByte(1); // Hardcoded in current version
                stream.write(item.getCompoundTag());
            } else {
                userDataBuf.writeShortLE(0);
            }

            List<String> canPlaceOn = extractStringList(item, "CanPlaceOn");
            stream.writeInt(canPlaceOn.size());
            for (String string : canPlaceOn) {
                stream.writeUTF(string);
            }

            List<String> canDestroy = extractStringList(item, "CanDestroy");
            stream.writeInt(canDestroy.size());
            for (String string : canDestroy) {
                stream.writeUTF(string);
            }

            if (id == ItemID.SHIELD) {
                stream.writeLong(0);
            }

            byte[] bytes = new byte[userDataBuf.readableBytes()];
            userDataBuf.readBytes(bytes);
            putByteArray(bytes);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to write item user data", e);
        } finally {
            userDataBuf.release();
        }
    }

    public void putString(String string) {
        byte[] b = string.getBytes(StandardCharsets.UTF_8);
        this.putByteArray(b);
    }

    public void putTriad(int triad) {
        this.put(Binary.writeTriad(triad));
    }

    public void putUUID(UUID uuid) {
        this.put(Binary.writeUUID(uuid));
    }

    public void putUnsignedVarInt(long v) {
        VarInt.writeUnsignedVarInt(this, v);
    }

    public void putUnsignedVarLong(long v) {
        VarInt.writeUnsignedVarLong(this, v);
    }

    public void putVarInt(int v) {
        VarInt.writeVarInt(this, v);
    }

    public void putVarLong(long v) {
        VarInt.writeVarLong(this, v);
    }

    public void putVector3f(Vector3f v) {
        this.putVector3f(v.x, v.y, v.z);
    }

    public void putVector3f(float x, float y, float z) {
        this.putLFloat(x);
        this.putLFloat(y);
        this.putLFloat(z);
    }

    public BinaryStream reset() {
        this.offset = 0;
        this.count = 0;
        return this;
    }

    public void setBuffer(byte[] buffer, int offset) {
        this.setBuffer(buffer);
        this.setOffset(offset);
    }
}
