package cn.nukkit.form.handler;

import cn.nukkit.Player;

import java.util.function.IntConsumer;

public interface FormResponseHandler {

    void handle(Player player, int formID);

    static FormResponseHandler withoutPlayer(IntConsumer formIDConsumer) {
        return (player, formID) -> formIDConsumer.accept(formID);
    }
}
