package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.springframework.test.web.reactive.server.StatusAssertions;
import org.testng.annotations.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.not.NotMatcher.notOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapOf;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.SUCCESS;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.spring.web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient.TestStringInjector.getMessages;
import static ru.tinkoff.qa.neptune.spring.web.testclient.WebTestClientContext.webTestClient;

public class ResponseMultithreadingTest extends BaseTest {

    @BeforeClass
    public void setUpBeforeClass() {
        super.setUpBeforeClass();
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS);
    }

    @AfterClass
    public void clear() {
        DO_CAPTURES_OF_INSTANCE.accept(null);
    }

    @Test(threadPoolSize = 10, invocationCount = 10)
    public void test() {
        var body = webTestClient(send(client, w -> w.get()
                .uri("https://google.com/api/request/1"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .hasBody()
                .thenGetBody());

        assertThat(body, arrayWithSize(20));

        assertThat(getMessages(),
                mapOf(
                        mapEntry("Request and response", notOf(emptyOrNullString()))
                ));
    }

    @AfterMethod
    @BeforeMethod
    public void prepare() {
        getMessages().clear();
    }
}
