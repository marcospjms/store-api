Criar pacote:
mvn -f pom.xml clean package

Executar
cd target
java -jar store-api-0.0.1-SNAPSHOT.jar

Testar:
mvn test

Visualizar api:
http://localhost:8080/swagger-ui.html