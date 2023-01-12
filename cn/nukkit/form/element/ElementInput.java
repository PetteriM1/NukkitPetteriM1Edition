/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.form.element;

import cn.nukkit.form.element.Element;
import com.google.gson.annotations.SerializedName;

public class ElementInput
extends Element {
    private final String type;
    private String text = "";
    private String placeholder = "";
    @SerializedName(value="default")
    private String defaultText = "";

    public ElementInput(String string) {
        this(string, "");
    }

    public ElementInput(String string, String string2) {
        this(string, string2, "");
    }

    public ElementInput(String string, String string2, String string3) {
        this.type = "input";
        this.text = string;
        this.placeholder = string2;
        this.defaultText = string3;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String string) {
        this.text = string;
    }

    public String getPlaceHolder() {
        return this.placeholder;
    }

    public void setPlaceHolder(String string) {
        this.placeholder = string;
    }

    public String getDefaultText() {
        return this.defaultText;
    }

    public void setDefaultText(String string) {
        this.defaultText = string;
    }
}

