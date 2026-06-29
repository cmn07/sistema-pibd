-- Indices para consultas frequentes da aplicacao.

CREATE INDEX idx_credencial_login ON credencial_acesso(login);
CREATE INDEX idx_reserva_sala_data ON reserva(codigo_sala, data);
CREATE INDEX idx_reserva_usuario ON reserva(codigo_usuario);
CREATE INDEX idx_reserva_data_status ON reserva(data, status_reserva);
CREATE INDEX idx_emprestimo_usuario_status ON emprestimo(codigo_usuario, status_emprestimo);
CREATE INDEX idx_pendencia_status ON pendencia(status_pendencia);
CREATE INDEX idx_material_titulo ON material_acervo(titulo);
CREATE INDEX idx_autor_nome ON autor(nome);
