/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level;

import cn.nukkit.level.GameRules;
import cn.nukkit.utils.BinaryStream;

final class i
extends GameRules.Type {
    @Override
    void a(BinaryStream binaryStream, GameRules.Value value) {
        binaryStream.putLFloat(GameRules.Value.access$300(value));
    }
}

