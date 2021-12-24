Integration of Neptune with [Spring WebTestClient](https://spring.getdocs.org/en-US/spring-framework-docs/docs/testing/integration-testing/webtestclient.html)

## Maven

```xml
    <dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>spring.web.testclient</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>

        <!--the dependency below is recommended-->
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>neptune-spring-boot-starter-webtest-client</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

## Gradle

```groovy
    dependencies {
        testImplementation  group: 'ru.tinkoff.qa.neptune', name: 'spring.web.testclient', version: LATEST_RELEASE_OR_BETA_VERSION
        //the dependency below is recommended
        testImplementation  group: 'ru.tinkoff.qa.neptune', name: 'neptune-spring-boot-starter-webtest-client', version: LATEST_RELEASE_OR_BETA_VERSION
    }
```

About [neptune-spring-boot-starter-webtest-client](./../neptune-spring-boot-starter-webtest-client/README.md)

[Краткая документация на русском](./doc/rus/README.MD)

[Brief documentation in English](./doc/eng/README.MD)

[API overview](https://tinkoffcreditsystems.github.io/neptune/spring.web.testclient/index.html)