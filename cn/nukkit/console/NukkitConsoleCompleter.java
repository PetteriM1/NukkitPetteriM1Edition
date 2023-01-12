/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.console;

import cn.nukkit.Server;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Consumer;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

public class NukkitConsoleCompleter
implements Completer {
    @Override
    public void complete(LineReader lineReader, ParsedLine parsedLine, List<Candidate> list) {
        block4: {
            block3: {
                if (parsedLine.wordIndex() != 0) break block3;
                if (parsedLine.word().isEmpty()) {
                    NukkitConsoleCompleter.a(string -> list.add(new Candidate((String)string)));
                    return;
                }
                TreeSet treeSet = new TreeSet();
                NukkitConsoleCompleter.a(treeSet::add);
                for (String string2 : treeSet) {
                    if (!string2.toLowerCase().startsWith(parsedLine.word())) continue;
                    list.add(new Candidate(string2));
                }
                break block4;
            }
            if (parsedLine.wordIndex() <= 0 || parsedLine.word().isEmpty()) break block4;
            String string3 = parsedLine.word();
            TreeSet treeSet = new TreeSet();
            Server.getInstance().getOnlinePlayers().values().forEach(player -> treeSet.add(player.getName()));
            for (String string4 : treeSet) {
                if (!string4.toLowerCase().startsWith(string3.toLowerCase())) continue;
                list.add(new Candidate(string4));
            }
        }
    }

    private static void a(Consumer<String> consumer) {
        for (String string : Server.getInstance().getCommandMap().getCommands().keySet()) {
            if (string.contains(":")) continue;
            consumer.accept(string);
        }
    }
}

