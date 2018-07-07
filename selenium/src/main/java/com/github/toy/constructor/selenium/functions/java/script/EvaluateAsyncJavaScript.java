package com.github.toy.constructor.selenium.functions.java.script;

import com.github.toy.constructor.selenium.SeleniumSteps;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

import static java.lang.String.format;

final class EvaluateAsyncJavaScript implements Function<WebDriver, Object> {

    private final String script;
    private final Object[] parameters;

    private EvaluateAsyncJavaScript(String script, Object[] parameters) {
        this.script = script;
        this.parameters = parameters;
    }

    static Function<WebDriver, Object> evalAsyncJS(String script, Object... parameters) {
        return  new EvaluateAsyncJavaScript(script, parameters);
    }

    @Override
    public Object apply(WebDriver driver) {
        if (!JavascriptExecutor.class.isAssignableFrom(driver.getClass())) {
            throw new UnsupportedOperationException(format("%s does not implement %s. Can't perform " +
                            "evaluation of the script '%s' asynchronously", driver.getClass(),
                    JavascriptExecutor.class.getName(), script));
        }

        JavascriptExecutor executor = JavascriptExecutor.class.cast(driver);
        return executor.executeAsyncScript(script, parameters);
    }
}
