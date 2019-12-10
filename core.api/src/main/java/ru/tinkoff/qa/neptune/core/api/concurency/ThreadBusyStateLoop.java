package ru.tinkoff.qa.neptune.core.api.concurency;

import com.google.common.annotations.Beta;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.concurrent.locks.AbstractOwnableSynchronizer;

import static java.lang.Thread.State.TERMINATED;
import static java.lang.Thread.State.WAITING;
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
    private final Field targetField;
    private final Field exclusiveOwnerThreadField;

    ThreadBusyStateLoop(Thread threadToListenTo, ObjectContainer<?> container) {
        this.threadToListenTo = threadToListenTo;
        this.container = container;

        try {
            targetField = Thread.class.getDeclaredField("target");
            targetField.setAccessible(true);
            exclusiveOwnerThreadField = AbstractOwnableSynchronizer.class.getDeclaredField("exclusiveOwnerThread");
            exclusiveOwnerThreadField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Beta
    private boolean isFinishedAndWaitingForTermination() {
        try {
            var val = targetField.get(threadToListenTo);
            if (val == null) {
                return false;
            }

            if (!AbstractOwnableSynchronizer.class.isAssignableFrom(val.getClass())) {
                return false;
            }

            return exclusiveOwnerThreadField.get(val) == null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
                        && TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY.get()
                        && isFinishedAndWaitingForTermination()) {
                    break;
                }
            } catch (InterruptedException ignored) {
            }
        } while (true);
        container.setFree(FREE_RESOURCES_ON_INACTIVITY_AFTER.get().toMillis());
    }
}
