package ru.tinkoff.qa.neptune.core.api.localization;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Locale;

import static java.util.List.of;
import static org.apache.commons.lang3.LocaleUtils.toLocale;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocaleProperty.DEFAULT_LOCALE_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocalizationEngine.DEFAULT_LOCALIZATION_ENGINE;

@SuppressWarnings("unchecked")
public class MatcherLocalizationTest {

    private final static Locale RUSSIAN = toLocale("ru_RU");

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {null, null, "an empty array"},
                {RUSSIAN, LocalizationByResourceBundle.class, "Пустой массив"},
        };
    }

    @DataProvider
    public static Object[][] data2() {
        return new Object[][]{
                {null, null, "a CharSequence with length <3>"},
                {RUSSIAN, LocalizationByResourceBundle.class, "длина '3'"},
        };
    }

    @DataProvider
    public static Object[][] data3() {
        return new Object[][]{
                {null, null, "iterable containing [<1>, <2>, <true>, \"Hello world\"]"},
                {RUSSIAN, LocalizationByResourceBundle.class, "состоит из элементов, которые соответствуют: {1,2,true,Hello world}. В строгом порядке"},
        };
    }

    @DataProvider
    public static Object[][] data4() {
        return new Object[][]{
                {null, null, "[a value greater than <1>, a value less than <100>] "},
                {RUSSIAN, LocalizationByResourceBundle.class,
                        "Массив состоит из элементов, которые соответствуют: больше чем 1, меньше чем 100. В строгом порядке"},
        };
    }

    @DataProvider
    public static Object[][] data5() {
        return new Object[][]{
                {null, null, "(a value greater than <100> or a value less than <200>)"},
                {RUSSIAN, LocalizationByResourceBundle.class,
                        "больше чем 100 либо меньше чем 200"},
        };
    }

    @DataProvider
    public static Object[][] data6() {
        return new Object[][]{
                {null, null, "(a value greater than <100> and a value less than <200>)"},
                {RUSSIAN, LocalizationByResourceBundle.class,
                        "больше чем 100 и меньше чем 200"},
        };
    }

    @DataProvider
    public static Object[][] data7() {
        return new Object[][]{
                {null, null, "((a value greater than <100> and a value less than <200>) or <300>)"},
                {RUSSIAN, LocalizationByResourceBundle.class,
                        "(больше чем 100 и меньше чем 200) либо равно '300'"},
        };
    }

    @Test(dataProvider = "data")
    public void testOfAMatcherWithoutSignature(Locale l,
                                               Class<StepLocalization> localizationClass,
                                               String expected) {
        DEFAULT_LOCALE_PROPERTY.accept(l);
        DEFAULT_LOCALIZATION_ENGINE.accept(localizationClass);

        assertThat(emptyArray().toString(), is(expected));
    }

    @Test(dataProvider = "data2")
    public void testOfAMatcherWithSimpleSignature(Locale l,
                                                  Class<StepLocalization> localizationClass,
                                                  String expected) {
        DEFAULT_LOCALE_PROPERTY.accept(l);
        DEFAULT_LOCALIZATION_ENGINE.accept(localizationClass);

        assertThat(hasLength(3).toString(), is(expected));
    }

    @Test(dataProvider = "data3")
    public void testOfAMatcherWithVarArgSignature(Locale l,
                                                  Class<StepLocalization> localizationClass,
                                                  String expected) {
        DEFAULT_LOCALE_PROPERTY.accept(l);
        DEFAULT_LOCALIZATION_ENGINE.accept(localizationClass);

        assertThat(contains(1, 2, true, "Hello world").toString(), is(expected));
    }

    @Test(dataProvider = "data4")
    public void testOfAMatcherWithCollectionSignature(Locale l,
                                                      Class<StepLocalization> localizationClass,
                                                      String expected) {
        DEFAULT_LOCALE_PROPERTY.accept(l);
        DEFAULT_LOCALIZATION_ENGINE.accept(localizationClass);

        assertThat(arrayContaining(of(greaterThan(1), lessThan(100))).toString(),
                is(expected));
    }

    @Test(dataProvider = "data5")
    public void testOfCombinableMatcherOr(Locale l,
                                          Class<StepLocalization> localizationClass,
                                          String expected) {
        DEFAULT_LOCALE_PROPERTY.accept(l);
        DEFAULT_LOCALIZATION_ENGINE.accept(localizationClass);

        assertThat(either(greaterThan(100)).or(lessThan(200)).toString(),
                is(expected));
    }

    @Test(dataProvider = "data6")
    public void testOfCombinableMatcherBoth(Locale l,
                                            Class<StepLocalization> localizationClass,
                                            String expected) {
        DEFAULT_LOCALE_PROPERTY.accept(l);
        DEFAULT_LOCALIZATION_ENGINE.accept(localizationClass);

        assertThat(both(greaterThan(100)).and(lessThan(200)).toString(),
                is(expected));
    }

    @Test(dataProvider = "data7")
    public void testOfCombinableMatcherLong(Locale l,
                                            Class<StepLocalization> localizationClass,
                                            String expected) {
        DEFAULT_LOCALE_PROPERTY.accept(l);
        DEFAULT_LOCALIZATION_ENGINE.accept(localizationClass);

        assertThat(both(greaterThan(100)).and(lessThan(200)).or(equalTo(300)).toString(),
                is(expected));
    }

    @AfterMethod
    public void afterMethod() {
        DEFAULT_LOCALE_PROPERTY.accept(null);
        DEFAULT_LOCALIZATION_ENGINE.accept(null);
    }
}
