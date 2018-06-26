package com.github.toy.constructor.allure;

import com.github.toy.constructor.core.api.captors.CapturedImageInjector;

import java.awt.image.*;
import java.io.ByteArrayInputStream;

import static io.qameta.allure.Allure.addAttachment;

public class AllureImageInjector implements CapturedImageInjector {

    private static byte[] getBytes(BufferedImage image) {
        DataBuffer dataBuffer = image.getRaster().getDataBuffer();
        Class<?> dataBufferClass = dataBuffer.getClass();
        if (DataBufferByte.class.isAssignableFrom(dataBufferClass)) {
            return DataBufferByte.class.cast(dataBuffer).getData();
        }

        BufferedImage newImage = new BufferedImage(image.getWidth(),
                image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        return getBytes(newImage);
    }

    @Override
    public void inject(BufferedImage toBeInjected, String message) {
        byte[] bytes = getBytes(toBeInjected);
        addAttachment(message, "image/*", new ByteArrayInputStream(bytes), "jpeg");
    }
}
