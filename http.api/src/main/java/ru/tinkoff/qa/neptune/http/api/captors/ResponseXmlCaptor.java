package ru.tinkoff.qa.neptune.http.api.captors;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.http.HttpResponse;

import static java.io.File.createTempFile;
import static java.nio.charset.Charset.defaultCharset;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;
import static javax.xml.transform.OutputKeys.INDENT;
import static org.apache.commons.io.FileUtils.writeStringToFile;

@Deprecated(forRemoval = true)
public class ResponseXmlCaptor extends FileCaptor<HttpResponse<String>> {

    public ResponseXmlCaptor() {
        super("Response. XML");
    }

    private static Document parse(String xmlString) {
        try {
            var documentBuilderFactory = DocumentBuilderFactory.newInstance();
            var documentBuilder = documentBuilderFactory.newDocumentBuilder();
            var inputSource = new InputSource(new StringReader(xmlString));
            return documentBuilder.parse(inputSource);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public HttpResponse<String> getCaptured(Object toBeCaptured) {
        if (!HttpResponse.class.isAssignableFrom(toBeCaptured.getClass())) {
            return null;
        }

        HttpResponse<?> response = (HttpResponse<?>) toBeCaptured;
        return ofNullable(response.body()).map(o -> {
            if (!String.class.isAssignableFrom(o.getClass())) {
                return null;
            }

            String stringBody = String.valueOf(o);
            if (nonNull(parse(stringBody))) {
                return (HttpResponse<String>) response;
            }

            return null;
        }).orElse(null);
    }

    @Override
    public File getData(HttpResponse<String> caught) {
        var uuid = randomUUID().toString();


        Document xmlDocument;
        if (nonNull(xmlDocument = parse(caught.body()))) {
            var stringWriter = new StringWriter();
            var xmlOutput = new StreamResult(stringWriter);

            var factory = TransformerFactory.newInstance();
            factory.setAttribute("indent-number", 2);

            Transformer transformer;
            try {
                transformer = factory.newTransformer();
            } catch (TransformerConfigurationException e) {
                return null;
            }
            transformer.setOutputProperty(INDENT, "yes");
            try {
                transformer.transform(new DOMSource(xmlDocument), xmlOutput);
            } catch (TransformerException e) {
                return null;
            }

            try {
                var xml = createTempFile("xml_response_body", uuid + ".xml");
                writeStringToFile(xml, xmlOutput.getWriter().toString(),
                        defaultCharset(), true);
                xml.deleteOnExit();
                return xml;
            } catch (IOException e) {
                return null;
            }
        }

        return null;
    }
}
