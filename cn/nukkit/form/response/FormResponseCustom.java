/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.form.response;

import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseData;
import java.util.HashMap;

public class FormResponseCustom
extends FormResponse {
    private final HashMap<Integer, Object> responses;
    private final HashMap<Integer, FormResponseData> dropdownResponses;
    private final HashMap<Integer, String> inputResponses;
    private final HashMap<Integer, Float> sliderResponses;
    private final HashMap<Integer, FormResponseData> stepSliderResponses;
    private final HashMap<Integer, Boolean> toggleResponses;
    private final HashMap<Integer, String> labelResponses;

    public FormResponseCustom(HashMap<Integer, Object> hashMap, HashMap<Integer, FormResponseData> hashMap2, HashMap<Integer, String> hashMap3, HashMap<Integer, Float> hashMap4, HashMap<Integer, FormResponseData> hashMap5, HashMap<Integer, Boolean> hashMap6, HashMap<Integer, String> hashMap7) {
        this.responses = hashMap;
        this.dropdownResponses = hashMap2;
        this.inputResponses = hashMap3;
        this.sliderResponses = hashMap4;
        this.stepSliderResponses = hashMap5;
        this.toggleResponses = hashMap6;
        this.labelResponses = hashMap7;
    }

    public HashMap<Integer, Object> getResponses() {
        return this.responses;
    }

    public Object getResponse(int n) {
        return this.responses.get(n);
    }

    public FormResponseData getDropdownResponse(int n) {
        return this.dropdownResponses.get(n);
    }

    public String getInputResponse(int n) {
        return this.inputResponses.get(n);
    }

    public float getSliderResponse(int n) {
        return this.sliderResponses.get(n).floatValue();
    }

    public FormResponseData getStepSliderResponse(int n) {
        return this.stepSliderResponses.get(n);
    }

    public boolean getToggleResponse(int n) {
        return this.toggleResponses.get(n);
    }

    public String getLabelResponse(int n) {
        return this.labelResponses.get(n);
    }
}

