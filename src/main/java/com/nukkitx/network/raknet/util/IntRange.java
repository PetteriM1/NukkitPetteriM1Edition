package com.nukkitx.network.raknet.util;

import com.nukkitx.network.util.Preconditions;

public class IntRange {

    public int start;
    public int end;

    public IntRange(int num) {
        this(num, num);
    }

    public IntRange(int start, int end) {
        Preconditions.checkArgument(start <= end, "start is greater than end");
        this.start = start;
        this.end = end;
    }
}