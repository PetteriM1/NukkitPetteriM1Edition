/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.randomitem;

import cn.nukkit.item.Item;
import cn.nukkit.item.randomitem.Selector;

public class ConstantItemSelector
extends Selector {
    protected final Item item;

    public ConstantItemSelector(int n, Selector selector) {
        this(n, 0, selector);
    }

    public ConstantItemSelector(int n, Integer n2, Selector selector) {
        this(n, n2, 1, selector);
    }

    public ConstantItemSelector(int n, int n2, Selector selector) {
        this(n, n2, 1, selector);
    }

    public ConstantItemSelector(int n, Integer n2, int n3, Selector selector) {
        this(Item.get(n, n2, n3), selector);
    }

    public ConstantItemSelector(int n, int n2, int n3, Selector selector) {
        this(Item.get(n, n2, n3), selector);
    }

    public ConstantItemSelector(Item item, Selector selector) {
        super(selector);
        this.item = item;
    }

    public Item getItem() {
        return this.item;
    }

    @Override
    public Object select() {
        return this.item;
    }
}

