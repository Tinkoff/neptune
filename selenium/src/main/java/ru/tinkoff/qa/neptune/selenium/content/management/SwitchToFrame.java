package ru.tinkoff.qa.neptune.selenium.content.management;

import org.openqa.selenium.By;
import org.openqa.selenium.support.How;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameSupplier;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static java.lang.String.format;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.*;
import static org.openqa.selenium.support.How.UNSET;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameSupplier.frame;

/**
 * Defines a frame to switch to. Sequence of SwitchToFrame annotations defines a frame path to
 * target content
 */
@Retention(RUNTIME)
@Target({METHOD, TYPE})
@Repeatable(Frames.class)
public @interface SwitchToFrame {

    /**
     * @return index of a frame to switch to
     */
    int index() default -1;

    /**
     * @return name or id of a frame to switch to
     */
    String nameOrId() default EMPTY;

    /**
     * @return a locator strategy to find a frame-element to switch to
     */
    How howToFindFrameElement() default UNSET;

    /**
     * @return a value for locator strategy defined by {@link #howToFindFrameElement()}
     */
    String locatorValue() default EMPTY;

    /**
     * @return a unit of a time to wait for a frame to switch to. Default is {@link ChronoUnit#SECONDS}
     */
    ChronoUnit waitingTimeUnit() default SECONDS;

    /**
     * @return value of a time to wait for a frame to switch to
     */
    long waitingTime() default 0;

    /**
     * Util class which is used to read metadata of some class/method and create an instance of {@link GetFrameSupplier}
     */
    class SwitchToFrameReader {

        static GetFrameSupplier getFrame(SwitchToFrame switchToFrame, AnnotatedElement annotatedElement) {
            if (switchToFrame.index() < 0
                    && isBlank(switchToFrame.nameOrId())
                    && switchToFrame.howToFindFrameElement().equals(UNSET)) {
                throw new IllegalArgumentException(format("Frame to switch to is not defined. " +
                                "Annotation %s should have value of `index` that greater or equals to 0, " +
                                "or not blank value of `nameOrId`, " +
                                "or `howToFindFrameElement` that differs from UNSET and not blank value of `locatorValue`. " +
                                "Please check and correct annotations of %s",
                        SwitchToFrame.class.getName(),
                        annotatedElement));
            }

            var index = switchToFrame.index();
            var nameOrId = switchToFrame.nameOrId();
            By by;
            if (isNotBlank(switchToFrame.locatorValue())) {
                if (switchToFrame.howToFindFrameElement().equals(UNSET)) {
                    throw new UnsupportedOperationException(format("It is not possible to create an instance of %s because " +
                                    "type of a locator is not defined. " +
                                    "Please check and correct annotations of %s. " +
                                    "It is necessary to define value of `howToFindFrameElement` when " +
                                    "some WebElement is a frame to switch to",
                            By.class.getName(),
                            annotatedElement));
                } else {
                    by = switchToFrame.howToFindFrameElement().buildBy(switchToFrame.locatorValue());
                }
            } else {
                if (!switchToFrame.howToFindFrameElement().equals(UNSET)) {
                    throw new UnsupportedOperationException(format("It is not possible to create an instance of %s because " +
                                    "value a locator is not defined. " +
                                    "Please check and correct annotations of %s. " +
                                    "It is necessary to define value of `locatorValue` when " +
                                    "some WebElement is a frame to switch to",
                            By.class.getName(),
                            annotatedElement));
                } else {
                    by = null;
                }
            }

            if ((index >= 0 && (isNotBlank(nameOrId) || nonNull(by))) || (isNotBlank(nameOrId) && nonNull(by))) {
                throw new IllegalArgumentException(format("Frame to switch to is not correctly defined. " +
                                "Annotation %s should only have value of `index` that greater or equals to 0, " +
                                "or not blank value of `nameOrId`, " +
                                "or `howToFindFrameElement` that differs from UNSET and not blank value of `locatorValue`. " +
                                "Please check and correct annotations of %s",
                        SwitchToFrame.class.getName(),
                        annotatedElement));
            }

            GetFrameSupplier result;
            if (index >= 0) {
                result = frame(index);
            } else if (isNotBlank(nameOrId)) {
                result = frame(nameOrId);
            } else {
                result = frame(by);
            }

            if (switchToFrame.waitingTime() > 0) {
                result = result.timeOut(Duration.of(switchToFrame.waitingTime(), switchToFrame.waitingTimeUnit()));
            }

            return result;
        }
    }
}
