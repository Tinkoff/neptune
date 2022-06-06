package ru.tinkoff.qa.neptune.selenium.functions.browser.proxy;

import org.openqa.selenium.devtools.v102.network.Network;
import org.openqa.selenium.devtools.v102.network.model.RequestWillBeSent;
import org.openqa.selenium.devtools.v102.network.model.ResponseReceived;

import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;

public class HttpTraffic {
    private RequestWillBeSent request;
    private ResponseReceived response;
    private Network.GetResponseBodyResponse body;

    public RequestWillBeSent getRequest() {
        return request;
    }

    public HttpTraffic setRequest(RequestWillBeSent request) {
        this.request = request;
        return this;
    }

    public ResponseReceived getResponse() {
        return response;
    }

    public HttpTraffic setResponse(ResponseReceived response) {
        this.response = response;
        return this;
    }

    public Network.GetResponseBodyResponse getBody() {
        return body;
    }

    public HttpTraffic setBody(Network.GetResponseBodyResponse body) {
        this.body = body;
        return this;
    }

    @Override
    public String toString() {
        return "HttpTraffic{" +
                "request{ url =" + request.getRequest().getUrl() +
                ", urlFragment =" + request.getRequest().getUrlFragment() +
                ", method =" + request.getRequest().getMethod() +
                "}, response{ status = " + ofNullable(response).map(r -> valueOf(r.getResponse().getStatus())).orElse("response is missing") +
                ", statusText = " + ofNullable(response).map(r -> r.getResponse().getStatusText()).orElse("response is missing") +
                "}, body=" + ofNullable(body).map(Network.GetResponseBodyResponse::getBody).orElse("body is missing") +
                '}';
    }
}
