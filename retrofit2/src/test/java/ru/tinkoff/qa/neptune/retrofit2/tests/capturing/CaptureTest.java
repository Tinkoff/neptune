package ru.tinkoff.qa.neptune.retrofit2.tests.capturing;

import com.github.tomakehurst.wiremock.WireMockServer;
import okhttp3.OkHttpClient;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.tinkoff.qa.neptune.retrofit2.service.setup.HttpServiceCreator;
import ru.tinkoff.qa.neptune.retrofit2.service.setup.RetrofitBuilderSupplier;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static com.github.tomakehurst.wiremock.http.Fault.MALFORMED_RESPONSE_CHUNK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.entryKey;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher.mapIncludes;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.retrofit2.RetrofitContext.retrofit;
import static ru.tinkoff.qa.neptune.retrofit2.properties.DefaultRetrofitURLProperty.DEFAULT_RETROFIT_URL_PROPERTY;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectSupplier.bodyFromCall;
import static ru.tinkoff.qa.neptune.retrofit2.tests.capturing.TestEventLogger.ADDITIONAL_ARGUMENTS;
import static ru.tinkoff.qa.neptune.retrofit2.tests.capturing.TestFileInjector.MESSAGES;

public class CaptureTest {

    private static final String LOCALHOST = "127.0.0.1";
    private static WireMockServer wireMockServer;
    private CapturingService service;

    @AfterClass
    public static void tearDown() {
        wireMockServer.stop();
    }

    @BeforeClass
    public void preparation() throws Exception {
        wireMockServer = new WireMockServer(options().port(8092));
        wireMockServer.start();
        configureFor(LOCALHOST, 8092);

        stubFor(post(urlPathEqualTo("/success.html"))
                .willReturn(aResponse().withBody("SUCCESS").withStatus(200)));

        var randomByteArray = new byte[25];
        new Random().nextBytes(randomByteArray);

        stubFor(get(urlPathEqualTo("/failure.html"))
                .willReturn(serverError()
                        .withFault(MALFORMED_RESPONSE_CHUNK)
                        .withBody(randomByteArray)));

        DEFAULT_RETROFIT_URL_PROPERTY.accept(new URL("http://" + LOCALHOST + ":8092/"));

        service = (CapturingService) HttpServiceCreator.create(CapturingService.class,
                new RetrofitBuilderSupplier() {
                    @Override
                    protected Retrofit.Builder prepareRetrofitBuilder() {
                        return new Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create());
                    }

                    @Override
                    protected OkHttpClient.Builder prepareClientBuilder() {
                        return new OkHttpClient.Builder();
                    }
                });
    }

    @BeforeMethod
    public void beforeEveryTest(Method m) {
        ADDITIONAL_ARGUMENTS.set(null);
        MESSAGES.set(null);
        TestStringInjector.MESSAGES.set(null);
        DO_CAPTURES_OF_INSTANCE.accept(null);
    }

    @Test
    public void requestCaptureTest1() {
        retrofit(service).receive(bodyFromCall(
                s -> s.getSuccessful("Test body")
        ));

        assertThat(ADDITIONAL_ARGUMENTS.get(), mapIncludes(
                entryKey("URL"),
                entryKey("METHOD")));

        assertThat(MESSAGES.get(), mapInOrder(
                mapEntry("Request body", instanceOf(File.class))));

        assertThat(TestStringInjector.MESSAGES.get(), nullValue());
    }

    @Test
    public void requestCaptureTest2() {
        retrofit(service).receive(bodyFromCall(
                s -> s.getFailed("Test body")));

        assertThat(ADDITIONAL_ARGUMENTS.get(), mapIncludes(
                entryKey("URL"),
                entryKey("METHOD")));

        assertThat(MESSAGES.get(), mapInOrder(
                mapEntry("Request body", instanceOf(File.class))));

        assertThat(TestStringInjector.MESSAGES.get(), nullValue());
    }






















    /*@Test
    public void responseCaptureTest1() {
        retrofit(service).receive(bodyFromCall(
                s -> s.getSuccessful("Test body")
        ));

        assertThat(ADDITIONAL_ARGUMENTS.get(), mapIncludes(
                entryKey("URL"),
                entryKey("METHOD")));

        assertThat(MESSAGES.get(), mapInOrder(
                mapEntry("Request body", instanceOf(File.class))));

        assertThat(TestStringInjector.MESSAGES.get(), nullValue());
    }

    @Test
    public void responseCaptureTest2() {
        retrofit(service).receive(bodyFromCall(
                s -> s.getFailed("Test body")
        ));

        assertThat(ADDITIONAL_ARGUMENTS.get(), mapIncludes(
                entryKey("URL"),
                entryKey("METHOD")));

        assertThat(MESSAGES.get(), mapInOrder(
                mapEntry("Request body", instanceOf(File.class))));

        assertThat(TestStringInjector.MESSAGES.get(), nullValue());
    }*/
}
