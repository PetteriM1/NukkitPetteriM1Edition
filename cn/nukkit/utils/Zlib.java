/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.ZlibOriginal;
import cn.nukkit.utils.ZlibSingleThreadLowMem;
import cn.nukkit.utils.ZlibThreadLocal;
import cn.nukkit.utils.e;
import java.io.IOException;

public abstract class Zlib {
    private static final e[] b = new e[3];
    private static e a;

    public static void setProvider(int n) {
        block5: {
            switch (n) {
                case 0: {
                    Zlib.b[n] = new ZlibOriginal();
                    break;
                }
                case 1: {
                    Zlib.b[n] = new ZlibSingleThreadLowMem();
                    break;
                }
                case 2: {
                    Zlib.b[n] = new ZlibThreadLocal();
                    break;
                }
                default: {
                    throw new UnsupportedOperationException("Invalid Zlib provider: " + n);
                }
            }
            a = b[n];
            if (a instanceof ZlibThreadLocal) break block5;
            MainLogger.getLogger().warning("Selected Zlib provider (" + a.getClass().getCanonicalName() + ") may affect performance negatively. Please consider setting zlib-provider=2 in server.properties");
        }
    }

    public static byte[] deflate(byte[] byArray) throws Exception {
        return Zlib.deflate(byArray, -1);
    }

    public static byte[] deflate(byte[] byArray, int n) throws Exception {
        return a.deflate(byArray, n);
    }

    public static byte[] deflate(byte[][] byArray, int n) throws Exception {
        return a.deflate(byArray, n);
    }

    public static byte[] deflateRaw(byte[] byArray, int n) throws Exception {
        return a.deflateRaw(byArray, n);
    }

    public static byte[] deflateRaw(byte[][] byArray, int n) throws Exception {
        return a.deflateRaw(byArray, n);
    }

    public static byte[] inflate(byte[] byArray) throws IOException {
        return Zlib.inflate(byArray, -1);
    }

    public static byte[] inflate(byte[] byArray, int n) throws IOException {
        return a.inflate(byArray, n);
    }

    public static byte[] inflateRaw(byte[] byArray, int n) throws IOException {
        return a.inflateRaw(byArray, n);
    }

    private static UnsupportedOperationException a(UnsupportedOperationException unsupportedOperationException) {
        return unsupportedOperationException;
    }
}

