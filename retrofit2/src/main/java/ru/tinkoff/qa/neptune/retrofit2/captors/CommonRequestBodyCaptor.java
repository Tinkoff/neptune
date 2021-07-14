package ru.tinkoff.qa.neptune.retrofit2.captors;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.Buffer;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeTypeException;

import java.io.File;
import java.io.IOException;

import static java.io.File.createTempFile;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;
import static org.apache.commons.io.FileUtils.writeByteArrayToFile;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public final class CommonRequestBodyCaptor extends AbstractRequestBodyCaptor {

    public CommonRequestBodyCaptor() {
        super();
    }

    public CommonRequestBodyCaptor(String message) {
        super(message);
    }

    @Override
    public RequestBody getCaptured(Object toBeCaptured) {
        if (toBeCaptured instanceof RequestBody
                && !(toBeCaptured instanceof FormBody)
                && !(toBeCaptured instanceof MultipartBody)) {
            return (RequestBody) toBeCaptured;
        }
        return null;
    }

    @Override
    public File getData(RequestBody caught) {
        var b = new Buffer();
        try {
            caught.writeTo(b);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        var bytes = b.readByteArray();
        if (bytes.length == 0) {
            return null;
        }

        var fileExt = ofNullable(caught.contentType())
                .map(mediaType -> {
                    var type = mediaType.type() + "/" + mediaType.subtype();
                    var config = TikaConfig.getDefaultConfig();
                    var allTypes = config.getMimeRepository();
                    try {
                        return allTypes.forName(type).getExtension();
                    } catch (MimeTypeException e) {
                        e.printStackTrace();
                        return EMPTY;
                    }
                })
                .orElse(EMPTY);

        try {
            var tempFile = createTempFile(randomUUID().toString(), fileExt);
            writeByteArrayToFile(tempFile, bytes);
            tempFile.deleteOnExit();

            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
