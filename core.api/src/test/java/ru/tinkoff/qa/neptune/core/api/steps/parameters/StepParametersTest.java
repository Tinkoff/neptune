package ru.tinkoff.qa.neptune.core.api.steps.parameters;

import org.hamcrest.Matcher;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

import java.util.Map;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static java.util.Arrays.stream;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.ONLY_ONE;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.core.api.steps.parameters.TestActionStepSupplier.getTestActionStepSupplier;
import static ru.tinkoff.qa.neptune.core.api.steps.parameters.TestActionStepSupplier2.getTestActionStepSupplier2;
import static ru.tinkoff.qa.neptune.core.api.steps.parameters.TestGetStepSupplier.getTestGetStepSupplier;
import static ru.tinkoff.qa.neptune.core.api.steps.parameters.TestGetStepSupplier2.getTestGetStepSupplier2;

public class StepParametersTest {

    @DataProvider
    public static Object[][] data1() {
        return new Object[][]{
                {new TestGetStepSupplier(), hasEntry(equalTo("Parameter 1"), equalTo("null"))},
                {new TestGetStepSupplier2(), hasEntry(equalTo("Parameter 11"), equalTo("null"))}
        };
    }

    @DataProvider
    public static Object[][] data2() {
        return new Object[][]{
                {getTestGetStepSupplier().from(getTestGetStepSupplier2()),
                        "Criteria",
                        "Timeout/time for retrying",
                        "Polling time",
                        hasEntry(equalTo("Get from"), anything())},

                {getTestGetStepSupplier2().from(getTestGetStepSupplier()),
                        "Custom criteria",
                        "Custom Time out",
                        "Custom sleeping",
                        hasEntry(equalTo("Custom from"), anything())},

                {getTestGetStepSupplier().from(o -> null),
                        "Criteria",
                        "Timeout/time for retrying",
                        "Polling time",
                        not(hasEntry(equalTo("Get from"), anything()))},

                {getTestGetStepSupplier2().from(o -> null),
                        "Custom criteria",
                        "Custom Time out",
                        "Custom sleeping",
                        not(hasEntry(equalTo("Custom from"), anything()))},

                {getTestGetStepSupplier().from(5),
                        "Criteria",
                        "Timeout/time for retrying",
                        "Polling time",
                        hasEntry(equalTo("Get from"), equalTo("5"))},

                {getTestGetStepSupplier2().from(5),
                        "Custom criteria",
                        "Custom Time out",
                        "Custom sleeping",
                        hasEntry(equalTo("Custom from"), equalTo("5"))},
        };
    }

    @DataProvider
    public static Object[][] data3() {
        return new Object[][]{
                {new TestActionStepSupplier()},
                {new TestActionStepSupplier2()}
        };
    }

    @DataProvider
    public static Object[][] data4() {
        return new Object[][]{
                {getTestActionStepSupplier().performOn(getTestGetStepSupplier().from(new Object())),
                        hasEntry(equalTo("Perform action on"), anything())},
                {getTestActionStepSupplier2().performOn(getTestGetStepSupplier().from(new Object())),
                        hasEntry(equalTo("Perform on custom"), anything())},
                {getTestActionStepSupplier().performOn(o -> null),
                        not(hasEntry(equalTo("Perform action on"), anything()))},

                {getTestActionStepSupplier2().performOn(o -> null),
                        not(hasEntry(equalTo("Perform on custom"), anything()))},

                {getTestActionStepSupplier().performOn(5),
                        hasEntry(equalTo("Perform action on"), equalTo("5"))},

                {getTestActionStepSupplier2().performOn(5),
                        hasEntry(equalTo("Perform on custom"), equalTo("5"))},
        };
    }

    @DataProvider
    public static Object[][] data5() {
        return new Object[][]{
                {getTestGetStepSupplier().from(getTestGetStepSupplier2()
                        .setParam1(true)
                        .setParam2("Some string")
                        .timeOut(ofSeconds(5))
                        .pollingInterval(ofMillis(500))
                        .criteria("Some criteria 1", o -> true)
                        .criteria(ONLY_ONE(condition("Some criteria 2", o -> true), condition("Some criteria 3", o -> true)))),
                        hasKey("Custom criteria"),
                        hasKey("Custom criteria 2"),
                        hasKey("Custom Time out"),
                        hasKey("Custom sleeping"),
                        hasKey("Parameter 11"),
                        hasKey("Parameter 21")},

                {getTestGetStepSupplier2().from(getTestGetStepSupplier()
                        .setParam1(true)
                        .setParam2("Some string")
                        .timeOut(ofSeconds(5))
                        .pollingInterval(ofMillis(500))
                        .criteria("Some criteria 1", o -> true)
                        .criteria(ONLY_ONE(condition("Some criteria 2", o -> true), condition("Some criteria 3", o -> true)))),
                        anything(),
                        not(hasKey("Criteria")),
                        not(hasKey("Criteria 2")),
                        not(hasKey("Timeout/time for retrying")),
                        not(hasKey("Polling time")),
                        not(hasKey("Parameter 1")),
                        not(hasKey("Parameter 2"))},
        };
    }

    @DataProvider
    public static Object[][] data6() {
        return new Object[][]{
                {getTestActionStepSupplier().performOn(getTestGetStepSupplier2()
                        .setParam1(true)
                        .setParam2("Some string")
                        .timeOut(ofSeconds(5))
                        .pollingInterval(ofMillis(500))
                        .criteria("Some criteria 1", o -> true)
                        .criteria(ONLY_ONE(condition("Some criteria 2", o -> true), condition("Some criteria 3", o -> true)))),
                        hasKey("Custom criteria"),
                        hasKey("Custom criteria 2"),
                        hasKey("Custom Time out"),
                        hasKey("Custom sleeping"),
                        hasKey("Parameter 11"),
                        hasKey("Parameter 21")},

                {getTestActionStepSupplier2().performOn(getTestGetStepSupplier()
                        .setParam1(true)
                        .setParam2("Some string")
                        .timeOut(ofSeconds(5))
                        .pollingInterval(ofMillis(500))
                        .criteria("Some criteria 1", o -> true)
                        .criteria(ONLY_ONE(condition("Some criteria 2", o -> true), condition("Some criteria 3", o -> true)))),
                        anything(),
                        not(hasKey("Criteria")),
                        not(hasKey("Criteria 2")),
                        not(hasKey("Timeout/time for retrying")),
                        not(hasKey("Polling time")),
                        not(hasKey("Parameter 1")),
                        not(hasKey("Parameter 2"))},
        };
    }

    @Test(description = "When SequentialGetStepSupplier has no parameters",
            dataProvider = "data1")
    public void test1(SequentialGetStepSupplier<?, ?, ?, ?, ?> s, Matcher<Map<?, ?>> matcher) {
        var p = s.getParameters();
        assertThat(p, aMapWithSize(1));
        assertThat(p, matcher);
    }

    @Test(description = "SequentialGetStepSupplier has reserved parameters",
            dataProvider = "data2")
    public void test2(SequentialGetStepSupplier<?, ?, ?, ?, ?> s,
                      String criteriaName,
                      String timeName,
                      String sleepName,
                      Matcher<Map<?, ?>> entryMatcher) {

        Map<String, String> p = null;
        if (s instanceof TestGetStepSupplier) {
            p = ((TestGetStepSupplier) s).timeOut(ofSeconds(5))
                    .pollingInterval(ofMillis(500))
                    .criteria("Some criteria 1", o -> true)
                    .criteria(ONLY_ONE(condition("Some criteria 2", o -> true), condition("Some criteria 3", o -> true)))
                    .getParameters();
        }

        if (s instanceof TestGetStepSupplier2) {
            p = ((TestGetStepSupplier2) s).timeOut(ofSeconds(5))
                    .pollingInterval(ofMillis(500))
                    .criteria("Some criteria 1", o -> true)
                    .criteria(ONLY_ONE(condition("Some criteria 2", o -> true), condition("Some criteria 3", o -> true)))
                    .getParameters();
        }

        assertThat(p, hasEntry(equalTo(criteriaName), equalTo("Some criteria 1")));
        assertThat(p, hasEntry(equalTo(criteriaName + " 2"), equalTo("(Some criteria 2) xor (Some criteria 3)")));
        assertThat(p, hasEntry(equalTo(timeName), equalTo("00:00:05.000")));
        assertThat(p, hasEntry(equalTo(sleepName), equalTo("00:00:00.500")));
        assertThat(p, entryMatcher);
        assertThat(p.keySet(), containsInRelativeOrder(criteriaName, criteriaName + " 2", timeName, sleepName));
    }

    @Test(description = "SequentialGetStepSupplier has extra parameters")
    public void test3() {
        var p = new TestGetStepSupplier()
                .setParam1(true)
                .setParam2("ABC")
                .setParam3(5)
                .setParamObj(new ParamObj()
                        .setParam1(true)
                        .setParam1(true)
                        .setParam2("ABC")
                        .setParam3(5))
                .getParameters();

        assertThat(p, aMapWithSize(4));
        assertThat(p, hasEntry(equalTo("Parameter 1"), equalTo("true")));
        assertThat(p, hasEntry(equalTo("Pojo Parameter 1"), equalTo("true")));
        assertThat(p, hasEntry(equalTo("Pojo Parameter 2"), equalTo("ABC")));
        assertThat(p, hasEntry(equalTo("Parameter 2"), equalTo("ABC")));
        assertThat(p.keySet(), containsInRelativeOrder("Parameter 1", "Pojo Parameter 1", "Pojo Parameter 2", "Parameter 2"));
    }

    @Test(description = "SequentialGetStepSupplier has all parameters", dataProvider = "data2")
    public void test4(SequentialGetStepSupplier<?, ?, ?, ?, ?> s,
                      String criteriaName,
                      String timeName,
                      String sleepName,
                      Matcher<Map<?, ?>> entryMatcher) {

        if (s instanceof TestGetStepSupplier) {
            ((TestGetStepSupplier) s).setParam1(true)
                    .setParam2("ABC")
                    .setParam3(5)
                    .timeOut(ofSeconds(5))
                    .pollingInterval(ofMillis(500))
                    .criteria("Some criteria 1", o -> true)
                    .criteria(ONLY_ONE(condition("Some criteria 2", o -> true), condition("Some criteria 3", o -> true)))
                    .setParamObj(new ParamObj()
                            .setParam1(true)
                            .setParam1(true)
                            .setParam2("ABC")
                            .setParam3(5))
                    .getParameters();
        }

        if (s instanceof TestGetStepSupplier2) {
            ((TestGetStepSupplier2) s).setParam1(true)
                    .setParam2("ABC")
                    .setParam3(5)
                    .timeOut(ofSeconds(5))
                    .pollingInterval(ofMillis(500))
                    .criteria("Some criteria 1", o -> true)
                    .criteria(ONLY_ONE(condition("Some criteria 2", o -> true), condition("Some criteria 3", o -> true)))
                    .setParamObj(new ParamObj()
                            .setParam1(true)
                            .setParam1(true)
                            .setParam2("ABC")
                            .setParam3(5))
                    .getParameters();
        }

        var p = s.getParameters();
        assertThat(p, hasEntry(equalTo(criteriaName), equalTo("Some criteria 1")));
        assertThat(p, hasEntry(equalTo(criteriaName + " 2"), equalTo("(Some criteria 2) xor (Some criteria 3)")));
        assertThat(p, hasEntry(equalTo(timeName), equalTo("00:00:05.000")));
        assertThat(p, hasEntry(equalTo(sleepName), equalTo("00:00:00.500")));
        assertThat(p, entryMatcher);
        assertThat(p, hasEntry(containsString("Parameter 1"), equalTo("true")));
        assertThat(p, hasEntry(equalTo("Pojo Parameter 1"), equalTo("true")));
        assertThat(p, hasEntry(equalTo("Pojo Parameter 2"), equalTo("ABC")));
        assertThat(p, hasEntry(containsString("Parameter 2"), equalTo("ABC")));

        assertThat(p.keySet(), containsInRelativeOrder(containsString("Parameter 1"),
                equalTo("Pojo Parameter 1"),
                equalTo("Pojo Parameter 2"),
                containsString("Parameter 2"),
                equalTo(criteriaName), equalTo(criteriaName + " 2"), equalTo(timeName), equalTo(sleepName)));
    }

    @Test(description = "When SequentialActionSupplier has no parameters",
            dataProvider = "data3")
    public void test5(SequentialActionSupplier<?, ?, ?> s) {
        var p = s.getParameters();
        assertThat(p, aMapWithSize(1));
        assertThat(p, hasEntry(equalTo("Action Parameter 1"), equalTo("null")));
    }

    @Test(description = "SequentialActionSupplier has reserved parameters",
            dataProvider = "data4")
    public void test6(SequentialActionSupplier<?, ?, ?> s,
                      Matcher<Map<?, ?>> entryMatcher) {
        var p = s.getParameters();
        assertThat(p, hasEntry(equalTo("Action Parameter 1"), equalTo("null")));
        assertThat(p, entryMatcher);
        assertThat(p.keySet(), containsInRelativeOrder("Action Parameter 1"));
    }

    @Test(description = "SequentialActionSupplier has extra parameters")
    public void test7() {
        var p = new TestActionStepSupplier()
                .setParam1(true)
                .setParam2("ABC")
                .setParam3(5)
                .setParamObj(new ParamObj()
                        .setParam1(true)
                        .setParam1(true)
                        .setParam2("ABC")
                        .setParam3(5))
                .getParameters();

        assertThat(p, aMapWithSize(4));
        assertThat(p, hasEntry(equalTo("Action Parameter 1"), equalTo("true")));
        assertThat(p, hasEntry(equalTo("Pojo Parameter 1"), equalTo("true")));
        assertThat(p, hasEntry(equalTo("Pojo Parameter 2"), equalTo("ABC")));
        assertThat(p, hasEntry(equalTo("Action Parameter 2"), equalTo("ABC")));
        assertThat(p.keySet(), containsInRelativeOrder("Action Parameter 1", "Pojo Parameter 1", "Pojo Parameter 2", "Action Parameter 2"));
    }


    @Test(description = "SequentialGetStepSupplier has all parameters", dataProvider = "data4")
    public void test8(SequentialActionSupplier<?, ?, ?> s,
                      Matcher<Map<?, ?>> entryMatcher) {

        if (s instanceof TestActionStepSupplier) {
            ((TestActionStepSupplier) s).setParam1(true)
                    .setParam2("ABC")
                    .setParam3(5)
                    .setParamObj(new ParamObj()
                            .setParam1(true)
                            .setParam1(true)
                            .setParam2("ABC")
                            .setParam3(5))
                    .getParameters();
        }

        if (s instanceof TestActionStepSupplier2) {
            ((TestActionStepSupplier2) s).setParam1(true)
                    .setParam2("ABC")
                    .setParam3(5)
                    .setParamObj(new ParamObj()
                            .setParam1(true)
                            .setParam1(true)
                            .setParam2("ABC")
                            .setParam3(5))
                    .getParameters();
        }

        var p = s.getParameters();
        assertThat(p, entryMatcher);
        assertThat(p, hasEntry(equalTo("Action Parameter 1"), equalTo("true")));
        assertThat(p, hasEntry(equalTo("Pojo Parameter 1"), equalTo("true")));
        assertThat(p, hasEntry(equalTo("Pojo Parameter 2"), equalTo("ABC")));
        assertThat(p, hasEntry(equalTo("Action Parameter 2"), equalTo("ABC")));

        assertThat(p.keySet(), containsInRelativeOrder("Action Parameter 1",
                "Pojo Parameter 1",
                "Pojo Parameter 2",
                "Action Parameter 2"));
    }

    @SafeVarargs
    @Test(description = "Test of parameter including. SequentialGetStepSupplier", dataProvider = "data5")
    public final void test9(SequentialGetStepSupplier<?, ?, ?, ?, ?> supplier, Matcher<Map<String, ?>>... matchers) {
        var params = supplier.getParameters();

        stream(matchers).forEach(matcher -> assertThat(params, matcher));
    }

    @SafeVarargs
    @Test(description = "Test of parameter including. SequentialGetStepSupplier", dataProvider = "data6")
    public final void test6(SequentialActionSupplier<?, ?, ?> supplier, Matcher<Map<String, ?>>... matchers) {
        var params = supplier.getParameters();

        stream(matchers).forEach(matcher -> assertThat(params, matcher));
    }
}
