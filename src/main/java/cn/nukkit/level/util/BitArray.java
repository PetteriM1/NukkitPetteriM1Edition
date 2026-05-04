package cn.nukkit.level.util;

public interface BitArray {

    BitArrayVersion getVersion();

    int[] getWords();

    BitArray copy();

    int get(int index);

    void set(int index, int value);

    int size();
}
