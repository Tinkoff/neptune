package ru.tinkoff.qa.neptune.core.api.concurency;

import java.util.Objects;

import static java.lang.Thread.State.*;

/**
 * This thread checks a container (is it busy or not).
 * When another thread that takes an {@link ObjectContainer} is stopped or waits for termination
 * then current {@link ThreadBusyStateLoop} marks {@link ObjectContainer} free by invocation of
 * {@link ObjectContainer#setFree()}
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
        boolean isThreadActive = true;
        do {
            try {
                sleep(1);
                var state = threadToListenTo.getState();
                isThreadActive = threadToListenTo.isAlive() &&
                        (!Objects.equals(state, WAITING) || !Objects.equals(state, TERMINATED));

            } catch (InterruptedException ignored) {
            }
        } while (isThreadActive);
        container.setFree();
    }
}
