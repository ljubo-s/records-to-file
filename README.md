# Records To File
## Efficiently write huge amout of records in a spreadsheet file.

* ### Apache POI  
* ### NamedParameterJdbcTemplate  
* ### JPA  
* ### Entity to DTO   
* ### Global Exception Handler  
* ### SSL  
<pre>
Put [cert_file_name].p12 file into the "src/main/resources/keystore" directory.
</pre>
* ### Swagger  
<pre>
https://164.68.100.119:1111/records-to-file/swagger-ui/index.html  
- export: https://164.68.100.119:1111/records-to-file/api-docs  
- import into Postman.  
- for request with Download use Send and Download on sending request, or in response Save Response->Save to a file.  
</pre>
* ### Spring Boot Admin  
<pre>
http://164.68.100.119:9090/sba  
username:sba_monitor  
password:@0HaPnbY2N78  
</pre>

### Application Properties  
Write application-related properties into "src/main/resources/application.properties". 

### Build
`./gradlew bootJar`

### Run
`java -jar records-to-file.jar`  
or  
Create a service on server and start as a service.
