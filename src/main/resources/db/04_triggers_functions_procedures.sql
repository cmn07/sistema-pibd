-- Script para triggers, functions e procedures.
-- Adicionar aqui regras de negocio implementadas no PostgreSQL quando necessario.

-- Trigger para infraestrutura - valida a capacidade das salas(Rafaela)
CREATE OR REPLACE FUNCTION trg_validar_capacidade_sala()
RETURNS TRIGGER AS $$
BEGIN
    -- Validação para Auditórios
    IF NEW.categoria = 'auditorio' AND NEW.capacidade < 50 THEN
        RAISE EXCEPTION 'Erro de Validação: Um auditório deve ter capacidade para pelo menos 50 pessoas.';
END IF;

    -- Validação para Salas de Reunião
IF NEW.categoria = 'sala_reuniao' AND NEW.capacidade > 40 THEN
        RAISE EXCEPTION 'Erro de Validação: Uma sala de reunião não pode ter capacidade superior a 40 pessoas.';
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validar_capacidade_sala_infra
    BEFORE INSERT OR UPDATE ON sala
    FOR EACH ROW
EXECUTE FUNCTION trg_validar_capacidade_sala();
-- Trigger para reservas - impede conflitos de sala/data/horario em reservas pendentes ou aprovadas.
CREATE OR REPLACE FUNCTION trg_impedir_reserva_conflitante()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status_reserva IN ('pendente', 'aprovada') AND EXISTS (
        SELECT 1
        FROM reserva r
        WHERE r.codigo_sala = NEW.codigo_sala
          AND r.data = NEW.data
          AND r.status_reserva IN ('pendente', 'aprovada')
          AND r.horario_inicio < NEW.horario_fim
          AND r.horario_fim > NEW.horario_inicio
          AND (TG_OP = 'INSERT' OR r.id_reserva <> NEW.id_reserva)
    ) THEN
        RAISE EXCEPTION 'Ja existe reserva pendente ou aprovada para esta sala no mesmo intervalo.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_impedir_reserva_conflitante_reserva
    BEFORE INSERT OR UPDATE ON reserva
    FOR EACH ROW
EXECUTE FUNCTION trg_impedir_reserva_conflitante();

-- Trigger para biblioteca - impede emprestimo para usuario com pendencia ativa.
CREATE OR REPLACE FUNCTION trg_impedir_emprestimo_com_pendencia_ativa()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM pendencia p
        INNER JOIN emprestimo e ON e.id_emprestimo = p.codigo_emprestimo
        WHERE e.codigo_usuario = NEW.codigo_usuario
          AND p.status_pendencia = 'ativa'
    ) THEN
        RAISE EXCEPTION 'Usuario possui pendencia ativa e nao pode realizar emprestimo.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_impedir_emprestimo_com_pendencia_ativa_emprestimo
    BEFORE INSERT OR UPDATE ON emprestimo
    FOR EACH ROW
EXECUTE FUNCTION trg_impedir_emprestimo_com_pendencia_ativa();

-- Trigger para reservas - impede o cadastro de reservas no passado (Antonio).
CREATE OR REPLACE FUNCTION trg_impedir_reserva_no_passado()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status_reserva IN ('pendente', 'aprovada')
       AND NEW.data < CURRENT_DATE THEN
        RAISE EXCEPTION 'Nao e permitido cadastrar reserva pendente ou aprovada em uma data passada.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_impedir_reserva_no_passado_reserva
    BEFORE INSERT OR UPDATE OF data, status_reserva ON reserva
    FOR EACH ROW
EXECUTE FUNCTION trg_impedir_reserva_no_passado();
