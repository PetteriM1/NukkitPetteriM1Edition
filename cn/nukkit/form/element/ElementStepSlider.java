/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.form.element;

import cn.nukkit.form.element.Element;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class ElementStepSlider
extends Element {
    private final String type;
    private String text = "";
    private List<String> steps;
    @SerializedName(value="default")
    private int defaultStepIndex = 0;

    public ElementStepSlider(String string) {
        this(string, new ArrayList<String>());
    }

    public ElementStepSlider(String string, List<String> list) {
        this(string, list, 0);
    }

    public ElementStepSlider(String string, List<String> list, int n) {
        this.type = "step_slider";
        this.text = string;
        this.steps = list;
        this.defaultStepIndex = n;
    }

    public int getDefaultStepIndex() {
        return this.defaultStepIndex;
    }

    public void setDefaultOptionIndex(int n) {
        if (n >= this.steps.size()) {
            return;
        }
        this.defaultStepIndex = n;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String string) {
        this.text = string;
    }

    public List<String> getSteps() {
        return this.steps;
    }

    public void addStep(String string) {
        this.addStep(string, false);
    }

    public void addStep(String string, boolean bl) {
        this.steps.add(string);
        if (bl) {
            this.defaultStepIndex = this.steps.size() - 1;
        }
    }
}

