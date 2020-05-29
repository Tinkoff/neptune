package ru.tinkoff.qa.neptune.http.api.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;
import ru.tinkoff.qa.neptune.http.api.dto.DTObject;
import ru.tinkoff.qa.neptune.http.api.dto.JsonDTObject;
import ru.tinkoff.qa.neptune.http.api.dto.XmlDTObject;

import java.io.File;
import java.net.http.HttpResponse;

import static java.io.File.createTempFile;
import static java.nio.charset.Charset.defaultCharset;
import static java.util.UUID.randomUUID;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class ResponseDTOCaptor extends FileCaptor<HttpResponse<DTObject>> implements BaseResponseObjectBodyCaptor<DTObject> {

    public ResponseDTOCaptor() {
        super("Response Body. Dto");
    }

    @Override
    public File getData(HttpResponse<DTObject> caught) {
        var uuid = randomUUID().toString();
        try {
            var body = caught.body();
            var bodyCls = body.getClass();
            String extension;

            if (JsonDTObject.class.isAssignableFrom(bodyCls)) {
                extension = ".json";
            } else if (XmlDTObject.class.isAssignableFrom(bodyCls)) {
                extension = ".xml";
            } else {
                extension = EMPTY;
            }

            var toAttach = createTempFile("json_response_body", uuid + extension);
            writeStringToFile(toAttach, body.serialize(),
                    defaultCharset(), true);
            toAttach.deleteOnExit();
            return toAttach;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public HttpResponse<DTObject> getCaptured(Object toBeCaptured) {
        return getCaptured(toBeCaptured, DTObject.class);
    }
}
