package ru.tinkoff.qa.neptune.selenium.properties;

import org.openqa.selenium.Cookie;

import java.util.List;
import java.util.function.Supplier;

public interface RequiredCookieSupplier extends Supplier<List<Cookie>> {
}
