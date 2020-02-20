package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.url;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.ResourceLocatorMatcher;

import java.net.URL;

class PageUrlMatcher extends ResourceLocatorMatcher<URL, URL> {

    private final Matcher<URL> matcher;

    protected PageUrlMatcher(Matcher<URL> matcher) {
        super(matcher.toString(), matcher, url -> url);
        this.matcher = matcher;
    }

    public String toString() {
        return matcher.toString();
    }
}
