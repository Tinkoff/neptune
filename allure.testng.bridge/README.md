This a bridge which connects [Neptune Integration with Testng](./../testng.integration/README.md) and [Neptune Integration with Allure Framework](./../allure.integration/README.md) via [Allure TestNG](https://docs.qameta.io/allure/#_testng).

## Maven

```xml
    <dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>allure.testng.bridge</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

## Gradle

```groovy
    dependencies {
        testImplementation  group: 'ru.tinkoff.qa.neptune', name: 'allure.testng.bridge', version: LATEST_RELEASE_OR_BETA_VERSION    
    }
```

...and there is nothing special to do.
