Произвольные объекты
====================

.. code-block:: java
   :caption: Объекты для примера

    package org.my.pack;

    public class MyObject {

        private Object a;

        private Object b;

        private Object c;

        public Object getA() {
            return a;
        }

        public MyObject setA(Object a) {
            this.a = a;
            return this;
        }

        public Object getB() {
            return b;
        }

        public MyObject setB(Object b) {
            this.b = b;
            return this;
        }

        public Object getC() {
            return c;
        }

        public MyObject setC(Object c) {
            this.c = c;
            return this;
        }
    }

    public class MyExtendedObject extends MyObject{

        private Object d;

        public Object getD() {
            return d;
        }

        public MyExtendedObject setD(Object d) {
            this.d = d;
            return this;
        }
    }

Одиночный объект
################

Вариант 1
**********

.. code-block:: java
   :caption: Пример реализации

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
   import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectByClassPropertySupplier;

   @PropertyName("MY_PROPERTY")
   public class MyObjectProperty implements
           ObjectByClassPropertySupplier<MyObject> { // В алмазных скобках
           //указывается тип возвращаемого объекта. Это может быть и интерфейс,
           //и абстрактный класс

       public static final MyObjectProperty PROPERTY = new MyObjectProperty();
   }

.. code-block:: properties
   :caption: Как указывается в ``neptune.properties`` / ``neptune.global.properties``

   # Указывается полное пакетное имя нужного класса
   # ВАЖНО!!!!!:
   # - указанное значение НЕ должно быть интерфейсом,
   #   и НЕ должно быть абстрактным классом
   # - У указанного класса не должно быть объявленных конструкторов,
   #   или среди объявленных конструкторов должен присутствовать
   #   доступный конструктор без параметров
   MY_PROPERTY=org.my.pack.MyExtendedObject

.. code-block:: java
   :caption: Пример использования

   package org.my.pack;

   import static org.my.pack.MyObjectProperty.PROPERTY;

   public class MyUseCase {

     public void useCase() {
       //чтение
       MyObject value = PROPERTY.get();
       //Такой способ подойдет, если параметры объекта заполняются
       // неявно в момент создания этого объекта, либо параметры
       //вернувшегося объекта никакой роли не играют в дальнейшей
       //логике работы
     }
   }

Вариант 2
**********

.. code-block:: java
   :caption: Пример реализации

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
   import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

   import java.util.function.Supplier;

   @PropertyName("MY_PROPERTY")
   public class MyObjectProperty implements
           ObjectPropertySupplier<MyObject, Supplier<MyObject>> {
           //в алмазных скобках 2 параметра:
           // - тип возвращаемого объекта.
           //   Это может быть и интерфейс, и абстрактный класс.
           //
           // - тип объекта, с помощью которого происходит создание и настройка
           //   результирующего объекта. Этот тип должен расширять Supplier<MyObject>.

     public static final MyObjectProperty PROPERTY = new MyObjectProperty();
   }

.. code-block:: java
   :caption: Supplier<MyObject>

   package org.my.pack;

   import java.util.function.Supplier;

   public class MyObjectSupplier implements Supplier<MyObject> {

       @Override
       public MyObject get() {
           return new MyObject() //Если бы у класса был непустой конструктор,
                   // им можно быо бы воспользоваться.
                   .setA(1) //Так выполняется настройка / заполнение результирующего
                   .setB("A") //объекта.
                   .setC(true);
       }
   }

.. code-block:: properties
   :caption: Как указывается в ``neptune.properties`` / ``neptune.global.properties``

   # Указывается полное пакетное имя нужного класса
   # ВАЖНО!!!!!:
   # - указанное значение НЕ должно быть интерфейсом,
   #   и НЕ должно быть абстрактным классом
   # - У указанного класса не должно быть объявленных конструкторов,
   #   или среди объявленных конструкторов должен присутствовать
   #   доступный конструктор без параметров
   MY_PROPERTY=org.my.pack.MyObjectSupplier

.. code-block:: java
   :caption: Пример использования

   package org.my.pack;

   import static org.my.pack.MyObjectProperty.PROPERTY;

   public class MyUseCase {

     public void useCase() {
       //чтение
       MyObject value = PROPERTY.get();
       //Такой способ подойдет, если класс объекта не имеет доступного
       // конструктора без параметров, и если параметры результирующего
       // объекта имеют значение для дальнейшей работы
     }
   }

Коллекция объектов
##################

.. code-block:: java
   :caption: Пример реализации

   package org.my.pack;
   
   import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
   import ru.tinkoff.qa.neptune.core.api.properties.object.MultipleObjectPropertySupplier;
   
   import java.util.function.Supplier;
   
   @PropertyName("MY_PROPERTY")
   public class MyObjectsProperty implements
           MultipleObjectPropertySupplier<MyObject, Supplier<MyObject>> {
           //в алмазных скобках 2 параметра:
           // - тип объектов, из которых состоит возвращаемый лист.
           //   Это может быть и интерфейс, и абстрактный класс.
           //
           // - тип объектов, с помощью которых создается каждый элемент
           //   результирующего листа. Этот тип должен расширять Supplier<MyObject>.
   
     public static final MyObjectsProperty PROPERTY = new MyObjectsProperty();
   }

.. code-block:: java
   :caption: Supplier<MyObject> 1

   package org.my.pack;

   import java.util.function.Supplier;

   public class MyObjectSupplier implements Supplier<MyObject> {

     @Override
     public MyObject get() {
       return new MyObject()
               .setA(1)
               .setB("A")
               .setC(true);
     }
   }

.. code-block:: java
   :caption: Supplier<MyObject> 2

   package org.my.pack;

   import java.util.Date;
   import java.util.function.Supplier;

   public class MyExtendedObjectSupplier implements Supplier<MyObject> {
     @Override
     public MyObject get() {
       return new MyExtendedObject()
               .setD(new Date())
               .setA(2)
               .setB("B")
               .setC(false);
     }
   }

.. code-block:: properties
   :caption: Как указывается в ``neptune.properties`` / ``neptune.global.properties``

   # Указываются полные пакетные имена нужных классов
   # ВАЖНО!!!!!:
   # - указанные значения НЕ должны быть интерфейсами,
   #   и НЕ должны быть абстрактными классами
   # - У указанных классов не должно быть объявленных конструкторов,
   #   или среди объявленных конструкторов должны присутствовать
   #   доступные конструкторы без параметров
   MY_PROPERTY=org.my.pack.MyObjectSupplier,org.my.pack.MyExtendedObjectSupplier

.. code-block:: java
   :caption: Пример использования

   package org.my.pack;

   import java.util.List;

   import static org.my.pack.MyObjectsProperty.PROPERTY;

   public class MyUseCase {

     public void useCase() {
       //чтение
       List<MyObject> value = PROPERTY.get(); //вернется созданный
       //List<> из 2-х элементов
     }
   }