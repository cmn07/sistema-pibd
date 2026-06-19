-- Script para criacao das tabelas do banco universidade.
-- Adicionar aqui os comandos CREATE TABLE conforme o modelo fisico do projeto.
-- =========================================================
-- CRIAÇÃO DAS TABELAS
-- =========================================================

-- ---------------------------------------------------------
-- PESSOA E ESPECIALIZAÇÕES
-- ---------------------------------------------------------

CREATE TABLE pessoa (
                        id_pessoa SERIAL PRIMARY KEY,
                        nome VARCHAR(150) NOT NULL,
                        cpf VARCHAR(11) NOT NULL UNIQUE,
                        email VARCHAR(100),
                        telefone VARCHAR(20)
);

CREATE TABLE usuario (
                         id_pessoa INT PRIMARY KEY,

                         CONSTRAINT fk_usuario_pessoa
                             FOREIGN KEY (id_pessoa)
                                 REFERENCES pessoa(id_pessoa)
);

CREATE TABLE administrador (
                               id_pessoa INT PRIMARY KEY,
                               id_admin VARCHAR(30) NOT NULL UNIQUE,
                               setor VARCHAR(100),

                               CONSTRAINT fk_administrador_pessoa
                                   FOREIGN KEY (id_pessoa)
                                       REFERENCES pessoa(id_pessoa)
);

CREATE TABLE funcionario (
                             id_pessoa INT PRIMARY KEY,
                             id_func VARCHAR(30) NOT NULL UNIQUE,
                             tipo_vinculo VARCHAR(50) NOT NULL,

                             CONSTRAINT fk_funcionario_usuario
                                 FOREIGN KEY (id_pessoa)
                                     REFERENCES usuario(id_pessoa),

                             CONSTRAINT chk_funcionario_tipo_vinculo
                                 CHECK (tipo_vinculo IN (
                                                         'professor',
                                                         'servidor',
                                                         'tecnico_administrativo',
                                                         'externo'
                                     ))
);

CREATE TABLE discente (
                          id_pessoa INT PRIMARY KEY,
                          ra VARCHAR(20) NOT NULL UNIQUE,
                          curso VARCHAR(100) NOT NULL,

                          CONSTRAINT fk_discente_usuario
                              FOREIGN KEY (id_pessoa)
                                  REFERENCES usuario(id_pessoa)
);

-- ---------------------------------------------------------
-- INFRAESTRUTURA
-- ---------------------------------------------------------

CREATE TABLE campus (
                        codigo SERIAL PRIMARY KEY,
                        nome VARCHAR(100) NOT NULL,
                        cidade VARCHAR(100) NOT NULL,

                        CONSTRAINT uq_campus_nome_cidade
                            UNIQUE (nome, cidade)
);

CREATE TABLE predio (
                        codigo SERIAL PRIMARY KEY,
                        nome VARCHAR(100) NOT NULL,
                        bloco VARCHAR(20),
                        codigo_campus INT NOT NULL,

                        CONSTRAINT fk_predio_campus
                            FOREIGN KEY (codigo_campus)
                                REFERENCES campus(codigo),

                        CONSTRAINT uq_predio_nome_bloco_campus
                            UNIQUE (nome, bloco, codigo_campus)
);

CREATE TABLE sala (
                      codigo SERIAL PRIMARY KEY,
                      numero_sala VARCHAR(20) NOT NULL,
                      categoria VARCHAR(50) NOT NULL,
                      capacidade INT NOT NULL,
                      codigo_predio INT NOT NULL,

                      CONSTRAINT fk_sala_predio
                          FOREIGN KEY (codigo_predio)
                              REFERENCES predio(codigo),

                      CONSTRAINT chk_sala_capacidade
                          CHECK (capacidade > 0),

                      CONSTRAINT chk_sala_categoria
                          CHECK (categoria IN (
                                               'sala_aula',
                                               'laboratorio',
                                               'sala_informatica',
                                               'auditorio',
                                               'sala_reuniao',
                                               'outro'
                              )),

                      CONSTRAINT uq_sala_numero_predio
                          UNIQUE (numero_sala, codigo_predio)
);

CREATE TABLE laboratorio (
                             codigo_sala INT PRIMARY KEY,
                             departamento_setor VARCHAR(100),
                             quantidade_equipamentos_informatica INT DEFAULT 0,

                             CONSTRAINT fk_laboratorio_sala
                                 FOREIGN KEY (codigo_sala)
                                     REFERENCES sala(codigo),

                             CONSTRAINT chk_laboratorio_qtd_equipamentos
                                 CHECK (quantidade_equipamentos_informatica >= 0)
);

-- ---------------------------------------------------------
-- BIBLIOTECA: MATERIAL, LIVRO, PERIÓDICO, OUTROS E AUTOR
-- ---------------------------------------------------------

CREATE TABLE material_acervo (
                                 codigo SERIAL PRIMARY KEY,
                                 tipo_material VARCHAR(20) NOT NULL,
                                 titulo VARCHAR(200) NOT NULL,
                                 quantidade_copias INT NOT NULL DEFAULT 1,
                                 ano_publicacao SMALLINT,
                                 idioma VARCHAR(50),
                                 categoria VARCHAR(80),
                                 editora VARCHAR(100),

                                 CONSTRAINT chk_material_tipo
                                     CHECK (tipo_material IN ('LIVRO', 'PERIODICO', 'OUTROS')),

                                 CONSTRAINT chk_material_quantidade_copias
                                     CHECK (quantidade_copias >= 0),

                                 CONSTRAINT chk_material_ano_publicacao
                                     CHECK (
                                         ano_publicacao IS NULL
                                             OR (ano_publicacao > 1000 AND ano_publicacao <= 2100)
                                         )
);

CREATE TABLE livro (
                       codigo_material INT PRIMARY KEY,
                       edicao VARCHAR(30),
                       isbn VARCHAR(20) UNIQUE,
                       tradutor VARCHAR(100),

                       CONSTRAINT fk_livro_material
                           FOREIGN KEY (codigo_material)
                               REFERENCES material_acervo(codigo)
);

CREATE TABLE periodico (
                           codigo_material INT PRIMARY KEY,
                           volume VARCHAR(30),
                           issn VARCHAR(20) UNIQUE,
                           numero VARCHAR(30),

                           CONSTRAINT fk_periodico_material
                               FOREIGN KEY (codigo_material)
                                   REFERENCES material_acervo(codigo)
);

CREATE TABLE outros (
                        codigo_material INT PRIMARY KEY,
                        descricao TEXT,

                        CONSTRAINT fk_outros_material
                            FOREIGN KEY (codigo_material)
                                REFERENCES material_acervo(codigo)
);

CREATE TABLE autor (
                       id SERIAL PRIMARY KEY,
                       nome VARCHAR(150) NOT NULL
);

CREATE TABLE autor_material_acervo (
                                       id_autor INT NOT NULL,
                                       codigo_material INT NOT NULL,

                                       CONSTRAINT pk_autor_material_acervo
                                           PRIMARY KEY (id_autor, codigo_material),

                                       CONSTRAINT fk_autor_material_autor
                                           FOREIGN KEY (id_autor)
                                               REFERENCES autor(id),

                                       CONSTRAINT fk_autor_material_material
                                           FOREIGN KEY (codigo_material)
                                               REFERENCES material_acervo(codigo)
);

-- ---------------------------------------------------------
-- RESERVA
-- ---------------------------------------------------------

CREATE TABLE reserva (
                         id_reserva SERIAL PRIMARY KEY,
                         data DATE NOT NULL,
                         tipo_reserva VARCHAR(50) NOT NULL,
                         status_reserva VARCHAR(30) NOT NULL,
                         objetivo TEXT,
                         horario_inicio TIME NOT NULL,
                         horario_fim TIME NOT NULL,
                         codigo_usuario INT NOT NULL,
                         codigo_sala INT NOT NULL,
                         codigo_administrador INT,

                         CONSTRAINT fk_reserva_usuario
                             FOREIGN KEY (codigo_usuario)
                                 REFERENCES usuario(id_pessoa),

                         CONSTRAINT fk_reserva_sala
                             FOREIGN KEY (codigo_sala)
                                 REFERENCES sala(codigo),

                         CONSTRAINT fk_reserva_administrador
                             FOREIGN KEY (codigo_administrador)
                                 REFERENCES administrador(id_pessoa),

                         CONSTRAINT chk_reserva_horario
                             CHECK (horario_fim > horario_inicio),

                         CONSTRAINT chk_reserva_tipo
                             CHECK (tipo_reserva IN (
                                                     'aula',
                                                     'reuniao',
                                                     'evento',
                                                     'palestra',
                                                     'minicurso',
                                                     'extensao',
                                                     'manutencao',
                                                     'outro'
                                 )),

                         CONSTRAINT chk_reserva_status
                             CHECK (status_reserva IN (
                                                       'pendente',
                                                       'aprovada',
                                                       'cancelada',
                                                       'rejeitada',
                                                       'finalizada'
                                 ))
);

-- ---------------------------------------------------------
-- EMPRÉSTIMO
-- ---------------------------------------------------------

CREATE TABLE emprestimo (
                            id_emprestimo SERIAL PRIMARY KEY,
                            data_emprestimo DATE NOT NULL DEFAULT CURRENT_DATE,
                            status_emprestimo VARCHAR(30) NOT NULL,
                            data_devolucao_real DATE,
                            data_devolucao_prevista DATE NOT NULL,
                            codigo_material_acervo INT NOT NULL,
                            codigo_usuario INT NOT NULL,
                            codigo_administrador INT,

                            CONSTRAINT fk_emprestimo_material
                                FOREIGN KEY (codigo_material_acervo)
                                    REFERENCES material_acervo(codigo),

                            CONSTRAINT fk_emprestimo_usuario
                                FOREIGN KEY (codigo_usuario)
                                    REFERENCES usuario(id_pessoa),

                            CONSTRAINT fk_emprestimo_administrador
                                FOREIGN KEY (codigo_administrador)
                                    REFERENCES administrador(id_pessoa),

                            CONSTRAINT chk_emprestimo_status
                                CHECK (status_emprestimo IN (
                                                             'ativo',
                                                             'devolvido',
                                                             'atrasado',
                                                             'perdido',
                                                             'cancelado'
                                    )),

                            CONSTRAINT chk_emprestimo_data_prevista
                                CHECK (data_devolucao_prevista >= data_emprestimo),

                            CONSTRAINT chk_emprestimo_data_real
                                CHECK (
                                    data_devolucao_real IS NULL
                                        OR data_devolucao_real >= data_emprestimo
                                    )
);

-- ---------------------------------------------------------
-- PENDÊNCIA
-- ---------------------------------------------------------

CREATE TABLE pendencia (
                           codigo SERIAL PRIMARY KEY,
                           motivo TEXT,
                           status_pendencia VARCHAR(30) NOT NULL,
                           data_inicio_pendencia DATE NOT NULL DEFAULT CURRENT_DATE,
                           data_fim_pendencia DATE,
                           codigo_emprestimo INT NOT NULL UNIQUE,
                           codigo_administrador INT,

                           CONSTRAINT fk_pendencia_emprestimo
                               FOREIGN KEY (codigo_emprestimo)
                                   REFERENCES emprestimo(id_emprestimo),

                           CONSTRAINT fk_pendencia_administrador
                               FOREIGN KEY (codigo_administrador)
                                   REFERENCES administrador(id_pessoa),

                           CONSTRAINT chk_pendencia_status
                               CHECK (status_pendencia IN (
                                                           'ativa',
                                                           'cumprida',
                                                           'cancelada'
                                   )),

                           CONSTRAINT chk_pendencia_datas
                               CHECK (
                                   data_fim_pendencia IS NULL
                                       OR data_fim_pendencia >= data_inicio_pendencia
                                   )
);

