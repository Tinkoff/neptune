package ru.tinkoff.qa.neptune.selenium.functions.cookies;

import org.openqa.selenium.Cookie;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;

import java.util.Date;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

/**
 * Default criteria for {@link org.openqa.selenium.Cookie}
 *
 * @see ru.tinkoff.qa.neptune.core.api.steps.Criteria
 */
public final class CommonBrowserCookieCriteria {

    private CommonBrowserCookieCriteria() {
        super();
    }

    /**
     * The checking of a browser cookie.
     *
     * @param name is a cookie name
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#getName()
     */
    public static Criteria<Cookie> cookieName(String name) {
        checkArgument(isNotBlank(name), "Name should be defined");
        return condition(format("has name '%s'", name), c -> Objects.equals(name, c.getName()));
    }

    /**
     * The checking of a browser cookie.
     *
     * @param expression is a substring of text that a cookie name is supposed to have.
     *                   It is possible to pass reg exp pattern that name of a cookie should fit.
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#getName()
     */
    public static Criteria<Cookie> cookieNameMatches(String expression) {
        checkArgument(isNotBlank(expression), "Substring/RegEx pattern should be defined");

        return condition(format("has name that contains '%s' or fits regExp pattern '%s'", expression, expression), c ->
                ofNullable(c.getName())
                        .map(s -> {
                            if (s.contains(expression)) {
                                return true;
                            }

                            try {
                                var p = compile(expression);
                                var mather = p.matcher(s);
                                return mather.matches() || mather.find();
                            } catch (Throwable thrown) {
                                thrown.printStackTrace();
                                return false;
                            }
                        })
                        .orElse(false));
    }


    /**
     * The checking of a browser cookie.
     *
     * @param value is a cookie value
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#getValue()
     */
    public static Criteria<Cookie> cookieValue(String value) {
        checkArgument(isNotBlank(value), "Value should be defined");
        return condition(format("has value '%s'", value), c -> Objects.equals(value, c.getValue()));
    }

    /**
     * The checking of a browser cookie.
     *
     * @param expression is a substring of text that a cookie value is supposed to have.
     *                   It is possible to pass reg exp pattern that value of a cookie should fit.
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#getValue()
     */
    public static Criteria<Cookie> cookieValueMatches(String expression) {
        checkArgument(isNotBlank(expression), "Substring/RegEx pattern should be defined");

        return condition(format("has value that contains '%s' or fits regExp pattern '%s'", expression, expression), c ->
                ofNullable(c.getValue())
                        .map(s -> {
                            if (s.contains(expression)) {
                                return true;
                            }

                            try {
                                var p = compile(expression);
                                var mather = p.matcher(s);
                                return mather.matches() || mather.find();
                            } catch (Throwable thrown) {
                                thrown.printStackTrace();
                                return false;
                            }
                        })
                        .orElse(false));
    }


    /**
     * The checking of a browser cookie.
     *
     * @param domain is a cookie domain
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#getDomain()
     */
    public static Criteria<Cookie> cookieDomain(String domain) {
        checkArgument(isNotBlank(domain), "Domain should be defined");
        return condition(format("has domain '%s'", domain), c -> Objects.equals(domain, c.getDomain()));
    }

    /**
     * The checking of a browser cookie.
     *
     * @param expression is a substring of text that a cookie domain is supposed to have.
     *                   It is possible to pass reg exp pattern that domain of a cookie should fit.
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#getDomain()
     */
    public static Criteria<Cookie> cookieDomainMatches(String expression) {
        checkArgument(isNotBlank(expression), "Substring/RegEx pattern should be defined");

        return condition(format("has domain that contains '%s' or fits regExp pattern '%s'", expression, expression), c ->
                ofNullable(c.getDomain())
                        .map(s -> {
                            if (s.contains(expression)) {
                                return true;
                            }

                            try {
                                var p = compile(expression);
                                var mather = p.matcher(s);
                                return mather.matches() || mather.find();
                            } catch (Throwable thrown) {
                                thrown.printStackTrace();
                                return false;
                            }
                        })
                        .orElse(false));
    }


    /**
     * The checking of a browser cookie.
     *
     * @param path is a cookie path
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#getPath()
     */
    public static Criteria<Cookie> cookiePath(String path) {
        checkArgument(isNotBlank(path), "Path should be defined");
        return condition(format("has path '%s'", path), c -> Objects.equals(path, c.getPath()));
    }

    /**
     * The checking of a browser cookie.
     *
     * @param expression is a substring of text that a cookie path is supposed to have.
     *                   It is possible to pass reg exp pattern that path of a cookie should fit.
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#getPath()
     */
    public static Criteria<Cookie> cookiePathMatches(String expression) {
        checkArgument(isNotBlank(expression), "Substring/RegEx pattern should be defined");

        return condition(format("has path that contains '%s' or fits regExp pattern '%s'", expression, expression), c ->
                ofNullable(c.getPath())
                        .map(s -> {
                            if (s.contains(expression)) {
                                return true;
                            }

                            try {
                                var p = compile(expression);
                                var mather = p.matcher(s);
                                return mather.matches() || mather.find();
                            } catch (Throwable thrown) {
                                thrown.printStackTrace();
                                return false;
                            }
                        })
                        .orElse(false));
    }


    /**
     * The checking of a browser cookie. It is used to filter secure cookies.
     * The sample below shows how to make inverted criteria
     * <p>
     * {@code NOT(cookieIsSecure())}
     * </p>
     *
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#isSecure()
     * @see Criteria#NOT(Criteria[])
     */
    public static Criteria<Cookie> cookieIsSecure() {
        return condition("is secure", Cookie::isSecure);
    }

    /**
     * The checking of a browser cookie. It is used to filter http-only cookies.
     * The sample below shows how to make inverted criteria
     * <p>
     * {@code NOT(cookieIsHttpOnly())}
     * </p>
     *
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#isHttpOnly()
     * @see Criteria#NOT(Criteria[])
     */
    public static Criteria<Cookie> cookieIsHttpOnly() {
        return condition("is http only", Cookie::isHttpOnly);
    }


    /**
     * The checking of a browser cookie. It is used to filter cookies by expiry date.
     *
     * @param dateExpiry is expected expiry date
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#getExpiry()
     */
    public static Criteria<Cookie> cookieExpiry(Date dateExpiry) {
        checkNotNull(dateExpiry);
        return condition("date expiry is " + dateExpiry, c -> Objects.equals(c.getExpiry(), dateExpiry));
    }

    /**
     * The checking of a browser cookie. It is used to filter cookies by expiry date.
     *
     * @param before is upper border of a cookie expiry date
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#getExpiry()
     */
    public static Criteria<Cookie> cookieExpiryBefore(Date before) {
        checkNotNull(before);
        return condition("date expiry is before " + before,
                c -> ofNullable(c.getExpiry())
                        .map(date -> date.before(before))
                        .orElse(false));
    }

    /**
     * The checking of a browser cookie. It is used to filter cookies by expiry date.
     *
     * @param after is lower border of a cookie expiry date
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#getExpiry()
     */
    public static Criteria<Cookie> cookieExpiryAfter(Date after) {
        checkNotNull(after);
        return condition("date expiry is after " + after,
                c -> ofNullable(c.getExpiry())
                        .map(date -> date.after(after))
                        .orElse(false));
    }
}
