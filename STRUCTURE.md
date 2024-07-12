# Entendendo decisões arquiteturais e a estrutura do projeto

## Requisitos para rodar o projeto

### Setup de ambiente:

- [Java 21](https://www.java.com/pt-BR/)
  - Instalar [`Java 21`](https://www.oracle.com/br/java/technologies/downloads/#java21)
- [Spring Boot 3.3.0](https://spring.io/projects/spring-boot)
  - Usando [`JVM`](https://github.com/nvm-sh/nvm)
    - `winget search Oracle.JDK`
   
### Como rodar na minha máquina?

- Clone o projeto `git clone git@github.com:omattaeus/api-users.git`
  
- Rode `javac RegexApplication.java`
- Pronto 🎉

### Documentação
    
     🕗 EM DESENVOLVIMENTO

- [Documentação](https://github.com/omattaeus/api-users/tree/master/documenta%C3%A7%C3%A3o)
    
- `./documentação`: É onde você pode encontrar os arquivos de documentação, como planilha de teste, digramas e entre outros.
- `./documentação/planilha-de-teste`: É onde você pode encontrar os arquivos de planilha de teste com cobertura de teste prevista de 90% e também fazendo utilização do Pitest para Teste de Mutação para fazer verificação e análises dos testes implementados.
- `./documentação/diagramas`: É onde você pode encontrar os arquivos de diagramas, entre eles: Diagramas De Classe, Diagramas de Casos de Uso e Diagrama De Implantação.
Com os Diagramas podemos ter uma visão mais ampla e geral do sistema ao todo.

### Estrutura do projeto

- [src](https://github.com/omattaeus/api-users/tree/master/src)

- `./src`: É onde você pode encontrar todas as pastas e classes do projeto.
- `./configuration`: É onde estará a configuração de segurança, como quais endpoints serão liberado para acesso e quais serão bloqueados para acesso apenas sendo permito atráves de autenticação.
- `./controllers`: É onde está os controladores da aplicação, ou seja, onde está a parte que será feita a requesição pela URL.
- `./exceptions`: É onde está a toda a parte de tratamento de exceção.
- `./interfaces`: É onde está a toda a parte de Annotations personalizadas, como @EmailValidator e @CellPhoneValidator, com a sua regra de negócio, por exemplo, o Regex para validação.
- `./mapper`: É onde está a toda a parte do Mapper, ou seja, onde os objetos de dominios internos não serão visto em camadas de apresentação, ou seja, por consumidores externos.
- `./models`: É a parte onde estão representados a entidade que sera mapeada para o banco de dados, inclusive enums e records.
- `./repositores`: É a pasta onde estarão a implementação do JPA ou seja, as interfaces, juntamente com métodos declarados mas ainda não implementado.
- `./serialization/converter`: É onde estará a conversão de HTTP utilzando Jackson para serializar e desserializar dados no formatao YAML.
- `./services`: São todas as lógicas da aplicação, aqui estará fazendo todo o processamento de dados.
` ./util`: É a pasta de utilidades, ou seja, por exemplo a classe de MediaType para declarar atributos que serão usados em outras classes.

### Estrutura do projeto

- [Resources](https://github.com/omattaeus/api-users/tree/master/src/main/resources)
- [Resources/Static](https://github.com/omattaeus/api-users/tree/master/src/main/resources/static)
- [Resources/Templates](https://github.com/omattaeus/api-users/tree/master/src/main/resources/templates)
- [Pom.xml](https://github.com/omattaeus/api-users/blob/master/pom.xml)

- `./resources`: Aqui estará toda a configuração de acesso, como a parte de conexão com o banco de dados e outras configurações e também as pasta de **Static** que contém os estilos CSS, imagens e até mesmo o código em Javascript. Já na pasta **Templates** podemos ter toda a parte do HTML do sistema.
- `./pom.xml`: Aqui está todas as dependências necessária para que o projeto funcione.

# ENDPOINTS
### ENDPOINTS PARA LOGIN
### ROLE
Aqui temos o ROLE do Usuário (qual ENDPOINTS o usuário terá liberado para estar fazendo requisições)

    ROLE_CUSTOMER
    ROLE_ADMINISTRATOR

#### ENDPOINTS QUE NÃO PRECISAM DE AUTENTICAÇÃO

            "/auth",
            "/",
            "/login",
            "/login-users",
            "/register",
            "/register-user",
            "/verify-account",
            "/regenerate-otp",
            "/reset-password",
            "/reset-password**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/send-reset-email",
            "/send-reset-email**",
            "/verify-account",
            "/verify-account**",
            "/webhook/stripe",
            "/payment/**",
            "/payment**",
            "/payment/checkout",
            "/payment/teste",
            "/create-payment-intent",
            "/ads.txt",
            "/forgot_password",
            "/reset_password"
    
#### ENDPOINTS QUE PRECISAM DE AUTENTICAÇÃO

            "/users/test"

#### ENDPOINTS DE CUSTOMER

            "/users/test/customer",
            "/users/id/{id}",
            "/users/update",
            "/users/find/by/{firstName}",
            "/api/sms/send",
            "/users/success",
            "/users/all",
            "/users/all-users",
            "/users/all**",
            "/users/create",
            "/users/create-html",
            "/users/create-user",
            "/users/search",
            "/users/update-html",
            "/users/update"

### ENDPOINTS DE ADMIN

            "/admin/test/administrator",
            "/admin/delete/{id}"

#### POST

    localhost:8080/users
    
Podemos estar criando o LOGIN atráves do metódo HTTP POST passando o JSON a seguir:

Criando ROLE CUSTOMER:

    {
      "email": "teste123@gmail.com",
      "password": "123456789",
      "role": "ROLE_CUSTOMER"
    }

Criando ROLE ADMINISTRATOR:

    {
      "email": "teste321@gmail.com",
      "password": "123456",
      "role": "ROLE_ADMINISTRATOR"
    }

Se estiver tudo certo, deverá retornar: 201 CREATED.

Na ROLE a gente pode estar passando qual acesso o usuário terá acesso, baseado nos ENDPOINTS que cada um terá acesso (como mencionado acima).

#### POST (Logando e obtendo um token JWT)

    localhost:8080/users/login

Podemos estar logando e obtendo TOKEN JWT atráves do metódo HTTP POST passando email e password como o JSON a seguir:

    {
      "email": "test@test.com",   
      "password": "123456789"
    }

Se estiver tudo certo, deverá retornar: 200 OK.

#### GET (Testando endpoint com autorização "comum" tanto quanto cliente quanto administrador pode acesso)

    localhost:8080/users/test

Testando endpoint com autorização “comum” (que tanto o cliente quanto o administrador podem acessar), 
para isso nós copiamos o token gerado na requisição anterior sem as aspas, e colamos no Authorization.

Se estiver tudo certo deverá retornar está mensagem:

    Autenticado com sucesso

É deverá retornar: 200 OK

#### GET (Testando endpoint que apenas usuário com permissão de cliente tem acesso)

    localhost:8080/users/test/customer

Testando endpoint que apenas o usuário comum tem acesso, 
para isso nós copiamos o token gerado na requisição anterior sem as aspas, e colamos no Authorization.

Se estiver tudo certo deverá retornar está mensagem:

    Cliente autenticado com sucesso

É deverá retornar: 200 OK

--------
### ENDPOINTS DE CADASTRO DE USUÁRIO
Todos os Endpoints foi testado pela ferramenda do POSTMAN.

#### GET (por id)


  
    localhost:8080/regex/id/{id}

Podemos estar passando na URL qual ID a genter quer pesquisar, por exemplo:

    localhost:8080/regex/id/2

Onde está {id} a gente pode estar trocado pelo ID que a gente deseja buscar.

É Deve retornar este JSON:

    {
      "id": 2,
      "username": "15457585",
      "fullname": "Teste Teste",
      "email": "qualqu222@teste.com",
      "cellphone": "(12) 91334-1234"
    }

É deverá retornar: 200 OK

#### GET (todos os usuários)


  
    localhost:8080/regex/all

Deve retornar todos os usuários em formato JSON (uma lista com todos os usuários)

É deverá retornar: 200 OK

#### GET (com busca paginada)

    localhost:8080/regex/find/by/{firstName}

Neste ENDPOINTS podemos estar passando na própria URL qual dados a gente quer buscar.
Por exemplo: Quero encontrar pessoas que tenham no nome as letras "mat", então a eu passaria assim:

      /regex/find/by/mat

Se eu quiser fazer uma busca páginada, eu posso estar passando a página "page", o tamanho da página (quantos dados terá) "size" e qual será a ordenação dos dados "asc" ou "desc".
Então ficaria assim a URL:

      localhost:8080/regex/find/by/mat?page=0&size=12&direction=asc

Se estiver tudo certo, deverá retornar: 200 OK

#### POST (criar usuário)


  
    localhost:8080/regex/create

Neste Endpoint deve ser passado um usuário, com username, fullName, email e cellphone.

    {
      "username": "15457585",
      "fullname": "Teste Teste",
      "email": "qualqu222@teste.com",
      "cellphone": "(12) 91334-1234"
    }

Neste ENDPOINTS terá validações para verificar se o USERNAME está escrito de 6 a 12 caracteres, se o FULL NAME não está vazio e se o EMAIL está escrito de forma correta.
Também terá validações para verificar se o USERNAME ou EMAIL não duplicado, se já existe um usuário com esses mesmo dados.

Se estiver tudo certo, deverá retornar: 201 CREATED.

#### PUT (alterar dados)

    localhost:8080/regex/update

Deve ser passado como JSON a alteração que desejar e também o ID no JSON (o id no formato Long)

    {
      "id": 2,
      "username": "12445857",
      "fullname": "Teste2",
      "email": "qualquuercoisa222@teste.com",
      "cellphone": "(12) 91334-1234"
    }

É deverá retornar: 200 OK

#### DELETE (excluir dados)

    localhost:8080/regex/delete/{id}

Neste Endpoints passado pelo ID qual usuário será deletado do banco de dados.

Deverá retornar: 204 NO CONTENT.

--------
