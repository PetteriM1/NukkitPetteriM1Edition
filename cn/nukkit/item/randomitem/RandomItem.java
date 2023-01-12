/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.randomitem;

import cn.nukkit.item.randomitem.Selector;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class RandomItem {
    private static final Map<Selector, Float> a = new HashMap<Selector, Float>();
    public static final Selector ROOT = new Selector(null);

    public static Selector putSelector(Selector selector) {
        return RandomItem.putSelector(selector, 1.0f);
    }

    public static Selector putSelector(Selector selector, float f2) {
        if (selector.getParent() == null) {
            selector.setParent(ROOT);
        }
        a.put(selector, Float.valueOf(f2));
        return selector;
    }

    static Object a(Selector selector) {
        Objects.requireNonNull(selector);
        HashMap<Selector, Float> hashMap = new HashMap<Selector, Float>();
        a.forEach((selector2, f2) -> {
            if (selector2.getParent() == selector) {
                hashMap.put((Selector)selector2, (Float)f2);
            }
        });
        if (hashMap.isEmpty()) {
            return selector.select();
        }
        return RandomItem.a(Selector.selectRandom(hashMap));
    }
}

