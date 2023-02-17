package ru.tinkoff.qa.neptune.spring.mock.mvc;

import org.hamcrest.Matcher;
import org.springframework.test.web.servlet.result.RequestResultMatchers;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.localization.LocalizationByResourceBundle;
import ru.tinkoff.qa.neptune.core.api.localization.StepLocalization;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static java.util.List.of;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.LocaleUtils.toLocale;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocaleProperty.DEFAULT_LOCALE_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocalizationEngine.DEFAULT_LOCALIZATION_ENGINE;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.properties.SpringMockMvcDefaultResponseBodyTransformer.SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER;

public class AspectTest {

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
                {null, null, iterableInOrder(
                        "Forwarded URL. 'http://someforward/url'",
                        "Forwarded URL. meets pattern 'someforward'. Arguments {1,2,3}",
                        "Forwarded URL. meets pattern 'someforward'",
                        "Redirected URL. 'http://someredirect/url'",
                        "Redirected URL. meets pattern 'someredirect'. Arguments {1,2,3}",
                        "Redirected URL. meets pattern 'someredirect'",
                        "Jsonpath '$.store.book[*].author'. Arguments {1,2,3}. Exists",
                        "Jsonpath '$.store.book[*].author'. Arguments {}. Value is 'ABC'",
                        "Jsonpath '$.store.book[*].author'. Arguments {1,2,3}. Value meets 'is \"ABC\"'",
                        "Jsonpath '$.store.book[*].author'. Arguments {}. Value meets 'is \"ABC\"' as 'class java.lang.String'",
                        "Xpath './/store/book/author'. Namespaces '{}'. Arguments {123}. Exists",
                        "Xpath './/store/book/author'. Namespaces '{a=b, c=d}'. Arguments {123}. Exists",
                        "Xpath './/store/book/author'. Namespaces '{a=b, c=d}'. Arguments {1,2,3}. Node that meets 'an XML document with XPath .//* '",
                        "Xpath './/store/book/author'. Namespaces '{}'. Arguments {1,2,3}. Node that meets 'an XML document with XPath .//* '",
                        "Xpath './/store/book/author'. Namespaces '{a=b, c=d}'. Arguments {}. Count of nodes is 2",
                        "Xpath './/store/book/author'. Namespaces '{}'. Arguments {1,2,3}. Count of nodes is 2",
                        "Status. is 200",
                        "Status. Meets 'a value greater than <100>'",
                        "Status. is informational (1xx)",
                        "Status. is successful (2xx)",
                        "Status. is redirection (3xx)",
                        "Status. is client error (4xx)",
                        "Status. is server error (5xx)",
                        "Status. Reason is 'Some reason'",
                        "Status. Reason matches '\"Some reason\"'",
                        "Request. Asynchronous processing not started",
                        "View. Name is 'someName'",
                        "View. Name meets 'is \"expectViewName\"'",
                        "Model. Attribute 'attr'. Value is '123'",
                        "Model. Attribute 'attr'. Value meets 'a value greater than <100>'",
                        "Model. Attribute(s) {attr1,attr2,attr3} exist(s)",
                        "Flash attribute(s). 'attr'. Value is '123'",
                        "Flash attribute(s). 'attr'. Value meets 'a value greater than <100>'",
                        "Flash attribute(s). Attribute(s) {attr1,attr2,attr3} exist(s)",
                        "Flash attribute(s). Count of attributes is 10",
                        "Handler(s). Handler type is class ru.tinkoff.qa.neptune.spring.mock.mvc.SimpleController",
                        "Handler(s). Method is public org.springframework.http.ResponseEntity ru.tinkoff.qa.neptune.spring.mock.mvc.SimpleController.handle(java.lang.Object[])",
                        "Handler(s). Method name is 'handle'",
                        "Handler(s). Method name meets 'is \"handle\"'",
                        "Handler(s). Method call is 'controller type 'class ru.tinkoff.qa.neptune.spring.mock.mvc.SimpleController'; controller method 'handle'; args {{1,2,true}}'",
                        "Header(s). 'someHeader'. Exists",
                        "Header(s). 'someHeader'. Value is 'someValue1'",
                        "Header(s). 'someHeader'. Value meets 'is \"someValue1\"'",
                        "Header(s). 'someHeader'. Value is {someValue1,someValue2}",
                        "Header(s). 'someHeader'. Value meets '(a collection containing \"someValue1\" and a collection containing \"someValue2\")'",
                        "Content. Media type is application/json",
                        "Content. Media type is application/json",
                        "Content. Encoding is UTF-8",
                        "Content. Encoding is UTF-8",
                        "Content. As byte array is {65,66,67,68}",
                        "Content. Is json '{}'",
                        "Content. Is json '{}'. Check strictly='false'",
                        "Content. Is xml '</>'",
                        "Content. Is the string '{\"stringValue\":\"abc\",\"arrayValue1\":[\"1\",\"2\",\"3\"],\"arrayValue2\":null}'",
                        "Cookie. 'someCookieName'. Exists",
                        "Cookie. 'someCookieName'. Value is 'someValue'",
                        "Cookie. 'someCookieName'. Value meets 'is \"someValue\"'",
                        "Cookie. 'someCookieName'. Max age is '2'",
                        "Cookie. 'someCookieName'. Max age meets 'a value greater than <1>'",
                        "Cookie. 'someCookieName'. Path is '/some/path'",
                        "Cookie. 'someCookieName'. Path meets 'is \"/some/path\"'",
                        "Cookie. 'someCookieName'. Domain is 'some.domain'",
                        "Cookie. 'someCookieName'. Domain meets 'is \"some.domain\"'",
                        "Cookie. 'someCookieName'. Comment is 'some comment'",
                        "Cookie. 'someCookieName'. Comment meets 'is \"some comment\"'",
                        "Cookie. 'someCookieName'. Version is '2'",
                        "Cookie. 'someCookieName'. Version meets 'a value greater than <1>'",
                        "Cookie. 'someCookieName'. Secure is 'true'",
                        "Cookie. 'someCookieName'. Http only is 'true'",
                        "No resolved exception",
                        "Resolved exception",
                        "Resolved exception matches 'is object of class 'class java.lang.RuntimeException''",
                        "Resolved exception matches 'is object of class 'class java.lang.RuntimeException', throwable has message '\"Some message\"''",
                        "Resolved exception matches 'is object of class 'class java.lang.RuntimeException', throwable has message 'a string containing \"Some\", a string containing \"message\"''"
                )},
                {RUSSIAN, LocalizationByResourceBundle.class,
                    iterableInOrder("URL. Форвард. 'http://someforward/url'",
                        "URL. Форвард. соответствует шаблону 'someforward'. Аргументы {1,2,3}",
                        "URL. Форвард. соответствует шаблону 'someforward'",
                        "URL. Редирект. 'http://someredirect/url'",
                        "URL. Редирект. соответствует шаблону 'someredirect'. Аргументы {1,2,3}",
                        "URL. Редирект. соответствует шаблону 'someredirect'",
                        "Jsonpath '$.store.book[*].author'. Аргументы {1,2,3}. Существует",
                        "Jsonpath '$.store.book[*].author'. Аргументы {}. Значение 'ABC'",
                        "Jsonpath '$.store.book[*].author'. Аргументы {1,2,3}. Значение соответствует 'is \"ABC\"'",
                        "Jsonpath '$.store.book[*].author'. Аргументы {}. Значение соответствует 'is \"ABC\"' как 'class java.lang.String'",
                        "Xpath './/store/book/author'. Неймспейсы '{}'. Аргументы {123}. Существует",
                        "Xpath './/store/book/author'. Неймспейсы '{a=b, c=d}'. Аргументы {123}. Существует",
                        "Xpath './/store/book/author'. Неймспейсы '{a=b, c=d}'. Аргументы {1,2,3}. Нода соответствует 'an XML document with XPath .//* '",
                        "Xpath './/store/book/author'. Неймспейсы '{}'. Аргументы {1,2,3}. Нода соответствует 'an XML document with XPath .//* '",
                        "Xpath './/store/book/author'. Неймспейсы '{a=b, c=d}'. Аргументы {}. Число нод 2",
                        "Xpath './/store/book/author'. Неймспейсы '{}'. Аргументы {1,2,3}. Число нод 2",
                        "Статус. 200",
                        "Статус. Соответствует 'a value greater than <100>'",
                        "Статус. Информационный (1xx)",
                        "Статус. Успешный (2xx)",
                        "Статус. Редирект (3xx)",
                        "Статус. Ошибка клиента (4xx)",
                        "Статус. Ошибка сервера (5xx)",
                        "Статус. Причина 'Some reason'",
                        "Статус. Причина соответствует '\"Some reason\"'",
                        "Запрос. Асинхронная обработка не стартовала",
                        "View. Имя 'someName'",
                        "View. Имя соответствует 'is \"expectViewName\"'",
                        "Model. Атрибут 'attr'. Значение '123'",
                        "Model. Атрибут 'attr'. Значение соответствует 'a value greater than <100>'",
                        "Model. Атрибут(ы) {attr1,attr2,attr3} существует(ют)",
                        "Flash-атрибут(ы). 'attr'. Значение '123'",
                        "Flash-атрибут(ы). 'attr'. Значение соответствует 'a value greater than <100>'",
                        "Flash-атрибут(ы). Есть атрибут(ы) {attr1,attr2,attr3}",
                        "Flash-атрибут(ы). Количество атрибутов 10",
                        "Обработчик (обработчики). Тип обработчика class ru.tinkoff.qa.neptune.spring.mock.mvc.SimpleController",
                        "Обработчик (обработчики). Метод public org.springframework.http.ResponseEntity ru.tinkoff.qa.neptune.spring.mock.mvc.SimpleController.handle(java.lang.Object[])",
                        "Обработчик (обработчики). Имя метода 'handle'",
                        "Обработчик (обработчики). Имя метода соответствует 'is \"handle\"'",
                        "Обработчик (обработчики). Вызов метода 'controller type 'class ru.tinkoff.qa.neptune.spring.mock.mvc.SimpleController'; controller method 'handle'; args {{1,2,true}}'",
                        "Заголовок (заголовки). 'someHeader'. Существует",
                        "Заголовок (заголовки). 'someHeader'. Значение 'someValue1'",
                        "Заголовок (заголовки). 'someHeader'. Значение соответствует 'is \"someValue1\"'",
                        "Заголовок (заголовки). 'someHeader'. Значение {someValue1,someValue2}",
                        "Заголовок (заголовки). 'someHeader'. Значение соответствует '(a collection containing \"someValue1\" and a collection containing \"someValue2\")'",
                        "Контент. Тип контента application/json",
                        "Контент. Тип контента application/json",
                        "Контент. Кодировка UTF-8",
                        "Контент. Кодировка UTF-8",
                        "Контент. Соответствует массиву байтов {65,66,67,68}",
                        "Контент. Json '{}'",
                        "Контент. Json '{}'. Строгая проверка='false'",
                        "Контент. xml '</>'",
                        "Контент. Строка '{\"stringValue\":\"abc\",\"arrayValue1\":[\"1\",\"2\",\"3\"],\"arrayValue2\":null}'",
                        "Куки. 'someCookieName'. Существует",
                        "Куки. 'someCookieName'. Значение 'someValue'",
                        "Куки. 'someCookieName'. Значение соответствует 'is \"someValue\"'",
                        "Куки. 'someCookieName'. Срок '2'",
                        "Куки. 'someCookieName'. Срок соответствует 'a value greater than <1>'",
                        "Куки. 'someCookieName'. Path=='/some/path'",
                        "Куки. 'someCookieName'. Path соответствует 'is \"/some/path\"'",
                        "Куки. 'someCookieName'. Домен 'some.domain'",
                        "Куки. 'someCookieName'. Домен соответствует 'is \"some.domain\"'",
                        "Куки. 'someCookieName'. Коммент 'some comment'",
                        "Куки. 'someCookieName'. Коммент соответствует 'is \"some comment\"'",
                        "Куки. 'someCookieName'. Версия '2'",
                        "Куки. 'someCookieName'. Версия соответствует 'a value greater than <1>'",
                        "Куки. 'someCookieName'. Secure=='true'",
                        "Куки. 'someCookieName'. Http only=='true'",
                        "Нет исключения",
                        "Исключение (любое)",
                        "Исключение соответствует 'объект класса 'class java.lang.RuntimeException''",
                        "Исключение соответствует 'объект класса 'class java.lang.RuntimeException', сообщение '\"Some message\"''",
                        "Исключение соответствует 'объект класса 'class java.lang.RuntimeException', сообщение 'a string containing \"Some\", a string containing \"message\"''")}
        };
    }

    @Test(dataProvider = "data")
    public void test(Locale locale,
                     Class<? extends StepLocalization> localizationClass,
                     Matcher<Iterable<String>> matcher) throws Exception {
        DEFAULT_LOCALE_PROPERTY.accept(locale);
        DEFAULT_LOCALIZATION_ENGINE.accept(localizationClass);
        SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.accept(TestDataTransformer.class);

        var responseSupplier = response(standaloneSetup().build(), post("/api/request/1"))
                .expectForward("http://someforward/url")
                .expectForwardTemplate("someforward", 1, 2, 3)
                .expectForwardPattern("someforward")
                .expectRedirect("http://someredirect/url")
                .expectRedirectTemplate("someredirect", 1, 2, 3)
                .expectRedirectPattern("someredirect")
                .expectJsonPath("$.store.book[*].author", 1, 2, 3)
                .expectJsonPathValue("$.store.book[*].author", "ABC")
                .expectJsonPathValue("$.store.book[*].author", is("ABC"), 1, 2, 3)
                .expectJsonPath("$.store.book[*].author", is("ABC"), String.class)
                .expectXPath(".//store/book/author", 123)
                .expectXPath(".//store/book/author", NAME_SPACES, 123)
                .expectXPathNode(".//store/book/author",
                        NAME_SPACES,
                        hasXPath(".//*"),
                        1, 2, 3)
                .expectXPathNode(".//store/book/author",
                        hasXPath(".//*"),
                        1, 2, 3)
                .expectXPathNodeCount(".//store/book/author",
                        NAME_SPACES,
                        2)
                .expectXPathNodeCount(".//store/book/author",
                        2,
                        1, 2, 3)
                .expectStatus(200)
                .expectStatus(greaterThan(100))
                .expectStatusIs1xxInformational()
                .expectStatusIs2xxSuccessful()
                .expectStatusIs3xxRedirection()
                .expectStatusIs4xxClientError()
                .expectStatusIs5xxServerError()
                .expectStatusReason("Some reason")
                .expectStatusReason(equalTo("Some reason"))
                .expectRequest(RequestResultMatchers::asyncNotStarted)
                .expectViewName("someName")
                .expectViewName(is("expectViewName"))
                .expectModelAttribute("attr", 123)
                .expectModelAttribute("attr", greaterThan(100))
                .expectModelAttributes("attr1", "attr2", "attr3")

                .expectFlashAttribute("attr", 123)
                .expectFlashAttribute("attr", greaterThan(100))
                .expectFlashAttributes("attr1", "attr2", "attr3")
                .expectFlashAttributeCount(10)
                .expectHandlerType(SimpleController.class)
                .expectHandlerMethod(SimpleController.class.getDeclaredMethod("handle", Object[].class))
                .expectHandlerMethod("handle")
                .expectHandlerMethod(is("handle"))
                .expectHandlerCall(SimpleController.class, sh -> sh.handle(1, 2, true))
                .expectHeader("someHeader")
                .expectHeaderValue("someHeader", "someValue1")
                .expectHeaderValue("someHeader", is("someValue1"))
                .expectHeaderValues("someHeader", "someValue1", "someValue2")
                .expectHeaderValues("someHeader", hasItems("someValue1", "someValue2"))
                .expectContentType(APPLICATION_JSON.toString())
                .expectContentType(APPLICATION_JSON)
                .expectContentEncoding("UTF-8")
                .expectContentEncoding(StandardCharsets.UTF_8)
                .expectContentBytes("ABCD".getBytes())
                .expectJsonContent("{}")
                .expectJsonContent("{}", false)
                .expectXmlContent("</>")
                .expectContent(new BoundedDto().setStringValue("abc").setArrayValue1(of("1", "2", "3")))
                .expectCookie("someCookieName")
                .expectCookieValue("someCookieName", "someValue")
                .expectCookieValue("someCookieName", is("someValue"))
                .expectCookieMaxAge("someCookieName", 2)
                .expectCookieMaxAge("someCookieName", greaterThan(1))
                .expectCookiePath("someCookieName", "/some/path")
                .expectCookiePath("someCookieName", is("/some/path"))
                .expectCookieDomain("someCookieName", "some.domain")
                .expectCookieDomain("someCookieName", is("some.domain"))
                .expectCookieComment("someCookieName", "some comment")
                .expectCookieComment("someCookieName", is("some comment"))
                .expectCookieVersion("someCookieName", 2)
                .expectCookieVersion("someCookieName", greaterThan(1))
                .expectCookieSecure("someCookieName", true)
                .expectCookieHttpOnly("someCookieName", true)
                .expectNoResolvedException()
                .expectResolvedException()
                .expectResolvedException(RuntimeException.class)
                .expectResolvedException(RuntimeException.class, "Some message")
                .expectResolvedException(RuntimeException.class,
                        containsString("Some"),
                        containsString("message"));

        try {
            mockMvcGet(responseSupplier);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        assertThat(
                responseSupplier.getResultMatches().stream().map(SequentialActionSupplier::toString).collect(toList()),
                matcher
        );
    }

    @AfterMethod
    public void afterMethod() {
        DEFAULT_LOCALE_PROPERTY.accept(null);
        DEFAULT_LOCALIZATION_ENGINE.accept(null);
        SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.accept(null);
    }
}
