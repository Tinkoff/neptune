package ru.tinkoff.qa.neptune.selenium.functions.browser.proxy;

import com.browserup.harreader.model.HarEntry;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.collections.CollectionCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import java.time.Duration;
import java.util.List;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.GetProxy.getBrowserProxy;

@Description("Http traffic")
@CaptureOnSuccess(by = CollectionCaptor.class)
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria for http request/response")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Waiting time")
public class BrowserProxyGetStepSupplier extends SequentialGetStepSupplier.GetListStepSupplier<SeleniumStepContext, List<HarEntry>, HarEntry, BrowserProxyGetStepSupplier> {

    private BrowserProxyGetStepSupplier() {
        super(getBrowserProxy().andThen(proxy -> ofNullable(proxy)
                .map(p -> {
                    var harEntries = new NeptuneHarEntries();

                    if (!p.isStarted()) {
                        return harEntries;
                    }

                    var har = p.getHar();
                    if (har == null) {
                        System.err.println("HAR recording is not started");
                        return harEntries;
                    }

                    harEntries.addAll(har.getLog().getEntries().stream().map(harEntry -> {
                        HarEntry entry = new HarEntry() {
                            @Override
                            public String toString() {
                                var request = getRequest();
                                return translate(new NeptuneHarEntryDescription()) + ": " + request.getMethod() + " "
                                        + request.getUrl();
                            }
                        };
                        entry.setCache(harEntry.getCache());
                        entry.setComment(harEntry.getComment());
                        entry.setPageref(harEntry.getPageref());
                        entry.setRequest(harEntry.getRequest());
                        entry.setConnection(harEntry.getConnection());
                        entry.setResponse(harEntry.getResponse());
                        entry.setServerIPAddress(harEntry.getServerIPAddress());
                        entry.setStartedDateTime(harEntry.getStartedDateTime());
                        entry.setTime(harEntry.getTime());
                        entry.setTimings(harEntry.getTimings());
                        return entry;
                    }).collect(toList()));

                    return harEntries;
                })
                .orElseGet(NeptuneHarEntries::new)));
    }

    public static BrowserProxyGetStepSupplier proxiedRequests() {
        return new BrowserProxyGetStepSupplier();
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
