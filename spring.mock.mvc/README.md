Integration of Neptune with [Spring MockMvc](https://spring.getdocs.org/en-US/spring-framework-docs/docs/testing/integration-testing/spring-mvc-test-framework.html)

## Maven

```xml
    <dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>spring.mock.mvc</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

## Gradle

```groovy
    dependencies {
        testImplementation  group: 'ru.tinkoff.qa.neptune', name: 'spring.mock.mvc', version: LATEST_RELEASE_OR_BETA_VERSION
    }
```

[Краткая документация на русском](./doc/rus/README.MD)

[Brief documentation in English](./doc/eng/README.MD)

[API overview](https://tinkoffcreditsystems.github.io/neptune/spring.mock.mvc/index.html)