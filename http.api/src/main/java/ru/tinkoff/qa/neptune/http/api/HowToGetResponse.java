package ru.tinkoff.qa.neptune.http.api;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;

class HowToGetResponse {

    private final HttpClient client;
    private final HttpRequest request;

    HowToGetResponse(HttpClient client, HttpRequest request) {
        this.client = client;
        this.request = request;
    }

    HttpClient getClient() {
        return client;
    }

    HttpRequest getRequest() {
        return request;
    }
}
