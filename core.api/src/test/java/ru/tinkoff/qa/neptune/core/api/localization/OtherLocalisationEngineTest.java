package ru.tinkoff.qa.neptune.core.api.localization;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.NewEngine;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

import java.util.Locale;
import java.util.function.Function;

import static java.lang.System.getProperties;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.localization.OtherLocalisationEngineTest.GetStepSupplier.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocaleProperty.DEFAULT_LOCALE_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocalizationEngine.DEFAULT_LOCALIZATION_ENGINE;

public class OtherLocalisationEngineTest {
    @DataProvider()
    public static Object[][] data1() {
        return new Object[][]{
                {
                        methodWithoutAnnotation(),
                        "I'm class"
                },
                {
                        methodWithAnnotation(),
                        "I'm method"
                },
                {
                        methodWithCompositeAnnotation("some string"),
                        "I'm method"
                }
        };
    }

    @BeforeClass
    public void beforeClass() {
        DEFAULT_LOCALIZATION_ENGINE.accept(NewEngine.class);
        DEFAULT_LOCALE_PROPERTY.accept(new Locale("RU"));
    }

    @Test(dataProvider = "data1")
    public void test1(GetStepSupplier testGetStepSupplier, String description) {
        assertThat(testGetStepSupplier.toString(), is(description));
    }

    @Test
    public void test2() {
        var supplier = methodWithoutAnnotation().from(5);

        var parameters = supplier.getParameters();
        assertThat(parameters, aMapWithSize(1));
        assertThat(parameters, hasEntry(equalTo("I'm member"), equalTo("5")));
    }

    @AfterClass
    public void afterClass() {
        getProperties().remove(DEFAULT_LOCALIZATION_ENGINE.getName());
        getProperties().remove(DEFAULT_LOCALE_PROPERTY.getName());
    }

    @Description("Class Description GetSupplier")
    static class GetStepSupplier extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<Object, Object, Object, GetStepSupplier> {

        protected GetStepSupplier() {
            super(o -> o);
        }

        public static GetStepSupplier methodWithoutAnnotation() {
            return new GetStepSupplier();
        }

        @Description("Method Description GetSupplier")
        public static GetStepSupplier methodWithAnnotation() {
            return new GetStepSupplier();
        }

        @Description("Method with Composite Description + {element} GetSupplier")
        public static GetStepSupplier methodWithCompositeAnnotation(@DescriptionFragment("element") String s) {
            return new GetStepSupplier();
        }

        @Override
        protected Function<Object, Object> getEndFunction() {
            return null;
        }

        protected GetStepSupplier from(Object o) {
            return super.from(o);
        }
    }

}
