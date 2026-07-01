-- Massa de dados para demonstracao dos CRUDs, consultas e filtros.
-- O script e idempotente: pode ser executado novamente sem duplicar os exemplos.

BEGIN;

-- =========================================================
-- INFRAESTRUTURA
-- =========================================================

INSERT INTO campus (nome, cidade) VALUES
    ('Campus Araras', 'Araras'),
    ('Campus Lagoa do Sino', 'Buri')
ON CONFLICT (nome, cidade) DO NOTHING;

INSERT INTO predio (nome, bloco, codigo_campus)
SELECT dados.nome, dados.bloco, c.codigo
FROM (VALUES
    ('Biblioteca Comunitaria', 'BCo', 'Campus São Carlos'),
    ('Departamento de Engenharia', 'DE', 'Campus São Carlos'),
    ('Centro de Ciencias Humanas', 'CCHB', 'Campus Sorocaba'),
    ('Laboratorios de Biologia', 'LB', 'Campus Sorocaba'),
    ('Centro de Ciencias Agrarias', 'CCA', 'Campus Araras'),
    ('Predio de Aulas', 'PA', 'Campus Lagoa do Sino')
) AS dados(nome, bloco, campus_nome)
JOIN campus c ON c.nome = dados.campus_nome
ON CONFLICT (nome, bloco, codigo_campus) DO NOTHING;

-- O Departamento de Computacao fica com mais de 10 salas e apenas um laboratorio,
-- produzindo um resultado visivel no relatorio de infraestrutura critica.
INSERT INTO sala (numero_sala, categoria, capacidade, codigo_predio)
SELECT dados.numero, dados.categoria, dados.capacidade, p.codigo
FROM (VALUES
    ('Sala 01', 'sala_aula', 35, 'Departamento de Computação'),
    ('Sala 02', 'sala_aula', 40, 'Departamento de Computação'),
    ('Sala 03', 'sala_aula', 45, 'Departamento de Computação'),
    ('Sala 04', 'sala_aula', 30, 'Departamento de Computação'),
    ('Sala 05', 'sala_aula', 50, 'Departamento de Computação'),
    ('Lab Redes', 'laboratorio', 25, 'Departamento de Computação'),
    ('Lab Informatica 1', 'sala_informatica', 40, 'Departamento de Computação'),
    ('Sala Reuniao 1', 'sala_reuniao', 12, 'Departamento de Computação'),
    ('Sala Reuniao 2', 'sala_reuniao', 20, 'Departamento de Computação'),
    ('Auditorio DC', 'auditorio', 90, 'Departamento de Computação'),
    ('Espaco Maker', 'outro', 30, 'Departamento de Computação'),
    ('AT2-101', 'sala_aula', 60, 'Prédio das Aulas AT2'),
    ('AT2-102', 'sala_aula', 60, 'Prédio das Aulas AT2'),
    ('AT2-Auditorio', 'auditorio', 180, 'Prédio das Aulas AT2'),
    ('BCo-Reuniao', 'sala_reuniao', 16, 'Biblioteca Comunitaria'),
    ('BCo-Multimidia', 'outro', 24, 'Biblioteca Comunitaria'),
    ('DE-101', 'sala_aula', 45, 'Departamento de Engenharia'),
    ('DE-Lab 1', 'laboratorio', 28, 'Departamento de Engenharia'),
    ('CCHB-01', 'sala_aula', 50, 'Centro de Ciencias Humanas'),
    ('CCHB-Auditorio', 'auditorio', 100, 'Centro de Ciencias Humanas'),
    ('LB-01', 'laboratorio', 24, 'Laboratorios de Biologia'),
    ('CCA-01', 'sala_aula', 40, 'Centro de Ciencias Agrarias'),
    ('PA-01', 'sala_aula', 45, 'Predio de Aulas')
) AS dados(numero, categoria, capacidade, predio_nome)
JOIN predio p ON p.nome = dados.predio_nome
ON CONFLICT (numero_sala, codigo_predio) DO NOTHING;

INSERT INTO laboratorio (codigo_sala, departamento_setor, quantidade_equipamentos_informatica)
SELECT s.codigo, dados.setor, dados.equipamentos
FROM (VALUES
    ('Lab Redes', 'Departamento de Computacao', 25),
    ('DE-Lab 1', 'Engenharia', 18),
    ('LB-01', 'Biologia', 12)
) AS dados(numero, setor, equipamentos)
JOIN sala s ON s.numero_sala = dados.numero
ON CONFLICT (codigo_sala) DO NOTHING;

-- =========================================================
-- PESSOAS E PERFIS
-- =========================================================

INSERT INTO pessoa (nome, cpf, email, telefone) VALUES
    ('Eduardo Professor', '55555555555', 'eduardo@ufscar.br', '5555-5555'),
    ('Fernanda Tecnica', '66666666666', 'fernanda@ufscar.br', '6666-6666'),
    ('Gabriel Externo', '77777777777', 'gabriel@exemplo.com', '7777-7777'),
    ('Helena Discente', '88888888888', 'helena@ufscar.br', '8888-8888'),
    ('Igor Discente', '99999999999', 'igor@ufscar.br', '9999-9999'),
    ('Juliana Discente', '10101010101', 'juliana@ufscar.br', '1010-1010'),
    ('Lucas Servidor', '12121212121', 'lucas@ufscar.br', '1212-1212'),
    ('Marina Professora', '13131313131', 'marina@ufscar.br', '1313-1313')
ON CONFLICT (cpf) DO NOTHING;

INSERT INTO usuario (id_pessoa)
SELECT id_pessoa FROM pessoa
WHERE cpf IN (
    '55555555555', '66666666666', '77777777777', '88888888888',
    '99999999999', '10101010101', '12121212121', '13131313131'
)
ON CONFLICT (id_pessoa) DO NOTHING;

INSERT INTO funcionario (id_pessoa, id_func, tipo_vinculo)
SELECT p.id_pessoa, dados.id_func, dados.tipo
FROM (VALUES
    ('55555555555', 'FUNC-DEMO-01', 'professor'),
    ('66666666666', 'FUNC-DEMO-02', 'tecnico_administrativo'),
    ('77777777777', 'FUNC-DEMO-03', 'externo'),
    ('12121212121', 'FUNC-DEMO-04', 'servidor'),
    ('13131313131', 'FUNC-DEMO-05', 'professor')
) AS dados(cpf, id_func, tipo)
JOIN pessoa p ON p.cpf = dados.cpf
ON CONFLICT (id_pessoa) DO NOTHING;

INSERT INTO discente (id_pessoa, ra, curso)
SELECT p.id_pessoa, dados.ra, dados.curso
FROM (VALUES
    ('88888888888', 'DEMO800001', 'Ciencia da Computacao'),
    ('99999999999', 'DEMO800002', 'Engenharia de Computacao'),
    ('10101010101', 'DEMO800003', 'Biblioteconomia')
) AS dados(cpf, ra, curso)
JOIN pessoa p ON p.cpf = dados.cpf
ON CONFLICT (id_pessoa) DO NOTHING;

-- Todas as credenciais de demonstracao usam a senha 123456.
INSERT INTO credencial_acesso (id_pessoa, login, senha_hash)
SELECT p.id_pessoa, dados.login,
       '$2b$12$DeCLLy2ayuvoAz5XzM.UW.vAJZl.9IEDzxi6nGnHF53KVQMR1171S'
FROM (VALUES
    ('55555555555', 'eduardo'),
    ('66666666666', 'fernanda'),
    ('77777777777', 'gabriel'),
    ('88888888888', 'helena'),
    ('99999999999', 'igor'),
    ('10101010101', 'juliana'),
    ('12121212121', 'lucas'),
    ('13131313131', 'marina')
) AS dados(cpf, login)
JOIN pessoa p ON p.cpf = dados.cpf
ON CONFLICT (id_pessoa) DO NOTHING;

-- =========================================================
-- ACERVO: LIVROS, PERIODICOS, OUTROS E AUTORES
-- =========================================================

INSERT INTO material_acervo
    (tipo_material, titulo, quantidade_copias, ano_publicacao, idioma, categoria, editora)
SELECT * FROM (VALUES
    ('LIVRO', 'Introducao a Banco de Dados', 5, 2021::SMALLINT, 'Portugues', 'Banco de Dados', 'Pearson'),
    ('LIVRO', 'SQL para Iniciantes', 4, 2022::SMALLINT, 'Portugues', 'Banco de Dados', 'Novatec'),
    ('LIVRO', 'Algoritmos e Estruturas de Dados', 6, 2018::SMALLINT, 'Portugues', 'Computacao', 'LTC'),
    ('LIVRO', 'Clean Code', 3, 2008::SMALLINT, 'Ingles', 'Engenharia de Software', 'Prentice Hall'),
    ('LIVRO', 'Redes de Computadores', 4, 2020::SMALLINT, 'Portugues', 'Redes', 'Pearson'),
    ('LIVRO', 'Inteligencia Artificial Moderna', 2, 2023::SMALLINT, 'Portugues', 'Inteligencia Artificial', 'Campus'),
    ('LIVRO', 'Calculo Volume 1', 7, 2017::SMALLINT, 'Portugues', 'Matematica', 'Cengage'),
    ('PERIODICO', 'Revista de Sistemas de Informacao', 3, 2024::SMALLINT, 'Portugues', 'Computacao', 'SBC'),
    ('PERIODICO', 'Journal of Database Studies', 2, 2025::SMALLINT, 'Ingles', 'Banco de Dados', 'ACM'),
    ('PERIODICO', 'Cadernos de Biblioteconomia', 4, 2023::SMALLINT, 'Portugues', 'Biblioteconomia', 'UFSCar'),
    ('PERIODICO', 'Tecnologia em Foco', 5, 2022::SMALLINT, 'Portugues', 'Tecnologia', 'SBC'),
    ('OUTROS', 'TCC - Sistema de Biblioteca', 1, 2024::SMALLINT, 'Portugues', 'Trabalho Academico', 'UFSCar'),
    ('OUTROS', 'Mapa dos Campi UFSCar', 10, 2026::SMALLINT, 'Portugues', 'Institucional', 'UFSCar'),
    ('OUTROS', 'Guia de Normalizacao ABNT', 8, 2025::SMALLINT, 'Portugues', 'Normas', 'UFSCar'),
    ('OUTROS', 'DVD Historia da Computacao', 2, 2015::SMALLINT, 'Portugues', 'Multimidia', 'Documenta')
) AS dados(tipo_material, titulo, quantidade_copias, ano_publicacao, idioma, categoria, editora)
WHERE NOT EXISTS (
    SELECT 1 FROM material_acervo m WHERE m.titulo = dados.titulo
);

INSERT INTO livro (codigo_material, edicao, isbn, tradutor)
SELECT m.codigo, dados.edicao, dados.isbn, dados.tradutor
FROM (VALUES
    ('Introducao a Banco de Dados', '2a', '9780000000001', NULL),
    ('SQL para Iniciantes', '1a', '9780000000002', NULL),
    ('Algoritmos e Estruturas de Dados', '3a', '9780000000003', NULL),
    ('Clean Code', '1a', '9780000000004', 'Traducao demonstrativa'),
    ('Redes de Computadores', '6a', '9780000000005', NULL),
    ('Inteligencia Artificial Moderna', '1a', '9780000000006', NULL),
    ('Calculo Volume 1', '8a', '9780000000007', NULL)
) AS dados(titulo, edicao, isbn, tradutor)
JOIN material_acervo m ON m.titulo = dados.titulo
ON CONFLICT (codigo_material) DO NOTHING;

INSERT INTO periodico (codigo_material, volume, issn, numero)
SELECT m.codigo, dados.volume, dados.issn, dados.numero
FROM (VALUES
    ('Revista de Sistemas de Informacao', '18', '0000-0001', '2'),
    ('Journal of Database Studies', '12', '0000-0002', '1'),
    ('Cadernos de Biblioteconomia', '9', '0000-0003', '4'),
    ('Tecnologia em Foco', '7', '0000-0004', '3')
) AS dados(titulo, volume, issn, numero)
JOIN material_acervo m ON m.titulo = dados.titulo
ON CONFLICT (codigo_material) DO NOTHING;

INSERT INTO outros (codigo_material, descricao)
SELECT m.codigo, dados.descricao
FROM (VALUES
    ('TCC - Sistema de Biblioteca', 'Trabalho academico para consulta local.'),
    ('Mapa dos Campi UFSCar', 'Mapa impresso com os principais predios.'),
    ('Guia de Normalizacao ABNT', 'Guia rapido para trabalhos academicos.'),
    ('DVD Historia da Computacao', 'Documentario em midia fisica.')
) AS dados(titulo, descricao)
JOIN material_acervo m ON m.titulo = dados.titulo
ON CONFLICT (codigo_material) DO NOTHING;

INSERT INTO autor (nome)
SELECT nome FROM (VALUES
    ('Ana Silva'), ('Carlos Souza'), ('Mariana Lima'), ('Robert Martin'),
    ('Joao Santos'), ('Equipe UFSCar'), ('Sociedade Brasileira de Computacao')
) AS dados(nome)
WHERE NOT EXISTS (SELECT 1 FROM autor a WHERE a.nome = dados.nome);

INSERT INTO autor_material_acervo (id_autor, codigo_material)
SELECT a.id, m.codigo
FROM (VALUES
    ('Ana Silva', 'Introducao a Banco de Dados'),
    ('Carlos Souza', 'SQL para Iniciantes'),
    ('Mariana Lima', 'Algoritmos e Estruturas de Dados'),
    ('Robert Martin', 'Clean Code'),
    ('Joao Santos', 'Redes de Computadores'),
    ('Equipe UFSCar', 'Guia de Normalizacao ABNT'),
    ('Sociedade Brasileira de Computacao', 'Revista de Sistemas de Informacao')
) AS dados(autor_nome, material_titulo)
JOIN autor a ON a.nome = dados.autor_nome
JOIN material_acervo m ON m.titulo = dados.material_titulo
ON CONFLICT (id_autor, codigo_material) DO NOTHING;

-- =========================================================
-- EMPRESTIMOS EM PERIODOS E STATUS VARIADOS
-- =========================================================

INSERT INTO emprestimo
    (data_emprestimo, status_emprestimo, data_devolucao_real,
     data_devolucao_prevista, codigo_material_acervo, codigo_usuario, codigo_administrador)
SELECT
    CURRENT_DATE + dados.dias_emprestimo,
    dados.status,
    CASE WHEN dados.dias_devolucao IS NULL THEN NULL
         ELSE CURRENT_DATE + dados.dias_devolucao END,
    CURRENT_DATE + dados.dias_previstos,
    m.codigo,
    p.id_pessoa,
    CASE WHEN dados.com_admin THEN admin.id_pessoa ELSE NULL END
FROM (VALUES
    ('Introducao a Banco de Dados', '88888888888', -120, -106, -110, 'devolvido', TRUE),
    ('SQL para Iniciantes', '88888888888', -80, -66, -70, 'devolvido', TRUE),
    ('Clean Code', '88888888888', -10, 4, NULL, 'ativo', FALSE),
    ('Algoritmos e Estruturas de Dados', '99999999999', -60, -46, -45, 'devolvido', TRUE),
    ('Redes de Computadores', '99999999999', -30, -16, NULL, 'atrasado', TRUE),
    ('Inteligencia Artificial Moderna', '10101010101', -15, -1, NULL, 'atrasado', TRUE),
    ('Calculo Volume 1', '10101010101', -5, 9, NULL, 'ativo', FALSE),
    ('Revista de Sistemas de Informacao', '55555555555', -200, -186, -185, 'devolvido', TRUE),
    ('Journal of Database Studies', '55555555555', -20, -6, NULL, 'perdido', TRUE),
    ('Cadernos de Biblioteconomia', '66666666666', -7, 7, NULL, 'ativo', FALSE),
    ('Tecnologia em Foco', '66666666666', -90, -76, -89, 'cancelado', TRUE),
    ('TCC - Sistema de Biblioteca', '77777777777', -45, -31, -30, 'devolvido', TRUE),
    ('Mapa dos Campi UFSCar', '12121212121', -2, 12, NULL, 'ativo', FALSE),
    ('Guia de Normalizacao ABNT', '13131313131', -150, -136, -140, 'devolvido', TRUE),
    ('DVD Historia da Computacao', '13131313131', -40, -26, -20, 'devolvido', TRUE)
) AS dados(titulo, cpf, dias_emprestimo, dias_previstos, dias_devolucao, status, com_admin)
JOIN material_acervo m ON m.titulo = dados.titulo
JOIN pessoa p ON p.cpf = dados.cpf
LEFT JOIN administrador admin ON admin.setor = 'Operacional'
WHERE NOT EXISTS (
    SELECT 1
    FROM emprestimo e
    WHERE e.codigo_material_acervo = m.codigo
      AND e.codigo_usuario = p.id_pessoa
      AND e.data_emprestimo = CURRENT_DATE + dados.dias_emprestimo
);

-- Pendencias ativa, cumprida e cancelada para demonstrar os filtros.
INSERT INTO pendencia
    (motivo, status_pendencia, data_inicio_pendencia, data_fim_pendencia,
     codigo_emprestimo, codigo_administrador)
SELECT
    dados.motivo,
    dados.status,
    CURRENT_DATE + dados.dias_inicio,
    CASE WHEN dados.dias_fim IS NULL THEN NULL ELSE CURRENT_DATE + dados.dias_fim END,
    e.id_emprestimo,
    admin.id_pessoa
FROM (VALUES
    ('Redes de Computadores', 'Atraso na devolucao', 'ativa', -15, NULL),
    ('Inteligencia Artificial Moderna', 'Material com devolucao vencida', 'ativa', -1, NULL),
    ('Journal of Database Studies', 'Material declarado perdido', 'cumprida', -6, 0),
    ('Tecnologia em Foco', 'Emprestimo cancelado pelo usuario', 'cancelada', -89, -88)
) AS dados(titulo, motivo, status, dias_inicio, dias_fim)
JOIN material_acervo m ON m.titulo = dados.titulo
JOIN emprestimo e ON e.codigo_material_acervo = m.codigo
LEFT JOIN administrador admin ON admin.setor = 'Operacional'
ON CONFLICT (codigo_emprestimo) DO NOTHING;

-- =========================================================
-- RESERVAS: TODOS OS TIPOS E STATUS
-- =========================================================

INSERT INTO reserva
    (data, tipo_reserva, status_reserva, objetivo, horario_inicio, horario_fim,
     codigo_usuario, codigo_sala, codigo_administrador)
SELECT
    CURRENT_DATE + dados.dias,
    dados.tipo,
    dados.status,
    dados.objetivo,
    dados.inicio::TIME,
    dados.fim::TIME,
    pessoa.id_pessoa,
    sala.codigo,
    CASE WHEN dados.com_admin THEN admin.id_pessoa ELSE NULL END
FROM (VALUES
    (2, 'aula', 'pendente', '[DEMO] Aula de Banco de Dados', '08:00', '10:00', 'Sala 01', '55555555555', FALSE),
    (3, 'reuniao', 'aprovada', '[DEMO] Reuniao do Departamento', '10:00', '11:30', 'Sala Reuniao 1', '12121212121', TRUE),
    (5, 'evento', 'aprovada', '[DEMO] Semana da Computacao', '14:00', '18:00', 'Auditorio DC', '55555555555', TRUE),
    (7, 'palestra', 'pendente', '[DEMO] Palestra sobre Inteligencia Artificial', '19:00', '21:00', 'AT2-Auditorio', '13131313131', FALSE),
    (10, 'minicurso', 'aprovada', '[DEMO] Minicurso de SQL', '08:30', '12:00', 'Lab Informatica 1', '66666666666', TRUE),
    (12, 'extensao', 'pendente', '[DEMO] Projeto de Inclusao Digital', '13:00', '16:00', 'BCo-Multimidia', '66666666666', FALSE),
    (14, 'manutencao', 'aprovada', '[DEMO] Manutencao dos Computadores', '07:00', '09:00', 'DE-Lab 1', '12121212121', TRUE),
    (20, 'outro', 'pendente', '[DEMO] Encontro de Egressos', '17:00', '20:00', 'CCHB-Auditorio', '77777777777', FALSE),
    (-2, 'aula', 'finalizada', '[DEMO] Aula Finalizada', '08:00', '10:00', 'Sala 02', '55555555555', TRUE),
    (-4, 'reuniao', 'cancelada', '[DEMO] Reuniao Cancelada', '10:00', '11:00', 'Sala Reuniao 2', '12121212121', TRUE),
    (-6, 'evento', 'rejeitada', '[DEMO] Evento Rejeitado', '14:00', '17:00', 'Auditorio DC', '77777777777', TRUE),
    (-10, 'palestra', 'finalizada', '[DEMO] Palestra Finalizada', '19:00', '21:00', 'AT2-Auditorio', '13131313131', TRUE),
    (-15, 'minicurso', 'finalizada', '[DEMO] Minicurso Finalizado', '08:30', '12:00', 'Lab Informatica 1', '66666666666', TRUE),
    (-20, 'extensao', 'cancelada', '[DEMO] Extensao Cancelada', '13:00', '16:00', 'BCo-Multimidia', '66666666666', TRUE),
    (-25, 'manutencao', 'finalizada', '[DEMO] Manutencao Concluida', '07:00', '09:00', 'DE-Lab 1', '12121212121', TRUE),
    (-30, 'outro', 'rejeitada', '[DEMO] Solicitacao Rejeitada', '17:00', '20:00', 'CCHB-Auditorio', '77777777777', TRUE)
) AS dados(dias, tipo, status, objetivo, inicio, fim, numero_sala, cpf, com_admin)
JOIN pessoa ON pessoa.cpf = dados.cpf
JOIN sala ON sala.numero_sala = dados.numero_sala
LEFT JOIN administrador admin ON admin.setor = 'Operacional'
WHERE NOT EXISTS (
    SELECT 1 FROM reserva r WHERE r.objetivo = dados.objetivo
);

COMMIT;

-- Resumo exibido ao final quando executado pelo psql.
SELECT 'campus' AS tabela, COUNT(*) AS total FROM campus
UNION ALL SELECT 'predio', COUNT(*) FROM predio
UNION ALL SELECT 'sala', COUNT(*) FROM sala
UNION ALL SELECT 'pessoa', COUNT(*) FROM pessoa
UNION ALL SELECT 'material_acervo', COUNT(*) FROM material_acervo
UNION ALL SELECT 'emprestimo', COUNT(*) FROM emprestimo
UNION ALL SELECT 'pendencia', COUNT(*) FROM pendencia
UNION ALL SELECT 'reserva', COUNT(*) FROM reserva
ORDER BY tabela;
