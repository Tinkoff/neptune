package com.github.toy.constructor.selenium.test.enums;

public enum WindowHandles {
    HANDLE1("handle1"),
    HANDLE2("handle2"),
    HANDLE3("handle3");

    private final String handle;

    WindowHandles(String handle) {
        this.handle = handle;
    }

    public String getHandle() {
        return handle;
    }
}
