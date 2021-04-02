package ru.tinkoff.qa.neptune.core.api.localization;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.*;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.localization.BasicLocalizationTest.SomeCriteria.*;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

public class BasicLocalizationTest {
    @DataProvider(parallel = true)
    public static Object[][] data1() {
        return new Object[][]{
                {
                        TestActionStepSupplier.methodWithoutAnnotation(),
                        "Class Description ActionSupplier"
                },
                {
                        TestActionStepSupplier.methodWithAnnotation(),
                        "Method Description ActionSupplier"
                },
                {
                        TestActionStepSupplier.methodWithCompositeAnnotation1("some string"),
                        "Method with Composite Description + some string ActionSupplier"
                },
                {
                        TestActionStepSupplier.methodWithCompositeAnnotation2("some string1", "some string2"),
                        "Method with Composite Description + some string1,some string2 ActionSupplier"
                },
                {
                        TestGetStepSupplier.methodWithoutAnnotation(),
                        "Class Description GetSupplier"
                },
                {
                        TestGetStepSupplier.methodWithAnnotation(),
                        "Method Description GetSupplier"
                },
                {
                        TestGetStepSupplier.methodWithCompositeAnnotation1("some string"),
                        "Method with Composite Description + some string GetSupplier"
                },
                {
                        TestGetStepSupplier.methodWithCompositeAnnotation2("some string1", "some string2"),
                        "Method with Composite Description + some string1,some string2 GetSupplier"
                }
        };
    }

    @DataProvider(parallel = true)
    public static Object[][] data2() {
        return new Object[][]{
                {someCriteriaWithoutAnnotation(), "This is some criteria Class"},
                {someCriteriaWithAnnotation(), "This is some criteria Method"},
                {someCriteriaWithCompositeAnnotation("sss"), "This is some criteria Method with DescriptionFragment sss"}
        };
    }

    @Test(dataProvider = "data1")
    public void test1(Supplier<?> supplier, String description) {
        assertThat(supplier.toString(), is(description));
    }

    @Test(dataProvider = "data2")
    public void test2(Criteria<Object> criteria, String value) {
        var p = TestGetStepSupplier.methodWithoutAnnotation().criteria(criteria).getParameters();
        assertThat(p, aMapWithSize(1));
        assertThat(p, hasEntry(equalTo("Criteria"), equalTo(value)));
    }

    @Description("Class Description ActionSupplier")
    static class TestActionStepSupplier extends SequentialActionSupplier<Object, Object, TestActionStepSupplier> {

        protected TestActionStepSupplier() {
            super();
        }

        public static TestActionStepSupplier methodWithoutAnnotation() {
            return new TestActionStepSupplier();
        }

        @Description("Method Description ActionSupplier")
        public static TestActionStepSupplier methodWithAnnotation() {
            return new TestActionStepSupplier();
        }

        @Description("Method with Composite Description + {element} ActionSupplier")
        public static TestActionStepSupplier methodWithCompositeAnnotation1(@DescriptionFragment("element") String s) {
            return new TestActionStepSupplier();
        }

        @Description("Method with Composite Description + {element1},{element2} ActionSupplier")
        public static TestActionStepSupplier methodWithCompositeAnnotation2(@DescriptionFragment("element1") String s1,
                                                                            @DescriptionFragment("element2") String s2) {
            return new TestActionStepSupplier();
        }

        @Override
        protected void performActionOn(Object value) {

        }
    }

    @Description("Class Description GetSupplier")
    @SequentialGetStepSupplier.DefineCriteriaParameterName
    static class TestGetStepSupplier extends SequentialGetStepSupplier<Object, Object, Object, Object, TestGetStepSupplier> {

        protected TestGetStepSupplier() {
            super();
        }

        public static TestGetStepSupplier methodWithoutAnnotation() {
            return new TestGetStepSupplier();
        }

        @Description("Method Description GetSupplier")
        public static TestGetStepSupplier methodWithAnnotation() {
            return new TestGetStepSupplier();
        }

        @Description("Method with Composite Description + {element} GetSupplier")
        public static TestGetStepSupplier methodWithCompositeAnnotation1(@DescriptionFragment("element") String s) {
            return new TestGetStepSupplier();
        }

        @Description("Method with Composite Description + {element1},{element2} GetSupplier")
        public static TestGetStepSupplier methodWithCompositeAnnotation2(@DescriptionFragment("element1") String s1,
                                                                         @DescriptionFragment("element2") String s2) {
            return new TestGetStepSupplier();
        }

        @Override
        protected Function<Object, Object> getEndFunction() {
            return null;
        }

        @Override
        protected TestGetStepSupplier criteria(Criteria<? super Object> criteria) {
            return super.criteria(criteria);
        }
    }

    @Description("This is some criteria Class")
    static class SomeCriteria {

        public static <T> Criteria<T> someCriteriaWithoutAnnotation() {
            return condition(o -> true);
        }

        @Description("This is some criteria Method")
        public static <T> Criteria<T> someCriteriaWithAnnotation() {
            return condition(o -> true);
        }

        @Description("This is some criteria Method with DescriptionFragment {element}")
        public static <T> Criteria<T> someCriteriaWithCompositeAnnotation(@DescriptionFragment("element") String element) {
            return condition(o -> true);
        }
    }

}
