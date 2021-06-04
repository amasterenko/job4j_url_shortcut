# UrlShortcut  


---  
This project demonstrates the principles of the following technologies:
- Spring Boot (Web, Data, Security)  
- REST API  
- JWT  
- PostgreSQL   

### DB Schema  

![ScreenShot](img/dbschema.png)
  
### Features  



### Configuration:    
Create a PostgreSQL database with the name _urlshortcut_ and add the credentials to _/resources/application.properties_.
The default ones are :
```
spring.datasource.url=jdbc:postgresql://127.0.0.1:5432/urlshortcut
spring.datasource.username=postgres
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver
```
Run _schema.sql_ from _resources/db_ folder on the db.  

### Usage   
Build the JAR file with
```
Linux: ./mvnw clean package  
Windows: .\mvnw.cmd clean package  
```
and then run the JAR file, as follows:  
```
java -jar target/job4j_urlshortcut-1.0.jar
```
The service's address by default:  http://localhost:8080/.  

### Description  