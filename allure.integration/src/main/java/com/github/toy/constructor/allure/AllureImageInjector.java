package com.github.toy.constructor.allure;

import com.github.toy.constructor.core.api.event.firing.captors.CapturedImageInjector;
import io.qameta.allure.AllureResultsWriteException;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.*;

import static io.qameta.allure.Allure.addAttachment;
import static java.io.File.createTempFile;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;

public class AllureImageInjector implements CapturedImageInjector {

    private InputStream inputStream(BufferedImage image) {
        File picForLog = null;
        try {
            picForLog = createTempFile("picture", randomUUID().toString() + ".png");
            ImageIO.write(image, "png", picForLog);
            return new FileInputStream(picForLog);
        }
        catch (IOException e) {
            throw new AllureResultsWriteException(e.getMessage(), e);
        }
        finally {
            ofNullable(picForLog).ifPresent(file -> {
                if (file.exists()) {
                    file.deleteOnExit();
                }
            });
        }
    }

    @Override
    public void inject(BufferedImage toBeInjected, String message) {
        InputStream inputStream = inputStream(toBeInjected);
        addAttachment(message, "image/*", inputStream, "png");
    }
}
