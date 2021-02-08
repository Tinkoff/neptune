# Интеграция с Selenium WebDriver

Данный модуль предоставляет интеграцию с Selenium WebDriver.

## Maven

В pom.xml

```xml
    <dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>selenium</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

## Gradle

В build.gradle

```groovy
    dependencies {
        testCompile group: 'ru.tinkoff.qa.neptune', name: 'selenium', version: LATEST_RELEASE_OR_BETA_VERSION    
    }
```

## Оглавление. Описание дальнейшей работы

[Настройки](./SETTINGS.MD)

Работа с элементами:
- [Виджеты](./WIDGET.MD)
- [Поиск элементов](./ELEMENTS_SEARCHING.MD)
- [Клик по элементам](./CLICK.MD)
- [Редактирование элементов](./EDIT.MD)
- [Свертывание и развертывание](./EXPAND_COLLAPSE.MD)  
- [Значение элементов](./ELEMENT_VALUE.MD)
- [Атрибуты элементов](./ELEMENT_ATTR.MD)
- [CSS элементов](./ELEMENT_CSS.MD)
- [Авто-скроллирование](./AUTO_SCROLLING.MD)

[Выполнение javascript](./JAVA_SCRIPT.MD)

Переключение/смена контента: 
- [Работа с окнами/вкладками браузера](./WINDOWS.MD)
- [Навигация по страницам](./NAVIGATION.MD)
- [Переключение во фреймы](./FRAMES.MD)
- [Работа с алертами](./ALERTS.MD)  
- [Переключение контента. Прочее.](./TARGET_LOCATOR_OTHER.MD)
- [Переключение контента/управление контентом](./CONTENT_MANAGEMENT.MD)

[Работа с cookie браузера](./COOKIES.MD)

[Интерактивные действия](./INTERACTION.MD)

[Логин / Аутентификация в браузере](./LOGIN_IN_BROWSER.MD)

[Прокси для браузера](./BROWSER_PROXY.MD)

[Дополнительные матчеры](./HAMCREST.MD)

