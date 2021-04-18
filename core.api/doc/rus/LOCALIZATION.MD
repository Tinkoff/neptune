# Локализация отчетов

Фреймворк поддерживает локализацию отчетов на разные языки.

## Базовый механиз локализации

[Настройка](./SETTINGS.MD#Локализация)

Neptune поддерживает по дефолту механизм, основанный на
использовании [AspectJ](https://www.eclipse.org/aspectj/doc/released/index.html)
и [ResourceBundle](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ResourceBundle.html).

### Как настроить использование базового механизма:

- ##### указать в properties механизм перевода через resourceBundle

```properties
DEFAULT_LOCALIZATION_ENGINE=ru.tinkoff.qa.neptune.core.api.steps.localization.LocalizationByResourceBundle
```

- ##### сгенерировать и настроить resourceBundle
- ##### указать в properties какую локаль использовать

```properties
DEFAULT_LOCALE=ru_RU
```

### Механизм генерации resourceBundle

Имеется возможность автогенерации resourceBundle для перевода. Для этого необходимо вызвать
mainClass `ru.tinkoff.qa.neptune.core.api.steps.localization.ResourceBundleGenerator` с двумя аргументами:

- arg0 - строка в формате language_country
- arg1 - путь к каталогу, в котором должен быть сохранен файл

###### Пример для pom.xml

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
                    <phase>compile</phase>
                    <goals>
                        <goal>java</goal>
                    </goals>
                    <configuration>
                        <mainClass>ru.tinkoff.qa.neptune.core.api.steps.localization.ResourceBundleGenerator</mainClass>
                        <arguments>
                            <argument>ru_RU</argument>
                            <argument>${basedir}/src/main/resources</argument>
                        </arguments>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>            
```

###### Пример для build.gradle

```groovy
task generateBundle(type: JavaExec) {
    classpath sourceSets.main.runtimeClasspath
    main = "ru.tinkoff.qa.neptune.core.api.steps.localization.ResourceBundleGenerator"
    args "ru_RU", "$projectDir/src/main/resources"
}
```

#### В результате сгенерируется похожий файл, где необходимо изменить значение свойств

```properties
#Values for translation of steps, their parameters and attachments are defined here. Format key = value

#============================================ STEPS ============================================ 

######################## core.api.steps.SequentialGetStepSupplier #
#_________________________________Parameters_____________________________________
#Original text = Get:
core.api.steps.SequentialGetStepSupplier.imperative = Get:
#Original text = Result
core.api.steps.SequentialGetStepSupplier.resultDescription = Result

######################## core.api.steps.SequentialActionSupplier #
#_________________________________Parameters_____________________________________
#Original text = Perform:
core.api.steps.SequentialActionSupplier.imperative = Perform:

######################## core.api.steps.Absence #
#_________________________________Parameters_____________________________________
#Original text = Time of the waiting for absence
core.api.steps.Absence.timeOut = Time of the waiting for absence
#Original text = Is absent
core.api.steps.Absence.resultDescription = Is absent
#__________________________________ Methods _______________________________________
#Original text = Absence of {toBeAbsent}
core.api.steps.Absence.absence(core.api.steps.SequentialGetStepSupplier<T,?,?,?,?>) = Absence of {toBeAbsent}
#Original text = Absence of {toBeAbsent}
core.api.steps.Absence.absence(java.util.function.Function<T,?>) = Absence of {toBeAbsent}

######################## core.api.steps.Presence #
#_________________________________Parameters_____________________________________
#Original text = Is present
core.api.steps.Presence.resultDescription = Is present
#__________________________________ Methods _______________________________________
#Original text = Presence of {toBePresent}
core.api.steps.Presence.presence(java.util.function.Function<T,?>) = Presence of {toBePresent}
#Original text = Presence of {toBePresent}
core.api.steps.Presence.presence(core.api.steps.SequentialGetStepSupplier<T,?,?,?,?>) = Presence of {toBePresent}

#============================================ ATTACHMENTS ============================================ 

######################## core.api.event.firing.collections.ArrayCaptor #
#Original text = Resulted array
core.api.event.firing.collections.ArrayCaptor = Resulted array

######################## core.api.event.firing.collections.CollectionCaptor #
#Original text = Resulted collection
core.api.event.firing.collections.CollectionCaptor = Resulted collection

######################## core.api.event.firing.collections.MapCaptor #
#Original text = Resulted map
core.api.event.firing.collections.MapCaptor = Resulted map
```

### Использовать aspectj для собственных расширений механизма шагов

Про механизм шагов (полезные статьи):

- [Шаги, которые возвращают результат](./IDEA.MD#Шаги,-которые-возвращают-результат)

- [Шаги, которые выполняют действие](./IDEA.MD#Шаги,-которые-выполняют-действие)

1. настроить `pom.xml`

 ```xml
<!-- Добавить зависимость -->
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>${aspectj.version}</version>
    <!-- Если новые шаги лежат в main, то scope необязателен  -->
    <scope>test</scope>
</dependency> 
 ```

 ```xml
<!-- Добавить плагин -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0-M5</version>
    <configuration>
        <argLine>
            -javaagent:"${settings.localRepository}/org/aspectj/aspectjweaver/1.9.6/aspectjweaver-1.9.6.jar"
        </argLine>
    </configuration>
</plugin>
 ```

или `build.gradle`

```groovy
plugins {
    id "io.freefair.aspectj.post-compile-weaving" version 'ACTUAL_PLUGIN_VERSION'
}

dependencies {
    inpath(group: 'ru.tinkoff.qa.neptune', name: 'core.api', version: 'NEPTUNE_BETA_OR_RELEASE') {
        transitive = false
    }
}

```

2. Использовать для своих шагов @Description, @DescriptionFragment, @StepParameter, @DefaultParameterNames

## Собственный механизм локализации

Так же существует возможность реализовать собственный механизм локализации.

Для этого необходимо:

* заимплементить интерфейс
  `ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization`

 ```java
public interface StepLocalization {

    /**
     * Makes translation using class.
     *
     * @param clz                       is a class whose metadata may be used for auxiliary purposes
     * @param description               is a description of a step taken from {@link Description} of a class
     * @param descriptionTemplateParams is a map of parameters and their values. These parameters are taken from
     *                                  fields annotated by {@link DescriptionFragment}. These parameters should
     *                                  replace strings between {@code '{}'} in the given {@code description}
     * @param locale                    is a used locale
     * @param <T>                       is a type of a class
     * @return translated text
     */
    <T> String classTranslation(Class<T> clz,
                                String description,
                                Map<String, String> descriptionTemplateParams,
                                Locale locale);

    /**
     * Makes translation using method.
     *
     * @param method                    is a method whose metadata may be used for auxiliary purposes
     * @param description               is a description of a step taken from {@link Description} of a method
     * @param descriptionTemplateParams is a map of parameters and their values. These parameters are taken from
     *                                  method parameters annotated by {@link DescriptionFragment}.These parameters should
     *                                  replace strings between {@code '{}'} in the given {@code description}
     * @param locale                    is a used locale
     * @return translated text
     */
    String methodTranslation(Method method,
                             String description,
                             Map<String, String> descriptionTemplateParams,
                             Locale locale);

    /**
     * Makes translation using other annotated element that differs from {@link Class} and {@link Method}
     *
     * @param member      is an annotated element whose metadata may be used for auxiliary purposes
     * @param description is a description taken from the member
     * @param locale      is a used locale
     * @param <T>         is a type of a member
     * @return translated text
     */
    <T extends AnnotatedElement & Member> String memberTranslation(T member,
                                                                   String description,
                                                                   Locale locale);

    /**
     * Makes translation of a text.
     *
     * @param text   to be translated
     * @param locale is a used locale
     * @return translated text
     */
    String translation(String text, Locale locale);
}
```

* указать в propeties новый механизм

```properties
DEFAULT_LOCALIZATION_ENGINE=org.my.project.MyLocalizationEngine
```
 
 
