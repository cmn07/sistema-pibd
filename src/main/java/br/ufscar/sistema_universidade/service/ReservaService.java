package br.ufscar.sistema_universidade.service;

import br.ufscar.sistema_universidade.model.Reserva;
import br.ufscar.sistema_universidade.repository.FuncionarioRepository;
import br.ufscar.sistema_universidade.repository.ReservaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    public static final List<String> TIPOS_RESERVA = List.of(
        "aula",
        "reuniao",
        "evento",
        "palestra",
        "minicurso",
        "extensao",
        "manutencao",
        "outro"
    );

    public static final List<String> STATUS_RESERVA = List.of(
        "pendente",
        "aprovada",
        "cancelada",
        "rejeitada",
        "finalizada"
    );

    private final ReservaRepository reservaRepository;
    private final FuncionarioRepository funcionarioRepository;

    public ReservaService(ReservaRepository reservaRepository, FuncionarioRepository funcionarioRepository) {
        this.reservaRepository = reservaRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    public void salvar(Reserva reserva) {
        validar(reserva);
        reservaRepository.salvar(reserva);
    }

    public void atualizar(Reserva reserva) {
        validar(reserva);
        reservaRepository.atualizar(reserva);
    }

    public void excluir(Long idReserva) {
        reservaRepository.deletarPorId(idReserva);
    }

    public Reserva buscarPorId(Long idReserva) {
        return reservaRepository.buscarPorId(idReserva);
    }

    public List<Reserva> listarTodos() {
        return reservaRepository.listarTodos();
    }

    public List<Reserva> listarPorUsuario(Long idPessoa) {
        return reservaRepository.listarPorUsuario(idPessoa);
    }

    private void validar(Reserva reserva) {
        if (!TIPOS_RESERVA.contains(reserva.getTipoReserva())) {
            throw new IllegalArgumentException("Tipo de reserva invalido.");
        }
        if (!STATUS_RESERVA.contains(reserva.getStatusReserva())) {
            throw new IllegalArgumentException("Status de reserva invalido.");
        }
        if (!reserva.getHorarioFim().isAfter(reserva.getHorarioInicio())) {
            throw new IllegalArgumentException("O horario final deve ser posterior ao horario inicial.");
        }
        if (!funcionarioRepository.existePorId(reserva.getCodigoUsuario())) {
            throw new IllegalArgumentException("A reserva deve ser solicitada por um funcionario.");
        }
        if (reservaRepository.existeConflito(reserva)) {
            throw new IllegalArgumentException("Ja existe reserva pendente ou aprovada para essa sala no mesmo intervalo.");
        }
    }
}
