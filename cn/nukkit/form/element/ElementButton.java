/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.form.element;

import cn.nukkit.form.element.ElementButtonImageData;

public class ElementButton {
    private String text = "";
    private ElementButtonImageData image;

    public ElementButton(String string) {
        this.text = string;
    }

    public ElementButton(String string, ElementButtonImageData elementButtonImageData) {
        this.text = string;
        if (!elementButtonImageData.getData().isEmpty() && !elementButtonImageData.getType().isEmpty()) {
            this.image = elementButtonImageData;
        }
    }

    public String getText() {
        return this.text;
    }

    public void setText(String string) {
        this.text = string;
    }

    public ElementButtonImageData getImage() {
        return this.image;
    }

    public void addImage(ElementButtonImageData elementButtonImageData) {
        if (!elementButtonImageData.getData().isEmpty() && !elementButtonImageData.getType().isEmpty()) {
            this.image = elementButtonImageData;
        }
    }
}

