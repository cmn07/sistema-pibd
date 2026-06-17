# Sistema Universidade

Projeto base para a disciplina Projeto e Implementacao de Banco de Dados.

O objetivo deste repositorio e fornecer uma estrutura inicial em Spring Boot para que cada integrante do grupo implemente sua propria funcionalidade depois. Neste momento, o projeto contem apenas configuracao, models simples, repositories/services esqueleto e uma tela inicial.

## Tecnologias

- Java
- Spring Boot
- Spring Web MVC
- Thymeleaf
- JdbcTemplate
- PostgreSQL
- Docker Compose
- Maven

Nao usar neste projeto base:

- Spring Security
- JPA/Hibernate
- Controllers com regras de negocio prontas
- Funcionalidades completas de cadastro, listagem, edicao ou exclusao

## Como subir o banco

Na raiz do projeto, execute:

```bash
docker compose up -d
```

Configuracao do PostgreSQL:

- Host: `localhost`
- Porta: `5432`
- Banco: `universidade`
- Usuario: `postgres`
- Senha: `postgres`

Os scripts em `src/main/resources/db/` sao montados no container para inicializacao do banco. Por enquanto eles possuem apenas comentarios e devem ser preenchidos pelo grupo conforme o desenvolvimento.

## Como acessar o pgAdmin

Acesse:

```text
http://localhost:8081
```

Credenciais:

- Email: `admin@admin.com`
- Senha: `admin`

Para cadastrar o servidor PostgreSQL no pgAdmin:

- Host: `postgres`
- Porta: `5432`
- Maintenance database: `universidade`
- Username: `postgres`
- Password: `postgres`

## Como rodar a aplicacao no IntelliJ

1. Abra a pasta do projeto no IntelliJ.
2. Aguarde o Maven carregar as dependencias.
3. Configure o SDK Java do projeto.
4. Suba o banco com `docker compose up -d`.
5. Execute a classe `SistemaUniversidadeApplication`.
6. Acesse `http://localhost:8080`.

Tambem e possivel rodar pelo terminal:

```bash
./mvnw spring-boot:run
```

Se a porta `8080` estiver ocupada, finalize o processo que esta usando a porta ou rode em outra porta temporariamente:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8082
```

## Estrutura de pastas

```text
src/main/java/br/ufscar/sistema_universidade/
  config/       Configuracoes futuras do projeto
  controller/   Controllers MVC
  dto/          Objetos de transferencia de dados futuros
  model/        Classes Java simples que representam o DER
  repository/   Acesso ao banco com JdbcTemplate e SQL explicito
  service/      Regras de negocio futuras

src/main/resources/
  db/           Scripts SQL do banco
  static/css/   Arquivos CSS
  templates/    Paginas Thymeleaf
```

## Scripts SQL

- `01_create_tables.sql`: criacao das tabelas
- `02_insert_data.sql`: dados iniciais
- `03_indexes.sql`: indices
- `04_triggers_functions_procedures.sql`: triggers, functions e procedures

## Organizacao do trabalho

Cada integrante deve criar uma branch para sua funcionalidade:

```bash
git checkout -b feature/nome-da-funcionalidade
```

Exemplos:

- `feature/pessoas`
- `feature/infraestrutura`
- `feature/acervo`
- `feature/reservas`
- `feature/emprestimos`

Nao fazer commit direto na `main`. Implemente a funcionalidade na sua branch, rode os testes e depois abra merge/pull request para revisao do grupo.

Antes de enviar alteracoes, execute:

```bash
./mvnw test
```
