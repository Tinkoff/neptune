# Шаблонизированные шаги

Такие шаги нужны для:

- для инкапсуляции логики выполнения шага
- для повторного использования одних и тех же шагов
- для упрощения написания тестов, если приложение выполняет те или иные действия несинхронно с тестом
- для упрощения написания тестов, если получаемые результат должен соответствовать / фильтроваться по заданному набору критериев

Ниже схематичные примеры того, как работает все шаблонизированные шаги, включенные в модули Neptune

```{toctree}
:maxdepth: 1

get_step/index.md
action_steps.md
```

