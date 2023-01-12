/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.form.element;

import cn.nukkit.form.element.Element;

public class ElementLabel
extends Element {
    private final String type;
    private String text = "";

    public ElementLabel(String string) {
        this.type = "label";
        this.text = string;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String string) {
        this.text = string;
    }
}

