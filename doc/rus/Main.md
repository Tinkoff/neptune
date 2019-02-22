# Главная

Данный проект разработан для автоматизации интеграционного тестирования. На данный момент возможно взаимодействие с:
- UI прилоложения (браузер)
- базами данных
- http API

Поддерживаемые фреймворки:
- TestNg. Поддержка многопоточного выполнения тестов.

Интеграция с инструментами для построения отчетов:
- Allure

Вы хотите видеть код автотестов таким?

```java
    @Test
    public void test() {
        perform("Заполнить форму заявки", testContext -> {
            var clientAge = browser
                    .click(on(button("Начать").criteria(shouldBeEnabled()).timeOut(ofMinutes(1))))
                    .edit(valueOfThe(textField("Фамилия"), of(ARROW_DOWN, "Иванов"))
                            .andValueOfThe(textField("Имя"), of("Петр"))
                            .andValueOfThe(textField("Отчество"), of("Сидорович")))
                    .find(widget(FieldsBlock.class, "Возраст клиента"));

            browser.edit(valueOfThe(textField("Дата рождения"), of("01.01.1990")))
                    .click(on(tab("Деньги")))
                    .edit(valueOfThe(select("Желаемая сумма"), "Больше 1.000.0000"))
                    .click(on(button("Все верно")));
        });

        check.verify(thatValue("Наличие кнопки 'Далее'",
                browser.get(presenceOfAnElement(button("Далее")
                        .timeOut(ofSeconds(30))
                        .criteria(shouldBeEnabled()))))
                .suitsCriteria(is(true)))

                .verify(thatValue("Текущая форма",
                        inBrowser().find(widget(Form.class, "Форма заявки")))
                        .suitsCriteria(hasNestedElement(webElement(tagName("span"))
                                .criteria(shouldHaveText("Успешно заполнено. Вы можете продолжать")))));
    }
```

Может таким?
```java

    @Test
    public void test() {
        var catalogItems = dataBase.get(selected(listOfTypeByIds(Catalog.class, 
        "0-930267-39-7", "0-671-73246-3")));

        check.verify(thatValue("'Элементы книжного каталога", catalogItems)
                .suitsCriteria("ISBN коды", 
                        catalogs -> catalogs.stream().map(Catalog::getIsbn).collect(toList()),
                        contains("0-930267-39-7", "0-671-73246-3"))); 
    }
    
    @Test
    public void test2() {
        var c = QCatalog.candidate();
        var catalogItem = dataBase.get(selected(aSingleByQuery(ofType(Catalog.class)
                .where(c.book.name.eq("Ruslan and Ludmila")
                        .and(c.book.author.lastName.eq("Pushkin")))
                .orderBy(c.isbn.desc()))));

        check.verify(thatValue("'Элемент книжного каталога", catalogItems)
                .suitsCriteria("ISBN код", catalog -> Catalog::getIsbn, is("0-930267-39-7")));
    }    

```

Или таким?
```java
    @Test
    public void test() {
        check.verify(thatValue("Статус ответа на http запрос", 
                http.get(statusCodeOf(responseOf(GET("http://127.0.0.1/test_page.html"), ofString()))))
                .suitsCriteria(equalTo(200)));        
    }
    
    @Test
    public void test2() {
        var result = http.get(bodyDataOf(responseOf(GET(format("http://127.0.0.1/data.html", REQUEST_URI)), ofString()),
                "Список тэгов <a>", toNodeList("a"))
                .criteria("В списке 1 тэг <a>", nodeList -> nodeList.size() == 1));

        check.verify(thatValue("Список тэгов <a>", result)
                .suitsCriteria(not(nullValue())));      
    }    
```
Да!? Тогда читайте

# Идея

Идея на которой базируется проект:

- Любой шаг теста можно декомпозировать на ограниченное количество атомарных действий
- Каждое атомарное действие, выполняемое в шаге теста, можно считать шагом
- Общий набор атомарных действий ограничен, поэтому может быть описан полностью 
- Любой шаг теста можно представить в виде функции:
  
  - шаг который выполняет действие и ничего не возвращает - `java.util.function.Consumer`
  - шаг который выполняет действие и возвращает результат - `java.util.function.Function`
  - значение, являющееся аргументом для выполнения функции, условно можно назвать `контекстом`. Этим термином будем оперировать в дальнейшем.
  Например, `контекстом` можно считать открытое соединение с базой данных, запущенный через WebDriver браузер, или работающий клиент для выполнения http запросов и т.п. 
  Так же, технически, контекстом можно считать любую сущность, которая хранит перечисленные ресурсы и предоставляет их для выполнения шага. Сам тест, в рамках которого происходит инициализация названных выше объектов, так же можно считать `контекстом`. 

- шаги могут быть объединены в неразрывные последовательности
  
  - путем последовательного выполнения
  - путем использования одного шага в качестве аргумента для выполнения другого
  
  Например: `В браузере нужно выполнить клик по некоторой кнопке.`   
  Последовательный путь выполнения: `1. найти кнопку`. `2 выполнить клик`
  Используя один шаг в качестве аргумента для другого: 
  Можно действие `click` представить в качестве объекта `java.util.function.Consumer`, а поиск кнопки, по которой нужно выполнить клик, как объект `java.util.function.Function`. 
  Если предположить, что обе функции принимают в качестве аргумента WebDriver, то может получится нечто вроде
  
  ```java
  (Consumer<WebDriver>) webDriver -> {  
    var buttonElement = ((Function<WebDriver, WebElement>) wd -> {  
      return wd.findElement(tagName("button"));
    }).apply(webDriver);
    buttonElement.click();
  }
  ```
  Технически, это частный случай последовательного выполнения 2-х атомарных действий. Но благодаря тому, что обе функции имеют одинаковый входной параметр (контекст), можно 
  передать функцию поиска элемента как аргумент для функции, выполняющей клик. Для наглядности, пример ниже:
  
  ```java
  static Consumer<WebDriver> clickOn(Function<WebDriver, WebElement> on) {
    return webDriver -> {
      on.apply(webDriver).click();
    }
  }

  static Function<? extends SearchContext, WebElement> element(By by) {  
    searchContext -> searchContext.findElement(by);
  }

  ...
  //и тогда
  WebDriver driver = //открытие браузера
  ...
  clickOn(element(tagName("button"))).accept(driver);
  //пример, если целевой элемент находится внутри другого элемента
  clickOn(element(tagName("button")).compose(element(tagName("form")))).accept(driver);
  ```
- тааким образом, при грамотном подходе, можно реализовать BDD-образный java-код. 

# Используемые технологии

- [Используемые технологии](/doc/Tech_Stack.md)

# Начало работы

## Требования
 
 - Операционнаяя система - Windows/Mac Os X/Linux
 - Java Development Kit 11
 - [maven](https://maven.apache.org/) или [gradle](https://gradle.org/)
 
## Общие шаги

1. Указать необходимые зависимости от модулей проекта
2. Создать в корне проекта файл `general.properties`
3. Объявить в нем свойства и указать необходимые значения этих свойств.
4. Произвести необходимую кастомизацию

### Фремворки для автоматизации тестирования
 - [testng.integration](/doc/rus/testng/Main.md)

### Инструменты для репортинга
 - [allure.integration](/doc/rus/allure/Main.md)

### Прочие инструменты 
  
  - [check](/doc/rus/check/Main.md)
  - [core.api](/doc/rus/core/Main.md)
  - [selenium](/doc/rus/selenium/Main.md)
  - [data.base.api](/doc/rus/data.dase/Main.md)
  - [http.api](/doc/rus/data.dase/Main.md)
  
## Для разработчиков 
- [Как начать. Для разработчиков/контрибъютеров](/doc/rus/Get_Started_For_Delelopers.md)  

- [Полный Javadoc](https://tinkoffcreditsystems.github.io/neptune/)  