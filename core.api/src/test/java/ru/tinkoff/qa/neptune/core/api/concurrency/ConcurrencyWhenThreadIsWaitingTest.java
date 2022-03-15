package ru.tinkoff.qa.neptune.core.api.concurrency;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.sleep;
import static java.time.Duration.ofSeconds;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.concurrency.BusyThreads.setBusy;
import static ru.tinkoff.qa.neptune.core.api.concurrency.BusyThreads.setFree;
import static ru.tinkoff.qa.neptune.core.api.concurrency.ObjectContainer.containers;
import static ru.tinkoff.qa.neptune.core.api.concurrency.TestContext.getContext;
import static ru.tinkoff.qa.neptune.core.api.properties.general.resorces.FreeResourcesOnInactivity.TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.general.resorces.FreeResourcesOnInactivityAfter.FreeResourcesOnInactivityAfterTimeUnit.FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT;
import static ru.tinkoff.qa.neptune.core.api.properties.general.resorces.FreeResourcesOnInactivityAfter.FreeResourcesOnInactivityAfterTimeValue.FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE;

public class ConcurrencyWhenThreadIsWaitingTest extends BaseConcurrencyTest {

    private Thread createThread() {
        return new Thread(() -> {
            getContext().doSomething();
            new EndlessLoopObject().startEndlessWaitingLoop();
        });
    }

    @Override
    Thread createThread1() {
        return createThread();
    }

    @Override
    Thread createThread2() {
        return createThread();
    }

    @Override
    @BeforeMethod
    public void runThreads() throws Exception {
        super.runThreads();
        setBusy(thread1);
        setBusy(thread2);
    }

    @Override
    @AfterMethod(alwaysRun = true)
    public void clearAfter() {
        super.clearAfter();
        setFree(thread1);
        setFree(thread2);
    }

    @Test(groups = "basic")
    public void busyTest() {
        var containerList = new ArrayList<>(containers);
        assertThat(containerList, hasSize(2));
        assertThat(containerList.stream()
                .map(ObjectContainer::getWrappedObject)
                .distinct()
                .collect(toList()), hasSize(2));

        assertThat(containerList.stream().map(ObjectContainer::isBusy)
                .collect(toList()), everyItem(is(true)));
    }

    @Test(groups = "basic")
    public void stillNotFreeTest() throws Exception {
        setFree(thread2);
        sleep(1000);

        var containerList = new ArrayList<>(containers);
        assertThat(containerList, hasSize(2));
        assertThat(containerList.stream()
                .map(ObjectContainer::getWrappedObject)
                .distinct()
                .collect(toList()), hasSize(2));

        assertThat(containerList.stream().map(ObjectContainer::isBusy)
                .collect(toList()), not(hasItem(is(false))));
    }

    @Test(groups = "basic")
    public void freeTest() throws Exception {
        TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY.accept(true);
        setFree(thread2);
        sleep(1000);

        var containerList = new ArrayList<>(containers);
        assertThat(containerList, hasSize(2));
        assertThat(containerList.stream()
                .map(ObjectContainer::getWrappedObject)
                .distinct()
                .collect(toList()), hasSize(2));

        assertThat(containerList.stream().map(ObjectContainer::isBusy)
                .collect(toList()), hasItem(is(false)));

        assertThat(((TestContext) containerList.stream().filter(objectContainer -> !objectContainer.isBusy())
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Free context container was not found"))
                        .getWrappedObject()).isActive(),
                is(true));
    }

    @Test(dependsOnGroups = "basic", priority = 1)
    public void resourcesFreeByDefaultTest() throws Exception {
        TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY.accept(true);
        setFree(thread2);
        sleep(1000);

        var containerList = new ArrayList<>(containers);
        var freeObject = ((TestContext) containerList.stream().filter(objectContainer -> !objectContainer.isBusy())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Free context container was not found"))
                .getWrappedObject());

        var start = currentTimeMillis();
        var active = freeObject.isActive();
        while (active && currentTimeMillis() <= start + ofSeconds(30).toMillis()) {
            active = freeObject.isActive();
        }
        var stop = currentTimeMillis();

        assertThat(freeObject.isActive(), is(false));
        //30 seconds is default value. -1 second of the sleeping. see above
        assertThat(new BigDecimal(stop - start),
                closeTo(new BigDecimal(30000), new BigDecimal(1000)));
    }

    @Test(dependsOnGroups = "basic", priority = 1)
    public void resourcesFreeAfterDefinedTimeTest() throws Exception {
        TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY.accept(true);
        FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT.accept(SECONDS);
        FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE.accept(5L);

        setFree(thread2);
        sleep(1000);

        var containerList = new ArrayList<>(containers);
        var freeObject = ((TestContext) containerList.stream().filter(objectContainer -> !objectContainer.isBusy())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Free context container was not found"))
                .getWrappedObject());

        var start = currentTimeMillis();
        var active = freeObject.isActive();
        while (active && currentTimeMillis() <= start + ofSeconds(5).toMillis()) {
            active = freeObject.isActive();
        }
        var stop = currentTimeMillis();

        assertThat(freeObject.isActive(), is(false));
        //5 seconds is defined. -1 second of the sleeping. see above
        assertThat(new BigDecimal(stop - start),
                closeTo(new BigDecimal(5000), new BigDecimal(1000)));
    }

    @Test(dependsOnMethods = {"resourcesFreeByDefaultTest", "resourcesFreeAfterDefinedTimeTest"})
    public void resourcesAreBusyAgain() throws Exception {
        TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY.accept(true);
        FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT.accept(SECONDS);
        FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE.accept(1L);

        setFree(thread2);
        sleep(1500);

        var containerList = new ArrayList<>(containers);
        var freeContainer = containerList.stream().filter(objectContainer -> !objectContainer.isBusy())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Free context container was not found"));
        var freeObject = ((TestContext) freeContainer.getWrappedObject());

        assertThat(freeObject.isActive(), is(false));

        thread2 = createThread2();
        thread2.start();
        setBusy(thread2);
        sleep(1000);

        containerList = new ArrayList<>(containers);

        assertThat(containerList, hasSize(2));
        assertThat(containerList.stream()
                .map(ObjectContainer::getWrappedObject)
                .distinct()
                .collect(toList()), hasSize(2));

        assertThat(containerList.stream().map(ObjectContainer::isBusy)
                .collect(toList()), everyItem(is(true)));
        assertThat(freeObject.isActive(), is(true));
        assertThat(freeContainer.isBusy(), is(true));
    }
}
