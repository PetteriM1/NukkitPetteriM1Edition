/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.data;

import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandOverload;
import java.util.HashMap;
import java.util.Map;

public class CommandData
implements Cloneable {
    public CommandEnum aliases = null;
    public String description = "description";
    public Map<String, CommandOverload> overloads = new HashMap<String, CommandOverload>();
    public int flags;
    public int permission;

    public CommandData clone() {
        try {
            return (CommandData)super.clone();
        }
        catch (Exception exception) {
            return new CommandData();
        }
    }
}

