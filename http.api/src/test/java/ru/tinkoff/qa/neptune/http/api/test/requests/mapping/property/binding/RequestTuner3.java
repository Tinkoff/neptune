package ru.tinkoff.qa.neptune.http.api.test.requests.mapping.property.binding;

import ru.tinkoff.qa.neptune.http.api.request.RequestSettings;
import ru.tinkoff.qa.neptune.http.api.request.RequestTuner;
import ru.tinkoff.qa.neptune.http.api.service.mapping.APIUses;

import static java.net.http.HttpClient.Version.HTTP_2;
import static java.time.Duration.ofSeconds;

@APIUses(usedBy = ServiceAPI2.class, isUsedBySubclasses = true, exclude = ServiceAPI5.class)
public class RequestTuner3 implements RequestTuner {

    @Override
    public void setUp(RequestSettings<?> requestSettings) {
        requestSettings.queryParam("param", false, "val1", 3, "Hello world")
                .timeout(ofSeconds(2))
                .version(HTTP_2)
                .expectContinue(true);
    }
}
