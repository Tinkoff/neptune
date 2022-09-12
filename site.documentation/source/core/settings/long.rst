Long-свойства
================

.. code-block:: java
   :caption: Пример реализации

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
   import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;


   @PropertyName("MY_PROPERTY")
   public class MyLongProperty implements LongValuePropertySupplier {

     public static final MyLongProperty PROPERTY = new MyLongProperty();

   }

.. code-block:: properties
   :caption: Как указывается в ``neptune.properties`` / ``neptune.global.properties``

   MY_PROPERTY=2

.. code-block:: java
   :caption: Пример использования

   package org.my.pack;


   import static org.my.pack.MyLongProperty.PROPERTY;

   public class MyUseCase {

     public void useCase() {
       //чтение
       Long value = PROPERTY.get();
     }
   }