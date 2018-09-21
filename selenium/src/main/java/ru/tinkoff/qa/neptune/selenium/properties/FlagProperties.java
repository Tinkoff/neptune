package ru.tinkoff.qa.neptune.selenium.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;
import org.openqa.selenium.WebDriver;

public enum FlagProperties implements PropertySupplier<Boolean> {
    /**
     * This enum item reads the property {@code 'find.only.visible.elements.when.no.conditions'}.
     * That means that the searching for some element finds only visible elements when the property
     * is defined and has value {@code true} in case if user doesn't define any match criteria of the
     * searching. Otherwise any suitable elements are found.
     */
    FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION("find.only.visible.elements.when.no.condition"),

    /**
     * This enum item reads the property {@code 'keep.web.driver.session.opened'}. It means that
     * a {@link org.openqa.selenium.WebDriver} session which has been opened is kept until all tests
     * are finished if the property is defined and has value {@code true}.
     */
    KEEP_WEB_DRIVER_SESSION_OPENED("keep.web.driver.session.opened"),

    /**
     * This enum item reads the property {@code 'clear.web.driver.cookies'}. It means that it
     * will try to clear {@link org.openqa.selenium.WebDriver} cookies after the test running has finished if the
     * property is defined and has value {@code true}. This option has sense if {@code 'keep.web.driver.session.opened'}
     * has value {@code true}.
     * @see #KEEP_WEB_DRIVER_SESSION_OPENED
     * @see WebDriver.Options#deleteAllCookies()
     */
    CLEAR_WEB_DRIVER_COOKIES("clear.web.driver.cookies"),

    /**
     * This enum item reads the property {@code 'get.back.to.base.web.driver.url'}. It means that it
     * will try to get to defined URL which is supposed to be loaded in browser if the property is defined and
     * has value {@code true}. This option has sense if {@code 'keep.web.driver.session.opened'} has value {@code true}.
     * @see #KEEP_WEB_DRIVER_SESSION_OPENED
     * @see URLProperties#BASE_WEB_DRIVER_URL_PROPERTY
     * @see WebDriver#get(String)
     */
    GET_BACK_TO_BASE_URL("get.back.to.base.web.driver.url"),

    /**
     * This enum item reads the property {@code 'force.window.maximizing.on.start'}. It means that browser window is
     * maximized on each starting when value is {@code true}.
     */
    FORCE_WINDOW_MAXIMIZING_ON_START("force.window.maximizing.on.start"),

    /**
     * This enum item reads the property {@code 'enable.ability.to.use.relative.url'}. It means that it is possible to
     * navigate with only part of an url. It should be relative to value defined by {@link URLProperties#BASE_WEB_DRIVER_URL_PROPERTY}.
     * <p>Use case:</p>
     * <p>{@code seleniumSteps.navigate(toUrl("/relative/url/path.html")}</p>
     * <p>WARNING: it is necessary to define {@link URLProperties#BASE_WEB_DRIVER_URL_PROPERTY}</p>
     */
    ENABLE_ABILITY_TO_USE_RELATIVE_URL("enable.ability.to.use.relative.url");

    private final String propertyName;

    FlagProperties(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public Boolean get() {
        return returnOptionalFromEnvironment()
                .map(Boolean::parseBoolean).orElse(false);
    }
}
