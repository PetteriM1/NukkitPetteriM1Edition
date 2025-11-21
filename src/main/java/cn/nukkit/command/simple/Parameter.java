package cn.nukkit.command.simple;

import cn.nukkit.command.data.CommandParamType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author nilsbrychzy
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {
    String name();

    boolean optional() default false;

    CommandParamType type() default CommandParamType.RAWTEXT;
}
