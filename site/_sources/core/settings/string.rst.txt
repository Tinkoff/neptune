String-свойства
===============

.. code-block:: java
   :caption: Пример реализации

    package org.my.pack;

    import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
    import ru.tinkoff.qa.neptune.core.api.properties.string.StringValuePropertySupplier;


    @PropertyName("MY_PROPERTY")
    public class MyStringProperty implements StringValuePropertySupplier {

      public static final MyStringProperty PROPERTY = new MyStringProperty();

    }

.. code-block:: properties
   :caption: Как указывается в ``neptune.properties`` / ``neptune.global.properties``

   MY_PROPERTY=Some string

.. code-block:: java
   :caption: Пример использования

   package org.my.pack;

   import static org.my.pack.MyStringProperty.PROPERTY;

   public class MyUseCase {

     public void useCase() {
       //чтение
       String value = PROPERTY.get();
     }
   }