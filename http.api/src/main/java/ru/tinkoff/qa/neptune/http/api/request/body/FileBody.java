package ru.tinkoff.qa.neptune.http.api.request.body;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.http.HttpRequest;
import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.net.http.HttpRequest.BodyPublishers.ofFile;

public final class FileBody extends RequestBody<File> {

    FileBody(File body) {
        super(body);
        checkNotNull(body);
        checkArgument(!body.isDirectory(), "Directory should not be defined as a body of http request");
        checkArgument(body.exists(), "File " + body.getAbsolutePath() + " doesn't exists");
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

    @Override
    public String toString() {
        var file = body();
        return "File " + file.getAbsolutePath() + " size (bytes) " + file.length();
    }
}
