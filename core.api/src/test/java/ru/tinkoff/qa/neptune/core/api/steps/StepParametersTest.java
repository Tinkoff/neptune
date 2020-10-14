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
