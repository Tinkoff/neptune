package ru.tinkoff.qa.neptune.http.api.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.tinkoff.qa.neptune.http.api.dto.DTObject;
import ru.tinkoff.qa.neptune.http.api.mapper.DefaultBodyMappers;
import ru.tinkoff.qa.neptune.http.api.properties.mapper.DefaultJsonObjectMapper;
import ru.tinkoff.qa.neptune.http.api.properties.mapper.DefaultXmlObjectMapper;
import ru.tinkoff.qa.neptune.http.api.request.body.MultiPartBody;
import ru.tinkoff.qa.neptune.http.api.request.body.RequestBody;
import ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart;

import javax.xml.transform.Transformer;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.net.URI.create;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.http.api.request.body.RequestBodyFactory.body;

public abstract class RequestBuilder implements RequestSettings<RequestBuilder> {
    final HttpRequest.Builder builder;
    private final QueryBuilder queryBuilder = new QueryBuilder();
    final RequestBody<?> body;
    private final TreeMap<String, List<String>> headersMap = new TreeMap<>(CASE_INSENSITIVE_ORDER);
    private final URI endPoint;

    private RequestBuilder(URI endPoint, RequestBody<?> body) {
        checkNotNull(endPoint);
        this.endPoint = endPoint;
        builder = HttpRequest.newBuilder();
        this.body = body;
        defineRequestMethodAndBody();
    }

    private static URI toURI(URL url) {
        checkNotNull(url);
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a builder of a POST http request.
     *
     * @param endPoint is a target endpoint
     * @param body     is a request body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URI endPoint, RequestBody<?> body) {
        checkNotNull(body);
        return new RequestBuilder(endPoint, body) {
            @Override
            void defineRequestMethodAndBody() {
                builder.POST(this.body.createPublisher());
            }
        };
    }

    /**
     * Creates a builder of a POST http request with empty body
     *
     * @param endPoint is a target endpoint
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URI endPoint) {
        return POST(endPoint, body());
    }

    /**
     * Creates a builder of a POST http request with binary body
     *
     * @param endPoint is a target endpoint
     * @param body     is a full binary array
     * @param length   is length of a sub-array to send
     * @param offset   the offset of the first byte
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URI endPoint, byte[] body, int length, int offset) {
        return POST(endPoint, body(body, length, offset));
    }

    /**
     * Creates a builder of a POST http request with binary body
     *
     * @param endPoint is a target endpoint
     * @param body     is a binary array body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URI endPoint, byte[] body) {
        return POST(endPoint, body(body));
    }

    /**
     * Creates a builder of a POST http request with file to send
     *
     * @param endPoint is a target endpoint
     * @param body     is a file to send
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URI endPoint, File body) {
        return POST(endPoint, body(body));
    }

    /**
     * Creates a builder of a POST http request with file to send
     *
     * @param endPoint is a target endpoint
     * @param body     is a path to a file to send
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URI endPoint, Path body) {
        return POST(endPoint, body(body));
    }

    /**
     * Creates a builder of a POST http request with string body
     *
     * @param endPoint is a target endpoint
     * @param body     is an original string content
     * @param encoding is necessary encoding of of a body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URI endPoint, String body, Charset encoding) {
        return POST(endPoint, body(body, encoding));
    }

    /**
     * Creates a builder of a POST http request with UTF-8 string body
     *
     * @param endPoint is a target endpoint
     * @param body     is an original string content
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URI endPoint, String body) {
        return POST(endPoint, body(body));
    }

    /**
     * Creates a builder of a POST http request with stream body
     *
     * @param endPoint is a target endpoint
     * @param body     is an input stream body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URI endPoint, InputStream body) {
        return POST(endPoint, body(body));
    }

    /**
     * Creates a builder of a POST http request with stream body. It is considered that an input stream is created
     * at the moment when http request is sent
     *
     * @param endPoint is a target endpoint
     * @param body     is an input stream body. It is considered that an input stream is created
     *                 at the moment when http request is sent
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URI endPoint, Supplier<InputStream> body) {
        return POST(endPoint, body(body));
    }

    /**
     * Creates a builder of a POST http request with body of {@code application/x-www-form-urlencoded} format.
     *
     * @param endPoint is a target endpoint
     * @param body     is a map of parameter names and values
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URI endPoint, Map<String, String> body) {
        return POST(endPoint, body(body));
    }

    /**
     * Creates a builder of a POST http request. W3C {@link org.w3c.dom.Document} is used as string content
     *
     * @param endPoint is a target endpoint
     * @param body     is a document that is used as a request body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URI endPoint, org.w3c.dom.Document body) {
        return POST(endPoint, body(body));
    }

    /**
     * Creates a builder of a POST http request. W3C {@link org.w3c.dom.Document} is used as string content
     *
     * @param endPoint    is a target endpoint
     * @param body        is a document that is used as a request body
     * @param transformer is a document transformer
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URI endPoint, org.w3c.dom.Document body, Transformer transformer) {
        return POST(endPoint, body(body, transformer));
    }

    /**
     * Creates a builder of a POST http request. Html {@link org.jsoup.nodes.Document body} is used as string content.
     *
     * @param endPoint is a target endpoint
     * @param body     is a document that is used as a request body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URI endPoint, org.jsoup.nodes.Document body) {
        return POST(endPoint, body(body));
    }

    /**
     * Creates a builder of a POST http request. An object is transformed into json/xml string body.
     *
     * @param endPoint is a target endpoint
     * @param body     object to be serialized
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URI endPoint, DTObject body) {
        return POST(endPoint, body(body));
    }

    /**
     * Creates a builder of a POST http request. An object is transformed into json/xml string body.
     *
     * @param endPoint is a target endpoint
     * @param mapper   is an instance of {@link ObjectMapper} that is used to transform object
     *                 into json/xml string
     * @param body     object to be serialized
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URI endPoint, ObjectMapper mapper, Object body) {
        return POST(endPoint, body(mapper, body));
    }

    /**
     * Creates a builder of a POST http request. An object is transformed into json/xml string body.
     *
     * @param endPoint is a target endpoint
     * @param mapper   creates/gets an instance of {@link ObjectMapper} that is used by default to transform object
     *                 into json/xml string.
     * @param body     object to be serialized
     * @return a POST request builder.
     * @see DefaultBodyMappers
     * @see DefaultJsonObjectMapper
     * @see DefaultXmlObjectMapper
     */
    public static RequestBuilder POST(URI endPoint, DefaultBodyMappers mapper, Object body) {
        return POST(endPoint, body(mapper, body));
    }

    /**
     * Creates a builder of a POST http request with multipart body.
     *
     * @param endPoint is a target endpoint
     * @param boundary is a boundary between body parts
     * @param parts    are parts of multipart body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URI endPoint, String boundary, BodyPart... parts) {
        return POST(endPoint, body(boundary, parts));
    }

    /**
     * Creates a builder of a POST http request with multipart body.
     *
     * @param endPoint is a target endpoint
     * @param parts    are parts of multipart body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URI endPoint, BodyPart... parts) {
        return POST(endPoint, body(randomAlphanumeric(15), parts));
    }


    /**
     * Creates a builder of a POST http request.
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a request body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URL endPointUrl, RequestBody<?> body) {
        return POST(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a POST http request with empty body
     *
     * @param endPointUrl is a URL of target endpoint
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URL endPointUrl) {
        return POST(toURI(endPointUrl));
    }

    /**
     * Creates a builder of a POST http request with binary body
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a full binary array
     * @param length      is length of a sub-array to send
     * @param offset      the offset of the first byte
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URL endPointUrl, byte[] body, int length, int offset) {
        return POST(toURI(endPointUrl), body, length, offset);
    }

    /**
     * Creates a builder of a POST http request with binary body
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a binary array body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URL endPointUrl, byte[] body) {
        return POST(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a POST http request with file to send
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a file to send
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URL endPointUrl, File body) {
        return POST(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a POST http request with file to send
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a path to a file to send
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URL endPointUrl, Path body) {
        return POST(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a POST http request with string body
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is an original string content
     * @param encoding    is necessary encoding of of a body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URL endPointUrl, String body, Charset encoding) {
        return POST(toURI(endPointUrl), body, encoding);
    }

    /**
     * Creates a builder of a POST http request with UTF-8 string body
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is an original string content
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URL endPointUrl, String body) {
        return POST(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a POST http request with stream body
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is an input stream body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URL endPointUrl, InputStream body) {
        return POST(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a POST http request with stream body. It is considered that an input stream is created
     * at the moment when http request is sent
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is an input stream body. It is considered that an input stream is created
     *                    at the moment when http request is sent
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URL endPointUrl, Supplier<InputStream> body) {
        return POST(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a POST http request with body of {@code application/x-www-form-urlencoded} format.
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a map of parameter names and values
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URL endPointUrl, Map<String, String> body) {
        return POST(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a POST http request. W3C {@link org.w3c.dom.Document} is used as string content
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a document that is used as a request body\
     * @param transformer is a document transformer
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URL endPointUrl, org.w3c.dom.Document body, Transformer transformer) {
        return POST(toURI(endPointUrl), body, transformer);
    }

    /**
     * Creates a builder of a POST http request. W3C {@link org.w3c.dom.Document} is used as string content
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a document that is used as a request body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URL endPointUrl, org.w3c.dom.Document body) {
        return POST(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a POST http request. Html {@link org.jsoup.nodes.Document body} is used as string content.
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a document that is used as a request body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URL endPointUrl, org.jsoup.nodes.Document body) {
        return POST(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a POST http request. An object is transformed into json/xml string body.
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        object to be serialized
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URL endPointUrl, DTObject body) {
        return POST(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a POST http request. An object is transformed into json/xml string body.
     *
     * @param endPointUrl is a URL of target endpoint
     * @param mapper      is an instance of {@link ObjectMapper} that is used to transform object
     *                    into json/xml string
     * @param body        object to be serialized
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URL endPointUrl, ObjectMapper mapper, Object body) {
        return POST(toURI(endPointUrl), mapper, body);
    }

    /**
     * Creates a builder of a POST http request. An object is transformed into json/xml string body.
     *
     * @param endPointUrl is a URL of target endpoint
     * @param mapper      creates/gets an instance of {@link ObjectMapper} that is used by default to transform object
     *                    into json/xml string.
     * @param body        object to be serialized
     * @return a POST request builder.
     * @see DefaultBodyMappers
     * @see DefaultJsonObjectMapper
     * @see DefaultXmlObjectMapper
     */
    public static RequestBuilder POST(URL endPointUrl, DefaultBodyMappers mapper, Object body) {
        return POST(toURI(endPointUrl), mapper, body);
    }

    /**
     * Creates a builder of a POST http request with multipart body.
     *
     * @param endPointUrl is a URL of target endpoint
     * @param boundary    is a boundary between body parts
     * @param parts       are parts of multipart body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URL endPointUrl, String boundary, BodyPart... parts) {
        return POST(toURI(endPointUrl), boundary, parts);
    }

    /**
     * Creates a builder of a POST http request with multipart body.
     *
     * @param endPointUrl is a URL of target endpoint
     * @param parts       are parts of multipart body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(URL endPointUrl, BodyPart... parts) {
        return POST(toURI(endPointUrl), parts);
    }


    /**
     * Creates a builder of a POST http request.
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a request body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(String uriStr, RequestBody<?> body) {
        return POST(create(uriStr), body);
    }

    /**
     * Creates a builder of a POST http request with empty body
     *
     * @param uriStr is a string value of URI of target endpoint
     * @return a POST request builder.
     */
    public static RequestBuilder POST(String uriStr) {
        return POST(create(uriStr));
    }

    /**
     * Creates a builder of a POST http request with binary body
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a full binary array
     * @param length is length of a sub-array to send
     * @param offset the offset of the first byte
     * @return a POST request builder.
     */
    public static RequestBuilder POST(String uriStr, byte[] body, int length, int offset) {
        return POST(create(uriStr), body, length, offset);
    }

    /**
     * Creates a builder of a POST http request with binary body
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a binary array body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(String uriStr, byte[] body) {
        return POST(create(uriStr), body);
    }

    /**
     * Creates a builder of a POST http request with file to send
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a file to send
     * @return a POST request builder.
     */
    public static RequestBuilder POST(String uriStr, File body) {
        return POST(create(uriStr), body);
    }

    /**
     * Creates a builder of a POST http request with file to send
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a path to a file to send
     * @return a POST request builder.
     */
    public static RequestBuilder POST(String uriStr, Path body) {
        return POST(create(uriStr), body);
    }

    /**
     * Creates a builder of a POST http request with string body
     *
     * @param uriStr   is a string value of URI of target endpoint
     * @param body     is an original string content
     * @param encoding is necessary encoding of of a body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(String uriStr, String body, Charset encoding) {
        return POST(create(uriStr), body, encoding);
    }

    /**
     * Creates a builder of a POST http request with UTF-8 string body
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is an original string content
     * @return a POST request builder.
     */
    public static RequestBuilder POST(String uriStr, String body) {
        return POST(create(uriStr), body);
    }

    /**
     * Creates a builder of a POST http request with stream body
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is an input stream body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(String uriStr, InputStream body) {
        return POST(create(uriStr), body);
    }

    /**
     * Creates a builder of a POST http request with stream body. It is considered that an input stream is created
     * at the moment when http request is sent
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is an input stream body. It is considered that an input stream is created
     *               at the moment when http request is sent
     * @return a POST request builder.
     */
    public static RequestBuilder POST(String uriStr, Supplier<InputStream> body) {
        return POST(create(uriStr), body);
    }

    /**
     * Creates a builder of a POST http request with body of {@code application/x-www-form-urlencoded} format.
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a map of parameter names and values
     * @return a POST request builder.
     */
    public static RequestBuilder POST(String uriStr, Map<String, String> body) {
        return POST(create(uriStr), body);
    }

    /**
     * Creates a builder of a POST http request. W3C {@link org.w3c.dom.Document} is used as string content
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a document that is used as a request body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(String uriStr, org.w3c.dom.Document body) {
        return POST(create(uriStr), body);
    }

    /**
     * Creates a builder of a POST http request. W3C {@link org.w3c.dom.Document} is used as string content
     *
     * @param uriStr      is a string value of URI of target endpoint
     * @param body        is a document that is used as a request body
     * @param transformer is a document transformer
     * @return a POST request builder.
     */
    public static RequestBuilder POST(String uriStr, org.w3c.dom.Document body, Transformer transformer) {
        return POST(create(uriStr), body, transformer);
    }

    /**
     * Creates a builder of a POST http request. Html {@link org.jsoup.nodes.Document body} is used as string content.
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a document that is used as a request body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(String uriStr, org.jsoup.nodes.Document body) {
        return POST(create(uriStr), body);
    }

    /**
     * Creates a builder of a POST http request. An object is transformed into json/xml string body.
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   object to be serialized
     * @return a POST request builder.
     */
    public static RequestBuilder POST(String uriStr, DTObject body) {
        return POST(create(uriStr), body);
    }

    /**
     * Creates a builder of a POST http request. An object is transformed into json/xml string body.
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param mapper is an instance of {@link ObjectMapper} that is used to transform object
     *               into json/xml string
     * @param body   object to be serialized
     * @return a POST request builder.
     */
    public static RequestBuilder POST(String uriStr, ObjectMapper mapper, Object body) {
        return POST(create(uriStr), mapper, body);
    }

    /**
     * Creates a builder of a POST http request. An object is transformed into json/xml string body.
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param mapper creates/gets an instance of {@link ObjectMapper} that is used by default to transform object
     *               into json/xml string.
     * @param body   object to be serialized
     * @return a POST request builder.
     * @see DefaultBodyMappers
     * @see DefaultJsonObjectMapper
     * @see DefaultXmlObjectMapper
     */
    public static RequestBuilder POST(String uriStr, DefaultBodyMappers mapper, Object body) {
        return POST(create(uriStr), mapper, body);
    }

    /**
     * Creates a builder of a POST http request with multipart body.
     *
     * @param uriStr   is a string value of URI of target endpoint
     * @param boundary is a boundary between body parts
     * @param parts    are parts of multipart body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(String uriStr, String boundary, BodyPart... parts) {
        return POST(create(uriStr), boundary, parts);
    }

    /**
     * Creates a builder of a POST http request with multipart body.
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param parts  are parts of multipart body
     * @return a POST request builder.
     */
    public static RequestBuilder POST(String uriStr, BodyPart... parts) {
        return POST(create(uriStr), parts);
    }


    /**
     * Creates a builder of a GET http request.
     *
     * @param endPoint is a target endpoint
     * @return a GET request builder.
     */
    public static RequestBuilder GET(URI endPoint) {
        return new RequestBuilder(endPoint, null) {
            @Override
            void defineRequestMethodAndBody() {
                builder.GET();
            }
        };
    }

    /**
     * Creates a builder of a GET http request.
     *
     * @param endPointUrl is a URL of target endpoint
     * @return a GET request builder.
     */
    public static RequestBuilder GET(URL endPointUrl) {
        return GET(toURI(endPointUrl));
    }

    /**
     * Creates a builder of a GET http request.
     *
     * @param uriStr is a string value of URI of target endpoint
     * @return a GET request builder.
     */
    public static RequestBuilder GET(String uriStr) {
        return GET(create(uriStr));
    }


    /**
     * Creates a builder of a DELETE http request.
     *
     * @param endPoint is a target endpoint
     * @return a DELETE request builder.
     */
    public static RequestBuilder DELETE(URI endPoint) {
        return new RequestBuilder(endPoint, null) {
            @Override
            void defineRequestMethodAndBody() {
                builder.DELETE();
            }
        };
    }

    /**
     * Creates a builder of a DELETE http request.
     *
     * @param endPointUrl is a URL of target endpoint
     * @return a DELETE request builder.
     */
    public static RequestBuilder DELETE(URL endPointUrl) {
        return DELETE(toURI(endPointUrl));
    }

    /**
     * ------------
     * Creates a builder of a DELETE http request.
     *
     * @param uriStr is a string value of URI of target endpoint
     * @return a DELETE request builder.
     */
    public static RequestBuilder DELETE(String uriStr) {
        return DELETE(create(uriStr));
    }


    /**
     * Creates a builder of a PUT http request.
     *
     * @param endPoint is a target endpoint
     * @param body     is a request body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URI endPoint, RequestBody<?> body) {
        return new RequestBuilder(endPoint, body) {
            @Override
            void defineRequestMethodAndBody() {
                builder.PUT(this.body.createPublisher());
            }
        };
    }

    /**
     * Creates a builder of a PUT http request with empty body
     *
     * @param endPoint is a target endpoint
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URI endPoint) {
        return PUT(endPoint, body());
    }

    /**
     * Creates a builder of a PUT http request with binary body
     *
     * @param endPoint is a target endpoint
     * @param body     is a full binary array
     * @param length   is length of a sub-array to send
     * @param offset   the offset of the first byte
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URI endPoint, byte[] body, int length, int offset) {
        return PUT(endPoint, body(body, length, offset));
    }

    /**
     * Creates a builder of a PUT http request with binary body
     *
     * @param endPoint is a target endpoint
     * @param body     is a binary array body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URI endPoint, byte[] body) {
        return PUT(endPoint, body(body));
    }

    /**
     * Creates a builder of a PUT http request with file to send
     *
     * @param endPoint is a target endpoint
     * @param body     is a file to send
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URI endPoint, File body) {
        return PUT(endPoint, body(body));
    }

    /**
     * Creates a builder of a PUT http request with file to send
     *
     * @param endPoint is a target endpoint
     * @param body     is a path to a file to send
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URI endPoint, Path body) {
        return PUT(endPoint, body(body));
    }

    /**
     * Creates a builder of a PUT http request with string body
     *
     * @param endPoint is a target endpoint
     * @param body     is an original string content
     * @param encoding is necessary encoding of of a body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URI endPoint, String body, Charset encoding) {
        return PUT(endPoint, body(body, encoding));
    }

    /**
     * Creates a builder of a PUT http request with UTF-8 string body
     *
     * @param endPoint is a target endpoint
     * @param body     is an original string content
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URI endPoint, String body) {
        return PUT(endPoint, body(body));
    }

    /**
     * Creates a builder of a PUT http request with stream body
     *
     * @param endPoint is a target endpoint
     * @param body     is an input stream body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URI endPoint, InputStream body) {
        return PUT(endPoint, body(body));
    }

    /**
     * Creates a builder of a PUT http request with stream body. It is considered that an input stream is created
     * at the moment when http request is sent
     *
     * @param endPoint is a target endpoint
     * @param body     is an input stream body. It is considered that an input stream is created
     *                 at the moment when http request is sent
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URI endPoint, Supplier<InputStream> body) {
        return PUT(endPoint, body(body));
    }

    /**
     * Creates a builder of a PUT http request with body of {@code application/x-www-form-urlencoded} format.
     *
     * @param endPoint is a target endpoint
     * @param body     is a map of parameter names and values
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URI endPoint, Map<String, String> body) {
        return PUT(endPoint, body(body));
    }

    /**
     * Creates a builder of a PUT http request. W3C {@link org.w3c.dom.Document} is used as string content
     *
     * @param endPoint is a target endpoint
     * @param body     is a document that is used as a request body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URI endPoint, org.w3c.dom.Document body) {
        return PUT(endPoint, body(body));
    }

    /**
     * Creates a builder of a PUT http request. W3C {@link org.w3c.dom.Document} is used as string content
     *
     * @param endPoint    is a target endpoint
     * @param body        is a document that is used as a request body
     * @param transformer is a document transformer
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URI endPoint, org.w3c.dom.Document body, Transformer transformer) {
        return PUT(endPoint, body(body, transformer));
    }

    /**
     * Creates a builder of a PUT http request. Html {@link org.jsoup.nodes.Document body} is used as string content.
     *
     * @param endPoint is a target endpoint
     * @param body     is a document that is used as a request body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URI endPoint, org.jsoup.nodes.Document body) {
        return PUT(endPoint, body(body));
    }

    /**
     * Creates a builder of a PUT http request. An object is transformed into json/xml string body.
     *
     * @param endPoint is a target endpoint
     * @param body     object to be serialized
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URI endPoint, DTObject body) {
        return PUT(endPoint, body(body));
    }

    /**
     * Creates a builder of a PUT http request. An object is transformed into json/xml string body.
     *
     * @param endPoint is a target endpoint
     * @param mapper   is an instance of {@link ObjectMapper} that is used to transform object
     *                 into json/xml string
     * @param body     object to be serialized
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URI endPoint, ObjectMapper mapper, Object body) {
        return PUT(endPoint, body(mapper, body));
    }

    /**
     * Creates a builder of a PUT http request. An object is transformed into json/xml string body.
     *
     * @param endPoint is a target endpoint
     * @param mapper   creates/gets an instance of {@link ObjectMapper} that is used by default to transform object
     *                 into json/xml string.
     * @param body     object to be serialized
     * @return a PUT request builder.
     * @see DefaultBodyMappers
     * @see DefaultJsonObjectMapper
     * @see DefaultXmlObjectMapper
     */
    public static RequestBuilder PUT(URI endPoint, DefaultBodyMappers mapper, Object body) {
        return PUT(endPoint, body(mapper, body));
    }

    /**
     * Creates a builder of a PUT http request with multipart body.
     *
     * @param endPoint is a target endpoint
     * @param boundary is a boundary between body parts
     * @param parts    are parts of multipart body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URI endPoint, String boundary, BodyPart... parts) {
        return PUT(endPoint, body(boundary, parts));
    }

    /**
     * Creates a builder of a PUT http request with multipart body.
     *
     * @param endPoint is a target endpoint
     * @param parts    are parts of multipart body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URI endPoint, BodyPart... parts) {
        return PUT(endPoint, body(randomAlphanumeric(15), parts));
    }


    /**
     * Creates a builder of a PUT http request.
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a request body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URL endPointUrl, RequestBody<?> body) {
        return PUT(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a PUT http request with empty body
     *
     * @param endPointUrl is a URL of target endpoint
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URL endPointUrl) {
        return PUT(toURI(endPointUrl));
    }

    /**
     * Creates a builder of a PUT http request with binary body
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a full binary array
     * @param length      is length of a sub-array to send
     * @param offset      the offset of the first byte
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URL endPointUrl, byte[] body, int length, int offset) {
        return PUT(toURI(endPointUrl), body, length, offset);
    }

    /**
     * Creates a builder of a PUT http request with binary body
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a binary array body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URL endPointUrl, byte[] body) {
        return PUT(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a PUT http request with file to send
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a file to send
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URL endPointUrl, File body) {
        return PUT(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a PUT http request with file to send
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a path to a file to send
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URL endPointUrl, Path body) {
        return PUT(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a PUT http request with string body
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is an original string content
     * @param encoding    is necessary encoding of of a body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URL endPointUrl, String body, Charset encoding) {
        return PUT(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a PUT http request with UTF-8 string body
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is an original string content
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URL endPointUrl, String body) {
        return PUT(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a PUT http request with stream body
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is an input stream body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URL endPointUrl, InputStream body) {
        return PUT(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a PUT http request with stream body. It is considered that an input stream is created
     * at the moment when http request is sent
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is an input stream body. It is considered that an input stream is created
     *                    at the moment when http request is sent
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URL endPointUrl, Supplier<InputStream> body) {
        return PUT(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a PUT http request with body of {@code application/x-www-form-urlencoded} format.
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a map of parameter names and values
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URL endPointUrl, Map<String, String> body) {
        return PUT(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a PUT http request. W3C {@link org.w3c.dom.Document} is used as string content
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a document that is used as a request body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URL endPointUrl, org.w3c.dom.Document body) {
        return PUT(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a PUT http request. W3C {@link org.w3c.dom.Document} is used as string content
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a document that is used as a request body
     * @param transformer is a document transformer
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URL endPointUrl, org.w3c.dom.Document body, Transformer transformer) {
        return PUT(toURI(endPointUrl), body, transformer);
    }

    /**
     * Creates a builder of a PUT http request. Html {@link org.jsoup.nodes.Document body} is used as string content.
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a document that is used as a request body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URL endPointUrl, org.jsoup.nodes.Document body) {
        return PUT(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a PUT http request. An object is transformed into json/xml string body.
     *
     * @param endPointUrl is a URL of target endpoint
     * @param body        object to be serialized
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URL endPointUrl, DTObject body) {
        return PUT(toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a PUT http request. An object is transformed into json/xml string body.
     *
     * @param endPointUrl is a URL of target endpoint
     * @param mapper      is an instance of {@link ObjectMapper} that is used to transform object
     *                    into json/xml string
     * @param body        object to be serialized
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URL endPointUrl, ObjectMapper mapper, Object body) {
        return PUT(toURI(endPointUrl), mapper, body);
    }

    /**
     * Creates a builder of a PUT http request. An object is transformed into json/xml string body.
     *
     * @param endPointUrl is a URL of target endpoint
     * @param mapper      creates/gets an instance of {@link ObjectMapper} that is used by default to transform object
     *                    into json/xml string.
     * @param body        object to be serialized
     * @return a PUT request builder.
     * @see DefaultBodyMappers
     * @see DefaultJsonObjectMapper
     * @see DefaultXmlObjectMapper
     */
    public static RequestBuilder PUT(URL endPointUrl, DefaultBodyMappers mapper, Object body) {
        return PUT(toURI(endPointUrl), mapper, body);
    }

    /**
     * Creates a builder of a PUT http request with multipart body.
     *
     * @param endPointUrl is a URL of target endpoint
     * @param boundary    is a boundary between body parts
     * @param parts       are parts of multipart body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URL endPointUrl, String boundary, BodyPart... parts) {
        return PUT(toURI(endPointUrl), boundary, parts);
    }

    /**
     * Creates a builder of a PUT http request with multipart body.
     *
     * @param endPointUrl is a URL of target endpoint
     * @param parts       are parts of multipart body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(URL endPointUrl, BodyPart... parts) {
        return PUT(toURI(endPointUrl), parts);
    }


    /**
     * Creates a builder of a PUT http request.
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a request body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(String uriStr, RequestBody<?> body) {
        return PUT(create(uriStr), body);
    }

    /**
     * Creates a builder of a PUT http request with empty body
     *
     * @param uriStr is a string value of URI of target endpoint
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(String uriStr) {
        return PUT(create(uriStr));
    }

    /**
     * Creates a builder of a PUT http request with binary body
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a full binary array
     * @param length is length of a sub-array to send
     * @param offset the offset of the first byte
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(String uriStr, byte[] body, int length, int offset) {
        return PUT(create(uriStr), body, length, offset);
    }

    /**
     * Creates a builder of a PUT http request with binary body
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a binary array body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(String uriStr, byte[] body) {
        return PUT(create(uriStr), body);
    }

    /**
     * Creates a builder of a PUT http request with file to send
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a file to send
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(String uriStr, File body) {
        return PUT(create(uriStr), body);
    }

    /**
     * Creates a builder of a PUT http request with file to send
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a path to a file to send
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(String uriStr, Path body) {
        return PUT(create(uriStr), body);
    }

    /**
     * Creates a builder of a PUT http request with string body
     *
     * @param uriStr   is a string value of URI of target endpoint
     * @param body     is an original string content
     * @param encoding is necessary encoding of of a body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(String uriStr, String body, Charset encoding) {
        return PUT(create(uriStr), body, encoding);
    }

    /**
     * Creates a builder of a PUT http request with UTF-8 string body
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is an original string content
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(String uriStr, String body) {
        return PUT(create(uriStr), body);
    }

    /**
     * Creates a builder of a PUT http request with stream body
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is an input stream body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(String uriStr, InputStream body) {
        return PUT(create(uriStr), body);
    }

    /**
     * Creates a builder of a PUT http request with stream body. It is considered that an input stream is created
     * at the moment when http request is sent
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is an input stream body. It is considered that an input stream is created
     *               at the moment when http request is sent
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(String uriStr, Supplier<InputStream> body) {
        return PUT(create(uriStr), body);
    }

    /**
     * Creates a builder of a PUT http request with body of {@code application/x-www-form-urlencoded} format.
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a map of parameter names and values
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(String uriStr, Map<String, String> body) {
        return PUT(create(uriStr), body);
    }

    /**
     * Creates a builder of a PUT http request. W3C {@link org.w3c.dom.Document} is used as string content
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a document that is used as a request body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(String uriStr, org.w3c.dom.Document body) {
        return PUT(create(uriStr), body);
    }

    /**
     * Creates a builder of a PUT http request. W3C {@link org.w3c.dom.Document} is used as string content
     *
     * @param uriStr      is a string value of URI of target endpoint
     * @param body        is a document that is used as a request body
     * @param transformer is a document transformer
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(String uriStr, org.w3c.dom.Document body, Transformer transformer) {
        return PUT(create(uriStr), body, transformer);
    }

    /**
     * Creates a builder of a PUT http request. Html {@link org.jsoup.nodes.Document body} is used as string content.
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a document that is used as a request body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(String uriStr, org.jsoup.nodes.Document body) {
        return PUT(create(uriStr), body);
    }

    /**
     * Creates a builder of a PUT http request. An object is transformed into json/xml string body.
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param body   object to be serialized
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(String uriStr, DTObject body) {
        return PUT(create(uriStr), body);
    }

    /**
     * Creates a builder of a PUT http request. An object is transformed into json/xml string body.
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param mapper is an instance of {@link ObjectMapper} that is used to transform object
     *               into json/xml string
     * @param body   object to be serialized
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(String uriStr, ObjectMapper mapper, Object body) {
        return PUT(create(uriStr), mapper, body);
    }

    /**
     * Creates a builder of a PUT http request. An object is transformed into json/xml string body.
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param mapper creates/gets an instance of {@link ObjectMapper} that is used by default to transform object
     *               into json/xml string.
     * @param body   object to be serialized
     * @return a PUT request builder.
     * @see DefaultBodyMappers
     * @see DefaultJsonObjectMapper
     * @see DefaultXmlObjectMapper
     */
    public static RequestBuilder PUT(String uriStr, DefaultBodyMappers mapper, Object body) {
        return PUT(create(uriStr), mapper, body);
    }

    /**
     * Creates a builder of a PUT http request with multipart body.
     *
     * @param uriStr   is a string value of URI of target endpoint
     * @param boundary is a boundary between body parts
     * @param parts    are parts of multipart body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(String uriStr, String boundary, BodyPart... parts) {
        return PUT(create(uriStr), boundary, parts);
    }

    /**
     * Creates a builder of a PUT http request with multipart body.
     *
     * @param uriStr is a string value of URI of target endpoint
     * @param parts  are parts of multipart body
     * @return a PUT request builder.
     */
    public static RequestBuilder PUT(String uriStr, BodyPart... parts) {
        return PUT(create(uriStr), parts);
    }


    /**
     * Creates a builder of a fluent http request.
     *
     * @param method   is a name of http method
     * @param endPoint is a target endpoint
     * @param body     is a request body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URI endPoint, RequestBody<?> body) {
        checkNotNull(body);
        return new RequestBuilder(endPoint, body) {
            @Override
            void defineRequestMethodAndBody() {
                checkArgument(isNotBlank(method));
                builder.method(method, this.body.createPublisher());
            }
        };
    }

    /**
     * Creates a builder of a fluent http request with empty body
     *
     * @param method   is a name of http method
     * @param endPoint is a target endpoint
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URI endPoint) {
        return METHOD(method, endPoint, body());
    }

    /**
     * Creates a builder of a fluent http request with binary body
     *
     * @param method   is a name of http method
     * @param endPoint is a target endpoint
     * @param body     is a full binary array
     * @param length   is length of a sub-array to send
     * @param offset   the offset of the first byte
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URI endPoint, byte[] body, int length, int offset) {
        return METHOD(method, endPoint, body(body, length, offset));
    }

    /**
     * Creates a builder of a fluent http request with binary body
     *
     * @param method   is a name of http method
     * @param endPoint is a target endpoint
     * @param body     is a binary array body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URI endPoint, byte[] body) {
        return METHOD(method, endPoint, body(body));
    }

    /**
     * Creates a builder of a fluent http request with file to send
     *
     * @param method   is a name of http method
     * @param endPoint is a target endpoint
     * @param body     is a file to send
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URI endPoint, File body) {
        return METHOD(method, endPoint, body(body));
    }

    /**
     * Creates a builder of a fluent http request with file to send
     *
     * @param method   is a name of http method
     * @param endPoint is a target endpoint
     * @param body     is a path to a file to send
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URI endPoint, Path body) {
        return METHOD(method, endPoint, body(body));
    }

    /**
     * Creates a builder of a fluent http request with string body
     *
     * @param method   is a name of http method
     * @param endPoint is a target endpoint
     * @param body     is an original string content
     * @param encoding is necessary encoding of of a body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URI endPoint, String body, Charset encoding) {
        return METHOD(method, endPoint, body(body, encoding));
    }

    /**
     * Creates a builder of a fluent http request with UTF-8 string body
     *
     * @param method   is a name of http method
     * @param endPoint is a target endpoint
     * @param body     is an original string content
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URI endPoint, String body) {
        return METHOD(method, endPoint, body(body));
    }

    /**
     * Creates a builder of a fluent http request with stream body
     *
     * @param method   is a name of http method
     * @param endPoint is a target endpoint
     * @param body     is an input stream body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URI endPoint, InputStream body) {
        return METHOD(method, endPoint, body(body));
    }

    /**
     * Creates a builder of a fluent http request with stream body. It is considered that an input stream is created
     * at the moment when http request is sent
     *
     * @param method   is a name of http method
     * @param endPoint is a target endpoint
     * @param body     is an input stream body. It is considered that an input stream is created
     *                 at the moment when http request is sent
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URI endPoint, Supplier<InputStream> body) {
        return METHOD(method, endPoint, body(body));
    }

    /**
     * Creates a builder of a fluent http request with body of {@code application/x-www-form-urlencoded} format.
     *
     * @param method   is a name of http method
     * @param endPoint is a target endpoint
     * @param body     is a map of parameter names and values
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URI endPoint, Map<String, String> body) {
        return METHOD(method, endPoint, body(body));
    }

    /**
     * Creates a builder of a fluent http request. W3C {@link org.w3c.dom.Document} is used as string content
     *
     * @param method   is a name of http method
     * @param endPoint is a target endpoint
     * @param body     is a document that is used as a request body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URI endPoint, org.w3c.dom.Document body) {
        return METHOD(method, endPoint, body(body));
    }

    /**
     * Creates a builder of a fluent http request. W3C {@link org.w3c.dom.Document} is used as string content
     *
     * @param method      is a name of http method
     * @param endPoint    is a target endpoint
     * @param body        is a document that is used as a request body
     * @param transformer is a document transformer
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URI endPoint, org.w3c.dom.Document body, Transformer transformer) {
        return METHOD(method, endPoint, body(body, transformer));
    }


    /**
     * Creates a builder of a fluent http request. Html {@link org.jsoup.nodes.Document body} is used as string content.
     *
     * @param method   is a name of http method
     * @param endPoint is a target endpoint
     * @param body     is a document that is used as a request body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URI endPoint, org.jsoup.nodes.Document body) {
        return METHOD(method, endPoint, body(body));
    }

    /**
     * Creates a builder of a fluent http request. An object is transformed into json/xml string body.
     *
     * @param method   is a name of http method
     * @param endPoint is a target endpoint
     * @param body     object to be serialized
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URI endPoint, DTObject body) {
        return METHOD(method, endPoint, body(body));
    }

    /**
     * Creates a builder of a fluent http request. An object is transformed into json/xml string body.
     *
     * @param method   is a name of http method
     * @param endPoint is a target endpoint
     * @param mapper   is an instance of {@link ObjectMapper} that is used to transform object
     *                 into json/xml string
     * @param body     object to be serialized
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URI endPoint, ObjectMapper mapper, Object body) {
        return METHOD(method, endPoint, body(mapper, body));
    }

    /**
     * Creates a builder of a fluent http request. An object is transformed into json/xml string body.
     *
     * @param method   is a name of http method
     * @param endPoint is a target endpoint
     * @param mapper   creates/gets an instance of {@link ObjectMapper} that is used by default to transform object
     *                 into json/xml string.
     * @param body     object to be serialized
     * @return a fluent http request builder.
     * @see DefaultBodyMappers
     * @see DefaultJsonObjectMapper
     * @see DefaultXmlObjectMapper
     */
    public static RequestBuilder METHOD(String method, URI endPoint, DefaultBodyMappers mapper, Object body) {
        return METHOD(method, endPoint, body(mapper, body));
    }

    /**
     * Creates a builder of a fluent http request with multipart body.
     *
     * @param method   is a name of http method
     * @param endPoint is a target endpoint
     * @param boundary is a boundary between body parts
     * @param parts    are parts of multipart body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URI endPoint, String boundary, BodyPart... parts) {
        return METHOD(method, endPoint, body(boundary, parts));
    }

    /**
     * Creates a builder of a fluent http request with multipart body.
     *
     * @param method   is a name of http method
     * @param endPoint is a target endpoint
     * @param parts    are parts of multipart body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URI endPoint, BodyPart... parts) {
        return METHOD(method, endPoint, body(randomAlphanumeric(15), parts));
    }


    /**
     * Creates a builder of a fluent http request.
     *
     * @param method      is a name of http method
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a request body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URL endPointUrl, RequestBody<?> body) {
        return METHOD(method, toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a fluent http request with empty body
     *
     * @param method      is a name of http method
     * @param endPointUrl is a URL of target endpoint
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URL endPointUrl) {
        return METHOD(method, toURI(endPointUrl));
    }

    /**
     * Creates a builder of a fluent http request with binary body
     *
     * @param method      is a name of http method
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a full binary array
     * @param length      is length of a sub-array to send
     * @param offset      the offset of the first byte
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URL endPointUrl, byte[] body, int length, int offset) {
        return METHOD(method, toURI(endPointUrl), body, length, offset);
    }

    /**
     * Creates a builder of a fluent http request with binary body
     *
     * @param method      is a name of http method
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a binary array body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URL endPointUrl, byte[] body) {
        return METHOD(method, toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a fluent http request with file to send
     *
     * @param method      is a name of http method
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a file to send
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URL endPointUrl, File body) {
        return METHOD(method, toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a fluent http request with file to send
     *
     * @param method      is a name of http method
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a path to a file to send
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URL endPointUrl, Path body) {
        return METHOD(method, toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a fluent http request with string body
     *
     * @param method      is a name of http method
     * @param endPointUrl is a URL of target endpoint
     * @param body        is an original string content
     * @param encoding    is necessary encoding of of a body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URL endPointUrl, String body, Charset encoding) {
        return METHOD(method, toURI(endPointUrl), body, encoding);
    }

    /**
     * Creates a builder of a fluent http request with UTF-8 string body
     *
     * @param method      is a name of http method
     * @param endPointUrl is a URL of target endpoint
     * @param body        is an original string content
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URL endPointUrl, String body) {
        return METHOD(method, toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a fluent http request with stream body
     *
     * @param method      is a name of http method
     * @param endPointUrl is a URL of target endpoint
     * @param body        is an input stream body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URL endPointUrl, InputStream body) {
        return METHOD(method, toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a fluent http request with stream body. It is considered that an input stream is created
     * at the moment when http request is sent
     *
     * @param method      is a name of http method
     * @param endPointUrl is a URL of target endpoint
     * @param body        is an input stream body. It is considered that an input stream is created
     *                    at the moment when http request is sent
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URL endPointUrl, Supplier<InputStream> body) {
        return METHOD(method, toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a fluent http request with body of {@code application/x-www-form-urlencoded} format.
     *
     * @param method      is a name of http method
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a map of parameter names and values
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URL endPointUrl, Map<String, String> body) {
        return METHOD(method, toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a fluent http request. W3C {@link org.w3c.dom.Document} is used as string content
     *
     * @param method      is a name of http method
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a document that is used as a request body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URL endPointUrl, org.w3c.dom.Document body) {
        return METHOD(method, toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a fluent http request. W3C {@link org.w3c.dom.Document} is used as string content
     *
     * @param method      is a name of http method
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a document that is used as a request body
     * @param transformer is a document transformer
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URL endPointUrl, org.w3c.dom.Document body, Transformer transformer) {
        return METHOD(method, toURI(endPointUrl), body, transformer);
    }

    /**
     * Creates a builder of a fluent http request. Html {@link org.jsoup.nodes.Document body} is used as string content.
     *
     * @param method      is a name of http method
     * @param endPointUrl is a URL of target endpoint
     * @param body        is a document that is used as a request body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URL endPointUrl, org.jsoup.nodes.Document body) {
        return METHOD(method, toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a fluent http request. An object is transformed into json/xml string body.
     *
     * @param method      is a name of http method
     * @param endPointUrl is a URL of target endpoint
     * @param body        object to be serialized
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URL endPointUrl, DTObject body) {
        return METHOD(method, toURI(endPointUrl), body);
    }

    /**
     * Creates a builder of a fluent http request. An object is transformed into json/xml string body.
     *
     * @param method      is a name of http method
     * @param endPointUrl is a URL of target endpoint
     * @param mapper      is an instance of {@link ObjectMapper} that is used to transform object
     *                    into json/xml string
     * @param body        object to be serialized
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URL endPointUrl, ObjectMapper mapper, Object body) {
        return METHOD(method, toURI(endPointUrl), mapper, body);
    }

    /**
     * Creates a builder of a fluent http request. An object is transformed into json/xml string body.
     *
     * @param method      is a name of http method
     * @param endPointUrl is a URL of target endpoint
     * @param mapper      creates/gets an instance of {@link ObjectMapper} that is used by default to transform object
     *                    into json/xml string.
     * @param body        object to be serialized
     * @return a fluent http request builder.
     * @see DefaultBodyMappers
     * @see DefaultJsonObjectMapper
     * @see DefaultXmlObjectMapper
     */
    public static RequestBuilder METHOD(String method, URL endPointUrl, DefaultBodyMappers mapper, Object body) {
        return METHOD(method, toURI(endPointUrl), mapper, body);
    }

    /**
     * Creates a builder of a fluent http request with multipart body.
     *
     * @param method      is a name of http method
     * @param endPointUrl is a URL of target endpoint
     * @param boundary    is a boundary between body parts
     * @param parts       are parts of multipart body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URL endPointUrl, String boundary, BodyPart... parts) {
        return METHOD(method, toURI(endPointUrl), boundary, parts);
    }

    /**
     * Creates a builder of a fluent http request with multipart body.
     *
     * @param method      is a name of http method
     * @param endPointUrl is a URL of target endpoint
     * @param parts       are parts of multipart body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, URL endPointUrl, BodyPart... parts) {
        return METHOD(method, toURI(endPointUrl), parts);
    }


    /**
     * Creates a builder of a fluent http request.
     *
     * @param method is a name of http method
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a request body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, String uriStr, RequestBody<?> body) {
        return METHOD(method, create(uriStr), body);
    }

    /**
     * Creates a builder of a fluent http request with empty body
     *
     * @param method is a name of http method
     * @param uriStr is a string value of URI of target endpoint
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, String uriStr) {
        return METHOD(method, create(uriStr));
    }

    /**
     * Creates a builder of a fluent http request with binary body
     *
     * @param method is a name of http method
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a full binary array
     * @param length is length of a sub-array to send
     * @param offset the offset of the first byte
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, String uriStr, byte[] body, int length, int offset) {
        return METHOD(method, create(uriStr), body, length, offset);
    }

    /**
     * Creates a builder of a fluent http request with binary body
     *
     * @param method is a name of http method
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a binary array body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, String uriStr, byte[] body) {
        return METHOD(method, create(uriStr), body);
    }

    /**
     * Creates a builder of a fluent http request with file to send
     *
     * @param method is a name of http method
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a file to send
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, String uriStr, File body) {
        return METHOD(method, create(uriStr), body);
    }

    /**
     * Creates a builder of a fluent http request with file to send
     *
     * @param method is a name of http method
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a path to a file to send
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, String uriStr, Path body) {
        return METHOD(method, create(uriStr), body);
    }

    /**
     * Creates a builder of a fluent http request with string body
     *
     * @param method   is a name of http method
     * @param uriStr   is a string value of URI of target endpoint
     * @param body     is an original string content
     * @param encoding is necessary encoding of of a body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, String uriStr, String body, Charset encoding) {
        return METHOD(method, create(uriStr), body, encoding);
    }

    /**
     * Creates a builder of a fluent http request with UTF-8 string body
     *
     * @param method is a name of http method
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is an original string content
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, String uriStr, String body) {
        return METHOD(method, create(uriStr), body);
    }

    /**
     * Creates a builder of a fluent http request with stream body
     *
     * @param method is a name of http method
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is an input stream body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, String uriStr, InputStream body) {
        return METHOD(method, create(uriStr), body);
    }

    /**
     * Creates a builder of a fluent http request with stream body. It is considered that an input stream is created
     * at the moment when http request is sent
     *
     * @param method is a name of http method
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is an input stream body. It is considered that an input stream is created
     *               at the moment when http request is sent
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, String uriStr, Supplier<InputStream> body) {
        return METHOD(method, create(uriStr), body);
    }

    /**
     * Creates a builder of a fluent http request with body of {@code application/x-www-form-urlencoded} format.
     *
     * @param method is a name of http method
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a map of parameter names and values
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, String uriStr, Map<String, String> body) {
        return METHOD(method, create(uriStr), body);
    }

    /**
     * Creates a builder of a fluent http request. W3C {@link org.w3c.dom.Document} is used as string content
     *
     * @param method is a name of http method
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a document that is used as a request body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, String uriStr, org.w3c.dom.Document body) {
        return METHOD(method, create(uriStr), body);
    }

    /**
     * Creates a builder of a fluent http request. W3C {@link org.w3c.dom.Document} is used as string content
     *
     * @param method      is a name of http method
     * @param uriStr      is a string value of URI of target endpoint
     * @param body        is a document that is used as a request body
     * @param transformer is a document transformer
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, String uriStr, org.w3c.dom.Document body, Transformer transformer) {
        return METHOD(method, create(uriStr), body, transformer);
    }

    /**
     * Creates a builder of a fluent http request. Html {@link org.jsoup.nodes.Document body} is used as string content.
     *
     * @param method is a name of http method
     * @param uriStr is a string value of URI of target endpoint
     * @param body   is a document that is used as a request body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, String uriStr, org.jsoup.nodes.Document body) {
        return METHOD(method, create(uriStr), body);
    }

    /**
     * Creates a builder of a fluent http request. An object is transformed into json/xml string body.
     *
     * @param method is a name of http method
     * @param uriStr is a string value of URI of target endpoint
     * @param body   object to be serialized
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, String uriStr, DTObject body) {
        return METHOD(method, create(uriStr), body);
    }

    /**
     * Creates a builder of a fluent http request. An object is transformed into json/xml string body.
     *
     * @param method is a name of http method
     * @param uriStr is a string value of URI of target endpoint
     * @param mapper is an instance of {@link ObjectMapper} that is used to transform object
     *               into json/xml string
     * @param body   object to be serialized
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, String uriStr, ObjectMapper mapper, Object body) {
        return METHOD(method, create(uriStr), mapper, body);
    }

    /**
     * Creates a builder of a fluent http request. An object is transformed into json/xml string body.
     *
     * @param method is a name of http method
     * @param uriStr is a string value of URI of target endpoint
     * @param mapper creates/gets an instance of {@link ObjectMapper} that is used by default to transform object
     *               into json/xml string.
     * @param body   object to be serialized
     * @return a fluent http request builder.
     * @see DefaultBodyMappers
     * @see DefaultJsonObjectMapper
     * @see DefaultXmlObjectMapper
     */
    public static RequestBuilder METHOD(String method, String uriStr, DefaultBodyMappers mapper, Object body) {
        return METHOD(method, create(uriStr), mapper, body);
    }

    /**
     * Creates a builder of a fluent http request with multipart body.
     *
     * @param method   is a name of http method
     * @param uriStr   is a string value of URI of target endpoint
     * @param boundary is a boundary between body parts
     * @param parts    are parts of multipart body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, String uriStr, String boundary, BodyPart... parts) {
        return METHOD(method, create(uriStr), boundary, parts);
    }

    /**
     * Creates a builder of a fluent http request with multipart body.
     *
     * @param method is a name of http method
     * @param uriStr is a string value of URI of target endpoint
     * @param parts  are parts of multipart body
     * @return a fluent http request builder.
     */
    public static RequestBuilder METHOD(String method, String uriStr, BodyPart... parts) {
        return METHOD(method, create(uriStr), parts);
    }


    abstract void defineRequestMethodAndBody();

    @Override
    public RequestBuilder expectContinue(boolean enable) {
        builder.expectContinue(enable);
        return this;
    }

    @Override
    public RequestBuilder version(HttpClient.Version version) {
        builder.version(version);
        return this;
    }

    @Override
    public RequestBuilder header(String name, String... values) {
        var list = headersMap.computeIfAbsent(name, k -> new ArrayList<>());
        list.addAll(List.of(values));
        return this;
    }

    @Override
    public RequestBuilder timeout(Duration duration) {
        builder.timeout(duration);
        return this;
    }

    @Override
    public RequestBuilder queryParam(String name, QueryValueDelimiters delimiter, Object... values) {
        queryBuilder.addParameter(name, false, delimiter, values);
        return this;
    }

    @Override
    public RequestBuilder queryParam(String name, Object... values) {
        queryBuilder.addParameter(name, true, null, values);
        return this;
    }

    /**
     * Defines algorithms of the preparing of http requests.
     * These algorithms are wrapped/implemented by {@link RequestTuner}
     *
     * @param tuners objects that help to prepare resulted http-request
     * @return self-reference
     */
    public RequestBuilder tuneWith(RequestTuner... tuners) {
        checkNotNull(tuners);
        checkArgument(tuners.length > 0, "There is nothing that helps to prepare http request");
        var thisBuilder = this;
        Arrays.stream(tuners).forEach(t -> t.setUp(thisBuilder));
        return this;
    }

    /**
     * Defines algorithms of the preparing of http requests.
     * These algorithms are wrapped/implemented by {@link RequestTuner}
     *
     * @param tuners objects that help to prepare resulted http-request
     * @return self-reference
     */
    @SafeVarargs
    public final RequestBuilder tuneWith(Class<? extends RequestTuner>... tuners) {
        checkNotNull(tuners);
        checkArgument(tuners.length > 0, "There is nothing that helps to prepare http request");
        return tuneWith(Arrays.stream(tuners)
                .distinct()
                .map(c -> {
                    try {
                        var constructor = c.getDeclaredConstructor();
                        constructor.setAccessible(true);
                        return constructor.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).toArray(RequestTuner[]::new));
    }

    /**
     * Defines algorithms of the preparing of http requests.
     * These algorithms are wrapped/implemented by {@link RequestTuner}
     *
     * @param tuners classes of objects that help to prepare resulted http request.
     *               Defined classes should declare no constructor or these classes should have
     *               a constructor with no parameter.
     * @return self-reference
     */
    public RequestBuilder tuneWith(Iterable<RequestTuner> tuners) {
        checkNotNull(tuners);
        return tuneWith(stream(tuners.spliterator(), false).distinct().toArray(RequestTuner[]::new));
    }


    public HttpRequest build() {
        var newBuilder = builder.copy();

        headersMap.forEach((s, strings) -> {
            var valueList = new ArrayList<>(strings);
            if (equalsIgnoreCase(s, "Content-Type")
                    && valueList.size() > 0
                    && (body != null && body instanceof MultiPartBody)) {
                var val = valueList.get(0);
                valueList.set(0, val + ";boundary=" + ((MultiPartBody) body).getBoundary());
            }

            valueList.forEach(s1 -> newBuilder.header(s, s1));
        });

        return new NeptuneHttpRequestImpl(newBuilder.uri(queryBuilder.appendURI(endPoint)).build(), body);
    }


    @Override
    public String toString() {
        return build().toString();
    }
}
