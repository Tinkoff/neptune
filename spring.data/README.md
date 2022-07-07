Integration of Neptune with [Spring Data](https://spring.io/projects/spring-data)

## Maven

```xml

<dependencies>
    <dependency>
        <groupId>ru.tinkoff.qa.neptune</groupId>
        <artifactId>spring.data</artifactId>
        <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Gradle

```groovy
    dependencies {
    testImplementation group: 'ru.tinkoff.qa.neptune', name: 'spring.data', version: LATEST_RELEASE_OR_BETA_VERSION
}
```

[Краткая документация на русском](./doc/rus/README.MD)

[Brief documentation in English](./doc/eng/README.MD)

[API overview](https://tinkoff.github.io/neptune/spring.data/index.html)