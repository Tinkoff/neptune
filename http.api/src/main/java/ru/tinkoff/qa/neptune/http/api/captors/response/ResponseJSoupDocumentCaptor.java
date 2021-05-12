package ru.tinkoff.qa.neptune.http.api.captors.response;

import org.jsoup.nodes.Document;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedFileInjector;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.io.File;

import static java.io.File.createTempFile;
import static java.nio.charset.Charset.defaultCharset;
import static java.util.UUID.randomUUID;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static ru.tinkoff.qa.neptune.core.api.utils.SPIUtil.loadSPI;

@Description("Response Body. JSoup document")
public final class ResponseJSoupDocumentCaptor extends AbstractResponseBodyObjectCaptor<Document, File> {

    public ResponseJSoupDocumentCaptor() {
        super(loadSPI(CapturedFileInjector.class), Document.class);
    }

    @Override
    public File getData(Document caught) {
        var uuid = randomUUID().toString();
        try {
            var toAttach = createTempFile("jsoup_doc", uuid + ".html");
            writeStringToFile(toAttach, caught.outerHtml(),
                    defaultCharset(), true);
            toAttach.deleteOnExit();
            return toAttach;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
