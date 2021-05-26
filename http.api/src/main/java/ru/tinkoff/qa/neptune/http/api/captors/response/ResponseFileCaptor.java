package ru.tinkoff.qa.neptune.http.api.captors.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.UseInjectors;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedFileInjector;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.io.File;
import java.io.IOException;
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
@UseInjectors(CapturedFileInjector.class)
@Description("Response. File")
public final class ResponseFileCaptor extends AbstractResponseBodyObjectCaptor<Path, File> {


    public ResponseFileCaptor() {
        super(Path.class);
    }

    @Override
    public File getData(Path caught) {
        var uuid = randomUUID().toString();

        File file = caught.toFile();

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
}
