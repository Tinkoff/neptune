package ru.tinkoff.qa.neptune.selenium;

import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v95.network.Network;
import org.openqa.selenium.devtools.v95.network.model.RequestWillBeSent;
import org.openqa.selenium.devtools.v95.network.model.ResponseReceived;
import ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.HttpTraffic;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HttpProxy {
    private final DevTools devTools;
    private final List<RequestWillBeSent> requestList = new CopyOnWriteArrayList<>();
    private final List<ResponseReceived> responseList = new CopyOnWriteArrayList<>();
    private final List<HttpTraffic> httpTrafficList = new CopyOnWriteArrayList<>();
    private final List<HttpTraffic> dumpHttpTrafficList = new CopyOnWriteArrayList<>();

    public HttpProxy(DevTools devTools) {
        this.devTools = devTools;
    }

    public void listen() {
        devTools.addListener(Network.requestWillBeSent(), requestList::add);
        devTools.addListener(Network.responseReceived(), responseList::add);
    }

    public void clearDump() {
        requestList.clear();
        responseList.clear();
        dumpHttpTrafficList.clear();
    }

    public List<RequestWillBeSent> getRequestList() {
        return requestList;
    }

    public List<ResponseReceived> getResponseList() {
        return responseList;
    }

    public List<HttpTraffic> getHttpTrafficList() {
        return httpTrafficList;
    }


    public DevTools getDevTools() {
        return devTools;
    }
}
