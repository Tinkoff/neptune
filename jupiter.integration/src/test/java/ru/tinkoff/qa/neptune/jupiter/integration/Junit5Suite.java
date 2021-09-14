package ru.tinkoff.qa.neptune.jupiter.integration;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({Junit5StubTest.class, Junit5ParalleledTest.class})
public class Junit5Suite {
}
