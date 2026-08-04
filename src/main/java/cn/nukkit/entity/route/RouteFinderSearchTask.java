package cn.nukkit.entity.route;

/**
 * @author zzz1999 @ MobPlugin
 */
public class RouteFinderSearchTask implements Runnable {

    private final RouteFinder route;
    private int retryTime;

    public RouteFinderSearchTask(RouteFinder route) {
        this.route = route;
    }

    @Override
    public void run() {
        if (this.route == null) {
            return;
        }

        while (this.retryTime < 5) {
            if (!this.route.isSearching()) {
                this.route.research();
                return;
            } else {
                this.retryTime++;

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignore) {
                }
            }
        }

        this.route.interrupt();
    }
}
