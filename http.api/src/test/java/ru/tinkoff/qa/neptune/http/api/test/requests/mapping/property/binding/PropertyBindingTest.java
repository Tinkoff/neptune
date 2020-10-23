package ru.tinkoff.qa.neptune.http.api.test.requests.mapping.property.binding;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.URI;

import static java.lang.System.getProperties;
import static java.net.http.HttpClient.Version.HTTP_2;
import static java.time.Duration.ofSeconds;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static ru.tinkoff.qa.neptune.core.api.dependency.injection.DependencyInjector.injectValues;
import static ru.tinkoff.qa.neptune.http.api.properties.DefaultEndPointOfTargetAPIProperty.DEFAULT_END_POINT_OF_TARGET_API_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI.createAPI;

public class PropertyBindingTest {

    @DataProvider
    public static Object[][] testData() {
        return new Object[][]{
                {createAPI(ServiceAPI3.class)},
                {createAPI(ServiceAPI4.class)},
                {createAPI(ServiceAPI6.class)}
        };
    }

    @Test
    public void test1() {
        var request = createAPI(ServiceAPI.class).post().build();

        assertThat(request.uri(), is(URI.create("http://127.0.0.1:8089/?param=val1&param=3&param=Hello+world")));
        assertThat(request.version().orElse(null), is(HTTP_2));
        assertThat(request.timeout().orElse(null), is(ofSeconds(2)));
        assertThat(request.expectContinue(), is(true));
        assertThat(request.headers().map(), allOf(hasEntry("header1", of("abc", "one more value")),
                hasEntry("header2", of("one more value")),
                hasEntry("header3", of("one more value again"))));
    }

    @Test(dataProvider = "testData")
    public void test2(ServiceAPI2<?> serviceAPI2) {
        var request = serviceAPI2.post().build();

        assertThat(request.uri(), is(URI.create("http://127.0.0.1:8090/?param=val1&param=3&param=Hello+world")));
        assertThat(request.version().orElse(null), is(HTTP_2));
        assertThat(request.timeout().orElse(null), is(ofSeconds(2)));
        assertThat(request.expectContinue(), is(true));
        assertThat(request.headers().map(), allOf(hasEntry("header1", of("abc", "one more value")),
                hasEntry("header2", of("one more value")),
                hasEntry("header3", of("one more value again"))));
    }

    @Test
    public void test3() {
        try {
            DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.accept("http://127.0.0.1:10");

            var request = createAPI(ServiceAPI5.class).post().build();

            assertThat(request.uri(), is(URI.create("http://127.0.0.1:10")));
            assertThat(request.version().orElse(null), nullValue());
            assertThat(request.timeout().orElse(null), nullValue());
            assertThat(request.expectContinue(), is(false));
            assertThat(request.headers().map(), anEmptyMap());
        } finally {
            getProperties().remove(DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.getName());
        }
    }

    @Test
    public void test4() {
        var o = new ObjectWithServiceField();
        injectValues(o);

        var request = o.getServiceAPI4().post().build();

        assertThat(request.uri(), is(URI.create("http://127.0.0.1:8090/?param=val1&param=3&param=Hello+world")));
        assertThat(request.version().orElse(null), is(HTTP_2));
        assertThat(request.timeout().orElse(null), is(ofSeconds(2)));
        assertThat(request.expectContinue(), is(true));
        assertThat(request.headers().map(), allOf(hasEntry("header1", of("abc", "one more value")),
                hasEntry("header2", of("one more value")),
                hasEntry("header3", of("one more value again"))));
    }
}
