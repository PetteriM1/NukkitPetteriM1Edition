/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.BookEditPacket;

class c {
    static final int[] a = new int[BookEditPacket.Action.values().length];

    static {
        try {
            c.a[BookEditPacket.Action.REPLACE_PAGE.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            c.a[BookEditPacket.Action.ADD_PAGE.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            c.a[BookEditPacket.Action.DELETE_PAGE.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            c.a[BookEditPacket.Action.SWAP_PAGES.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            c.a[BookEditPacket.Action.SIGN_BOOK.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}

