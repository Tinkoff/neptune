#Как начать. Для разработчиков/контрибъютеров

## Требования
 
 - Операционнаяя система - Windows/Mac Os X/Linux
 - Java Development Kit 11
 - [gradle](https://gradle.org/). Версия не ниже 5.0
 - [IntelligIDEA](https://www.jetbrains.com/idea/). Версия не ниже 2018.2
 
## Шаги

### Установка
- сделать форк проекта. [Инструкция](https://help.github.com/articles/fork-a-repo/#fork-an-example-repository)
- клонировать форк. [Инструкция](https://help.github.com/articles/fork-a-repo/#step-2-create-a-local-clone-of-your-fork)  
- загрузить проект в IntelligIDEA. Загружать как Gradle-проект.
- Указать настройки 
  ![image](https://user-images.githubusercontent.com/4927589/46202183-cf59bc80-c31e-11e8-8a86-81a50948392a.png)
- в файле `gradle.properties` указать (любые значения) следующих свойств
  - REPO_USER_NAME=
  - REPO_USER_PASSWORD=  
  

### Сборка

- при помощи IntelligIDEA: `Build -> Clean Project`, `Build -> Rebuild Project`
- через командную строку 
  - `gradle clean test` - очистка и запуск тестов
  - `gradle clean build` - очистка, сборка 
  - `gradle clean build -x test` - очистка, сборка без запуска тестов
  
### Далее
- исправить баг/заимплементить фичу
- создать пулл-реквест. [Инструкция](https://help.github.com/articles/about-pull-requests/). Не забываем писать тесты для нового кода или ранее непокрытого тестом бага.  