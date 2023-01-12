/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.lang;

import cn.nukkit.lang.TextContainer;

public class TranslationContainer
extends TextContainer
implements Cloneable {
    protected String[] params;

    public TranslationContainer(String string) {
        this(string, new String[0]);
    }

    public TranslationContainer(String string, String string2) {
        super(string);
        this.setParameters(new String[]{string2});
    }

    public TranslationContainer(String string, String ... stringArray) {
        super(string);
        this.setParameters(stringArray);
    }

    public String[] getParameters() {
        return this.params;
    }

    public void setParameters(String[] stringArray) {
        this.params = stringArray;
    }

    public String getParameter(int n) {
        return n >= 0 && n < this.params.length ? this.params[n] : null;
    }

    public void setParameter(int n, String string) {
        if (n >= 0 && n < this.params.length) {
            this.params[n] = string;
        }
    }

    @Override
    public TranslationContainer clone() {
        return new TranslationContainer(this.text, (String[])this.params.clone());
    }
}

