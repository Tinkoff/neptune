package ru.tinkoff.qa.neptune.core.api.steps;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ru.tinkoff.qa.neptune.core.api.steps.LocalizationTest.TestActionStepSupplier.*;

public class LocalizationTest {

    @DataProvider(parallel = true)
    public Object[][] data1() {
        return new Object[][]{
                {
                        methodWithoutAnnotation(),
                        "Class Description"
                },
                {
                        methodWithAnnotation(),
                        "Method Description"
                },
                {
                        methodWithCompositeAnnotation1("some string"),
                        "Method with Composite Description + some string"
                },
                {
                        methodWithCompositeAnnotation2("some string1", "some string2"),
                        "Method with Composite Description + some string1,some string2"
                }
        };
    }

    @Test(dataProvider = "data1")
    public void test1(TestActionStepSupplier testActionStepSupplier, String description) {
        assertThat(testActionStepSupplier.toString(), is(description));
    }

    // TODO: 11.03.2021 GETSupplier,кондишен
    @Description("Class Description")
    static class TestActionStepSupplier extends SequentialActionSupplier<Object, Object, TestActionStepSupplier> {

        protected TestActionStepSupplier() {
            super();
        }

        public static TestActionStepSupplier methodWithoutAnnotation() {
            return new TestActionStepSupplier();
        }

        @Description("Method Description")
        public static TestActionStepSupplier methodWithAnnotation() {
            return new TestActionStepSupplier();
        }

        @Description("Method with Composite Description + {element}")
        public static TestActionStepSupplier methodWithCompositeAnnotation1(@DescriptionFragment("element") String s) {
            return new TestActionStepSupplier();
        }

        @Description("Method with Composite Description + {element1},{element2}")
        public static TestActionStepSupplier methodWithCompositeAnnotation2(@DescriptionFragment("element1") String s1,
                                                                            @DescriptionFragment("element2") String s2) {
            return new TestActionStepSupplier();
        }

        @Override
        protected void performActionOn(Object value) {

        }
    }

}
