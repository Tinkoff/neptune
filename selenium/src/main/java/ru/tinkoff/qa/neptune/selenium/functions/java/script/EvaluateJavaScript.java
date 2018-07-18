package ru.tinkoff.qa.neptune.selenium.functions.java.script;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

import static java.lang.String.format;

final class EvaluateJavaScript implements Function<WebDriver, Object> {

    private final String script;
    private final Object[] parameters;

    private EvaluateJavaScript(String script, Object... parameters) {
        this.script = script;
        this.parameters = parameters;
    }

    static Function<WebDriver, Object> evalJS(String script, Object... parameters) {
        return new EvaluateJavaScript(script, parameters);
    }

    @Override
    public Object apply(WebDriver driver) {
        if (!JavascriptExecutor.class.isAssignableFrom(driver.getClass())) {
            throw new UnsupportedOperationException(format("%s does not implement %s. Can't perform " +
                    "evaluation of the script '%s'", driver.getClass(),
                    JavascriptExecutor.class.getName(), script));
        }

        JavascriptExecutor executor = JavascriptExecutor.class.cast(driver);
        return executor.executeScript(script, parameters);
    }
}
