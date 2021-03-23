package ru.tinkoff.qa.neptune.selenium.functions.cookies;

import org.openqa.selenium.Cookie;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;

import java.util.Date;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
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
    @Description("has name '{name}'")
    public static Criteria<Cookie> cookieName(@DescriptionFragment("name") String name) {
        checkArgument(isNotBlank(name), "Name should be defined");
        return condition(c -> Objects.equals(name, c.getName()));
    }

    /**
     * The checking of a browser cookie.
     *
     * @param expression is a substring of text that a cookie name is supposed to have.
     *                   It is possible to pass reg exp pattern that name of a cookie should fit.
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#getName()
     */
    @Description("has name that contains '{expression}' or fits regExp pattern '{expression}'")
    public static Criteria<Cookie> cookieNameMatches(@DescriptionFragment("expression") String expression) {
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
     * The checking of a browser cookie.
     *
     * @param value is a cookie value
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#getValue()
     */
    @Description("has value '{value}'")
    public static Criteria<Cookie> cookieValue(@DescriptionFragment("value") String value) {
        checkArgument(isNotBlank(value), "Value should be defined");
        return condition(c -> Objects.equals(value, c.getValue()));
    }

    /**
     * The checking of a browser cookie.
     *
     * @param expression is a substring of text that a cookie value is supposed to have.
     *                   It is possible to pass reg exp pattern that value of a cookie should fit.
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#getValue()
     */
    @Description("has value that contains '{expression}' or fits regExp pattern '{expression}'")
    public static Criteria<Cookie> cookieValueMatches(@DescriptionFragment("expression") String expression) {
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
     * The checking of a browser cookie.
     *
     * @param domain is a cookie domain
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#getDomain()
     */
    @Description("has domain '{domain}'")
    public static Criteria<Cookie> cookieDomain(@DescriptionFragment("domain") String domain) {
        checkArgument(isNotBlank(domain), "Domain should be defined");
        return condition(c -> Objects.equals(domain, c.getDomain()));
    }

    /**
     * The checking of a browser cookie.
     *
     * @param expression is a substring of text that a cookie domain is supposed to have.
     *                   It is possible to pass reg exp pattern that domain of a cookie should fit.
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#getDomain()
     */
    @Description("has domain that contains '{expression}' or fits regExp pattern '{expression}'")
    public static Criteria<Cookie> cookieDomainMatches(@DescriptionFragment("expression") String expression) {
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
     * The checking of a browser cookie.
     *
     * @param path is a cookie path
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#getPath()
     */
    @Description("has path '{path}'")
    public static Criteria<Cookie> cookiePath(@DescriptionFragment("path") String path) {
        checkArgument(isNotBlank(path), "Path should be defined");
        return condition(c -> Objects.equals(path, c.getPath()));
    }

    /**
     * The checking of a browser cookie.
     *
     * @param expression is a substring of text that a cookie path is supposed to have.
     *                   It is possible to pass reg exp pattern that path of a cookie should fit.
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#getPath()
     */
    @Description("has path that contains '{expression}' or fits regExp pattern '{expression}'")
    public static Criteria<Cookie> cookiePathMatches(@DescriptionFragment("expression") String expression) {
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
    @Description("is secure")
    public static Criteria<Cookie> cookieIsSecure() {
        return condition(Cookie::isSecure);
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
    @Description("is http only")
    public static Criteria<Cookie> cookieIsHttpOnly() {
        return condition(Cookie::isHttpOnly);
    }


    /**
     * The checking of a browser cookie. It is used to filter cookies by expiry date.
     *
     * @param dateExpiry is expected expiry date
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#getExpiry()
     */
    @Description("date expiry is {dateExpiry}")
    public static Criteria<Cookie> cookieExpiry(@DescriptionFragment("dateExpiry") Date dateExpiry) {
        checkNotNull(dateExpiry);
        return condition(c -> Objects.equals(c.getExpiry(), dateExpiry));
    }

    /**
     * The checking of a browser cookie. It is used to filter cookies by expiry date.
     *
     * @param before is upper border of a cookie expiry date
     * @return criteria that checks/filters a browser cookie
     * @see Cookie#getExpiry()
     */
    @Description("date expiry is before {before}")
    public static Criteria<Cookie> cookieExpiryBefore(@DescriptionFragment("before") Date before) {
        checkNotNull(before);
        return condition(c -> ofNullable(c.getExpiry())
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
    @Description("date expiry is after {after}")
    public static Criteria<Cookie> cookieExpiryAfter(@DescriptionFragment("after") Date after) {
        checkNotNull(after);
        return condition(c -> ofNullable(c.getExpiry())
                .map(date -> date.after(after))
                .orElse(false));
    }
}
