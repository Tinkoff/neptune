package ru.tinkoff.qa.neptune.core.api.concurency;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static java.lang.System.*;
import static java.lang.Thread.sleep;
import static java.time.Duration.ofSeconds;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.concurency.ObjectContainer.containers;
import static ru.tinkoff.qa.neptune.core.api.properties.general.resorces.FreeResourcesOnInactivity.TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.general.resorces.FreeResourcesOnInactivityAfter.FreeResourcesOnInactivityAfterTimeUnit.FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT;
import static ru.tinkoff.qa.neptune.core.api.properties.general.resorces.FreeResourcesOnInactivityAfter.FreeResourcesOnInactivityAfterTimeValue.FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE;
import static ru.tinkoff.qa.neptune.core.api.steps.proxy.ProxyFactory.getProxied;

public class ConcurrencyTest {

    private final TestContext context = getProxied(TestContext.class);

    private Thread thread1;
    private Thread thread2 = new Thread(() -> {
        while (true) {
            context.get("Some value", testContext -> new Object());
        }
    });

    @BeforeMethod
    public void runThreads() throws Exception {
        containers.clear();

        thread1 = new Thread(() -> {
            while (true) {
                context.perform("Some action", testContext -> {});
            }
        });
        thread1.start();

        sleep(500);

        thread2 = new Thread(() -> {
            while (true) {
                context.get("Some value", testContext -> new Object());
            }
        });
        thread2.start();

        sleep(1000);
    }

    @AfterMethod(alwaysRun = true)
    public void clearAfter() {
        thread1.stop();
        thread2.stop();

        clearProperty(TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY.getPropertyName());
        clearProperty(FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT.getPropertyName());
        clearProperty(FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE.getPropertyName());
    }

    @Test
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

    @Test
    public void freeTest() throws Exception {
        thread2.stop();
        sleep(1000);

        var containerList = new ArrayList<>(containers);
        assertThat(containerList, hasSize(2));
        assertThat(containerList.stream()
                .map(ObjectContainer::getWrappedObject)
                .distinct()
                .collect(toList()), hasSize(2));

        assertThat(containerList.stream().map(ObjectContainer::isBusy)
                .collect(toList()), hasItem(is(false)));
    }

    @Test
    public void resourcesStillBusyTest() {
        thread2.stop();

        var start = currentTimeMillis();
        //30 seconds is default value + additional 5 seconds
        while (currentTimeMillis() <= start + ofSeconds(35).toMillis()) {
        }

        var containerList = new ArrayList<>(containers);
        assertThat(((TestContext) containerList.stream().filter(objectContainer -> !objectContainer.isBusy())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Free context container was not found"))
                .getWrappedObject()).isActive(),
                is(true));
    }

    @Test
    public void resourcesFreeByDefaultTest() throws Exception {
        setProperty(TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY.getPropertyName(), "true");
        thread2.stop();
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

    @Test
    public void resourcesFreeAfterDefinedTimeTest() throws Exception {
        setProperty(TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY.getPropertyName(), "true");
        setProperty(FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE.getPropertyName(), "5");
        thread2.stop();
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

    @Test(dependsOnMethods = {"resourcesFreeByDefaultTest", "resourcesFreeAfterDefinedTimeTest", "resourcesStillBusyTest"})
    public void resourcesAreBusyAgain() throws Exception {
        setProperty(TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY.getPropertyName(), "true");
        setProperty(FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE.getPropertyName(), "1");

        thread2.stop();
        sleep(1500);

        var containerList = new ArrayList<>(containers);
        var freeContainer = containerList.stream().filter(objectContainer -> !objectContainer.isBusy())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Free context container was not found"));
        var freeObject = ((TestContext) freeContainer.getWrappedObject());

        assertThat(freeObject.isActive(), is(false));

        thread2 = new Thread(() -> {
            while (true) {
                context.get("Some value", testContext -> new Object());
            }
        });
        thread2.start();
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
