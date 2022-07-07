package ru.tinkoff.qa.neptune.core.api.steps.parameters;

import org.hamcrest.Matcher;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

import java.util.Map;

import static java.time.Duration.ofMillis;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapInOrder;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.core.api.steps.TestEventLogger.ADDITIONAL_PARAMETERS;
import static ru.tinkoff.qa.neptune.core.api.steps.TestEventLogger.PARAMETERS;
import static ru.tinkoff.qa.neptune.core.api.steps.parameters.TestActionStepSupplier.getTestActionStepSupplier;
import static ru.tinkoff.qa.neptune.core.api.steps.parameters.TestActionStepSupplier2.getTestActionStepSupplier2;
import static ru.tinkoff.qa.neptune.core.api.steps.parameters.TestGetStepSupplier.getTestGetStepSupplier;
import static ru.tinkoff.qa.neptune.core.api.steps.parameters.TestGetStepSupplier2.getTestGetStepSupplier2;
import static ru.tinkoff.qa.neptune.core.api.steps.parameters.TestGetStepSupplier3.getTestGetStepSupplier3;

public class RuntimeStepParameterTest {

    @DataProvider
    public static Object[][] data1() {
        return new Object[][]{
                {
                        getTestGetStepSupplier().from(getTestGetStepSupplier2()
                                        .from(new Object())
                                        .setParam1(true)
                                        .setParam2("Some string")
                                        .timeOut(ofMillis(500))
                                        .pollingInterval(ofMillis(50))
                                        .criteria("Some criteria", o -> true)
                                        .criteria(condition("Some criteria 2", o -> true)))
                                .setParam1(true)
                                .setParam2("Some string")
                                .timeOut(ofMillis(500))
                                .pollingInterval(ofMillis(50))
                                .criteria("Some criteria", o -> true)
                                .criteria(condition("Some criteria 2", o -> true)),
                        iterableInOrder(
                                mapInOrder(
                                        mapEntry("Parameter 1", "true"),
                                        mapEntry("Parameter 2", "Some string"),
                                        mapEntry("Criteria", "Some criteria"),
                                        mapEntry("Criteria 2", "Some criteria 2"),
                                        mapEntry("Timeout/time for retrying", "00:00:00.500"),
                                        mapEntry("Polling time", "00:00:00.050"),
                                        mapEntry("Get from", "getTestGetStepSupplier2"),
                                        mapEntry("Parameter 11", "true"),
                                        mapEntry("Parameter 21", "Some string"),
                                        mapEntry("Custom criteria", "Some criteria"),
                                        mapEntry("Custom criteria 2", "Some criteria 2"),
                                        mapEntry("Custom Time out", "00:00:00.500"),
                                        mapEntry("Custom sleeping", "00:00:00.050")
                                ),
                                mapInOrder(
                                        mapEntry("Parameter 11", "true"),
                                        mapEntry("Parameter 21", "Some string"),
                                        mapEntry("Custom criteria", "Some criteria"),
                                        mapEntry("Custom criteria 2", "Some criteria 2"),
                                        mapEntry("Custom Time out", "00:00:00.500"),
                                        mapEntry("Custom sleeping", "00:00:00.050")
                                )
                        ),

                        iterableInOrder(
                                mapInOrder(
                                        mapEntry("Get Step 2: test parameter 1", "test value 1"),
                                        mapEntry("Get Step 2: test parameter 2", "test value 2"),
                                        mapEntry("Get Step 2: test parameter 3", "test value 3"),
                                        mapEntry("Get Step 2: test parameter 4", "test value 4")
                                ),
                                mapInOrder(
                                        mapEntry("Get Step 2: test parameter 1", "test value 1"),
                                        mapEntry("Get Step 2: test parameter 2", "test value 2"),
                                        mapEntry("Get Step 2: test parameter 3", "test value 3"),
                                        mapEntry("Get Step 2: test parameter 4", "test value 4"),
                                        mapEntry("Get Step 1: test parameter 1", "test value 1"),
                                        mapEntry("Get Step 1: test parameter 2", "test value 2"),
                                        mapEntry("Get Step 1: test parameter 3", "test value 3"),
                                        mapEntry("Get Step 1: test parameter 4", "test value 4")
                                )
                        ),
                },

                {
                        getTestGetStepSupplier2().from(getTestGetStepSupplier()
                                        .from(new Object())
                                        .setParam1(true)
                                        .setParam2("Some string")
                                        .timeOut(ofMillis(500))
                                        .pollingInterval(ofMillis(50))
                                        .criteria("Some criteria", o -> true)
                                        .criteria(condition("Some criteria 2", o -> true)))
                                .setParam1(true)
                                .setParam2("Some string")
                                .timeOut(ofMillis(500))
                                .pollingInterval(ofMillis(50))
                                .criteria("Some criteria", o -> true)
                                .criteria(condition("Some criteria 2", o -> true)),
                        iterableInOrder(
                                mapInOrder(
                                        mapEntry("Parameter 11", "true"),
                                        mapEntry("Parameter 21", "Some string"),
                                        mapEntry("Custom criteria", "Some criteria"),
                                        mapEntry("Custom criteria 2", "Some criteria 2"),
                                        mapEntry("Custom Time out", "00:00:00.500"),
                                        mapEntry("Custom sleeping", "00:00:00.050"),
                                        mapEntry("Custom from", "getTestGetStepSupplier")
                                ),
                                mapInOrder(
                                        mapEntry("Parameter 1", "true"),
                                        mapEntry("Parameter 2", "Some string"),
                                        mapEntry("Criteria", "Some criteria"),
                                        mapEntry("Criteria 2", "Some criteria 2"),
                                        mapEntry("Timeout/time for retrying", "00:00:00.500"),
                                        mapEntry("Polling time", "00:00:00.050")
                                )
                        ),
                        iterableInOrder(
                                mapInOrder(
                                        mapEntry("Get Step 1: test parameter 1", "test value 1"),
                                        mapEntry("Get Step 1: test parameter 2", "test value 2"),
                                        mapEntry("Get Step 1: test parameter 3", "test value 3"),
                                        mapEntry("Get Step 1: test parameter 4", "test value 4")
                                ),
                                mapInOrder(
                                        mapEntry("Get Step 2: test parameter 1", "test value 1"),
                                        mapEntry("Get Step 2: test parameter 2", "test value 2"),
                                        mapEntry("Get Step 2: test parameter 3", "test value 3"),
                                        mapEntry("Get Step 2: test parameter 4", "test value 4")
                                )
                        ),
                },

                {
                        getTestGetStepSupplier().from(getTestGetStepSupplier3()
                                        .from(new Object())
                                        .timeOut(ofMillis(500))
                                        .pollingInterval(ofMillis(50))
                                        .criteria("Some criteria", o -> true)
                                        .criteria(condition("Some criteria 2", o -> true)))
                                .setParam1(true)
                                .setParam2("Some string")
                                .timeOut(ofMillis(500))
                                .pollingInterval(ofMillis(50))
                                .criteria("Some criteria", o -> true)
                                .criteria(condition("Some criteria 2", o -> true)),
                        iterableInOrder(
                                mapInOrder(
                                        mapEntry("Parameter 1", "true"),
                                        mapEntry("Parameter 2", "Some string"),
                                        mapEntry("Criteria", "Some criteria"),
                                        mapEntry("Criteria 2", "Some criteria 2"),
                                        mapEntry("Timeout/time for retrying", "00:00:00.500"),
                                        mapEntry("Polling time", "00:00:00.050"),
                                        mapEntry("Get from", "getTestGetStepSupplier3")
                                ),
                                anEmptyMap()
                        ),

                        iterableInOrder(
                                mapInOrder(
                                        mapEntry("Get Step 1: test parameter 1", "test value 1"),
                                        mapEntry("Get Step 1: test parameter 2", "test value 2"),
                                        mapEntry("Get Step 1: test parameter 3", "test value 3"),
                                        mapEntry("Get Step 1: test parameter 4", "test value 4")
                                )
                        ),
                },

                {
                        getTestGetStepSupplier3().from(getTestGetStepSupplier()
                                        .from(new Object())
                                        .setParam1(true)
                                        .setParam2("Some string")
                                        .timeOut(ofMillis(500))
                                        .pollingInterval(ofMillis(50))
                                        .criteria("Some criteria", o -> true)
                                        .criteria(condition("Some criteria 2", o -> true)))
                                .timeOut(ofMillis(500))
                                .pollingInterval(ofMillis(50))
                                .criteria("Some criteria", o -> true)
                                .criteria(condition("Some criteria 2", o -> true)),
                        iterableInOrder(
                                anEmptyMap(),
                                mapInOrder(
                                        mapEntry("Parameter 1", "true"),
                                        mapEntry("Parameter 2", "Some string"),
                                        mapEntry("Criteria", "Some criteria"),
                                        mapEntry("Criteria 2", "Some criteria 2"),
                                        mapEntry("Timeout/time for retrying", "00:00:00.500"),
                                        mapEntry("Polling time", "00:00:00.050")
                                )
                        ),
                        iterableInOrder(
                                mapInOrder(
                                        mapEntry("Get Step 1: test parameter 1", "test value 1"),
                                        mapEntry("Get Step 1: test parameter 2", "test value 2"),
                                        mapEntry("Get Step 1: test parameter 3", "test value 3"),
                                        mapEntry("Get Step 1: test parameter 4", "test value 4")
                                )
                        ),
                },
        };
    }

    @DataProvider
    public static Object[][] data2() {
        return new Object[][]{
                {
                        getTestActionStepSupplier().performOn(getTestGetStepSupplier()
                                .from(getTestGetStepSupplier2()
                                        .from(new Object())
                                        .setParam1(true)
                                        .setParam2("Some string")
                                        .timeOut(ofMillis(500))
                                        .pollingInterval(ofMillis(50))
                                        .criteria("Some criteria", o -> true)
                                        .criteria(condition("Some criteria 2", o -> true)))
                                .setParam1(true)
                                .setParam2("Some string")
                                .timeOut(ofMillis(500))
                                .pollingInterval(ofMillis(50))
                                .criteria("Some criteria", o -> true)
                                .criteria(condition("Some criteria 2", o -> true))
                        ),
                        iterableInOrder(
                                mapInOrder(
                                        mapEntry("Action Parameter 1", "null"),
                                        mapEntry("Perform action on", "getTestGetStepSupplier"),
                                        mapEntry("Parameter 1", "true"),
                                        mapEntry("Parameter 2", "Some string"),
                                        mapEntry("Criteria", "Some criteria"),
                                        mapEntry("Criteria 2", "Some criteria 2"),
                                        mapEntry("Timeout/time for retrying", "00:00:00.500"),
                                        mapEntry("Polling time", "00:00:00.050")
                                ),
                                mapInOrder(
                                        mapEntry("Parameter 1", "true"),
                                        mapEntry("Parameter 2", "Some string"),
                                        mapEntry("Criteria", "Some criteria"),
                                        mapEntry("Criteria 2", "Some criteria 2"),
                                        mapEntry("Timeout/time for retrying", "00:00:00.500"),
                                        mapEntry("Polling time", "00:00:00.050"),
                                        mapEntry("Get from", "getTestGetStepSupplier2"),
                                        mapEntry("Parameter 11", "true"),
                                        mapEntry("Parameter 21", "Some string"),
                                        mapEntry("Custom criteria", "Some criteria"),
                                        mapEntry("Custom criteria 2", "Some criteria 2"),
                                        mapEntry("Custom Time out", "00:00:00.500"),
                                        mapEntry("Custom sleeping", "00:00:00.050")
                                ),
                                mapInOrder(
                                        mapEntry("Parameter 11", "true"),
                                        mapEntry("Parameter 21", "Some string"),
                                        mapEntry("Custom criteria", "Some criteria"),
                                        mapEntry("Custom criteria 2", "Some criteria 2"),
                                        mapEntry("Custom Time out", "00:00:00.500"),
                                        mapEntry("Custom sleeping", "00:00:00.050")
                                )
                        ),

                        iterableInOrder(
                                mapInOrder(
                                        mapEntry("Get Step 2: test parameter 1", "test value 1"),
                                        mapEntry("Get Step 2: test parameter 2", "test value 2"),
                                        mapEntry("Get Step 2: test parameter 3", "test value 3"),
                                        mapEntry("Get Step 2: test parameter 4", "test value 4")
                                ),
                                mapInOrder(
                                        mapEntry("Get Step 2: test parameter 1", "test value 1"),
                                        mapEntry("Get Step 2: test parameter 2", "test value 2"),
                                        mapEntry("Get Step 2: test parameter 3", "test value 3"),
                                        mapEntry("Get Step 2: test parameter 4", "test value 4"),
                                        mapEntry("Get Step 1: test parameter 1", "test value 1"),
                                        mapEntry("Get Step 1: test parameter 2", "test value 2"),
                                        mapEntry("Get Step 1: test parameter 3", "test value 3"),
                                        mapEntry("Get Step 1: test parameter 4", "test value 4")
                                ),
                                mapInOrder(
                                        mapEntry("Get Step 2: test parameter 1", "test value 1"),
                                        mapEntry("Get Step 2: test parameter 2", "test value 2"),
                                        mapEntry("Get Step 2: test parameter 3", "test value 3"),
                                        mapEntry("Get Step 2: test parameter 4", "test value 4"),
                                        mapEntry("Get Step 1: test parameter 1", "test value 1"),
                                        mapEntry("Get Step 1: test parameter 2", "test value 2"),
                                        mapEntry("Get Step 1: test parameter 3", "test value 3"),
                                        mapEntry("Get Step 1: test parameter 4", "test value 4"),
                                        mapEntry("Action Step 1: test parameter 1", "test value 1"),
                                        mapEntry("Action Step 1: test parameter 2", "test value 2"),
                                        mapEntry("Action Step 1: test parameter 3", "test value 3"),
                                        mapEntry("Action Step 1: test parameter 4", "test value 4")
                                )
                        ),
                },

                {
                        getTestActionStepSupplier2().performOn(getTestGetStepSupplier()
                                .from(getTestGetStepSupplier2()
                                        .from(new Object())
                                        .setParam1(true)
                                        .setParam2("Some string")
                                        .timeOut(ofMillis(500))
                                        .pollingInterval(ofMillis(50))
                                        .criteria("Some criteria", o -> true)
                                        .criteria(condition("Some criteria 2", o -> true)))
                                .setParam1(true)
                                .setParam2("Some string")
                                .timeOut(ofMillis(500))
                                .pollingInterval(ofMillis(50))
                                .criteria("Some criteria", o -> true)
                                .criteria(condition("Some criteria 2", o -> true))
                        ),
                        iterableInOrder(
                                mapInOrder(
                                        mapEntry("Action Parameter 1", "null"),
                                        mapEntry("Perform on custom", "getTestGetStepSupplier")
                                ),
                                mapInOrder(
                                        mapEntry("Parameter 1", "true"),
                                        mapEntry("Parameter 2", "Some string"),
                                        mapEntry("Criteria", "Some criteria"),
                                        mapEntry("Criteria 2", "Some criteria 2"),
                                        mapEntry("Timeout/time for retrying", "00:00:00.500"),
                                        mapEntry("Polling time", "00:00:00.050"),
                                        mapEntry("Get from", "getTestGetStepSupplier2"),
                                        mapEntry("Parameter 11", "true"),
                                        mapEntry("Parameter 21", "Some string"),
                                        mapEntry("Custom criteria", "Some criteria"),
                                        mapEntry("Custom criteria 2", "Some criteria 2"),
                                        mapEntry("Custom Time out", "00:00:00.500"),
                                        mapEntry("Custom sleeping", "00:00:00.050")
                                ),
                                mapInOrder(
                                        mapEntry("Parameter 11", "true"),
                                        mapEntry("Parameter 21", "Some string"),
                                        mapEntry("Custom criteria", "Some criteria"),
                                        mapEntry("Custom criteria 2", "Some criteria 2"),
                                        mapEntry("Custom Time out", "00:00:00.500"),
                                        mapEntry("Custom sleeping", "00:00:00.050")
                                )
                        ),

                        iterableInOrder(
                                mapInOrder(
                                        mapEntry("Get Step 2: test parameter 1", "test value 1"),
                                        mapEntry("Get Step 2: test parameter 2", "test value 2"),
                                        mapEntry("Get Step 2: test parameter 3", "test value 3"),
                                        mapEntry("Get Step 2: test parameter 4", "test value 4")
                                ),
                                mapInOrder(
                                        mapEntry("Get Step 2: test parameter 1", "test value 1"),
                                        mapEntry("Get Step 2: test parameter 2", "test value 2"),
                                        mapEntry("Get Step 2: test parameter 3", "test value 3"),
                                        mapEntry("Get Step 2: test parameter 4", "test value 4"),
                                        mapEntry("Get Step 1: test parameter 1", "test value 1"),
                                        mapEntry("Get Step 1: test parameter 2", "test value 2"),
                                        mapEntry("Get Step 1: test parameter 3", "test value 3"),
                                        mapEntry("Get Step 1: test parameter 4", "test value 4")
                                ),
                                mapInOrder(
                                        mapEntry("Action Step 2: test parameter 1", "test value 1"),
                                        mapEntry("Action Step 2: test parameter 2", "test value 2"),
                                        mapEntry("Action Step 2: test parameter 3", "test value 3"),
                                        mapEntry("Action Step 2: test parameter 4", "test value 4")
                                )
                        ),
                }
        };
    }

    @BeforeMethod
    public void prepare() {
        PARAMETERS.clear();
        ADDITIONAL_PARAMETERS.clear();
    }

    @Test(dataProvider = "data1")
    public void getStepRuntimeParameters(SequentialGetStepSupplier<Object, ?, ?, ?, ?> s,
                                         Matcher<Iterable<Map<String, String>>> parameterMatchers,
                                         Matcher<Iterable<Map<String, String>>> additionalParameterMatchers) {
        s.get().apply(new Object());
        assertThat(PARAMETERS, parameterMatchers);
        assertThat(ADDITIONAL_PARAMETERS, additionalParameterMatchers);
    }

    @Test(dataProvider = "data2")
    public void actionStepRuntimeParameters(SequentialActionSupplier<Object, ?, ?> s,
                                            Matcher<Iterable<Map<String, String>>> parameterMatchers,
                                            Matcher<Iterable<Map<String, String>>> additionalParameterMatchers) {
        s.get().performAction(new Object());
        assertThat(PARAMETERS, parameterMatchers);
        assertThat(ADDITIONAL_PARAMETERS, additionalParameterMatchers);
    }
}
