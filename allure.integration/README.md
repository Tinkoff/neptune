Integration of Neptune with [Allure test reporting framework](https://docs.qameta.io/allure/)

## Maven

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

```groovy
    dependencies {
        testImplementation  group: 'ru.tinkoff.qa.neptune', name: 'allure.integration', version: LATEST_RELEASE_OR_BETA_VERSION    
    }
```

...and there is nothing special to do.

[API overview](https://tinkoff.github.io/neptune/allure.integration/index.html)