package ru.tinkoff.qa.neptune.selenium.test.enums;


import org.openqa.selenium.Dimension;

public enum InitialSizes {
    SIZE1(new Dimension(10, 20)),
    SIZE2(new Dimension(20, 30)),
    SIZE3(new Dimension(20, 30));

    private final Dimension size;

    InitialSizes(Dimension size) {
        this.size = size;
    }

    public Dimension getSize() {
        return size;
    }
}
