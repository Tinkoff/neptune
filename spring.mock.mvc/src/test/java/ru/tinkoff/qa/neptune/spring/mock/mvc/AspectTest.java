package ru.tinkoff.qa.neptune.spring.mock.mvc;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.localization.LocalizationByResourceBundle;
import ru.tinkoff.qa.neptune.core.api.localization.StepLocalization;

import java.util.Locale;

import static org.apache.commons.lang3.LocaleUtils.toLocale;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocaleProperty.DEFAULT_LOCALE_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocalizationEngine.DEFAULT_LOCALIZATION_ENGINE;

public class AspectTest {

    private final static Locale RUSSIAN = toLocale("ru_RU");

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {null, null, "Status. 200"},
                {RUSSIAN, LocalizationByResourceBundle.class, "Статус. 200"}
        };
    }

    @DataProvider
    public static Object[][] data2() {
        return new Object[][]{
                {null, null, "redirected Url 'https://google.com'"},
                {RUSSIAN, LocalizationByResourceBundle.class, "Url редиректа 'https://google.com'"}
        };
    }

    @DataProvider
    public static Object[][] data3() {
        return new Object[][]{
                {null, null, "redirected Url meets pattern '/orders/{orderId}/items/{itemId}'. Arguments {1,2}"},
                {RUSSIAN, LocalizationByResourceBundle.class, "Url редиректа соответствует шаблону '/orders/{orderId}/items/{itemId}'. Аргументы {1,2}"}
        };
    }


    @DataProvider
    public static Object[][] data4() {
        return new Object[][]{
                {null, null, "Handler(s). Method call is 'controller type 'class ru.tinkoff.qa.neptune.spring.mock.mvc.SimpleController'; controller method 'handle'; args {{1,2,true}}'"},
                {RUSSIAN, LocalizationByResourceBundle.class, "Обработчик (обработчики). Вызов метода 'controller type 'class ru.tinkoff.qa.neptune.spring.mock.mvc.SimpleController'; controller method 'handle'; args {{1,2,true}}'"}
        };
    }

    @Test(description = "Test of string representation of ResultMatcher. Method without signature",
            dataProvider = "data")
    public void test(Locale l,
                     Class<StepLocalization> localizationClass,
                     String expected) {

        DEFAULT_LOCALE_PROPERTY.accept(l);
        DEFAULT_LOCALIZATION_ENGINE.accept(localizationClass);

        assertThat(status().isOk().toString(), is(expected));
    }

    @Test(description = "Test of string representation of ResultMatcher. Method with one-parameter signature",
            dataProvider = "data2")
    public void test2(Locale l,
                     Class<StepLocalization> localizationClass,
                     String expected) {

        DEFAULT_LOCALE_PROPERTY.accept(l);
        DEFAULT_LOCALIZATION_ENGINE.accept(localizationClass);

        assertThat(redirectedUrl("https://google.com").toString(), is(expected));
    }

    @Test(description = "Test of string representation of ResultMatcher. Method with vararg-parameter signature",
            dataProvider = "data3")
    public void test3(Locale l,
                      Class<StepLocalization> localizationClass,
                      String expected) {

        DEFAULT_LOCALE_PROPERTY.accept(l);
        DEFAULT_LOCALIZATION_ENGINE.accept(localizationClass);

        assertThat(redirectedUrlTemplate("/orders/{orderId}/items/{itemId}", 1, 2)
                .toString(), is(expected));
    }

    @Test(description = "Test of string representation of ResultMatcher. Method call info",
            dataProvider = "data4")
    public void test4(Locale l,
                      Class<StepLocalization> localizationClass,
                      String expected) {

        DEFAULT_LOCALE_PROPERTY.accept(l);
        DEFAULT_LOCALIZATION_ENGINE.accept(localizationClass);

        assertThat(handler().methodCall(on(SimpleController.class).handle(1, 2, true))
                .toString(), is(expected));
    }

    @AfterMethod
    public void afterMethod() {
        DEFAULT_LOCALE_PROPERTY.accept(null);
        DEFAULT_LOCALIZATION_ENGINE.accept(null);
    }
}
