package ru.tinkoff.qa.neptune.http.api.request.body;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.http.HttpRequest;
import java.nio.file.Path;

import static java.net.http.HttpRequest.BodyPublishers.ofFile;

class FileBody extends RequestBody<File> {

    FileBody(File body) {
        super(body);
    }

    FileBody(Path path) {
        this(path.toFile());
    }

    @Override
    public HttpRequest.BodyPublisher createPublisher() {
        try {
            return ofFile(body().toPath());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
