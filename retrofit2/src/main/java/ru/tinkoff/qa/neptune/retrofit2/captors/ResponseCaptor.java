package ru.tinkoff.qa.neptune.retrofit2.captors;

import okhttp3.Response;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

@Description("Response")
public class ResponseCaptor extends StringCaptor<Response> {

    @Override
    public Response getCaptured(Object toBeCaptured) {
        return null;
    }

    @Override
    public StringBuilder getData(Response caught) {
        return null;
    }
}
