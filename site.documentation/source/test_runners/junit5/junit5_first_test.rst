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