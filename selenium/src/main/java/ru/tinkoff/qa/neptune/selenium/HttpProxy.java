package ru.tinkoff.qa.neptune.selenium;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v112.network.Network;
import org.openqa.selenium.devtools.v112.network.model.RequestWillBeSent;
import org.openqa.selenium.devtools.v112.network.model.ResponseReceived;
import ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.HttpTraffic;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.Optional.ofNullable;

public class HttpProxy {
    private final DevTools devTools;
    private final List<RequestWillBeSent> requestList = new CopyOnWriteArrayList<>();
    private final List<ResponseReceived> responseList = new CopyOnWriteArrayList<>();
    private final List<HttpTraffic> httpTrafficList = new CopyOnWriteArrayList<>();

    public HttpProxy(DevTools dt) {
        this.devTools = dt;
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
    }

    public void listen() {
        devTools.addListener(Network.requestWillBeSent(), requestList::add);
        devTools.addListener(Network.responseReceived(), responseList::add);
    }

    public void clearDump() {
        requestList.clear();
        responseList.clear();
        httpTrafficList.clear();
    }

    public void disabledNetwork() {
        ofNullable(devTools.getCdpSession())
                .ifPresent(cdp -> devTools.send(Network.disable()));
    }

    public List<HttpTraffic> getTraffic() {
        try {
            requestList.forEach(request -> {
                var httpTraffic = new HttpTraffic();
                var response = responseList
                        .stream()
                        .filter(r -> r.getRequestId().toString().equals(request.getRequestId().toString()))
                        .findFirst()
                        .orElse(null);

                httpTraffic.setRequest(request)
                        .setResponse(response);

                try {
                    ofNullable(devTools)
                            .map(DevTools::getCdpSession)
                            .ifPresent(session ->
                                    httpTraffic.setBody(devTools.send(Network.getResponseBody(request.getRequestId()))));
                } catch (WebDriverException e) {
                    e.printStackTrace();
                }

                httpTrafficList.add(httpTraffic);
            });
        } finally {
            requestList.clear();
            responseList.clear();
        }
        return httpTrafficList;
    }
}
