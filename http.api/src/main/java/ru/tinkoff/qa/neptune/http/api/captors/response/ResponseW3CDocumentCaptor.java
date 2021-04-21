package ru.tinkoff.qa.neptune.http.api.captors.response;

import org.w3c.dom.Document;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedFileInjector;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;

import static java.io.File.createTempFile;
import static java.nio.charset.Charset.defaultCharset;
import static java.util.UUID.randomUUID;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static ru.tinkoff.qa.neptune.core.api.utils.SPIUtil.loadSPI;

@Description("Response Body. W3C document")
public final class ResponseW3CDocumentCaptor extends AbstractResponseBodyObjectCaptor<Document, File> {

    public ResponseW3CDocumentCaptor() {
        super(loadSPI(CapturedFileInjector.class), Document.class);
    }

    @Override
    public File getData(Document caught) {
        var uuid = randomUUID().toString();
        try {
            var toAttach = createTempFile("w3c_doc", uuid + ".xml");

            var transformer = TransformerFactory.newInstance().newTransformer();
            var domSource = new DOMSource(caught);
            var sw = new StringWriter();

            StreamResult sr = new StreamResult(sw);
            transformer.transform(domSource, sr);

            writeStringToFile(toAttach, sw.toString(),
                    defaultCharset(), true);
            toAttach.deleteOnExit();
            return toAttach;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
