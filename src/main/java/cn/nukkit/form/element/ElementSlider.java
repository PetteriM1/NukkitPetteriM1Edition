package cn.nukkit.form.element;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class ElementSlider extends Element {

    @SuppressWarnings("unused")
    private final String type = "slider";
    private String text = "";
    private float min = 0f;
    private float max = 100f;
    private int step;
    @SerializedName("default")
    private float defaultValue;
    private String tooltip = null;

    public ElementSlider(String text, float min, float max) {
        this(text, min, max, -1);
    }

    public ElementSlider(String text, float min, float max, int step) {
        this(text, min, max, step, -1);
    }

    public ElementSlider(String text, float min, float max, int step, float defaultValue) {
        this.text = text;
        this.min = Math.max(min, 0f);
        this.max = Math.max(max, this.min);
        if (step != -1f && step > 0) this.step = step;
        if (defaultValue != -1f) this.defaultValue = defaultValue;
    }

    public void setDefaultValue(float defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public float getDefaultValue() {
        return defaultValue;
    }

    public float getMax() {
        return max;
    }

    public float getMin() {
        return min;
    }

    public int getStep() {
        return step;
    }

    public String getText() {
        return text;
    }

    @Nullable
    public String getTooltip() {
        return tooltip;
    }
}
