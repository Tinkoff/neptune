.. code-block:: java
   :caption: Можно унаследовать базовый тест-класс

   import org.testng.annotations.Test;
   import ru.tinkoff.qa.neptune.testng.integration.BaseTestNgTest;

   public class MyTest extends BaseTestNgTest {

       @Test
       public test() {
         //some checks
       }
   }

.. code-block:: java
   :caption: Можно подключить лисенер

   import org.testng.annotations.Test;
   import org.testng.annotations.Listeners;

   import ru.tinkoff.qa.neptune.testng.integration.DefaultTestRunningListener;

   @Listeners(DefaultTestRunningListener.class)
   public class MyTest {

       @Test
       public test() {
         //some checks
       }
   }

.. code-block:: none
   :caption: Можно настроить SPI в `test/resources/` и в файл `org.testng.ITestNGListener` внести запись `ru.tinkoff.qa.neptune.testng.integration.DefaultTestRunningListener`. После этого ничего с кодом делать не нужно

   $ META-INF
   ├── services
       ├── org.testng.ITestNGListener
