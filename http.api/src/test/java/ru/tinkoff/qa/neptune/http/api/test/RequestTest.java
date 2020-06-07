package ru.tinkoff.qa.neptune.http.api.test;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.http.api.request.NeptuneHttpRequestImpl;

import java.net.URI;
import java.net.URL;

import static java.net.http.HttpClient.Version.HTTP_2;
import static java.time.Duration.ofSeconds;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.*;

public class RequestTest {

    @Test
    public void test1() throws Exception {
        var request = DELETE(new URL("https://www.google.com/"))
                .setHeader("header1", "abc")
                .header("header1", "one more value")
                .queryParam("param", "val1", 3, "Hello world")
                .timeout(ofSeconds(2))
                .version(HTTP_2)
                .expectContinue(true)
                .headers("header2", "one more value", "header3", "one more value again")
                .build();

        assertThat(request, instanceOf(NeptuneHttpRequestImpl.class));
        assertThat(request.uri(), is(URI.create("https://www.google.com/?param=val1&param=3&param=Hello+world")));
        assertThat(request.version().orElse(null), is(HTTP_2));
        assertThat(request.timeout().orElse(null), is(ofSeconds(2)));
        assertThat(request.expectContinue(), is(true));
        assertThat(request.headers().map(), allOf(hasEntry("header1", of("abc", "one more value")),
                hasEntry("header2", of("one more value")),
                hasEntry("header3", of("one more value again"))));
        assertThat(request.method(), is("DELETE"));
    }

    @Test
    public void test2() throws Exception {
        var request = METHOD("Some_method", new URL("https://www.google.com/"))
                .tuneWith(new RequestTuner1(), new RequestTuner2())
                .build();

        assertThat(request, instanceOf(NeptuneHttpRequestImpl.class));
        assertThat(request.uri(), is(URI.create("https://www.google.com/?param=val1&param=3&param=Hello+world")));
        assertThat(request.version().orElse(null), is(HTTP_2));
        assertThat(request.timeout().orElse(null), is(ofSeconds(2)));
        assertThat(request.expectContinue(), is(true));
        assertThat(request.headers().map(), allOf(hasEntry("header1", of("abc", "one more value")),
                hasEntry("header2", of("one more value")),
                hasEntry("header3", of("one more value again"))));
        assertThat(request.method(), is("Some_method"));
    }

    @Test
    public void test3() {
        var request = PUT("https://www.google.com/")
                .tuneWith(RequestTuner1.class, RequestTuner1.class, RequestTuner2.class)
                .build();

        assertThat(request, instanceOf(NeptuneHttpRequestImpl.class));
        assertThat(request.uri(), is(URI.create("https://www.google.com/?param=val1&param=3&param=Hello+world")));
        assertThat(request.version().orElse(null), is(HTTP_2));
        assertThat(request.timeout().orElse(null), is(ofSeconds(2)));
        assertThat(request.expectContinue(), is(true));
        assertThat(request.headers().map(), allOf(hasEntry("header1", of("abc", "one more value")),
                hasEntry("header2", of("one more value")),
                hasEntry("header3", of("one more value again"))));
        assertThat(request.method(), is("PUT"));
    }
}
