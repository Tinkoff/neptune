package ru.tinkoff.qa.neptune.http.api.request.body;

import org.jsoup.nodes.Document;

import java.net.http.HttpRequest;

import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static java.util.Optional.ofNullable;

class JSoupHtmlBody extends RequestBody<String> {

    protected JSoupHtmlBody(Document body) {
        super(ofNullable(body).map(Document::outerHtml).orElseThrow());
    }

    @Override
    public HttpRequest.BodyPublisher createPublisher() {
        return ofString(super.body());
    }
}
