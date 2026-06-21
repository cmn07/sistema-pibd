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