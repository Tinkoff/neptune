package ru.tinkoff.qa.neptune.http.api.captors.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Path;

import static com.google.common.io.Files.getFileExtension;
import static com.google.common.io.Files.getNameWithoutExtension;
import static java.io.File.createTempFile;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.apache.commons.io.FileUtils.copyFile;

/**
 * This class is designed to convert some {@link String} and {@link Path} bodies of received responses
 * to files.
 */
public final class ResponseFileCaptor extends FileCaptor<HttpResponse<Path>> implements BaseResponseObjectBodyCaptor<Path> {

    public ResponseFileCaptor() {
        super("Response. File");
    }

    @Override
    public File getData(HttpResponse<Path> caught) {
        var uuid = randomUUID().toString();

        File file = caught.body().toFile();

        var absolutePath = file.getAbsolutePath();
        try {
            var tempFile = createTempFile(format("%s_%s", getNameWithoutExtension(absolutePath), uuid),
                    "." + getFileExtension(absolutePath));
            tempFile.deleteOnExit();
            copyFile(file, tempFile);
            return tempFile;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public HttpResponse<Path> getCaptured(Object toBeCaptured) {
        return getCaptured(toBeCaptured, Path.class);
    }
}
