This module contains additional [auto-configurations](https://docs.spring.io/spring-boot/docs/2.1.13.RELEASE/reference/html/boot-features-developing-auto-configuration.html) 
of  [Spring Boot](https://spring.io/guides/gs/spring-boot/) for the testing with [MockMVC](./../spring.mock.mvc/README.md).
It is extension of [Neptune Spring Boot starter module](./../neptune-spring-boot-starter/README.md)


## Maven

```xml
    <dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>neptune-spring-boot-starter-mockmvc</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

## Gradle

```groovy
    dependencies {
        testImplementation  group: 'ru.tinkoff.qa.neptune', name: 'neptune-spring-boot-starter-mockmvc', version: LATEST_RELEASE_OR_BETA_VERSION    
    }
```

[API overview](https://tinkoffcreditsystems.github.io/neptune/neptune-spring-boot-starter-mockmvc/index.html)