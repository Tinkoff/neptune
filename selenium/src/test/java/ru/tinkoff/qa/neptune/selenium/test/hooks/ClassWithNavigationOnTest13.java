package ru.tinkoff.qa.neptune.selenium.test.hooks;

import ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;

@Navigate(to = "/{parameter1}/{parameter2}/{parameter3}?&p4={parameter4}" +
        "&p5={parameter5}" +
        "&p6={parameter6}" +
        "&p7={parameter7}")
public class ClassWithNavigationOnTest13 {

    private final int p1 = 1;

    private final String p2 = "ABC ";

    private final String p3 = "ABC ";

    @BrowserUrlVariable
    private final int parameter4 = 1;

    @BrowserUrlVariable(name = "parameter5", method = "getP5", toEncodeForQueries = false)
    @BrowserUrlVariable(name = "parameter6", method = "getP6")
    private final UrlVarPojo p7 = new UrlVarPojo();


    public void test1() {
    }

    @Navigate(to = "{parameter1}" +
            "/{parameter2}" +
            "/{parameter3}?&p4={parameter4}" +
            "&p5={parameter5}" +
            "&p6={parameter6}" +
            "&p7={parameter7}")
    public void test2() {
    }

    @BrowserUrlVariable(name = "parameter3")
    public String getP3() {
        return p3;
    }

    @BrowserUrlVariable(name = "parameter2")
    public String getP2() {
        return p2;
    }

    @BrowserUrlVariable(name = "parameter1")
    public int getP1() {
        return p1;
    }

    @BrowserUrlVariable
    public UrlVarPojo parameter7() {
        return p7;
    }

    public static class UrlVarPojo {
        private final String p5 = "ABC ";

        private final String p6 = "ABCD ";

        private final int p7 = 1;

        @Override
        public String toString() {
            return "ABCDE ";
        }

        public String getP5() {
            return p5;
        }

        public String getP6() {
            return p6;
        }

        public int getP7() {
            return p7;
        }
    }
}
