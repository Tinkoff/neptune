package ru.tinkoff.qa.neptune.core.api.concurency;

class ThreadStateLoop extends Thread {

    private final Thread threadToListenTo;
    private final ObjectContainer<?> container;

    ThreadStateLoop(Thread threadToListenTo, ObjectContainer<?> container) {
        this.threadToListenTo = threadToListenTo;
        this.container = container;
    }

    @Override
    public void run() {
        boolean isAlive = true;
         do {
            isAlive = threadToListenTo.isAlive();
            if (isAlive) {
                try {
                    sleep(1);
                } catch (InterruptedException ignored) {
                }
            }
         } while (isAlive);
        container.setFree();
    }
}
