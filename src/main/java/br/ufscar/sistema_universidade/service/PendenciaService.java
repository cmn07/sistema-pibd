package br.ufscar.sistema_universidade.service;

import br.ufscar.sistema_universidade.dto.PendenciaResumoDTO;
import br.ufscar.sistema_universidade.model.Pendencia;
import br.ufscar.sistema_universidade.repository.PendenciaRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PendenciaService {

    private static final String STATUS_ATIVA = "ativa";

    private final PendenciaRepository pendenciaRepository;

    public PendenciaService(PendenciaRepository pendenciaRepository) {
        this.pendenciaRepository = pendenciaRepository;
    }

    public List<PendenciaResumoDTO> listarTodasResumidas() {
        return pendenciaRepository.listarTodasResumidas();
    }

    public List<PendenciaResumoDTO> listarResumidasPorUsuario(Long idPessoa) {
        return pendenciaRepository.listarResumidasPorUsuario(idPessoa);
    }

    public void criarPendenciaAtiva(Long codigoEmprestimo, Long codigoAdministrador, String motivo) {
        String motivoNormalizado = normalizarMotivo(motivo);
        if (pendenciaRepository.existePorEmprestimo(codigoEmprestimo)) {
            throw new IllegalArgumentException("Ja existe pendencia cadastrada para este emprestimo.");
        }

        pendenciaRepository.salvar(new Pendencia(
                null,
                motivoNormalizado,
                STATUS_ATIVA,
                LocalDate.now(),
                null,
                codigoEmprestimo,
                codigoAdministrador
        ));
    }

    public void encerrarPendencia(Long codigoPendencia) {
        if (codigoPendencia == null) {
            throw new IllegalArgumentException("Informe a pendencia que deve ser encerrada.");
        }

        int atualizados = pendenciaRepository.encerrar(codigoPendencia);
        if (atualizados == 0) {
            throw new IllegalArgumentException("Pendencia nao encontrada ou ja encerrada.");
        }
    }

    private String normalizarMotivo(String motivo) {
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o motivo da pendencia.");
        }
        return motivo.trim();
    }
}
