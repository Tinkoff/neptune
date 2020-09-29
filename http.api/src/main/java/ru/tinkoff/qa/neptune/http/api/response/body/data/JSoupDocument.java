package ru.tinkoff.qa.neptune.http.api.response.body.data;

import org.jsoup.nodes.Document;

import java.util.function.Function;

import static org.jsoup.Jsoup.parse;

final class JSoupDocument implements Function<String, Document> {

    @Override
    public Document apply(String s) {
        try {
            return parse(s);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
}
