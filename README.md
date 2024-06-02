# API Users - Cadastro de Usuário
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
--------

### SOBRE
A API de Cadastro de Usuário consiste no cadastro de usuário pelo JSON, passando parâmetros como username, fullname, email e cellphone.
A API foi criada para estudar validações no Spring Boot através do REGEX, sendo assim, o USERNAME não pode ser nulo e tem que ter de 6 a 12 caracteres, o FULLNAME não pode estar vazio,
é através do REGEX a API irá verificar se o e-mail está escrito de forma correta e da mesma forma o REGEX irá verificar se o CELLPHONE está escrito da forma correta.
É podemos também estar criando login de usuário passando email, password e qual acesso o usuário terá para acessar os ENDPOINTS.

--------
### ENDPOINTS PARA LOGIN
### ROLE
Aqui temos o ROLE do Usuário (qual ENDPOINTS o usuário terá liberado para estar fazendo requisições)

    ROLE_CUSTOMER
    ROLE_ADMINISTRATOR

#### ENDPOINTS QUE NÃO PRECISAM DE AUTENTICAÇÃO

    localhost:8080/users
    localhost:8080/users/login
    
#### ENDPOINTS QUE PRECISAM DE AUTENTICAÇÃO

    localhost:8080/users/test

#### ENDPOINTS DE CUSTOMER

     localhost:8080/users/test/customer
     localhost:8080/regex/create
     localhost:8080/regex/all
     localhost:8080/regex/id/{id}
     localhost:8080/regex/update
     localhost:8080/regex/find/by/{firstName}

### ENDPOINTS DE ADMIN


    localhost:8080/users/test/administrator
    localhost:8080/regex/delete/{id}


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

Deve retornar este JSON:

    {
      "id": 2,
      "username": "15457585",
      "fullname": "Teste Teste",
      "email": "qualqu222@teste.com",
      "cellphone": "(12) 91334-1234"
    }

#### GET (todos os usuários)


  
    localhost:8080/regex/all

Deve retornar todos os usuários em formato JSON (uma lista com todos os usuários)

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

#### DELETE (excluir dados)

    localhost:8080/regex/delete/{id}

Neste Endpoints passado pelo ID qual usuário será deletado do banco de dados.
Deverá retornar: 204 NO CONTENT.

--------
## Vamos nos conectar :handshake:

<a href="https://www.linkedin.com/in/mateusgd/"><img src="https://cdn2.iconfinder.com/data/icons/social-media-2285/512/1_Linkedin_unofficial_colored_svg-128.png" width="40"></a>|
|--
