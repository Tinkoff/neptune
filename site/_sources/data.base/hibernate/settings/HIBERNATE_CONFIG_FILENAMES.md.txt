# HIBERNATE_CONFIG_FILENAMES

В свойстве через запятую перечисляются названия необходимых `*.cfg.xml`-файлов с настройками подключения.
Применяется если значение свойства [SESSION_FACTORY_SOURCE](SESSION_FACTORY_SOURCE.md) равно дефолтному и
[USE_JPA_CONFIG](USE_JPA_CONFIG.md)`=false`

Пример *.cfg.xml:

```xml
<?xml version='1.0' encoding='utf-8'?>
<!DOCTYPE hibernate-configuration PUBLIC
"-//Hibernate/Hibernate Configuration DTD//EN"
"https://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">

<hibernate-configuration>
    <session-factory>
        <!-- Настройки соединения с БД -->
        <property name="hibernate.connection.driver_class">com.mysql.jdbc.Driver</property>
        <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/test</property>
        <property name="hibernate.connection.username">root</property>
        <property name="hibernate.connection.password">password</property>
      
        <!-- Настройки SQL диалекта -->
        <property name="dialect">org.hibernate.dialect.MySQLDialect</property>
        <property name="hibernate.current_session_context_class">thread</property>
        <property name="hibernate.connection.CharSet">utf8</property>
        <property name="hibernate.connection.characterEncoding">utf8</property>
        <property name="hibernate.connection.useUnicode">true</property>

        <!-- Перечисление классов-маппингов таблиц/коллекций из БД или пакетов с ними -->
        <mapping package="org.example.entities" />

    </session-factory>
</hibernate-configuration>
```

```properties
#Значение свойства указывается так
HIBERNATE_CONFIG_FILENAMES=mysql.cfg.xml,oracletest.cfg.xml
```

```java
package org.my.pack;

import java.util.List;

import static ru.tinkoff.qa.neptune.hibernate.properties.ConnectionConfig.HIBERNATE_CONFIG_FILENAMES;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        List<String> fileNames = HIBERNATE_CONFIG_FILENAMES.get();
    }
}
```

