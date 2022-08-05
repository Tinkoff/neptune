Сериализация и десериализация
==============================

Инструменты и библиотеки Java для сериализации и десериализации данных очень разнообразны. Для
уменьшения сложностей, связанных с этим, Neptune включает в себя описанный ниже механизм.

⚠️ *Рекомендуется повторно использовать сущности, механизмы и настройки сериализации/десериализации, которые использует тестируемое приложение, для релевантности*

.. code-block:: java
   :caption: Интерфейс фасада для сериализации и десериализации, который надо реализовать

    import org.my.pack;

    import com.fasterxml.jackson.core.type.TypeReference;
    import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

    public class MyDataTransformer implements DataTransformer {

        @Override
        public <T> T deserialize(String message, Class<T> cls) {
            //десериализация с использованием класса объекта,
            // в который должна быть преобразована строка
        }

        @Override //Для ссылки на нужный тип используется
        // com.fasterxml.jackson.core.type.TypeReference
        public <T> T deserialize(String string, TypeReference<T> type) {
            //десериализация с использованием типа объекта,
            // в который должна быть преобразована строка
        }

        @Override
        public String serialize(Object obj) {
            //сериализация объекта в строку
        }
    }

.. code-block:: java
   :caption: Пример для ``com.fasterxml.jackson``

    import org.my.pack;

    import com.fasterxml.jackson.core.JsonProcessingException;
    import com.fasterxml.jackson.core.type.TypeReference;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

    public class MyJacksonDataTransformer implements DataTransformer {

        private final ObjectMapper mapper;

        public MyJacksonDataTransformer() {
            mapper = new ObjectMapper()
            //Указываем нужные настройки
            ;
        }

        @Override
        public <T> T deserialize(String message, Class<T> cls) {
            try {
                return mapper.readValue(message, cls);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public <T> T deserialize(String string, TypeReference<T> type) {
            try {
                return mapper.readValue(message, type);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String serialize(Object obj) {
            try {
                return mapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

.. code-block:: java
   :caption: Пример для ``com.google.gson``

    import org.my.pack;

    import com.fasterxml.jackson.core.type.TypeReference;
    import com.google.gson.Gson;
    import com.google.gson.GsonBuilder;
    import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

    public class MyGsonDataTransformer implements DataTransformer {

        public MyGsonDataTransformer() {
            this.gson = new GsonBuilder()
                    //Указываем нужные настройки
                    .create();
        }

        @Override
        public <T> T deserialize(String message, Class<T> cls) {
            return gson.fromJson(message, cls);
        }

        @Override
        public <T> T deserialize(String string, TypeReference<T> type) {
            return gson.fromJson(string, type.getType());
        }

        @Override
        public String serialize(Object obj) {
            return gson.toJson(obj);
        }
    }