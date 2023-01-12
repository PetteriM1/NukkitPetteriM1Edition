/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemDurable;
import cn.nukkit.item.RuntimeItemMapping;
import cn.nukkit.item.RuntimeItems;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.GameRules;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector3f;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.network.LittleEndianByteBufInputStream;
import cn.nukkit.network.LittleEndianByteBufOutputStream;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.network.protocol.types.EntityLink;
import cn.nukkit.utils.Binary;
import cn.nukkit.utils.PersonaPiece;
import cn.nukkit.utils.PersonaPieceTint;
import cn.nukkit.utils.SerializedImage;
import cn.nukkit.utils.SkinAnimation;
import cn.nukkit.utils.VarInt;
import com.google.common.collect.ImmutableList;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import it.unimi.dsi.fastutil.io.FastByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BinaryStream {
    public int offset;
    private byte[] c;
    protected int count;
    private static final int a = 0x7FFFFFF7;
    private static byte[] b;

    public BinaryStream() {
        this.c = new byte[32];
        this.offset = 0;
        this.count = 0;
    }

    public BinaryStream(byte[] byArray) {
        this(byArray, 0);
    }

    public BinaryStream(byte[] byArray, int n) {
        this.c = byArray;
        this.offset = n;
        this.count = byArray.length;
    }

    public BinaryStream reset() {
        this.offset = 0;
        this.count = 0;
        return this;
    }

    public void setBuffer(byte[] byArray) {
        this.c = byArray;
        this.count = byArray == null ? -1 : byArray.length;
    }

    public void setBuffer(byte[] byArray, int n) {
        this.setBuffer(byArray);
        this.setOffset(n);
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int n) {
        this.offset = n;
    }

    public byte[] getBuffer() {
        return Arrays.copyOf(this.c, this.count);
    }

    public int getCount() {
        return this.count;
    }

    public byte[] get() {
        return this.get(this.count - this.offset);
    }

    public byte[] get(int n) {
        if (n < 0) {
            this.offset = this.count - 1;
            return new byte[0];
        }
        n = Math.min(n, this.count - this.offset);
        this.offset += n;
        return Arrays.copyOfRange(this.c, this.offset - n, this.offset);
    }

    public void put(byte[] byArray) {
        this.d(this.count + byArray.length);
        System.arraycopy(byArray, 0, this.c, this.count, byArray.length);
        this.count += byArray.length;
    }

    public long getLong() {
        return Binary.readLong(this.get(8));
    }

    public void putLong(long l) {
        this.put(Binary.writeLong(l));
    }

    public int getInt() {
        return Binary.readInt(this.get(4));
    }

    public void putInt(int n) {
        this.put(Binary.writeInt(n));
    }

    public long getLLong() {
        return Binary.readLLong(this.get(8));
    }

    public void putLLong(long l) {
        this.put(Binary.writeLLong(l));
    }

    public int getLInt() {
        return Binary.readLInt(this.get(4));
    }

    public void putLInt(int n) {
        this.put(Binary.writeLInt(n));
    }

    public int getShort() {
        return Binary.readShort(this.get(2));
    }

    public void putShort(int n) {
        this.put(Binary.writeShort(n));
    }

    public int getLShort() {
        return Binary.readLShort(this.get(2));
    }

    public void putLShort(int n) {
        this.put(Binary.writeLShort(n));
    }

    public float getFloat() {
        return this.getFloat(-1);
    }

    public float getFloat(int n) {
        return Binary.readFloat(this.get(4), n);
    }

    public void putFloat(float f2) {
        this.put(Binary.writeFloat(f2));
    }

    public float getLFloat() {
        return this.getLFloat(-1);
    }

    public float getLFloat(int n) {
        return Binary.readLFloat(this.get(4), n);
    }

    public void putLFloat(float f2) {
        this.put(Binary.writeLFloat(f2));
    }

    public int getTriad() {
        return Binary.readTriad(this.get(3));
    }

    public void putTriad(int n) {
        this.put(Binary.writeTriad(n));
    }

    public int getLTriad() {
        return Binary.readLTriad(this.get(3));
    }

    public void putLTriad(int n) {
        this.put(Binary.writeLTriad(n));
    }

    public boolean getBoolean() {
        return this.getByte() == 1;
    }

    public void putBoolean(boolean bl) {
        this.putByte((byte)(bl ? 1 : 0));
    }

    public int getByte() {
        return this.c[this.offset++] & 0xFF;
    }

    public void putByte(byte by) {
        this.put(new byte[]{by});
    }

    public Attribute[] getAttributeList() throws Exception {
        ArrayList<Attribute> arrayList = new ArrayList<Attribute>();
        long l = this.getUnsignedVarInt();
        int n = 0;
        while ((long)n < l) {
            String string = this.getString();
            Attribute attribute = Attribute.getAttributeByName(string);
            if (attribute == null) {
                throw new Exception("Unknown attribute type \"" + string + '\"');
            }
            attribute.setMinValue(this.getLFloat());
            attribute.setValue(this.getLFloat());
            attribute.setMaxValue(this.getLFloat());
            arrayList.add(attribute);
            ++n;
        }
        return arrayList.toArray(new Attribute[0]);
    }

    public void putAttributeList(Attribute[] attributeArray) {
        this.putUnsignedVarInt(attributeArray.length);
        for (Attribute attribute : attributeArray) {
            this.putString(attribute.getName());
            this.putLFloat(attribute.getMinValue());
            this.putLFloat(attribute.getValue());
            this.putLFloat(attribute.getMaxValue());
        }
    }

    public void putUUID(UUID uUID) {
        this.put(Binary.writeUUID(uUID));
    }

    public UUID getUUID() {
        return Binary.readUUID(this.get(16));
    }

    public void putSkin(Skin skin) {
        Server.mvw("BinaryStream#putSkin(Skin)");
        this.putSkin(ProtocolInfo.CURRENT_PROTOCOL, skin);
    }

    public void putSkin(int n, Skin skin) {
        block13: {
            block12: {
                this.putString(skin.getSkinId());
                if (n >= 388) break block12;
                if (skin.isPersona()) {
                    this.putByteArray(b != null ? b : (b = Base64.getDecoder().decode("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ll9B/5ZfQf+WX0H/lm9b/6p9Zv+Wb1v/lm9b/4dVO/+WX0H/ll9B/5ZfQf+qfWb/lm9b/6p9Zv+Wb1v/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABWScz/VknM/1ZJzP9WScz/KCgo/ygoKP8oKCj/KCgo/wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKio/wDMzP8AzMz/AKio/1ExJf9qQDD/UTEl/2pAMP8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA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")));
                    if (n >= 223) {
                        this.putByteArray(skin.getCapeData().data);
                    }
                    this.putString("geometry.humanoid.custom");
                    this.putString("{\"geometry.humanoid\":{\"bones\":[{\"name\":\"body\",\"pivot\":[0,24,0],\"cubes\":[{\"origin\":[-4,12,-2],\"size\":[8,12,4],\"uv\":[16,16]}]},{\"name\":\"waist\",\"neverRender\":true,\"pivot\":[0,12,0]},{\"name\":\"head\",\"pivot\":[0,24,0],\"cubes\":[{\"origin\":[-4,24,-4],\"size\":[8,8,8],\"uv\":[0,0]}]},{\"name\":\"hat\",\"pivot\":[0,24,0],\"cubes\":[{\"origin\":[-4,24,-4],\"size\":[8,8,8],\"uv\":[32,0],\"inflate\":0.5}],\"neverRender\":true},{\"name\":\"rightArm\",\"pivot\":[-5,22,0],\"cubes\":[{\"origin\":[-8,12,-2],\"size\":[4,12,4],\"uv\":[40,16]}]},{\"name\":\"leftArm\",\"pivot\":[5,22,0],\"cubes\":[{\"origin\":[4,12,-2],\"size\":[4,12,4],\"uv\":[40,16]}],\"mirror\":true},{\"name\":\"rightLeg\",\"pivot\":[-1.9,12,0],\"cubes\":[{\"origin\":[-3.9,0,-2],\"size\":[4,12,4],\"uv\":[0,16]}]},{\"name\":\"leftLeg\",\"pivot\":[1.9,12,0],\"cubes\":[{\"origin\":[-0.1,0,-2],\"size\":[4,12,4],\"uv\":[0,16]}],\"mirror\":true}]},\"geometry.cape\":{\"texturewidth\":64,\"textureheight\":32,\"bones\":[{\"name\":\"cape\",\"pivot\":[0,24,-3],\"cubes\":[{\"origin\":[-5,8,-3],\"size\":[10,16,1],\"uv\":[0,0]}],\"material\":\"alpha\"}]},\"geometry.humanoid.custom:geometry.humanoid\":{\"bones\":[{\"name\":\"hat\",\"neverRender\":false,\"material\":\"alpha\",\"pivot\":[0,24,0]},{\"name\":\"leftArm\",\"reset\":true,\"mirror\":false,\"pivot\":[5,22,0],\"cubes\":[{\"origin\":[4,12,-2],\"size\":[4,12,4],\"uv\":[32,48]}]},{\"name\":\"rightArm\",\"reset\":true,\"pivot\":[-5,22,0],\"cubes\":[{\"origin\":[-8,12,-2],\"size\":[4,12,4],\"uv\":[40,16]}]},{\"name\":\"rightItem\",\"pivot\":[-6,15,1],\"neverRender\":true,\"parent\":\"rightArm\"},{\"name\":\"leftSleeve\",\"pivot\":[5,22,0],\"cubes\":[{\"origin\":[4,12,-2],\"size\":[4,12,4],\"uv\":[48,48],\"inflate\":0.25}],\"material\":\"alpha\"},{\"name\":\"rightSleeve\",\"pivot\":[-5,22,0],\"cubes\":[{\"origin\":[-8,12,-2],\"size\":[4,12,4],\"uv\":[40,32],\"inflate\":0.25}],\"material\":\"alpha\"},{\"name\":\"leftLeg\",\"reset\":true,\"mirror\":false,\"pivot\":[1.9,12,0],\"cubes\":[{\"origin\":[-0.1,0,-2],\"size\":[4,12,4],\"uv\":[16,48]}]},{\"name\":\"leftPants\",\"pivot\":[1.9,12,0],\"cubes\":[{\"origin\":[-0.1,0,-2],\"size\":[4,12,4],\"uv\":[0,48],\"inflate\":0.25}],\"pos\":[1.9,12,0],\"material\":\"alpha\"},{\"name\":\"rightPants\",\"pivot\":[-1.9,12,0],\"cubes\":[{\"origin\":[-3.9,0,-2],\"size\":[4,12,4],\"uv\":[0,32],\"inflate\":0.25}],\"pos\":[-1.9,12,0],\"material\":\"alpha\"},{\"name\":\"jacket\",\"pivot\":[0,24,0],\"cubes\":[{\"origin\":[-4,12,-2],\"size\":[8,12,4],\"uv\":[16,32],\"inflate\":0.25}],\"material\":\"alpha\"}]},\"geometry.humanoid.customSlim:geometry.humanoid\":{\"bones\":[{\"name\":\"hat\",\"neverRender\":false,\"material\":\"alpha\"},{\"name\":\"leftArm\",\"reset\":true,\"mirror\":false,\"pivot\":[5,21.5,0],\"cubes\":[{\"origin\":[4,11.5,-2],\"size\":[3,12,4],\"uv\":[32,48]}]},{\"name\":\"rightArm\",\"reset\":true,\"pivot\":[-5,21.5,0],\"cubes\":[{\"origin\":[-7,11.5,-2],\"size\":[3,12,4],\"uv\":[40,16]}]},{\"pivot\":[-6,14.5,1],\"neverRender\":true,\"name\":\"rightItem\",\"parent\":\"rightArm\"},{\"name\":\"leftSleeve\",\"pivot\":[5,21.5,0],\"cubes\":[{\"origin\":[4,11.5,-2],\"size\":[3,12,4],\"uv\":[48,48],\"inflate\":0.25}],\"material\":\"alpha\"},{\"name\":\"rightSleeve\",\"pivot\":[-5,21.5,0],\"cubes\":[{\"origin\":[-7,11.5,-2],\"size\":[3,12,4],\"uv\":[40,32],\"inflate\":0.25}],\"material\":\"alpha\"},{\"name\":\"leftLeg\",\"reset\":true,\"mirror\":false,\"pivot\":[1.9,12,0],\"cubes\":[{\"origin\":[-0.1,0,-2],\"size\":[4,12,4],\"uv\":[16,48]}]},{\"name\":\"leftPants\",\"pivot\":[1.9,12,0],\"cubes\":[{\"origin\":[-0.1,0,-2],\"size\":[4,12,4],\"uv\":[0,48],\"inflate\":0.25}],\"material\":\"alpha\"},{\"name\":\"rightPants\",\"pivot\":[-1.9,12,0],\"cubes\":[{\"origin\":[-3.9,0,-2],\"size\":[4,12,4],\"uv\":[0,32],\"inflate\":0.25}],\"material\":\"alpha\"},{\"name\":\"jacket\",\"pivot\":[0,24,0],\"cubes\":[{\"origin\":[-4,12,-2],\"size\":[8,12,4],\"uv\":[16,32],\"inflate\":0.25}],\"material\":\"alpha\"}]}}");
                } else {
                    this.putByteArray(skin.getSkinData().data);
                    if (n >= 223) {
                        this.putByteArray(skin.getCapeData().data);
                    }
                    this.putString(skin.isLegacySlim ? "geometry.humanoid.customSlim" : "geometry.humanoid.custom");
                    this.putString(skin.getGeometryData());
                }
                break block13;
            }
            if (n >= 428) {
                this.putString(skin.getPlayFabId());
            }
            this.putString(skin.getSkinResourcePatch());
            this.putImage(skin.getSkinData());
            List<SkinAnimation> list2 = skin.getAnimations();
            this.putLInt(list2.size());
            for (SkinAnimation list3 : list2) {
                this.putImage(list3.image);
                this.putLInt(list3.type);
                this.putLFloat(list3.frames);
                if (n < 419) continue;
                this.putLInt(list3.expression);
            }
            this.putImage(skin.getCapeData());
            this.putString(skin.getGeometryData());
            if (n >= 465) {
                this.putString(skin.getGeometryDataEngineVersion());
            }
            this.putString(skin.getAnimationData());
            if (n < 465) {
                this.putBoolean(skin.isPremium());
                this.putBoolean(skin.isPersona());
                this.putBoolean(skin.isCapeOnClassic());
            }
            this.putString(skin.getCapeId());
            this.putString(skin.getFullSkinId());
            if (n < 390) break block13;
            this.putString(skin.getArmSize());
            this.putString(skin.getSkinColor());
            List<PersonaPiece> list4 = skin.getPersonaPieces();
            this.putLInt(list4.size());
            Iterator iterator = list4.iterator();
            while (iterator.hasNext()) {
                PersonaPiece personaPiece = (PersonaPiece)iterator.next();
                this.putString(personaPiece.id);
                this.putString(personaPiece.type);
                this.putString(personaPiece.packId);
                this.putBoolean(personaPiece.isDefault);
                this.putString(personaPiece.productId);
            }
            List<PersonaPieceTint> list = skin.getTintColors();
            this.putLInt(list.size());
            for (PersonaPieceTint personaPieceTint : list) {
                this.putString(personaPieceTint.pieceType);
                ImmutableList<String> immutableList = personaPieceTint.colors;
                this.putLInt(immutableList.size());
                for (String string : immutableList) {
                    this.putString(string);
                }
            }
            if (n >= 465) {
                this.putBoolean(skin.isPremium());
                this.putBoolean(skin.isPersona());
                this.putBoolean(skin.isCapeOnClassic());
                this.putBoolean(skin.isPrimaryUser());
            }
        }
    }

    public void putImage(SerializedImage serializedImage) {
        this.putLInt(serializedImage.width);
        this.putLInt(serializedImage.height);
        this.putByteArray(serializedImage.data);
    }

    public SerializedImage getImage() {
        int n = this.getLInt();
        int n2 = this.getLInt();
        byte[] byArray = this.getByteArray();
        return new SerializedImage(n, n2, byArray);
    }

    public Skin getSkin() {
        Server.mvw("BinaryStream#getSkin()");
        return this.getSkin(ProtocolInfo.CURRENT_PROTOCOL);
    }

    public Skin getSkin(int n) {
        Skin skin;
        block8: {
            int n2;
            int n3;
            int n4;
            skin = new Skin();
            skin.setSkinId(this.getString());
            if (n >= 428) {
                skin.setPlayFabId(this.getString());
            }
            skin.setSkinResourcePatch(this.getString());
            skin.setSkinData(this.getImage());
            int n5 = this.getLInt();
            for (n4 = 0; n4 < Math.min(n5, 1024); ++n4) {
                SerializedImage serializedImage = this.getImage();
                int n6 = this.getLInt();
                float f2 = this.getLFloat();
                int n7 = n >= 419 ? this.getLInt() : 0;
                skin.getAnimations().add(new SkinAnimation(serializedImage, n6, f2, n7));
            }
            skin.setCapeData(this.getImage());
            skin.setGeometryData(this.getString());
            if (n >= 465) {
                skin.setGeometryDataEngineVersion(this.getString());
            }
            skin.setAnimationData(this.getString());
            if (n < 465) {
                skin.setPremium(this.getBoolean());
                skin.setPersona(this.getBoolean());
                skin.setCapeOnClassic(this.getBoolean());
            }
            skin.setCapeId(this.getString());
            this.getString();
            if (n < 390) break block8;
            skin.setArmSize(this.getString());
            skin.setSkinColor(this.getString());
            n4 = this.getLInt();
            for (n3 = 0; n3 < Math.min(n4, 1024); ++n3) {
                String string = this.getString();
                String string2 = this.getString();
                String string3 = this.getString();
                n2 = this.getBoolean();
                String string4 = this.getString();
                skin.getPersonaPieces().add(new PersonaPiece(string, string2, string3, n2 != 0, string4));
            }
            n3 = this.getLInt();
            for (int k = 0; k < Math.min(n3, 1024); ++k) {
                String string = this.getString();
                ArrayList<String> arrayList = new ArrayList<String>();
                n2 = this.getLInt();
                for (int i2 = 0; i2 < Math.min(n2, 1024); ++i2) {
                    arrayList.add(this.getString());
                }
                skin.getTintColors().add(new PersonaPieceTint(string, arrayList));
            }
            if (n >= 465) {
                skin.setPremium(this.getBoolean());
                skin.setPersona(this.getBoolean());
                skin.setCapeOnClassic(this.getBoolean());
                skin.setPrimaryUser(this.getBoolean());
            }
        }
        return skin;
    }

    public Item getSlot() {
        Server.mvw("BinaryStream#getSlot()");
        return this.getSlot(ProtocolInfo.CURRENT_PROTOCOL);
    }

    public Item getSlot(int n) {
        Tag tag;
        int n2;
        int n3;
        if (n >= 431) {
            return this.a(n);
        }
        int n4 = this.getVarInt();
        if (n4 == 0) {
            return Item.get(0, 0, 0);
        }
        int n5 = this.getVarInt();
        int n6 = n5 >> 8;
        if (n6 == Short.MAX_VALUE) {
            n6 = -1;
        }
        if (n < 419) {
            n3 = n4;
        } else {
            RuntimeItemMapping runtimeItemMapping = RuntimeItems.getMapping(n);
            RuntimeItemMapping.LegacyEntry legacyEntry = runtimeItemMapping.fromRuntime(n4);
            n3 = legacyEntry.getLegacyId();
            if (legacyEntry.isHasDamage()) {
                n6 = legacyEntry.getDamage();
            }
        }
        int n7 = n5 & 0xFF;
        int n8 = this.getLShort();
        byte[] byArray = new byte[]{};
        if (n8 < Short.MAX_VALUE) {
            byArray = this.get(n8);
        } else if (n8 == 65535) {
            int n9 = (int)this.getUnsignedVarInt();
            n2 = this.offset;
            FastByteArrayInputStream fastByteArrayInputStream = new FastByteArrayInputStream(this.get());
            for (int k = 0; k < n9; ++k) {
                try {
                    tag = NBTIO.read(fastByteArrayInputStream, ByteOrder.LITTLE_ENDIAN, true);
                    if (((CompoundTag)tag).contains("Damage")) {
                        n6 = ((CompoundTag)tag).getInt("Damage");
                        ((CompoundTag)tag).remove("Damage");
                    }
                    if (((CompoundTag)tag).contains("__DamageConflict__")) {
                        ((CompoundTag)tag).put("Damage", (Tag)((CompoundTag)tag).removeAndGet("__DamageConflict__"));
                    }
                    if (((CompoundTag)tag).getAllTags().size() <= 0) continue;
                    byArray = NBTIO.write((CompoundTag)tag, ByteOrder.LITTLE_ENDIAN, false);
                    continue;
                }
                catch (IOException iOException) {
                    throw new RuntimeException(iOException);
                }
            }
            this.setOffset(n2 + (int)fastByteArrayInputStream.position());
        }
        String[] stringArray = new String[this.getVarInt()];
        for (n2 = 0; n2 < stringArray.length; ++n2) {
            stringArray[n2] = this.getString();
        }
        String[] stringArray2 = new String[this.getVarInt()];
        for (int k = 0; k < stringArray2.length; ++k) {
            stringArray2[k] = this.getString();
        }
        if (n3 == 513 && n >= 354) {
            this.getVarLong();
        }
        try {
            CompoundTag compoundTag;
            if (n3 == 248 && (compoundTag = NBTIO.read(byArray, ByteOrder.LITTLE_ENDIAN)).contains("mv_origin_id") && compoundTag.contains("mv_origin_meta")) {
                Item item = Item.get(compoundTag.getInt("mv_origin_id"), compoundTag.getInt("mv_origin_meta"), n7);
                if (compoundTag.contains("mv_origin_nbt")) {
                    item.setNamedTag(compoundTag.getCompound("mv_origin_nbt"));
                }
                return item;
            }
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        Item item = Item.get(n3, n6, n7, byArray);
        if (stringArray2.length > 0 || stringArray.length > 0) {
            CompoundTag compoundTag = item.getNamedTag();
            if (compoundTag == null) {
                compoundTag = new CompoundTag();
            }
            if (stringArray2.length > 0) {
                tag = new ListTag("CanDestroy");
                for (String string : stringArray2) {
                    ((ListTag)tag).add(new StringTag("", string));
                }
                compoundTag.put("CanDestroy", tag);
            }
            if (stringArray.length > 0) {
                tag = new ListTag("CanPlaceOn");
                for (String string : stringArray) {
                    ((ListTag)tag).add(new StringTag("", string));
                }
                compoundTag.put("CanPlaceOn", tag);
            }
            item.setNamedTag(compoundTag);
        }
        return item;
    }

    private Item a(int n) {
        String[] stringArray;
        String[] stringArray2;
        Object object;
        Object object2;
        int n2;
        int n3 = this.getVarInt();
        if (n3 == 0) {
            return Item.get(0, 0, 0);
        }
        int n4 = this.getLShort();
        int n5 = (int)this.getUnsignedVarInt();
        RuntimeItemMapping runtimeItemMapping = RuntimeItems.getMapping(n);
        RuntimeItemMapping.LegacyEntry legacyEntry = runtimeItemMapping.fromRuntime(n3);
        int n6 = legacyEntry.getLegacyId();
        if (legacyEntry.isHasDamage()) {
            n5 = legacyEntry.getDamage();
        }
        if (this.getBoolean()) {
            this.getVarInt();
        }
        int n7 = this.getVarInt();
        if (n >= 526 && n6 < 256 && n6 != 166 && (n2 = GlobalBlockPalette.getLegacyFullId(n, n7)) != -1) {
            n5 = n2 & 0x3F;
        }
        byte[] byArray = this.getByteArray();
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer(byArray.length);
        byteBuf.writeBytes(byArray);
        byte[] byArray2 = new byte[]{};
        try {
            object2 = new LittleEndianByteBufInputStream(byteBuf);
            object = null;
            try {
                int n8;
                short s2 = ((LittleEndianByteBufInputStream)object2).readShort();
                String[] stringArray3 = null;
                if (s2 > 0) {
                    stringArray3 = NBTIO.read((InputStream)object2, ByteOrder.LITTLE_ENDIAN);
                } else if (s2 == -1) {
                    n8 = ((ByteBufInputStream)object2).readUnsignedByte();
                    if (n8 != 1) {
                        throw new IllegalArgumentException("Expected 1 tag but got " + n8);
                    }
                    stringArray3 = NBTIO.read((InputStream)object2, ByteOrder.LITTLE_ENDIAN);
                }
                if (stringArray3 != null && stringArray3.getAllTags().size() > 0) {
                    if (stringArray3.contains("Damage") && !legacyEntry.isHasDamage()) {
                        n5 = stringArray3.getInt("Damage");
                        stringArray3.remove("Damage");
                    }
                    if (stringArray3.contains("__DamageConflict__")) {
                        stringArray3.put("Damage", (Tag)stringArray3.removeAndGet("__DamageConflict__"));
                    }
                    if (!stringArray3.isEmpty()) {
                        byArray2 = NBTIO.write((CompoundTag)stringArray3, ByteOrder.LITTLE_ENDIAN);
                    }
                }
                stringArray2 = new String[((LittleEndianByteBufInputStream)object2).readInt()];
                for (n8 = 0; n8 < stringArray2.length; ++n8) {
                    stringArray2[n8] = ((ByteBufInputStream)object2).readUTF();
                }
                stringArray = new String[((LittleEndianByteBufInputStream)object2).readInt()];
                for (n8 = 0; n8 < stringArray.length; ++n8) {
                    stringArray[n8] = ((ByteBufInputStream)object2).readUTF();
                }
                if (n6 == 513) {
                    ((LittleEndianByteBufInputStream)object2).readLong();
                }
                if (n6 == 248 && stringArray3 != null && stringArray3.contains("mv_origin_id") && stringArray3.contains("mv_origin_meta")) {
                    Item item = Item.get(stringArray3.getInt("mv_origin_id"), stringArray3.getInt("mv_origin_meta"), n4);
                    if (stringArray3.contains("mv_origin_nbt")) {
                        item.setNamedTag(stringArray3.getCompound("mv_origin_nbt"));
                    }
                    Item item2 = item;
                    return item2;
                }
            }
            catch (Throwable throwable) {
                object = throwable;
                throw throwable;
            }
            finally {
                if (object2 != null) {
                    if (object != null) {
                        try {
                            ((ByteBufInputStream)object2).close();
                        }
                        catch (Throwable throwable) {
                            ((Throwable)object).addSuppressed(throwable);
                        }
                    } else {
                        ((ByteBufInputStream)object2).close();
                    }
                }
            }
        }
        catch (IOException iOException) {
            throw new IllegalStateException("Unable to read item user data", iOException);
        }
        finally {
            byteBuf.release();
        }
        object2 = Item.get(n6, n5, n4, byArray2);
        if (stringArray.length > 0 || stringArray2.length > 0) {
            object = ((Item)object2).getNamedTag();
            if (object == null) {
                object = new CompoundTag();
            }
            if (stringArray.length > 0) {
                ListTag<StringTag> listTag = new ListTag<StringTag>("CanDestroy");
                for (String string : stringArray) {
                    listTag.add(new StringTag("", string));
                }
                ((CompoundTag)object).put("CanDestroy", listTag);
            }
            if (stringArray2.length > 0) {
                ListTag<StringTag> listTag = new ListTag<StringTag>("CanPlaceOn");
                for (String string : stringArray2) {
                    listTag.add(new StringTag("", string));
                }
                ((CompoundTag)object).put("CanPlaceOn", listTag);
            }
            ((Item)object2).setNamedTag((CompoundTag)object);
        }
        return object2;
    }

    public void putSlot(Item item) {
        Server.mvw("BinaryStream#putSlot(Item)");
        this.putSlot(ProtocolInfo.CURRENT_PROTOCOL, item);
    }

    public void putSlot(int n, Item item) {
        this.putSlot(n, item, false);
    }

    public void putSlot(int n, Item item, boolean bl) {
        if (n >= 526) {
            this.a(n, item, bl);
        } else if (n >= 431) {
            this.c(n, item, bl);
        } else {
            this.b(n, item, bl);
        }
    }

    private void b(int n, Item item, boolean bl) {
        block19: {
            Object object;
            int n2;
            int n3;
            if (item == null || item.getId() == 0) {
                this.putVarInt(0);
                return;
            }
            if (!item.isSupportedOn(n)) {
                Item item2 = item;
                item = Item.get(248, 0, item2.getCount());
                CompoundTag compoundTag = item2.getNamedTag();
                if (compoundTag != null) {
                    item.setNamedTag(new CompoundTag().putCompound("mv_origin_nbt", compoundTag));
                }
                item.setCustomName(item2.getName());
                item.setNamedTag(item.getNamedTag().putInt("mv_origin_id", item2.getId()).putInt("mv_origin_meta", item2.getDamage()));
            }
            int n4 = item.getId();
            int n5 = n3 = item.hasMeta() ? item.getDamage() : -1;
            if (n >= 419) {
                RuntimeItemMapping runtimeItemMapping = RuntimeItems.getMapping(n);
                RuntimeItemMapping.RuntimeEntry runtimeEntry = runtimeItemMapping.toRuntime(item.getId(), item.getDamage());
                n4 = runtimeEntry.getRuntimeId();
                n3 = runtimeEntry.isHasDamage() ? 0 : item.getDamage();
            }
            this.putVarInt(n4);
            boolean bl2 = item instanceof ItemDurable;
            if (n >= 361) {
                n2 = item.getCount();
                if (!bl2) {
                    int n6 = n < 419 ? (item.hasMeta() ? item.getDamage() : -1) : n3;
                    n2 |= (n6 & Short.MAX_VALUE) << 8;
                }
            } else {
                n2 = ((item.hasMeta() ? item.getDamage() : -1) & Short.MAX_VALUE) << 8 | item.getCount();
            }
            this.putVarInt(n2);
            if (bl) {
                this.putLShort(0);
                this.putVarInt(0);
                this.putVarInt(0);
                if (item.getId() == 513 && n >= 354) {
                    this.putVarLong(0L);
                }
                return;
            }
            if (item.hasCompoundTag() || bl2 && n >= 361) {
                if (n < 361) {
                    byte[] byArray = item.getCompoundTag();
                    this.putLShort(byArray.length);
                    this.put(byArray);
                } else {
                    try {
                        byte[] byArray = item.getCompoundTag();
                        object = byArray == null || byArray.length == 0 ? new CompoundTag() : NBTIO.read(byArray, ByteOrder.LITTLE_ENDIAN, false);
                        if (((CompoundTag)object).contains("Damage")) {
                            ((CompoundTag)object).put("__DamageConflict__", (Tag)((CompoundTag)object).removeAndGet("Damage"));
                        }
                        if (bl2) {
                            ((CompoundTag)object).putInt("Damage", item.getDamage());
                        }
                        this.putLShort(65535);
                        this.putByte((byte)1);
                        this.put(NBTIO.write((CompoundTag)object, ByteOrder.LITTLE_ENDIAN, true));
                    }
                    catch (IOException iOException) {
                        throw new RuntimeException(iOException);
                    }
                }
            } else {
                this.putLShort(0);
            }
            List<String> list = BinaryStream.a(item, "CanPlaceOn");
            object = BinaryStream.a(item, "CanDestroy");
            this.putVarInt(list.size());
            for (String string : list) {
                this.putString(string);
            }
            this.putVarInt(object.size());
            Iterator<String> iterator = object.iterator();
            while (iterator.hasNext()) {
                String string;
                string = iterator.next();
                this.putString(string);
            }
            if (item.getId() != 513 || n < 354) break block19;
            this.putVarLong(0L);
        }
    }

    private void c(int n, Item item, boolean bl) {
        Block block;
        Object object;
        Object object2;
        if (item == null || item.getId() == 0) {
            this.putByte((byte)0);
            return;
        }
        if (!item.isSupportedOn(n)) {
            object2 = item;
            item = Item.get(248, 0, ((Item)object2).getCount());
            object = ((Item)object2).getNamedTag();
            if (object != null) {
                item.setNamedTag(new CompoundTag().putCompound("mv_origin_nbt", (CompoundTag)object));
            }
            item.setCustomName(((Item)object2).getName());
            item.setNamedTag(item.getNamedTag().putInt("mv_origin_id", ((Item)object2).getId()).putInt("mv_origin_meta", ((Item)object2).getDamage()));
        }
        object2 = RuntimeItems.getMapping(n);
        object = ((RuntimeItemMapping)object2).toRuntime(item.getId(), item.getDamage());
        int n2 = ((RuntimeItemMapping.RuntimeEntry)object).getRuntimeId();
        int n3 = ((RuntimeItemMapping.RuntimeEntry)object).isHasDamage() ? 0 : item.getDamage();
        this.putVarInt(n2);
        this.putLShort(item.getCount());
        this.putUnsignedVarInt(n3);
        if (!bl) {
            this.putBoolean(true);
            this.putVarInt(1);
        }
        int n4 = (block = item.getBlockUnsafe()) == null ? 0 : GlobalBlockPalette.getOrCreateRuntimeId(n, block.getId(), block.getDamage());
        this.putVarInt(n4);
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        try (LittleEndianByteBufOutputStream littleEndianByteBufOutputStream = new LittleEndianByteBufOutputStream(byteBuf);){
            Object object4;
            Object object5;
            if ((item instanceof ItemDurable && item.getDamage() > 0 || block != null && block.getDamage() > 0) && !((RuntimeItemMapping.RuntimeEntry)object).isHasDamage()) {
                object5 = item.getCompoundTag();
                object4 = object5 == null || ((byte[])object5).length == 0 ? new CompoundTag() : NBTIO.read(object5, ByteOrder.LITTLE_ENDIAN);
                if (((CompoundTag)object4).contains("Damage")) {
                    ((CompoundTag)object4).put("__DamageConflict__", (Tag)((CompoundTag)object4).removeAndGet("Damage"));
                }
                ((CompoundTag)object4).putInt("Damage", item.getDamage());
                littleEndianByteBufOutputStream.writeShort(-1);
                littleEndianByteBufOutputStream.writeByte(1);
                littleEndianByteBufOutputStream.write(NBTIO.write(object4, ByteOrder.LITTLE_ENDIAN));
            } else if (item.hasCompoundTag()) {
                littleEndianByteBufOutputStream.writeShort(-1);
                littleEndianByteBufOutputStream.writeByte(1);
                littleEndianByteBufOutputStream.write(item.getCompoundTag());
            } else {
                byteBuf.writeShortLE(0);
            }
            object5 = BinaryStream.a(item, "CanPlaceOn");
            littleEndianByteBufOutputStream.writeInt(object5.size());
            for (String object32 : object5) {
                littleEndianByteBufOutputStream.writeUTF(object32);
            }
            object4 = BinaryStream.a(item, "CanDestroy");
            littleEndianByteBufOutputStream.writeInt(object4.size());
            Iterator<String> iterator = object4.iterator();
            while (iterator.hasNext()) {
                String string = iterator.next();
                littleEndianByteBufOutputStream.writeUTF(string);
            }
            if (item.getId() == 513) {
                littleEndianByteBufOutputStream.writeLong(0L);
            }
            byte[] byArray = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(byArray);
            this.putByteArray(byArray);
        }
        catch (IOException iOException) {
            throw new IllegalStateException("Unable to write item user data", iOException);
        }
        finally {
            byteBuf.release();
        }
    }

    private void a(int n, Item item, boolean bl) {
        if (item == null || item.getId() == 0) {
            this.putByte((byte)0);
            return;
        }
        if (!item.isSupportedOn(n)) {
            Item item2 = item;
            item = Item.get(248, 0, item2.getCount());
            CompoundTag compoundTag = item2.getNamedTag();
            if (compoundTag != null) {
                item.setNamedTag(new CompoundTag().putCompound("mv_origin_nbt", compoundTag));
            }
            item.setCustomName(item2.getName());
            item.setNamedTag(item.getNamedTag().putInt("mv_origin_id", item2.getId()).putInt("mv_origin_meta", item2.getDamage()));
        }
        int n2 = item.getId();
        int n3 = item.getDamage();
        boolean bl2 = item instanceof ItemBlock;
        boolean bl3 = item instanceof ItemDurable;
        RuntimeItemMapping runtimeItemMapping = RuntimeItems.getMapping(n);
        RuntimeItemMapping.RuntimeEntry runtimeEntry = runtimeItemMapping.toRuntime(n2, n3);
        int n4 = runtimeEntry.getRuntimeId();
        int n5 = bl2 || bl3 || runtimeEntry.isHasDamage() ? 0 : n3;
        this.putVarInt(n4);
        this.putLShort(item.getCount());
        this.putUnsignedVarInt(n5);
        if (!bl) {
            this.putBoolean(true);
            this.putVarInt(1);
        }
        Block block = bl2 ? item.getBlockUnsafe() : null;
        int n6 = block == null ? 0 : GlobalBlockPalette.getOrCreateRuntimeId(n, block.getId(), block.getDamage());
        this.putVarInt(n6);
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        try (LittleEndianByteBufOutputStream littleEndianByteBufOutputStream = new LittleEndianByteBufOutputStream(byteBuf);){
            Object object2;
            Object object3;
            if (!bl && bl3 && !runtimeEntry.isHasDamage()) {
                object3 = item.getCompoundTag();
                object2 = object3 == null || ((byte[])object3).length == 0 ? new CompoundTag() : NBTIO.read(object3, ByteOrder.LITTLE_ENDIAN);
                if (((CompoundTag)object2).contains("Damage")) {
                    ((CompoundTag)object2).put("__DamageConflict__", (Tag)((CompoundTag)object2).removeAndGet("Damage"));
                }
                ((CompoundTag)object2).putInt("Damage", n3);
                littleEndianByteBufOutputStream.writeShort(-1);
                littleEndianByteBufOutputStream.writeByte(1);
                littleEndianByteBufOutputStream.write(NBTIO.write(object2, ByteOrder.LITTLE_ENDIAN));
            } else if (item.hasCompoundTag()) {
                littleEndianByteBufOutputStream.writeShort(-1);
                littleEndianByteBufOutputStream.writeByte(1);
                littleEndianByteBufOutputStream.write(item.getCompoundTag());
            } else {
                byteBuf.writeShortLE(0);
            }
            object3 = BinaryStream.a(item, "CanPlaceOn");
            littleEndianByteBufOutputStream.writeInt(object3.size());
            for (String object4 : object3) {
                littleEndianByteBufOutputStream.writeUTF(object4);
            }
            object2 = BinaryStream.a(item, "CanDestroy");
            littleEndianByteBufOutputStream.writeInt(object2.size());
            Iterator<String> iterator = object2.iterator();
            while (iterator.hasNext()) {
                String string = iterator.next();
                littleEndianByteBufOutputStream.writeUTF(string);
            }
            if (n2 == 513) {
                littleEndianByteBufOutputStream.writeLong(0L);
            }
            byte[] byArray = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(byArray);
            this.putByteArray(byArray);
        }
        catch (IOException iOException) {
            throw new IllegalStateException("Unable to write item user data", iOException);
        }
        finally {
            byteBuf.release();
        }
    }

    public Item getRecipeIngredient(int n) {
        int n2;
        int n3 = this.getVarInt();
        if (n3 == 0) {
            return Item.get(0, 0, 0);
        }
        int n4 = this.getVarInt();
        if (n4 == Short.MAX_VALUE) {
            n4 = -1;
        }
        if (n < 419) {
            n2 = n3;
        } else {
            RuntimeItemMapping runtimeItemMapping = RuntimeItems.getMapping(n);
            RuntimeItemMapping.LegacyEntry legacyEntry = runtimeItemMapping.fromRuntime(n3);
            n2 = legacyEntry.getLegacyId();
            if (legacyEntry.isHasDamage()) {
                n4 = legacyEntry.getDamage();
            }
        }
        int n5 = this.getVarInt();
        return Item.get(n2, n4, n5);
    }

    public void putRecipeIngredient(int n, Item item) {
        int n2;
        if (item == null || item.getId() == 0) {
            if (n >= 553) {
                this.putBoolean(false);
                this.putVarInt(0);
            } else {
                this.putVarInt(0);
            }
            return;
        }
        if (n >= 553) {
            this.putBoolean(true);
        }
        int n3 = item.getId();
        int n4 = n2 = item.hasMeta() ? item.getDamage() : Short.MAX_VALUE;
        if (n >= 419) {
            RuntimeItemMapping runtimeItemMapping = RuntimeItems.getMapping(n);
            if (!item.hasMeta()) {
                RuntimeItemMapping.RuntimeEntry runtimeEntry = runtimeItemMapping.toRuntime(item.getId(), 0);
                n3 = runtimeEntry.getRuntimeId();
                n2 = Short.MAX_VALUE;
            } else {
                RuntimeItemMapping.RuntimeEntry runtimeEntry = runtimeItemMapping.toRuntime(item.getId(), item.getDamage());
                n3 = runtimeEntry.getRuntimeId();
                int n5 = n2 = runtimeEntry.isHasDamage() ? 0 : item.getDamage();
            }
        }
        if (n >= 553) {
            this.putLShort(n3);
            this.putLShort(n2);
        } else {
            this.putVarInt(n3);
            this.putVarInt(n2);
        }
        this.putVarInt(item.getCount());
    }

    private static List<String> a(Item item, String string) {
        CompoundTag compoundTag = item.getNamedTag();
        if (compoundTag == null) {
            return Collections.emptyList();
        }
        ListTag<StringTag> listTag = compoundTag.getList(string, StringTag.class);
        if (listTag == null) {
            return Collections.emptyList();
        }
        int n = listTag.size();
        ArrayList<String> arrayList = new ArrayList<String>(n);
        for (int k = 0; k < n; ++k) {
            StringTag stringTag = listTag.get(k);
            if (stringTag == null) continue;
            arrayList.add(stringTag.data);
        }
        return arrayList;
    }

    public byte[] getByteArray() {
        return this.get((int)this.getUnsignedVarInt());
    }

    public void putByteArray(byte[] byArray) {
        this.putUnsignedVarInt(byArray.length);
        this.put(byArray);
    }

    public String getString() {
        return new String(this.getByteArray(), StandardCharsets.UTF_8);
    }

    public void putString(String string) {
        byte[] byArray = string.getBytes(StandardCharsets.UTF_8);
        this.putByteArray(byArray);
    }

    public long getUnsignedVarInt() {
        return VarInt.readUnsignedVarInt(this);
    }

    public void putUnsignedVarInt(long l) {
        VarInt.writeUnsignedVarInt(this, l);
    }

    public int getVarInt() {
        return VarInt.readVarInt(this);
    }

    public void putVarInt(int n) {
        VarInt.writeVarInt(this, n);
    }

    public long getVarLong() {
        return VarInt.readVarLong(this);
    }

    public void putVarLong(long l) {
        VarInt.writeVarLong(this, l);
    }

    public long getUnsignedVarLong() {
        return VarInt.readUnsignedVarLong(this);
    }

    public void putUnsignedVarLong(long l) {
        VarInt.writeUnsignedVarLong(this, l);
    }

    public BlockVector3 getBlockVector3() {
        return new BlockVector3(this.getVarInt(), (int)this.getUnsignedVarInt(), this.getVarInt());
    }

    public BlockVector3 getSignedBlockPosition() {
        return new BlockVector3(this.getVarInt(), this.getVarInt(), this.getVarInt());
    }

    public void putSignedBlockPosition(BlockVector3 blockVector3) {
        this.putVarInt(blockVector3.x);
        this.putVarInt(blockVector3.y);
        this.putVarInt(blockVector3.z);
    }

    public void putBlockVector3(BlockVector3 blockVector3) {
        this.putBlockVector3(blockVector3.x, blockVector3.y, blockVector3.z);
    }

    public void putBlockVector3(int n, int n2, int n3) {
        this.putVarInt(n);
        this.putUnsignedVarInt(n2);
        this.putVarInt(n3);
    }

    public Vector3f getVector3f() {
        return new Vector3f(this.getLFloat(4), this.getLFloat(4), this.getLFloat(4));
    }

    public void putVector3f(Vector3f vector3f) {
        this.putVector3f(vector3f.x, vector3f.y, vector3f.z);
    }

    public void putVector3f(float f2, float f3, float f4) {
        this.putLFloat(f2);
        this.putLFloat(f3);
        this.putLFloat(f4);
    }

    public void putGameRules(GameRules gameRules) {
        Server.mvw("BinaryStream#putGameRules(GameRules)");
        this.putGameRules(ProtocolInfo.CURRENT_PROTOCOL, gameRules);
    }

    public void putGameRules(int n, GameRules gameRules) {
        Map<GameRule, GameRules.Value> map = gameRules.getGameRules();
        HashMap<GameRule, GameRules.Value> hashMap = new HashMap<GameRule, GameRules.Value>();
        map.forEach((gameRule, value) -> {
            block0: {
                if (n <= value.getMinProtocol()) break block0;
                hashMap.put((GameRule)((Object)gameRule), (GameRules.Value)value);
            }
        });
        this.putUnsignedVarInt(hashMap.size());
        hashMap.forEach((gameRule, value) -> {
            this.putString(gameRule.getName().toLowerCase());
            value.write(n, this);
        });
    }

    public void putGameRulesMap(int n, Map<GameRule, GameRules.Value> map) {
        HashMap<GameRule, GameRules.Value> hashMap = new HashMap<GameRule, GameRules.Value>();
        map.forEach((gameRule, value) -> {
            if (n > value.getMinProtocol()) {
                if (gameRule == GameRule.NATURAL_REGENERATION) {
                    hashMap.put((GameRule)((Object)gameRule), new GameRules.Value<Boolean>(GameRules.Type.BOOLEAN, false));
                } else {
                    hashMap.put((GameRule)((Object)gameRule), (GameRules.Value)value);
                }
            }
        });
        this.putUnsignedVarInt(hashMap.size());
        hashMap.forEach((gameRule, value) -> {
            this.putString(gameRule.getName().toLowerCase());
            value.write(n, this);
        });
    }

    public long getEntityUniqueId() {
        return this.getVarLong();
    }

    public void putEntityUniqueId(long l) {
        this.putVarLong(l);
    }

    public long getEntityRuntimeId() {
        return this.getUnsignedVarLong();
    }

    public void putEntityRuntimeId(long l) {
        this.putUnsignedVarLong(l);
    }

    public BlockFace getBlockFace() {
        return BlockFace.fromIndex(this.getVarInt());
    }

    public void putBlockFace(BlockFace blockFace) {
        this.putVarInt(blockFace.getIndex());
    }

    public void putEntityLink(EntityLink entityLink) {
        Server.mvw("BinaryStream#putEntityLink(EntityLink)");
        this.putEntityLink(ProtocolInfo.CURRENT_PROTOCOL, entityLink);
    }

    public void putEntityLink(int n, EntityLink entityLink) {
        block0: {
            this.putEntityUniqueId(entityLink.fromEntityUniquieId);
            this.putEntityUniqueId(entityLink.toEntityUniquieId);
            this.putByte(entityLink.type);
            this.putBoolean(entityLink.immediate);
            if (n < 407) break block0;
            this.putBoolean(entityLink.riderInitiated);
        }
    }

    public EntityLink getEntityLink() {
        return new EntityLink(this.getEntityUniqueId(), this.getEntityUniqueId(), (byte)this.getByte(), this.getBoolean(), this.getBoolean());
    }

    public <T> T[] getArray(Class<T> clazz, Function<BinaryStream, T> function) {
        ArrayDeque<T> arrayDeque = new ArrayDeque<T>();
        int n = (int)this.getUnsignedVarInt();
        for (int k = 0; k < n; ++k) {
            arrayDeque.add(function.apply(this));
        }
        return arrayDeque.toArray((Object[])Array.newInstance(clazz, 0));
    }

    public <T> void putArray(Collection<T> collection, BiConsumer<BinaryStream, T> biConsumer) {
        this.putUnsignedVarInt(collection.size());
        for (T t : collection) {
            biConsumer.accept(this, (BinaryStream)t);
        }
    }

    public boolean feof() {
        return this.offset < 0 || this.offset >= this.c.length;
    }

    private void d(int n) {
        block0: {
            if (n - this.c.length <= 0) break block0;
            this.b(n);
        }
    }

    private void b(int n) {
        int n2 = this.c.length;
        int n3 = n2 << 1;
        if (n3 - n < 0) {
            n3 = n;
        }
        if (n3 - 0x7FFFFFF7 > 0) {
            n3 = BinaryStream.c(n);
        }
        this.c = Arrays.copyOf(this.c, n3);
    }

    private static int c(int n) {
        if (n < 0) {
            throw new OutOfMemoryError();
        }
        return n > 0x7FFFFFF7 ? Integer.MAX_VALUE : 0x7FFFFFF7;
    }

    private static Throwable a(Throwable throwable) {
        return throwable;
    }
}

