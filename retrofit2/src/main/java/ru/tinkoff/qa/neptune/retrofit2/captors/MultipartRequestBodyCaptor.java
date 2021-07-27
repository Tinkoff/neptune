package ru.tinkoff.qa.neptune.retrofit2.captors;

import okhttp3.MultipartBody;
import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

import java.util.List;

import static java.util.List.of;

public final class MultipartRequestBodyCaptor extends Captor<MultipartBody, List<MultipartBody.Part>> {

    public MultipartRequestBodyCaptor() {
        super(of(new BodyPartCapturedDataInjector()));
    }

    @Override
    public List<MultipartBody.Part> getData(MultipartBody caught) {
        return caught.parts();
    }

    @Override
    public MultipartBody getCaptured(Object toBeCaptured) {
        if (toBeCaptured instanceof MultipartBody) {
            return (MultipartBody) toBeCaptured;
        }
        return null;
    }
}
