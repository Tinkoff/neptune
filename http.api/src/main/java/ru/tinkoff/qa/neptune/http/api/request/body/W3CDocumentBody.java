package ru.tinkoff.qa.neptune.http.api.request.body;

import org.w3c.dom.Document;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.net.http.HttpRequest;

import static com.google.common.base.Preconditions.checkArgument;
import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static java.util.Objects.nonNull;

public final class W3CDocumentBody extends RequestBody<String> {

    W3CDocumentBody(Document body) {
        super(transform(body));
    }

    private static String transform(Document document) {
        checkArgument(nonNull(document), "Document should not be a null value");

        try {
            var transformer = TransformerFactory.newInstance().newTransformer();
            var domSource = new DOMSource(document);
            var sw = new StringWriter();

            StreamResult sr = new StreamResult(sw);

            transformer.transform(domSource, sr);
            return sw.toString();
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HttpRequest.BodyPublisher createPublisher() {
        return ofString(super.body());
    }

    @Override
    public String toString() {
        return body();
    }
}
