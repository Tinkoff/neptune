# Переключение контента/управление контентом

Важная информация: [Хуки](./../../../core.api/doc/rus/HOOKS.MD)

Интеграции в фреймворками для запуска тестов, которые поддерживаю хуки:

- [Testng](./../../../testng.integration/doc/rus/README.MD)
-

JUnit5. [Интеграция с JUnit 5 не реализована, задача на реализацию](https://github.com/TinkoffCreditSystems/neptune/issues/31)

- Cucumber
  JVM. [Интеграция с Cucumber JVM не реализована, задача на реализацию](https://github.com/TinkoffCreditSystems/neptune/issues/32)

Как правило, приложения имеют множество форм. Чтобы на них попасть, надо выполнять навигацию по страницам, менять
активное окно/вкладку браузера, переключаться в различные фреймы. Ниже, на примерах, разобраны задачи управления
тестируемым контентом, загруженным в браузере.

## Типовые решения и их недостатки

Ниже примеры которые довольно часто можно встретить

```java
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameSupplier.frame;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;

public class Tezt {
    
    @Test
    public void test1() {
        inBrowser().navigateTo("/some/path") //навигация на нужную страницу используя полный URL или относительный путь  
                .switchTo(frame(0)) //когда страница загрузилась и нужно переключиться в нулевой iframe 
                .edit(textField("Some field"), "Some value")
                .edit(checkBox("Some flag"), true)
                .click(button("Submit"));
        
        //далее проверка или дальнейшие действия
    }

    @Test
    public void test2() {
        //предположим, тесты идут последовательно, каждый предыдущий тест меняет страницу,
        //и почти во всех случаях надо возвращаться к одному и тому же контенту.
        inBrowser().navigateTo("/some/path") 
                .switchTo(frame(0)) //выполняем тоже самое, что в test1
                .edit(textField("Some field2"), "Some value2")
                .edit(radioButton("Some flag2"), true)
                .click(button("Submit"));

        //далее проверка или дальнейшие действия
    }

    @Test
    public void test3() {
        //тесты идут последовательно. Предположим, этот тест зависит от предыдущего.
        //Действия, выполненные в предыдущем тесте, привели к тому, что надо переключиться в новое окно/вкладку браузера
        //и переключиться в другой iframe
        inBrowser().switchTo(window(1)) //переход в нужное окно/вкладку браузера
                .switchTo(frame(xpath("./some/xpath"))); //фокус на фрейм, найденный по xpath

        //далее действия и проверка
    }

    @Test
    public void test4() {
        //тесты идут последовательно.
        //Предположим что после test3 нужно вернуться в основное окно/вкладку браузера, где загружено приложение,
        //и перезагрузить дефолтный контент, который нам надо протестировать
        inBrowser().switchTo(window(0))
                .navigateTo("/some/path")
                .switchTo(frame(0));
    }
}

```

Вариант неплохой, но:

- тут есть повторяющиеся действия, которые загромождают тесты

Логичный вариант исправления похож на то, что ниже

```java
public class Tezt {
    
    /**
     * Данный вариант хорош, если все тесты идут последовательно
     * на одной и той же странице
     * и каждый каждый проходящий тест не меняет загруженный контент, т.е. нет необходимости 
     * перед каждым тестом перезагружать одну и ту же страницу.
     * 
    @BeforeClass
    public void prepareClass() {
        inBrowser().navigateTo("/some/path")
                .switchTo(frame(0)); 
    }*/

    //предположим, тесты идут последовательно, каждый предыдущий тест меняет страницу,
    //и почти во всех случаях надо возвращаться к одному и тому же контенту.
    @BeforeMethod
    public void prepare() {
        inBrowser().navigateTo("/some/path") //навигация на нужную страницу используя полный URL или относительный путь
                .switchTo(frame(0)); //когда страница загрузилась и нужно переключиться в нулевой iframe 
    }
    
    @Test
    public void test1() {
        inBrowser().edit(textField("Some field"), "Some value")
                .edit(checkBox("Some flag"), true)
                .click(button("Submit"));

        //далее проверка или дальнейшие действия
    }

    @Test
    public void test2() {
        inBrowser().edit(textField("Some field2"), "Some value2")
                .edit(radioButton("Some flag2"), true)
                .click(button("Submit"));
    }

    @Test
    public void test3() {
        //тесты идут последовательно. Предположим, этот тест зависит от предыдущего
        //действия предыдущего теста привели к тому, что надо переключиться в новое окно/вкладку браузера
        //и переключиться в другой iframe
        inBrowser().switchTo(window(1)) //переход в нужное окно/вкладку браузера
                .switchTo(frame(xpath("./some/xpath"))); //фокус на фрейм, найденный по xpath

        //далее действия и проверка
    }

    @Test
    public void test4() {
        //тесты идут последовательно.
        //Предположим что после test3 нужно вернуться в основное окно/вкладку браузера, где загружено приложение,
        //и перезагрузить дефолтный контент, который нам надо протестировать
        inBrowser().switchTo(window(0))
                .navigateTo("/some/path")
                .switchTo(frame(0));
    }
}
```

<details>
    <summary>Нажми, чтобы прочесть</summary>

- Как видим, для `test3` данная схема неудобна, т.к. нам не важен контент основной страницы, нужно сразу переключиться
  на новое окно/вкладку. Но в данном случае получится, что перед выполнением `test3` все равно будет выполнено
  переключение в дефолтный контент.


- для `test4` данная схема нерабочая, т.к. `prepare` реализован таким образом, что перезагрузка дефолтного контента
  происходит в текущем окне/вкладке браузера. В `test3` было переключение в другое окно/вкладку, и представим, что фокус
  на основное окно после окончания `test3`
  не был восстановлен. Поэтому происходит повторение подготовительного действия в `test4`. Исправить можно следующим
  образом

```java
public class Tezt {
    
    @BeforeMethod
    public void prepare() {
        inBrowser().navigateTo("/some/path") 
                .switchTo(frame(0));
    }
    
    //тесты

    @AfterMethod
    public void after() { //после каждого теста устанавливаем 
        //фокус на основном браузерном окне/вкладке
        inBrowser().switchTo(window(0));
    }
}
```

Но такая реализация не оптимальна. Как вариант, можно организовать методы в группы (Testng) или использовать тэги (
JUnit5). Но есть ли более простые способы?

-

</details>
