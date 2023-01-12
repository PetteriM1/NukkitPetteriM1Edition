/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.form.element;

import cn.nukkit.form.element.Element;
import com.google.gson.annotations.SerializedName;

public class ElementToggle
extends Element {
    private final String type;
    private String text;
    @SerializedName(value="default")
    private boolean defaultValue;

    public ElementToggle(String string) {
        this(string, false);
    }

    public ElementToggle(String string, boolean bl) {
        this.type = "toggle";
        this.text = string;
        this.defaultValue = bl;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String string) {
        this.text = string;
    }

    public boolean isDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(boolean bl) {
        this.defaultValue = bl;
    }
}

