# Важно
Если у вас есть директория tomcat/libexec/uploads/iamges и в ней есть файлы, необходимо в [application.properties](src/main/resources/application.properties) 
установить параметр ```clearUploadDir=false```. Иначе после остановки приложения все файлы в данной директории удалятся.
## Запуск проекта

- [**Скачиваем TomCat с официального сайта**](https://tomcat.apache.org/download-90.cgi)
- `Настройка TomCat`

  В установленной директории tomcat необходимо в tomcat/libexec/conf в файле **tomcat-users.xml** установить логин и
  пароль
  для входа в webapps tomcat в ui веб сервера

  ```
  <user username="yourUsername" password="yourPassword" roles="manager-gui"/>
  <user username="yourUsername" password="yourPassword" roles="manager-script"/>
  ```
  В файле **server.xml** необходимо задать порт, на котором необходимо развернуть TomCat (Установить **8888**)

    ````
      port="8888" protocol="HTTP/1.1"
      connectionTimeout="20000"
      redirectPort="8443" 
    ````

- `Установка локальной Базы данных`

    - Разворачиваем Postgres Docker Container.
    - Устанавливаем необходимые параметры в **application.properties**
- `Собираем проект`
- `Запуск приложения`

  Копируем **my-blog-1.0-SNAPSHOT**.war из my-blog/build/libs в tomcat/libexec/webapps/
- `Переходим по http://localhost:8888/my-blog-1.0-SNAPSHOT/posts`