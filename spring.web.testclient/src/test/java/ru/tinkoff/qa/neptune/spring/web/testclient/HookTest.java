package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.ExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.reactive.server.MockAssertionsCreator.createHeaderAssertion;
import static org.springframework.test.web.reactive.server.MockAssertionsCreator.createStatusAssertion;
import static org.testng.Assert.fail;

@SuppressWarnings({"rawtypes", "unchecked"})
public class HookTest {

    private static final PseudoTest PSEUDO_TEST = new PseudoTest();
    private static final SpringWebTestClientExecutionHook HOOK = new SpringWebTestClientExecutionHook();

    @Mock
    private WebTestClient client;

    @Mock
    private WebTestClient.RequestHeadersUriSpec uriSpec;

    @Mock
    private WebTestClient.ResponseSpec responseSpec;

    @Mock
    private ExchangeResult result;

    @Mock
    private EntityExchangeResult<byte[]> rawResult;

    @Mock
    private WebTestClient.BodyContentSpec bodyContentSpec;

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {true},
                {false}
        };
    }

    @BeforeClass
    public void setUpBeforeClass() {
        byte[] b = new byte[20];
        new Random().nextBytes(b);

        openMocks(this);
        when(client.get()).thenReturn(uriSpec);
        when(uriSpec.uri("https://google.com/api/request/1")).thenReturn(uriSpec);
        when(uriSpec.exchange()).thenReturn(responseSpec);

        when(responseSpec.expectStatus()).thenReturn(createStatusAssertion(result, responseSpec));
        when(responseSpec.expectHeader()).thenReturn(createHeaderAssertion(result, responseSpec));

        when(result.getRawStatusCode()).thenReturn(200);
        when(result.getResponseHeaders()).thenReturn(new HttpHeaders());
        doNothing().when(result).assertWithDiagnostics(any(Runnable.class));

        when(responseSpec.expectBody()).thenReturn(bodyContentSpec);
        when(bodyContentSpec.returnResult()).thenReturn(rawResult);
        when(rawResult.getResponseBody()).thenReturn(b);
    }

    @Test(dataProvider = "data", expectedExceptions = IllegalStateException.class)
    public void test1(boolean isTest) throws Exception {
        PSEUDO_TEST.setWebTestClient(null);
        HOOK.executeMethodHook(PseudoTest.class.getDeclaredMethod("test1"), PSEUDO_TEST, isTest);
        PSEUDO_TEST.test1();
        fail("Exception was expected");
    }

    @Test(dataProvider = "data")
    public void test2(boolean isTest) throws Exception {
        PSEUDO_TEST.setWebTestClient(client);
        HOOK.executeMethodHook(PseudoTest.class.getDeclaredMethod("test1"), PSEUDO_TEST, isTest);
        assertThat(PSEUDO_TEST.test1(), is(client));
    }

    @Test(dataProvider = "data", dependsOnMethods = "test2")
    public void test3(boolean isTest) throws Exception {
        PSEUDO_TEST.setWebTestClient(client);
        HOOK.executeMethodHook(PseudoTest.class.getDeclaredMethod("test1"), PSEUDO_TEST, isTest);
        assertThat(PSEUDO_TEST.test2(), arrayWithSize(20));
    }
}
