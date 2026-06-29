# Sistema Universidade

Aplicacao web da disciplina Projeto e Implementacao de Banco de Dados para o subsistema **Infraestrutura e Biblioteca** de uma universidade.

O projeto usa o DER/esquema relacional definido pelo grupo e serve como base integrada para a Etapa 3: interface web basica, acesso ao PostgreSQL via Java, SQL explicito com `JdbcTemplate`, regras de banco em scripts SQL e funcionalidades separadas por integrantes.

## Tecnologias

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Security
- Thymeleaf
- Thymeleaf Extras Spring Security
- JdbcTemplate
- PostgreSQL
- pgAdmin
- Docker Compose
- Maven

Nao usar neste projeto:

- JPA
- Hibernate
- `@Entity`
- Spring Data JPA

## Funcionalidades existentes

- Gestao de pessoas, usuarios, discentes, funcionarios e administradores.
- Gestao de infraestrutura com campus e predios.
- Relatorio SQL de salas por campus/predio.
- Relatorio de reservas por periodo e status, relacionando solicitante, sala, predio e campus.
- Reservas com validacao de funcionario solicitante e bloqueio de conflito de horario.
- Login com Spring Security e perfis derivados do DER.
- Gestao de acervo, emprestimos e pendencias da biblioteca.

## Banco com Docker

Suba PostgreSQL e pgAdmin na raiz do projeto:

```bash
docker compose up -d
```

PostgreSQL:

- Host local: `localhost`
- Host dentro do Docker/pgAdmin: `postgres`
- Porta local: `5433`
- Porta dentro do Docker/pgAdmin: `5432`
- Banco: `universidade`
- Usuario: `postgres`
- Senha: `postgres`

Os scripts em `src/main/resources/db/` sao montados em `/docker-entrypoint-initdb.d` e executados na primeira criacao do volume do PostgreSQL.

Se alterar scripts e quiser recriar o banco local de desenvolvimento:

```bash
docker compose down -v
docker compose up -d
```

## pgAdmin

Acesse:

```text
http://localhost:8083
```

Credenciais:

- Email: `admin@admin.com`
- Senha: `admin`

Servidor PostgreSQL no pgAdmin:

- Host: `postgres`
- Porta: `5432`
- Maintenance database: `universidade`
- Username: `postgres`
- Password: `postgres`

## Rodar no IntelliJ

1. Abra a pasta do projeto no IntelliJ.
2. Aguarde o Maven carregar as dependencias.
3. Configure o SDK Java 17.
4. Suba o banco com `docker compose up -d`.
5. Execute `SistemaUniversidadeApplication`.
6. Acesse `http://localhost:8082`.

Tambem e possivel rodar pelo terminal:

```bash
./mvnw spring-boot:run
```

## Login de teste

Todos usam senha:

```text
123456
```

Usuarios iniciais:

| Login | Perfis derivados |
| --- | --- |
| `ana` | `ROLE_USUARIO`, `ROLE_FUNCIONARIO` |
| `bruno` | `ROLE_ADMINISTRADOR`, `ROLE_ADMIN_MASTER` |
| `carla` | `ROLE_ADMINISTRADOR`, `ROLE_ADMIN_OPERACIONAL` |
| `diego` | `ROLE_USUARIO`, `ROLE_DISCENTE` |

Os perfis nao ficam em tabela propria. Eles sao derivados das tabelas `usuario`, `discente`, `funcionario` e `administrador`. Para administradores, o campo `setor` define o papel especifico:

- `Master`: cadastros-base, credenciais e infraestrutura.
- `Operacional`: reservas, acervo, emprestimos e pendencias.

## Regras de acesso

Rotas publicas:

- `/login`
- `/css/**`
- `/images/**`

Rotas autenticadas:

- `/`
- `/estrutura`: `ROLE_USUARIO` ou `ROLE_ADMIN_MASTER`

Rotas por papel:

- `/pessoas/**`: `ROLE_ADMIN_MASTER`
- `/usuarios/**`: `ROLE_ADMIN_MASTER`
- `/credenciais/**`: `ROLE_ADMIN_MASTER`
- `/funcionarios/**`: `ROLE_ADMIN_MASTER`
- `/discentes/**`: `ROLE_ADMIN_MASTER`
- `/administradores/**`: `ROLE_ADMIN_MASTER`
- `/infraestrutura/**`: `ROLE_ADMIN_MASTER`
- `/relatorios/**`: `ROLE_ADMIN_MASTER`
- `/relatorios/reservas`: `ROLE_ADMIN_OPERACIONAL` ou `ROLE_ADMIN_MASTER`
- `/admin/**`: `ROLE_ADMIN_MASTER`
- `/reservas`: `ROLE_FUNCIONARIO` ou `ROLE_ADMIN_OPERACIONAL`
- `/reservas/nova`: `ROLE_FUNCIONARIO` ou `ROLE_ADMIN_OPERACIONAL`
- `/reservas/salvar`: `ROLE_FUNCIONARIO` ou `ROLE_ADMIN_OPERACIONAL`
- `/reservas/editar`: `ROLE_ADMIN_OPERACIONAL`
- `/reservas/excluir`: `ROLE_ADMIN_OPERACIONAL`
- `/acervo/**`: `ROLE_USUARIO` ou `ROLE_ADMIN_OPERACIONAL`
- `/emprestimos/**`: `ROLE_USUARIO` ou `ROLE_ADMIN_OPERACIONAL`
- `/pendencias/**`: `ROLE_USUARIO` ou `ROLE_ADMIN_OPERACIONAL`

## Estrutura de pacotes

```text
src/main/java/br/ufscar/sistema_universidade/
  config/       Configuracoes do projeto, incluindo Spring Security
  controller/   Controllers MVC
  dto/          Objetos de transferencia para consultas especificas
  model/        Classes Java simples que representam o DER
  repository/   Acesso ao banco com JdbcTemplate e SQL explicito
  service/      Regras de negocio

src/main/resources/
  db/           Scripts SQL do banco
  static/css/   Estilos da interface
  templates/    Paginas Thymeleaf
```

## Scripts SQL

- `01_create_tables.sql`: criacao das tabelas do esquema relacional.
- `02_insert_data.sql`: dados iniciais e usuarios de teste.
- `03_indexes.sql`: indices para consultas frequentes.
- `04_triggers_functions_procedures.sql`: triggers, functions e procedures.

Regras de banco ja previstas:

- Validacao de capacidade de sala por categoria.
- Bloqueio de reservas conflitantes para mesma sala/data/horario.
- Bloqueio de emprestimo para usuario com pendencia ativa.
- Bloqueio de reservas pendentes ou aprovadas em datas passadas.

## Organizacao do grupo

Cada integrante deve trabalhar em uma branch propria:

```bash
git checkout -b feature/nome-da-funcionalidade
```

Sugestao de divisao:

- `feature/gestao-pessoas`
- `feature/infraestrutura`
- `feature/reservas`
- `feature/acervo`
- `feature/emprestimos-pendencias`

Fluxo recomendado:

1. Nao fazer commit direto na `main`.
2. Criar branch a partir da `main` atualizada.
3. Implementar a funcionalidade mantendo `controller/service/repository/model` separados.
4. Usar SQL explicito com `JdbcTemplate`.
5. Rodar testes/compilacao antes de enviar.
6. Abrir Pull Request para revisao do grupo.

Comando de verificacao:

```bash
./mvnw test
```
