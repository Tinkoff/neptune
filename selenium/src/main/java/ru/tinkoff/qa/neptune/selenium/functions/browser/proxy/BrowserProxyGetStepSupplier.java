package ru.tinkoff.qa.neptune.selenium.functions.browser.proxy;

import io.netty.handler.codec.http.HttpMethod;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.devtools.v95.network.Network;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.collections.CollectionCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.GetHttpProxy.getBrowserProxy;

@Description("Http traffic")
@CaptureOnSuccess(by = CollectionCaptor.class)
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria for http request/response")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Waiting time")
public class BrowserProxyGetStepSupplier extends SequentialGetStepSupplier.GetListStepSupplier<SeleniumStepContext, List<HttpTraffic>, HttpTraffic, BrowserProxyGetStepSupplier> {

    private BrowserProxyGetStepSupplier() {
        super(getBrowserProxy().andThen(httpProxy -> ofNullable(httpProxy)
                .map(proxy -> {
                    var devTools = proxy.getDevTools();
                    var requests = proxy.getRequestList();
                    var responses = proxy.getResponseList();

                    requests.forEach(request -> {
                        var httpTraffic = new HttpTraffic();
                        var response = responses
                                .stream()
                                .filter(r -> r.getRequestId().toString().equals(request.getRequestId().toString()))
                                .findFirst()
                                .orElse(null);

                        httpTraffic.setRequest(request)
                                .setResponse(response);

                        try {
                            var body = devTools.send(Network.getResponseBody(request.getRequestId()));
                            httpTraffic.setBody(body);
                        } catch (WebDriverException e) {
                            e.printStackTrace();
                        }

                        proxy.getHttpTrafficList()
                                .add(httpTraffic);
                    });
                    return proxy.getHttpTrafficList();
                })
                .orElseGet(ArrayList::new)));

    }

    public static BrowserProxyGetStepSupplier proxiedRequests() {
        return new BrowserProxyGetStepSupplier();
    }

    public BrowserProxyGetStepSupplier recordedRequestUrl(String url) {
        return criteria(BrowserProxyCriteria.recordedRequestUrl(url));
    }

    public BrowserProxyGetStepSupplier recordedRequestUrlMatches(String urlExpression) {
        return criteria(BrowserProxyCriteria.recordedRequestUrlMatches(urlExpression));
    }

    public BrowserProxyGetStepSupplier recordedRequestMethod(HttpMethod method) {
        return criteria(BrowserProxyCriteria.recordedRequestMethod(method));
    }

    public BrowserProxyGetStepSupplier recordedResponseHttpVersion(HttpClient.Version version) {
        return criteria(BrowserProxyCriteria.recordedResponseHttpVersion(version));
    }

    public BrowserProxyGetStepSupplier recordedRequestHeader(String name, String value) {
        return criteria(BrowserProxyCriteria.recordedRequestHeader(name, value));
    }

    public BrowserProxyGetStepSupplier recordedRequestHeaderMatches(String name, String valueExpression) {
        return criteria(BrowserProxyCriteria.recordedRequestHeaderMatches(name, valueExpression));
    }

    public BrowserProxyGetStepSupplier recordedResponseHeader(String name, String value) {
        return criteria(BrowserProxyCriteria.recordedResponseHeader(name, value));
    }

    public BrowserProxyGetStepSupplier recordedResponseHeaderMatches(String name, String valueExpression) {
        return criteria(BrowserProxyCriteria.recordedResponseHeaderMatches(name, valueExpression));
    }

    public BrowserProxyGetStepSupplier recordedRequestBody(String body) {
        return criteria(BrowserProxyCriteria.recordedRequestBody(body));
    }

    public BrowserProxyGetStepSupplier recordedResponseBody(String body) {
        return criteria(BrowserProxyCriteria.recordedResponseBody(body));
    }

    public BrowserProxyGetStepSupplier recordedRequestBodyMatches(String bodyExpression) {
        return criteria(BrowserProxyCriteria.recordedRequestBodyMatches(bodyExpression));
    }

    public BrowserProxyGetStepSupplier recordedResponseBodyMatches(String bodyExpression) {
        return criteria(BrowserProxyCriteria.recordedResponseBodyMatches(bodyExpression));
    }

    public BrowserProxyGetStepSupplier recordedResponseStatusCode(int status) {
        return criteria(BrowserProxyCriteria.recordedResponseStatusCode(status));
    }

    @Override
    public BrowserProxyGetStepSupplier timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public BrowserProxyGetStepSupplier pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }
}
