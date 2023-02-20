# Аспекты

В данном документе описано, как можно создать [аспеты](https://en.wikipedia.org/wiki/Aspect-oriented_programming) используя 
механизмы, которые предоставляет Neptune. Например, по описанному ниже алгоритму работают [неявные шаги](../quick_start/steps/fluent_steps/implicit_steps.md).

1. Нужно добавить зависимость

Maven
```xml
<dependency>
    <groupId>net.bytebuddy</groupId>
    <artifactId>byte-buddy</artifactId>
    <version>${actual_version}</version>
    <!--указать нужный scope-->
</dependency>
```

Gradle
```groovy
//указать нужный scope
implementation group: 'net.bytebuddy', name: 'byte-buddy', version: 'actual_version'
```

2. Реализовать интерфейс

```java
package org.my.pack;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.matcher.ElementMatcher;

import ru.tinkoff.qa.neptune.core.api.agent.NeptuneTransformParameters;

import java.lang.reflect.Method;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class SomeTransformParameters implements NeptuneTransformParameters {

    @Override
    public ElementMatcher<? super MethodDescription> methodMatcher() {
        //описание того, к каким методам применяется аспект
        return named("someMethod");
    }

    @Override
    public Class<?> interceptor() {
        //Класс-перехватчик
        return SomeInterceptor.class;
    }

    @Override
    public ElementMatcher<? super TypeDescription> typeMatcher() {
        //описание того, к методам каких классов применяется аспект
        return is(SomeClass.class);
    }
    
    //перехватчик
    //Необязательно это должен быть вложенный класс, но
    //он обязательно должен быть публичным
    public static class SomeInterceptor {

        @Advice.OnMethodEnter
        public static void interceptEnter(@Advice.This Object target,
                                          @Advice.Origin Method method,
                                          @Advice.AllArguments Object[] args,
                                          @Advice.Return Object returned) {
            //логика в начале выполнения перехватываемого метода
            //данный метод не обязательный
        }

        @Advice.OnMethodExit
        public static void interceptExit(@Advice.This Object target,
                                         @Advice.Origin Method method,
                                         @Advice.AllArguments Object[] args,
                                         @Advice.Return Object returned) {
            //логика в конце выполнения перехватываемого метода
            //данный метод не обязательный
        }
    }
}
```

Хорошая статья про [используемые аннотации Byte Buddy](https://medium.com/@lnishada/introduction-to-byte-buddy-advice-annotations-48ac7dae6a94)

3. 
```{eval-rst}
.. include:: aspect_spi.rst
```

## Ограничения

Аспекты нельзя создавать для классов и методов из следующих пакетов:

- `net.bytebuddy.*`
- `java.*`
- `com.sun.*`
- `org.testng.*`
- `org.junit.*`
- `com.gradle.*`
- `*.maven.*`
- `*.apache.*`
- `com.google.*`
- `com.fasterxml.*`
