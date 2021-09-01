package cn.nukkit.utils;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureCombiner {

    private List<CompletableFuture<?>> futures = Collections.synchronizedList(new ObjectArrayList<>());
    private CompletableFuture<Void> promise;

    private void checkFutures() {
        try {
            for (CompletableFuture<?> future : this.futures) {
                if (future.isCompletedExceptionally()) {
                    future.join(); // throw an exception
                } else if (!future.isDone()) {
                    return;
                }
            }
            this.complete();
        } catch (Throwable throwable) {
            this.completeExceptionally(throwable.getCause());
        }
    }

    public void addFuture(CompletableFuture<?> future) {
        if (this.promise != null && this.promise.isDone()) {
            throw new IllegalStateException("Promise is already completed");
        }

        future.whenComplete((result, error) -> {
            if (error == null) {
                this.checkFutures();
            } else {
                this.completeExceptionally(error);
            }
        });

        this.futures.add(future);
    }

    public void setPromise(CompletableFuture<Void> promise) {
        if (promise.isDone()) {
            throw new IllegalStateException("Promise is already completed");
        }
        this.promise = promise;
        this.checkFutures();
    }

    public void completeExceptionally(Throwable throwable) {
        if (this.promise != null && !this.promise.isDone()) {
            this.promise.completeExceptionally(throwable);
        }
    }

    public void complete() {
        if (this.promise != null && !this.promise.isDone()) {
            this.promise.complete(null);
        }
    }
}
