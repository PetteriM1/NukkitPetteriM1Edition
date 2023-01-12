/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.form.window;

import cn.nukkit.form.element.Element;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.ElementSlider;
import cn.nukkit.form.element.ElementStepSlider;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseData;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.a;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FormWindowCustom
extends FormWindow {
    private final String type;
    private String title = "";
    private ElementButtonImageData icon;
    private List<Element> content;
    private FormResponseCustom response;

    public FormWindowCustom(String string) {
        this(string, new ArrayList<Element>());
    }

    public FormWindowCustom(String string, List<Element> list) {
        this(string, list, (ElementButtonImageData)null);
    }

    public FormWindowCustom(String string, List<Element> list, String string2) {
        this(string, list, string2.isEmpty() ? null : new ElementButtonImageData("url", string2));
    }

    public FormWindowCustom(String string, List<Element> list, ElementButtonImageData elementButtonImageData) {
        this.type = "custom_form";
        this.title = string;
        this.content = list;
        this.icon = elementButtonImageData;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String string) {
        this.title = string;
    }

    public List<Element> getElements() {
        return this.content;
    }

    public void addElement(Element element) {
        this.content.add(element);
    }

    public ElementButtonImageData getIcon() {
        return this.icon;
    }

    public void setIcon(String string) {
        if (!string.isEmpty()) {
            this.icon = new ElementButtonImageData("url", string);
        }
    }

    public void setIcon(ElementButtonImageData elementButtonImageData) {
        this.icon = elementButtonImageData;
    }

    @Override
    public FormResponseCustom getResponse() {
        return this.response;
    }

    @Override
    public void setResponse(String string) {
        if (string.equals("null")) {
            this.closed = true;
            return;
        }
        List list = (List)GSON.fromJson(string, new ListTypeToken(null).getType());
        int n = 0;
        HashMap<Integer, FormResponseData> hashMap = new HashMap<Integer, FormResponseData>();
        HashMap<Integer, String> hashMap2 = new HashMap<Integer, String>();
        HashMap<Integer, Float> hashMap3 = new HashMap<Integer, Float>();
        HashMap<Integer, FormResponseData> hashMap4 = new HashMap<Integer, FormResponseData>();
        HashMap<Integer, Boolean> hashMap5 = new HashMap<Integer, Boolean>();
        HashMap<Integer, Object> hashMap6 = new HashMap<Integer, Object>();
        HashMap<Integer, String> hashMap7 = new HashMap<Integer, String>();
        for (String string2 : list) {
            Object object;
            Element element;
            if (n >= this.content.size() || (element = this.content.get(n)) == null) break;
            if (element instanceof ElementLabel) {
                hashMap7.put(n, ((ElementLabel)element).getText());
                hashMap6.put(n, ((ElementLabel)element).getText());
            } else if (element instanceof ElementDropdown) {
                object = ((ElementDropdown)element).getOptions().get(Integer.parseInt(string2));
                hashMap.put(n, new FormResponseData(Integer.parseInt(string2), (String)object));
                hashMap6.put(n, object);
            } else if (element instanceof ElementInput) {
                hashMap2.put(n, string2);
                hashMap6.put(n, string2);
            } else if (element instanceof ElementSlider) {
                object = Float.valueOf(Float.parseFloat(string2));
                hashMap3.put(n, (Float)object);
                hashMap6.put(n, object);
            } else if (element instanceof ElementStepSlider) {
                object = ((ElementStepSlider)element).getSteps().get(Integer.parseInt(string2));
                hashMap4.put(n, new FormResponseData(Integer.parseInt(string2), (String)object));
                hashMap6.put(n, object);
            } else if (element instanceof ElementToggle) {
                object = Boolean.parseBoolean(string2);
                hashMap5.put(n, (Boolean)object);
                hashMap6.put(n, object);
            }
            ++n;
        }
        this.response = new FormResponseCustom(hashMap6, hashMap, hashMap2, hashMap3, hashMap4, hashMap5, hashMap7);
    }

    public void setElementsFromResponse() {
        if (this.response != null) {
            this.response.getResponses().forEach((n, object) -> {
                Element element = this.content.get((int)n);
                if (element != null) {
                    if (element instanceof ElementDropdown) {
                        ((ElementDropdown)element).setDefaultOptionIndex(((ElementDropdown)element).getOptions().indexOf(object));
                    } else if (element instanceof ElementInput) {
                        ((ElementInput)element).setDefaultText((String)object);
                    } else if (element instanceof ElementSlider) {
                        ((ElementSlider)element).setDefaultValue(((Float)object).floatValue());
                    } else if (element instanceof ElementStepSlider) {
                        ((ElementStepSlider)element).setDefaultOptionIndex(((ElementStepSlider)element).getSteps().indexOf(object));
                    } else if (element instanceof ElementToggle) {
                        ((ElementToggle)element).setDefaultValue((Boolean)object);
                    }
                }
            });
        }
    }

    private static class ListTypeToken
    extends TypeToken<List<String>> {
        private ListTypeToken() {
        }

        /* synthetic */ ListTypeToken(a a2) {
            this();
        }
    }
}

