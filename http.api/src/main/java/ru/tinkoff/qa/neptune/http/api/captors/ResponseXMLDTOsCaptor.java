package ru.tinkoff.qa.neptune.http.api.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;
import ru.tinkoff.qa.neptune.http.api.dto.XmlDTObject;

import java.io.File;
import java.util.List;

import static java.io.File.createTempFile;
import static java.nio.charset.Charset.defaultCharset;
import static java.util.UUID.randomUUID;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static ru.tinkoff.qa.neptune.http.api.mapper.DefaultBodyMappers.XML;

public class ResponseXMLDTOsCaptor extends FileCaptor<List<XmlDTObject>> implements BaseResponseObjectsBodyCaptor<XmlDTObject> {

    public ResponseXMLDTOsCaptor() {
        super("Response Body. Dto. Array/collection of XML elements");
    }

    @Override
    public File getData(List<XmlDTObject> caught) {
        var uuid = randomUUID().toString();
        try {
            var toAttach = createTempFile("xml_elements_response_body", uuid + ".xml");
            writeStringToFile(toAttach, XML.getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(caught),
                    defaultCharset(), true);
            toAttach.deleteOnExit();
            return toAttach;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<XmlDTObject> getCaptured(Object toBeCaptured) {
        return getCaptured(toBeCaptured, XmlDTObject.class);
    }
}
