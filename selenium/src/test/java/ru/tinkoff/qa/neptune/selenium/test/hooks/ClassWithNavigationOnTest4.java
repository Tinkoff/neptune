package ru.tinkoff.qa.neptune.selenium.test.hooks;

import ru.tinkoff.qa.neptune.selenium.hooks.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.hooks.DefaultBrowserPage;
import ru.tinkoff.qa.neptune.selenium.hooks.ForceNavigation;
import ru.tinkoff.qa.neptune.selenium.hooks.PreventNavigationToDefaultURL;

@DefaultBrowserPage(at = "/{parameter1}/{parameter2}/{parameter3}?&p4={parameter4}" +
        "&p5={parameter5}" +
        "&p6={parameter6}" +
        "&p7={parameter7}")
public class ClassWithNavigationOnTest4 {

    @BrowserUrlVariable(name = "parameter1")
    private final int p1 = 1;

    @BrowserUrlVariable(name = "parameter2")
    private final String p2 = "ABC ";

    @BrowserUrlVariable(name = "parameter3", toEncodeForQueries = true)
    private final String p3 = "ABC ";

    @BrowserUrlVariable
    private final int parameter4 = 1;

    @BrowserUrlVariable(name = "parameter5", field = "p5")
    @BrowserUrlVariable(name = "parameter6", field = "p6", toEncodeForQueries = true)
    @BrowserUrlVariable(toEncodeForQueries = true, field = "p7")
    private final UrlVarPojo parameter7 = new UrlVarPojo();


    public void test1() {

    }

    @PreventNavigationToDefaultURL
    public void test2() {

    }

    @ForceNavigation(to = "{parameter1}" +
            "/{parameter2}" +
            "/{parameter3}?&p4={parameter4}" +
            "&p5={parameter5}" +
            "&p6={parameter6}" +
            "&p7={parameter7}")
    @PreventNavigationToDefaultURL
    public void test3() {

    }

    public static class UrlVarPojo {
        private final String p5 = "ABC ";

        private final String p6 = "ABCD ";

        private final int p7 = 1;

        @Override
        public String toString() {
            return "ABCDE ";
        }
    }
}
