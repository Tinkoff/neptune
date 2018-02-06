package com.github.toy.constructor.selenium.functions.searching;

import com.github.toy.constructor.selenium.api.widget.Widget;
import org.openqa.selenium.SearchContext;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static java.lang.String.format;

class FindWidgets<R extends Widget> implements Function<SearchContext, List<R>> {

    private final Class<? extends R> classOfAWidget;
    private final TimeUnit timeUnit;
    private final long time;

    private FindWidgets(Class<R> classOfAWidget, TimeUnit timeUnit, long time) {
        this.classOfAWidget = classOfAWidget;
        this.timeUnit = timeUnit;
        this.time = time;
    }

    static <R extends Widget> Function<SearchContext, List<R>> widgets(Class<R> classOfAWidget,
                                                                       TimeUnit timeUnit,
                                                                       long time) {
        return toGet(format("Find widgets of type %s", classOfAWidget.getName()),
                new FindWidgets<>(classOfAWidget, timeUnit, time));
    }

    @Override
    public List<R> apply(SearchContext searchContext) {
        return null;
    }
}
