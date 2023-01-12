/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt;

import cn.nukkit.item.Item;
import cn.nukkit.nbt.stream.FastByteArrayOutputStream;
import cn.nukkit.nbt.stream.NBTInputStream;
import cn.nukkit.nbt.stream.NBTOutputStream;
import cn.nukkit.nbt.stream.PGZIPOutputStream;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.ThreadCache;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;

public class NBTIO {
    public static CompoundTag putItemHelper(Item item) {
        return NBTIO.putItemHelper(item, null);
    }

    public static CompoundTag putItemHelper(Item item, Integer n) {
        CompoundTag compoundTag = new CompoundTag(null).putShort("id", item.getId()).putByte("Count", item.getCount()).putShort("Damage", item.getDamage());
        if (n != null) {
            compoundTag.putByte("Slot", n);
        }
        if (item.hasCompoundTag()) {
            compoundTag.putCompound("tag", item.getNamedTag());
        }
        return compoundTag;
    }

    public static Item getItemHelper(CompoundTag compoundTag) {
        if (!compoundTag.contains("id") || !compoundTag.contains("Count")) {
            return Item.get(0);
        }
        return Item.getSaved(compoundTag.getShort("id"), !compoundTag.contains("Damage") ? 0 : compoundTag.getShort("Damage"), compoundTag.getByte("Count"), compoundTag.get("tag"));
    }

    public static CompoundTag read(File file) throws IOException {
        return NBTIO.read(file, ByteOrder.BIG_ENDIAN);
    }

    public static CompoundTag read(File file, ByteOrder byteOrder) throws IOException {
        if (!file.exists()) {
            return null;
        }
        return NBTIO.read(new FileInputStream(file), byteOrder);
    }

    public static CompoundTag read(InputStream inputStream) throws IOException {
        return NBTIO.read(inputStream, ByteOrder.BIG_ENDIAN);
    }

    public static CompoundTag read(InputStream inputStream, ByteOrder byteOrder) throws IOException {
        return NBTIO.read(inputStream, byteOrder, false);
    }

    public static CompoundTag read(InputStream inputStream, ByteOrder byteOrder, boolean bl) throws IOException {
        Throwable throwable = null;
        try (NBTInputStream nBTInputStream = new NBTInputStream(inputStream, byteOrder, bl);){
            Tag tag = Tag.readNamedTag(nBTInputStream);
            if (tag instanceof CompoundTag) {
                CompoundTag compoundTag = (CompoundTag)tag;
                return compoundTag;
            }
            try {
                throw new IOException("Root tag must be a named compound tag");
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public static Tag readNetwork(InputStream inputStream) throws IOException {
        try (NBTInputStream nBTInputStream = new NBTInputStream(inputStream, ByteOrder.LITTLE_ENDIAN, true);){
            Tag tag = Tag.readNamedTag(nBTInputStream);
            return tag;
        }
    }

    public static Tag readTag(InputStream inputStream, ByteOrder byteOrder, boolean bl) throws IOException {
        try (NBTInputStream nBTInputStream = new NBTInputStream(inputStream, byteOrder, bl);){
            Tag tag = Tag.readNamedTag(nBTInputStream);
            return tag;
        }
    }

    public static CompoundTag read(byte[] byArray) throws IOException {
        return NBTIO.read(byArray, ByteOrder.BIG_ENDIAN);
    }

    public static CompoundTag read(byte[] byArray, ByteOrder byteOrder) throws IOException {
        return NBTIO.read(new ByteArrayInputStream(byArray), byteOrder);
    }

    public static CompoundTag read(byte[] byArray, ByteOrder byteOrder, boolean bl) throws IOException {
        return NBTIO.read(new ByteArrayInputStream(byArray), byteOrder, bl);
    }

    public static CompoundTag readCompressed(InputStream inputStream) throws IOException {
        return NBTIO.readCompressed(inputStream, ByteOrder.BIG_ENDIAN);
    }

    public static CompoundTag readCompressed(InputStream inputStream, ByteOrder byteOrder) throws IOException {
        return NBTIO.read(new BufferedInputStream(new GZIPInputStream(inputStream)), byteOrder);
    }

    public static CompoundTag readCompressed(byte[] byArray) throws IOException {
        return NBTIO.readCompressed(byArray, ByteOrder.BIG_ENDIAN);
    }

    public static CompoundTag readCompressed(byte[] byArray, ByteOrder byteOrder) throws IOException {
        return NBTIO.read(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(byArray))), byteOrder, true);
    }

    public static CompoundTag readNetworkCompressed(InputStream inputStream) throws IOException {
        return NBTIO.readNetworkCompressed(inputStream, ByteOrder.BIG_ENDIAN);
    }

    public static CompoundTag readNetworkCompressed(InputStream inputStream, ByteOrder byteOrder) throws IOException {
        return NBTIO.read(new BufferedInputStream(new GZIPInputStream(inputStream)), byteOrder);
    }

    public static CompoundTag readNetworkCompressed(byte[] byArray) throws IOException {
        return NBTIO.readNetworkCompressed(byArray, ByteOrder.BIG_ENDIAN);
    }

    public static CompoundTag readNetworkCompressed(byte[] byArray, ByteOrder byteOrder) throws IOException {
        return NBTIO.read(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(byArray))), byteOrder, true);
    }

    public static byte[] write(CompoundTag compoundTag) throws IOException {
        return NBTIO.write(compoundTag, ByteOrder.BIG_ENDIAN);
    }

    public static byte[] write(CompoundTag compoundTag, ByteOrder byteOrder) throws IOException {
        return NBTIO.write(compoundTag, byteOrder, false);
    }

    public static byte[] write(CompoundTag compoundTag, ByteOrder byteOrder, boolean bl) throws IOException {
        return NBTIO.write((Tag)compoundTag, byteOrder, bl);
    }

    public static byte[] write(Tag tag, ByteOrder byteOrder, boolean bl) throws IOException {
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get().reset();
        try (NBTOutputStream nBTOutputStream = new NBTOutputStream(fastByteArrayOutputStream, byteOrder, bl);){
            Tag.writeNamedTag(tag, nBTOutputStream);
            byte[] byArray = fastByteArrayOutputStream.toByteArray();
            return byArray;
        }
    }

    public static byte[] write(Collection<CompoundTag> collection) throws IOException {
        return NBTIO.write(collection, ByteOrder.BIG_ENDIAN);
    }

    public static byte[] write(Collection<CompoundTag> collection, ByteOrder byteOrder) throws IOException {
        return NBTIO.write(collection, byteOrder, false);
    }

    public static byte[] write(Collection<CompoundTag> collection, ByteOrder byteOrder, boolean bl) throws IOException {
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get().reset();
        try (NBTOutputStream nBTOutputStream = new NBTOutputStream(fastByteArrayOutputStream, byteOrder, bl);){
            for (CompoundTag compoundTag : collection) {
                Tag.writeNamedTag(compoundTag, nBTOutputStream);
            }
            Object object = fastByteArrayOutputStream.toByteArray();
            return object;
        }
    }

    public static void write(CompoundTag compoundTag, File file) throws IOException {
        NBTIO.write(compoundTag, file, ByteOrder.BIG_ENDIAN);
    }

    public static void write(CompoundTag compoundTag, File file, ByteOrder byteOrder) throws IOException {
        NBTIO.write(compoundTag, new FileOutputStream(file), byteOrder);
    }

    public static void write(CompoundTag compoundTag, OutputStream outputStream) throws IOException {
        NBTIO.write(compoundTag, outputStream, ByteOrder.BIG_ENDIAN);
    }

    public static void write(CompoundTag compoundTag, OutputStream outputStream, ByteOrder byteOrder) throws IOException {
        NBTIO.write(compoundTag, outputStream, byteOrder, false);
    }

    public static void write(CompoundTag compoundTag, OutputStream outputStream, ByteOrder byteOrder, boolean bl) throws IOException {
        try (NBTOutputStream nBTOutputStream = new NBTOutputStream(outputStream, byteOrder, bl);){
            Tag.writeNamedTag(compoundTag, nBTOutputStream);
        }
    }

    public static byte[] writeNetwork(Tag tag) throws IOException {
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get().reset();
        try (NBTOutputStream nBTOutputStream = new NBTOutputStream(fastByteArrayOutputStream, ByteOrder.LITTLE_ENDIAN, true);){
            Tag.writeNamedTag(tag, nBTOutputStream);
        }
        return fastByteArrayOutputStream.toByteArray();
    }

    public static byte[] writeGZIPCompressed(CompoundTag compoundTag) throws IOException {
        return NBTIO.writeGZIPCompressed(compoundTag, ByteOrder.BIG_ENDIAN);
    }

    public static byte[] writeGZIPCompressed(CompoundTag compoundTag, ByteOrder byteOrder) throws IOException {
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get().reset();
        NBTIO.writeGZIPCompressed(compoundTag, fastByteArrayOutputStream, byteOrder);
        return fastByteArrayOutputStream.toByteArray();
    }

    public static void writeGZIPCompressed(CompoundTag compoundTag, OutputStream outputStream) throws IOException {
        NBTIO.writeGZIPCompressed(compoundTag, outputStream, ByteOrder.BIG_ENDIAN);
    }

    public static void writeGZIPCompressed(CompoundTag compoundTag, OutputStream outputStream, ByteOrder byteOrder) throws IOException {
        NBTIO.write(compoundTag, new PGZIPOutputStream(outputStream), byteOrder);
    }

    public static byte[] writeNetworkGZIPCompressed(CompoundTag compoundTag) throws IOException {
        return NBTIO.writeNetworkGZIPCompressed(compoundTag, ByteOrder.BIG_ENDIAN);
    }

    public static byte[] writeNetworkGZIPCompressed(CompoundTag compoundTag, ByteOrder byteOrder) throws IOException {
        FastByteArrayOutputStream fastByteArrayOutputStream = ThreadCache.fbaos.get().reset();
        NBTIO.writeNetworkGZIPCompressed(compoundTag, fastByteArrayOutputStream, byteOrder);
        return fastByteArrayOutputStream.toByteArray();
    }

    public static void writeNetworkGZIPCompressed(CompoundTag compoundTag, OutputStream outputStream) throws IOException {
        NBTIO.writeNetworkGZIPCompressed(compoundTag, outputStream, ByteOrder.BIG_ENDIAN);
    }

    public static void writeNetworkGZIPCompressed(CompoundTag compoundTag, OutputStream outputStream, ByteOrder byteOrder) throws IOException {
        NBTIO.write(compoundTag, new PGZIPOutputStream(outputStream), byteOrder, true);
    }

    public static void writeZLIBCompressed(CompoundTag compoundTag, OutputStream outputStream) throws IOException {
        NBTIO.writeZLIBCompressed(compoundTag, outputStream, ByteOrder.BIG_ENDIAN);
    }

    public static void writeZLIBCompressed(CompoundTag compoundTag, OutputStream outputStream, ByteOrder byteOrder) throws IOException {
        NBTIO.writeZLIBCompressed(compoundTag, outputStream, -1, byteOrder);
    }

    public static void writeZLIBCompressed(CompoundTag compoundTag, OutputStream outputStream, int n) throws IOException {
        NBTIO.writeZLIBCompressed(compoundTag, outputStream, n, ByteOrder.BIG_ENDIAN);
    }

    public static void writeZLIBCompressed(CompoundTag compoundTag, OutputStream outputStream, int n, ByteOrder byteOrder) throws IOException {
        NBTIO.write(compoundTag, new DeflaterOutputStream(outputStream, new Deflater(n)), byteOrder);
    }

    public static void safeWrite(CompoundTag compoundTag, File file) throws IOException {
        File file2 = new File(file.getAbsolutePath() + "_tmp");
        if (file2.exists()) {
            file2.delete();
        }
        NBTIO.write(compoundTag, file2);
        Files.move(file2.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }

    private static Throwable a(Throwable throwable) {
        return throwable;
    }
}

