/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.NOBF;
import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class SimpleConfig {
    @NOBF
    private final File configFile;

    public SimpleConfig(Plugin plugin) {
        this(plugin, "config.yml");
    }

    public SimpleConfig(Plugin plugin, String string) {
        this(new File(plugin.getDataFolder() + File.separator + string));
    }

    public SimpleConfig(File file) {
        this.configFile = file;
        this.configFile.getParentFile().mkdirs();
    }

    public boolean save() {
        return this.save(false);
    }

    public boolean save(boolean bl) {
        if (this.configFile.exists()) {
            try {
                this.configFile.createNewFile();
            }
            catch (Exception exception) {
                return false;
            }
        }
        Config config = new Config(this.configFile, 2);
        for (Field field : this.getClass().getDeclaredFields()) {
            if (SimpleConfig.c(field)) continue;
            String string = SimpleConfig.a(field);
            try {
                if (string == null) continue;
                config.set(string, field.get(this));
            }
            catch (Exception exception) {
                return false;
            }
        }
        config.save(bl);
        return true;
    }

    public boolean load() {
        if (!this.configFile.exists()) {
            return false;
        }
        Config config = new Config(this.configFile, 2);
        for (Field field : this.getClass().getDeclaredFields()) {
            String string;
            if (field.getName().equals("configFile") || this.b(field) || (string = SimpleConfig.a(field)) == null || string.isEmpty()) continue;
            field.setAccessible(true);
            try {
                if (field.getType() == Integer.TYPE || field.getType() == Integer.class) {
                    field.set(this, config.getInt(string, field.getInt(this)));
                    continue;
                }
                if (field.getType() == Boolean.TYPE || field.getType() == Boolean.class) {
                    field.set(this, config.getBoolean(string, field.getBoolean(this)));
                    continue;
                }
                if (field.getType() == Long.TYPE || field.getType() == Long.class) {
                    field.set(this, config.getLong(string, field.getLong(this)));
                    continue;
                }
                if (field.getType() == Double.TYPE || field.getType() == Double.class) {
                    field.set(this, config.getDouble(string, field.getDouble(this)));
                    continue;
                }
                if (field.getType() == String.class) {
                    field.set(this, config.getString(string, (String)field.get(this)));
                    continue;
                }
                if (field.getType() == ConfigSection.class) {
                    field.set(this, config.getSection(string));
                    continue;
                }
                if (field.getType() == List.class) {
                    Type type = field.getGenericType();
                    if (type instanceof ParameterizedType) {
                        ParameterizedType parameterizedType = (ParameterizedType)type;
                        Class clazz = (Class)parameterizedType.getActualTypeArguments()[0];
                        if (clazz == Integer.class) {
                            field.set(this, config.getIntegerList(string));
                            continue;
                        }
                        if (clazz == Boolean.class) {
                            field.set(this, config.getBooleanList(string));
                            continue;
                        }
                        if (clazz == Double.class) {
                            field.set(this, config.getDoubleList(string));
                            continue;
                        }
                        if (clazz == Character.class) {
                            field.set(this, config.getCharacterList(string));
                            continue;
                        }
                        if (clazz == Byte.class) {
                            field.set(this, config.getByteList(string));
                            continue;
                        }
                        if (clazz == Float.class) {
                            field.set(this, config.getFloatList(string));
                            continue;
                        }
                        if (clazz == Short.class) {
                            field.set(this, config.getFloatList(string));
                            continue;
                        }
                        if (clazz != String.class) continue;
                        field.set(this, config.getStringList(string));
                        continue;
                    }
                    field.set(this, config.getList(string));
                    continue;
                }
                throw new IllegalStateException("SimpleConfig did not supports class: " + field.getType().getName() + " for config field " + this.configFile.getName());
            }
            catch (Exception exception) {
                Server.getInstance().getLogger().logException(exception);
                return false;
            }
        }
        return true;
    }

    private static String a(Field field) {
        String string;
        block3: {
            string = null;
            if (field.isAnnotationPresent(Path.class)) {
                Path path = field.getAnnotation(Path.class);
                string = path.value();
            }
            if (string == null || string.isEmpty()) {
                string = field.getName().replaceAll("_", ".");
            }
            if (Modifier.isFinal(field.getModifiers())) {
                return null;
            }
            if (!Modifier.isPrivate(field.getModifiers())) break block3;
            field.setAccessible(true);
        }
        return string;
    }

    private static boolean c(Field field) {
        if (!field.isAnnotationPresent(Skip.class)) {
            return false;
        }
        return field.getAnnotation(Skip.class).skipSave();
    }

    private boolean b(Field field) {
        if (!field.isAnnotationPresent(Skip.class)) {
            return false;
        }
        return field.getAnnotation(Skip.class).skipLoad();
    }

    private static Exception a(Exception exception) {
        return exception;
    }

    @Retention(value=RetentionPolicy.RUNTIME)
    @Target(value={ElementType.FIELD})
    public static @interface Skip {
        public boolean skipSave() default true;

        public boolean skipLoad() default true;
    }

    @Retention(value=RetentionPolicy.RUNTIME)
    @Target(value={ElementType.FIELD})
    public static @interface Path {
        public String value() default "";
    }
}

