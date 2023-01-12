/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.simple;

import cn.nukkit.command.simple.Parameters;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.METHOD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface CommandParameters {
    public Parameters[] parameters() default {};
}

