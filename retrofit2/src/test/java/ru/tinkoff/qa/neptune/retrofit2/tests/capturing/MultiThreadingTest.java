package ru.tinkoff.qa.neptune.retrofit2.tests.capturing;

import com.github.tomakehurst.wiremock.WireMockServer;
import okhttp3.OkHttpClient;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.tinkoff.qa.neptune.retrofit2.service.setup.HttpServiceCreator;
import ru.tinkoff.qa.neptune.retrofit2.service.setup.RetrofitBuilderSupplier;

import java.io.File;
import java.net.URL;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.not.NotMatcher.notOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.entryKey;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher.mapIncludes;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.SUCCESS_AND_FAILURE;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.retrofit2.RetrofitContext.retrofit;
import static ru.tinkoff.qa.neptune.retrofit2.properties.DefaultRetrofitURLProperty.DEFAULT_RETROFIT_URL_PROPERTY;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectSupplier.callBody;
import static ru.tinkoff.qa.neptune.retrofit2.tests.capturing.TestEventLogger.ADDITIONAL_ARGUMENTS;
import static ru.tinkoff.qa.neptune.retrofit2.tests.capturing.TestFileInjector.MESSAGES;

public class MultiThreadingTest {

    private static final String LOCALHOST = "127.0.0.1";
    private static WireMockServer wireMockServer;
    private CapturingService service;

    @AfterClass
    public static void tearDown() {
        wireMockServer.stop();
        DO_CAPTURES_OF_INSTANCE.accept(null);
    }

    @BeforeClass
    public void preparation() throws Exception {
        wireMockServer = new WireMockServer(options().port(8093));
        wireMockServer.start();
        configureFor(LOCALHOST, 8093);

        stubFor(post(urlPathEqualTo("/success.html"))
                .willReturn(aResponse().withBody("SUCCESS").withStatus(200)));


        DEFAULT_RETROFIT_URL_PROPERTY.accept(new URL("http://" + LOCALHOST + ":8093/"));

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

        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);
    }

    @Test(threadPoolSize = 6, invocationCount = 6)
    public void test() {
        DO_CAPTURES_OF_INSTANCE.accept(SUCCESS_AND_FAILURE);

        retrofit().get(callBody(
                () -> service.getSuccessful("Test body")));

        assertThat(ADDITIONAL_ARGUMENTS.get(), mapIncludes(
                entryKey("URL"),
                entryKey("METHOD")));

        assertThat(MESSAGES.get(), mapOf(
                mapEntry("Request body", instanceOf(File.class)),
                mapEntry("Response body", instanceOf(File.class))
        ));

        assertThat(TestStringInjector.MESSAGES.get(), mapOf(
                mapEntry("Response", notOf(nullValue()))
        ));
    }
}
