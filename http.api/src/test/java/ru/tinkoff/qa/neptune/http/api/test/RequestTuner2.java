package ru.tinkoff.qa.neptune.http.api.test;

import ru.tinkoff.qa.neptune.http.api.request.RequestSettings;
import ru.tinkoff.qa.neptune.http.api.request.RequestTuner;

public class RequestTuner2 implements RequestTuner {

    @Override
    public void setUp(RequestSettings<?> requestSettings) {
        requestSettings.headers("header2", "one more value", "header3", "one more value again")
                .setHeader("header1", "abc")
                .header("header1", "one more value");
    }
}
