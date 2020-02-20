package ru.tinkoff.qa.neptune.selenium.functions.target.locator.window;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;

import java.net.URL;
import java.util.function.Function;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
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
    public static Criteria<Window> title(String title) {
        checkArgument(isNotBlank(title), "Expected title should not be defined as a blank/null string");
        return condition(format("title '%s'", title), window -> title.equals(window.getTitle()));
    }

    /**
     * Builds criteria to match title of a window. It is expected that title contains defined string.
     *
     * @param toContain is a part of a window title
     * @return criteria
     */
    public static Criteria<Window> titleContains(String toContain) {
        checkArgument(isNotBlank(toContain), "Part of a window title should not be defined as a blank/null string");
        return condition(format("title contains '%s'", toContain), window -> valueOf(window.getTitle()).contains(toContain));
    }

    /**
     * Builds criteria to match title of a window by regExp pattern.
     *
     * @param titlePattern regExp pattern which is used to match title of a window.
     * @return criteria.
     */
    public static Criteria<Window> titleMatches(Pattern titlePattern) {
        checkNotNull(titlePattern, "Req exp to match title should not be defined as a null value");
        return condition(format("title meets regExp pattern '%s'", titlePattern),
                window -> {
                    var m = titlePattern.matcher(window.getTitle());
                    return m.matches();
                });
    }

    /**
     * Builds criteria to match url of the page loaded in a window.
     *
     * @param url expected url of the page loaded in a window.
     * @return criteria.
     */
    public static Criteria<Window> pageAt(String url) {
        checkArgument(isNotBlank(url), "Expected url should not be defined as a blank/null string");
        return condition(format("url '%s'", url), window -> url.equals(window.getCurrentUrl()));
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
     * Builds criteria to match url of the page loaded in a window by containing of defined substring.
     *
     * @param substring that is expected to be contained
     * @return criteria.
     */
    public static Criteria<Window> urlContains(String substring) {
        checkArgument(isNotBlank(substring), "Part of a page url should not be defined as a blank/null string");
        return condition(format("url contains '%s'", substring), window -> valueOf(window.getCurrentUrl()).contains(substring));
    }

    /**
     * Builds criteria to match url of the page loaded in a window by regExp pattern.
     *
     * @param urlPattern regExp pattern which is used to match url of the page loaded in a window.
     * @return criteria.
     */
    public static Criteria<Window> urlMatches(Pattern urlPattern) {
        checkNotNull(urlPattern, "Req exp to match url should not be defined as a null value");
        return condition(format("url meets regExp pattern '%s'", urlPattern),
                window -> {
                    var m = urlPattern.matcher(window.getCurrentUrl());
                    return m.matches();
                });
    }

    private static Criteria<Window> urlPartStringCriteria(String description, String expected, Function<URL, String> getPart) {
        checkArgument(isNotBlank(expected), format("Expected %s should not be defined as a blank/null string", description));
        return condition(format("url %s is '%s'", description, expected), window -> {
            try {
                return getPart.apply(new URL(valueOf(window.getCurrentUrl()))).equals(expected);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }

    private static Criteria<Window> urlPartRegExpCriteria(String description, Pattern expected, Function<URL, String> getPart) {
        checkNotNull(expected, format("Req exp to match url %s should not be defined as a null value", description));
        return condition(format("url %s meets regExp '%s'", description, expected), window -> {
            try {
                var m = expected.matcher(getPart.apply(new URL(valueOf(window.getCurrentUrl()))));
                return m.matches();
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }

    /**
     * Builds criteria to match url of the page loaded in by host value.
     *
     * @param host is a host value
     * @return criteria.
     */
    public static Criteria<Window> urlHost(String host) {
        return urlPartStringCriteria("host", host, URL::getHost);
    }

    /**
     * Builds criteria to match url of the page loaded in by host value.
     *
     * @param hostPattern is a pattern to match URL host
     * @return criteria.
     */
    public static Criteria<Window> urlHost(Pattern hostPattern) {
        return urlPartRegExpCriteria("host", hostPattern, URL::getHost);
    }

    /**
     * Builds criteria to match url of the page loaded in by protocol value.
     *
     * @param protocol is a protocol value
     * @return criteria.
     */
    public static Criteria<Window> urlProtocol(String protocol) {
        return urlPartStringCriteria("protocol", protocol, URL::getProtocol);
    }

    /**
     * Builds criteria to match url of the page loaded in by protocol value.
     *
     * @param protocolPattern is a pattern to match URL protocol
     * @return criteria.
     */
    public static Criteria<Window> urlProtocol(Pattern protocolPattern) {
        return urlPartRegExpCriteria("protocol", protocolPattern, URL::getProtocol);
    }

    /**
     * Builds criteria to match url of the page loaded in by path value.
     *
     * @param path is a path value
     * @return criteria.
     */
    public static Criteria<Window> urlPath(String path) {
        return urlPartStringCriteria("path", path, URL::getPath);
    }

    /**
     * Builds criteria to match url of the page loaded in by path value.
     *
     * @param pathPattern is a pattern to match URL path
     * @return criteria.
     */
    public static Criteria<Window> urlPath(Pattern pathPattern) {
        return urlPartRegExpCriteria("path", pathPattern, URL::getPath);
    }

    /**
     * Builds criteria to match url of the page loaded in by query value.
     *
     * @param query is a query value
     * @return criteria.
     */
    public static Criteria<Window> urlQuery(String query) {
        return urlPartStringCriteria("query", query, URL::getQuery);
    }

    /**
     * Builds criteria to match url of the page loaded in by query value.
     *
     * @param queryPattern is a pattern to match URL query
     * @return criteria.
     */
    public static Criteria<Window> urlQuery(Pattern queryPattern) {
        return urlPartRegExpCriteria("query", queryPattern, URL::getQuery);
    }

    /**
     * Builds criteria to match url of the page loaded in by user info value.
     *
     * @param userInfo is an user info value
     * @return criteria.
     */
    public static Criteria<Window> urlUserInfo(String userInfo) {
        return urlPartStringCriteria("user info", userInfo, URL::getUserInfo);
    }

    /**
     * Builds criteria to match url of the page loaded in by user info value.
     *
     * @param userInfoPattern is a pattern to match URL user info
     * @return criteria.
     */
    public static Criteria<Window> urlUserInfo(Pattern userInfoPattern) {
        return urlPartRegExpCriteria("user info", userInfoPattern, URL::getUserInfo);
    }

    /**
     * Builds criteria to match url of the page loaded in by port value.
     *
     * @param port is a port value
     * @return criteria.
     */
    public static Criteria<Window> urlPort(int port) {
        checkArgument(port > 0, "Port value should be greater than 0");
        return condition(format("url port is '%s'", port), window -> {
            try {
                return new URL(valueOf(window.getCurrentUrl())).getPort() == port;
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }

    /**
     * Builds criteria to match url of the page loaded in by port value. It is expected that url of the page
     * is in defined diapason.
     *
     * @param portMin is an expected min value of a port
     * @param portMax is an expected max value of a port
     * @return criteria.
     */
    public static Criteria<Window> urlPortIn(int portMin, int portMax) {
        checkArgument(portMin > 0, "Min port value should be greater than 0");
        checkArgument(portMax > 0, "Max port value should be greater than 0");
        checkArgument(portMax > portMin, "Max port value should be greater than defined min");
        return condition(format("url port is in ['%s', '%s']", portMin, portMax), window -> {
            try {
                var port =  new URL(valueOf(window.getCurrentUrl())).getPort();
                return port >= portMin && port <= portMax;
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }
}
