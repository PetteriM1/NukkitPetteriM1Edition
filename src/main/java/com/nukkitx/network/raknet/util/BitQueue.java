package com.nukkitx.network.raknet.util;

import com.nukkitx.network.raknet.RakNetUtils;

public class BitQueue {

    private byte[] queue;
    private int head;
    private int tail;

    public BitQueue(int capacity) {
        capacity = RakNetUtils.powerOfTwoCeiling(capacity);
        if (capacity <= 0) {
            capacity = 8;
        }

        this.queue = new byte[((capacity + 7) >> 3)];
        this.head = 0;
        this.tail = 0;
    }

    public void add(boolean bit) {
        if (((this.head + 1) & ((this.queue.length << 3) - 1)) == this.tail) {
            this.resize(this.queue.length << 4);
        }

        int by = this.head >> 3;
        byte bi = (byte) (1 << (this.head & 7));
        this.queue[by] ^= (byte) (((bit ? 0xFF : 0x00) ^ this.queue[by]) & bi);
        this.head = (this.head + 1) & ((this.queue.length << 3) - 1);
    }

    private void resize(int capacity) {
        byte[] newQueue = new byte[(capacity + 7) >> 3];
        int size = this.size();

        if ((this.tail & 7) == 0) {
            if (this.head > this.tail) {
                int srcPos = this.tail >> 3;
                int length = (this.head - this.tail + 7) >> 3;
                System.arraycopy(this.queue, srcPos, newQueue, 0, length);
            } else if (this.head < this.tail) {
                int length = this.tail >> 3;
                int adjustedPos = ((this.queue.length << 3) - this.tail + 7) >> 3;
                System.arraycopy(this.queue, length, newQueue, 0, adjustedPos);
                length = (this.head + 7) >> 3;
                System.arraycopy(this.queue, 0, newQueue, adjustedPos, length);
            }

            this.tail = 0;
            this.head = size;
        } else {
            int tailBits = (this.tail & 7);
            int tailIdx = this.tail >> 3;
            int by2 = (tailIdx + 1) & (this.queue.length - 1);
            int mask;
            int bit1;
            int bit2;

            int cursor = 0;
            while (cursor < size) {
                mask = ((1 << tailBits) - 1) & 0xFF;
                bit1 = ((this.queue[tailIdx] & (~mask & 0xFF)) >>> tailBits);
                bit2 = (this.queue[by2] << (8 - tailBits));
                newQueue[cursor >> 3] = (byte) (bit1 | bit2);

                cursor += 8;
                tailIdx = (tailIdx + 1) & (this.queue.length - 1);
                by2 = (by2 + 1) & (this.queue.length - 1);
            }

            this.tail = 0;
            this.head = size;
        }

        this.queue = newQueue;
    }

    public int size() {
        if (this.head > this.tail) {
            return (this.head - this.tail);
        } else if (this.head < this.tail) {
            return ((this.queue.length << 3) - (this.tail - this.head));
        } else {
            return 0;
        }
    }

    public void set(int n, boolean bit) {
        if (n >= this.size() || n < 0) {
            return;
        }

        int idx = (this.tail + n) & ((this.queue.length << 3) - 1);
        int arrIdx = idx >> 3;
        byte mask = (byte) (1 << (idx & 7));
        this.queue[arrIdx] ^= (byte) (((bit ? 0xFF : 0x00) ^ this.queue[arrIdx]) & mask);
    }

    public boolean get(int n) {
        if (n >= this.size() || n < 0) {
            return false;
        }

        int idx = (this.tail + n) & ((this.queue.length << 3) - 1);
        int arrIdx = idx >> 3;
        byte mask = (byte) (1 << (idx & 7));
        return (this.queue[arrIdx] & mask) != 0;
    }

    public boolean isEmpty() {
        return (this.head == this.tail);
    }

    public boolean peek() {
        if (this.head == this.tail) {
            return false;
        }

        int arrIdx = this.tail >> 3;
        byte mask = (byte) (1 << ((this.tail) & 7));
        return (this.queue[arrIdx] & mask) != 0;
    }

    public boolean poll() {
        if (this.head == this.tail) {
            return false;
        }

        int arrIdx = this.tail >> 3;
        byte mask = (byte) (1 << ((this.tail) & 7));
        this.tail = (this.tail + 1) & ((this.queue.length << 3) - 1);
        return (this.queue[arrIdx] & mask) != 0;
    }
}
