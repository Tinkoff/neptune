package ru.tinkoff.qa.neptune.http.api.captors;

import org.jsoup.nodes.Document;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;

import java.io.File;
import java.net.http.HttpResponse;

import static java.io.File.createTempFile;
import static java.nio.charset.Charset.defaultCharset;
import static java.util.UUID.randomUUID;
import static org.apache.commons.io.FileUtils.writeStringToFile;

public class ResponseJSoupDocumentCaptor extends FileCaptor<HttpResponse<Document>> implements BaseResponseObjectBodyCaptor<Document> {

    public ResponseJSoupDocumentCaptor() {
        super("Response Body. JSoup document");
    }

    @Override
    public File getData(HttpResponse<Document> caught) {
        var uuid = randomUUID().toString();
        try {
            var body = caught.body();

            var toAttach = createTempFile("jsoup_doc", uuid + ".html");
            writeStringToFile(toAttach, body.outerHtml(),
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
