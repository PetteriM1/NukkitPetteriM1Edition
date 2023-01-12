/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.randomitem;

import java.util.Map;

public class Selector {
    private Selector a;

    public Selector(Selector selector) {
        this.setParent(selector);
    }

    public Selector setParent(Selector selector) {
        this.a = selector;
        return selector;
    }

    public Selector getParent() {
        return this.a;
    }

    public Object select() {
        return this;
    }

    public static Selector selectRandom(Map<Selector, Float> map) {
        float[] fArray = new float[]{0.0f};
        map.values().forEach(f2 -> {
            fArray[0] = fArray[0] + f2.floatValue();
        });
        float f4 = (float)(Math.random() * (double)fArray[0]);
        float[] fArray2 = new float[]{0.0f};
        boolean[] blArray = new boolean[]{false};
        Selector[] selectorArray = new Selector[]{null};
        map.forEach((selector, f3) -> {
            fArray[0] = fArray2[0] + f3.floatValue();
            if (fArray2[0] > f4 && !blArray[0]) {
                selectorArray[0] = selector;
                blArray[0] = true;
            }
        });
        return selectorArray[0];
    }
}

