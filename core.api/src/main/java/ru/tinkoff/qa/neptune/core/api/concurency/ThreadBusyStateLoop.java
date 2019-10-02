package ru.tinkoff.qa.neptune.core.api.concurency;

/**
 * This thread checks a container (is it busy or not).
 * When another thread that takes an {@link ObjectContainer} stops then
 * this thread marks {@link ObjectContainer} free by invocation of
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
        do {
            try {
                sleep(1);
            } catch (InterruptedException ignored) {
            }
        } while (threadToListenTo.isAlive());
        container.setFree();
    }
}
