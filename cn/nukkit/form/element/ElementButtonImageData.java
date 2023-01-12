/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.form.element;

public class ElementButtonImageData {
    public static final String IMAGE_DATA_TYPE_PATH;
    public static final String IMAGE_DATA_TYPE_URL;
    private String type;
    private String data;

    public ElementButtonImageData(String string, String string2) {
        if (!string.equals("url") && !string.equals("path")) {
            return;
        }
        this.type = string;
        this.data = string2;
    }

    public String getType() {
        return this.type;
    }

    public String getData() {
        return this.data;
    }

    public void setType(String string) {
        this.type = string;
    }

    public void setData(String string) {
        this.data = string;
    }

    static {
        IMAGE_DATA_TYPE_URL = "url";
        IMAGE_DATA_TYPE_PATH = "path";
    }
}

