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