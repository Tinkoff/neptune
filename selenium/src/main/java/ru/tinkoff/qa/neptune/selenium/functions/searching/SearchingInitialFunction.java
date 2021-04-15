package ru.tinkoff.qa.neptune.selenium.functions.searching;

import org.openqa.selenium.SearchContext;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

final class SearchingInitialFunction implements Function<Object, SearchContext> {

    @Override
    public SearchContext apply(Object o) {
        checkNotNull(o);

        if (o instanceof SeleniumStepContext) {
            return ((SeleniumStepContext) o).getWrappedDriver();
        }

        if (o instanceof SearchContext) {
            return (SearchContext) o;
        }

        throw new IllegalArgumentException(format("Only instances of %s and %s are allowed to be used for the searching for" +
                        "elements on a page. " +
                        "Passed object is an instance of %s",
                SearchContext.class.getName(),
                SearchContext.class.getName(),
                o.getClass()));
    }
}
