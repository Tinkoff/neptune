.. code-block:: xml
   :caption: maven. build/plugins

   <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>exec-maven-plugin</artifactId>
        <version>3.0.0</version>
        <executions>
             <execution>
                 <id>generate properties</id>
                 <phase>compile</phase>
                 <goals>
                     <goal>java</goal>
                 </goals>
                 <configuration>
                     <mainClass>ru.tinkoff.qa.neptune.core.api.localization.ResourceBundleGenerator</mainClass>
                     <arguments>
                          <!--Нужная локаль-->
                          <argument>de_DE</argument>
                          <!--или ${basedir}/src/main/resources-->
                          <!--или любая другая директория-->
                          <argument>${basedir}/src/test/resources</argument>
                          <!--кастомный бандл (true) или дефолтный (false)-->
                          <!--В данном случае false-->
                          <argument>false</argument>
                          <!--Имя бандла, который надо сформировать-->
                          <!--У каждого модуля Neptune уникальный набор имен бандлов-->
                          <!--У данного это - core-->
                          <!--Если не указывать, то сформируются бандлы для-->
                          <!--всех подключенных модулей-->
                          <argument>name_of_the_module</argument>
                     </arguments>
                 </configuration>
             </execution>
        </executions>
   </plugin>