package ru.tinkoff.qa.neptune.http.api.captors.response;

import org.w3c.dom.Document;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.Description;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.net.http.HttpResponse;

import static java.io.File.createTempFile;
import static java.nio.charset.Charset.defaultCharset;
import static java.util.UUID.randomUUID;
import static org.apache.commons.io.FileUtils.writeStringToFile;

@Description("Response Body. W3C document")
public final class ResponseW3CDocumentCaptor extends FileCaptor<HttpResponse<Document>> implements BaseResponseObjectBodyCaptor<Document> {

    @Override
    public File getData(HttpResponse<Document> caught) {
        var uuid = randomUUID().toString();
        try {
            var body = caught.body();

            var toAttach = createTempFile("w3c_doc", uuid + ".xml");

            var transformer = TransformerFactory.newInstance().newTransformer();
            var domSource = new DOMSource(body);
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

    @Override
    public HttpResponse<Document> getCaptured(Object toBeCaptured) {
        return getCaptured(toBeCaptured, Document.class);
    }
}
