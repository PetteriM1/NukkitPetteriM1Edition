/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.form.handler;

import cn.nukkit.NOBF;
import cn.nukkit.Player;
import java.util.function.IntConsumer;

public interface FormResponseHandler {
    @NOBF
    public static FormResponseHandler withoutPlayer(IntConsumer intConsumer) {
        return (player, n) -> intConsumer.accept(n);
    }

    @NOBF
    public void handle(Player var1, int var2);
}

