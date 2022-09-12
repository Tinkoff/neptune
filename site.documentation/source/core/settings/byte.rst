Byte-свойства
===============

.. code-block:: java
   :caption: Пример реализации

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
   import ru.tinkoff.qa.neptune.core.api.properties.bytes.ByteValuePropertySupplier;


   @PropertyName("MY_PROPERTY")
   public class MyByteProperty implements ByteValuePropertySupplier {

       public static final MyByteProperty PROPERTY = new MyByteProperty();

   }

.. code-block:: properties
   :caption: Как указывается в ``neptune.properties`` / ``neptune.global.properties``

   MY_PROPERTY=2

.. code-block:: java
   :caption: Пример использования

   package org.my.pack;

   import static org.my.pack.MyByteProperty.PROPERTY;

   public class MyUseCase {

     public void useCase() {
       //чтение
       Byte value = PROPERTY.get();
     }
   }