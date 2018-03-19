package com.github.toy.constructor.selenium.functions.java.script;

import com.github.toy.constructor.selenium.SeleniumSteps;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static java.lang.String.format;

final class EvaluateAsyncJavaScript implements Function<SeleniumSteps, Object> {

    private final String script;
    private final Object[] parameters;

    private EvaluateAsyncJavaScript(String script, Object[] parameters) {
        this.script = script;
        this.parameters = parameters;
    }

    static Function<SeleniumSteps, Object> evalAsyncJS(String script, Object... parameters) {
        String description = format("Evaluation of asynchronous java script '%s'", script);
        if (parameters.length > 0) {
            description = format("%s with parameters %s", description, Arrays.toString(parameters));
        }
        return toGet(description, new EvaluateAsyncJavaScript(script, parameters));
    }

    @Override
    public Object apply(SeleniumSteps seleniumSteps) {
        WebDriver driver = seleniumSteps.getWrappedDriver();
        if (!JavascriptExecutor.class.isAssignableFrom(driver.getClass())) {
            throw new UnsupportedOperationException(format("%s does not implement %s. Can't perform " +
                            "evaluation of the script '%s' asynchronously", driver.getClass(),
                    JavascriptExecutor.class.getName(), script));
        }

        JavascriptExecutor executor = JavascriptExecutor.class.cast(driver);
        return executor.executeAsyncScript(script, parameters);
    }
}
