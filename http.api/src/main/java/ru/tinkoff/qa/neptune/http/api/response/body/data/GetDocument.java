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
 *
 * @param <T>
 */
public abstract class GetDocument<T> implements Function<String, T> {

    /**
     *
     * @param documentBuilder
     * @return
     */
    public static GetDocument<Document> getDocument(DocumentBuilder documentBuilder) {
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
     *
     * @return
     */
    public static GetDocument<org.jsoup.nodes.Document> getDocument() {
        return new GetDocument<>() {
            @Override
            public org.jsoup.nodes.Document apply(String s) {
                return parse(s);
            }
        };
    }
}
