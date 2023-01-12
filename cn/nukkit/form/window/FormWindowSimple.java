/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.form.window;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindow;
import java.util.ArrayList;
import java.util.List;

public class FormWindowSimple
extends FormWindow {
    private final String type;
    private String title = "";
    private String content = "";
    private List<ElementButton> buttons;
    private FormResponseSimple response = null;

    public FormWindowSimple(String string, String string2) {
        this(string, string2, new ArrayList<ElementButton>());
    }

    public FormWindowSimple(String string, String string2, List<ElementButton> list) {
        this.type = "form";
        this.title = string;
        this.content = string2;
        this.buttons = list;
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

    public List<ElementButton> getButtons() {
        return this.buttons;
    }

    public void addButton(ElementButton elementButton) {
        this.buttons.add(elementButton);
    }

    @Override
    public FormResponseSimple getResponse() {
        return this.response;
    }

    @Override
    public void setResponse(String string) {
        int n;
        if (string.equals("null")) {
            this.closed = true;
            return;
        }
        try {
            n = Integer.parseInt(string);
        }
        catch (Exception exception) {
            return;
        }
        if (n >= this.buttons.size()) {
            this.response = new FormResponseSimple(n, null);
            return;
        }
        this.response = new FormResponseSimple(n, this.buttons.get(n));
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

