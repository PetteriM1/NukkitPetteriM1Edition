/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event;

import cn.nukkit.event.EventPriority;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.METHOD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface EventHandler {
    public EventPriority priority() default EventPriority.NORMAL;

    public boolean ignoreCancelled() default false;
}

