package ru.tinkoff.qa.neptune.selenium.test;

import org.openqa.selenium.interactions.Sequence;

import java.util.Collection;

public final class SequenceSpy {

    private static Collection<Sequence> actions;

    private SequenceSpy() {
        super();
    }

    public static Collection<Sequence> getActions() {
        return actions;
    }

    public static void setActions(Collection<Sequence> actions) {
        SequenceSpy.actions = actions;
    }
}
