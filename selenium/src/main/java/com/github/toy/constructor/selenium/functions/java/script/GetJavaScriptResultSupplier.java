package com.github.toy.constructor.selenium.functions.java.script;

import com.github.toy.constructor.core.api.GetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.ToGetConditionalHelper.getSingleOnCondition;
import static com.github.toy.constructor.selenium.functions.java.script.EvaluateAsyncJavaScript.evalAsyncJS;
import static com.github.toy.constructor.selenium.functions.java.script.EvaluateJavaScript.evalJS;
import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isBlank;

public final class GetJavaScriptResultSupplier extends GetSupplier<SeleniumSteps, Object, GetJavaScriptResultSupplier> {

    private GetJavaScriptResultSupplier() {
        super();
    }

    private static void checkScript(String script) {
        checkArgument(!isBlank(script), "Script to be evaluated should not be null value or empty string");
    }

    private static void checkArguments(Object...arguments) {
        checkArgument(arguments != null, "Arguments to be used by script evaluation should not be null");
    }

    /**
     * This method builds a function which evaluates java script, checks the result by criteria and returns it.
     * IT IS IMPORTANT!!!! If script evaluation returns {@code null} and it is expected so it is good to make it
     * return some {@code boolean} value instead.
     *
     * The documentation below was taken from Selenium:
     * <>p</>
     * The script
     * fragment provided will be executed as the body of an anonymous function.
     *
     * <>p</>
     * Within the script, use <code>document</code> to refer to the current document. Note that local
     * variables will not be available once the script has finished executing, though global variables
     * will persist.
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
     * <p>
     * Arguments must be a number, a boolean, a String, WebElement, or a List of any combination of
     * the above. An exception will be thrown if the arguments do not meet these criteria. The
     * arguments will be made available to the JavaScript via the "arguments" magic variable, as if
     * the function were called via "Function.apply"
     *
     * @param script to be evaluated
     * @param criteria to check the result of script evaluation.
     * @param timeToGetResult time to evaluate script and get the result which suits the criteria.
     * @param timeToSleep time for the sleeping between attempts to get the desired result.
     * @param exceptionSupplier which returns the exception to be thrown when script returns the result of the evaluation
     *                          that doesn't suit the given criteria and time is expired.
     * @param arguments to be used by script evaluation
     * @return the function which evaluates java script, checks the result by criteria and returns it.
     */
    public static GetJavaScriptResultSupplier javaScript(String script, Predicate<Object> criteria,
                                                         Duration timeToGetResult,
                                                         Duration timeToSleep,
                                                         Supplier<RuntimeException> exceptionSupplier,
                                                         Object... arguments) {
        checkScript(script);
        checkArguments(arguments);
        return new GetJavaScriptResultSupplier()
                .set(getSingleOnCondition("Result",
                        evalJS(script, arguments), criteria, timeToGetResult, timeToSleep, true, exceptionSupplier));
    }

    /**
     * This method builds a function which evaluates java script, checks the result by criteria and returns it.
     * IT IS IMPORTANT!!!! If script evaluation returns {@code null} and it is expected so it is good to make it
     * return some {@code boolean} value instead.
     *
     * The documentation below was taken from Selenium:
     * <>p</>
     * The script
     * fragment provided will be executed as the body of an anonymous function.
     *
     * <>p</>
     * Within the script, use <code>document</code> to refer to the current document. Note that local
     * variables will not be available once the script has finished executing, though global variables
     * will persist.
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
     * <p>
     * Arguments must be a number, a boolean, a String, WebElement, or a List of any combination of
     * the above. An exception will be thrown if the arguments do not meet these criteria. The
     * arguments will be made available to the JavaScript via the "arguments" magic variable, as if
     * the function were called via "Function.apply"
     *
     * @param script to be evaluated
     * @param criteria to check the result of script evaluation.
     * @param timeToGetResult time to evaluate script and get the result which suits the criteria.
     * @param exceptionSupplier which returns the exception to be thrown when script returns the result of the evaluation
     *                          that doesn't suit the given criteria and time is expired.
     * @param arguments to be used by script evaluation
     * @return the function which evaluates java script, checks the result by criteria and returns it.
     */
    public static GetJavaScriptResultSupplier javaScript(String script, Predicate<Object> criteria,
                                                         Duration timeToGetResult,
                                                         Supplier<RuntimeException> exceptionSupplier,
                                                         Object... arguments) {
        checkScript(script);
        checkArguments(arguments);
        return new GetJavaScriptResultSupplier()
                .set(getSingleOnCondition("Result",
                        evalJS(script, arguments), criteria, timeToGetResult, true, exceptionSupplier));
    }


    /**
     * This method builds a function which evaluates java script, checks the result by criteria and returns it.
     * If the result of evaluation doesn't match criteria then function returns {@code null} when waiting time is
     * expired. IT IS IMPORTANT!!!! If script evaluation returns {@null} and it is expected so it is good to make it
     * return some {@code boolean} value instead.
     *
     * The documentation below was taken from Selenium:
     * <>p</>
     * The script
     * fragment provided will be executed as the body of an anonymous function.
     *
     * <>p</>
     * Within the script, use <code>document</code> to refer to the current document. Note that local
     * variables will not be available once the script has finished executing, though global variables
     * will persist.
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
     * <p>
     * Arguments must be a number, a boolean, a String, WebElement, or a List of any combination of
     * the above. An exception will be thrown if the arguments do not meet these criteria. The
     * arguments will be made available to the JavaScript via the "arguments" magic variable, as if
     * the function were called via "Function.apply"
     *
     * @param script to be evaluated
     * @param criteria to check the result of script evaluation
     * @param timeToGetResult time to evaluate script and get the result which suits the criteria.
     * @param arguments to be used by script evaluation
     * @return the function which evaluates java script, checks the result by criteria and returns it.
     * If the result of evaluation doesn't match criteria then function returns {@code null} when waiting time is
     * expired.
     */
    public static GetJavaScriptResultSupplier javaScript(String script, Predicate<Object> criteria,
                                                         Duration timeToGetResult,
                                                         Object... arguments) {
        checkScript(script);
        checkArguments(arguments);
        return new GetJavaScriptResultSupplier()
                .set(getSingleOnCondition("Result",
                        evalJS(script, arguments), criteria, timeToGetResult, true));
    }

    /**
     * This method builds a function which evaluates java script, checks the result by criteria and returns it.
     * If the result of evaluation doesn't match criteria then function returns {@code null}.
     * IT IS IMPORTANT!!!! If script evaluation returns {@null} and it is expected so it is good to make it
     * return some {@code boolean} value instead.
     *
     * The documentation below was taken from Selenium:
     * <>p</>
     * The script
     * fragment provided will be executed as the body of an anonymous function.
     *
     * <>p</>
     * Within the script, use <code>document</code> to refer to the current document. Note that local
     * variables will not be available once the script has finished executing, though global variables
     * will persist.
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
     * <p>
     * Arguments must be a number, a boolean, a String, WebElement, or a List of any combination of
     * the above. An exception will be thrown if the arguments do not meet these criteria. The
     * arguments will be made available to the JavaScript via the "arguments" magic variable, as if
     * the function were called via "Function.apply"
     *
     * @param script to be evaluated
     * @param criteria to check the result of script evaluation
     * @param arguments to be used by script evaluation
     * @return the function which evaluates java script, checks the result by criteria and returns it.
     * If the result of evaluation doesn't match criteria then function returns {@code null}.
     */
    public static GetJavaScriptResultSupplier javaScript(String script, Predicate<Object> criteria,
                                                         Object... arguments) {
        checkScript(script);
        checkArguments(arguments);
        return new GetJavaScriptResultSupplier()
                .set(getSingleOnCondition("Result",
                        evalJS(script, arguments), criteria, true));
    }

    /**
     * This methods builds a function which evaluates java script and returns the result as it is.
     *
     * The documentation below was taken from Selenium:
     * <>p</>
     * The script
     * fragment provided will be executed as the body of an anonymous function.
     *
     * <>p</>
     * Within the script, use <code>document</code> to refer to the current document. Note that local
     * variables will not be available once the script has finished executing, though global variables
     * will persist.
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
     * <p>
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
        return new GetJavaScriptResultSupplier().set(evalJS(script, arguments));
    }

    /**
     * This method builds a function which evaluates java asynchronous script, checks the result by criteria and returns it.
     * IT IS IMPORTANT!!!! If script evaluation returns {@code null} and it is expected so it is good to make it
     * return some {@code boolean} value instead.
     *
     * The documentation below was taken from Selenium:
     * <>p</>
     * Unlike executing {@link org.openqa.selenium.JavascriptExecutor#executeScript(String, Object...) synchronous JavaScript},
     * scripts executed with this method must explicitly signal they are finished by invoking the
     * provided callback. This callback is always injected into the executed function as the last
     * argument.
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
     * <p>
     * Script arguments must be a number, a boolean, a String, WebElement, or a List of any
     * combination of the above. An exception will be thrown if the arguments do not meet these
     * criteria. The arguments will be made available to the JavaScript via the "arguments"
     * variable.
     *
     * @param script to be evaluated
     * @param criteria to check the result of script evaluation
     * @param exceptionSupplier which returns the exception to be thrown when script returns the result of the evaluation
     *                          that doesn't suit the given criteria.
     * @param arguments to be used by script evaluation
     * @return the function which evaluates java script, checks the result by criteria and returns it.
     */
    public static GetJavaScriptResultSupplier asynchronousJavaScript(String script, Predicate<Object> criteria,
                                                                     Supplier<RuntimeException> exceptionSupplier,
                                                                     Object... arguments) {
        checkScript(script);
        checkArguments(arguments);
        return new GetJavaScriptResultSupplier()
                .set(getSingleOnCondition("Result",
                        evalAsyncJS(script, arguments), criteria, true, exceptionSupplier));
    }

    /**
     * This method builds a function which evaluates java asynchronous script, checks the result by criteria and returns it.
     * If the result of evaluation doesn't match criteria then function returns {@code null}.
     * IT IS IMPORTANT!!!! If script evaluation returns {@null} and it is expected so it is good to make it
     * return some {@code boolean} value instead.
     *
     * The documentation below was taken from Selenium:
     * <>p</>
     * Unlike executing {@link org.openqa.selenium.JavascriptExecutor#executeScript(String, Object...) synchronous JavaScript},
     * scripts executed with this method must explicitly signal they are finished by invoking the
     * provided callback. This callback is always injected into the executed function as the last
     * argument.
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
     * <p>
     * Script arguments must be a number, a boolean, a String, WebElement, or a List of any
     * combination of the above. An exception will be thrown if the arguments do not meet these
     * criteria. The arguments will be made available to the JavaScript via the "arguments"
     * variable.
     *
     * @param script to be evaluated
     * @param criteria to check the result of script evaluation
     * @param arguments to be used by script evaluation
     * @return the function which evaluates java script, checks the result by criteria and returns it.
     */
    public static GetJavaScriptResultSupplier asynchronousJavaScript(String script, Predicate<Object> criteria,
                                                                     Object... arguments) {
        checkScript(script);
        checkArguments(arguments);
        return new GetJavaScriptResultSupplier()
                .set(getSingleOnCondition("Result",
                        evalAsyncJS(script, arguments), criteria, true));
    }

    /**
     * This method builds a function which evaluates java asynchronous script and returns the result as it is.
     *
     * The documentation below was taken from Selenium:
     * <>p</>
     * Unlike executing {@link org.openqa.selenium.JavascriptExecutor#executeScript(String, Object...) synchronous JavaScript},
     * scripts executed with this method must explicitly signal they are finished by invoking the
     * provided callback. This callback is always injected into the executed function as the last
     * argument.
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
     * <p>
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
        return new GetJavaScriptResultSupplier().set(evalAsyncJS(script, arguments));
    }
}
