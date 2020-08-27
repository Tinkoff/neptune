package ru.tinkoff.qa.neptune.http.api.request.body;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper;
import ru.tinkoff.qa.neptune.http.api.properties.mapper.DefaultJsonObjectMapper;
import ru.tinkoff.qa.neptune.http.api.properties.mapper.DefaultXmlObjectMapper;
import ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart;
import ru.tinkoff.qa.neptune.http.api.request.body.url.encoded.FormParameter;
import ru.tinkoff.qa.neptune.http.api.request.body.url.encoded.URLEncodedForm;

import javax.xml.transform.Transformer;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.function.Supplier;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Creates instances of {@link RequestBody}.
 */
public final class RequestBodyFactory {

    private RequestBodyFactory() {
        super();
    }

    /**
     * Creates empty body of http request.
     *
     * @return an instance of {@link RequestBody}
     */
    public static RequestBody<Void> body() {
        return new EmptyBody();
    }


    /**
     * Creates binary body of http request.
     *
     * @param body   is a full binary array
     * @param length is length of a sub-array to send
     * @param offset the offset of the first byte
     * @return an instance of {@link RequestBody}
     */
    public static RequestBody<byte[]> body(byte[] body, int length, int offset) {
        return new ByteArrayBody(body, length, offset);
    }

    /**
     * Creates binary body of http request.
     *
     * @param body is a binary content
     * @return an instance of {@link RequestBody}
     */
    public static RequestBody<byte[]> body(byte[] body) {
        return new ByteArrayBody(body);
    }

    /**
     * Creates file body of http request.
     *
     * @param file is a file to send
     * @return an instance of {@link RequestBody}
     */
    public static RequestBody<File> body(File file) {
        return new FileBody(file);
    }

    /**
     * Creates file body of http request.
     *
     * @param path is a path to a file to send
     * @return an instance of {@link RequestBody}
     */
    public static RequestBody<File> body(Path path) {
        return new FileBody(path);
    }

    /**
     * Creates encoded string body of http request.
     *
     * @param body     is an original string content
     * @param encoding is necessary encoding of of a body
     * @return an instance of {@link RequestBody}
     */
    public static RequestBody<String> body(String body, Charset encoding) {
        return new StringBody(body, encoding);
    }

    /**
     * Creates UTF-8 string body of http request.
     *
     * @param body is an original string content
     * @return an instance of {@link RequestBody}
     */
    public static RequestBody<String> body(String body) {
        return body(body, UTF_8);
    }

    /**
     * Creates a stream body of http request.
     *
     * @param body is an input stream body
     * @return an instance of {@link RequestBody}
     */
    public static RequestBody<InputStream> body(InputStream body) {
        return new StreamBody(body);
    }

    /**
     * Creates a body of http request.
     *
     * @param body is a body of http request. It is considered that an input stream is created
     *             at the moment when http request is sent
     * @return an instance of {@link RequestBody}
     */
    public static RequestBody<Supplier<InputStream>> body(Supplier<InputStream> body) {
        return new StreamSuppliedBody(body);
    }

    /**
     * Creates a body of {@code application/x-www-form-urlencoded} format.
     *
     * @param formParameters array of form parameters
     * @return an instance of {@link RequestBody}
     */
    public static RequestBody<String> body(FormParameter... formParameters) {
        return new URLEncodedForm(formParameters);
    }

    /**
     * Creates a body of http request. W3C {@link org.w3c.dom.Document} is used as string content
     *
     * @param body is a document that is used as a request body
     * @return an instance of {@link RequestBody}
     */
    public static RequestBody<String> body(org.w3c.dom.Document body) {
        return new W3CDocumentBody(body);
    }

    /**
     * Creates a body of http request. W3C {@link org.w3c.dom.Document} is used as string content
     *
     * @param body        is a document that is used as a request body
     * @param transformer is a document transformer
     * @return an instance of {@link RequestBody}
     */
    public static RequestBody<String> body(org.w3c.dom.Document body, Transformer transformer) {
        return new W3CDocumentBody(body, transformer);
    }

    /**
     * Creates a body of http request. Html {@link org.jsoup.nodes.Document body} is used as string content
     *
     * @param body is a document that is used as a request body
     * @return an instance of {@link RequestBody}
     */
    public static RequestBody<String> body(org.jsoup.nodes.Document body) {
        return new JSoupDocumentBody(body);
    }

    /**
     * Creates a body of http request. An object is transformed into json/xml string body.
     *
     * @param mapper is an instance of {@link ObjectMapper} that is used to transform object
     *               into json/xml string
     * @param body   object to be serialized
     * @return an instance of {@link RequestBody}
     */
    public static RequestBody<String> body(ObjectMapper mapper, Object body) {
        return new SerializedBody(mapper, body);
    }

    /**
     * Creates a body of http request. An object is transformed into json/xml string body.
     *
     * @param mapper creates/gets an instance of {@link ObjectMapper} that is used by default to transform object
     *               into json/xml string.
     * @param body   object to be serialized
     * @return an instance of {@link RequestBody}
     * @see DefaultMapper
     * @see DefaultJsonObjectMapper
     * @see DefaultXmlObjectMapper
     */
    public static RequestBody<String> body(DefaultMapper mapper, Object body) {
        return body(mapper.getMapper(), body);
    }

    /**
     * Creates a body of http request. The body consists of several parts.
     *
     * @param boundary  is a separator between parts
     * @param bodyParts parts of a complex body
     * @return an instance of {@link RequestBody}
     */
    public static RequestBody<BodyPart[]> body(String boundary, BodyPart... bodyParts) {
        return new MultiPartBody(boundary, bodyParts);
    }
}
