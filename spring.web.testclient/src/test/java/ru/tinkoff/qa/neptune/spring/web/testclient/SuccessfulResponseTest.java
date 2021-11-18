package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.hamcrest.Matcher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents;

import java.util.List;
import java.util.Map;

import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.not.NotMatcher.notOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapOf;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.spring.web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient.TestStringInjector.getMessages;
import static ru.tinkoff.qa.neptune.spring.web.testclient.WebTestClientContext.webTestClient;

public class SuccessfulResponseTest extends BaseTest {

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {SUCCESS, mapOf(
                        mapEntry("Request and response", notOf(emptyOrNullString()))
                )},
                {FAILURE, anEmptyMap()},
                {SUCCESS_AND_FAILURE, mapOf(
                        mapEntry("Request and response", notOf(emptyOrNullString()))
                )},
        };
    }

    @AfterMethod
    @BeforeMethod
    public void prepare() {
        DO_CAPTURES_OF_INSTANCE.accept(null);
        getMessages().clear();
    }

    @Test(description = "successful")
    public void successfulTestWithoutBodySpec() {
        webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN)));
    }

    @Test(description = "successful with body", dependsOnMethods = "successfulTestWithoutBodySpec")
    public void successfulBody() {
        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .thenGetBody());

        assertThat(body, arrayWithSize(20));
    }

    @Test(description = "successful with empty body", dependsOnMethods = "successfulTestWithoutBodySpec")
    public void successfulEmptyBody() {
        setByteSupplier(() -> new byte[0]);

        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .emptyBody()
                .thenGetBody());

        assertThat(body, nullValue());
    }

    @Test(description = "successful with empty body", dependsOnMethods = "successfulTestWithoutBodySpec")
    public void successfulEmptyBody2() {
        setByteSupplier(() -> null);

        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .emptyBody()
                .thenGetBody());

        assertThat(body, nullValue());
    }

    @Test(description = "successful with any body", dependsOnMethods = "successfulTestWithoutBodySpec")
    public void successfulAnyBody() {
        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .hasBody()
                .thenGetBody());

        assertThat(body, arrayWithSize(20));
    }

    @Test(description = "successful with class body", dependsOnMethods = "successfulTestWithoutBodySpec")
    public void successfulBodyByClass() {
        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAs(Integer.class)
                .thenGetBody());

        assertThat(body, is(1));
    }

    @Test(description = "successful with type body", dependsOnMethods = "successfulTestWithoutBodySpec")
    public void successfulBodyByType() {
        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAs(new ParameterizedTypeReference<List<Integer>>() {
                })
                .thenGetBody());

        assertThat(body, contains(1, 2, 3));
    }

    @Test(description = "successful with list body", dependsOnMethods = "successfulTestWithoutBodySpec")
    public void successfulListBodyByClass() {
        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAsListOf(Integer.class)
                .thenGetBody());

        assertThat(body, contains(1, 2, 3));
    }

    @Test(description = "successful with list body by type", dependsOnMethods = "successfulTestWithoutBodySpec")
    public void successfulListBodyByType() {
        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAsListOf(new ParameterizedTypeReference<List<Integer>>() {
                })
                .thenGetBody());

        assertThat(body, contains(of(1, 2, 3), of(1, 2, 3), of(1, 2, 3)));
    }

    @Test(dependsOnMethods = "successfulTestWithoutBodySpec", dataProvider = "data")
    public void capturesOfSuccessful(CapturedEvents eventType, Matcher<Map<String, String>> matcher) {
        DO_CAPTURES_OF_INSTANCE.accept(eventType);

        webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN)));

        assertThat(getMessages(), matcher);
    }
}
