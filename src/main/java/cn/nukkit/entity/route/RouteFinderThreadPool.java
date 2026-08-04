package cn.nukkit.entity.route;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zzz1999 @ MobPlugin
 */
public class RouteFinderThreadPool {

    private static volatile boolean running = true;

    private static final ThreadPoolExecutor executor =
            new ThreadPoolExecutor(
                    1,
                    Runtime.getRuntime().availableProcessors(),
                    1, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(),
                    new ThreadPoolExecutor.AbortPolicy()
            );

    public static void executeRouteFinderThread(RouteFinderSearchTask t) {
        if (running) {
            executor.execute(t);
        }
    }

    public static void shutdown() {
        running = false;
        executor.shutdownNow();
    }
}
