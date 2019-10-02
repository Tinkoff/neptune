package ru.tinkoff.qa.neptune.core.api.cleaning;

/**
 * This interface is designed to declare and implement actions that should be done on the stopping.
 * There are such actions as the stopping/finishing of jvm, cleaning of unused resources etc
 */
public interface Stoppable {
    /**
     * An action to be performed when stopped
     */
    void stop();
}
