package ru.tinkoff.qa.neptune.http.api.captors.request;

import ru.tinkoff.qa.neptune.core.api.event.firing.CapturedDataInjector;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;
import ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart;

import java.io.File;
import java.io.IOException;

import static java.io.File.createTempFile;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;
import static org.apache.commons.io.FileUtils.writeByteArrayToFile;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

final class BodyPartCapturedDataInjector implements CapturedDataInjector<BodyPart[]> {

    @Override
    public void inject(BodyPart[] toBeInjected, String message) {
        stream(toBeInjected).forEach(bodyPart -> {
            var bytes = bodyPart.getBytes();
            var name = ofNullable(bodyPart.getName()).orElse("<no name>");
            var contentType = bodyPart.getContentType();
            var encoding = bodyPart.getContentTransferEncoding();
            var fileName = bodyPart.getFileName();

            var randomUUID = randomUUID().toString();
            try {
                var tempFile = createTempFile(isNotBlank(fileName) ? randomUUID + fileName : randomUUID, EMPTY);
                writeByteArrayToFile(tempFile, bytes);
                tempFile.deleteOnExit();

                var sb = new StringBuilder(message).append("name ").append(name);
                if (isNotBlank(contentType)) {
                    sb.append(";").append(" Content-Type ").append(contentType);
                }

                if (isNotBlank(encoding)) {
                    sb.append(";").append(" Content-Transfer-Encoding ").append(encoding);
                }

                if (isNotBlank(fileName)) {
                    sb.append(";").append(" filename ").append(fileName);
                }

                new PipeLineFileCaptor(sb.toString()).capture(tempFile);
            } catch (IOException ignored) {
            }
        });
    }

    private static class PipeLineFileCaptor extends FileCaptor<File> {

        public PipeLineFileCaptor(String message) {
            super(message);
        }

        @Override
        public File getData(File caught) {
            return caught;
        }

        @Override
        public File getCaptured(Object toBeCaptured) {
            return (File) toBeCaptured;
        }
    }
}
