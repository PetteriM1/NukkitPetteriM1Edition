/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level;

import cn.nukkit.level.GameRules;
import cn.nukkit.utils.BinaryStream;

final class g
extends GameRules.Type {
    @Override
    void a(BinaryStream binaryStream, GameRules.Value value) {
        binaryStream.putBoolean(GameRules.Value.access$100(value));
    }
}

