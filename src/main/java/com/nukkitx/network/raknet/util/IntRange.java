package com.nukkitx.network.raknet.util;

public class IntRange {

    public int start;
    public int end;

    public IntRange(int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException("Start is greater than end");
        }

        this.start = start;
        this.end = end;
    }
}
