.. code-block:: java
   :caption: Можно унаследовать базовый тест-класс

   import org.junit.jupiter.api.Test;
   import ru.tinkoff.qa.neptune.jupiter.integration.BaseJunit5Test;

   public class MyTest extends BaseJunit5Test {

       @Test
       public test() {
         //some checks
       }
   }

.. code-block:: java
   :caption: Можно подключить расширение

   import org.junit.jupiter.api.Test;
   import org.junit.jupiter.api.extension.ExtendWith;
   import ru.tinkoff.qa.neptune.jupiter.integration.BaseJunit5Test;
   import ru.tinkoff.qa.neptune.jupiter.integration.NeptuneJUnit5Extension;

   @ExtendWith(NeptuneJUnit5Extension.class)
   public class MyTest {

       @Test
       public test() {
         //some checks
       }
   }

.. code-block:: none
   :caption: Можно настроить SPI в `test/resources/` и в файл `org.junit.jupiter.api.extension.Extension` внести запись `ru.tinkoff.qa.neptune.jupiter.integration.NeptuneJUnit5Extension`. После этого ничего с кодом делать не нужно

   $ META-INF
   ├── services
       ├── org.junit.jupiter.api.extension.Extension
