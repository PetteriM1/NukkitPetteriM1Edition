/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

public class EventException
extends RuntimeException {
    private final Throwable a;

    public EventException(Throwable throwable) {
        this.a = throwable;
    }

    public EventException() {
        this.a = null;
    }

    public EventException(Throwable throwable, String string) {
        super(string);
        this.a = throwable;
    }

    public EventException(String string) {
        super(string);
        this.a = null;
    }

    @Override
    public Throwable getCause() {
        return this.a;
    }
}

