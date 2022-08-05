# Свойства Neptune

В данном разделе описано, как настроить свойства _Neptune_, их приоритет, а так же базовый набор свойств, которые присутствуют
независимо от подключенных модулей.

## Как указываются значения свойств

Ниже перечислены способы, которыми можно указать свойства _Neptune_.

1. Значения свойств окружения для запуска тестируемого приложения. 

   Как данный механизм работает, описано в [этом документе](./../../core/settings/property_sources.rst). Сейчас
   реализовано:

   - [Spring boot](./../../spring/spring.boot.sterter.md)

   Значения, указанные таким способом, имеют наивысший приоритет.

2. Значения свойств, указанные:
   
   - в [gradle](https://docs.gradle.org/current/userguide/build_environment.html)
   - в [maven](https://maven.apache.org/pom.html#Properties)
   - в параметрах командной строки, запускающей тесты
   - в переменных окружения / среды

   Значения, указанные таким способом, имеют приоритет над значениями, указываемыми способами ниже.
   
3. Значения указанные в файле `neptune.properties`

4. Значения указанные в файле `neptune.global.properties`

## `neptune.properties` и `neptune.global.properties`

`neptune.properties` - файл со свойствами модуля или проекта

`neptune.global.properties` - файл с общими свойствами и их значениями для нескольких проектов или модулей. 
Такой файл следует создавать:
- для библиотек на основе `Neptune` для использования несколькими проектами/модулями
- при условии, когда зависимым проектам/модулям необходим примерно одинаковый набор свойств и их значений

Значения свойств, указанные в `neptune.global.properties`, перекрываются значениями `neptune.properties` зависимого проекта.

Файлы `neptune.properties` и `neptune.global.properties` должны располагаться в `main/resources/` или `test/resources/`.

### Автоматическая генерация `neptune.global.properties` и `neptune.properties`

#### Генерация для Maven

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.codehaus.mojo</groupId>
      <artifactId>exec-maven-plugin</artifactId>
      <version>3.0.0</version>
      <executions>
        <execution>
          <id>generate properties</id>
          <phase>test-compile</phase>
          <!--<phase>compile</phase> тоже можно-->
          <goals>
            <goal>java</goal>
          </goals>
          <configuration>
            <mainClass>ru.tinkoff.qa.neptune.core.api.properties.NeptunePropertyGenerator</mainClass>
             <!--необходимо, если настройки генерируются для тестовых классов--> 
            <classpathScope>test</classpathScope> 
            <arguments>
              <!--генерировать neptune.properties (false), 
              или neptune.global.properties (true)-->
              <argument>false</argument>
              <!--наследовать свойства из neptune.global.properties (true)
               или нет (false)-->
              <argument>false</argument>
              <!--Где расположить сгенерированный файл-->
              <argument>${basedir}/src/test/resources</argument>  
              <!--или ${basedir}/src/main/resources-->
              <!--или любая другая директория-->
            </arguments>
          </configuration>
        </execution>
      </executions>
    </plugin>    
  </plugins>
</build>
```
#### Генерация для Gradle

```groovy
task generateProperties(type: JavaExec) {
    classpath sourceSets.test.runtimeClasspath //или sourceSets.main.runtimeClasspath
    main = "ru.tinkoff.qa.neptune.core.api.properties.NeptunePropertyGenerator"
    //генерировать neptune.properties (false), или neptune.global.properties (true).
    args false,
            //наследовать свойства из neptune.global.properties (true) 
            // или нет (false).
            false,  
            //Где расположить сгенерированный файл.
            "$projectDir/src/test/resources"
            //или $projectDir/src/main/resources
            //или любая другая директория
}
```

#### Сгенерированный *.properties

Получится файл, как в примере ниже. Набор свойств зависит от подключенных модулей

```properties
#Neptune properties and some properties of JDK or 3rd party frameworks are defined there
#===========================General properties. Report
#To limit report/log output or not by depth of step performing
#It is about steps which are built by `ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier`
#and `ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier`
TO_LIMIT_REPORT_DEPTH=true
#===========================General properties. Report captures/Attachments
#Defines events to make log attachments
#Available values: SUCCESS, FAILURE, SUCCESS_AND_FAILURE
DO_CAPTURES_OF=
#===========================General properties. Resource management
#Is it necessary to free unused resources, e.g. opened browser, connections to databases etc.
TO_FREE_RESOURCES_ON_INACTIVITY=
#Time unit (see java.time.temporal.ChronoUnit) of time to wait for activity
#It have sense when value of the property 'TO_FREE_RESOURCES_ON_INACTIVITY' is 'true'
TO_FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT=SECONDS
#Value of time to wait for activity
#It have sense when value of the property 'TO_FREE_RESOURCES_ON_INACTIVITY' is 'true'
TO_FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE=30
#===========================Report localization
#Defines default locale
DEFAULT_LOCALE=
#Define localisation mechanism
DEFAULT_LOCALIZATION_ENGINE=
```

## Формат значений свойств

```properties
# Обычный способ указания значения свойства
DEFAULT_APPLICATION_HOST_PROPERTY=my.app.host

# Так же можно указывать составные значения.
# Такие значения задаются маской, в которой значения 
# других свойств заключаются в ${}
# Так же можно использовать свойства тестируемого приложения, переменные среды.
SOME_PATH_URL_PROPERTY = http://${DEFAULT_APPLICATION_HOST_PROPERTY}/path/to/something
```

```{toctree}
:hidden:

resorces.md
attachments.md
to_limit_report_depth.md
internationalization.md
```