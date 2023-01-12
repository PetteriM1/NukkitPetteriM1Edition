/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public enum TextFormat {
    BLACK('0', 0),
    DARK_BLUE('1', 1),
    DARK_GREEN('2', 2),
    DARK_AQUA('3', 3),
    DARK_RED('4', 4),
    DARK_PURPLE('5', 5),
    GOLD('6', 6),
    GRAY('7', 7),
    DARK_GRAY('8', 8),
    BLUE('9', 9),
    GREEN('a', 10),
    AQUA('b', 11),
    RED('c', 12),
    LIGHT_PURPLE('d', 13),
    YELLOW('e', 14),
    WHITE('f', 15),
    MINECOIN_GOLD('g', 22),
    OBFUSCATED('k', 16, true),
    BOLD('l', 17, true),
    STRIKETHROUGH('m', 18, true),
    UNDERLINE('n', 19, true),
    ITALIC('o', 20, true),
    RESET('r', 21);

    public static final char ESCAPE = '\u00a7';
    private static final Pattern c;
    private static final Map<Character, TextFormat> b;
    private final char d;
    private final boolean a;
    private final String e;

    private TextFormat(char c2, int n2) {
        this(c2, n2, false);
    }

    private TextFormat(char c2, int n2, boolean bl) {
        this.d = c2;
        this.a = bl;
        this.e = new String(new char[]{'\u00a7', c2});
    }

    public static TextFormat getByChar(char c2) {
        return b.get(Character.valueOf(c2));
    }

    public static TextFormat getByChar(String string) {
        if (string == null || string.length() <= 1) {
            return null;
        }
        return b.get(Character.valueOf(string.charAt(0)));
    }

    public static String clean(String string) {
        return TextFormat.clean(string, false);
    }

    public static String clean(String string, boolean bl) {
        if (string == null) {
            return null;
        }
        String string2 = c.matcher(string).replaceAll("");
        if (bl && c.matcher(string2).find()) {
            return TextFormat.clean(string2, true);
        }
        return string2;
    }

    public static String colorize(char c2, String string) {
        char[] cArray = string.toCharArray();
        for (int k = 0; k < cArray.length - 1; ++k) {
            int n = k + 1;
            if (cArray[k] != c2 || "0123456789AaBbCcDdEeFfGgKkLlMmNnOoRr".indexOf(cArray[n]) <= -1) continue;
            cArray[k] = 167;
            cArray[n] = Character.toLowerCase(cArray[n]);
        }
        return new String(cArray);
    }

    public static String colorize(String string) {
        return TextFormat.colorize('&', string);
    }

    public static String getLastColors(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        int n = string.length();
        for (int k = n - 1; k > -1; --k) {
            TextFormat textFormat;
            if (string.charAt(k) != '\u00a7' || k >= n - 1 || (textFormat = TextFormat.getByChar(string.charAt(k + 1))) == null) continue;
            stringBuilder.insert(0, textFormat.toString());
            if (textFormat.isColor() || textFormat.equals((Object)RESET)) break;
        }
        return stringBuilder.toString();
    }

    public char getChar() {
        return this.d;
    }

    public String toString() {
        return this.e;
    }

    public boolean isFormat() {
        return this.a;
    }

    public boolean isColor() {
        return !this.a && this != RESET;
    }

    static {
        c = Pattern.compile("(?i)\u00a7[0-9A-GK-OR]");
        b = new HashMap<Character, TextFormat>();
        for (TextFormat textFormat : TextFormat.values()) {
            b.put(Character.valueOf(textFormat.d), textFormat);
        }
    }
}

