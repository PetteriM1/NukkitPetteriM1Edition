package cn.nukkit.form.element;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class ElementInput extends Element {

    @SuppressWarnings("unused")
    private final String type = "input";
    private String text = "";
    private String placeholder = "";
    @SerializedName("default")
    private String defaultText = "";
    private String tooltip = null;

    public ElementInput(String text) {
        this(text, "");
    }

    public ElementInput(String text, String placeholder) {
        this(text, placeholder, "");
    }

    public ElementInput(String text, String placeholder, String defaultText) {
        this.text = text;
        this.placeholder = placeholder;
        this.defaultText = defaultText;
    }

    public String getDefaultText() {
        return defaultText;
    }

    public String getPlaceHolder() {
        return placeholder;
    }

    public String getText() {
        return text;
    }

    @Nullable
    public String getTooltip() {
        return tooltip;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public void setPlaceHolder(String placeholder) {
        this.placeholder = placeholder;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }
}
