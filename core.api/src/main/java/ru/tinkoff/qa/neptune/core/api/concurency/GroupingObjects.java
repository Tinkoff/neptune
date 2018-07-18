package ru.tinkoff.qa.neptune.core.api.concurency;

public final class GroupingObjects {

    private static final ThreadLocal<Object> GROUPING_OBJECTS = new ThreadLocal<>();

    private GroupingObjects() {
        super();
    }

    public static void addGroupingObjectForCurrentThread(Object groupBy) {
        GROUPING_OBJECTS.set(groupBy);
    }

    public static Object getGroupingObject() {
        return GROUPING_OBJECTS.get();
    }
}
