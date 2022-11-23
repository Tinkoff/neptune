package ru.tinkoff.qa.neptune.allure;

import io.qameta.allure.AllureResultsWriteException;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedImageInjector;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static io.qameta.allure.Allure.addAttachment;
import static java.io.File.createTempFile;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;
import static ru.tinkoff.qa.neptune.allure.lifecycle.ItemsToNotBeReported.toReport;

public class AllureImageInjector implements CapturedImageInjector {

    private InputStream inputStream(BufferedImage image) {
        File picForLog = null;
        try {
            picForLog = createTempFile("picture", randomUUID() + ".png");
            ImageIO.write(image, "png", picForLog);
            return new FileInputStream(picForLog);
        } catch (IOException e) {
            throw new AllureResultsWriteException(e.getMessage(), e);
        } finally {
            ofNullable(picForLog).ifPresent(file -> {
                if (file.exists()) {
                    file.deleteOnExit();
                }
            });
        }
    }

    @Override
    public void inject(BufferedImage toBeInjected, String message) {
        if (!toReport()) {
            return;
        }

        InputStream inputStream = inputStream(toBeInjected);
        addAttachment(message, "image/png", inputStream, "png");
    }
}
