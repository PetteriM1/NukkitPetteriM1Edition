/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.math.NukkitRandom;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.channels.spi.AbstractInterruptibleChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.SplittableRandom;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Utils {
    public static final SplittableRandom random = new SplittableRandom();
    public static final NukkitRandom nukkitRandom = new NukkitRandom();
    public static final int[] emptyDamageArray = new int[]{0, 0, 0, 0};
    public static final IntSet monstersList = new IntOpenHashSet(new int[]{43, 40, 33, 110, 50, 38, 55, 104, 41, 49, 124, 47, 127, 114, 59, 54, 39, 34, 37, 35, 46, 105, 57, 45, 52, 48, 126, 32, 36, 44, 116});
    public static final IntSet freezingBiomes = new IntOpenHashSet(new int[]{10, 11, 12, 26, 30, 31, 140, 158});
    public static int[] faces2534 = new int[]{2, 5, 3, 4};

    public static void writeFile(String string, String string2) throws IOException {
        Utils.writeFile(string, (InputStream)new ByteArrayInputStream(string2.getBytes(StandardCharsets.UTF_8)));
    }

    public static void writeFile(String string, InputStream inputStream) throws IOException {
        Utils.writeFile(new File(string), inputStream);
    }

    public static void writeFile(File file, String string) throws IOException {
        Utils.writeFile(file, (InputStream)new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8)));
    }

    public static void writeFile(File file, InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("content must not be null");
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);){
            int n;
            byte[] byArray = new byte[1024];
            while ((n = inputStream.read(byArray)) != -1) {
                fileOutputStream.write(byArray, 0, n);
            }
        }
        inputStream.close();
    }

    public static String readFile(File file) throws IOException {
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException();
        }
        return Utils.readFile(new FileInputStream(file));
    }

    public static String readFile(String string) throws IOException {
        File file = new File(string);
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException();
        }
        return Utils.readFile(new FileInputStream(file));
    }

    public static String readFile(InputStream inputStream) throws IOException {
        return Utils.a(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    private static String a(Reader reader) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(reader);){
            StringBuilder stringBuilder = new StringBuilder();
            String string = bufferedReader.readLine();
            while (string != null) {
                if (stringBuilder.length() != 0) {
                    stringBuilder.append('\n');
                }
                stringBuilder.append(string);
                string = bufferedReader.readLine();
            }
            String string2 = stringBuilder.toString();
            return string2;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void copyFile(File file, File file2) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        if (file.isDirectory() || file2.isDirectory()) {
            throw new FileNotFoundException();
        }
        FileInputStream fileInputStream = null;
        AbstractInterruptibleChannel abstractInterruptibleChannel = null;
        FileOutputStream fileOutputStream = null;
        AbstractInterruptibleChannel abstractInterruptibleChannel2 = null;
        try {
            if (!file2.exists()) {
                file2.createNewFile();
            }
            fileInputStream = new FileInputStream(file);
            abstractInterruptibleChannel = fileInputStream.getChannel();
            fileOutputStream = new FileOutputStream(file2);
            abstractInterruptibleChannel2 = fileOutputStream.getChannel();
            ((FileChannel)abstractInterruptibleChannel).transferTo(0L, ((FileChannel)abstractInterruptibleChannel).size(), (WritableByteChannel)((Object)abstractInterruptibleChannel2));
        }
        finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (abstractInterruptibleChannel != null) {
                abstractInterruptibleChannel.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            if (abstractInterruptibleChannel2 != null) {
                abstractInterruptibleChannel2.close();
            }
        }
    }

    public static String getAllThreadDumps() {
        ThreadInfo[] threadInfoArray = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);
        StringBuilder stringBuilder = new StringBuilder();
        for (ThreadInfo threadInfo : threadInfoArray) {
            stringBuilder.append('\n').append(threadInfo);
        }
        return stringBuilder.toString();
    }

    public static String getExceptionMessage(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        try (PrintWriter printWriter = new PrintWriter(stringWriter);){
            throwable.printStackTrace(printWriter);
            printWriter.flush();
        }
        return stringWriter.toString();
    }

    public static UUID dataToUUID(String ... stringArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : stringArray) {
            stringBuilder.append(string);
        }
        return UUID.nameUUIDFromBytes(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static UUID dataToUUID(byte[] ... byArray) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (byte[] byArray2 : byArray) {
            try {
                byteArrayOutputStream.write(byArray2);
            }
            catch (IOException iOException) {
                break;
            }
        }
        return UUID.nameUUIDFromBytes(byteArrayOutputStream.toByteArray());
    }

    public static String rtrim(String string, char c2) {
        int n;
        for (n = string.length() - 1; n >= 0 && string.charAt(n) == c2; --n) {
        }
        return string.substring(0, n + 1);
    }

    public static boolean isByteArrayEmpty(byte[] byArray) {
        for (byte by : byArray) {
            if (by == 0) continue;
            return false;
        }
        return true;
    }

    public static long toRGB(byte by, byte by2, byte by3, byte by4) {
        long l = by & 0xFF;
        l |= (long)((by2 & 0xFF) << 8);
        l |= (long)((by3 & 0xFF) << 16);
        return (l |= (long)((by4 & 0xFF) << 24)) & 0xFFFFFFFFL;
    }

    public static long toABGR(int n) {
        long l = (long)n & 0xFF00FF00L;
        l |= (long)(n << 16) & 0xFF0000L;
        return (l |= (long)(n >>> 16) & 0xFFL) & 0xFFFFFFFFL;
    }

    public static Object[][] splitArray(Object[] objectArray, int n) {
        Object[][] objectArray2;
        block2: {
            if (n <= 0) {
                return null;
            }
            int n2 = objectArray.length % n;
            int n3 = objectArray.length / n + (n2 > 0 ? 1 : 0);
            objectArray2 = new Object[n3][];
            for (int k = 0; k < (n2 > 0 ? n3 - 1 : n3); ++k) {
                objectArray2[k] = Arrays.copyOfRange(objectArray, k * n, k * n + n);
            }
            if (n2 <= 0) break block2;
            objectArray2[n3 - 1] = Arrays.copyOfRange(objectArray, (n3 - 1) * n, (n3 - 1) * n + n2);
        }
        return objectArray2;
    }

    public static <T> void reverseArray(T[] TArray) {
        Utils.reverseArray(TArray, false);
    }

    public static <T> T[] reverseArray(T[] TArray, boolean bl) {
        T[] TArray2 = TArray;
        if (bl) {
            TArray2 = Arrays.copyOf(TArray, TArray.length);
        }
        int n = 0;
        for (int k = TArray2.length - 1; n < k; ++n, --k) {
            T t = TArray2[n];
            TArray2[n] = TArray2[k];
            TArray2[k] = t;
        }
        return TArray2;
    }

    public static <T> T[][] clone2dArray(T[][] TArray) {
        Object[][] objectArray = (Object[][])Arrays.copyOf(TArray, TArray.length);
        for (int k = 0; k < TArray.length; ++k) {
            objectArray[k] = Arrays.copyOf(TArray[k], TArray[k].length);
        }
        return objectArray;
    }

    public static <T, U, V> Map<U, V> getOrCreate(Map<T, Map<U, V>> map, T t) {
        ConcurrentHashMap concurrentHashMap;
        ConcurrentHashMap concurrentHashMap2 = map.get(t);
        if (concurrentHashMap2 == null && (concurrentHashMap2 = (ConcurrentHashMap)map.putIfAbsent(t, concurrentHashMap = new ConcurrentHashMap())) == null) {
            concurrentHashMap2 = concurrentHashMap;
        }
        return concurrentHashMap2;
    }

    public static <T, U, V extends U> U getOrCreate(Map<T, U> map, Class<V> clazz, T t) {
        U u = map.get(t);
        if (u != null) {
            return u;
        }
        try {
            V v = clazz.newInstance();
            u = map.putIfAbsent(t, v);
            if (u == null) {
                return (U)v;
            }
            return u;
        }
        catch (IllegalAccessException | InstantiationException reflectiveOperationException) {
            throw new RuntimeException(reflectiveOperationException);
        }
    }

    public static int toInt(Object object) {
        if (object instanceof Integer) {
            return (Integer)object;
        }
        return (int)Math.round((Double)object);
    }

    public static byte[] parseHexBinary(String string) {
        int n = string.length();
        if (n % 2 != 0) {
            throw new IllegalArgumentException("hexBinary needs to be even-length: " + string);
        }
        byte[] byArray = new byte[n >> 1];
        for (int k = 0; k < n; k += 2) {
            int n2 = Utils.a(string.charAt(k));
            int n3 = Utils.a(string.charAt(k + 1));
            if (n2 == -1 || n3 == -1) {
                throw new IllegalArgumentException("contains illegal character for hexBinary: " + string);
            }
            byArray[k >> 1] = (byte)((n2 << 4) + n3);
        }
        return byArray;
    }

    private static int a(char c2) {
        if ('0' <= c2 && c2 <= '9') {
            return c2 - 48;
        }
        if ('A' <= c2 && c2 <= 'F') {
            return c2 - 65 + 10;
        }
        if ('a' <= c2 && c2 <= 'f') {
            return c2 - 97 + 10;
        }
        return -1;
    }

    public static int rand(int n, int n2) {
        if (n == n2) {
            return n2;
        }
        return random.nextInt(n2 + 1 - n) + n;
    }

    public static double rand(double d2, double d3) {
        if (d2 == d3) {
            return d3;
        }
        return d2 + random.nextDouble() * (d3 - d2);
    }

    public static boolean rand() {
        return random.nextBoolean();
    }

    public static String getVersionByProtocol(int n) {
        switch (n) {
            case 388: {
                return "1.13.0";
            }
            case 389: {
                return "1.14.0";
            }
            case 390: {
                return "1.14.60";
            }
            case 407: {
                return "1.16.0";
            }
            case 408: {
                return "1.16.20";
            }
            case 409: 
            case 410: 
            case 411: 
            case 419: {
                return "1.16.100";
            }
            case 420: 
            case 422: {
                return "1.16.200";
            }
            case 423: 
            case 424: 
            case 428: {
                return "1.16.210";
            }
            case 431: {
                return "1.16.220";
            }
            case 433: 
            case 434: 
            case 435: {
                return "1.16.230";
            }
            case 440: {
                return "1.17.0";
            }
            case 448: {
                return "1.17.10";
            }
            case 453: {
                return "1.17.20";
            }
            case 465: {
                return "1.17.30";
            }
            case 471: {
                return "1.17.40";
            }
            case 474: 
            case 475: 
            case 476: {
                return "1.18.0";
            }
            case 485: 
            case 486: {
                return "1.18.10";
            }
            case 503: {
                return "1.18.30";
            }
            case 524: 
            case 526: 
            case 527: {
                return "1.19.0";
            }
            case 534: {
                return "1.19.10";
            }
            case 544: {
                return "1.19.20";
            }
            case 545: {
                return "1.19.21";
            }
            case 553: 
            case 554: {
                return "1.19.30";
            }
            case 557: {
                return "1.19.40";
            }
            case 558: 
            case 560: {
                return "1.19.50";
            }
        }
        throw new IllegalArgumentException("Invalid protocol: " + n);
    }

    public static String getOS(Player player) {
        switch (player.getLoginChainData().getDeviceOS()) {
            case 1: {
                return "Android";
            }
            case 2: {
                return "iOS";
            }
            case 3: {
                return "macOS";
            }
            case 4: {
                return "Fire OS";
            }
            case 5: {
                return "Gear VR";
            }
            case 6: {
                return "HoloLens";
            }
            case 7: {
                return "Windows 10";
            }
            case 8: {
                return "Windows";
            }
            case 9: {
                return "Dedicated";
            }
            case 10: {
                return "tvOS";
            }
            case 11: {
                return "PlayStation";
            }
            case 12: {
                return "Switch";
            }
            case 13: {
                return "Xbox";
            }
            case 14: {
                return "Windows Phone";
            }
        }
        return "Unknown";
    }

    private static Throwable a(Throwable throwable) {
        return throwable;
    }
}

