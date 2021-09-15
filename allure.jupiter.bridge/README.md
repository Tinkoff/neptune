This a bridge which connects [Neptune Integration with Junit5](./../jupiter.integration/doc) and [Neptune Integration with Allure Framework](./../allure.integration/doc) via [Allure JUnit5](https://docs.qameta.io/allure/#_junit_5).

## Maven

```xml
    <dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>allure.jupiter.bridge</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

## Gradle

```groovy
    dependencies {
        testImplementation  group: 'ru.tinkoff.qa.neptune', name: 'allure.jupiter.bridge', version: LATEST_RELEASE_OR_BETA_VERSION    
    }
```

...and there is nothing special to do.
