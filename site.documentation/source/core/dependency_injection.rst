Внедрение зависимостей
======================

Neptune имеет свой собственный механизм внедрения зависимостей.

Тестовый класс, как и любой класс, может содержать поля, которые могут быть заполнены в ходе выполнения ``@Before*``-методов.
Но бывает так, что механизм / логика / условия заполнения некоторых из этих полей всегда одни и те же, что ведет к
дублированию кода в `@Before*`-методах или чрезмерному использованию механизма наследования.

.. code-block:: java
   :caption: Класс, выполняющий внедрение зависимостей / инициализацию полей

    package org.my.pack;

    import ru.tinkoff.qa.neptune.core.api.dependency.injection.DependencyInjector;

    import java.lang.reflect.Field;

    public class MyDependencyInjector implements DependencyInjector {

        //прежде чем этот метод вызывается, происходит
        //проверка на то, чтобы поле не было статическим и финальным,
        //так же заполнению подлежат поля,
        //значения которых в момент вызова == null,
        //либо поля примитивных типов (можно, но не рекомендуется)
        @Override
        public boolean toSet(Field field) {
            return //проверяется дополнительное условие, что можно заполнять поле;
        }

        //если поле можно заполнять, то тогда формируется значение для этого поля,
        //используя аннотации над полем или какую-либо другую метаинформацию данного поля ;
        @Override
        public Object getValueToSet(Field field) {
            return //сформированное значение
        }
    }

.. code-block:: none
   :caption: Настройка SPI в `main/resources/` или `test/resources/`

   $ META-INF
   ├── services
       ├── ru.tinkoff.qa.neptune.core.api.dependency.injection.DependencyInjector

В файл ``ru.tinkoff.qa.neptune.core.api.dependency.injection.DependencyInjector`` внести строку ``org.my.pack.MyDependencyInjector``