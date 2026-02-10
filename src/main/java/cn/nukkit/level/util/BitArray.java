package cn.nukkit.level.util;

public interface BitArray {

    BitArray copy();

    int get(int index);

    BitArrayVersion getVersion();

    int[] getWords();

    void set(int index, int value);

    int size();
}
