package ru.tinkoff.qa.neptune.http.api.response.body.data;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.StringReader;
import java.util.function.Function;

final class W3CDocument implements Function<String, Document> {

    private final DocumentBuilder documentBuilder;

    W3CDocument(DocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;
    }

    W3CDocument() {
        this(documentBuilder());
    }

    private static DocumentBuilder documentBuilder() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Document apply(String s) {
        var inputSource = new InputSource(new StringReader(s));
        try {
            return documentBuilder.parse(inputSource);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
