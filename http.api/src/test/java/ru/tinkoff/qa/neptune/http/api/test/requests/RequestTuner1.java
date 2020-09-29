package ru.tinkoff.qa.neptune.http.api.test.requests;

import ru.tinkoff.qa.neptune.http.api.request.RequestSettings;
import ru.tinkoff.qa.neptune.http.api.request.RequestTuner;

import static java.net.http.HttpClient.Version.HTTP_2;
import static java.time.Duration.ofSeconds;

public class RequestTuner1 implements RequestTuner {

    @Override
    public void setUp(RequestSettings<?> requestSettings) {
        requestSettings.queryParam("param", false, "val1", 3, "Hello world")
                .timeout(ofSeconds(2))
                .version(HTTP_2)
                .expectContinue(true);
    }
}
