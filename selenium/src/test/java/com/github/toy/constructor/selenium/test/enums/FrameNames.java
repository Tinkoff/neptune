package com.github.toy.constructor.selenium.test.enums;

public enum FrameNames {
    FRAME1("name1"),
    FRAME2("name2"),
    FRAME3("name3");

    private final String nameOrId;

    FrameNames(String nameOrId) {
        this.nameOrId = nameOrId;
    }

    public String getNameOrId() {
        return nameOrId;
    }
}
