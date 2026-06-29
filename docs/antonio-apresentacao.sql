-- Consulta individual de Antonio Adolfo Geraldino Bernardo.
-- Relatorio de reservas por periodo, envolvendo seis entidades e cinco relacionamentos:
-- pessoa -> usuario -> reserva -> sala -> predio -> campus.

SELECT
    r.id_reserva,
    r.data,
    r.horario_inicio,
    r.horario_fim,
    pessoa.nome AS solicitante,
    r.tipo_reserva,
    r.status_reserva,
    sala.numero_sala,
    predio.nome AS predio,
    campus.nome AS campus
FROM reserva r
INNER JOIN usuario ON usuario.id_pessoa = r.codigo_usuario
INNER JOIN pessoa ON pessoa.id_pessoa = usuario.id_pessoa
INNER JOIN sala ON sala.codigo = r.codigo_sala
INNER JOIN predio ON predio.codigo = sala.codigo_predio
INNER JOIN campus ON campus.codigo = predio.codigo_campus
WHERE r.data BETWEEN CURRENT_DATE AND CURRENT_DATE + 30
ORDER BY r.data, r.horario_inicio, campus.nome, predio.nome, sala.numero_sala;

-- Demonstracao do trigger de Antonio.
-- Execute primeiro o INSERT valido. A transacao e desfeita para nao alterar os dados da demonstracao.

BEGIN;

INSERT INTO reserva (
    data, tipo_reserva, status_reserva, objetivo,
    horario_inicio, horario_fim, codigo_usuario, codigo_sala
) VALUES (
    CURRENT_DATE + 10, 'reuniao', 'pendente', 'Teste valido do trigger',
    '17:00', '18:00', 1, 1
);

ROLLBACK;

-- Depois execute apenas o bloco abaixo. O trigger deve rejeitar o INSERT.

BEGIN;

INSERT INTO reserva (
    data, tipo_reserva, status_reserva, objetivo,
    horario_inicio, horario_fim, codigo_usuario, codigo_sala
) VALUES (
    CURRENT_DATE - 1, 'reuniao', 'pendente', 'Teste invalido do trigger',
    '17:00', '18:00', 1, 1
);

ROLLBACK;
