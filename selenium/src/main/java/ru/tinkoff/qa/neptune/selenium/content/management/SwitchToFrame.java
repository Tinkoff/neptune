package ru.tinkoff.qa.neptune.selenium.content.management;

import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameSupplier;

import java.lang.annotation.Annotation;
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
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.*;
import static org.openqa.selenium.support.How.UNSET;
import static ru.tinkoff.qa.neptune.selenium.content.management.SwitchToFrame.FrameLocatorReader.readBy;
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
     * @return an id-attribute of a frame-element to switch to
     */
    String id() default EMPTY;

    /**
     * @return a name-attribute of a frame-element to switch to
     */
    String name() default EMPTY;

    /**
     * @return a class-attribute of a frame-element to switch to
     */
    String className() default EMPTY;

    /**
     * @return a css-property of a frame-element to switch to
     */
    String css() default EMPTY;

    /**
     * @return a tag name of a frame-element to switch to
     */
    String tagName() default EMPTY;

    /**
     * @return a link text of a frame-element to switch to
     */
    String linkText() default EMPTY;

    /**
     * @return a partial link text of a frame-element to switch to
     */
    String partialLinkText() default EMPTY;

    /**
     * @return xpath to a frame-element to switch to
     */
    String xpath() default EMPTY;

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

            var by = readBy(switchToFrame);

            if (switchToFrame.index() < 0
                    && isBlank(switchToFrame.nameOrId())
                    && isNull(by)) {
                throw new IllegalArgumentException(format("Frame to switch to is not defined. " +
                                "Annotation %s should have value of `index` that greater or equals to 0, " +
                                "or not blank value of `nameOrId`, " +
                                "or not blank value of only one of following attributes: id, name, className, css, " +
                                "tagName, linkText, partialLinkText, xpath" +
                                "Please check and correct annotations of %s",
                        SwitchToFrame.class.getName(),
                        annotatedElement));
            }

            var index = switchToFrame.index();
            var nameOrId = switchToFrame.nameOrId();

            if ((index >= 0 && (isNotBlank(nameOrId) || nonNull(by))) || (isNotBlank(nameOrId) && nonNull(by))) {
                throw new IllegalArgumentException(format("Frame to switch to is not correctly defined. " +
                                "Annotation %s should only have value of `index` that greater or equals to 0, " +
                                "or not blank value of `nameOrId`, " +
                                "or not blank value of only one of following attributes: id, name, className, css, " +
                                "tagName, linkText, partialLinkText, xpath" +
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

    class FrameLocatorReader extends FindBy.FindByBuilder {

        static By readBy(SwitchToFrame switchToFrame) {
            return new FrameLocatorReader().buildByFromShortFindBy(new FindBy() {

                @Override
                public Class<? extends Annotation> annotationType() {
                    return FindBy.class;
                }

                @Override
                public How how() {
                    return UNSET;
                }

                @Override
                public String using() {
                    return EMPTY;
                }

                @Override
                public String id() {
                    return switchToFrame.id();
                }

                @Override
                public String name() {
                    return switchToFrame.name();
                }

                @Override
                public String className() {
                    return switchToFrame.className();
                }

                @Override
                public String css() {
                    return switchToFrame.css();
                }

                @Override
                public String tagName() {
                    return switchToFrame.tagName();
                }

                @Override
                public String linkText() {
                    return switchToFrame.linkText();
                }

                @Override
                public String partialLinkText() {
                    return switchToFrame.partialLinkText();
                }

                @Override
                public String xpath() {
                    return switchToFrame.xpath();
                }
            });
        }
    }
}
