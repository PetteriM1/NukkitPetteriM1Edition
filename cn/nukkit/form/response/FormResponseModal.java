/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.form.response;

import cn.nukkit.form.response.FormResponse;

public class FormResponseModal
extends FormResponse {
    private final int clickedButtonId;
    private final String clickedButtonText;

    public FormResponseModal(int n, String string) {
        this.clickedButtonId = n;
        this.clickedButtonText = string;
    }

    public int getClickedButtonId() {
        return this.clickedButtonId;
    }

    public String getClickedButtonText() {
        return this.clickedButtonText;
    }
}

