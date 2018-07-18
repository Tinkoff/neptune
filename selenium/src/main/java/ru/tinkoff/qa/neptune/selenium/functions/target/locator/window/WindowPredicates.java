package ru.tinkoff.qa.neptune.selenium.functions.target.locator.window;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.tinkoff.qa.neptune.core.api.StoryWriter.condition;
import static java.lang.String.format;

public final class WindowPredicates {

    private WindowPredicates() {
        super();
    }

    /**
     * Builds criteria to match title of a window.
     *
     * @param title expected title of a window.
     * @return predicate.
     */
    public static Predicate<Window> hasTitle(String title) {
        return condition(format("title '%s'", title), window -> title.equals(window.getTitle()));
    }

    /**
     * Builds criteria to match title of a window by regExp pattern.
     *
     * @param titlePattern regExp pattern which is used to match title of a window.
     * @return predicate.
     */
    public static Predicate<Window> hasTitle(Pattern titlePattern) {
        return condition(format("title meets regExp pattern '%s'", titlePattern),
                window -> {
                    Matcher m = titlePattern.matcher(window.getTitle());
                    return m.find();
                });
    }

    /**
     * Builds criteria to match url of the page loaded in a window.
     *
     * @param url expected url of the page loaded in a window.
     * @return predicate.
     */
    public static Predicate<Window> hasUrl(String url) {
        return condition(format("url '%s'", url), window -> url.equals(window.getCurrentUrl()));
    }

    /**
     * Builds criteria to match url of the page loaded in a window by regExp pattern.
     *
     * @param urlPattern regExp pattern which is used to match url of the page loaded in a window.
     * @return predicate.
     */
    public static Predicate<Window> hasUrl(Pattern urlPattern) {
        return condition(format("url meets regExp pattern '%s'", urlPattern),
                window -> {
                    Matcher m = urlPattern.matcher(window.getCurrentUrl());
                    return m.find();
                });
    }
}
