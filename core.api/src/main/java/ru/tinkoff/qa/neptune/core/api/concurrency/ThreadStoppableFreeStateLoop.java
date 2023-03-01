package ru.tinkoff.qa.neptune.core.api.concurrency;

import ru.tinkoff.qa.neptune.core.api.cleaning.Stoppable;

import static java.lang.System.currentTimeMillis;

/**
 * This thread checks an instance of {@link ObjectContainer} that is marked free by
 * invocation of {@link ObjectContainer#setFree(long)} and those class implements {@link Stoppable}.
 * It checks whether the container active or not. When the container is still inactive after some time
 * this thread invokes {@link Stoppable#stop()}
 */
class ThreadStoppableFreeStateLoop extends Thread {

    private final ObjectContainer<? extends Stoppable> container;
    private final long millisToStopAfter;

    ThreadStoppableFreeStateLoop(ObjectContainer<? extends Stoppable> container, long millisToStopAfter) {
        this.container = container;
        this.millisToStopAfter = millisToStopAfter;
    }

    @Override
    public void run() {
        var startTime = currentTimeMillis();
        while (currentTimeMillis() <= startTime + millisToStopAfter) {
            if (container.isBusy()) {
                return;
            }
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                currentThread().interrupt();
            }
        }

        if (!container.isBusy()) {
            synchronized (container) {
                container.getWrappedObject().stop();
            }
        }
    }
}
