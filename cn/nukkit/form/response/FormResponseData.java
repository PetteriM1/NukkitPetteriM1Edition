/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.form.response;

public class FormResponseData {
    private final int elementID;
    private final String elementContent;

    public FormResponseData(int n, String string) {
        this.elementID = n;
        this.elementContent = string;
    }

    public int getElementID() {
        return this.elementID;
    }

    public String getElementContent() {
        return this.elementContent;
    }
}

