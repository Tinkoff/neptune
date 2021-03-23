package ru.tinkoff.qa.neptune.http.api.cookies;


import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;

import java.net.HttpCookie;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
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
    @Description("has name '{name}'")
    public static Criteria<HttpCookie> httpCookieName(@DescriptionFragment("name") String name) {
        checkArgument(isNotBlank(name), "Name should be defined");
        return condition(c -> Objects.equals(name, c.getName()));
    }

    /**
     * The checking of a cookie.
     *
     * @param expression is a substring of text that a cookie name is supposed to have.
     *                   It is possible to pass reg exp pattern that name of a cookie should fit.
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getName()
     */
    @Description("has name that contains '{expression}' or fits regExp pattern '{expression}'")
    public static Criteria<HttpCookie> httpCookieNameMatches(@DescriptionFragment("expression") String expression) {
        checkArgument(isNotBlank(expression), "Substring/RegEx pattern should be defined");

        return condition(c ->
                ofNullable(c.getName())
                        .map(s -> {
                            if (s.contains(expression)) {
                                return true;
                            }

                            try {
                                var p = compile(expression);
                                var mather = p.matcher(s);
                                return mather.matches();
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
    @Description("has value '{value}'")
    public static Criteria<HttpCookie> httpCookieValue(@DescriptionFragment("value") String value) {
        checkArgument(isNotBlank(value), "Value should be defined");
        return condition(c -> Objects.equals(value, c.getValue()));
    }

    /**
     * The checking of a cookie.
     *
     * @param expression is a substring of text that a cookie value is supposed to have.
     *                   It is possible to pass reg exp pattern that value of a cookie should fit.
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getValue()
     */
    @Description("has value that contains '{expression}' or fits regExp pattern '{expression}'")
    public static Criteria<HttpCookie> httpCookieValueMatches(@DescriptionFragment("expression") String expression) {
        checkArgument(isNotBlank(expression), "Substring/RegEx pattern should be defined");

        return condition(c ->
                ofNullable(c.getValue())
                        .map(s -> {
                            if (s.contains(expression)) {
                                return true;
                            }

                            try {
                                var p = compile(expression);
                                var mather = p.matcher(s);
                                return mather.matches();
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
    @Description("has comment '{comment}'")
    public static Criteria<HttpCookie> httpCookieComment(@DescriptionFragment("comment") String comment) {
        checkArgument(isNotBlank(comment), "Comment should be defined");
        return condition(c -> Objects.equals(comment, c.getComment()));
    }

    /**
     * The checking of a cookie.
     *
     * @param expression is a substring of text that a cookie comment is supposed to have.
     *                   It is possible to pass reg exp pattern that comment of a cookie should fit.
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getComment()
     */
    @Description("has comment that contains '{expression}' or fits regExp pattern '{expression}'")
    public static Criteria<HttpCookie> httpCookieCommentMatches(@DescriptionFragment("expression") String expression) {
        checkArgument(isNotBlank(expression), "Substring/RegEx pattern should be defined");

        return condition(c ->
                ofNullable(c.getComment())
                        .map(s -> {
                            if (s.contains(expression)) {
                                return true;
                            }

                            try {
                                var p = compile(expression);
                                var mather = p.matcher(s);
                                return mather.matches();
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
    @Description("has URL comment '{urlComment}'")
    public static Criteria<HttpCookie> httpCookieURLComment(@DescriptionFragment("urlComment") String urlComment) {
        checkArgument(isNotBlank(urlComment), "URL comment should be defined");
        return condition(c -> Objects.equals(urlComment, c.getCommentURL()));
    }

    /**
     * The checking of a cookie.
     *
     * @param expression is a substring of text that a cookie URL comment is supposed to have.
     *                   It is possible to pass reg exp pattern that URL comment of a cookie should fit.
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getCommentURL()
     */
    @Description("has URL comment that contains '{expression}' or fits regExp pattern '{expression}'")
    public static Criteria<HttpCookie> httpCookieURLCommentMatches(@DescriptionFragment("expression") String expression) {
        checkArgument(isNotBlank(expression), "Substring/RegEx pattern should be defined");

        return condition(c ->
                ofNullable(c.getCommentURL())
                        .map(s -> {
                            if (s.contains(expression)) {
                                return true;
                            }

                            try {
                                var p = compile(expression);
                                var mather = p.matcher(s);
                                return mather.matches();
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
    @Description("has port list '{portList}'")
    public static Criteria<HttpCookie> httpCookiePortList(@DescriptionFragment("portList") String portList) {
        checkArgument(isNotBlank(portList), "port list should be defined");
        return condition(c -> Objects.equals(portList, c.getPortlist()));
    }

    /**
     * The checking of a cookie.
     *
     * @param expression is a substring of text that a cookie port list is supposed to have.
     *                   It is possible to pass reg exp pattern that port list of a cookie should fit.
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getPortlist()
     */
    @Description("has port list that contains '{expression}' or fits regExp pattern '{expression}'")
    public static Criteria<HttpCookie> httpCookiePortListMatches(@DescriptionFragment("expression") String expression) {
        checkArgument(isNotBlank(expression), "Substring/RegEx pattern should be defined");

        return condition(c ->
                ofNullable(c.getPortlist())
                        .map(s -> {
                            if (s.contains(expression)) {
                                return true;
                            }

                            try {
                                var p = compile(expression);
                                var mather = p.matcher(s);
                                return mather.matches();
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
    @Description("has domain '{domain}'")
    public static Criteria<HttpCookie> httpCookieDomain(@DescriptionFragment("domain") String domain) {
        checkArgument(isNotBlank(domain), "Domain should be defined");
        return condition(c -> Objects.equals(domain, c.getDomain()));
    }

    /**
     * The checking of a cookie.
     *
     * @param expression is a substring of text that a cookie domain is supposed to have.
     *                   It is possible to pass reg exp pattern that domain of a cookie should fit.
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getDomain()
     */
    @Description("has domain that contains '{expression}' or fits regExp pattern '{expression}'")
    public static Criteria<HttpCookie> httpCookieDomainMatches(@DescriptionFragment("expression") String expression) {
        checkArgument(isNotBlank(expression), "Substring/RegEx pattern should be defined");

        return condition(c ->
                ofNullable(c.getDomain())
                        .map(s -> {
                            if (s.contains(expression)) {
                                return true;
                            }

                            try {
                                var p = compile(expression);
                                var mather = p.matcher(s);
                                return mather.matches();
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
    @Description("has path '{path}'")
    public static Criteria<HttpCookie> httpCookiePath(@DescriptionFragment("path") String path) {
        checkArgument(isNotBlank(path), "Path should be defined");
        return condition(c -> Objects.equals(path, c.getPath()));
    }

    /**
     * The checking of a cookie.
     *
     * @param expression is a substring of text that a cookie path is supposed to have.
     *                   It is possible to pass reg exp pattern that path of a cookie should fit.
     * @return criteria that checks/filters a cookie
     * @see HttpCookie#getPath()
     */
    @Description("has path that contains '{expression}' or fits regExp pattern '{expression}'")
    public static Criteria<HttpCookie> httpCookiePathMatches(@DescriptionFragment("expression") String expression) {
        checkArgument(isNotBlank(expression), "Substring/RegEx pattern should be defined");

        return condition(c ->
                ofNullable(c.getPath())
                        .map(s -> {
                            if (s.contains(expression)) {
                                return true;
                            }

                            try {
                                var p = compile(expression);
                                var mather = p.matcher(s);
                                return mather.matches();
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
    @Description("is secure")
    public static Criteria<HttpCookie> httpCookieIsSecure() {
        return condition(HttpCookie::getSecure);
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
    @Description("is http only")
    public static Criteria<HttpCookie> httpCookieIsHttpOnly() {
        return condition(HttpCookie::isHttpOnly);
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
    @Description("to discard")
    public static Criteria<HttpCookie> httpCookieToDiscard() {
        return condition(HttpCookie::getDiscard);
    }
}
