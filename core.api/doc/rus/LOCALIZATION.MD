# Локализация отчетов

Фреймворк поддерживает локализацию отчетов на разные языки.

## Базовый механиз локализации

[Настройка](./SETTINGS.MD#Локализация)

Neptune поддерживает по дефолту механизм, основанный на использовании AspectJ и ResourceBundle.

AspectJ используется для изменения description, посредством перехвата методов где дескрипшен уже полностью сформирован и готов к переводу.

Класс ResourceBundle предназначен для чтения данных из текстовых файлов свойств (расширение - properties). Каждый отдельный файл с расширением properties содержит информацию для отдельной локали.

В файлах свойств (*.properties) информация должна быть организована по принципу:

```properties
#Значение свойства указывается так
key1 = value1
key2 = value2
key3 = value3
```
Где **key** - это полное пакетное имя метода или поля, a **value** - это значение description

### Механизм для генерации ResourceBundle
Имеется возможность автогенерации resourceBundle для перевода.
Для этого необходимо вызвать mainClass `ru.tinkoff.qa.neptune.core.api.steps.localization.ResourceBundleGenerator` с двумя аргументами:
 - arg0 - строка в формате language_country
 - arg1 - путь к каталогу, в котором должен быть сохранен файл 
 
#######Пример для pom.xml

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
                </executions>
```
#######Пример для build.gradle 
```groovy
task generateBundle(type: JavaExec) {
    classpath sourceSets.main.runtimeClasspath
    main = "ru.tinkoff.qa.neptune.core.api.steps.localization.ResourceBundleGenerator"
    args "ru_RU", "$projectDir/src/main/resources"
}
```

#### По итогу сгенерируется похожий файл

```properties
core.api.steps.TranslateByResourceBundleTest$SomeCriteria = Class Description from ResourceBundle
core.api.steps.TranslateByResourceBundleTest$SomeCriteria.someCriteriaWithAnnotation() = Method Description from ResourceBundle
core.api.steps.TranslateByResourceBundleTest$SomeCriteria.someCriteriaWithCompositeAnnotation(java.lang.String) = Composite Method Description from ResourceBundle + {element}
```


## Собственный механиз локализации
Так же существует возможность реализовать собственный механизм локализации.
 
Для этого необходимо заимплементить интерфейс
 `ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization`
 ```java
  public interface StepLocalization {

      String classTranslation(Class<?> clz, Locale locale);
  
      String methodTranslation(Method method,Locale locale, Object... parameters);
  
      <T extends AnnotatedElement & Member> String memberTranslation(T member, Locale locale);
  
      String translation(String text,Locale locale);
  }
```
  и реализовать следующие методы:
 - `String classTranslation(Class<?> clz, Locale locale)` - для перевода классов
 - `String methodTranslation(Method method, Locale locale, Object... parameters)` - для перевода методов
 - `<T extends AnnotatedElement & Member> String memberTranslation(T member, Locale locale)` - для перевода полей
 - `String translation(String text, Locale locale)` - например, для перевода строки, используя переводчик или ИИ