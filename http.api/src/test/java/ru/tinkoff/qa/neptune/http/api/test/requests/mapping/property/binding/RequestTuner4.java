package ru.tinkoff.qa.neptune.http.api.test.requests.mapping.property.binding;

import ru.tinkoff.qa.neptune.http.api.request.RequestSettings;
import ru.tinkoff.qa.neptune.http.api.request.RequestTuner;
import ru.tinkoff.qa.neptune.http.api.service.mapping.APIUses;

@APIUses(usedBy = ServiceAPI2.class, isUsedBySubclasses = true, exclude = ServiceAPI5.class)
public class RequestTuner4 implements RequestTuner {

    @Override
    public void setUp(RequestSettings<?> requestSettings) {
        requestSettings.header("header2", "one more value")
                .header("header3", "one more value again")
                .header("header1", "abc")
                .header("header1", "one more value");
    }
}
