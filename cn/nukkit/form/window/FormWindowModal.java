/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.form.window;

import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.window.FormWindow;

public class FormWindowModal
extends FormWindow {
    private final String type;
    private String title = "";
    private String content = "";
    private String button1 = "";
    private String button2 = "";
    private FormResponseModal response = null;

    public FormWindowModal(String string, String string2, String string3, String string4) {
        this.type = "modal";
        this.title = string;
        this.content = string2;
        this.button1 = string3;
        this.button2 = string4;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String string) {
        this.title = string;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String string) {
        this.content = string;
    }

    public String getButton1() {
        return this.button1;
    }

    public void setButton1(String string) {
        this.button1 = string;
    }

    public String getButton2() {
        return this.button2;
    }

    public void setButton2(String string) {
        this.button2 = string;
    }

    @Override
    public FormResponseModal getResponse() {
        return this.response;
    }

    @Override
    public void setResponse(String string) {
        if (string.equals("null")) {
            this.closed = true;
            return;
        }
        this.response = string.equals("true") ? new FormResponseModal(0, this.button1) : new FormResponseModal(1, this.button2);
    }
}

