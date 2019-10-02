package ru.tinkoff.qa.neptune.core.api.concurency;

import ru.tinkoff.qa.neptune.core.api.cleaning.Stoppable;

import static java.lang.System.currentTimeMillis;
import static ru.tinkoff.qa.neptune.core.api.properties.general.resorces.FreeResourcesOnInactivityAfter.FREE_RESOURCES_ON_INACTIVITY_AFTER;

/**
 * This thread checks an instance of {@link ObjectContainer} that is marked free by
 * invocation of {@link ObjectContainer#setFree()} and those class implements {@link Stoppable}.
 * It checks is the container active or not. When the container is still inactive after some time
 * this thread invokes {@link Stoppable#stop()}
 */
class ThreadFreeStateLoop extends Thread {

    private final ObjectContainer<?> container;

    ThreadFreeStateLoop(ObjectContainer<?> container) {
        this.container = container;
    }

    @Override
    public void run() {
        var startTime = currentTimeMillis();
        var timeToWait = FREE_RESOURCES_ON_INACTIVITY_AFTER.get().toMillis();

        while (currentTimeMillis() <= startTime + timeToWait) {
            if (container.isBusy()) {
                return;
            }
            try {
                sleep(1);
            } catch (InterruptedException ignored) {
            }
        }

        if (!container.isBusy()) {
            synchronized (container) {
                var contained = container.getWrappedObject();
                if (Stoppable.class.isAssignableFrom(contained.getClass())) {
                    ((Stoppable) contained).stop();
                }
            }
        }
    }
}
