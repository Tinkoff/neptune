package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.hamcrest.Matcher;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testng.annotations.*;
import ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents;

import java.util.Map;
import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.not.NotMatcher.notOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapOf;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.spring.web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient.TestStringInjector.getMessages;
import static ru.tinkoff.qa.neptune.spring.web.testclient.WebTestClientContext.webTestClient;

@SuppressWarnings({"unchecked"})
public class FailedResponseTest extends BaseTest {

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {SUCCESS, anEmptyMap()},
                {FAILURE, mapOf(
                        mapEntry("Request and response", notOf(emptyOrNullString()))
                )},
                {SUCCESS_AND_FAILURE, mapOf(
                        mapEntry("Request and response", notOf(emptyOrNullString()))
                )},
        };
    }

    @BeforeClass
    public void setUpBeforeClass() {
        super.setUpBeforeClass();

        Mockito.<WebTestClient.BodySpec<Integer, ?>>when(responseSpec.expectBody(Integer.class)).thenReturn(integerBodySpec);
        doAnswer(invocation -> {
            var consumer = invocation.getArgument(0, Consumer.class);
            consumer.accept(intResult);
            return integerBodySpec;
        }).when(integerBodySpec).consumeWith(any(Consumer.class));
        when(intResult.getResponseBody()).thenThrow(new RuntimeException("Test parse exception") {
        });
    }

    @Test
    public void failedTest() {
        try {
            webTestClient(send(client, w -> w.get()
                    .uri("https://google.com/api/request/1"))
                    .expectStatus(StatusAssertions::isOk)
                    .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                    .emptyBody());
        } catch (AssertionError e) {
            assertThat(e.getCause(), nullValue());
            mapEntry("Request and response", notOf(emptyOrNullString()));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void failedTest2() {
        try {
            setByteSupplier(() -> null);
            webTestClient(send(client, w -> w.get()
                    .uri("https://google.com/api/request/1"))
                    .expectStatus(StatusAssertions::isOk)
                    .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                    .hasBody());
        } catch (AssertionError e) {
            assertThat(e.getCause(), nullValue());
            mapEntry("Request and response", notOf(emptyOrNullString()));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void failedTest3() {
        try {
            setByteSupplier(() -> new byte[0]);
            webTestClient(send(client, w -> w.get()
                    .uri("https://google.com/api/request/1"))
                    .expectStatus(StatusAssertions::isOk)
                    .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                    .hasBody());
        } catch (AssertionError e) {
            assertThat(e.getCause(), nullValue());
            mapEntry("Request and response", notOf(emptyOrNullString()));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void failedTest4() {
        try {
            setByteSupplier(() -> {
                throw new RuntimeException();
            });
            webTestClient(send(client, w -> w.get()
                    .uri("https://google.com/api/request/1"))
                    .expectStatus(StatusAssertions::isOk)
                    .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                    .hasBody());
        } catch (AssertionError e) {
            assertThat(e.getCause(), nullValue());
            mapEntry("Request and response", notOf(emptyOrNullString()));
            return;
        }

        fail("Exception was expected");
    }

    @Test
    public void failedTest5() {
        try {
            webTestClient(send(client, w -> w.get()
                    .uri("https://google.com/api/request/1"))
                    .expectStatus(StatusAssertions::isOk)
                    .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                    .bodyAs(Integer.class)
                    .thenGetBody());
        } catch (AssertionError e) {
            mapEntry("Request and response", notOf(emptyOrNullString()));
            return;
        }

        fail("Exception was expected");
    }

    @AfterMethod
    @BeforeMethod
    public void prepare() {
        DO_CAPTURES_OF_INSTANCE.accept(null);
        getMessages().clear();
    }

    @Test(dependsOnMethods = "failedTest", dataProvider = "data")
    public void capturesOfFailed(CapturedEvents eventType, Matcher<Map<String, String>> matcher) {
        DO_CAPTURES_OF_INSTANCE.accept(eventType);

        try {
            webTestClient(send(client, w -> w.get()
                    .uri("https://google.com/api/request/1"))
                    .expectStatus(StatusAssertions::isOk)
                    .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                    .emptyBody()
                    .thenGetBody());
        } catch (Throwable ignored) {
        }

        assertThat(getMessages(), matcher);
    }
}
