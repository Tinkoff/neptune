package ru.tinkoff.qa.neptune.retrofit2.captors;

import okhttp3.RequestBody;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

@Description("Request body")
public abstract class AbstractRequestBodyCaptor extends FileCaptor<RequestBody> {

    public AbstractRequestBodyCaptor() {
        super();
    }

    protected AbstractRequestBodyCaptor(String message) {
        super(message);
    }
}
