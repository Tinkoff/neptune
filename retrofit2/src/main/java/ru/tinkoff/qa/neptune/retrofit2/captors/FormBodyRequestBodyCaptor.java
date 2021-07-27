package ru.tinkoff.qa.neptune.retrofit2.captors;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import java.io.File;
import java.io.IOException;

import static java.io.File.createTempFile;
import static java.util.UUID.randomUUID;
import static org.apache.commons.io.FileUtils.writeByteArrayToFile;

public final class FormBodyRequestBodyCaptor extends AbstractRequestBodyCaptor {

    @Override
    public RequestBody getCaptured(Object toBeCaptured) {
        if (toBeCaptured instanceof FormBody) {
            return (RequestBody) toBeCaptured;
        }
        return null;
    }

    @Override
    public File getData(RequestBody caught) {
        var form = (FormBody) caught;

        if (form.size() == 0) {
            return null;
        }

        var sb = new StringBuilder();
        for (int i = 0; i < form.size(); i++) {
            sb.append(form.name(i)).append(": ").append(form.value(i));
        }

        try {
            var tempFile = createTempFile(randomUUID().toString(), ".txt");
            writeByteArrayToFile(tempFile, sb.toString().getBytes());
            tempFile.deleteOnExit();

            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
