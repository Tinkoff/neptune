Float-свойства
===============

.. code-block:: java
   :caption: Пример реализации

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
   import ru.tinkoff.qa.neptune.core.api.properties.floats.FloatValuePropertySupplier;


   @PropertyName("MY_PROPERTY")
   public class MyFloatProperty implements FloatValuePropertySupplier {

     public static final MyFloatProperty PROPERTY = new MyFloatProperty();

   }

.. code-block:: properties
   :caption: Как указывается в ``neptune.properties`` / ``neptune.global.properties``

   MY_PROPERTY=2.5

.. code-block:: java
   :caption: Пример использования

   package org.my.pack;


   import static org.my.pack.MyFloatProperty.PROPERTY;

   public class MyUseCase {

     public void useCase() {
       //чтение
       Float value = PROPERTY.get();
     }
   }