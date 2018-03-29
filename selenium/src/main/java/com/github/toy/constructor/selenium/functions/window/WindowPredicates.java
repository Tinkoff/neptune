package com.github.toy.constructor.selenium.functions.window;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static java.lang.String.format;

final class WindowPredicates {

    private WindowPredicates() {
        super();
    }

    static Predicate<Window> hasTitle(String title) {
        return condition(format("Has title '%s'", title), window -> title.equals(window.getTitle()));
    }

    static Predicate<Window> hasTitle(Pattern titlePattern) {
        return condition(format("Has title which matches regExp patter '%s'", titlePattern),
                window -> {
                    Matcher m = titlePattern.matcher(window.getTitle());
                    return m.find();
                });
    }

    static Predicate<Window> hasUrl(String url) {
        return condition(format("Has loaded url '%s'", url), window -> url.equals(window.getCurrentUrl()));
    }

    static Predicate<Window> hasUrl(Pattern urlPattern) {
        return condition(format("Has loaded url which matches regExp patter '%s'", urlPattern),
                window -> {
                    Matcher m = urlPattern.matcher(window.getCurrentUrl());
                    return m.find();
                });
    }
}
