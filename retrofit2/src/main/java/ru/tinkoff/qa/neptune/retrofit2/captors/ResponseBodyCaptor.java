package ru.tinkoff.qa.neptune.retrofit2.captors;

import okhttp3.ResponseBody;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeTypeException;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.io.File;
import java.io.IOException;

import static java.io.File.createTempFile;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;
import static org.apache.commons.io.FileUtils.writeByteArrayToFile;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Description("Response body")
public final class ResponseBodyCaptor extends FileCaptor<ResponseBody> {

    @Override
    public File getData(ResponseBody caught) {
        var bytes = new byte[0];
        try {
            bytes = caught.bytes();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

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

    @Override
    public ResponseBody getCaptured(Object toBeCaptured) {
        if (toBeCaptured instanceof ResponseBody) {
            return (ResponseBody) toBeCaptured;
        }

        return null;
    }
}
