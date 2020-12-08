# Интеграция с Allure Framework

Данный модуль используется для формирования данных для посдедующего построения Allure-отчетов

## Maven

В pom.xml

```xml
    <dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>allure.integration</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

## Gradle

В build.gradle

```groovy
    dependencies {
        testCompile group: 'ru.tinkoff.qa.neptune', name: 'allure.integration', version: LATEST_RELEASE_OR_BETA_VERSION    
    }
```



Описанных выше действий достаточно.
