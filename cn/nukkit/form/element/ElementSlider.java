/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.form.element;

import cn.nukkit.form.element.Element;
import com.google.gson.annotations.SerializedName;

public class ElementSlider
extends Element {
    private final String type;
    private String text = "";
    private float min = 0.0f;
    private float max = 100.0f;
    private int step;
    @SerializedName(value="default")
    private float defaultValue;

    public ElementSlider(String string, float f2, float f3) {
        this(string, f2, f3, -1);
    }

    public ElementSlider(String string, float f2, float f3, int n) {
        this(string, f2, f3, n, -1.0f);
    }

    public ElementSlider(String string, float f2, float f3, int n, float f4) {
        this.type = "slider";
        this.text = string;
        this.min = Math.max(f2, 0.0f);
        this.max = Math.max(f3, this.min);
        if ((float)n != -1.0f && n > 0) {
            this.step = n;
        }
        if (f4 != -1.0f) {
            this.defaultValue = f4;
        }
    }

    public String getText() {
        return this.text;
    }

    public void setText(String string) {
        this.text = string;
    }

    public float getMin() {
        return this.min;
    }

    public void setMin(float f2) {
        this.min = f2;
    }

    public float getMax() {
        return this.max;
    }

    public void setMax(float f2) {
        this.max = f2;
    }

    public int getStep() {
        return this.step;
    }

    public void setStep(int n) {
        this.step = n;
    }

    public float getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(float f2) {
        this.defaultValue = f2;
    }
}

