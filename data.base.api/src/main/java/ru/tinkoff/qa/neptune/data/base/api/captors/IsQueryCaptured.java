package ru.tinkoff.qa.neptune.data.base.api.captors;

/**
 * This is the service interface designed to mark objects, captured by {@link QueryCaptor},
 * {@link TypedQueryCaptor} or {@link StringQueryCaptor}
 */
public interface IsQueryCaptured {
    boolean isCaptured();

    void setCaptured();
}
