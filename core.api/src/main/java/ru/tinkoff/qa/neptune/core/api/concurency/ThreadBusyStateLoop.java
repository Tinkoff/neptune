package ru.tinkoff.qa.neptune.core.api.concurency;

import java.util.Arrays;
import java.util.Objects;

import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.State.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.resorces.FreeResourcesOnInactivity.TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.general.resorces.FreeResourcesOnInactivityAfter.FREE_RESOURCES_ON_INACTIVITY_AFTER;

/**
 * This thread checks a container (is it busy or not).
 * When another thread that takes an {@link ObjectContainer} is stopped or waits for termination
 * then current {@link ThreadBusyStateLoop} marks {@link ObjectContainer} free by invocation of
 * {@link ObjectContainer#setFree(long)}
 */
class ThreadBusyStateLoop extends Thread {

    private final Thread threadToListenTo;
    private final ObjectContainer<?> container;

    ThreadBusyStateLoop(Thread threadToListenTo, ObjectContainer<?> container) {
        this.threadToListenTo = threadToListenTo;
        this.container = container;
    }

    @Override
    public void run() {
        do {
            try {
                sleep(1);
                if (!threadToListenTo.isAlive()) {
                    break;
                }

                var state = threadToListenTo.getState();

                if (Objects.equals(state, TERMINATED)) {
                    break;
                }

                //test thread may hang up
                //or may be finished and wait the finishing of for other tests
                if (Objects.equals(state, WAITING)
                        && TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY.get()) {
                    var stack = threadToListenTo.getStackTrace();
                    var startTime = currentTimeMillis();
                    var toWait = FREE_RESOURCES_ON_INACTIVITY_AFTER.get().toMillis();
                    boolean isSomethingChanged = false;

                    while (!isSomethingChanged && currentTimeMillis() <= startTime + toWait) {
                        isSomethingChanged = !Arrays.equals(stack, threadToListenTo.getStackTrace());
                    }

                    if (!isSomethingChanged) {
                        container.setFree(0);
                        break;
                    }
                }
            } catch (InterruptedException ignored) {
            }
        } while (true);
        container.setFree(FREE_RESOURCES_ON_INACTIVITY_AFTER.get().toMillis());
    }
}
