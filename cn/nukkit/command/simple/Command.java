/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.simple;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.METHOD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Command {
    public String name();

    public String description() default "";

    public String usageMessage() default "";

    public String[] aliases() default {};
}

