package ru.tinkoff.qa.neptune.allure;

import io.qameta.allure.AllureResultsWriteException;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedFileInjector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.google.common.io.Files.getFileExtension;
import static io.qameta.allure.Allure.addAttachment;
import static java.nio.file.Files.probeContentType;
import static java.util.Optional.ofNullable;

public class AllureFileInjector implements CapturedFileInjector {

    @Override
    public void inject(File toBeInjected, String message) {
        String contentType = null;
        try {
            contentType = ofNullable(probeContentType(toBeInjected.toPath())).orElse(null);
        } catch (Exception ignored) {
        }

        try {
            addAttachment(message, contentType, new FileInputStream(toBeInjected),
                    getFileExtension(toBeInjected.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            throw new AllureResultsWriteException(e.getMessage(), e);
        }
    }
}
