package com.github.toy.constructor.selenium.test.function.descriptions;

import com.github.toy.constructor.selenium.functions.target.locator.window.Window;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

import java.net.URL;

public class DescribedWindow implements Window {
    @Override
    public void close() {

    }

    @Override
    public boolean isPresent() {
        return false;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getCurrentUrl() {
        return null;
    }

    @Override
    public void switchToMe() {

    }

    @Override
    public void back() {

    }

    @Override
    public void forward() {

    }

    @Override
    public void to(String url) {

    }

    @Override
    public void to(URL url) {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void setSize(Dimension targetSize) {

    }

    @Override
    public void setPosition(Point targetPosition) {

    }

    @Override
    public Dimension getSize() {
        return null;
    }

    @Override
    public Point getPosition() {
        return null;
    }

    @Override
    public void maximize() {

    }

    @Override
    public void fullscreen() {

    }

    @Override
    public WebDriver getWrappedDriver() {
        return null;
    }

    @Override
    public String toString() {
        return "Test stab window";
    }
}
