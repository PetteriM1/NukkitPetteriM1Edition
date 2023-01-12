/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.Server;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.entity.data.EntityData;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.data.FloatEntityData;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.entity.data.IntPositionEntityData;
import cn.nukkit.entity.data.LongEntityData;
import cn.nukkit.entity.data.NBTEntityData;
import cn.nukkit.entity.data.ShortEntityData;
import cn.nukkit.entity.data.StringEntityData;
import cn.nukkit.entity.data.Vector3fEntityData;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BinaryStream;
import it.unimi.dsi.fastutil.io.FastByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class Binary {
    public static int signByte(int n) {
        return n << 56 >> 56;
    }

    public static int unsignByte(int n) {
        return n & 0xFF;
    }

    public static int signShort(int n) {
        return n << 48 >> 48;
    }

    public int unsignShort(int n) {
        return n & 0xFFFF;
    }

    public static int signInt(int n) {
        return n << 32 >> 32;
    }

    public static int unsignInt(int n) {
        return n;
    }

    public static int readTriad(byte[] byArray) {
        return Binary.readInt(new byte[]{0, byArray[0], byArray[1], byArray[2]});
    }

    public static byte[] writeTriad(int n) {
        return new byte[]{(byte)(n >>> 16 & 0xFF), (byte)(n >>> 8 & 0xFF), (byte)(n & 0xFF)};
    }

    public static int readLTriad(byte[] byArray) {
        return Binary.readLInt(new byte[]{byArray[0], byArray[1], byArray[2], 0});
    }

    public static byte[] writeLTriad(int n) {
        return new byte[]{(byte)(n & 0xFF), (byte)(n >>> 8 & 0xFF), (byte)(n >>> 16 & 0xFF)};
    }

    public static UUID readUUID(byte[] byArray) {
        return new UUID(Binary.readLLong(byArray), Binary.readLLong(new byte[]{byArray[8], byArray[9], byArray[10], byArray[11], byArray[12], byArray[13], byArray[14], byArray[15]}));
    }

    public static byte[] writeUUID(UUID uUID) {
        return Binary.appendBytes(Binary.writeLLong(uUID.getMostSignificantBits()), (byte[][])new byte[][]{Binary.writeLLong(uUID.getLeastSignificantBits())});
    }

    public static byte[] writeMetadata(EntityMetadata entityMetadata) {
        Server.mvw("Binary#writeMetadata(EntityMetadata)");
        return Binary.writeMetadata(ProtocolInfo.CURRENT_PROTOCOL, entityMetadata);
    }

    public static byte[] writeMetadata(int n, EntityMetadata entityMetadata) {
        BinaryStream binaryStream = new BinaryStream();
        Map<Integer, EntityData> map = entityMetadata.getMap();
        if (n < 428) {
            map.remove(60);
        }
        binaryStream.putUnsignedVarInt(map.size());
        for (Map.Entry<Integer, EntityData> entry : map.entrySet()) {
            EntityData entityData = entry.getValue();
            int n2 = entry.getKey();
            boolean bl = false;
            if (n < 557) {
                if (n2 >= 120) {
                    ++n2;
                }
                if (n < 428) {
                    if (n2 >= 60) {
                        --n2;
                    }
                    if (n2 == 121) {
                        n2 = 119;
                    }
                    if (n == 354) {
                        if (n2 >= 40) {
                            ++n2;
                        }
                    } else if (n <= 201) {
                        if (n2 >= 29) {
                            ++n2;
                        }
                        if (n2 > 76) {
                            bl = true;
                        }
                    }
                }
            }
            if (bl) {
                binaryStream.putUnsignedVarInt(75L);
                binaryStream.putUnsignedVarInt(2L);
                binaryStream.putVarInt(0);
                continue;
            }
            binaryStream.putUnsignedVarInt(n2);
            binaryStream.putUnsignedVarInt(entityData.getType());
            switch (entityData.getType()) {
                case 0: {
                    binaryStream.putByte(((ByteEntityData)entityData).getData().byteValue());
                    break;
                }
                case 1: {
                    binaryStream.putLShort(((ShortEntityData)entityData).getData());
                    break;
                }
                case 2: {
                    if (n2 == 2 && n < 524 && ((IntEntityData)entityData).getData() == 6) {
                        binaryStream.putVarInt(0);
                        break;
                    }
                    binaryStream.putVarInt(((IntEntityData)entityData).getData());
                    break;
                }
                case 3: {
                    binaryStream.putLFloat(((FloatEntityData)entityData).getData().floatValue());
                    break;
                }
                case 4: {
                    String string = ((StringEntityData)entityData).getData();
                    binaryStream.putUnsignedVarInt(string.getBytes(StandardCharsets.UTF_8).length);
                    binaryStream.put(string.getBytes(StandardCharsets.UTF_8));
                    break;
                }
                case 5: {
                    NBTEntityData nBTEntityData = (NBTEntityData)entityData;
                    if (n < 361) {
                        binaryStream.putSlot(n, nBTEntityData.item);
                        break;
                    }
                    try {
                        binaryStream.put(NBTIO.write(nBTEntityData.getData(), ByteOrder.LITTLE_ENDIAN, true));
                        break;
                    }
                    catch (IOException iOException) {
                        throw new RuntimeException(iOException);
                    }
                }
                case 6: {
                    IntPositionEntityData intPositionEntityData = (IntPositionEntityData)entityData;
                    binaryStream.putVarInt(intPositionEntityData.x);
                    binaryStream.putVarInt(intPositionEntityData.y);
                    binaryStream.putVarInt(intPositionEntityData.z);
                    break;
                }
                case 7: {
                    Object object;
                    if (n < 560) {
                        if (n2 == 0) {
                            object = ((LongEntityData)entityData).dataVersions;
                            if (object != null && ((Object)object).length == 3) {
                                if (n < 291) {
                                    if (n < 223) {
                                        binaryStream.putVarLong((long)object[2]);
                                        break;
                                    }
                                    binaryStream.putVarLong((long)object[1]);
                                    break;
                                }
                                binaryStream.putVarLong((long)object[0]);
                                break;
                            }
                            if (Server.getInstance().minimumProtocol != ProtocolInfo.CURRENT_PROTOCOL) {
                                Server.getInstance().getLogger().debug("Invalid LongEntityData dataVersions for DATA_FLAGS, reverting to non-multiversion compatible flags: expected 3, got " + (object == null ? 0 : ((Object)object).length));
                            }
                        } else if (n2 == 92) {
                            object = ((LongEntityData)entityData).dataVersions;
                            if (object != null && ((Object)object).length == 1) {
                                binaryStream.putVarLong((long)object[0]);
                                break;
                            }
                            if (Server.getInstance().minimumProtocol != ProtocolInfo.CURRENT_PROTOCOL) {
                                Server.getInstance().getLogger().debug("Invalid LongEntityData dataVersions for DATA_FLAGS_EXTENDED, reverting to non-multiversion compatible flags: expected 1, got " + (object == null ? 0 : ((Object)object).length));
                            }
                        }
                    }
                    binaryStream.putVarLong(((LongEntityData)entityData).getData());
                    break;
                }
                case 8: {
                    Object object = (Vector3fEntityData)entityData;
                    binaryStream.putLFloat(((Vector3fEntityData)object).x);
                    binaryStream.putLFloat(((Vector3fEntityData)object).y);
                    binaryStream.putLFloat(((Vector3fEntityData)object).z);
                }
            }
        }
        return binaryStream.getBuffer();
    }

    public static EntityMetadata readMetadata(byte[] byArray) {
        BinaryStream binaryStream = new BinaryStream();
        binaryStream.setBuffer(byArray);
        long l = binaryStream.getUnsignedVarInt();
        EntityMetadata entityMetadata = new EntityMetadata();
        int n = 0;
        while ((long)n < l) {
            int n2 = (int)binaryStream.getUnsignedVarInt();
            int n3 = (int)binaryStream.getUnsignedVarInt();
            EntityData entityData = null;
            switch (n3) {
                case 0: {
                    entityData = new ByteEntityData(n2, binaryStream.getByte());
                    break;
                }
                case 1: {
                    entityData = new ShortEntityData(n2, binaryStream.getLShort());
                    break;
                }
                case 2: {
                    entityData = new IntEntityData(n2, binaryStream.getVarInt());
                    break;
                }
                case 3: {
                    entityData = new FloatEntityData(n2, binaryStream.getLFloat());
                    break;
                }
                case 4: {
                    entityData = new StringEntityData(n2, binaryStream.getString());
                    break;
                }
                case 5: {
                    try {
                        int n4 = binaryStream.getOffset();
                        FastByteArrayInputStream fastByteArrayInputStream = new FastByteArrayInputStream(binaryStream.get());
                        try {
                            CompoundTag compoundTag = NBTIO.read(fastByteArrayInputStream, ByteOrder.LITTLE_ENDIAN, true);
                            entityData = new NBTEntityData(n2, compoundTag);
                        }
                        catch (IOException iOException) {
                            throw new RuntimeException(iOException);
                        }
                        binaryStream.setOffset(n4 + (int)fastByteArrayInputStream.position());
                    }
                    catch (Exception exception) {
                        Server.getInstance().getLogger().debug("Read DATA_TYPE_NBT pre 1.12?", exception);
                    }
                    break;
                }
                case 6: {
                    BlockVector3 blockVector3 = binaryStream.getSignedBlockPosition();
                    entityData = new IntPositionEntityData(n2, blockVector3.x, blockVector3.y, blockVector3.z);
                    break;
                }
                case 7: {
                    entityData = new LongEntityData(n2, binaryStream.getVarLong());
                    break;
                }
                case 8: {
                    entityData = new Vector3fEntityData(n2, binaryStream.getVector3f());
                }
            }
            if (entityData != null) {
                entityMetadata.put(entityData);
            }
            ++n;
        }
        return entityMetadata;
    }

    public static boolean readBool(byte by) {
        return by == 0;
    }

    public static byte writeBool(boolean bl) {
        return (byte)(bl ? 1 : 0);
    }

    public static int readSignedByte(byte by) {
        return by & 0xFF;
    }

    public static byte writeByte(byte by) {
        return by;
    }

    public static int readShort(byte[] byArray) {
        return ((byArray[0] & 0xFF) << 8) + (byArray[1] & 0xFF);
    }

    public static short readSignedShort(byte[] byArray) {
        return (short)Binary.readShort(byArray);
    }

    public static byte[] writeShort(int n) {
        return new byte[]{(byte)(n >>> 8 & 0xFF), (byte)(n & 0xFF)};
    }

    public static int readLShort(byte[] byArray) {
        return ((byArray[1] & 0xFF) << 8) + (byArray[0] & 0xFF);
    }

    public static short readSignedLShort(byte[] byArray) {
        return (short)Binary.readLShort(byArray);
    }

    public static byte[] writeLShort(int n) {
        return new byte[]{(byte)((n &= 0xFFFF) & 0xFF), (byte)(n >>> 8 & 0xFF)};
    }

    public static int readInt(byte[] byArray) {
        return ((byArray[0] & 0xFF) << 24) + ((byArray[1] & 0xFF) << 16) + ((byArray[2] & 0xFF) << 8) + (byArray[3] & 0xFF);
    }

    public static byte[] writeInt(int n) {
        return new byte[]{(byte)(n >>> 24 & 0xFF), (byte)(n >>> 16 & 0xFF), (byte)(n >>> 8 & 0xFF), (byte)(n & 0xFF)};
    }

    public static int readLInt(byte[] byArray) {
        return ((byArray[3] & 0xFF) << 24) + ((byArray[2] & 0xFF) << 16) + ((byArray[1] & 0xFF) << 8) + (byArray[0] & 0xFF);
    }

    public static byte[] writeLInt(int n) {
        return new byte[]{(byte)(n & 0xFF), (byte)(n >>> 8 & 0xFF), (byte)(n >>> 16 & 0xFF), (byte)(n >>> 24 & 0xFF)};
    }

    public static float readFloat(byte[] byArray) {
        return Binary.readFloat(byArray, -1);
    }

    public static float readFloat(byte[] byArray, int n) {
        float f2 = Float.intBitsToFloat(Binary.readInt(byArray));
        if (n > -1) {
            return (float)NukkitMath.round(f2, n);
        }
        return f2;
    }

    public static byte[] writeFloat(float f2) {
        return Binary.writeInt(Float.floatToIntBits(f2));
    }

    public static float readLFloat(byte[] byArray) {
        return Binary.readLFloat(byArray, -1);
    }

    public static float readLFloat(byte[] byArray, int n) {
        float f2 = Float.intBitsToFloat(Binary.readLInt(byArray));
        if (n > -1) {
            return (float)NukkitMath.round(f2, n);
        }
        return f2;
    }

    public static byte[] writeLFloat(float f2) {
        return Binary.writeLInt(Float.floatToIntBits(f2));
    }

    public static double readDouble(byte[] byArray) {
        return Double.longBitsToDouble(Binary.readLong(byArray));
    }

    public static byte[] writeDouble(double d2) {
        return Binary.writeLong(Double.doubleToLongBits(d2));
    }

    public static double readLDouble(byte[] byArray) {
        return Double.longBitsToDouble(Binary.readLLong(byArray));
    }

    public static byte[] writeLDouble(double d2) {
        return Binary.writeLLong(Double.doubleToLongBits(d2));
    }

    public static long readLong(byte[] byArray) {
        return ((long)byArray[0] << 56) + ((long)(byArray[1] & 0xFF) << 48) + ((long)(byArray[2] & 0xFF) << 40) + ((long)(byArray[3] & 0xFF) << 32) + ((long)(byArray[4] & 0xFF) << 24) + (long)((byArray[5] & 0xFF) << 16) + (long)((byArray[6] & 0xFF) << 8) + (long)(byArray[7] & 0xFF);
    }

    public static byte[] writeLong(long l) {
        return new byte[]{(byte)(l >>> 56), (byte)(l >>> 48), (byte)(l >>> 40), (byte)(l >>> 32), (byte)(l >>> 24), (byte)(l >>> 16), (byte)(l >>> 8), (byte)l};
    }

    public static long readLLong(byte[] byArray) {
        return ((long)byArray[7] << 56) + ((long)(byArray[6] & 0xFF) << 48) + ((long)(byArray[5] & 0xFF) << 40) + ((long)(byArray[4] & 0xFF) << 32) + ((long)(byArray[3] & 0xFF) << 24) + (long)((byArray[2] & 0xFF) << 16) + (long)((byArray[1] & 0xFF) << 8) + (long)(byArray[0] & 0xFF);
    }

    public static byte[] writeLLong(long l) {
        return new byte[]{(byte)l, (byte)(l >>> 8), (byte)(l >>> 16), (byte)(l >>> 24), (byte)(l >>> 32), (byte)(l >>> 40), (byte)(l >>> 48), (byte)(l >>> 56)};
    }

    public static byte[] writeVarInt(int n) {
        BinaryStream binaryStream = new BinaryStream();
        binaryStream.putVarInt(n);
        return binaryStream.getBuffer();
    }

    public static byte[] writeUnsignedVarInt(long l) {
        BinaryStream binaryStream = new BinaryStream();
        binaryStream.putUnsignedVarInt(l);
        return binaryStream.getBuffer();
    }

    public static byte[] reserveBytes(byte[] byArray) {
        byte[] byArray2 = new byte[byArray.length];
        for (int k = 0; k < byArray.length; ++k) {
            byArray2[byArray.length - 1 - k] = byArray[k];
        }
        return byArray2;
    }

    public static String bytesToHexString(byte[] byArray) {
        return Binary.bytesToHexString(byArray, false);
    }

    public static String bytesToHexString(byte[] byArray, boolean bl) {
        StringBuilder stringBuilder = new StringBuilder();
        if (byArray == null || byArray.length == 0) {
            return null;
        }
        for (byte by : byArray) {
            int n;
            String string;
            if (stringBuilder.length() != 0 && bl) {
                stringBuilder.append(' ');
            }
            if ((string = Integer.toHexString(n = by & 0xFF)).length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(string);
        }
        return stringBuilder.toString().toUpperCase();
    }

    public static byte[] hexStringToBytes(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        String string2 = "0123456789ABCDEF";
        string = string.toUpperCase().replace(" ", "");
        int n = string.length() >> 1;
        char[] cArray = string.toCharArray();
        byte[] byArray = new byte[n];
        for (int k = 0; k < n; ++k) {
            int n2 = k << 1;
            byArray[k] = (byte)((byte)string2.indexOf(cArray[n2]) << 4 | (byte)string2.indexOf(cArray[n2 + 1]));
        }
        return byArray;
    }

    public static byte[] subBytes(byte[] byArray, int n, int n2) {
        int n3 = Math.min(byArray.length, n + n2);
        return Arrays.copyOfRange(byArray, n, n3);
    }

    public static byte[] subBytes(byte[] byArray, int n) {
        return Binary.subBytes(byArray, n, byArray.length - n);
    }

    public static byte[][] splitBytes(byte[] byArray, int n) {
        byte[][] byArray2 = new byte[(byArray.length + n - 1) / n][n];
        int n2 = 0;
        for (int k = 0; k < byArray.length; k += n) {
            byArray2[n2] = byArray.length - k > n ? Arrays.copyOfRange(byArray, k, k + n) : Arrays.copyOfRange(byArray, k, byArray.length);
            ++n2;
        }
        return byArray2;
    }

    public static byte[] appendBytes(byte[][] byArray) {
        int n = 0;
        for (byte[] byArray2 : byArray) {
            n += byArray2.length;
        }
        byte[] byArray3 = new byte[n];
        int n2 = 0;
        for (byte[] byArray4 : byArray) {
            System.arraycopy(byArray4, 0, byArray3, n2, byArray4.length);
            n2 += byArray4.length;
        }
        return byArray3;
    }

    public static byte[] appendBytes(byte by, byte[] ... byArray) {
        int n = 1;
        for (byte[] byArray2 : byArray) {
            n += byArray2.length;
        }
        Object object = ByteBuffer.allocate(n);
        ((ByteBuffer)object).put(by);
        for (byte[] byArray3 : byArray) {
            ((ByteBuffer)object).put(byArray3);
        }
        return ((ByteBuffer)object).array();
    }

    public static byte[] appendBytes(byte[] byArray, byte[] ... byArray2) {
        int n = byArray.length;
        for (byte[] byArray3 : byArray2) {
            n += byArray3.length;
        }
        byte[] byArray4 = new byte[n];
        System.arraycopy(byArray, 0, byArray4, 0, byArray.length);
        int n2 = byArray.length;
        for (byte[] byArray5 : byArray2) {
            System.arraycopy(byArray5, 0, byArray4, n2, byArray5.length);
            n2 += byArray5.length;
        }
        return byArray4;
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

