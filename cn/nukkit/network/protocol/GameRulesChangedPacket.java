/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.level.GameRule;
import cn.nukkit.level.GameRules;
import cn.nukkit.network.protocol.DataPacket;
import java.util.Map;

public class GameRulesChangedPacket
extends DataPacket {
    public static final byte NETWORK_ID = 72;
    public GameRules gameRules;
    public Map<GameRule, GameRules.Value> gameRulesMap;

    @Override
    public byte pid() {
        return 72;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        if (this.gameRulesMap == null) {
            this.putGameRules(this.protocol, this.gameRules);
        } else {
            this.putGameRulesMap(this.protocol, this.gameRulesMap);
        }
    }

    public String toString() {
        return "GameRulesChangedPacket(gameRules=" + this.gameRules + ", gameRulesMap=" + this.gameRulesMap + ")";
    }
}

