# Локализация отчетов

Фреймворк поддерживает локализацию отчетов на разные языки.

## Базовый механиз локализации

[Настройка](./SETTINGS.MD#Локализация)

Neptune поддерживает по дефолту механизм, основанный на использовании [AspectJ](https://www.eclipse.org/aspectj/doc/released/index.html) и [ResourceBundle](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ResourceBundle.html).

###Как настроить использование базового механизма:
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
Имеется возможность автогенерации resourceBundle для перевода.
Для этого необходимо вызвать mainClass `ru.tinkoff.qa.neptune.core.api.steps.localization.ResourceBundleGenerator` с двумя аргументами:
 - arg0 - строка в формате language_country
 - arg1 - путь к каталогу, в котором должен быть сохранен файл 
 
######Пример для pom.xml

```xml
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
```
######Пример для build.gradle 
```groovy
task generateBundle(type: JavaExec) {
    classpath sourceSets.main.runtimeClasspath
    main = "ru.tinkoff.qa.neptune.core.api.steps.localization.ResourceBundleGenerator"
    args "ru_RU", "$projectDir/src/main/resources"
}
```

#### В результате сгенерируется похожий файл, где необходимо изменить значение свойств

```properties
#Values for translation of steps and parameters are defined here. Format key = value
#=============================================================================================
#====================== core.api.steps.Absence
#=============================================================================================
#_________________________________ DefaultParameterNames _____________________________________
#Original text = Get from
core.api.steps.Absence.from = Get from
#Original text = Polling time
core.api.steps.Absence.pollingTime = Polling time
#Original text = Time of the waiting for absence
core.api.steps.Absence.timeOut = Time of the waiting for absence
#Original text = Criteria
core.api.steps.Absence.criteria = Criteria
#__________________________________ Method Description _______________________________________
#Original text = Absence of {toBeAbsent}
core.api.steps.Absence.absence(java.util.function.Function<T,?>) = Absence of {toBeAbsent}
#Original text = Absence of {toBeAbsent}
core.api.steps.Absence.absence(core.api.steps.SequentialGetStepSupplier<T,?,?,?,?>) = Absence of {toBeAbsent}
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

      String classTranslation(Class<?> clz, Locale locale);
  
      String methodTranslation(Method method,Locale locale, Object... parameters);
  
      <T extends AnnotatedElement & Member> String memberTranslation(T member, Locale locale);
  
      String translation(String text,Locale locale);
  }
```
 * реализовать следующие методы:
     * `String classTranslation(Class<?> clz, Locale locale)` - для перевода классов
     * `String methodTranslation(Method method, Locale locale, Object... parameters)` - для перевода методов
     * `<T extends AnnotatedElement & Member> String memberTranslation(T member, Locale locale)` - для перевода полей
     * `String translation(String text, Locale locale)` - например, для перевода строки, используя переводчик или ИИ
  
 * указать в propeties новый механизм
```properties
DEFAULT_LOCALIZATION_ENGINE=org.my.project.MyLocalizationEngine
```

При реализации интерфейса можно использовать метаинформацию, полученную из аннотаций @Description, @DescriptionFragment,
@StepParameter
 
 