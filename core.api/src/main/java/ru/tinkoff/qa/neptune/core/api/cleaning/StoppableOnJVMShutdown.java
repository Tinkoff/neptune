package ru.tinkoff.qa.neptune.core.api.cleaning;

/**
 * This interface is designed to declare and implement actions that should be done on
 * jvm stop.
 */
public interface StoppableOnJVMShutdown {
    /**
     * Method returns a thread that is run on the finishing of jvm work.
     *
     * @return a thread that is run on jvm stop
     */
    Thread getHookOnJvmStop();
}
