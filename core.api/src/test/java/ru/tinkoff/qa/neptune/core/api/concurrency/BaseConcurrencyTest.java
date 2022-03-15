package ru.tinkoff.qa.neptune.core.api.concurrency;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import static java.lang.Thread.sleep;
import static ru.tinkoff.qa.neptune.core.api.concurrency.ObjectContainer.containers;
import static ru.tinkoff.qa.neptune.core.api.properties.general.resorces.FreeResourcesOnInactivity.TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.general.resorces.FreeResourcesOnInactivityAfter.FreeResourcesOnInactivityAfterTimeValue.FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE;

public abstract class BaseConcurrencyTest {

    Thread thread1;
    Thread thread2;

    abstract Thread createThread1();

    abstract Thread createThread2();

    @BeforeMethod
    public void runThreads() throws Exception {
        containers.clear();

        thread1 = createThread1();
        thread1.start();
        sleep(500);

        thread2 = createThread2();
        thread2.start();
        sleep(1000);
    }

    @AfterMethod(alwaysRun = true)
    public void clearAfter() {
        thread1.stop();
        thread2.stop();

        TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY.accept(null);
        TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY.accept(null);
        FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE.accept(null);
    }
}
