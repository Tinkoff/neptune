# Интеграция со Swagger

Данный модуль используется для связывания функциональности **Neptune** и **Swagger 3.x**.
[Подробно о мэппинге http сервисов](./../../../http.api/doc/rus/MAPPING.MD)

## Maven

### Если сгенерированный код должен лежать в test
<details>
  <summary>Нажми, чтобы прочесть</summary>

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://maven.apache.org/POM/4.0.0"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.my.group</groupId>
    <artifactId>my.artefact</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>http.api</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope> <!--Чтобы зависимость не тянулась со сборкой, а оставалась в тестах-->
        </dependency>       
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>io.swagger.codegen.v3</groupId>
                <artifactId>swagger-codegen-maven-plugin</artifactId>
                <version>3.0.23</version> <!--Или выше-->
                <executions>
                    <execution>
                        <id>first API model generation</id> <!--Необязательно, если тестируется один сервис-->
                        <phase>generate-test-resources</phase> <!--Генерируются исходники тестов-->
                        <goals>
                            <goal>generate</goal>
                        </goals>
                        <configuration>
                            <!--URL локального или удаленного json файла, в котором хранится описание API от Swagger-->
                            <inputSpec>${URL_OF_LOCAL_OR_REMOTE_SWAGGER_JSON}</inputSpec>
                            <!--Именно такое значение должно быть-->
                            <language>neptune</language>
                            <!--Либо любая другая директория, которая не должна попасть в сборку приложения-->
                            <output>${project.build.testSourceDirectory}</output>
                            <!--Название пакета, где будут созданы классы, моделирующие API-->
                            <apiPackage>org.my.group.my.test.api</apiPackage>
                            <!--Название пакета, где будут созданы классы, описывающие DTO-->
                            <modelPackage>org.my.group.my.test.dto</modelPackage>
                        </configuration>
                    </execution>
                </executions>

                <dependencies>
                    <dependency>
                        <groupId>ru.tinkoff.qa.neptune</groupId>
                        <artifactId>neptune.swagger.codegen</artifactId>
                        <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>        
    </build>

</project>
```

</details>

### Если сгенерированный код должен лежать в main
<details>
  <summary>Нажми, чтобы прочесть</summary>

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://maven.apache.org/POM/4.0.0"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.my.group</groupId>
    <artifactId>my.artefact</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>http.api</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
        </dependency>       
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>io.swagger.codegen.v3</groupId>
                <artifactId>swagger-codegen-maven-plugin</artifactId>
                <version>3.0.23</version> <!--Или выше-->
                <executions>
                    <execution>
                        <id>first API model generation</id> <!--Необязательно, если тестируется один сервис-->
                        <phase>generate-sources</phase> <!--Генерируются исходники-->
                        <goals>
                            <goal>generate</goal>
                        </goals>
                        <configuration>
                            <!--URL локального или удаленного json файла, в котором хранится описание API от Swagger-->
                            <inputSpec>${URL_OF_LOCAL_OR_REMOTE_SWAGGER_JSON}</inputSpec>
                            <!--Именно такое значение должно быть-->
                            <language>neptune</language>
                            <!--Либо любая другая директория, которая должна попасть в сборку приложения-->
                            <output>${project.build.sourceDirectory}</output>
                            <!--Название пакета, где будут созданы классы, моделирующие API-->
                            <apiPackage>org.my.group.my.api</apiPackage>
                            <!--Название пакета, где будут созданы классы, описывающие DTO-->
                            <modelPackage>org.my.group.my.dto</modelPackage>
                        </configuration>
                    </execution>
                </executions>

                <dependencies>
                    <dependency>
                        <groupId>ru.tinkoff.qa.neptune</groupId>
                        <artifactId>neptune.swagger.codegen</artifactId>
                        <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>        
    </build>

</project>
```
</details>

## Gradle

С gradle ситуация немного сложнее, т.к:

- нет gradle плагина, который можно было бы считать стандартным

- имеющаяся интеграция изначально была разработана для `swagger-codegen-maven-plugin`.

Но все же

### Если сгенерированный код должен лежать в test
<details>
  <summary>Нажми, чтобы прочесть</summary>

```groovy
buildscript {
    repositories {
        mavenCentral()

        maven {
            url "Указываем другие необходимые репозитории maven"
        }
    }
    dependencies {
        classpath('io.swagger.codegen.v3:swagger-codegen-maven-plugin:3.0.23') //Или выше
        classpath('ru.tinkoff.qa.neptune:neptune.swagger.codegen:LATEST_RELEASE_OR_BETA_VERSION')
    }
}

plugins {
    id 'java'
}

sourceCompatibility = JavaVersion.VERSION_11
targetCompatibility = JavaVersion.VERSION_11

group 'org.example'
version '1.0-SNAPSHOT'

repositories {
    mavenCentral()

    maven {
        url "Указываем другие необходимые репозитории maven"
    }
}

dependencies {
    testCompile group: 'ru.tinkoff.qa.neptune', name: 'http.api', version: 'LATEST_RELEASE_OR_BETA_VERSION'
}

import io.swagger.codegen.v3.CodegenConfigLoader
import io.swagger.codegen.v3.DefaultGenerator
import io.swagger.codegen.v3.ClientOptInput
import io.swagger.codegen.v3.ClientOpts
import io.swagger.v3.parser.OpenAPIV3Parser

task generateAPI {
    doLast {
        System.setProperty("io.swagger.v3.parser.util.RemoteUrl.trustAll", "true"); //Желательно указать, если спецификация лежит удаленно
        //и генерация кода не доверяет сертификату
        def openAPI = new OpenAPIV3Parser().readLocation('URL-путь до json спецификации от swagger', null, null).getOpenAPI()

        def clientOpts = new ClientOptInput().openAPI(openAPI)
        def codegenConfig = CodegenConfigLoader.forName('neptune')
        codegenConfig.setOutputDir(project.rootDir.toString() + '/src/test/java') //или путь до любой директории, которая не должна 
        //попасть в сборку
        clientOpts.setConfig(codegenConfig)

        def clientOps = new ClientOpts()
        clientOps.setProperties([
                'apiPackage'       : 'org.my.group.my.test.api', //Название пакета, где будут созданы классы, моделирующие API
                'modelPackage'     : 'org.my.group.my.test.dto', //Название пакета, где будут созданы классы, описывающие DTO
        ])
        clientOpts.setOpts(clientOps)

        def generator = new DefaultGenerator().opts(clientOpts)
        generator.generate()
    }
}

//выстраиваем последовательность задач gradle
compileTestJava {
    dependsOn(generateAPI)
}


```
</details>

### Если сгенерированный код должен лежать в main
<details>
  <summary>Нажми, чтобы прочесть</summary>

```groovy
buildscript {
    repositories {
        mavenCentral()

        maven {
            url "Указываем другие необходимые репозитории maven"
        }
    }
    dependencies {
        classpath('io.swagger.codegen.v3:swagger-codegen-maven-plugin:3.0.23')  //Или выше
        classpath('ru.tinkoff.qa.neptune:neptune.swagger.codegen:LATEST_RELEASE_OR_BETA_VERSION')
    }
}

plugins {
    id 'java'
}

sourceCompatibility = JavaVersion.VERSION_11
targetCompatibility = JavaVersion.VERSION_11

group 'org.example'
version '1.0-SNAPSHOT'

repositories {
    mavenCentral()

    maven {
        url "Указываем другие необходимые репозитории maven"
    }
}

dependencies {
    compile group: 'ru.tinkoff.qa.neptune', name: 'http.api', version: 'LATEST_RELEASE_OR_BETA_VERSION'
}

import io.swagger.codegen.v3.CodegenConfigLoader
import io.swagger.codegen.v3.DefaultGenerator
import io.swagger.codegen.v3.ClientOptInput
import io.swagger.codegen.v3.ClientOpts
import io.swagger.v3.parser.OpenAPIV3Parser

task generateAPI {
    doLast {
        System.setProperty("io.swagger.v3.parser.util.RemoteUrl.trustAll", "true"); //Желательно указать, если спецификация лежит удаленно
        //и генерация кода не доверяет сертификату
        def openAPI = new OpenAPIV3Parser().readLocation('URL-путь до json спецификации от swagger', null, null).getOpenAPI()

        def clientOpts = new ClientOptInput().openAPI(openAPI)
        def codegenConfig = CodegenConfigLoader.forName('neptune')
        codegenConfig.setOutputDir(project.rootDir.toString() + '/src/main/java') //или путь до любой директории, которая должна 
        //попасть в сборку
        
        clientOpts.setConfig(codegenConfig)

        def clientOps = new ClientOpts()
        clientOps.setProperties([
                'apiPackage'       : 'org.my.group.my.api', //Название пакета, где будут созданы классы, моделирующие API
                'modelPackage'     : 'org.my.group.my.dto', //Название пакета, где будут созданы классы, описывающие DTO
        ])
        clientOpts.setOpts(clientOps)

        def generator = new DefaultGenerator().opts(clientOpts)
        generator.generate()
    }
}

//выстраиваем последовательность задач gradle
compileJava {
    dependsOn(generateAPI)
}

```
</details>

## Стоит иметь в виду

- поддерживается только `com.fasterxml.jackson.*` в качестве инструмента сериализации/десериализации
- поддерживается `java.util.Date` для работы с датами


