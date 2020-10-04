package ru.tinkoff.qa.neptune.http.api.test.requests.mapping.property.binding;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.http.api.request.NeptuneHttpRequestImpl;

import java.net.URI;

import static java.net.http.HttpClient.Version.HTTP_2;
import static java.time.Duration.ofSeconds;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI.createAPI;

public class PropertyBindingTest {

    @Test
    public void test1() {
        var request = createAPI(ServiceAPI.class).post().build();
        ;

        assertThat(request, instanceOf(NeptuneHttpRequestImpl.class));
        assertThat(request.uri(), is(URI.create("http://127.0.0.1:8089/?param=val1&param=3&param=Hello+world")));
        assertThat(request.version().orElse(null), is(HTTP_2));
        assertThat(request.timeout().orElse(null), is(ofSeconds(2)));
        assertThat(request.expectContinue(), is(true));
        assertThat(request.headers().map(), allOf(hasEntry("header1", of("abc", "one more value")),
                hasEntry("header2", of("one more value")),
                hasEntry("header3", of("one more value again"))));
    }
}
