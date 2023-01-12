/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.lang;

import cn.nukkit.Server;

public class TextContainer
implements Cloneable {
    protected String text;

    public TextContainer(String string) {
        this.text = string;
    }

    public void setText(String string) {
        this.text = string;
    }

    public String getText() {
        return this.text;
    }

    public String toString() {
        return this.text;
    }

    public TextContainer clone() {
        try {
            return (TextContainer)super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            Server.getInstance().getLogger().logException(cloneNotSupportedException);
            return null;
        }
    }
}

