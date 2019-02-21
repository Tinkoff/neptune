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
        var catalogItems = dataBase.get(selected(listOfTypeByIds(Catalog.class, -3,
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
        var result = http.get(bodyDataOf(responseOf(GET(format("%s/data.html", REQUEST_URI)), ofString()),
                "Список тэгов <a>", toNodeList("a"))
                .criteria("В списке 1 тэг <a>", nodeList -> nodeList.size() == 1));

        check.verify(thatValue("Список тэгов <a>", result)
                .suitsCriteria(not(nullValue())));      
    }    
```
Да!? Тогда читайте

- [Используемые технологии](/doc/Tech_Stack.md)
- [Идея](/doc/rus/Idea.md)
- [Как начать. Для разработчиков/контрибъютеров](/doc/rus/Get_Started_For_Delelopers.md)
- [Начало работы](/doc/rus/Get_Started.md)
- Модули
  - [core.api](/doc/rus/core/Main.md)
  - [check](/doc/rus/check/Main.md)
  - [selenium](/doc/rus/selenium/Main.md)
  - [data.base.api](/doc/rus/data.dase/Main.md)
  - [http.api](/doc/rus/data.dase/Main.md)
  - [testng.integration](/doc/rus/testng/Main.md)
  - [allure.integration](/doc/rus/allure/Main.md)
- [Полный Javadoc](https://tinkoffcreditsystems.github.io/neptune/)  