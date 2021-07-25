package ru.tinkoff.qa.neptune.selenium.functions.target.locator.window;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import java.net.URL;
import java.util.Objects;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.checkByStringContainingOrRegExp;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

public final class WindowCriteria {

    private WindowCriteria() {
        super();
    }

    /**
     * Builds criteria to match title of a window.
     *
     * @param title expected title of a window.
     * @return criteria.
     */
    @Description("title '{title}'")
    public static Criteria<Window> title(@DescriptionFragment("title") String title) {
        checkArgument(isNotBlank(title), "Title should be defined");
        return condition(window -> Objects.equals(title, window.getTitle()));
    }

    /**
     * Builds criteria to match title of a window.
     *
     * @param expression is a substring that title of a window is supposed to have.
     *                   It is possible to pass reg exp pattern that title of a window should fit.
     * @return criteria.
     */
    @Description("title contains '{expression}' or meets regExp pattern '{expression}'")
    public static Criteria<Window> titleMatches(@DescriptionFragment("expression") String expression) {
        checkArgument(isNotBlank(expression), "Title expression should be defined");
        return condition(window -> checkByStringContainingOrRegExp(expression).test(window.getTitle()));
    }

    /**
     * Builds criteria to match url of the page loaded in a window.
     *
     * @param url expected url of the page loaded in a window.
     * @return criteria.
     */
    @Description("url '{url}'")
    public static Criteria<Window> pageAt(@DescriptionFragment("url") String url) {
        checkArgument(isNotBlank(url), "Expected url should be defined");
        return condition(window -> url.equals(window.getCurrentUrl()));
    }

    /**
     * Builds criteria to match url of the page loaded in a window.
     *
     * @param url expected url of the page loaded in a window.
     * @return criteria.
     */
    public static Criteria<Window> pageAt(URL url) {
        checkNotNull(url, "Expected url should not be defined as a null value");
        return pageAt(url.toString());
    }

    /**
     * Builds criteria to match url of the page loaded in a window.
     *
     * @param expression is a substring that url of a window/tab is supposed to have.
     *                   It is possible to pass reg exp pattern that url should fit.
     * @return criteria.
     */
    @Description("url contains '{expression}' or meets regExp pattern '{expression}'")
    public static Criteria<Window> urlMatches(@DescriptionFragment("expression") String expression) {
        checkArgument(isNotBlank(expression), "URL expression should be defined");
        return condition(window -> checkByStringContainingOrRegExp(expression).test(window.getCurrentUrl()));
    }

    @Description("url {description} is '{expected}'")
    private static Criteria<Window> urlPartStringCriteria(@DescriptionFragment("description") String description,
                                                          @DescriptionFragment("expected") String expected,
                                                          Function<URL, String> getPart) {
        checkArgument(isNotBlank(expected), format("Expected url %s should not be defined as a blank/null string", description));
        return condition(window -> {
            try {
                return Objects.equals(getPart.apply(new URL(valueOf(window.getCurrentUrl()))), expected);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }

    @Description("url {description} contains '{expression}' or meets regExp pattern '{expression}'")
    private static Criteria<Window> urlPartRegExpCriteria(@DescriptionFragment("description") String description,
                                                          @DescriptionFragment("expression") String expression,
                                                          Function<URL, String> getPart) {
        checkArgument(isNotBlank(expression), format("expression of url %s should not be defined as a blank/null string", description));
        return condition(window -> checkByStringContainingOrRegExp(expression).test(window.getCurrentUrl()));
    }

    /**
     * Builds criteria to match url of the loaded page by host value.
     *
     * @param host is a host value
     * @return criteria.
     */
    public static Criteria<Window> urlHost(String host) {
        return urlPartStringCriteria("host", host, URL::getHost);
    }

    /**
     * Builds criteria to match url of the loaded page by host value.
     *
     * @param expression is a substring that host is supposed to have.
     *                   It is possible to pass reg exp pattern that host should fit.
     * @return criteria.
     */
    public static Criteria<Window> urlHostMatches(String expression) {
        return urlPartRegExpCriteria("host", expression, URL::getHost);
    }

    /**
     * Builds criteria to match url of the loaded page by protocol value.
     *
     * @param protocol is a protocol value
     * @return criteria.
     */
    public static Criteria<Window> urlProtocol(String protocol) {
        return urlPartStringCriteria("protocol", protocol, URL::getProtocol);
    }

    /**
     * Builds criteria to match url of the loaded page by protocol value.
     *
     * @param expression is a substring that protocol is supposed to have.
     *                   It is possible to pass reg exp pattern that protocol should fit.
     * @return criteria.
     */
    public static Criteria<Window> urlProtocolMatches(String expression) {
        return urlPartRegExpCriteria("protocol", expression, URL::getProtocol);
    }

    /**
     * Builds criteria to match url of the loaded page by reference value.
     *
     * @param reference is a reference value
     * @return criteria.
     */
    public static Criteria<Window> urlRef(String reference) {
        return urlPartStringCriteria("reference", reference, URL::getRef);
    }

    /**
     * Builds criteria to match url of the loaded page by reference value.
     *
     * @param expression is a substring that reference is supposed to have.
     *                   It is possible to pass reg exp pattern that reference should fit.
     * @return criteria.
     */
    public static Criteria<Window> urlRefMatches(String expression) {
        return urlPartRegExpCriteria("reference", expression, URL::getRef);
    }

    /**
     * Builds criteria to match url of the loaded page by query value.
     *
     * @param query is a query value
     * @return criteria.
     */
    public static Criteria<Window> urlQuery(String query) {
        return urlPartStringCriteria("query", query, URL::getQuery);
    }

    /**
     * Builds criteria to match url of the loaded page by query value.
     *
     * @param expression is a substring that query is supposed to have.
     *                   It is possible to pass reg exp pattern that query should fit.
     * @return criteria.
     */
    public static Criteria<Window> urlQueryMatches(String expression) {
        return urlPartRegExpCriteria("query", expression, URL::getQuery);
    }

    /**
     * Builds criteria to match url of the loaded page by user info value.
     *
     * @param userInfo is an user info value
     * @return criteria.
     */
    public static Criteria<Window> urlUserInfo(String userInfo) {
        return urlPartStringCriteria("user info", userInfo, URL::getUserInfo);
    }

    /**
     * Builds criteria to match url of the loaded page by user info value.
     *
     * @param expression is a substring that user info is supposed to have.
     *                   It is possible to pass reg exp pattern that user info should fit.
     * @return criteria.
     */
    public static Criteria<Window> urlUserInfoMatches(String expression) {
        return urlPartRegExpCriteria("user info", expression, URL::getUserInfo);
    }

    /**
     * Builds criteria to match url of the loaded page by path value.
     *
     * @param path is a path value
     * @return criteria.
     */
    public static Criteria<Window> urlPath(String path) {
        return urlPartStringCriteria("path", path, URL::getPath);
    }

    /**
     * Builds criteria to match url of the loaded page by path value.
     *
     * @param expression is a substring that path is supposed to have.
     *                   It is possible to pass reg exp pattern that path should fit.
     * @return criteria.
     */
    public static Criteria<Window> urlPathMatches(String expression) {
        return urlPartRegExpCriteria("path", expression, URL::getPath);
    }

    /**
     * Builds criteria to match url of the page loaded in by port value.
     *
     * @param port is a port value
     * @return criteria.
     */
    @Description("url port is '{port}'")
    public static Criteria<Window> urlPort(@DescriptionFragment("port") int port) {
        checkArgument(port > 0, "Port value should be greater than 0");
        return condition(window -> {
            try {
                return new URL(valueOf(window.getCurrentUrl())).getPort() == port;
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }
}
