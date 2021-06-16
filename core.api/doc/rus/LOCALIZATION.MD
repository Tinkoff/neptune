# Локализация отчетов

Фреймворк поддерживает локализацию отчетов на разные языки.

## Оглавление
  - [Базовый механизм локализации](#Базовый-механизм-локализации)
    - [Как настроить использование базового механизма](#Как-настроить-использование-базового-механизма)
    - [Механизм генерации resourceBundle](#Механизм-генерации-resourceBundle)
      - [Пример для pom.xml](#Пример-для-pomxml)
      - [Пример для build.gradle](#Пример-для-buildgradle)
      - [Результат](#Результат)
  - [Собственный механизм локализации](#Собственный-механизм-локализации)

## Базовый механизм локализации

[Настройка](./SETTINGS.MD#Локализация)

Neptune поддерживает по дефолту механизм, основанный на
использовании [AspectJ](https://www.eclipse.org/aspectj/doc/released/index.html)
и [ResourceBundle](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ResourceBundle.html).

### Как настроить использование базового механизма

- ##### сгенерировать и настроить resourceBundle

- ##### добавить в переменные среды или в `neptune.properties` или в `neptune.global.properties`

```properties
DEFAULT_LOCALIZATION_ENGINE=ru.tinkoff.qa.neptune.core.api.localization.LocalizationByResourceBundle
```

- ##### добавить в переменные среды или в `neptune.properties` или в `neptune.global.properties`

```properties
DEFAULT_LOCALE=ru_RU
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Механизм генерации resourceBundle

Имеется возможность автогенерации resourceBundle для перевода. Для этого необходимо вызвать
mainClass `ru.tinkoff.qa.neptune.core.api.localization.ResourceBundleGenerator` с двумя аргументами:

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
                        <mainClass>ru.tinkoff.qa.neptune.core.api.localization.ResourceBundleGenerator</mainClass>
                        <arguments>
                            <argument>ru_RU</argument>
                            <!--Нужная локаль-->
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
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

###### Пример для build.gradle

```groovy
task generateBundle(type: JavaExec) {
    classpath sourceSets.main.runtimeClasspath
    main = "ru.tinkoff.qa.neptune.core.api.localization.ResourceBundleGenerator"
    args "ru_RU", //нужная локаль
            "$projectDir/src/test/resources"
            //или $projectDir/src/main/resources
            //или любая другая директория
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

#### Результат

```properties
#Values for translation of steps, their parameters and attachments are defined here. Format key = value

#============================================ STEPS ============================================ 

######################## core.api.steps.SequentialGetStepSupplier #
#_________________________________Parameters_____________________________________
#Original text = Get:
core.api.steps.SequentialGetStepSupplier.imperative = Get:
#Original text = Result
core.api.steps.SequentialGetStepSupplier.resultDescription = Result
#Original text = Not present:
core.api.steps.SequentialGetStepSupplier.errorMessageStartingOnEmptyResult = Not present:

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
#Original text = Still present:
core.api.steps.Absence.errorMessageStartingOnEmptyResult = Still present:
#__________________________________ Methods _______________________________________
#Original text = Absence of {toBeAbsent}
core.api.steps.Absence.absence(core.api.steps.SequentialGetStepSupplier<T,?,?,?,?>) = Absence of {toBeAbsent}

######################## core.api.steps.Presence #
#_________________________________Parameters_____________________________________
#Original text = Is present
core.api.steps.Presence.resultDescription = Is present
#__________________________________ Methods _______________________________________
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
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Собственный механизм локализации

Так же существует возможность реализовать собственный механизм локализации.

Для этого необходимо:

* заимплементить интерфейс
  `ru.tinkoff.qa.neptune.core.api.localization.StepLocalization`

 ```java
public interface StepLocalization {

  /**
   * Makes translation by usage of a class.
   *
   * @param clz                       is a class whose metadata may be used for auxiliary purposes
   * @param description               is a description of a step. It is fully completed by value taken
   *                                  from {@link Description} of a class and by values which are taken from
   *                                  fields annotated by {@link DescriptionFragment}
   * @param descriptionTemplateParams is a map of parameters and their values. These parameters are taken from
   *                                  fields annotated by {@link DescriptionFragment}. This parameter is included
   *                                  because the map is possible to be re-used for some reasons.
   * @param locale                    is a used locale
   * @param <T>                       is a type of a class
   * @return translated text
   */
  <T> String classTranslation(Class<T> clz,
                              String description,
                              Map<String, String> descriptionTemplateParams,
                              Locale locale);

  /**
   * Makes translation by usage of a method.
   *
   * @param method                    is a method whose metadata may be used for auxiliary purposes
   * @param description               is a description of a step. It is fully completed by value taken
   *                                  from {@link Description} of a method and by values which are taken from
   *                                  method parameters annotated by {@link DescriptionFragment}
   * @param descriptionTemplateParams is a map of parameters and their values. These parameters are taken from
   *                                  method parameters annotated by {@link DescriptionFragment}.This parameter is included
   *                                  because the map is possible to be re-used for some reasons.
   * @param locale                    is a used locale
   * @return translated text
   */
  String methodTranslation(Method method,
                           String description,
                           Map<String, String> descriptionTemplateParams,
                           Locale locale);

  /**
   * Makes translation by usage of other annotated element that differs from {@link Class} and {@link Method}
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

* добавить в переменные среды или в `neptune.properties` или в `neptune.global.properties` (или поменять значение, 
  если уже добавлено)

```properties
DEFAULT_LOCALIZATION_ENGINE=org.my.project.MyLocalizationEngine
```
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление) 
 
