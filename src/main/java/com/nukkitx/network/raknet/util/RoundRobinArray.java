package com.nukkitx.network.raknet.util;

import com.nukkitx.network.raknet.RakNetUtils;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Consumer;

public class RoundRobinArray<E> {

    private final AtomicReferenceArray<E> elements;
    private final int mask;

    public RoundRobinArray(int fixedCapacity) {
        fixedCapacity = RakNetUtils.powerOfTwoCeiling(fixedCapacity);

        this.elements = new AtomicReferenceArray<>(fixedCapacity);
        this.mask = fixedCapacity - 1;
    }

    public E get(int index) {
        return this.elements.get(index & this.mask);
    }

    public void set(int index, E value) {
        int idx = index & this.mask;
        Object element = this.elements.getAndSet(idx, value);
        // Make sure to release any reference counted objects that get overwritten.
        ReferenceCountUtil.release(element);
    }

    public boolean remove(int index, E expected) {
        int idx = index & this.mask;
        return this.elements.compareAndSet(idx, expected, null);
    }

    public void forEach(Consumer<E> consumer) {
        for (int i = 0, len = mask + 1; i < len; i++) {
            consumer.accept(this.elements.get(i));
        }
    }
}
