/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.SOURCE)
@Target(value={ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
public @interface API {
    public Usage usage();

    public Definition definition();

    public static enum Definition {
        INTERNAL,
        PLATFORM_NATIVE,
        UNIVERSAL;

    }

    public static enum Usage {
        DEPRECATED,
        INCUBATING,
        BLEEDING,
        EXPERIMENTAL,
        MAINTAINED,
        STABLE;

    }
}

