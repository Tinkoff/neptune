Neptune Hibernate Integration API

## Maven

```xml
    <dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>hibernate</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

## Gradle

```groovy
    dependencies {
        testImplementation group: 'ru.tinkoff.qa.neptune', name: 'hibernate', version: LATEST_RELEASE_OR_BETA_VERSION    
    }
```

## Внешние зависимости 

Для работы с Hibernate и QueryDSL необходимо, чтобы соответствующие зависимости были подключены в проекте.

Для Hibernate ORM:

```xml
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>5.6.0.Final</version>
</dependency>

```

```groovy
    api group: 'org.hibernate', name: 'hibernate-core', version: '5.6.0.Final'
```

Для Hibernate OGM (пример артефакта для MongoDB):

```xml
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-ogm-mongodb</artifactId>
        <version>5.4.1.Final</version>
    </dependency>

```

```groovy
    api group: 'org.hibernate', name: 'hibernate-ogm-mongodb', version: '5.4.1.Final'
```

Для QueryDSL:

```xml
    <dependency>
        <groupId>com.querydsl</groupId>
        <artifactId>querydsl-jpa</artifactId>
        <version>5.0.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>com.querydsl</groupId>
        <artifactId>querydsl-core</artifactId>
        <version>5.0.0</version>
        <scope>test</scope>
    </dependency>
```

```groovy
    testImplementation group: 'com.querydsl', name: 'querydsl-core', version: '5.0.0'
    testImplementation group: 'com.querydsl', name: 'querydsl-jpa', version: '5.0.0'
```

[Краткая документация на русском](./doc/rus/README.md)

[Brief documentation in English](./doc/eng/README.md)

[API overview](https://tinkoffcreditsystems.github.io/neptune/hibernate/index.html)