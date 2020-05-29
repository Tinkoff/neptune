package ru.tinkoff.qa.neptune.http.api.cookies;


import ru.tinkoff.qa.neptune.core.api.steps.Criteria;

import java.net.HttpCookie;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

/**
 * Default criteria for {@link java.net.HttpCookie}
 *
 * @see ru.tinkoff.qa.neptune.core.api.steps.Criteria
 */
public final class CommonHttpCookieCriteria {

    private CommonHttpCookieCriteria() {
        super();
    }

    /**
     * The checking of a cookie.
     *
     * @param name is a cookie name
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getName()
     */
    public static Criteria<HttpCookie> httpCookieName(String name) {
        checkArgument(isNotBlank(name), "Name should be defined");
        return condition(format("has name '%s'", name), c -> Objects.equals(name, c.getName()));
    }

    /**
     * The checking of a cookie.
     *
     * @param expression is a substring of text that a cookie name is supposed to have.
     *                   It is possible to pass reg exp pattern that name of a cookie should fit.
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getName()
     */
    public static Criteria<HttpCookie> httpCookieNameMatches(String expression) {
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
     * The checking of a cookie.
     *
     * @param value is a cookie value
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getValue()
     */
    public static Criteria<HttpCookie> httpCookieValue(String value) {
        checkArgument(isNotBlank(value), "Value should be defined");
        return condition(format("has value '%s'", value), c -> Objects.equals(value, c.getValue()));
    }

    /**
     * The checking of a cookie.
     *
     * @param expression is a substring of text that a cookie value is supposed to have.
     *                   It is possible to pass reg exp pattern that value of a cookie should fit.
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getValue()
     */
    public static Criteria<HttpCookie> httpCookieValueMatches(String expression) {
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
     * The checking of a cookie.
     *
     * @param comment is a cookie comment
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getComment()
     */
    public static Criteria<HttpCookie> httpCookieComment(String comment) {
        checkArgument(isNotBlank(comment), "Comment should be defined");
        return condition(format("has comment '%s'", comment), c -> Objects.equals(comment, c.getComment()));
    }

    /**
     * The checking of a cookie.
     *
     * @param expression is a substring of text that a cookie comment is supposed to have.
     *                   It is possible to pass reg exp pattern that comment of a cookie should fit.
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getComment()
     */
    public static Criteria<HttpCookie> httpCookieCommentMatches(String expression) {
        checkArgument(isNotBlank(expression), "Substring/RegEx pattern should be defined");

        return condition(format("has comment that contains '%s' or fits regExp pattern '%s'", expression, expression), c ->
                ofNullable(c.getComment())
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
     * The checking of a cookie.
     *
     * @param urlComment is a cookie URL comment
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getCommentURL()
     */
    public static Criteria<HttpCookie> httpCookieURLComment(String urlComment) {
        checkArgument(isNotBlank(urlComment), "URL comment should be defined");
        return condition(format("has URL comment '%s'", urlComment), c -> Objects.equals(urlComment, c.getCommentURL()));
    }

    /**
     * The checking of a cookie.
     *
     * @param expression is a substring of text that a cookie URL comment is supposed to have.
     *                   It is possible to pass reg exp pattern that URL comment of a cookie should fit.
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getCommentURL()
     */
    public static Criteria<HttpCookie> httpCookieURLCommentMatches(String expression) {
        checkArgument(isNotBlank(expression), "Substring/RegEx pattern should be defined");

        return condition(format("has URL comment that contains '%s' or fits regExp pattern '%s'", expression, expression), c ->
                ofNullable(c.getCommentURL())
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
     * The checking of a cookie.
     *
     * @param portList is a cookie port list string value
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getPortlist()
     */
    public static Criteria<HttpCookie> httpCookiePortList(String portList) {
        checkArgument(isNotBlank(portList), "port list should be defined");
        return condition(format("has port list '%s'", portList), c -> Objects.equals(portList, c.getPortlist()));
    }

    /**
     * The checking of a cookie.
     *
     * @param expression is a substring of text that a cookie port list is supposed to have.
     *                   It is possible to pass reg exp pattern that port list of a cookie should fit.
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getPortlist()
     */
    public static Criteria<HttpCookie> httpCookiePortListMatches(String expression) {
        checkArgument(isNotBlank(expression), "Substring/RegEx pattern should be defined");

        return condition(format("has port list that contains '%s' or fits regExp pattern '%s'", expression, expression), c ->
                ofNullable(c.getPortlist())
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
     * The checking of a cookie.
     *
     * @param domain is a cookie domain
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getDomain()
     */
    public static Criteria<HttpCookie> httpCookieDomain(String domain) {
        checkArgument(isNotBlank(domain), "Domain should be defined");
        return condition(format("has domain '%s'", domain), c -> Objects.equals(domain, c.getDomain()));
    }

    /**
     * The checking of a cookie.
     *
     * @param expression is a substring of text that a cookie domain is supposed to have.
     *                   It is possible to pass reg exp pattern that domain of a cookie should fit.
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getDomain()
     */
    public static Criteria<HttpCookie> httpCookieDomainMatches(String expression) {
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
     * The checking of a cookie.
     *
     * @param path is a cookie path
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getPath()
     */
    public static Criteria<HttpCookie> httpCookiePath(String path) {
        checkArgument(isNotBlank(path), "Path should be defined");
        return condition(format("has path '%s'", path), c -> Objects.equals(path, c.getPath()));
    }

    /**
     * The checking of a cookie.
     *
     * @param expression is a substring of text that a cookie path is supposed to have.
     *                   It is possible to pass reg exp pattern that path of a cookie should fit.
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getPath()
     */
    public static Criteria<HttpCookie> httpCookiePathMatches(String expression) {
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
     * The checking of a cookie. It is used to filter secure cookies.
     * The sample below shows how to make inverted criteria
     * <p>
     * {@code NOT(httpCookieIsSecure())}
     * </p>
     *
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getSecure()
     * @see Criteria#NOT(Criteria[])
     */
    public static Criteria<HttpCookie> httpCookieIsSecure() {
        return condition("is secure", HttpCookie::getSecure);
    }

    /**
     * The checking of a cookie. It is used to filter http-only cookies.
     * The sample below shows how to make inverted criteria
     * <p>
     * {@code NOT(httpCookieIsHttpOnly())}
     * </p>
     *
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#isHttpOnly()
     * @see Criteria#NOT(Criteria[])
     */
    public static Criteria<HttpCookie> httpCookieIsHttpOnly() {
        return condition("is http only", HttpCookie::isHttpOnly);
    }

    /**
     * The checking of a cookie. It is used to filter cookies to be discarded unconditionally.
     * The sample below shows how to make inverted criteria
     * <p>
     * {@code NOT(httpCookieToDiscard())}
     * </p>
     *
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getDiscard()
     * @see Criteria#NOT(Criteria[])
     */
    public static Criteria<HttpCookie> httpCookieToDiscard() {
        return condition("to discard", HttpCookie::getDiscard);
    }
}
