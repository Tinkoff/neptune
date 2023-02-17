package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.hamcrest.Matcher;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.localization.LocalizationByResourceBundle;
import ru.tinkoff.qa.neptune.core.api.localization.StepLocalization;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static java.time.Duration.ofMillis;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.LocaleUtils.toLocale;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocaleProperty.DEFAULT_LOCALE_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocalizationEngine.DEFAULT_LOCALIZATION_ENGINE;
import static ru.tinkoff.qa.neptune.spring.web.testclient.DescribedConsumerBuilder.describeConsumer;
import static ru.tinkoff.qa.neptune.spring.web.testclient.LogWebTestClientExpectation.logExpectation;
import static ru.tinkoff.qa.neptune.spring.web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient.WebTestClientContext.webTestClient;

public class LogWebTestClientExpectationTest extends BaseTestPreparation {

    private final static Locale RUSSIAN = toLocale("ru_RU");
    private final static Map<String, String> NAME_SPACES = new LinkedHashMap<>() {
        {
            put("a", "b");
            put("c", "d");
        }
    };

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {null, null, "Response status. 200"},
                {RUSSIAN, LocalizationByResourceBundle.class, "Статус ответа. 200"},
        };
    }

    @DataProvider
    public static Object[][] data2() {
        return new Object[][]{
                {null, null, "Response header(s). 'Fake-Header'. Does not exist"},
                {RUSSIAN, LocalizationByResourceBundle.class, "Заголовок(ки) ответа. 'Fake-Header'. Не существует"},
        };
    }

    @DataProvider
    public static Object[][] data3() {
        return new Object[][]{
                {null, null, "Response header(s). 'Test-Header'. Value is {1,2,3}"},
                {RUSSIAN, LocalizationByResourceBundle.class, "Заголовок(ки) ответа. 'Test-Header'. Значение {1,2,3}"},
        };
    }

    @DataProvider
    public static Object[][] data4() {
        return new Object[][]{
                {null, null, "Body. Json path 'some.path'. Arguments '{1,2,3,4}'. Some description"},
                {RUSSIAN, LocalizationByResourceBundle.class, "Тело ответа. Json path 'some.path'. Аргументы '{1,2,3,4}'. Some description"},
        };
    }

    @DataProvider
    public static Object[][] data5() {
        return new Object[][]{
                {null, null, "Body as json-string is equal to or contains same key-value pairs as '{}'"},
                {RUSSIAN, LocalizationByResourceBundle.class, "json-строка тела ответа равна или содержит те же пары ключ-значение, что и '{}'"},
        };
    }

    @DataProvider
    public static Object[][] data6() {
        return new Object[][]{
                {null, null, iterableInOrder("Response status. is 200",
                        "Response status. is 200 OK",
                        "Response status. is informational (1xx)",
                        "Response status. is successful (2xx)",
                        "Response status. is redirection (3xx)",
                        "Response status. is client error (4xx)",
                        "Response status. is server error (5xx)",
                        "Response header(s). 'Some header'. Exists",
                        "Response header(s). 'Some header'. Value meets '\"Some value\"'",
                        "Response header(s). 'Some header'. Values meet 'in following order: \"Some value\", \"Some value2\"'",
                        "Response header(s). 'Some header'. Value matches 'Some'",
                        "Response header(s). 'Some header'. Values match {Some1,Some2}",
                        "Response header(s). Cache-Control is 'CacheControl [no-cache]'",
                        "Response header(s). Content-Length is 100",
                        "Response header(s). Content-Type is 'application/json'",
                        "Response header(s). Content-Type is 'application/json'",
                        "Response header(s). Content-Type is compatible with 'application/json'",
                        "Response header(s). Content-Type is compatible with 'application/json'",
                        "Response header(s). Expires is '100' (Date in Long-number format)",
                        "Response header(s). Last-Modified is '100' (Date in Long-number format)",
                        "Response header(s). Location is 'ABCD'",
                        "Response cookie(s). 'someCookie'. Exists",
                        "Response cookie(s). 'someCookie'. Value is 'someValue'",
                        "Response cookie(s). 'someCookie'. Value meets 'is \"someValue\"'",
                        "Response cookie(s). 'someCookie'. Max age is 'PT0.1S'",
                        "Response cookie(s). 'someCookie'. Max age meets 'is <100L>'",
                        "Response cookie(s). 'someCookie'. Path is 'somePath'",
                        "Response cookie(s). 'someCookie'. Path meets 'is \"somePath\"'",
                        "Response cookie(s). 'someCookie'. Domain is 'someDomain'",
                        "Response cookie(s). 'someCookie'. Domain meets 'is \"someDomain\"'",
                        "Response cookie(s). 'someCookie'. Secure is 'true'",
                        "Response cookie(s). 'someCookie'. Http only is 'true'",
                        "Body as xml-string is equal to or contains same elements as '</>'",
                        "Body. Json path 'some.path'. Arguments '{1,2,3,4}'. Exists",
                        "Body. Json path 'some.path'. Arguments '{1,2,3,4}'. Value is '1'",
                        "Body. Json path 'some.path'. Arguments '{1,2,3,4}'. Value meets 'a value greater than <1>'",
                        "Body. Json path 'some.path'. Arguments '{1,2,3,4}'. Value meets 'a value greater than <1>'. As instance of 'class java.lang.Integer'",
                        "Body. Xpath './/*'. Namespaces '{}'. Arguments '{1,2,true}'. Exists",
                    "Body. Xpath './/*'. Namespaces '{a=b, c=d}'. Arguments '{1,2,true}'. Exists",
                    "Body. Xpath './/*'. Namespaces '{}'. Arguments '{1,2,true}'. Is 'ABCD'",
                    "Body. Xpath './/*'. Namespaces '{a=b, c=d}'. Arguments '{1,2,true}'. Is 'ABCD'",
                    "Body. Xpath './/*'. Namespaces '{}'. Arguments '{1,2,true}'. String meets 'a string containing \"ABCD\"'",
                    "Body. Xpath './/*'. Namespaces '{a=b, c=d}'. Arguments '{1,2,true}'. String meets 'a string containing \"ABCD\"'",
                    "Body. Xpath './/*'. Namespaces '{}'. Arguments '{1,2,true}'. Count of nodes is 2",
                    "Body. Xpath './/*'. Namespaces '{a=b, c=d}'. Arguments '{1,2,true}'. Count of nodes is 2",
                    "Body. Xpath './/*'. Namespaces '{}'. Arguments '{1,2,true}'. Count of nodes meets 'a value greater than <1>'",
                    "Body. Xpath './/*'. Namespaces '{a=b, c=d}'. Arguments '{1,2,true}'. Count of nodes meets 'a value greater than <1>'")},
            {RUSSIAN, LocalizationByResourceBundle.class, iterableInOrder("Статус ответа. is 200",
                "Статус ответа. 200 OK",
                "Статус ответа. Информационный (1xx)",
                "Статус ответа. Успешный (2xx)",
                "Статус ответа. Редирект (3xx)",
                "Статус ответа. Ошибка клиента (4xx)",
                "Статус ответа. Ошибка сервера (5xx)",
                "Заголовок(ки) ответа. 'Some header'. Существует",
                "Заголовок(ки) ответа. 'Some header'. Значение соответствует '\"Some value\"'",
                "Заголовок(ки) ответа. 'Some header'. Значения соответствуют 'в следующем порядке: \"Some value\", \"Some value2\"'",
                "Заголовок(ки) ответа. 'Some header'. Значение соответствует 'Some'",
                "Заголовок(ки) ответа. 'Some header'. Значения соответствуют {Some1,Some2}",
                "Заголовок(ки) ответа. Cache-Control 'CacheControl [no-cache]'",
                "Заголовок(ки) ответа. Content-Length 100",
                "Заголовок(ки) ответа. Content-Type 'application/json'",
                "Заголовок(ки) ответа. Content-Type 'application/json'",
                "Заголовок(ки) ответа. Content-Type совместим с 'application/json'",
                "Заголовок(ки) ответа. Content-Type совместим с 'application/json'",
                "Заголовок(ки) ответа. Expires '100' (дата в формате Long-числа)",
                "Заголовок(ки) ответа. Last-Modified '100' (дата в формате Long-числа)",
                "Заголовок(ки) ответа. Location 'ABCD'",
                "Куки ответа.. 'someCookie'. Существует",
                "Куки ответа.. 'someCookie'. Значение 'someValue'",
                "Куки ответа.. 'someCookie'. Значение соответствует 'is \"someValue\"'",
                "Куки ответа.. 'someCookie'. Срок 'PT0.1S'",
                "Куки ответа.. 'someCookie'. Срок соответствует 'is <100L>'",
                "Куки ответа.. 'someCookie'. Path=='somePath'",
                "Куки ответа.. 'someCookie'. Path соответствует 'is \"somePath\"'",
                "Куки ответа.. 'someCookie'. Домен 'someDomain'",
                "Куки ответа.. 'someCookie'. Домен соответствует 'is \"someDomain\"'",
                "Куки ответа.. 'someCookie'. Secure=='true'",
                "Куки ответа.. 'someCookie'. Http only=='true'",
                "xml-string тела ответа равна или содержит те же элементы, что и '</>'",
                "Тело ответа. Json path 'some.path'. Аргументы '{1,2,3,4}'. Существует",
                "Тело ответа. Json path 'some.path'. Аргументы '{1,2,3,4}'. Значение '1'",
                "Тело ответа. Json path 'some.path'. Аргументы '{1,2,3,4}'. Значение соответствует 'a value greater than <1>'",
                "Тело ответа. Json path 'some.path'. Аргументы '{1,2,3,4}'. Значение соответствует 'a value greater than <1>'. Значение как объект 'class java.lang.Integer'",
                "Тело ответа. Xpath './/*'. Неймспейсы '{}'. Аргументы '{1,2,true}'. Существует",
                "Тело ответа. Xpath './/*'. Неймспейсы '{a=b, c=d}'. Аргументы '{1,2,true}'. Существует",
                "Тело ответа. Xpath './/*'. Неймспейсы '{}'. Аргументы '{1,2,true}'. 'ABCD'",
                "Тело ответа. Xpath './/*'. Неймспейсы '{a=b, c=d}'. Аргументы '{1,2,true}'. 'ABCD'",
                "Тело ответа. Xpath './/*'. Неймспейсы '{}'. Аргументы '{1,2,true}'. Строка, соответствующая 'a string containing \"ABCD\"'",
                "Тело ответа. Xpath './/*'. Неймспейсы '{a=b, c=d}'. Аргументы '{1,2,true}'. Строка, соответствующая 'a string containing \"ABCD\"'",
                "Тело ответа. Xpath './/*'. Неймспейсы '{}'. Аргументы '{1,2,true}'. Число нод 2",
                "Тело ответа. Xpath './/*'. Неймспейсы '{a=b, c=d}'. Аргументы '{1,2,true}'. Число нод 2",
                "Тело ответа. Xpath './/*'. Неймспейсы '{}'. Аргументы '{1,2,true}'. Число нод соответствует 'a value greater than <1>'",
                "Тело ответа. Xpath './/*'. Неймспейсы '{a=b, c=d}'. Аргументы '{1,2,true}'. Число нод соответствует 'a value greater than <1>'")},
        };
    }

    @Test(dataProvider = "data")
    public void test(Locale l,
                     Class<StepLocalization> localizationClass,
                     String expected) {

        DEFAULT_LOCALE_PROPERTY.accept(l);
        DEFAULT_LOCALIZATION_ENGINE.accept(localizationClass);

        var send = send(client, WebTestClient::get)
                .expectStatus(StatusAssertions::isOk);

        webTestClient(send);
        var expectation = send.assertions.getFirst();

        expectation.verify(responseSpec);
        assertThat(logExpectation(expectation).toString(), is(expected));
    }

    @Test(dataProvider = "data2")
    public void test2(Locale l,
                      Class<StepLocalization> localizationClass,
                      String expected) {

        DEFAULT_LOCALE_PROPERTY.accept(l);
        DEFAULT_LOCALIZATION_ENGINE.accept(localizationClass);

        var send = send(client, WebTestClient::get)
                .expectHeader(headerAssertions -> headerAssertions.doesNotExist("Fake-Header"));

        webTestClient(send);
        var expectation = send.assertions.getFirst();
        expectation.verify(responseSpec);
        assertThat(logExpectation(expectation).toString(), is(expected));
    }

    @Test(dataProvider = "data3")
    public void test3(Locale l,
                      Class<StepLocalization> localizationClass,
                      String expected) {

        DEFAULT_LOCALE_PROPERTY.accept(l);
        DEFAULT_LOCALIZATION_ENGINE.accept(localizationClass);

        var send = send(client, WebTestClient::get)
                .expectHeader(headerAssertions -> headerAssertions.valueEquals("Test-Header", "1", "2", "3"));

        webTestClient(send);
        var expectation = send.assertions.getFirst();

        expectation.verify(responseSpec);
        assertThat(logExpectation(expectation).toString(), is(expected));
    }

    @Test(dataProvider = "data4")
    public void test4(Locale l,
                      Class<StepLocalization> localizationClass,
                      String expected) {

        DEFAULT_LOCALE_PROPERTY.accept(l);
        DEFAULT_LOCALIZATION_ENGINE.accept(localizationClass);

        var send = send(client, WebTestClient::get).expectBodyJsonPath("some.path",
                jsonPathAssertions -> jsonPathAssertions.value(describeConsumer("Some description")
                        .of(Integer.class)
                        .consume(integer -> {
                        })
                        .get()),
                "1", "2", "3", "4");

        try {
            webTestClient(send);
        } catch (AssertionError ignored) {

        }

        var expectation = send.assertions.getFirst();

        expectation.verify(responseSpec);
        assertThat(logExpectation(expectation).toString(), is(expected));
    }

    @Test(dataProvider = "data5")
    public void test5(Locale l,
                      Class<StepLocalization> localizationClass,
                      String expected) {


        DEFAULT_LOCALE_PROPERTY.accept(l);
        DEFAULT_LOCALIZATION_ENGINE.accept(localizationClass);

        var send = send(client, WebTestClient::get)
                .expectBodyJson("{}");

        webTestClient(send);
        var expectation = send.assertions.getFirst();

        expectation.verify(responseSpec);
        assertThat(logExpectation(expectation).toString(), is(expected));
    }

    @Test(dataProvider = "data6")
    public void otherLocalizationTest(Locale locale,
                                      Class<? extends StepLocalization> localizationClass,
                                      Matcher<Iterable<String>> matcher) {

        DEFAULT_LOCALE_PROPERTY.accept(locale);
        DEFAULT_LOCALIZATION_ENGINE.accept(localizationClass);

        var send = send(client, WebTestClient::get)
                .expectStatus(200)
                .expectStatus(HttpStatus.OK)
                .expectStatusIs1xxInformational()
                .expectStatusIs2xxSuccessful()
                .expectStatusIs3xxRedirection()
                .expectStatusIs4xxClientError()
                .expectStatusIsis5xxServerError()
                .expectHeader("Some header")
                .expectHeaderValue("Some header", "Some value")
                .expectHeaderValues("Some header", "Some value", "Some value2")
                .expectHeaderValueMatches("Some header", "Some")
                .expectHeaderValuesMatch("Some header", "Some1", "Some2")
                .expectCacheControl(CacheControl.noCache())
                .expectContentLength(100)
                .expectContentType(MediaType.APPLICATION_JSON)
                .expectContentType((MediaType.APPLICATION_JSON.toString()))
                .expectContentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectContentTypeCompatibleWith((MediaType.APPLICATION_JSON.toString()))
                .expectExpires(100)
                .expectLastModified(100)
                .expectLocation("ABCD")
                .expectCookie("someCookie")
                .expectCookieValue("someCookie", "someValue")
                .expectCookieValue("someCookie", is("someValue"))
                .expectCookieMaxAge("someCookie", ofMillis(100))
                .expectCookieMaxAge("someCookie", is(100L))
                .expectCookiePath("someCookie", "somePath")
                .expectCookiePath("someCookie", is("somePath"))
                .expectCookieDomain("someCookie", "someDomain")
                .expectCookieDomain("someCookie", is("someDomain"))
                .expectCookieSecure("someCookie", true)
                .expectCookieHttpOnly("someCookie", true)
                .expectBodyXml("</>")
                .expectBodyJsonPath("some.path", "1", "2", "3", "4")
                .expectBodyJsonPathEquals("some.path", 1, "1", "2", "3", "4")
                .expectBodyJsonPathMatches("some.path", greaterThan(1), "1", "2", "3", "4")
                .expectBodyJsonPathMatches("some.path", Integer.class, greaterThan(1), "1", "2", "3", "4")
                .expectBodyXpath(".//*", 1, 2, true)
                .expectBodyXpath(".//*", NAME_SPACES, 1, 2, true)
                .expectBodyXpathEquals(".//*", "ABCD", 1, 2, true)
                .expectBodyXpathEquals(".//*", "ABCD", NAME_SPACES, 1, 2, true)
                .expectBodyXpathMatches(".//*", containsString("ABCD"), 1, 2, true)
                .expectBodyXpathMatches(".//*", containsString("ABCD"), NAME_SPACES, 1, 2, true)
                .expectBodyXpathNodeCount(".//*", 2, 1, 2, true)
                .expectBodyXpathNodeCount(".//*", 2, NAME_SPACES, 1, 2, true)
                .expectBodyXpathNodeCount(".//*", greaterThan(1), 1, 2, true)
                .expectBodyXpathNodeCount(".//*", greaterThan(1), NAME_SPACES, 1, 2, true);

        try {
            webTestClient(send);
        } catch (AssertionError ignored) {
        }

        assertThat(
                send.assertions.stream().map(Expectation::toString).collect(toList()),
                matcher
        );
    }

    @AfterMethod
    public void afterMethod() {
        DEFAULT_LOCALE_PROPERTY.accept(null);
        DEFAULT_LOCALIZATION_ENGINE.accept(null);
    }
}
