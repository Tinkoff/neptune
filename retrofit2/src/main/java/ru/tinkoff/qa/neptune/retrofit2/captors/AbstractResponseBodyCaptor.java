package ru.tinkoff.qa.neptune.retrofit2.captors;

import okhttp3.ResponseBody;
import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

@Description("Response body")
public abstract class AbstractResponseBodyCaptor<T> extends Captor<ResponseBody, T> {
}
