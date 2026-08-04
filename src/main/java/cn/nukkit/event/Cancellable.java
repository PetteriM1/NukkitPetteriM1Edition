package cn.nukkit.event;

/**
 * Created by Nukkit Team.
 */
public interface Cancellable {

    void setCancelled(boolean forceCancel);

    boolean isCancelled();

    void setCancelled();
}
