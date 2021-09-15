Integration of Neptune with [Junit5](https://junit.org/junit5/docs/current/user-guide/)

## Maven

```xml
    <dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>jupiter.integration</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

## Gradle

```groovy
    dependencies {
        testImplementation  group: 'ru.tinkoff.qa.neptune', name: 'jupiter.integration', version: LATEST_RELEASE_OR_BETA_VERSION    
    }
```

[Краткая документация на русском](./doc/rus/README.MD)

[Brief documentation in English](./doc/eng/README.MD)

[API overview](https://tinkoffcreditsystems.github.io/neptune/jupiter.integration/index.html)
