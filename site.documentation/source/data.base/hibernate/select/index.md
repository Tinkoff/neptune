# Hibernate. Select-операции

Важно:

- [Шаги, возвращающие объекты](../../../quick_start/steps/pattern_steps/get_step/index.md)


Пример модели объекта из базы данных:

```java
package org.mypack;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.copyOf;

@Entity
public class TestEntity {
    
  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private List<String> listData;

  private String[] arrayData;

  public Long getId() {
    return id;
  }

  public TestEntity setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public TestEntity setName(String name) {
    this.name = name;
    return this;
  }

  public List<String> getListData() {
    return listData;
  }

  public TestEntity setListData(List<String> listData) {
    this.listData = listData;
    return this;
  }

  public String[] getArrayData() {
    return arrayData;
  }

  public TestEntity setArrayData(String[] arrayData) {
    this.arrayData = arrayData;
    return this;
  }
}
```


```{toctree}
:hidden:

all.md
Id.md
ByOrder.md
pageable.md
jpa_criteria.md
hql.md
queryDSL.md
```