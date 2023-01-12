/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.lang;

import cn.nukkit.Server;
import cn.nukkit.lang.TextContainer;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BaseLang {
    public static final String FALLBACK_LANGUAGE = "eng";
    protected final String langName;
    protected Map<String, String> lang;
    protected Map<String, String> fallbackLang = new HashMap<String, String>();

    public BaseLang(String string) {
        this(string, null);
    }

    public BaseLang(String string, String string2) {
        this(string, string2, "eng");
    }

    public BaseLang(String string, String string2, String string3) {
        boolean bl;
        this.langName = string.toLowerCase();
        boolean bl2 = bl = !string.equals(string3);
        if (string2 == null) {
            string2 = "lang/";
            this.lang = BaseLang.loadLang(this.getClass().getClassLoader().getResourceAsStream(string2 + this.langName + "/lang.ini"));
            if (bl) {
                this.fallbackLang = BaseLang.loadLang(this.getClass().getClassLoader().getResourceAsStream(string2 + string3 + "/lang.ini"));
            }
        } else {
            this.lang = BaseLang.loadLang(string2 + this.langName + "/lang.ini");
            if (bl) {
                this.fallbackLang = BaseLang.loadLang(string2 + string3 + "/lang.ini");
            }
        }
        if (this.fallbackLang == null) {
            this.fallbackLang = this.lang;
        }
    }

    public Map<String, String> getLangMap() {
        return this.lang;
    }

    public Map<String, String> getFallbackLangMap() {
        return this.fallbackLang;
    }

    public String getName() {
        return this.get("language.name");
    }

    public String getLang() {
        return this.langName;
    }

    protected static Map<String, String> loadLang(String string) {
        try {
            String string2 = Utils.readFile(string);
            HashMap<String, String> hashMap = new HashMap<String, String>();
            for (String string3 : string2.split("\n")) {
                String[] stringArray;
                if ((string3 = string3.trim()).isEmpty() || string3.charAt(0) == '#' || (stringArray = string3.split("=")).length < 2) continue;
                String string4 = stringArray[0];
                StringBuilder stringBuilder = new StringBuilder();
                for (int k = 1; k < stringArray.length - 1; ++k) {
                    stringBuilder.append(stringArray[k]).append('=');
                }
                stringBuilder.append(stringArray[stringArray.length - 1]);
                if (stringBuilder.length() == 0) continue;
                hashMap.put(string4, stringBuilder.toString());
            }
            return hashMap;
        }
        catch (IOException iOException) {
            Server.getInstance().getLogger().logException(iOException);
            return null;
        }
    }

    protected static Map<String, String> loadLang(InputStream inputStream) {
        try {
            String string = Utils.readFile(inputStream);
            HashMap<String, String> hashMap = new HashMap<String, String>();
            for (String string2 : string.split("\n")) {
                String[] stringArray;
                if ((string2 = string2.trim()).isEmpty() || string2.charAt(0) == '#' || (stringArray = string2.split("=")).length < 2) continue;
                String string3 = stringArray[0];
                StringBuilder stringBuilder = new StringBuilder();
                for (int k = 1; k < stringArray.length - 1; ++k) {
                    stringBuilder.append(stringArray[k]).append('=');
                }
                stringBuilder.append(stringArray[stringArray.length - 1]);
                if (stringBuilder.length() == 0) continue;
                hashMap.put(string3, stringBuilder.toString());
            }
            return hashMap;
        }
        catch (IOException iOException) {
            Server.getInstance().getLogger().logException(iOException);
            return null;
        }
    }

    public String translateString(String string) {
        return this.translateString(string, new String[0], (String)null);
    }

    public String translateString(String string, String ... stringArray) {
        if (stringArray != null) {
            return this.translateString(string, stringArray, (String)null);
        }
        return this.translateString(string, new String[0], (String)null);
    }

    public String translateString(String string, Object ... objectArray) {
        if (objectArray != null) {
            String[] stringArray = new String[objectArray.length];
            for (int k = 0; k < objectArray.length; ++k) {
                stringArray[k] = Objects.toString(objectArray[k]);
            }
            return this.translateString(string, stringArray, (String)null);
        }
        return this.translateString(string, new String[0], (String)null);
    }

    public String translateString(String string, String string2, String string3) {
        return this.translateString(string, new String[]{string2}, string3);
    }

    public String translateString(String string, String[] stringArray, String string2) {
        String string3 = this.get(string);
        string3 = this.parseTranslation(string3 != null && (string2 == null || string.indexOf(string2) == 0) ? string3 : string, string2);
        for (int k = 0; k < stringArray.length; ++k) {
            string3 = string3.replace("{%" + k + '}', this.parseTranslation(((StringBuilder)((Object)stringArray[k])).toString()));
        }
        return string3;
    }

    public String translate(TextContainer textContainer) {
        String string = this.parseTranslation(textContainer.getText());
        if (textContainer instanceof TranslationContainer) {
            string = this.internalGet(textContainer.getText());
            string = this.parseTranslation(string != null ? string : textContainer.getText());
            for (int k = 0; k < ((TranslationContainer)textContainer).getParameters().length; ++k) {
                string = string.replace("{%" + k + '}', this.parseTranslation(((TranslationContainer)textContainer).getParameters()[k]));
            }
        }
        return string;
    }

    public String internalGet(String string) {
        if (this.lang.containsKey(string)) {
            return this.lang.get(string);
        }
        if (this.fallbackLang.containsKey(string)) {
            return this.fallbackLang.get(string);
        }
        return null;
    }

    public String get(String string) {
        if (this.lang.containsKey(string)) {
            return this.lang.get(string);
        }
        if (this.fallbackLang.containsKey(string)) {
            return this.fallbackLang.get(string);
        }
        return string;
    }

    protected String parseTranslation(String string) {
        return this.parseTranslation(string, null);
    }

    protected String parseTranslation(String string, String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        string = ((StringBuilder)((Object)string)).toString();
        StringBuilder stringBuilder2 = null;
        int n = string.length();
        for (int k = 0; k < n; ++k) {
            char c2 = string.charAt(k);
            if (stringBuilder2 != null) {
                if (c2 >= '0' && c2 <= '9' || c2 >= 'A' && c2 <= 'Z' || c2 >= 'a' && c2 <= 'z' || c2 == '.' || c2 == '-') {
                    stringBuilder2.append(c2);
                    continue;
                }
                String string3 = this.internalGet(stringBuilder2.substring(1));
                if (string3 != null && (string2 == null || stringBuilder2.indexOf(string2) == 1)) {
                    stringBuilder.append(string3);
                } else {
                    stringBuilder.append((CharSequence)stringBuilder2);
                }
                stringBuilder2 = null;
                if (c2 == '%') {
                    stringBuilder2 = new StringBuilder(String.valueOf(c2));
                    continue;
                }
                stringBuilder.append(c2);
                continue;
            }
            if (c2 == '%') {
                stringBuilder2 = new StringBuilder(String.valueOf(c2));
                continue;
            }
            stringBuilder.append(c2);
        }
        if (stringBuilder2 != null) {
            String string4 = this.internalGet(stringBuilder2.substring(1));
            if (string4 != null && (string2 == null || stringBuilder2.indexOf(string2) == 1)) {
                stringBuilder.append(string4);
            } else {
                stringBuilder.append((CharSequence)stringBuilder2);
            }
        }
        return stringBuilder.toString();
    }

    private static IOException a(IOException iOException) {
        return iOException;
    }
}

