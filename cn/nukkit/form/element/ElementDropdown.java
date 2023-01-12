/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.form.element;

import cn.nukkit.form.element.Element;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class ElementDropdown
extends Element {
    private final String type;
    private String text = "";
    private List<String> options;
    @SerializedName(value="default")
    private int defaultOptionIndex = 0;

    public ElementDropdown(String string) {
        this(string, new ArrayList<String>());
    }

    public ElementDropdown(String string, List<String> list) {
        this(string, list, 0);
    }

    public ElementDropdown(String string, List<String> list, int n) {
        this.type = "dropdown";
        this.text = string;
        this.options = list;
        this.defaultOptionIndex = n;
    }

    public int getDefaultOptionIndex() {
        return this.defaultOptionIndex;
    }

    public void setDefaultOptionIndex(int n) {
        if (n >= this.options.size()) {
            return;
        }
        this.defaultOptionIndex = n;
    }

    public List<String> getOptions() {
        return this.options;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String string) {
        this.text = string;
    }

    public void addOption(String string) {
        this.addOption(string, false);
    }

    public void addOption(String string, boolean bl) {
        this.options.add(string);
        if (bl) {
            this.defaultOptionIndex = this.options.size() - 1;
        }
    }
}

