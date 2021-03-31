package ru.tinkoff.qa.neptune.http.api.captors.request;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.http.api.request.body.FileBody;

import java.io.File;

import static java.util.List.of;

@Description("Request body. File")
public final class FileRequestBodyCaptor extends FileCaptor<FileBody> implements BaseRequestBodyCaptor {

    @Override
    public File getData(FileBody caught) {
        return caught.body();
    }

    @Override
    public FileBody getCaptured(Object toBeCaptured) {
        return (FileBody) getCaptured(toBeCaptured, of(FileBody.class));
    }
}
