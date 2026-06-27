package br.ufscar.sistema_universidade.service;

import br.ufscar.sistema_universidade.dto.EmprestimoResumoDTO;
import br.ufscar.sistema_universidade.model.Emprestimo;
import br.ufscar.sistema_universidade.model.MaterialAcervo;
import br.ufscar.sistema_universidade.repository.EmprestimoRepository;
import br.ufscar.sistema_universidade.repository.MaterialRepository;
import br.ufscar.sistema_universidade.repository.UsuarioRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EmprestimoService {

    private static final String STATUS_INICIAL = "ativo";

    private final EmprestimoRepository emprestimoRepository;
    private final MaterialRepository materialRepository;
    private final UsuarioRepository usuarioRepository;
    private final PendenciaService pendenciaService;

    public EmprestimoService(
            EmprestimoRepository emprestimoRepository,
            MaterialRepository materialRepository,
            UsuarioRepository usuarioRepository,
            PendenciaService pendenciaService
    ) {
        this.emprestimoRepository = emprestimoRepository;
        this.materialRepository = materialRepository;
        this.usuarioRepository = usuarioRepository;
        this.pendenciaService = pendenciaService;
    }

    public void salvarEmprestimo(Long codigoUsuario, Long codigoMaterialAcervo, LocalDate dataDevolucaoPrevista) {
        LocalDate dataEmprestimo = LocalDate.now();
        validar(codigoUsuario, codigoMaterialAcervo, dataEmprestimo, dataDevolucaoPrevista);

        emprestimoRepository.salvar(new Emprestimo(
                null,
                dataEmprestimo,
                STATUS_INICIAL,
                null,
                dataDevolucaoPrevista,
                codigoMaterialAcervo,
                codigoUsuario,
                null
        ));
    }

    public List<EmprestimoResumoDTO> listarTodosResumidos() {
        return emprestimoRepository.listarTodosResumidos();
    }

    public List<EmprestimoResumoDTO> listarResumidosPorUsuario(Long idPessoa) {
        return emprestimoRepository.listarResumidosPorUsuario(idPessoa);
    }

    public List<EmprestimoResumoDTO> listarAtivosResumidosPorUsuario(Long idPessoa) {
        return emprestimoRepository.listarAtivosResumidosPorUsuario(idPessoa);
    }

    public void registrarDevolucao(Long idEmprestimo, Long codigoAdministrador) {
        Emprestimo emprestimo = buscarEmprestimoExistente(idEmprestimo);
        if (!"ativo".equals(emprestimo.getStatusEmprestimo()) && !"atrasado".equals(emprestimo.getStatusEmprestimo())) {
            throw new IllegalArgumentException("Apenas emprestimos ativos ou atrasados podem ser devolvidos.");
        }
        int atualizados = emprestimoRepository.registrarDevolucao(idEmprestimo, codigoAdministrador);
        if (atualizados == 0) {
            throw new IllegalArgumentException("Emprestimo nao esta disponivel para devolucao.");
        }
    }

    public void criarPendencia(Long idEmprestimo, Long codigoAdministrador, String motivo) {
        Emprestimo emprestimo = buscarEmprestimoExistente(idEmprestimo);
        if (!"ativo".equals(emprestimo.getStatusEmprestimo()) && !"atrasado".equals(emprestimo.getStatusEmprestimo())) {
            throw new IllegalArgumentException("Apenas emprestimos ativos ou atrasados podem gerar pendencia.");
        }

        emprestimoRepository.marcarComoAtrasado(idEmprestimo, codigoAdministrador);
        pendenciaService.criarPendenciaAtiva(idEmprestimo, codigoAdministrador, motivo);
    }

    private void validar(Long codigoUsuario, Long codigoMaterialAcervo, LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista) {
        if (!usuarioRepository.existePorId(codigoUsuario)) {
            throw new IllegalArgumentException("Apenas pessoas cadastradas como usuario podem realizar emprestimos.");
        }

        if (dataDevolucaoPrevista == null || dataDevolucaoPrevista.isBefore(dataEmprestimo)) {
            throw new IllegalArgumentException("A data prevista de devolucao deve ser hoje ou uma data futura.");
        }

        MaterialAcervo material = materialRepository.buscarPorId(codigoMaterialAcervo);
        if (material == null) {
            throw new IllegalArgumentException("Material do acervo nao encontrado.");
        }

        if (material.getQuantidadeCopias() == null || material.getQuantidadeCopias() <= 0) {
            throw new IllegalArgumentException("Material sem copias disponiveis para emprestimo.");
        }

        int emprestimosAtivos = emprestimoRepository.contarEmprestimosAtivosPorMaterial(codigoMaterialAcervo);
        if (emprestimosAtivos >= material.getQuantidadeCopias()) {
            throw new IllegalArgumentException("Todas as copias deste material ja estao emprestadas.");
        }
    }

    private Emprestimo buscarEmprestimoExistente(Long idEmprestimo) {
        Emprestimo emprestimo = emprestimoRepository.buscarPorId(idEmprestimo);
        if (emprestimo == null) {
            throw new IllegalArgumentException("Emprestimo nao encontrado.");
        }
        return emprestimo;
    }
}
