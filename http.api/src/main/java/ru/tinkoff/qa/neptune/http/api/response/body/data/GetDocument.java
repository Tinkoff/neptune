package ru.tinkoff.qa.neptune.http.api.response.body.data;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import java.io.StringReader;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.jsoup.Jsoup.parse;

/**
 * This function is designed to read string content and transform it to XML/HTML documents.
 * @param <T> is a type of resulted object
 */
public abstract class GetDocument<T> implements Function<String, T> {

    /**
     * Creates an instance of {@link GetDocument} that reads string body of a response and transforms it to {@link Document}
     * @param documentBuilder an instance of that {@link DocumentBuilder} parses string and creates resulted xml/html document
     * @return an instance of anonymous {@link GetDocument} subclass
     */
    public static Function<String, org.w3c.dom.Document> getDocument(DocumentBuilder documentBuilder) {
        checkArgument(nonNull(documentBuilder), "Document builder should not be a null value");
        return new GetDocument<>() {
            @Override
            public Document apply(String s) {
                var inputSource = new InputSource(new StringReader(s));
                try {
                    return documentBuilder.parse(inputSource);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    /**
     * Creates an instance of {@link GetDocument} that reads string body of a response and transforms it to {@link org.jsoup.nodes.Document}
     *
     * @return an instance of anonymous {@link GetDocument} subclass
     */
    public static Function<String, org.jsoup.nodes.Document> getDocument() {
        return new GetDocument<>() {
            @Override
            public org.jsoup.nodes.Document apply(String s) {
                return parse(s);
            }
        };
    }
}
