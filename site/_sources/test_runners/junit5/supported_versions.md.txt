# Поддерживаемые версии JUnit 5

Версии Neptune до `0.24.0-ALPHA` предоставляли следующие зависимости от JUnit 5 транзитивно:

- `group: 'org.junit.jupiter', name: 'junit-jupiter-engine', version: (,5.8.2]`
- `group: 'org.junit.jupiter', name: 'junit-jupiter-api', version: (,5.8.2]`

Начиная с v`0.24.0-ALPHA` эти зависимости транзитивными не являются. Ниже таблица версий Neptune
и поддерживаемых ими версий Junit 5:

| Диапазон версий Neptune | Диапазон поддерживаемых  версий JUnit5                                                        |
|-------------------------|-----------------------------------------------------------------------------------------------|
| [0.24.0-ALPHA,)         | - junit-jupiter-engine и junit-jupiter-api: [5.8.0,)<br/> - junit-platform-launcher: [1.8.0,) |
