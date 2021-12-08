package ru.tinkoff.qa.neptune.core.api.concurrency;

import java.util.HashSet;
import java.util.Set;

public final class BusyThreads {

    private static final Set<Thread> threadSet = new HashSet<>();

    private BusyThreads() {
        super();
    }

    public static synchronized void setBusy(Thread thread) {
        threadSet.add(thread);
    }

    public static synchronized boolean isBusy(Thread thread) {
        return threadSet.contains(thread);
    }

    public static synchronized void setFree(Thread thread) {
        threadSet.remove(thread);
    }
}
