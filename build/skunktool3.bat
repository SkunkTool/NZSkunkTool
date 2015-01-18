SET JAVA_HOME=c:\Java\sun_i586_jdk1.7.0_25

::%JAVA_HOME%\bin\java -Dvendor=shell -DvendorFile=servers.properties -XX:MaxPermSize=256m -Xms512m -Xmx512m -Dfile.encoding=UTF-8 -Dyfs.logall=N -jar jar\nzskunktool2.jar
%JAVA_HOME%\bin\java -cp properties;resource;lib_app/*;nzskunktool3.jar -Dvendor=shell -DvendorFile=servers.properties -XX:MaxPermSize=256m -Xms512m -Xmx512m -Dfile.encoding=UTF-8 -Dyfs.logall=N com.javafx.main.Main