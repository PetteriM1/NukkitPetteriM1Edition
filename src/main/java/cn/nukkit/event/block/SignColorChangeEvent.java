package cn.nukkit.event.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.utils.BlockColor;

public class SignColorChangeEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final BlockColor color;
    public SignColorChangeEvent(Block block, Player player, BlockColor color) {
        super(block);
        this.player = player;
        this.color = color;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public BlockColor getColor() {
        return this.color;
    }
}
