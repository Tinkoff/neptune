package com.github.toy.constructor.selenium.test.steps.enums;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.*;

import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Arrays.stream;

public enum Scripts {
    SCRIPT_1("Script #1") {
        @Override
        public String execute(Object... args) {
            return String.join(",", stream(args).map(Object::toString).collect(Collectors.toList()));
        }
    },

    SCRIPT_2("Script #2") {
        @Override
        public String execute(Object... args) {
            throw new WebDriverException(format("It is not possible to execute script %s with parameters %s",
                    getScript(),  ArrayUtils.toString(args)));
        }
    };

    private final String script;

    Scripts(String script) {
        this.script = script;
    }

    public abstract Object execute(Object... args);

    public String getScript() {
        return script;
    }
}
