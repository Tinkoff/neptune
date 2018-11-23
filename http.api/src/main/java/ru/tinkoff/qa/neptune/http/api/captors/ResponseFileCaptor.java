package ru.tinkoff.qa.neptune.http.api.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Path;


import static com.google.common.io.Files.getFileExtension;
import static com.google.common.io.Files.getNameWithoutExtension;
import static java.io.File.createTempFile;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;
import static org.apache.commons.io.FileUtils.copyFile;
import static org.apache.commons.io.FileUtils.writeStringToFile;

/**
 * This class is designed to convert some {@link String} and {@link Path} bodies of received responses
 * to files.
 */
public class ResponseFileCaptor extends FileCaptor<HttpResponse> {

    public ResponseFileCaptor() {
        super();
    }

    public void capture(HttpResponse caught, String message) {
        super.capture(caught, format("Received response. %s", message));
    }

    @Override
    protected File getData(HttpResponse caught) {
        var body = caught.body();
        var uuid = randomUUID().toString();

        return ofNullable(body)
                .map(o -> {
                    Class<?> clazz = o.getClass();
                    if (Path.class.isAssignableFrom(clazz)) {
                        var path = (Path) o;
                        File file = path.toFile();

                        if (file.exists()) {
                            var absolutePath = file.getAbsolutePath();
                            try {
                                var tempFile = createTempFile(format("%s_%s", getNameWithoutExtension(absolutePath), uuid),
                                        getFileExtension(absolutePath));
                                tempFile.deleteOnExit();
                                copyFile(file, tempFile);
                                return tempFile;
                            } catch (IOException e) {
                                return null;
                            }
                        }
                        else {
                            return null;
                        }
                    }

                    if (String.class.equals(o.getClass())) {
                        var stringBody = (String.valueOf(o));
                        try {
                            var tempFile = createTempFile(format("StringBodyOfResponse_%s", uuid),
                                    ".txt");
                            tempFile.deleteOnExit();
                            writeStringToFile(tempFile, stringBody, "UTF-8", true);
                            return tempFile;
                        } catch (IOException e) {
                            return null;
                        }
                    }

                    return null;
                })
                .orElse(null);
    }

    @Override
    public Class<HttpResponse> getTypeToBeCaptured() {
        return HttpResponse.class;
    }
}
