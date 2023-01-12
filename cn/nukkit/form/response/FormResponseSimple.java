/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.form.response;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.response.FormResponse;

public class FormResponseSimple
extends FormResponse {
    private final int clickedButtonId;
    private final ElementButton clickedButton;

    public FormResponseSimple(int n, ElementButton elementButton) {
        this.clickedButtonId = n;
        this.clickedButton = elementButton;
    }

    public int getClickedButtonId() {
        return this.clickedButtonId;
    }

    public ElementButton getClickedButton() {
        return this.clickedButton;
    }
}

