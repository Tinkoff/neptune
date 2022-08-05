Для POJO
==========================

.. code-block:: java
   :caption: Примеры POJO

   package org.mypack;

   public class SomePojo {

       private Object a;

       private Object b;

       public Object getA() {
           return a;
       }

       public SomePojo setA(Object a) {
           this.a = a;
           return this;
       }

       private Object getB() {
           return b;
       }

       public SomePojo setB(Object b) {
           this.b = b;
           return this;
       }
   }

.. code-block:: java
   :caption: Примеры использования матчера для POJO

   package org.mypack;

   import static org.hamcrest.MatcherAssert.assertThat;
   import static org.hamcrest.Matchers.endsWith;
   import static org.hamcrest.Matchers.startsWith;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.pojo
            .PojoGetterReturnsMatcher.getterReturns;

   //пример использования
   public class MyTest {

       private static final SomePojo SOME_POJO = new SomePojo()
               .setA("AB")
               .setB(false);

       @Test(description = "Проверка значения, "
             + "которое возвращает публичный get*-метод")
       public void test() {
           assertThat(SOME_POJO,
               // можно проверять на
               // равенство указанному значению
               getterReturns("getA", "AB"));

           assertThat(SOME_POJO,
               //можно проверять на
               getterReturns("getA", getterReturns(
                   // соответствие переданным критериям
                   "getA", startsWith("A"), endsWith("B")
               ))
           );
       }
   }

