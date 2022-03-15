package ru.tinkoff.qa.neptune.core.api.concurrency;

public class EndlessLoopObject {

    public synchronized void startEndlessWaitingLoop() {
        while (true) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
