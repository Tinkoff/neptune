Core functionality of Neptune framework

## Maven

```xml
    <dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>core.api</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>

        <!--In case if you are going to implement our own functionality-->    
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>${aspectj.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <!--In case if you are going to implement our own functionality-->
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.0.0-M5</version>
                <configuration>
                    <argLine>
                        -javaagent:"${settings.localRepository}/org/aspectj/aspectjweaver/${aspectj.version}/aspectjweaver-${aspectj.version}.jar"
                    </argLine>
                </configuration>
            </plugin>            
        </plugins>
    </build>
```


## Gradle

In case if you only need to add a test dependency:

```groovy
    dependencies {
        testCompile group: 'ru.tinkoff.qa.neptune', name: 'core.api', version: LATEST_RELEASE_OR_BETA_VERSION    
    }
```

In case if you are going to implement our own functionality:

```groovy
plugins {
    id "io.freefair.aspectj.post-compile-weaving" version 'ACTUAL_PLUGIN_VERSION'
}

dependencies {
    testInpath(group: 'ru.tinkoff.qa.neptune', name: 'core.api', version: 'NEPTUNE_BETA_OR_RELEASE') {
        transitive = false
    }
}
```

[Краткая документация на русском](./doc/rus/README.MD)

[Brief documentation in English](./doc/eng/README.MD)

[API overview](https://tinkoffcreditsystems.github.io/neptune/core.api/index.html)
