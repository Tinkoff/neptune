# Переключение контента/управление контентом

Важная информация: 

- [Хуки](./../../../core.api/doc/rus/HOOKS.MD)
- [Настройки. BASE_WEB_DRIVER_URL](./SETTINGS.MD#base_web_driver_url)  
- [Работа с окнами/вкладками браузера](./WINDOWS.MD)  
- [Навигация по страницам](./NAVIGATION.MD)
- [Переключение во фреймы](./FRAMES.MD)

Интеграции в фреймворками для запуска тестов, которые поддерживаю хуки:

- [Testng](./../../../testng.integration/doc/rus/README.MD)
- JUnit5. [Интеграция с JUnit 5 не реализована, задача на реализацию](https://github.com/TinkoffCreditSystems/neptune/issues/31)
- Cucumber JVM. [Интеграция с Cucumber JVM не реализована, задача на реализацию](https://github.com/TinkoffCreditSystems/neptune/issues/32)

Как правило, приложения имеют множество форм. Чтобы на них попасть, надо выполнять навигацию по страницам, менять
активное окно/вкладку браузера, переключаться в различные фреймы. Ниже, на примерах, разобраны задачи управления
тестируемым контентом, загруженным в браузере.

## Оглавление
  - [Типовые решения и их недостатки](#Типовые-решения-и-их-недостатки)
  - [Решение, которое предлагает neptune](#Решение-которое-предлагает-neptune)
    - [Аннотация @SwitchToWindow](#Аннотация-switchtowindow)
    - [Аннотация @Navigate](#Аннотация-navigate)
    - [Аннотация @SwitchToFrame](#Аннотация-switchtoframe)
    - [Аннотация @UseDefaultBrowserContent](#Аннотация-usedefaultbrowsercontent)    
  - [Логин/аутентификация и управление контента](#Логинаутентификация-и-управление-контента)

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
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameSupplier.frame;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;

public class Tezt {

    /**
     * Данный вариант хорош, если все тесты идут последовательно
     * на одной и той же странице
     * и каждый каждый проходящий тест не меняет загруженный контент, т.е. нет необходимости 
     * перед каждым тестом перезагружать одну и ту же страницу.
     *
     @BeforeClass public void prepareClass() {
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

- при запуске теста на удаленном окружении, например на [selenoid](https://aerokube.com/selenoid/latest/) или
  [Selenium Grid](https://www.selenium.dev/documentation/en/grid/), может возникнуть ситуация, когда сессия WebDriver
  закрывается спустя какое-то время из-за отсутствия активности. Такая ситуация возможна когда удаленное окружение
  настроено соответствующим образом (так было надо) и между `prepare()` и тестовым методом, требующим открытый браузер,
  выполнялись подготовительные методы или тесты, которым браузер был не нужен, и их выполнение заняло продолжительное
  время.

- недостатки, которые вы сами могли бы добавить...

</details>

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Решение, которое предлагает neptune

```java
import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;
import ru.tinkoff.qa.neptune.selenium.content.management.SwitchToFrame;
import ru.tinkoff.qa.neptune.selenium.content.management.SwitchToWindow;
import ru.tinkoff.qa.neptune.selenium.content.management.UseDefaultBrowserContent;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.content.management.BrowserContentUsage.FOR_EVERY_TEST_METHOD;

//Над классом определяется дефолтный контент, который следует загружать в каждом тесте
@Navigate(to = "/some/path") //здесь указывается полный URL или относительный фрагмент URL страницы, 
// которую надо загрузить
@SwitchToFrame(index = 0) //Здесь указывается индекс фрейма, в который нужно переключиться
//Ниже описывается то, как используется дефолтный контент, описанный выше
@UseDefaultBrowserContent(howOften = FOR_EVERY_TEST_METHOD, //загружается в каждом тесте 
        addFrameParams = true) //Параметры фрейма, в который следует переключаться, и который описан выше,
//будут применены ко всем методам
//для которых указан контент для тестирования, но не указаны параметры фрейма.
//В данном примере это - `test4`
public class Tezt {

    @Test //Данный тест загружает контент, описанный в шапке класса
    public void test1() {
        inBrowser()//контент будет загружен перед выполнением метода `edit` 
                .edit(textField("Some field"), "Some value")
                .edit(checkBox("Some flag"), true)
                .click(button("Submit"));

        //далее проверка или дальнейшие действия
    }

    @Test //Данный тест загружает контент, описанный в шапке класса
    public void test2() {
        inBrowser() //контент будет загружен перед выполнением метода `edit` 
                .edit(textField("Some field2"), "Some value2")
                .edit(radioButton("Some flag2"), true)
                .click(button("Submit"));
    }

    //тесты идут последовательно. Предположим, этот тест зависит от предыдущего
    //действия предыдущего теста привели к тому, что надо переключиться в новое окно/вкладку браузера
    //и переключиться в другой iframe
    @Test //для данного теста описан его специфический контент
    @SwitchToWindow(index = 1) //указываем в какое браузерное окно следует переключаться
    @SwitchToFrame(xpath = "./some/xpath") //тут описан специфический фрейм для переключения. 
    // Параметры, описанные в шапке класса, тут неприменимы
    public void test3() {
        inBrowser();//контент будет загружен перед выполнением любого метода объекта, что возвращается inBrowser()

        //далее действия и проверка
    }

    //тесты идут последовательно.
    //Предположим что после test3 нужно вернуться в основное окно/вкладку браузера, где загружено приложение,
    //и перезагрузить дефолтный контент, который нам надо протестировать
    @Test
    @SwitchToWindow(index = 0) //указываем в какое браузерное окно следует переключаться
    @Navigate(to = "/some/path") //т.к. в шапке класса мы не указывали, нужно ли применять параметры навигации 
    //для специфического для теста контента в тех случаях, когда навигация не указана,
    //то нужно нужно повторить @Navigate(to = "/some/path")
    //В данном случае, `test3` нам мешает использовать дефолтный URL для навигации
    //А переключение во фрейм `@SwitchToFrame(index = 0)` произойдет. Для данного контента фрейм не указан
    public void test4() {
        inBrowser();//контент будет загружен перед выполнением любого метода объекта, что возвращается inBrowser()

        //далее действия и проверка
    }
}
```

Подробности ниже

### Аннотация @SwitchToWindow

<details>
    <summary>Нажми, чтобы прочесть</summary>

Данная аннотация описывает, в какое окно/вкладку браузера следует переключиться.

```java
//@SwitchToWindow(index = 1) 
// Указывает, что надо переключиться во второе окно. Нумерация с 0
```

```java
//@SwitchToWindow(url = "https://github.com") 
//Указывает, что надо переключиться в любую браузерную вкладку/окно, где загружена страница https://github.com
```

```java
//@SwitchToWindow(title = "Github inc") 
//Указывает, что надо переключиться в любую браузерную вкладку/окно, где загружена страница c заголовком Github inc
```

```java
//@SwitchToWindow(url = "https://github.com", title = "Github inc") 
//Комбинация двух вариантов выше
```

```java
//@SwitchToWindow(index = 1, url = "https://github.com", title = "Github inc") 
//Комбинация всех вариантов выше. Т.е. переключиться во второе окно, когда/если в нем 
//загружена страница https://github.com с заголовком Github inc
```

```java
//@SwitchToWindow(title = "^.*\\b(Github)\\b.*$", url = "^.*\\b(github)\\b.*$") 
//так же можно использовать как фрагменты строк, так и регулярные выражения для url и/или заголовка
```

```java
//import static java.time.temporal.ChronoUnit.MINUTES;

//@SwitchToWindow(index = 1, waitingTime = 1, waitingTimeUnit = MINUTES) 
//Если требуется некоторое время ожидания, пока нужное браузерное окно/вкладка не появится, 
//то есть возможность указать время
//waitingTimeUnit - для уточнения, в каких единицах указывать время ожидания
//если его не заполнять - считается в секундах
```

```java
import ru.tinkoff.qa.neptune.selenium.content.management.SwitchToWindow;

//Обозначает, что переключение в это окно/вкладку произойдет один раз,
//или будет происходить в каждом тестовом методе, или любом методе - 
//тестовом и подготовительном (с аннотациями @Before* и @After*),
//при первом вызове внутри этого метода браузерного шага. 
//Частота зависит от настройки @UseDefaultBrowserContent
@SwitchToWindow(url = "https://github.com", title = "Github inc")
public class Tezt {

    //Специфическое окно для данного теста
    //данную аннотацию можно применять и к подготовительному методу, если для него нужен браузер
    @Test
    @SwitchToWindow(index = 1)
    public void test1() {

    }

}
```

В присутствии данной аннотации первое, что будет происходить — переключение в указанное браузерное окно/вкладку.

Параметры окна/вкладки, указанные в виде аннотации `@SwitchToWindow` над классом, наследуются. Могут быть перекрыты
использованием аннотации `@SwitchToWindow` в классе-наследнике.

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

### Аннотация @Navigate

<details>
    <summary>Нажми, чтобы прочесть</summary>

Данная аннотация описывает, какую страницу браузера следует загрузить.

```java
//@Navigate(to = "https://www.google.com/search?q=test+automation")
//Можно указать полный URL для навигации
```

```java
//@Navigate(to = "/search?q=test+automation")
//Можно указать фрагмент относительно того значения, 
//которое указано в свойстве/переменной окружения BASE_WEB_DRIVER_URL
```

```java
import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;

//Обозначает, что навигация по указанной в классе ссылке произойдет один раз,
//или будет происходить в каждом тестовом методе, или любом методе - 
//тестовом и подготовительном (с аннотациями @Before* и @After*),
//при первом вызове внутри этого метода браузерного шага. 
//Частота зависит от настройки @UseDefaultBrowserContent
@Navigate(to = "/search?q=test+automation")
public class Tezt {

    //Специфическая ссылка для данного теста
    //данную аннотацию можно применять и к подготовительному методу, если для него нужен браузер
    @Test
    @Navigate(to = "/search?q=quality+assurance")
    public void test1() {

    }

}
```

Для данной аннотации есть интересная опция. Предположим, что описанный в главах выше тест стал параметризованным по следующему
виду ([TestNg](https://testng.org/doc/documentation-main.html#factories)), и ссылка для навигации теперь не постоянная,
а зависит от переданных параметров. Тогда

```java
import ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;

@Navigate(to = "/{parameter1}/{parameter2}?p3={parameter3}")
//переменные указываются в {}
//Класс должен иметь поля, над которыми проставлены аннотации @BrowserUrlVariable.
//В этих аннотациях должны встречаться все перечисленные переменные, и не более 1 раза
//на все поля в рамках класса.
public class Tezt {

    //Название переменной должно встречаться в шаблоне, указанном аннотацией @Navigate,
    //чтобы значение поля попало в ссылку для навигации или ее фрагмент
    @BrowserUrlVariable(name = "parameter1")
    private final int p1;

    @BrowserUrlVariable(name = "parameter2")
    private final String p2;

    @BrowserUrlVariable(name = "parameter3")
    private final String p3;

    @Factory(dataProvider = "dp")
    public Tezz(int p1, String p2, String p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    @DataProvider
    static public Object[][] dp() {
        return new Object[][]{
                //случайные данные или данные полученные, например,
                //из БД или из ответа от API сервиса
        };
    }


}
```

На самом деле, модификатор поля `final` не обязателен. Примеры ниже.

```java
import ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;

@Navigate(to = "/{parameter1}/{parameter2}?p3={parameter3}")
public class Tezt {

    //Название переменной должно встречаться в шаблоне, указанном аннотацией @Navigate,
    //чтобы значение поля попало в ссылку для навигации или ее фрагмент
    @BrowserUrlVariable(name = "parameter1")
    private int p1;

    @BrowserUrlVariable(name = "parameter2")
    private String p2;

    @BrowserUrlVariable(name = "parameter3")
    private String p3;

    @BeforeClass
    public void prepare() {
        p1;//= инициализируем, указываем число, откуда-то взятое
        p2;//= инициализируем, указываем строку, откуда-то взятую
        p3;//= инициализируем, указываем строку, откуда-то взятую
    }

    @Test
    public void test1() {
        inBrowser()//страница будет загружена перед выполнением метода `edit`
                //поля p1, p2, p3 к этому моменту должны быть заполнены        
                .edit(textField("Some field"), "Some value")
                .edit(checkBox("Some flag"), true)
                .click(button("Submit"));

        //далее проверка или дальнейшие действия
    }
}
```

или

```java
import ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;

public class Tezt {

    //Название переменной должно встречаться в шаблоне, указанном аннотацией @Navigate,
    //чтобы значение поля попало в ссылку для навигации или ее фрагмент
    @BrowserUrlVariable(name = "parameter1")
    private int p1;

    @BrowserUrlVariable(name = "parameter2")
    private String p2;

    @BrowserUrlVariable(name = "parameter3")
    private String p3;

    @BeforeClass
    public void prepare() {
        p1;//= инициализируем, указываем число, откуда-то взятое
        p2;//= инициализируем, указываем строку, откуда-то взятую
        p3;//= инициализируем, указываем строку, откуда-то взятую
    }

    @Test
    @Navigate(to = "/{parameter1}/{parameter2}?p3={parameter3}")
    public void test1() {
        inBrowser()//страница будет загружена перед выполнением метода `edit`
                //поля p1, p2, p3 к этому моменту должны быть заполнены        
                .edit(textField("Some field"), "Some value")
                .edit(checkBox("Some flag"), true)
                .click(button("Submit"));

        //далее проверка или дальнейшие действия
    }
}
```

Не всегда поля класса могут подойти для решения этой задачи. Тогда

```java
import ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;

public class Tezt {

    @Test
    @Navigate(to = "/{parameter1}/{parameter2}?p3={parameter3}")
    public void test1() {
        inBrowser()//страница будет загружена перед выполнением метода `edit`
                //методы getP1, getP2, getP3 к этому моменту должны возвращать валидные значения   
                .edit(textField("Some field"), "Some value")
                .edit(checkBox("Some flag"), true)
                .click(button("Submit"));

        //далее проверка или дальнейшие действия
    }

    //Название переменной должно встречаться в шаблоне, указанном аннотацией @Navigate,
    //чтобы возвращаемое методом значение попало в ссылку для навигации или ее фрагмент
    @BrowserUrlVariable(name = "parameter1")
    public int getP1() {
        //расчет и возврат числа
    }

    @BrowserUrlVariable(name = "parameter2")
    public String getP2() {
        //формирование и возврат строки
    }

    @BrowserUrlVariable(name = "parameter3")
    public String getP3() {
        //формирование и возврат строки
    }
}
```

Аннотацией `@BrowserUrlVariable` можно отмечать поля и методы класса-теста:

- поля примитивных/primitive wrapper типов
- методы объектные и статические, которые возвращают значения примитивных/primitive wrapper типов
- поля типов, отличающихся от примитивных/primitive wrapper. Объекты этих типов должны иметь строковое представление,
  которое можно использовать в качестве значения параметра URL
- методы объектные и статические, которые возвращают значения типов, отличающихся от примитивных/primitive wrapper.
  Объекты этих типов должны иметь строковое представление, которое можно использовать в качестве значения параметра URL

Можно ли использовать _get*_ методы объектов POJO? Можно!

```java
public static class MyPojo {

    private String parameter1;

    private String parameter2;

    private String parameter3;

    public MyPojo setParameter1(String parameter1) {
        this.parameter1 = parameter1;
        return this;
    }

    public String getParameter1() {
        return parameter1;
    }

    public MyPojo setParameter2(String parameter2) {
        this.parameter2 = parameter2;
        return this;
    }

    public String getParameter2() {
        return parameter2;
    }

    public MyPojo setParameter3(String parameter3) {
        this.parameter3 = parameter3;
        return this;
    }

    public String getParameter3() {
        return parameter2;
    }
}
```

И тогда

```java
import ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;

@Navigate(to = "/{parameter1}/{parameter2}?p3={parameter3}")
public class Tezt {

    //Название переменной должно встречаться в шаблоне, указанном аннотацией @Navigate,
    //чтобы значение, возвращенное методом объекта MyPojo, попало в ссылку для навигации или ее фрагмент
    @BrowserUrlVariable(name = "parameter1", method = "getParameter1")
    @BrowserUrlVariable(name = "parameter2", method = "getParameter2")
    @BrowserUrlVariable(name = "parameter3", method = "getParameter3")
    private MyPojo pojo;

    @Test
    public void test1() {
        inBrowser()//страница будет загружена перед выполнением метода `edit`
                //поле pojo к этому моменту должно быть проинициализировано, 
                //а методы должны возвращать значения, отличные от null
                .edit(textField("Some field"), "Some value")
                .edit(checkBox("Some flag"), true)
                .click(button("Submit"));

        //далее проверка или дальнейшие действия
    }
}
```

Всегда ли нужно указывать `@BrowserUrlVariable(name = *)`? Можно не указывать.

```java
import ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;

public class Tezt {
    
    @BrowserUrlVariable
    private int parameter1; //Название поля должно совпадать с названием одной из переменных в шаблоне, 
    // указанном аннотацией @Navigate,
    //чтобы значение поля попало в ссылку для навигации или ее фрагмент

    @BrowserUrlVariable
    private String parameter2;  //Название поля должно совпадать с названием одной из переменных в шаблоне, 
    // указанном аннотацией @Navigate,
    //чтобы значение поля попало в ссылку для навигации или ее фрагмент

    //Название метода должно совпадать с названием одной из переменных в шаблоне, 
    // указанном аннотацией @Navigate,
    //чтобы значение, которое он возвращает, попало в ссылку для навигации или ее фрагмент
    @BrowserUrlVariable
    private String parameter3() {
        //формирование и возврат строки
    }

    @Test
    @Navigate(to = "/{parameter1}/{parameter2}?p3={parameter3}")
    public void test1() {
        inBrowser()//страница будет загружена перед выполнением метода `edit`
                //поля parameter1, parameter2 к этому моменту должны быть заполнены 
                //метод parameter3 к этому моменту должен возвращать значение, отличное от null
                .edit(textField("Some field"), "Some value")
                .edit(checkBox("Some flag"), true)
                .click(button("Submit"));

        //далее проверка или дальнейшие действия
    }
}
```

Что если в query-части ссылки для навигации параметр не должен быть перекодирован?

```java
import ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;

public class Tezt {

    @BrowserUrlVariable(name = "parameter3",
            toEncodeForQueries = false) //Означает, что в query-часть ссылки для навигации 
    //значение этого поля попадет без изменений
    private String p3;

    @Test
    @Navigate(to = "/{parameter1}/{parameter2}?p3={parameter3}")
    public void test1() {
    }
}
```

Что если одно и то же поле или один и тот же метод должны использоваться для разных шаблонов ссылки.

```java
import ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;

@Navigate(to = "/{p2}/{p1}?&p3={p3}")
public class Tezt {

    @BrowserUrlVariable(name = "p1") //значение подставится в шаблон, где есть {p1}, 
    // в тот момент, когда будет происходить навигация по ссылке, указанной над классом
    @BrowserUrlVariable(name = "parameter1") //значение подставится в шаблон, где есть {parameter1}, 
    // в тот момент, когда будет происходить навигация по ссылке, указанной над методом test1
    private int value1;

    @BrowserUrlVariable(name = "p2") //значение подставится в шаблон, где есть {p2}
    // в тот момент, когда будет происходить навигация по ссылке, указанной над классом
    @BrowserUrlVariable(name = "parameter2") //значение подставится в шаблон, где есть {parameter2}, 
    // в тот момент, когда будет происходить навигация по ссылке, указанной над методом test1
    private String value2;

    @BrowserUrlVariable(name = "p3") //значение подставится в шаблон, где есть {p3}
    // в тот момент, когда будет происходить навигация по ссылке, указанной над классом
    @BrowserUrlVariable(name = "parameter3", toEncodeForQueries = false) //значение подставится в тот шаблон,
    // где есть {parameter3}, 
    // в тот момент, когда будет происходить навигация по ссылке, указанной над методом test1
    private String value3() {
        //формирование и возврат строки
    }

    @Test
    @Navigate(to = "/{parameter1}/{parameter2}?p3={parameter3}")
    public void test1() {

    }
}
```

Так тоже можно

```java
import ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;

@Navigate(to = "/{p2}/{p1}?&p3={p3}")
public class Tezt {

    @BrowserUrlVariable //(!!!) //значение подставится в шаблон, где есть {p1}, 
    // в тот момент, когда будет происходить навигация по ссылке, указанной над классом
    @BrowserUrlVariable(name = "parameter1")  //значение подставится в шаблон, где есть {parameter1}, 
    // в тот момент, когда будет происходить навигация по ссылке, указанной над методом test1
    private int p1;

    @BrowserUrlVariable //(!!!) //значение подставится в шаблон, где есть {p2}
    // в тот момент, когда будет происходить навигация по ссылке, указанной над классом
    @BrowserUrlVariable(name = "parameter2") //значение подставится в шаблон, где есть {parameter2}, 
    // в тот момент, когда будет происходить навигация по ссылке, указанной над методом test1
    private String p2;

    @BrowserUrlVariable //(!!!) //значение подставится в шаблон, где есть {p3}
    // в тот момент, когда будет происходить навигация по ссылке, указанной над классом
    @BrowserUrlVariable(name = "parameter3", toEncodeForQueries = false) //значение подставится в шаблон, 
    // где есть {parameter3}, 
    // в тот момент, когда будет происходить навигация по ссылке, указанной над методом test1
    private String p3() {
        //формирование и возврат строки
    }

    @Test
    @Navigate(to = "/{parameter1}/{parameter2}?p3={parameter3}")
    public void test1() {
    }
}
```

Можно усложнить кейс выше использованием POJO с вызовом его _get*_ метода

```java
public static class MyPojo {

    private String parameter2;

    //прочие поля

    public MyPojo setParameter2(String parameter2) {
        this.parameter2 = parameter2;
        return this;
    }

    public String getParameter2() {
        return parameter2;
    }

    //прочие методы
}
```

```java
import ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlVariable;
import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;

@Navigate(to = "/{p2}/{p1}?&p3={p3}")
public class Tezt {

    @BrowserUrlVariable
    @BrowserUrlVariable(name = "parameter1")
    private int p1;

    @BrowserUrlVariable(method = "getParameter2") //(!!!) //значение, возвращенное методом getParameter2, 
    // подставится в шаблон, где есть {p2} в тот момент, когда будет происходить навигация по ссылке, указанной над классом
    @BrowserUrlVariable(name = "parameter2") //значение подставится в шаблон, где есть {parameter2}, 
    // в тот момент, когда будет происходить навигация по ссылке, указанной над методом test1
    private MyPojo p2;

    @BrowserUrlVariable
    @BrowserUrlVariable(name = "parameter3", toEncodeForQueries = false)
    private String p3() {
        //формирование и возврат строки
    }

    @Test
    @Navigate(to = "/{parameter1}/{parameter2}?p3={parameter3}")
    public void test1() {
    }
}
```

Если указаны аннотации `@Navigate` и `SwitchToWindow`, то навигация произойдет после переключения в окно/вкладку.

Параметры навигации, указанные в виде аннотации `@Navigate` над классом, наследуются. Могут быть перекрыты
использованием аннотации `@Navigate` в классе-наследнике.

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

### Аннотация @SwitchToFrame

<details>
    <summary>Нажми, чтобы прочесть</summary>

Данная аннотация описывает, в какой фрейм следует переключаться.

```java
//@SwitchToFrame(index = 1) 
//переключение во фрейм по индексу
```

```java
//@SwitchToFrame(nameOrId = "someNameOrId") 
//переключение во фрейм по имени или id
```

```java
//@SwitchToFrame(tagName = "iframe") 
//переключение во фрейм используя локатор элемента, внутри которого фрейм находится
//Данный пример похож на использование аннотации @FindBy. Содержит те же самые варианты локаторов
```

```java
//@SwitchToFrame(index = 1, waitingTime = 1, waitingTimeUnit = MINUTES) 
//Если требуется некоторое время ожидания, пока фрейм не появится на странице и станет доступным для переключения, 
//то есть возможность указать время
//waitingTimeUnit - для уточнения, в каких единицах указывать время ожидания
//если его не заполнять - считается в секундах
```

Так же можно указать путь из фреймов

```java
//@SwitchToFrame(index = 1)
//@SwitchToFrame(tagName = "iframe") 
//@SwitchToFrame(index = 1, waitingTime = 1, waitingTimeUnit = MINUTES)
//Если используются одновременно несколько  аннотаций, то они образуют путь из фреймов, 
//в которые происходит последовательное переключение
//Следует помнить, что этот путь всегда АБСОЛЮТНЫЙ.
```

```java
import ru.tinkoff.qa.neptune.selenium.content.management.SwitchToFrame;

//Обозначает, что переключение в этот фрейм произойдет один раз,
//или будет происходить в каждом тестовом методе, или любом методе - 
//тестовом и подготовительном (с аннотациями @Before* и @After*),
//при первом вызове внутри этого метода браузерного шага. 
//Частота зависит от настройки @UseDefaultBrowserContent
@SwitchToFrame(index = 1)
public class Tezt {

    //Специфический фрейм для данного теста
    //данную аннотацию можно применять и к подготовительному методу, если для него нужен браузер
    @Test
    @SwitchToFrame(nameOrId = "someNameOrId")
    public void test1() {

    }

}
```

Если указаны аннотации `@SwitchToFrame` и/или `SwitchToWindow`/`@Navigate`, то переключение во фрейм произойдет после
переключения в окно/вкладку и навигации на нужную страницу.

Параметры фрейма(ов), указанные в виде аннотаций `@SwitchToFrame` над классом, наследуются. Могут быть перекрыты
использованием аннотаций `@SwitchToFrame` в классе-наследнике.

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

### Аннотация @UseDefaultBrowserContent

<details>
    <summary>Нажми, чтобы прочесть</summary>

С помощью данной аннотации описывается то, каким образом используется дефолтный контент.

Ниже примеры

```java
import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

@Navigate(to = "/some/path") //переход по ссылке произойдет один раз
//Эквивалентный вариант - UseDefaultBrowserContent(howOften = ONCE)
public class Tezzt {

    //некая подготовка данных в браузере
    @BeforeClass
    public void beforeClass() {
        inBrowser() //навигация на нужную страницу /some/path произойдет здесь
                .edit(textField("Some field"), "Some value")
                .edit(checkBox("Some flag"), true);
        //далеедальнейшие действия
    }

    @Test
    public void test1() {
        inBrowser() //остаемся на загруженной/измененной ранее странице
                .edit(textField("Some field2"), "Some value2")
                .edit(checkBox("Some flag2"), true)
                .click(button("Submit2"));

        //далее проверка или дальнейшие действия
    }

    @Test
    public void test2() {
        inBrowser() //остаемся на загруженной/измененной ранее странице
                .edit(textField("Some field3"), "Some value3")
                .edit(radioButton("Some flag3"), true)
                .click(button("Submit3"));
    }
}

```

```java
import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;
import ru.tinkoff.qa.neptune.selenium.content.management.UseDefaultBrowserContent;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.content.management.BrowserContentUsage.FOR_EVERY_TEST_METHOD;

@Navigate(to = "/some/path")
@UseDefaultBrowserContent(howOften = FOR_EVERY_TEST_METHOD) //означает, что указанные в шапке параметры
//применимы для всех тестовых методов.
public class Tezzt {

    //некая подготовка данных в браузере
    @BeforeClass
    //@Navigate(to = "/some/other/path")
    public void beforeClass() {
        inBrowser() //навигация на нужную страницу /some/path здесь не произойдет
                .edit(textField("Some field"), "Some value")
                .edit(checkBox("Some flag"), true);
        //далеедальнейшие действия
    }

    @Test
    public void test1() {
        inBrowser() //навигация на нужную страницу /some/path произойдет здесь
                .edit(textField("Some field2"), "Some value2")
                .edit(checkBox("Some flag2"), true)
                .click(button("Submit2"));

        //далее проверка или дальнейшие действия
    }

    @Test
    public void test2() {
        inBrowser() //навигация на нужную страницу /some/path произойдет здесь
                .edit(textField("Some field3"), "Some value3")
                .edit(radioButton("Some flag3"), true)
                .click(button("Submit3"));
    }
}

```

```java
import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;
import ru.tinkoff.qa.neptune.selenium.content.management.UseDefaultBrowserContent;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.content.management.BrowserContentUsage.FOR_EVERY_METHOD;

@Navigate(to = "/some/path")
@UseDefaultBrowserContent(howOften = FOR_EVERY_METHOD) //означает, что указанные в шапке параметры
//применимы для всех методов, как тестовых, так и конфигурационных (с аннотациями @Before* и @After*).
public class Tezzt {

    //некая подготовка данных в браузере
    @BeforeClass
    public void beforeClass() {
        inBrowser() //навигация на нужную страницу /some/path произойдет здесь
                .edit(textField("Some field"), "Some value")
                .edit(checkBox("Some flag"), true);

        //далеедальнейшие действия
    }

    @Test
    public void test1() {
        inBrowser() //навигация на нужную страницу /some/path произойдет здесь
                .edit(textField("Some field2"), "Some value2")
                .edit(checkBox("Some flag2"), true)
                .click(button("Submit2"));

        //далее проверка или дальнейшие действия
    }

    @Test
    public void test2() {
        inBrowser() //навигация на нужную страницу /some/path произойдет здесь
                .edit(textField("Some field3"), "Some value3")
                .edit(radioButton("Some flag3"), true)
                .click(button("Submit3"));
    }
}

```

Частота использования дефолтного контента (параметр `howOften`) может зависеть от:

- что этот тест из себя представляет — набор атомарных тестов, сгруппированных в классе, или это пользовательская
  история, и тесты в этом классе зависят один от другого

- всем ли методам (тестовым, конфигурационным), для которых необходим браузер, нужно работать с исходным контентом, или
  они последовательно выполняются на одной и той же странице, меняя ее содержимое.

- как вариант: тесты могут открывать браузер удаленно с использованием `selenoid` и `selenium grid`. Тогда нужно
  учитывать, как много промежуточных действий в рамках одного класса с тестами, для которых браузер не нужен. И как
  долго эти действия выполняются.

Так же параметры контента по умолчанию могут как использоваться самостоятельно и игнорироваться, если над методом
указаны параметры специфического для него контента, так и дополнять специфические для метода параметры.

```java
import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;
import ru.tinkoff.qa.neptune.selenium.content.management.SwitchToFrame;
import ru.tinkoff.qa.neptune.selenium.content.management.UseDefaultBrowserContent;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.content.management.BrowserContentUsage.FOR_EVERY_TEST_METHOD;

@Navigate(to = "/some/path")
@UseDefaultBrowserContent(howOften = FOR_EVERY_TEST_METHOD, //означает, что указанные в шапке параметры
        //применимы для всех тестовых методов.
        addNavigationParams = true)
//для методов, над которыми присутствуют аннотации @SwitchToFrame и/или @SwitchToWindow
//но не указаны параметры навигации в виде @Navigate, будет применен параметр навигации, указанный над классом.
public class Tezzt {

    @Test
    @Navigate(to = "/some/other/path")
    public void test1() {
        inBrowser() //навигация на /some/other/path произойдет здесь
                .edit(textField("Some field"), "Some value")
                .edit(checkBox("Some flag"), true)
                .click(button("Submit"));

        //далее проверка или дальнейшие действия
    }

    @Test
    @SwitchToFrame(index = 1)
    public void test2() {
        inBrowser()//навигация на нужную страницу /some/path произойдет здесь,
                //затем произойдет переключение во фрейм, указанный над методом
                .edit(textField("Some field2"), "Some value2")
                .edit(checkBox("Some flag2"), true)
                .click(button("Submit2"));

        //далее проверка или дальнейшие действия
    }

    @Test
    @SwitchToFrame(index = 2)
    public void test3() {
        inBrowser() //навигация на нужную страницу /some/path произойдет здесь,
                //затем произойдет переключение во фрейм, указанный над методом
                .edit(textField("Some field3"), "Some value3")
                .edit(radioButton("Some flag3"), true)
                .click(button("Submit3"));
    }
}

```

Кроме `addNavigationParams` так же существуют:

- `addWindowParams` - дополнение специфических параметров контента, указанных над методами, параметрами окна/вкладки,
  которые указаны над классом

- `addFrameParams`  - дополнение специфических параметров контента, указанных над методами, параметрами фрейма, которые
  указаны над классом

Параметры, указанные в виде аннотации `@UseDefaultBrowserContent` над классом, наследуются. Могут быть перекрыты
использованием аннотации `@UseDefaultBrowserContent` в классе-наследнике.

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Логин/аутентификация и управление контента

Описанный механизм [логина/аутентификации](./LOGIN_IN_BROWSER.MD) не конфликтует с описанным выше механизмом управления
тестируемым контентом. Сначала будет выполнена попытка логина, затем произойдет попытка загрузки и переключения нужного
контента.

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)