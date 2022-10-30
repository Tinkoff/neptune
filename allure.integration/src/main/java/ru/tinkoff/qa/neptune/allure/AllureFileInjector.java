package ru.tinkoff.qa.neptune.allure;

import io.qameta.allure.AllureResultsWriteException;
import org.apache.tika.Tika;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedFileInjector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.google.common.io.Files.getFileExtension;
import static io.qameta.allure.Allure.addAttachment;
import static ru.tinkoff.qa.neptune.allure.lifecycle.ItemsToNotBeReported.toReport;

public class AllureFileInjector implements CapturedFileInjector {

    @Override
    public void inject(File toBeInjected, String message) {
        if (!toReport()) {
            return;
        }
        
        String mime;
        Tika tika = new Tika();

        try {
            mime = tika.detect(toBeInjected);
        } catch (Exception e) {
            mime = null;
        }

        try {
            addAttachment(message, mime, new FileInputStream(toBeInjected), getFileExtension(toBeInjected.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            throw new AllureResultsWriteException(e.getMessage(), e);
        }
    }
}
