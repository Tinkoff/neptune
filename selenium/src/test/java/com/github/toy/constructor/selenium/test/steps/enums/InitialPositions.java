package com.github.toy.constructor.selenium.test.steps.enums;


import org.openqa.selenium.Point;

public enum  InitialPositions {
    POSITION_1(new Point(100, 150)),
    POSITION_2(new Point(150, 250)),
    POSITION_3(new Point(250, 350));

    private final Point position;

    InitialPositions(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }
}
