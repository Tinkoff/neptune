Integration of Neptune with [Spring WebTestClient](https://spring.getdocs.org/en-US/spring-framework-docs/docs/testing/integration-testing/webtestclient.html)

## Maven

```xml
    <dependencies>
        <dependency>
            <!--it is necessary to have this in classpath-->
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
            <!--range of supported versions-->
            <version>[2.6.1,)</version>
        </dependency>

        <dependency>
            <!--it is necessary to have this in test classpath-->
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <!--range of supported versions-->
            <version>[2.6.1,)</version>
            <scope>test</scope>
        </dependency>
    
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>spring.web.testclient</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

## Gradle

```groovy
    dependencies {
        //it is necessary to have this in classpath
        implementation group: 'org.springframework.boot', name: 'spring-boot-starter-webflux', version: '[2.6.1,)' //range of supported versions
        //it is necessary to have this in test classpath
        testImplementation group: 'org.springframework.boot', name: 'spring-boot-starter-test', version: '[2.6.1,)' //range of supported versions
        testImplementation  group: 'ru.tinkoff.qa.neptune', name: 'spring.web.testclient', version: LATEST_RELEASE_OR_BETA_VERSION
    }
```

[Краткая документация на русском](./doc/rus/README.MD)

[Brief documentation in English](./doc/eng/README.MD)

[API overview](https://tinkoff.github.io/neptune/spring.web.testclient/index.html)