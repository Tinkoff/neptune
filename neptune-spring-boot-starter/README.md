This module contains additional [auto-configurations](https://docs.spring.io/spring-boot/docs/2.1.13.RELEASE/reference/html/boot-features-developing-auto-configuration.html) 
of  [Spring Boot](https://spring.io/guides/gs/spring-boot/) for the testing.

## Standalone dependency configuration

### Maven

```xml
    <dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>neptune-spring-boot-starter</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

### Gradle

```groovy
    dependencies {
        testImplementation  group: 'ru.tinkoff.qa.neptune', name: 'neptune-spring-boot-starter', version: LATEST_RELEASE_OR_BETA_VERSION    
    }
```

## As a transitive dependency

This module is provided as a transitive dependency when

- there is a dependency on [Spring Data](./../spring.data/README.md)
- there is a dependency on [Spring MockMvc](./../spring.mock.mvc/README.md)
- there is a dependency on [Spring WebTestClient](./../spring.web.testclient/README.md)

[Краткая документация на русском](./doc/rus/README.MD)

[Brief documentation in English](./doc/eng/README.MD)

[API overview](https://tinkoffcreditsystems.github.io/neptune/neptune-spring-boot-starter/index.html)
