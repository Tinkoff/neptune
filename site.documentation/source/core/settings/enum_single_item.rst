Enum-свойства. Один элемент
===========================

.. code-block:: java
   :caption: Перечисление для примера

   package org.my.pack;

   public enum MyEnum {

       ITEM1,
       ITEM2,
       ITEM3
   }

.. code-block:: java
   :caption: Пример реализации

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
   import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;

   @PropertyName("MY_PROPERTY")
   public class MyEnumItemProperty implements
           EnumPropertySuppler<MyEnum> { //<-Указывается тип перечисления

     public static final MyEnumItemProperty PROPERTY = new MyEnumItemProperty();
   }

.. code-block:: properties
   :caption: Как указывается в ``neptune.properties`` / ``neptune.global.properties``

   MY_PROPERTY=ITEM1

.. code-block:: java
   :caption: Пример использования

   package org.my.pack;

   import static org.my.pack.MyEnumItemProperty.PROPERTY;

   public class MyUseCase {

     public void useCase() {
       //чтение
       MyEnum value = PROPERTY.get();
     }
   }