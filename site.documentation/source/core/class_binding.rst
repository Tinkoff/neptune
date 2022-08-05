Связывание классов
==================

Описанный ниже механизм реализует не-ссылочную связь один ко многим между классами, написанными для тестирования,
и классами продуктового кода.

Связывание без учета наследования
#################################

.. code-block:: java
   :caption: Продуктовый код

    package org.mypack;

    public class A {
        //реализация и логика
    }

    public class B {
        //реализация и логика
    }

.. code-block:: java
   :caption: Связь между тестовым и продуктовыми классами

   package org.mypack;

    import ru.tinkoff.qa.neptune.core.api.binding.Bind;

    @Bind(to = A.class)
    @Bind(to = B.class)
    public class C { //Данный класс как-то надо привязать к А и B
    }

.. code-block:: java
   :caption: Связь между полем тестового класса и продуктовыми классами

    package org.mypack;

    import ru.tinkoff.qa.neptune.core.api.binding.Bind;

    public class D {

      @Bind(to = A.class)
      @Bind(to = B.class)
      private Object f; //Данное поле как-то надо привязать к А и B
    }

Связывание с учетом наследования
#################################

.. code-block:: java
   :caption: Продуктовый код

    package org.mypack;

    public class E extends A {
      //реализация и логика
    }

    public class F extends A {
      //реализация и логика
    }

.. code-block:: java
   :caption: Связь. Пример

    package org.mypack;

    import ru.tinkoff.qa.neptune.core.api.binding.Bind;

    @Bind(to = A.class,
            withSubclasses = true, //привязка к классам-наследникам
            //можно перечислить, какие наследники класса A должны быть исключены
            exclude = {E.class})
    //точно так же можно аннотировать поля
    public class G {
    }

Пример получения связанных элементов кода
##########################################

.. code-block:: java
   :caption: Получение связанных элемента кода

    package org.mypack;

    import java.lang.reflect.AnnotatedElement;
    import java.util.List;

    import static ru.tinkoff.qa.neptune.core.api.binding.Bind.DefaultBindReader.getBoundTo;

    public class SomeCode {

      public void someMethod() {
        //получает список связанных классов и полей
        List<AnnotatedElement> annotatedElements = getBoundTo(A.class);

        //Обработки связей реализована для каждого модуля Neptune по-своему
      }
    }