package ru.tinkoff.qa.neptune.core.api.concurency;

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
