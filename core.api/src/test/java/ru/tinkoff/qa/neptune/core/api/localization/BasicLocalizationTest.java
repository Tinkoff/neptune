package ru.tinkoff.qa.neptune.core.api.localization;

import net.bytebuddy.asm.Advice;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.localization.BasicLocalizationTest.SomeCriteria.*;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

@SuppressWarnings("unused")
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
                TestActionStepSupplier.methodWithCompositeAnnotation3("some string1", "some string2"),
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
            },
            {
                TestGetStepSupplier.methodWithCompositeAnnotation3("some string1", "some string2"),
                "Method with Composite Description + some string1,some string2 GetSupplier"
            },
            {
                TestGetStepSupplier.methodDelegator(),
                "Real method which was invoked"
            }
        };
    }

    @DataProvider(parallel = true)
    public static Object[][] data2() {
        return new Object[][]{
            {someCriteriaWithoutAnnotation(), "not described criteria"},
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
        arrayContaining(List.of(1, 3, "String"));
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

        @Description("Method with Composite Description + {0},{1} ActionSupplier")
        public static TestActionStepSupplier methodWithCompositeAnnotation3(String s1, String s2) {
            return new TestActionStepSupplier();
        }

        @Override
        protected void howToPerform(Object value) {

        }
    }

    @Description("Class Description GetSupplier")
    @SequentialGetStepSupplier.DefineCriteriaParameterName
    static class TestGetStepSupplier extends SequentialGetStepSupplier.GetObjectStepSupplier<Object, Object, TestGetStepSupplier> {

        protected TestGetStepSupplier() {
            super(null);
        }

        public static TestGetStepSupplier methodWithoutAnnotation() {
            return new TestGetStepSupplier();
        }

        @Description("Real method which was invoked")
        public static TestGetStepSupplier methodDelegate(String a, String b) {
            return new TestGetStepSupplier();
        }

        public static TestGetStepSupplier methodDelegator() {
            return methodDelegate("a", "b");
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

        @Description("Method with Composite Description + {0},{1} GetSupplier")
        public static TestGetStepSupplier methodWithCompositeAnnotation3(String s1, String s2) {
            return new TestGetStepSupplier();
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


    public static class MethodInterceptor {
        @Advice.OnMethodEnter
        public static void interceptEnter(
            @Advice.Origin Method method
            /*@AllArguments Object[] args,
            @SuperCall Callable<?> zuper*/) throws Exception {
            System.out.println("Entered " + method);
            /*if (isStatic(method.getModifiers()) &&
                ((returned instanceof SequentialGetStepSupplier)
                    || (returned instanceof SequentialActionSupplier)
                    || (returned instanceof Criteria))) {
                var description = translate(returned, method, args);
                if (description != null) {
                    Method setDescription = returned.getClass().getDeclaredMethod("setDescription", String.class);
                    setDescription.setAccessible(true);
                    setDescription.invoke(returned, description);
                }
            }*/
        }

        @Advice.OnMethodExit
        public static void interceptExit(
            @Advice.Origin Method method
            /*@AllArguments Object[] args,
            @SuperCall Callable<?> zuper*/) throws Exception {
            System.out.println("Exited " + method);
            /*if (isStatic(method.getModifiers()) &&
                ((returned instanceof SequentialGetStepSupplier)
                    || (returned instanceof SequentialActionSupplier)
                    || (returned instanceof Criteria))) {
                var description = translate(returned, method, args);
                if (description != null) {
                    Method setDescription = returned.getClass().getDeclaredMethod("setDescription", String.class);
                    setDescription.setAccessible(true);
                    setDescription.invoke(returned, description);
                }
            }*/
        }
    }

}
