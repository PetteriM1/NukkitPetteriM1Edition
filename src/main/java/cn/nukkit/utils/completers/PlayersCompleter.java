package cn.nukkit.utils.completers;

import cn.nukkit.Server;
import jline.console.completer.Completer;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static jline.internal.Preconditions.checkNotNull;

public class PlayersCompleter implements Completer {

    @Override
    public int complete(String buffer, int cursor, List<CharSequence> candidates) {
        checkNotNull(candidates);

        if (buffer == null) {
            Server.getInstance().getOnlinePlayers().values().forEach((p) -> candidates.add(p.getName()));
        } else {
            String[] split = buffer.split(" ");
            buffer = split[split.length - 1];
            split[split.length - 1] = "";
            String cmd = String.join(" ", split);
            SortedSet<String> names = new TreeSet<String>();
            Server.getInstance().getOnlinePlayers().values().forEach((p) -> names.add(p.getName()));
            for (String match : names) {
                if (!match.toLowerCase().startsWith(buffer.toLowerCase())) {
                    continue;
                }

                candidates.add(cmd + match);
            }
        }

        return candidates.isEmpty() ? -1 : 0;
    }
}
