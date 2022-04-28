Neptune Hibernate Integration API

## Maven

```xml
    <dependencies>
        <dependency>
            <!--it is necessary to have this in classpath-->
            <!--or anything that depends on this transitively-->
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-core</artifactId>
            <!--range of supported versions-->
            <version>[5.6.0.Final,)</version>
        </dependency>

        <!--it is optional-->
        <!--These dependencies are needed to unlock usage of QueryDsl JPA-->
        <dependency>
            <groupId>com.querydsl</groupId>
            <artifactId>querydsl-jpa</artifactId>
            <!--range of supported versions-->
            <version>[5.0.0,)</version>
            <!-- scope is needed when main functionality doesn't use QueryDsl -->
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.querydsl</groupId>
            <artifactId>querydsl-core</artifactId>
            <!--range of supported versions-->
            <version>[5.0.0,)</version>
            <!-- scope is needed when main functionality doesn't use QueryDsl -->
            <scope>test</scope>
        </dependency>
    
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
        //it is necessary to have this in classpath
        //or anything that depends on this transitively
        implementation group: 'org.hibernate', name: 'hibernate-ogm-mongodb', version: '[5.6.0.Final,)' //range of supported versions

        //it is optional
        //These dependencies are needed to unlock usage of QueryDsl JPA
        //When main functionality doesn't use QueryDsl then 'testImplementation' is ok
        implementation group: 'com.querydsl', name: 'querydsl-core', version: '[5.0.0,)' //range of supported versions
        implementation group: 'com.querydsl', name: 'querydsl-jpa', version: '[5.0.0,)'
    
        testImplementation group: 'ru.tinkoff.qa.neptune', name: 'hibernate', version: LATEST_RELEASE_OR_BETA_VERSION    
    }
```

[Краткая документация на русском](./doc/rus/README.MD)

[Brief documentation in English](./doc/eng/README.MD)

[API overview](https://tinkoff.github.io/neptune/hibernate/index.html)
