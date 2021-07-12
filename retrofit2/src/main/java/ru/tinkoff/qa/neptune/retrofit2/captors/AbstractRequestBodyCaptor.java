package ru.tinkoff.qa.neptune.retrofit2.captors;

import okhttp3.RequestBody;
import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

@Description("Request body")
public abstract class AbstractRequestBodyCaptor<T> extends Captor<RequestBody, T> {
}
