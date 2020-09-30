package ru.tinkoff.qa.neptune.selenium.functions.browser.proxy;

import com.browserup.harreader.model.Har;
import com.browserup.harreader.model.HarEntry;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Optional.ofNullable;

public class BrowserProxyGetStepSupplier extends SequentialGetStepSupplier.GetIterableStepSupplier<SeleniumStepContext, List<HarEntry>, HarEntry, BrowserProxyGetStepSupplier> {

    private BrowserProxyGetStepSupplier() {
        super("Proxied requests", seleniumStepContext ->
                ofNullable(seleniumStepContext.getProxy())
                        .map(proxy -> {
                            Har har = proxy.getHar();

                            if (har == null) {
                                throw new IllegalStateException("HAR recording is not started");
                            }

                            return har.getLog().getEntries();
                        })
                        .orElseGet(ArrayList::new));
    }

    public static BrowserProxyGetStepSupplier proxiedRequests() {
        return new BrowserProxyGetStepSupplier();
    }

    @Override
    public BrowserProxyGetStepSupplier criteria(Criteria<? super HarEntry> criteria) {
        return super.criteria(criteria);
    }

    @Override
    public BrowserProxyGetStepSupplier criteria(String description, Predicate<? super HarEntry> predicate) {
        return super.criteria(description, predicate);
    }

    @Override
    public BrowserProxyGetStepSupplier timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public BrowserProxyGetStepSupplier pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    public BrowserProxyGetStepSupplier throwOnEmptyResult(String exceptionMessage) {
        return throwOnEmptyResult(() -> new IllegalStateException(exceptionMessage));
    }
}
