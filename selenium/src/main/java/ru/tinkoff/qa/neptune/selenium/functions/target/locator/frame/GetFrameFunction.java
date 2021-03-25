package ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

import static java.lang.String.format;

final class GetFrameFunction implements Function<WebDriver, Frame> {

    private final String description;
    private final Object frame;

    private GetFrameFunction(String description, Object frame) {
        this.description = description;
        this.frame = frame;
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     *
     * @param index index of the frame to switch to.
     * @return instance of {@link Function}
     */
    static GetFrameFunction index(int index) {
        return new GetFrameFunction(format("by index %s", index), index);
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     *
     * @param nameOrId name or id of the frame to switch to.
     * @return instance of {@link Function}
     */
    static GetFrameFunction nameOrId(String nameOrId) {
        return new GetFrameFunction(format("by name or id %s", nameOrId), nameOrId);
    }

    /**
     * Builds a function which performs switching to the frame and returns an instance of {@link Frame}.
     *
     * @param by is a {@link By}-strategy which describes how to find a frame-element to switch to
     * @return instance of {@link Function}
     */
    static GetFrameFunction by(By by) {
        return new GetFrameFunction(by.toString(), by);
    }

    @Override
    public String toString() {
        return description;
    }

    @Override
    public Frame apply(WebDriver webDriver) {
        try {
            return new Frame(webDriver, frame);
        } catch (NoSuchFrameException | NoSuchElementException e) {
            return null;
        }
    }
}
