package ru.tinkoff.qa.neptune.http.api.captors.request;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;
import ru.tinkoff.qa.neptune.http.api.request.body.FileBody;

import java.io.File;

public final class FileRequestBodyCaptor extends FileCaptor<FileBody> implements BaseRequestBodyCaptor<FileBody> {

    public FileRequestBodyCaptor() {
        super("Request body. File");
    }

    @Override
    public File getData(FileBody caught) {
        return caught.body();
    }

    @Override
    public FileBody getCaptured(Object toBeCaptured) {
        return getCaptured(toBeCaptured, FileBody.class);
    }
}
