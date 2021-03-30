package ru.tinkoff.qa.neptune.core.api.localization;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeCaptureOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.*;
import ru.tinkoff.qa.neptune.core.api.steps.localization.LocalizationByResourceBundle;

import java.util.function.Function;
import java.util.function.Supplier;

import static java.lang.System.getProperties;
import static org.apache.commons.lang3.LocaleUtils.toLocale;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.localization.TranslateByResourceBundleTest.SomeCriteria.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.SUCCESS;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocaleProperty.DEFAULT_LOCALE_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocalizationEngine.DEFAULT_LOCALIZATION_ENGINE;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

public class TranslateByResourceBundleTest {

    @DataProvider()
    public static Object[][] data1() {
        return new Object[][]{
                {GetStepSupplier.methodWithoutAnnotation(), "TRATATA"},
                {GetStepSupplier.methodWithAnnotation(), "TRATATA METHOD"},
                {GetStepSupplier.methodWithCompositeAnnotation("some string"), "TRATATA + some string"},
                {GetStepSupplier.methodWithAnnotationWithoutKey(), "Method Description without Key from Annotation"},

                {ActionStepSupplier.methodWithoutAnnotation(), "CLASS"},
                {ActionStepSupplier.methodWithAnnotation(), "METHOD"},
                {ActionStepSupplier.methodWithCompositeAnnotation("some string"), "METHOD + some string"},
                {ActionStepSupplier.methodWithAnnotationWithoutKey(), "Method Description without Key from Annotation"}
        };
    }

    @DataProvider()
    public static Object[][] data2() {
        return new Object[][]{
                {someCriteriaWithoutAnnotation(), "Class Description from ResourceBundle"},
                {someCriteriaWithAnnotation(), "Method Description from ResourceBundle"},
                {someCriteriaWithAnnotationWithoutResourceBundleKey(), "Method Description from Annotation"},
                {someCriteriaWithCompositeAnnotation("sss"), "Composite Method Description from ResourceBundle + sss"}
        };
    }

    @BeforeClass
    public void beforeClass() {
        DEFAULT_LOCALE_PROPERTY.accept(toLocale("ru_RU"));
        DEFAULT_LOCALIZATION_ENGINE.accept(LocalizationByResourceBundle.class);
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);
        TestNumberCaptor.messages.clear();
    }

    @Test(dataProvider = "data1")
    public void test1(Supplier stepSupplier, String description) {
        assertThat(stepSupplier.toString(), is(description));
    }

    @Test(dataProvider = "data2")
    public void test2(Criteria<Object> criteria, String value) {
        var p = GetStepSupplier.methodWithoutAnnotation().criteria(criteria).getParameters();
        assertThat(p, aMapWithSize(1));
        assertThat(p, hasEntry(equalTo("Criteria"), equalTo(value)));
    }

    @Test
    public void test3() {
        GetStepSupplier.methodWithoutAnnotation().from(new Object()).get().apply(new Object());
        assertThat(TestNumberCaptor.messages, contains("TEST NUMBER 1"));
    }

    @AfterClass
    public void afterClass() {
        getProperties().remove(DEFAULT_LOCALIZATION_ENGINE.getName());
        getProperties().remove(DEFAULT_LOCALE_PROPERTY.getName());
        getProperties().remove(DO_CAPTURES_OF_INSTANCE.getName());
        TestNumberCaptor.messages.clear();
    }

    @Description("Class Description from Annotation")
    @MakeStringCapturesOnFinishing
    @MakeCaptureOnFinishing(typeOfCapture = Number.class)
    static class GetStepSupplier extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<Object, Object, Object, GetStepSupplier> {

        protected GetStepSupplier() {
            super(o -> o);
        }

        public static GetStepSupplier methodWithoutAnnotation() {
            return new GetStepSupplier();
        }

        @Description("Method Description from Annotation")
        public static GetStepSupplier methodWithAnnotation() {
            return new GetStepSupplier();
        }

        @Description("Method Description without Key from Annotation")
        public static GetStepSupplier methodWithAnnotationWithoutKey() {
            return new GetStepSupplier();
        }

        @Description("Method with Composite Description from Annotation + {element}")
        public static GetStepSupplier methodWithCompositeAnnotation(@DescriptionFragment("element") String s) {
            return new GetStepSupplier();
        }

        @Override
        protected Function<Object, Object> getEndFunction() {
            return o -> 1;
        }

        protected GetStepSupplier criteria(Criteria<? super Object> criteria) {
            return super.criteria(criteria);
        }

        protected GetStepSupplier from(Object o) {
            return super.from(o);
        }
    }

    @Description("Class Description from Annotation")
    static class ActionStepSupplier extends SequentialActionSupplier<Object, Object, ActionStepSupplier> {

        protected ActionStepSupplier() {
            super();
        }

        public static ActionStepSupplier methodWithoutAnnotation() {
            return new ActionStepSupplier();
        }

        @Description("Method Description from Annotation")
        public static ActionStepSupplier methodWithAnnotation() {
            return new ActionStepSupplier();
        }

        @Description("Method Description without Key from Annotation")
        public static ActionStepSupplier methodWithAnnotationWithoutKey() {
            return new ActionStepSupplier();
        }

        @Description("Method with Composite Description from Annotation + {element} ")
        public static ActionStepSupplier methodWithCompositeAnnotation(@DescriptionFragment("element") String s) {
            return new ActionStepSupplier();
        }

        @Override
        protected void performActionOn(Object value) {
        }
    }

    @Description("Class Description from Annotation")
    static class SomeCriteria {

        public static <T> Criteria<T> someCriteriaWithoutAnnotation() {
            return condition(o -> true);
        }

        @Description("Method Description from Annotation")
        public static <T> Criteria<T> someCriteriaWithAnnotation() {
            return condition(o -> true);
        }

        @Description("Method Description from Annotation")
        public static <T> Criteria<T> someCriteriaWithAnnotationWithoutResourceBundleKey() {
            return condition(o -> true);
        }

        @Description("Composite Method Description {element}")
        public static <T> Criteria<T> someCriteriaWithCompositeAnnotation(@DescriptionFragment("element") String element) {
            return condition(o -> true);
        }
    }
}
