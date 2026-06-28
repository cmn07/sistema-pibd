-- Script para insercao de dados iniciais.
-- Adicionar aqui comandos INSERT para dados de teste e carga inicial.

-- Teste para CRUD da infraestrutura (Rafaela)
-- 1. Inserindo Campi
INSERT INTO campus (nome, cidade) VALUES ('Campus São Carlos', 'São Carlos');
INSERT INTO campus (nome, cidade) VALUES ('Campus Sorocaba', 'Sorocaba');

-- 2. Inserindo Prédios vinculados ao Campus 1 (São Carlos)
INSERT INTO predio (nome, bloco, codigo_campus) VALUES ('Departamento de Computação', 'DC', 1);
INSERT INTO predio (nome, bloco, codigo_campus) VALUES ('Prédio das Aulas AT2', 'AT2', 1);

-- 3. Inserindo Salas válidas no prédio 1 (DC)
INSERT INTO sala (numero_sala, categoria, capacidade, codigo_predio) VALUES ('Sala 20', 'sala_aula', 40, 1);
INSERT INTO sala (numero_sala, categoria, capacidade, codigo_predio) VALUES ('Auditório Principal', 'auditorio', 120, 1);
-- Dados de teste para autenticacao e demonstracao de perfis acumulaveis.
-- Senha de todos os usuarios de teste: 123456
INSERT INTO pessoa (nome, cpf, email, telefone) VALUES ('Ana Funcionaria', '11111111111', 'ana@ufscar.br', '1111-1111');
INSERT INTO pessoa (nome, cpf, email, telefone) VALUES ('Bruno Admin Master', '22222222222', 'bruno@ufscar.br', '2222-2222');
INSERT INTO pessoa (nome, cpf, email, telefone) VALUES ('Carla Admin Operacional', '33333333333', 'carla@ufscar.br', '3333-3333');
INSERT INTO pessoa (nome, cpf, email, telefone) VALUES ('Diego Discente', '44444444444', 'diego@ufscar.br', '4444-4444');

INSERT INTO usuario (id_pessoa) VALUES (1), (4);

INSERT INTO funcionario (id_pessoa, id_func, tipo_vinculo) VALUES (1, 'FUNC001', 'servidor');

INSERT INTO discente (id_pessoa, ra, curso) VALUES (4, '800000', 'Ciencia da Computacao');

INSERT INTO administrador (id_pessoa, id_admin, setor) VALUES (2, 'ADM001', 'Master');
INSERT INTO administrador (id_pessoa, id_admin, setor) VALUES (3, 'ADM002', 'Operacional');

INSERT INTO credencial_acesso (id_pessoa, login, senha_hash) VALUES
    (1, 'ana', '$2b$12$DeCLLy2ayuvoAz5XzM.UW.vAJZl.9IEDzxi6nGnHF53KVQMR1171S'),
    (2, 'bruno', '$2b$12$DeCLLy2ayuvoAz5XzM.UW.vAJZl.9IEDzxi6nGnHF53KVQMR1171S'),
    (3, 'carla', '$2b$12$DeCLLy2ayuvoAz5XzM.UW.vAJZl.9IEDzxi6nGnHF53KVQMR1171S'),
    (4, 'diego', '$2b$12$DeCLLy2ayuvoAz5XzM.UW.vAJZl.9IEDzxi6nGnHF53KVQMR1171S');

INSERT INTO reserva (data, tipo_reserva, status_reserva, objetivo, horario_inicio, horario_fim, codigo_usuario, codigo_sala, codigo_administrador)
VALUES (CURRENT_DATE + 1, 'reuniao', 'pendente', 'Reuniao de planejamento', '10:00', '11:00', 1, 1, NULL);

-- Dados de teste para consulta e gestao do acervo
INSERT INTO material_acervo (tipo_material, titulo, quantidade_copias, ano_publicacao, idioma, categoria, editora)
VALUES
    ('LIVRO', 'Sistemas de Banco de Dados', 4, 2019, 'Portugues', 'Banco de Dados', 'Pearson'),
    ('LIVRO', 'Engenharia de Software Moderna', 3, 2020, 'Portugues', 'Engenharia de Software', 'Casa do Codigo'),
    ('PERIODICO', 'Revista Brasileira de Computacao Aplicada', 2, 2023, 'Portugues', 'Computacao', 'SBC'),
    ('OUTROS', 'Manual de Normas Academicas', 1, 2022, 'Portugues', 'Institucional', 'UFSCar');

INSERT INTO emprestimo (data_emprestimo, status_emprestimo, data_devolucao_prevista, codigo_material_acervo, codigo_usuario, codigo_administrador)
VALUES (CURRENT_DATE, 'ativo', CURRENT_DATE + 14, 1, 4, NULL);
