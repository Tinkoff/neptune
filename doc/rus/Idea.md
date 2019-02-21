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
