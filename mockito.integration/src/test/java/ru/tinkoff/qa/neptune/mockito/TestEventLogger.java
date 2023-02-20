package ru.tinkoff.qa.neptune.mockito;

import ru.tinkoff.qa.neptune.core.api.event.firing.EventLogger;

import java.util.LinkedList;
import java.util.Map;

import static java.lang.ThreadLocal.withInitial;

public class TestEventLogger implements EventLogger {

    static ThreadLocal<LinkedList<String>> stepNames = withInitial(LinkedList::new);
    static ThreadLocal<Throwable> thrown = new ThreadLocal<>();
    static ThreadLocal<Boolean> isFinished = new ThreadLocal<>();


    @Override
    public void fireTheEventStarting(String message, Map<String, String> params) {
        stepNames.get().addLast(message);
    }

    @Override
    public void fireThrownException(Throwable throwable) {
        thrown.set(throwable);
    }

    @Override
    public void fireReturnedValue(String resultDescription, Object returned) {
    }

    @Override
    public void fireEventFinishing() {
        isFinished.set(true);
    }

    @Override
    public void addParameters(Map<String, String> parameters) {
    }
}
