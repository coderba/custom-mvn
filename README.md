# Custom Maven Plugin
Written with Mojo framework for Netaş company.You can change easly checked words in cronjob folder.

1)Add plugin to your pom.xml, inside the plugins tag.
```
      <!-- Custom Maven Plugin for cronjobs -->
      <plugin>
          <groupId>com.genband.plugin</groupId>
          <artifactId>cron-checker</artifactId>
          <version>1</version>
          <configuration>
              <includeFiles>
                  <includeFile>${project.basedir}\sbin\assetup</includeFile>
              </includeFiles>
          </configuration>
          <executions>
              <execution>
                  <phase>install</phase>
                  <goals>
                      <goal>check</goal>
                  </goals>
              </execution>
          </executions>
      </plugin>
```

2)Execution commands;
mvn custom-plugin:cron-checker:1:check