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

public class SuccessfulResponseTest extends BaseTestPreparation {

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

        assertThat(body.length, is(20));
    }

    @Test(description = "successful with empty body", dependsOnMethods = "successfulTestWithoutBodySpec")
    public void successfulEmptyBody() {
        setByteSupplier(() -> new byte[0]);

        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .expectEmptyBody()
                .thenGetBody());

        assertThat(body.length, is(0));
    }

    @Test(description = "successful with empty body", dependsOnMethods = "successfulTestWithoutBodySpec")
    public void successfulEmptyBody2() {
        setByteSupplier(() -> null);

        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .expectEmptyBody()
                .thenGetBody());

        assertThat(body, nullValue());
    }

    @Test(description = "successful with any body", dependsOnMethods = "successfulTestWithoutBodySpec")
    public void successfulAnyBody() {
        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .thenGetBody());

        assertThat(body.length, is(20));
    }

    @Test(description = "successful with class body", dependsOnMethods = "successfulTestWithoutBodySpec")
    public void successfulBodyByClass() {
        var body = webTestClient(send(client, w -> w.get()
                        .uri("https://google.com/api/request/1"),
                Integer.class)
                .expectBody(lessThan(2))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .thenGetBody());

        assertThat(body, is(1));
    }

    @Test(description = "successful with type body", dependsOnMethods = "successfulTestWithoutBodySpec")
    public void successfulBodyByType() {
        var body = webTestClient(send(client, w -> w.get()
                        .uri("https://google.com/api/request/1"),
                new ParameterizedTypeReference<List<Integer>>() {
                })
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .expectBody(hasSize(greaterThan(2)))
                .thenGetBody());

        assertThat(body, contains(1, 2, 3));
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
