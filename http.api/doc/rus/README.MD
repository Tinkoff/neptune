# Интеграция с нативным http клиентом Java (с v11)

Данный модуль предоставляет удобную функциональность поверх нативного клиента Java.

## Maven

В pom.xml

```xml
    <dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>http.api</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

## Gradle

В build.gradle

```groovy
    dependencies {
        testCompile group: 'ru.tinkoff.qa.neptune', name: 'http.api', version: LATEST_RELEASE_OR_BETA_VERSION    
    }
```

## Оглавление. Описание дальнейшей работы

[Настройки](./SETTINGS.MD)

[Отправка http запросов/получение ответов](./REQUEST_RESPONSE.MD)

[Работа с cookies](./COOKIES.MD)

[Мэппинг http-сервисов](./MAPPING.MD)

[Дополнительные матчеры](./HAMCREST.MD)