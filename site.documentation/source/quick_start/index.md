# Быстрый старт

Требования:

- Java >= 11

- Если необходимо использовать Kotlin, то >= 1.4

- Maven или gradle

- ⚠️ _Необходим доступ к maven-репозиторию [https://nexus-new.tcsbank.ru/repository/mvn-bigops-qa](https://nexus-new.tcsbank.ru/repository/mvn-bigops-qa)_.
  Если доступа нет, то рекомендуется необходимые модули собрать локально. См [документ для контрибъюторов](start_to_contribute.md)


Первые действия:

- В зависимости от используемого тест-раннера:
   
   - если репортинг не нужен, то доступны интеграции Neptune c [Junit 5](./../test_runners/junit5/index.md) и 
     [TestNG](./../test_runners/testng/index.md)
   - если репортинг имеет значение, то доступны:
  
     - [Интеграция Junit5 + Allure](./../test_runners/junit5/allure.jupiter.bridge.md)
     - [Интеграция NestNG + Allure](./../test_runners/testng/allure.testng.bridge.md)

- Указать в зависимостях необходимые модули. Описание каждого модуля см. соответствующей секции документации.

- указать необходимые [настройки](settings/index.md)

```{toctree}
:hidden:

settings/index.md
```