/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.TextFormat;
import java.util.ArrayList;
import java.util.List;

public class FormattedCommandAlias
extends Command {
    private final String[] i;

    public FormattedCommandAlias(String string, String[] stringArray) {
        super(string);
        this.i = stringArray;
    }

    public FormattedCommandAlias(String string, List<String> list) {
        super(string);
        this.i = list.toArray(new String[0]);
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        boolean bl = false;
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String string2 : this.i) {
            try {
                arrayList.add(this.a(string2, stringArray));
            }
            catch (Exception exception) {
                if (exception instanceof IllegalArgumentException) {
                    commandSender.sendMessage((Object)((Object)TextFormat.RED) + exception.getMessage());
                } else {
                    commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.exception"));
                    MainLogger mainLogger = commandSender.getServer().getLogger();
                    if (mainLogger != null) {
                        mainLogger.logException(exception);
                    }
                }
                return false;
            }
        }
        for (String string3 : arrayList) {
            bl |= Server.getInstance().dispatchCommand(commandSender, string3);
        }
        return bl;
    }

    private String a(String string, String[] stringArray) {
        int n = string.indexOf(36);
        while (n != -1) {
            int n2 = n;
            if (n > 0 && string.charAt(n2 - 1) == '\\') {
                string = string.substring(0, n2 - 1) + string.substring(n2);
                n = string.indexOf(36, n);
                continue;
            }
            boolean bl = false;
            if (string.charAt(n + 1) == '$') {
                bl = true;
                ++n;
            }
            int n3 = ++n;
            while (n < string.length() && FormattedCommandAlias.a(string.charAt(n) - 48, 0, 9)) {
                ++n;
            }
            if (n3 == n) {
                throw new IllegalArgumentException("Invalid replacement token");
            }
            int n4 = Integer.parseInt(string.substring(n3, n));
            if (n4 == 0) {
                throw new IllegalArgumentException("Invalid replacement token");
            }
            --n4;
            boolean bl2 = false;
            if (n < string.length() && string.charAt(n) == '-') {
                bl2 = true;
            }
            int n5 = ++n;
            if (bl && n4 >= stringArray.length) {
                throw new IllegalArgumentException("Missing required argument " + (n4 + 1));
            }
            StringBuilder stringBuilder = new StringBuilder();
            if (bl2 && n4 < stringArray.length) {
                for (int k = n4; k < stringArray.length; ++k) {
                    if (k != n4) {
                        stringBuilder.append(' ');
                    }
                    stringBuilder.append(stringArray[k]);
                }
            } else if (n4 < stringArray.length) {
                stringBuilder.append(stringArray[n4]);
            }
            string = string.substring(0, n2) + stringBuilder + string.substring(n5);
            n = n2 + stringBuilder.length();
            n = string.indexOf(36, n);
        }
        return string;
    }

    private static boolean a(int n, int n2, int n3) {
        return n >= n2 && n <= n3;
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

