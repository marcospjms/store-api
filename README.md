# Configuração inicial #
## Banco de dados ##
Para executar é necessário atualizar o application.properties. A seguinte propriedade deve ser atualizada para o arquivo que ficará seu banco de dados:

* spring.datasource.url=jdbc:h2:file:///home/marcos/testdb

## Criar usuário inicial ##
Como o sistema foi construído usando spring boot security, para conseguir requisitar alguns recursos será necessário criar um usuário. Basta enviar um post para seguinte url:
* http://localhost:8080/public/users/
Deverá ser enviado um json com o username, password, nome e email. Se for do interesse criar um admin, enviar a mesma requisição para:
* http://localhost:8080/public/users/admin
Essa url foi criada para facilitar a criação de usuário admin para fins de teste.

# Passos para execução #
    
1. Criar pacote:
    1. mvn -f pom.xml clean package

2. Executar
    1. cd target
    2. a -jar store-api-0.0.1-SNAPSHOT.jar

1. Testar:
    1. mvn test

# Visualizar a api #
Com o projeto em execução, será possível visualizar a api a partir:
* http://localhost:8080/swagger-ui.html
