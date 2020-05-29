package ru.tinkoff.qa.neptune.http.api.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;
import ru.tinkoff.qa.neptune.http.api.dto.JsonDTObject;

import java.io.File;
import java.util.List;

import static java.io.File.createTempFile;
import static java.nio.charset.Charset.defaultCharset;
import static java.util.UUID.randomUUID;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static ru.tinkoff.qa.neptune.http.api.mapper.DefaultBodyMappers.JSON;

public class ResponseJsonDTOsCaptor extends FileCaptor<List<JsonDTObject>> implements BaseResponseObjectsBodyCaptor<JsonDTObject> {

    public ResponseJsonDTOsCaptor() {
        super("Response Body. Dto. Json Array");
    }

    @Override
    public File getData(List<JsonDTObject> caught) {
        var uuid = randomUUID().toString();
        try {
            var toAttach = createTempFile("json_array_response_body", uuid + ".json");
            writeStringToFile(toAttach, JSON.getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(caught),
                    defaultCharset(), true);
            toAttach.deleteOnExit();
            return toAttach;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<JsonDTObject> getCaptured(Object toBeCaptured) {
        return getCaptured(toBeCaptured, JsonDTObject.class);
    }
}
