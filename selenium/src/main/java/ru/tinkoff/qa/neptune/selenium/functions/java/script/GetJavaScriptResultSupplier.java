package ru.tinkoff.qa.neptune.selenium.functions.java.script;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.ConditionConcatenation;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;
import static ru.tinkoff.qa.neptune.selenium.functions.java.script.EvaluateAsyncJavaScript.evalAsyncJS;
import static ru.tinkoff.qa.neptune.selenium.functions.java.script.EvaluateJavaScript.evalJS;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

@MakeImageCapturesOnFinishing
@MakeStringCapturesOnFinishing
public final class GetJavaScriptResultSupplier extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<SeleniumStepContext, Object, WebDriver, GetJavaScriptResultSupplier> {


    private GetJavaScriptResultSupplier(String description, Function<WebDriver, Object> originalFunction) {
        super(description, originalFunction);
    }

    private static void checkScript(String script) {
        checkArgument(!isBlank(script), "Script to be evaluated should not be null value or empty string");
    }

    private static void checkArguments(Object...arguments) {
        checkArgument(nonNull(arguments), "Arguments to be used by script evaluation should not be null");
    }

    private static String getScriptDescription(String script, Object...parameters) {
        var description = format("Evaluation of java script '%s'", script);
        if (nonNull(parameters) && parameters.length > 0) {
            description = format("%s with parameters %s", description, Arrays.toString(parameters));
        }
        return description;
    }

    private static String getAsyncScriptDescription(String script, Object...parameters) {
        var description = format("Evaluation of asynchronous java script '%s'", script);
        if (nonNull(parameters) && parameters.length > 0) {
            description = format("%s with parameters %s", description, Arrays.toString(parameters));
        }
        return description;
    }

    /**
     * This methods builds a function which evaluates java script and returns the result as it is.
     *
     * The documentation below was taken from Selenium:
     * <p>
     * Executes JavaScript in the context of the currently selected frame or window. The script
     * fragment provided will be executed as the body of an anonymous function.
     *
     *
     * <p>
     * Within the script, use <code>document</code> to refer to the current document. Note that local
     * variables will not be available once the script has finished executing, though global variables
     * will persist.
     *
     *
     * <p>
     * If the script has a return value (i.e. if the script contains a <code>return</code> statement),
     * then the following steps will be taken:
     *
     * <ul>
     * <li>For an HTML element, this method returns a WebElement</li>
     * <li>For a decimal, a Double is returned</li>
     * <li>For a non-decimal number, a Long is returned</li>
     * <li>For a boolean, a Boolean is returned</li>
     * <li>For all other cases, a String is returned.</li>
     * <li>For an array, return a List&lt;Object&gt; with each object following the rules above. We
     * support nested lists.</li>
     * <li>For a map, return a Map&lt;String, Object&gt; with values following the rules above.</li>
     * <li>Unless the value is null or there is no return value, in which null is returned</li>
     * </ul>
     *
     *
     * Arguments must be a number, a boolean, a String, WebElement, or a List of any combination of
     * the above. An exception will be thrown if the arguments do not meet these criteria. The
     * arguments will be made available to the JavaScript via the "arguments" magic variable, as if
     * the function were called via "Function.apply"
     *
     * @param script to be evaluated
     * @param arguments to be used by script evaluation
     * @return the function which evaluates java script and returns the result as it is.
     */
    public static GetJavaScriptResultSupplier javaScript(String script, Object... arguments) {
        checkScript(script);
        checkArguments(arguments);
        return new GetJavaScriptResultSupplier(getScriptDescription(script, arguments), evalJS(script, arguments))
                .from(currentContent());
    }

    /**
     * This method builds a function which evaluates java asynchronous script and returns the result as it is.
     *
     * The documentation below was taken from Selenium:
     * <p>
     * Execute an asynchronous piece of JavaScript in the context of the currently selected frame or
     * window. Unlike executing {@link org.openqa.selenium.JavascriptExecutor#executeScript(String, Object...) synchronous JavaScript},
     * scripts executed with this method must explicitly signal they are finished by invoking the
     * provided callback. This callback is always injected into the executed function as the last
     * argument.
     * 
     *
     * <p>
     * The first argument passed to the callback function will be used as the script's result. This
     * value will be handled as follows:
     *
     * <ul>
     * <li>For an HTML element, this method returns a WebElement</li>
     * <li>For a number, a Long is returned</li>
     * <li>For a boolean, a Boolean is returned</li>
     * <li>For all other cases, a String is returned.</li>
     * <li>For an array, return a List&lt;Object&gt; with each object following the rules above. We
     * support nested lists.</li>
     * <li>For a map, return a Map&lt;String, Object&gt; with values following the rules above.</li>
     * <li>Unless the value is null or there is no return value, in which null is returned</li>
     * </ul>
     * 
     *
     * <p>
     * The default timeout for a script to be executed is 0ms. In most cases, including the examples
     * below, one must set the script timeout
     * {@link WebDriver.Timeouts#setScriptTimeout(long, java.util.concurrent.TimeUnit)}  beforehand
     * to a value sufficiently large enough.
     * 
     *
     * <p>
     * Example #1: Performing a sleep in the browser under test. <pre>{@code
     *   long start = System.currentTimeMillis();
     *   ((JavascriptExecutor) driver).executeAsyncScript(
     *       "window.setTimeout(arguments[arguments.length - 1], 500);");
     *   System.out.println(
     *       "Elapsed time: " + System.currentTimeMillis() - start);
     * }</pre>
     * 
     *
     * <p>
     * Example #2: Synchronizing a test with an AJAX application: <pre>{@code
     *   WebElement composeButton = driver.findElement(By.id("compose-button"));
     *   composeButton.click();
     *   ((JavascriptExecutor) driver).executeAsyncScript(
     *       "var callback = arguments[arguments.length - 1];" +
     *       "mailClient.getComposeWindowWidget().onload(callback);");
     *   driver.switchTo().frame("composeWidget");
     *   driver.findElement(By.id("to")).sendKeys("bog@example.com");
     * }</pre>
     * 
     *
     * <p>
     * Example #3: Injecting a XMLHttpRequest and waiting for the result: <pre>{@code
     *   Object response = ((JavascriptExecutor) driver).executeAsyncScript(
     *       "var callback = arguments[arguments.length - 1];" +
     *       "var xhr = new XMLHttpRequest();" +
     *       "xhr.open('GET', '/resource/data.json', true);" +
     *       "xhr.onreadystatechange = function() {" +
     *       "  if (xhr.readyState == 4) {" +
     *       "    callback(xhr.responseText);" +
     *       "  }" +
     *       "};" +
     *       "xhr.send();");
     *   JsonObject json = new JsonParser().parse((String) response);
     *   assertEquals("cheese", json.get("food").getAsString());
     * }</pre>
     *
     * 
     *
     * Script arguments must be a number, a boolean, a String, WebElement, or a List of any
     * combination of the above. An exception will be thrown if the arguments do not meet these
     * criteria. The arguments will be made available to the JavaScript via the "arguments"
     * variable.
     *
     * @param script to be evaluated
     * @param arguments to be used by script evaluation
     * @return the function which evaluates java script and returns the result as it is.
     */
    public static GetJavaScriptResultSupplier asynchronousJavaScript(String script,
                                                                     Object... arguments) {
        checkScript(script);
        checkArguments(arguments);
        return new GetJavaScriptResultSupplier(getAsyncScriptDescription(script, arguments),
                evalAsyncJS(script, arguments)).from(currentContent());
    }

    @Override
    public GetJavaScriptResultSupplier criteria(ConditionConcatenation concat, Predicate<? super Object> condition) {
        return super.criteria(concat, condition);
    }

    @Override
    public GetJavaScriptResultSupplier criteria(ConditionConcatenation concat, String conditionDescription, Predicate<? super Object> condition) {
        return super.criteria(concat, conditionDescription, condition);
    }

    @Override
    public GetJavaScriptResultSupplier criteria(Predicate<? super Object> condition) {
        return super.criteria(condition);
    }

    public GetJavaScriptResultSupplier criteria(String conditionDescription, Predicate<? super Object> condition) {
        return super.criteria(conditionDescription, condition);
    }

    @Override
    public GetJavaScriptResultSupplier timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public GetJavaScriptResultSupplier pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    @Override
    public GetJavaScriptResultSupplier throwOnEmptyResult(Supplier<? extends RuntimeException> exceptionSupplier) {
        return super.throwOnEmptyResult(exceptionSupplier);
    }
}