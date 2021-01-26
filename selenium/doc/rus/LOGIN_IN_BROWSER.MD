# Логин / Аутентификация в браузере 

Значительная часть вэб-приложений так или иначе работает с базой пользователей. И очень часто есть необходимость
выполнять вход в приложение используя логин и пароль, код если аутентификация двух-факторная, или какую-либо другую
информацию, по которой пользователь может быть проверен и допущен/не допущен к работе с приложением.

Как правило, аутентификация реализована на сервере, а интерфейс лишь использует результаты его работы.

Что если нам необходимо написать интеграционные/e2e/smoke тесты для приложения, требующего аутентификацию? Пусть даже
этих тестов немного.

## Задача

Написать набор интеграционных тестов для связки сервера и веб-интерфейса приложения. Приложение требует аутентификации.
Каждый тестовый класс использует своего заранее сгенерированного или имеющегося в базе пользователя, тестируемое
приложение может быть подключено к сервису аутентификации/вместо сервиса аутентификации могут использоваться моки. В
некоторых методах-фикстурах или тестовых методах есть необходимость заходить под какими-то другим пользователем.

Предположим, что выбран вариант решения в лоб - переходить на форму ввода логина и пароля, вводить туда данные и
отправлять их нажатием на кнопку.

## Один из вариантов решения

```java
//предположим, логины и пароли мы храним в перечислении
public enum Credentials {
    JOHN_DOE("john_doe", "ххххххххххх");

    private final String login;
    private final String password;
    
    Credentials(String login, String password) {
        this.login = login;
        this.password = password;
    }
     
    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }
}
```

```java
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.textField;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;


public abstract class BaseIntegrationFrontEndTest {
    
    private final Credentials credentials; //логин и пароль для тестов некоего класса
    
    public BaseIntegrationFrontEndTest(Credentials credentials) {
        this.credentials = credentials;
    }
    
    @BeforeClass //в самом начале теста выполняем вход через браузер
    public void loginInBrowser() {
        //выполняем переход на форму...
        inBrowser().edit(textField("Login:"), credentials.getLogin())
                .edit(textField("Password:"), credentials.getPassword())
                .click(button("Enter"));
        //Далее проверка что вход произведен успешно.
    }
}
```

или так

```java
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.textField;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;


public abstract class BaseIntegrationFrontEndTest {

    private final Credentials credentials; //логин и пароль для тестов некоего класса

    public BaseIntegrationFrontEndTest(Credentials credentials) {
        this.credentials = credentials;
    }

    @BeforeClass //в самом начале теста выполняем вход через браузер
    public void loginInBrowser() {
        loginInBrowser(this.credentials);
    }

    //предположим, что хотим повторно использовать метод
    //если нужен другой логин в тесте или подготовительном методе 
    protected void loginInBrowser(Credentials credentials) {
        //выполняем переход на форму...
        inBrowser().edit(textField("Login:"), credentials.getLogin())
                .edit(textField("Password:"), credentials.getPassword())
                .click(button("Enter"));
        //Далее проверка что вход произведен успешно.              
    }
}
```

Далее

```java
public class SomeTest extends BaseIntegrationFrontEndTest {
    
    public SomeTest() {
        super(JOHN_DOE);    
    }

    @Test
    public void test() {
      //логика теста
    } 
   
}
```

Вполне себе неплохое решение. Но у него могут быть недостатки:

- наличие подготовительного метода заставляет заботиться о последовательности других аналогичных методов, т.к. 
какая-то часть из них может требовать того, чтобы пользователь уже был авторизован. Это же обстоятельство требует
того, чтобы метод был хорошо задокументирован. Ну или хотя бы того, чтобы все о нем знали.

- все наследники класса, описанного выше, не могут игнорировать конструктор класса-родителя.

- при запуске теста на удаленном окружении, например на [selenoid](https://aerokube.com/selenoid/latest/) или 
[Selenium Grid](https://www.selenium.dev/documentation/en/grid/), может возникнуть ситуация, когда сессия WebDriver 
закрывается спустя какое-то время из-за отсутствия активности. Такая ситуация возможна когда удаленное окружение настроено 
соответствующим образом (так было надо) и между `loginInBrowser()` и первым тестовым методом, требующим открытый браузер, 
выполнялись подготовительные методы или тесты, которым браузер был не нужен, и их выполнение заняло продолжительное время.
Если такое случилось, возможно переоткрыть браузер, но и авторизоваться тоже придется заново.   

- субъективно, но кому-то может не нравиться то, что в логе или отчете о выполнении теста есть строчки о том, 
как пользователь авторизуется. Это может отвлекать и смещать фокус внимания с полезной информации. Что если 
нам просто достаточно видеть запись, что вход в систему через интерфейс прошел успешно?

- недостатки, которые вы сами могли бы добавить...


## Альтернативное решение, которое предлагает Neptune.

Напишем класс

```java
import java.util.Objects;

import static org.openqa.selenium.By.name;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

/**
 * Класс BrowserCredentials был придуман для того, чтобы пользователь мог описать алгоритм авторизации 
 * в браузере
 */
public class LoginAndPasswordCredentials extends BrowserCredentials<Credentials> {

    //здесь мы запоминаем текущие креды
    private Credentials currentCredentials;

    /**
     * Метод проверяет, что новый объект, содержащий логин и пароль, отличается от текущего
     */
    @Override
    protected boolean areCredentialsDifferent(Credentials credentials) {
        var result = !Objects.equals(credentials, currentCredentials);
        if (result) {
            currentCredentials = credentials;
        }
        return result;
    }

    /**
     * Выполняется вход систему через браузер
     * Здесь WebDriver используется напрямую. Такой дизайн чтобы не вызывать лишний раз контексты
     * и не засорять логи/отчеты.
     * 
     * @param webDriver    открытый браузер
     * @param isNewSession это новая сессия или нет
     */
    @Override
    protected void authentication(WebDriver webDriver, boolean isNewSession) {
        //использовать $ необязательно
        //предположим, что мы хотим видеть в отчете или логе строчку с соответствующим действием
        //и что оно успешно завершилось

        var wait = new WebDriverWait(webDriver, 30);
        $("Логин в браузере. Логин:" + currentCredentials.getLogin(), () -> {
            wait.until(elementToBeClickable(name("login"))).sendKeys(currentCredentials.getLogin());
            wait.until(elementToBeClickable(name("password"))).sendKeys(currentCredentials.getLogin());
            wait.until(elementToBeClickable(name("submit"))).submit();

            //Далее проверка, что логин совершен успешно
        });
    }
}
```
Затем добавим еще класс

```java
package org.my.project;

import java.util.function.Supplier;

public class LoginAndPasswordCredentialsSupplier implements Supplier<LoginAndPasswordCredentials> {

    @Override
    public LoginAndPasswordCredentials get() {
        var creds = new LoginAndPasswordCredentials();
        //наш класс LoginAndPasswordCredentials может содержать дополнительную логику
        //в зависимости от окружения, она может быть применена
        return creds;
    }
}

```

Заполняем свойство [WEB_DRIVER_CREDENTIALS](./SETTINGS.MD#WEB_DRIVER_CREDENTIALS). Подробнее о свойствах, которые возвращают объекты 
см. [статью](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-создания-и-возврата-объектов)

```properties
WEB_DRIVER_CREDENTIALS=org.my.project.LoginAndPasswordCredentialsSupplier
```

И теперь достаточно

```java
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.authentication.BrowserCredentials.changeBrowserLogin;

public class SomeTest {

    @Test
    public void test() {
        //тут мы просто даем указания, что через браузер должен быть
        //выполнен вход в приложение под пользователем JOHN_DOE
        //сам логин здесь не происходит
        changeBrowserLogin(JOHN_DOE);
        
        //Вот тут-то, прямо перед выполнением навигации 
        //по относительному пути /my/desired/path вход в приложение
        //под пользователем JOHN_DOE будет выполнен.
        //После логина, произойдет переход по ссылке 
        inBrowser().navigateTo("/my/desired/path");
        //дальнейшая тестовая логика
    } 
   
}
```

или так

```java
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.authentication.BrowserCredentials.changeBrowserLogin;

public class SomeTest {
    
    @BeforeClass
    public void preparationMethod() {
        //тут мы просто даем указания, что через браузер должен быть
        //выполнен вход в приложение под пользователем JOHN_DOE
        //сам логин здесь не происходит
        changeBrowserLogin(JOHN_DOE);
    }

    //далее идут подготовки или тесты, не требующие браузер,
    //чье выполнение занимает продолжительное время

    @Test
    public void test() {        
        //Вот тут-то, прямо перед выполнением навигации 
        //по относительному пути /my/desired/path вход в приложение
        //под пользователем JOHN_DOE будет выполнен.
        //После логина, произойдет переход по ссылке 
        inBrowser().navigateTo("/my/desired/path");
        //дальнейшая тестовая логика
    } 
   
}
```

## Можно избавиться от явного вызова `changeBrowserLogin`

Для этого надо реализовать [хук](./../../../core.api/doc/rus/HOOKS.MD).