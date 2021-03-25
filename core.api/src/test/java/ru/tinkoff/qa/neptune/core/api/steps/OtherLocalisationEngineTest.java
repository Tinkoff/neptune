package ru.tinkoff.qa.neptune.core.api.steps;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.function.Function;

import static java.lang.System.getProperties;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocalizationEngine.DEFAULT_LOCALIZATION_ENGINE;
import static ru.tinkoff.qa.neptune.core.api.steps.OtherLocalisationEngineTest.GetStepSupplier.*;

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
    }

    @Description("Class Description GetSupplier")
    static class GetStepSupplier extends SequentialGetStepSupplier<Object, Object, Object, Object, GetStepSupplier> {

        protected GetStepSupplier() {
            super();
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
    }

}
