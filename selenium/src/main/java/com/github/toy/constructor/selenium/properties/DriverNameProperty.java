package com.github.toy.constructor.selenium.properties;

import org.openqa.selenium.remote.BrowserType;

public enum DriverNameProperty {
    /**
     * remote
     */
    REMOTE_NAME("remote"),

    /**
     * The same name as {@link BrowserType#CHROME}
     */
    CHROME_NAME(BrowserType.CHROME),

    /**
     * The same name as {@link BrowserType#EDGE}
     */
    EDGE_NAME(BrowserType.EDGE),

    /**
     * The same name as {@link BrowserType#FIREFOX}
     */
    FIREFOX_NAME(BrowserType.FIREFOX),

    /**
     * The same name as {@link BrowserType#IEXPLORE}
     */
    IE_NAME(BrowserType.IEXPLORE),

    /**
     * The same name as {@link BrowserType#OPERA_BLINK}
     */
    OPERA_NAME(BrowserType.OPERA_BLINK),

    /**
     * The same name as {@link BrowserType#SAFARI}
     */
    SAFARI_NAME(BrowserType.SAFARI),

    /**
     * The same name as {@link BrowserType#PHANTOMJS}
     */
    PHANTOM_JS_NAME(BrowserType.PHANTOMJS);

    private final String name;

    DriverNameProperty(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
