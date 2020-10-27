package ru.tinkoff.qa.neptune.selenium.hooks;


public enum DefaultNavigationStrategies {
    /**
     * Navigation to default URL should be performed on every test method which is not marked
     * by {@link PreventGettingBackToDefaultPage} and {@link ForceNavigation}
     */
    ON_EVERY_TEST,

    /**
     * Navigation to default URL should be performed on every (test or fixture) method which is not marked
     * by {@link PreventGettingBackToDefaultPage} and {@link ForceNavigation}
     */
    ON_EVERY_METHOD,
}
