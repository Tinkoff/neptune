Integration of Neptune with [Apache Kafka](https://kafka.apache.org/).

## Maven

```xml
    <dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>kafka</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

## Gradle

```groovy
    dependencies {
        testImplementation  group: 'ru.tinkoff.qa.neptune', name: 'kafka', version: LATEST_RELEASE_OR_BETA_VERSION    
    }
```

[Краткая документация на русском](./doc/rus/README.MD)

[Brief documentation in English](./doc/eng/README.MD)

[API overview](https://tinkoff.github.io/neptune/kafka/index.html)