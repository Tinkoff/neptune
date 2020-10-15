package ru.tinkoff.qa.neptune.core.api.steps;

import org.hamcrest.Matcher;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.XOR;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

public class StepParametersTest {

    @DataProvider
    public static Object[][] data1() {
        return new Object[][]{
                {new TestGetStepSupplier()},
                {new TestGetStepSupplier2()}
        };
    }

    @DataProvider
    public static Object[][] data2() {
        return new Object[][]{
                {new TestGetStepSupplier().from(new TestGetStepSupplier()),
                        6,
                        "Criteria",
                        "Timeout/time for retrying",
                        "Polling time",
                        hasEntry(equalTo("Get from"), equalTo("Test Step (is calculated while the step is executed)"))},

                {new TestGetStepSupplier2().from(new TestGetStepSupplier()),
                        6,
                        "Custom criteria",
                        "Custom Time out",
                        "Custom sleeping",
                        hasEntry(equalTo("Custom from"), equalTo("Test Step (is calculated while the step is executed)"))},

                {new TestGetStepSupplier().from(o -> null),
                        5,
                        "Criteria",
                        "Timeout/time for retrying",
                        "Polling time",
                        not(hasEntry(equalTo("Get from"), anything()))},

                {new TestGetStepSupplier2().from(o -> null),
                        5,
                        "Custom criteria",
                        "Custom Time out",
                        "Custom sleeping",
                        not(hasEntry(equalTo("Custom from"), anything()))},

                {new TestGetStepSupplier().from(5),
                        6,
                        "Criteria",
                        "Timeout/time for retrying",
                        "Polling time",
                        hasEntry(equalTo("Get from"), equalTo("5"))},

                {new TestGetStepSupplier2().from(5),
                        6,
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
                {new TestActionStepSupplier().performOn(new TestGetStepSupplier().from(new Object())),
                        2,
                        hasEntry(equalTo("Perform action on"), equalTo("Test Step (is calculated while the step is executed)"))},
                {new TestActionStepSupplier2().performOn(new TestGetStepSupplier().from(new Object())),
                        2,
                        hasEntry(equalTo("Perform on custom"), equalTo("Test Step (is calculated while the step is executed)"))},
                {new TestActionStepSupplier().performOn(o -> null),
                        1,
                        not(hasEntry(equalTo("Perform action on"), anything()))},

                {new TestActionStepSupplier2().performOn(o -> null),
                        1,
                        not(hasEntry(equalTo("Perform on custom"), anything()))},

                {new TestActionStepSupplier().performOn(5),
                        2,
                        hasEntry(equalTo("Perform action on"), equalTo("5"))},

                {new TestActionStepSupplier2().performOn(5),
                        2,
                        hasEntry(equalTo("Perform on custom"), equalTo("5"))},
        };
    }

    @Test(description = "When SequentialGetStepSupplier has no parameters",
            dataProvider = "data1")
    public void test1(SequentialGetStepSupplier<?, ?, ?, ?, ?> s) {
        var p = s.getParameters();
        assertThat(p, aMapWithSize(1));
        assertThat(p, hasEntry(equalTo("Parameter 1"), equalTo("null")));
    }

    @Test(description = "SequentialGetStepSupplier has reserved parameters",
            dataProvider = "data2")
    public void test2(SequentialGetStepSupplier<?, ?, ?, ?, ?> s,
                      int count,
                      String criteriaName,
                      String timeName,
                      String sleepName,
                      Matcher<Map<?, ?>> entryMatcher) {
        var p = s.timeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500))
                .criteria("Some criteria 1", o -> true)
                .criteria(XOR(condition("Some criteria 2", o -> true), condition("Some criteria 3", o -> true)))
                .getParameters();
        assertThat(p, aMapWithSize(count));
        assertThat(p, hasEntry(equalTo("Parameter 1"), equalTo("null")));
        assertThat(p, hasEntry(equalTo(criteriaName), equalTo("Some criteria 1")));
        assertThat(p, hasEntry(equalTo(criteriaName + " 2"), equalTo("(Some criteria 2) xor (Some criteria 3)")));
        assertThat(p, hasEntry(equalTo(timeName), equalTo("00:00:05.000")));
        assertThat(p, hasEntry(equalTo(sleepName), equalTo("00:00:00.500")));
        assertThat(p, entryMatcher);
        assertThat(p.keySet(), containsInRelativeOrder("Parameter 1", criteriaName, criteriaName + " 2", timeName, sleepName));
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
                      int count,
                      String criteriaName,
                      String timeName,
                      String sleepName,
                      Matcher<Map<?, ?>> entryMatcher) {
        s.timeOut(ofSeconds(5))
                .pollingInterval(ofMillis(500))
                .criteria("Some criteria 1", o -> true)
                .criteria(XOR(condition("Some criteria 2", o -> true), condition("Some criteria 3", o -> true)));

        if (s instanceof TestGetStepSupplier) {
            ((TestGetStepSupplier) s).setParam1(true)
                    .setParam2("ABC")
                    .setParam3(5)
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
                    .setParamObj(new ParamObj()
                            .setParam1(true)
                            .setParam1(true)
                            .setParam2("ABC")
                            .setParam3(5))
                    .getParameters();
        }

        var p = s.getParameters();
        assertThat(p, aMapWithSize(count + 3));
        assertThat(p, hasEntry(equalTo(criteriaName), equalTo("Some criteria 1")));
        assertThat(p, hasEntry(equalTo(criteriaName + " 2"), equalTo("(Some criteria 2) xor (Some criteria 3)")));
        assertThat(p, hasEntry(equalTo(timeName), equalTo("00:00:05.000")));
        assertThat(p, hasEntry(equalTo(sleepName), equalTo("00:00:00.500")));
        assertThat(p, entryMatcher);
        assertThat(p, hasEntry(equalTo("Parameter 1"), equalTo("true")));
        assertThat(p, hasEntry(equalTo("Pojo Parameter 1"), equalTo("true")));
        assertThat(p, hasEntry(equalTo("Pojo Parameter 2"), equalTo("ABC")));
        assertThat(p, hasEntry(equalTo("Parameter 2"), equalTo("ABC")));

        assertThat(p.keySet(), containsInRelativeOrder("Parameter 1",
                "Pojo Parameter 1",
                "Pojo Parameter 2",
                "Parameter 2",
                criteriaName, criteriaName + " 2", timeName, sleepName));
    }

    @Test(description = "When SequentialActionSupplier has no parameters",
            dataProvider = "data3")
    public void test5(SequentialActionSupplier<?, ?, ?> s) {
        var p = s.getParameters();
        assertThat(p, aMapWithSize(1));
        assertThat(p, hasEntry(equalTo("Parameter 1"), equalTo("null")));
    }

    @Test(description = "SequentialActionSupplier has reserved parameters",
            dataProvider = "data4")
    public void test6(SequentialActionSupplier<?, ?, ?> s,
                      int count,
                      Matcher<Map<?, ?>> entryMatcher) {
        var p = s.getParameters();
        assertThat(p, aMapWithSize(count));
        assertThat(p, hasEntry(equalTo("Parameter 1"), equalTo("null")));
        assertThat(p, entryMatcher);
        assertThat(p.keySet(), containsInRelativeOrder("Parameter 1"));
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
        assertThat(p, hasEntry(equalTo("Parameter 1"), equalTo("true")));
        assertThat(p, hasEntry(equalTo("Pojo Parameter 1"), equalTo("true")));
        assertThat(p, hasEntry(equalTo("Pojo Parameter 2"), equalTo("ABC")));
        assertThat(p, hasEntry(equalTo("Parameter 2"), equalTo("ABC")));
        assertThat(p.keySet(), containsInRelativeOrder("Parameter 1", "Pojo Parameter 1", "Pojo Parameter 2", "Parameter 2"));
    }


    @Test(description = "SequentialGetStepSupplier has all parameters", dataProvider = "data4")
    public void test8(SequentialActionSupplier<?, ?, ?> s,
                      int count,
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
        assertThat(p, aMapWithSize(count + 3));
        assertThat(p, entryMatcher);
        assertThat(p, hasEntry(equalTo("Parameter 1"), equalTo("true")));
        assertThat(p, hasEntry(equalTo("Pojo Parameter 1"), equalTo("true")));
        assertThat(p, hasEntry(equalTo("Pojo Parameter 2"), equalTo("ABC")));
        assertThat(p, hasEntry(equalTo("Parameter 2"), equalTo("ABC")));

        assertThat(p.keySet(), containsInRelativeOrder("Parameter 1",
                "Pojo Parameter 1",
                "Pojo Parameter 2",
                "Parameter 2"));
    }

    private static class TestGetStepSupplier extends SequentialGetStepSupplier.GetObjectFromArrayChainedStepSupplier<Object, Object, Object, TestGetStepSupplier> {

        @StepParameter("Parameter 1")
        private Boolean param1;
        private ParamObj paramObj;
        @StepParameter(value = "Parameter 2", doNotReportNullValues = true)
        private String param2;
        private Integer param3;

        protected TestGetStepSupplier() {
            super("Test Step", o -> new Object[]{});
        }

        @Override
        protected TestGetStepSupplier from(Object from) {
            return super.from(from);
        }

        @Override
        protected TestGetStepSupplier from(Function<Object, ?> from) {
            return super.from(from);
        }

        @Override
        protected TestGetStepSupplier from(SequentialGetStepSupplier<Object, ?, ?, ?, ?> from) {
            return super.from(from);
        }

        @Override
        protected TestGetStepSupplier criteria(Criteria<? super Object> criteria) {
            return super.criteria(criteria);
        }

        @Override
        protected TestGetStepSupplier criteria(String description, Predicate<? super Object> predicate) {
            return super.criteria(description, predicate);
        }

        @Override
        protected TestGetStepSupplier pollingInterval(Duration pollingTime) {
            return super.pollingInterval(pollingTime);
        }

        @Override
        protected TestGetStepSupplier timeOut(Duration timeOut) {
            return super.timeOut(timeOut);
        }

        public Boolean getParam1() {
            return param1;
        }

        public TestGetStepSupplier setParam1(Boolean param1) {
            this.param1 = param1;
            return this;
        }

        public String getParam2() {
            return param2;
        }

        public TestGetStepSupplier setParam2(String param2) {
            this.param2 = param2;
            return this;
        }

        public Integer getParam3() {
            return param3;
        }

        public TestGetStepSupplier setParam3(Integer param3) {
            this.param3 = param3;
            return this;
        }

        public ParamObj getParamObj() {
            return paramObj;
        }

        public TestGetStepSupplier setParamObj(ParamObj paramObj) {
            this.paramObj = paramObj;
            return this;
        }
    }

    @SequentialGetStepSupplier.DefaultParameterNames(timeOut = "Custom Time out",
            criteria = "Custom criteria",
            pollingTime = "Custom sleeping",
            from = "Custom from"
    )
    private static class TestGetStepSupplier2 extends SequentialGetStepSupplier.GetObjectFromArrayChainedStepSupplier<Object, Object, Object, TestGetStepSupplier2> {

        @StepParameter("Parameter 1")
        private Boolean param1;
        private ParamObj paramObj;
        @StepParameter(value = "Parameter 2", doNotReportNullValues = true)
        private String param2;
        private Integer param3;


        protected TestGetStepSupplier2() {
            super("Test Step", o -> new Object[]{});
        }

        @Override
        protected TestGetStepSupplier2 from(Object from) {
            return super.from(from);
        }

        @Override
        protected TestGetStepSupplier2 from(Function<Object, ?> from) {
            return super.from(from);
        }

        @Override
        protected TestGetStepSupplier2 from(SequentialGetStepSupplier<Object, ?, ?, ?, ?> from) {
            return super.from(from);
        }

        @Override
        protected TestGetStepSupplier2 criteria(Criteria<? super Object> criteria) {
            return super.criteria(criteria);
        }

        @Override
        protected TestGetStepSupplier2 criteria(String description, Predicate<? super Object> predicate) {
            return super.criteria(description, predicate);
        }

        @Override
        protected TestGetStepSupplier2 pollingInterval(Duration pollingTime) {
            return super.pollingInterval(pollingTime);
        }

        @Override
        protected TestGetStepSupplier2 timeOut(Duration timeOut) {
            return super.timeOut(timeOut);
        }

        public Boolean getParam1() {
            return param1;
        }

        public TestGetStepSupplier2 setParam1(Boolean param1) {
            this.param1 = param1;
            return this;
        }

        public String getParam2() {
            return param2;
        }

        public TestGetStepSupplier2 setParam2(String param2) {
            this.param2 = param2;
            return this;
        }

        public Integer getParam3() {
            return param3;
        }

        public TestGetStepSupplier2 setParam3(Integer param3) {
            this.param3 = param3;
            return this;
        }

        public ParamObj getParamObj() {
            return paramObj;
        }

        public TestGetStepSupplier2 setParamObj(ParamObj paramObj) {
            this.paramObj = paramObj;
            return this;
        }
    }

    private static class TestActionStepSupplier extends SequentialActionSupplier<Object, Object, TestActionStepSupplier> {

        @StepParameter("Parameter 1")
        private Boolean param1;
        private ParamObj paramObj;
        @StepParameter(value = "Parameter 2", doNotReportNullValues = true)
        private String param2;
        private Integer param3;

        protected TestActionStepSupplier() {
            super("Test action");
        }

        @Override
        protected TestActionStepSupplier performOn(Object value) {
            return super.performOn(value);
        }

        @Override
        protected TestActionStepSupplier performOn(Function<Object, ?> function) {
            return super.performOn(function);
        }

        @Override
        protected TestActionStepSupplier performOn(SequentialGetStepSupplier<Object, ?, ?, ?, ?> supplier) {
            return super.performOn(supplier);
        }

        @Override
        protected void performActionOn(Object value) {
        }

        public TestActionStepSupplier setParam1(Boolean param1) {
            this.param1 = param1;
            return this;
        }

        public String getParam2() {
            return param2;
        }

        public TestActionStepSupplier setParam2(String param2) {
            this.param2 = param2;
            return this;
        }

        public Integer getParam3() {
            return param3;
        }

        public TestActionStepSupplier setParam3(Integer param3) {
            this.param3 = param3;
            return this;
        }

        public ParamObj getParamObj() {
            return paramObj;
        }

        public TestActionStepSupplier setParamObj(ParamObj paramObj) {
            this.paramObj = paramObj;
            return this;
        }
    }

    @SequentialActionSupplier.DefaultParameterNames(performOn = "Perform on custom")
    private static class TestActionStepSupplier2 extends SequentialActionSupplier<Object, Object, TestActionStepSupplier2> {

        @StepParameter("Parameter 1")
        private Boolean param1;
        private ParamObj paramObj;
        @StepParameter(value = "Parameter 2", doNotReportNullValues = true)
        private String param2;
        private Integer param3;

        protected TestActionStepSupplier2() {
            super("Test action");
        }

        @Override
        protected TestActionStepSupplier2 performOn(Object value) {
            return super.performOn(value);
        }

        @Override
        protected TestActionStepSupplier2 performOn(Function<Object, ?> function) {
            return super.performOn(function);
        }

        @Override
        protected TestActionStepSupplier2 performOn(SequentialGetStepSupplier<Object, ?, ?, ?, ?> supplier) {
            return super.performOn(supplier);
        }

        @Override
        protected void performActionOn(Object value) {
        }

        public TestActionStepSupplier2 setParam1(Boolean param1) {
            this.param1 = param1;
            return this;
        }

        public String getParam2() {
            return param2;
        }

        public TestActionStepSupplier2 setParam2(String param2) {
            this.param2 = param2;
            return this;
        }

        public Integer getParam3() {
            return param3;
        }

        public TestActionStepSupplier2 setParam3(Integer param3) {
            this.param3 = param3;
            return this;
        }

        public ParamObj getParamObj() {
            return paramObj;
        }

        public TestActionStepSupplier2 setParamObj(ParamObj paramObj) {
            this.paramObj = paramObj;
            return this;
        }
    }

    private static class ParamObj implements StepParameterPojo {

        @StepParameter("Pojo Parameter 1")
        private Boolean param1;
        @StepParameter(value = "Pojo Parameter 2", doNotReportNullValues = true)
        private String param2;
        private Integer param3;

        public Boolean getParam1() {
            return param1;
        }

        public ParamObj setParam1(Boolean param1) {
            this.param1 = param1;
            return this;
        }

        public String getParam2() {
            return param2;
        }

        public ParamObj setParam2(String param2) {
            this.param2 = param2;
            return this;
        }

        public Integer getParam3() {
            return param3;
        }

        public ParamObj setParam3(Integer param3) {
            this.param3 = param3;
            return this;
        }
    }
}
