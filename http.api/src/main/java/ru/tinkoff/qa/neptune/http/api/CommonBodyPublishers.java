package ru.tinkoff.qa.neptune.http.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.net.URLEncoder.encode;
import static java.net.http.HttpRequest.BodyPublishers.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;

/**
 * Creates instances if {@link java.net.http.HttpRequest.BodyPublisher} for more comfort usage
 */
public final class CommonBodyPublishers {

    private CommonBodyPublishers() {
        super();
    }

    /**
     * Returns a request body publisher whose body is the given {@code
     * String}, converted using the {@link StandardCharsets#UTF_8 UTF_8}
     * character set.
     *
     * @param body the String containing the body
     * @return a BodyPublisher
     */
    public static HttpRequest.BodyPublisher stringBody(String body) {
        return ofString(body, UTF_8);
    }

    /**
     * Returns a request body publisher whose body is the given {@code
     * String}, converted using the given character set.
     *
     * @param s the String containing the body
     * @param charset the character set to convert the string to bytes
     * @return a BodyPublisher
     */
    public static HttpRequest.BodyPublisher stringBody(String s, Charset charset) {
        return ofString(s, charset);
    }

    /**
     * A request body publisher that reads its data from an {@link
     * InputStream}. A {@link Supplier} of {@code InputStream} is used in
     * case the request needs to be repeated, as the content is not buffered.
     * The {@code Supplier} may return {@code null} on subsequent attempts,
     * in which case the request fails.
     *
     * @param streamSupplier a Supplier of open InputStreams
     * @return a BodyPublisher
     */
    public static HttpRequest.BodyPublisher streamBody(Supplier<? extends InputStream> streamSupplier) {
        return ofInputStream(streamSupplier);
    }

    /**
     * Returns a request body publisher whose body is the given byte array.
     *
     * @param buf the byte array containing the body
     * @return a BodyPublisher
     */
    public static HttpRequest.BodyPublisher byteArrayBody(byte[] buf) {
        return ofByteArray(buf);
    }

    /**
     * Returns a request body publisher whose body is the content of the
     * given byte array of {@code length} bytes starting from the specified
     * {@code offset}.
     *
     * @param buf the byte array containing the body
     * @param offset the offset of the first byte
     * @param length the number of bytes to use
     * @return a BodyPublisher
     * @throws IndexOutOfBoundsException if the sub-range is defined to be
     *                                   out of bounds
     */
    public static HttpRequest.BodyPublisher byteArrayBody(byte[] buf, int offset, int length) {
        return ofByteArray(buf, offset, length);
    }

    /**
     * A request body publisher that takes data from the contents of a File.
     *
     * <p> Security manager permission checks are performed in this factory
     * method, when the {@code BodyPublisher} is created. Care must be taken
     * that the {@code BodyPublisher} is not shared with untrusted code.
     *
     * @param path the path to the file containing the body
     * @return a BodyPublisher
     * @throws java.io.FileNotFoundException if the path is not found
     * @throws SecurityException if a security manager has been installed
     *          and it denies {@link SecurityManager#checkRead(String)
     *          read access} to the given file
     */
    public static HttpRequest.BodyPublisher fileBody(Path path) throws FileNotFoundException {
        return ofFile(path);
    }

    /**
     * A request body publisher that takes data from an {@code Iterable}
     * of byte arrays. An {@link Iterable} is provided which supplies
     * {@link Iterator} instances. Each attempt to send the request results
     * in one invocation of the {@code Iterable}.
     *
     * @param iter an Iterable of byte arrays
     * @return a BodyPublisher
     */
    public static HttpRequest.BodyPublisher byteArraysBody(Iterable<byte[]> iter) {
        return ofByteArrays(iter);
    }

    /**
     * A request body publisher which sends no request body.
     *
     * @return a BodyPublisher which completes immediately and sends
     *         no request body.
     */
    public static HttpRequest.BodyPublisher empty() {
        return noBody();
    }

    /**
     * Serializes given object to json string for the using it as a request body
     *
     * @param t is an object to be serialized to json string
     * @param builder is used for the serialization
     * @param charset of a resulted request body
     * @param <T> is a type of the object to be serialized
     * @return a BodyPublisher
     */
    public static  <T> HttpRequest.BodyPublisher jsonStringBody(T t, GsonBuilder builder, Charset charset) {
        checkArgument(nonNull(builder), "Json builder should not be a null value");
        checkArgument(nonNull(charset), "Char set should not be a null value");
        return ofString(builder.create().toJson(t), charset);
    }

    /**
     * Serializes given object to json string for the using it as a request body
     *
     * @param t is an object to be serialized to json string
     * @param builder is used for the serialization
     * @param <T> is a type of the object to be serialized
     * @return a BodyPublisher
     */
    public static  <T> HttpRequest.BodyPublisher jsonStringBody(T t, GsonBuilder builder) {
        return jsonStringBody(t, builder, UTF_8);
    }

    /**
     * Serializes given object to json/xml string for the using it as a request body. The serializing is
     * performed by Jackson
     *
     * @param t is an object to be serialized to json string
     * @param mapper is an object mapper
     * @param charset  of a resulted request body
     * @param <T> is a type of the object to be serialized
     * @return a BodyPublisher
     */
    public static  <T> HttpRequest.BodyPublisher serializedStringBody(T t, ObjectMapper mapper, Charset charset) {
        checkArgument(nonNull(t), "Object to be serialized should not be a null value");
        checkArgument(nonNull(mapper), "Object mapper should not be a null value");
        checkArgument(nonNull(charset), "Char set should not be a null value");
        try {
            return ofString(mapper.writeValueAsString(t), charset);
        } catch (JsonProcessingException e) {
           throw new RuntimeException(e);
        }
    }

    /**
     * Serializes given object to json/xml string for the using it as a request body. The serializing is
     * performed by Jackson
     *
     * @param t is an object to be serialized to json string
     * @param mapper is an object mapper
     * @param <T> is a type of the object to be serialized
     * @return a BodyPublisher
     */
    public static <T> HttpRequest.BodyPublisher serializedStringBody(T t, ObjectMapper mapper) {
        return serializedStringBody(t, mapper, UTF_8);
    }

    /**
     * Transforms given xml/html document to string body of a request.
     *
     * @param document is xml/html document to be used
     * @param transformer is a transformer of the document to string value
     * @param charset of a resulted request body
     * @return a BodyPublisher
     */
    public static  HttpRequest.BodyPublisher documentStringBody(Document document,
                                                                Transformer transformer,
                                                                Charset charset) {
        checkArgument(nonNull(document), "Document should not be a null value");
        checkArgument(nonNull(transformer), "Transformer should not be a null value");
        checkArgument(nonNull(charset), "Char set should not be a null value");

        DOMSource domSource = new DOMSource(document);

        var sw = new StringWriter();
        StreamResult sr = new StreamResult(sw);
        try {
            transformer.transform(domSource, sr);
            return ofString(sw.toString(), charset);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Transforms given xml/html document to string body of a request.
     *
     * @param document is xml/html document to be used
     * @param transformer is a transformer of the document to string value
     * @return a BodyPublisher
     */
    public static HttpRequest.BodyPublisher documentStringBody(Document document, Transformer transformer) {
        return documentStringBody(document, transformer, UTF_8);
    }

    /**
     * Transforms given html document to string body of a request.
     *
     * @param document is html document to be used
     * @param charset of a resulted request body
     * @return a BodyPublisher
     */
    public static  HttpRequest.BodyPublisher documentStringBody(org.jsoup.nodes.Document document,
                                                                Charset charset) {
        checkArgument(nonNull(document), "Document should not be a null value");
        checkArgument(nonNull(charset), "Char set should not be a null value");
        return ofString(document.outerHtml(), charset);
    }

    /**
     * Transforms given html document to string body of a request.
     *
     * @param document is html document to be used
     * @return a BodyPublisher
     */
    public static  HttpRequest.BodyPublisher documentStringBody(org.jsoup.nodes.Document document) {
        return documentStringBody(document, UTF_8);
    }

    /**
     * Transforms a map to to string body of {@code application/x-www-form-urlencoded} format
     *
     * @param formParameters is a map where keys are parameter names and values are values of defined parameters
     * @param charset of a resulted request body
     * @return a BodyPublisher
     */
    public static HttpRequest.BodyPublisher formUrlEncodedStringParamsBody(Map<String, String> formParameters, Charset charset) {
        checkArgument(nonNull(formParameters), "Form parameters should not be a null value");
        checkArgument(formParameters.size() > 0, "Should be defined at least one parameter name and its value");
        return ofString(formParameters.entrySet()
                .stream()
                .map(entry -> {
                    try {
                        return format("%s=%s",
                                encode(entry.getKey(), UTF_8.toString()),
                                encode(entry.getValue(), UTF_8.toString()));
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(joining("&")), charset);
    }

    /**
     * Transforms a map to to string body of {@code application/x-www-form-urlencoded} format
     *
     * @param formParameters is a map where keys are parameter names and values are values of defined parameters
     * @return a BodyPublisher
     */
    public static HttpRequest.BodyPublisher formUrlEncodedStringParamsBody(Map<String, String> formParameters) {
        return formUrlEncodedStringParamsBody(formParameters,  UTF_8);
    }
}
