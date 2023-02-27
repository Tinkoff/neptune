# PERSISTENCE_UNITS

В свойстве через запятую перечисляются необходимые подключенные источники.
Применяется если значение свойства [SESSION_FACTORY_SOURCE](SESSION_FACTORY_SOURCE.md) равно дефолтному и 
[USE_JPA_CONFIG](USE_JPA_CONFIG.md)`=true`

Пример persistence.xml:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.1"
    xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">
    <persistence-unit name="MySql" transaction-type="JTA">
        <!-- Перечисление классов-маппингов таблиц/коллекций из БД -->
        <class>org.example.entities.TestEntity</class>      
        <properties>
            <!-- Настройки соединения с БД -->
            <property name="javax.persistence.jdbc.driver" value="com.mysql.jdbc.Driver" />
            <property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/test" />
            <property name="javax.persistence.jdbc.user" value="root" />
            <property name="javax.persistence.jdbc.password" value="password" />          
        </properties>
    </persistence-unit>
</persistence>
```

```properties
#Значение свойства указывается так
PERSISTENCE_UNITS=MySql,OracleTest
```

```java
package org.my.pack;

import java.util.List;

import static ru.tinkoff.qa.neptune.hibernate.properties.ConnectionConfig.PERSISTENCE_UNITS;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        List<String> units = PERSISTENCE_UNITS.get();
    }
}
```