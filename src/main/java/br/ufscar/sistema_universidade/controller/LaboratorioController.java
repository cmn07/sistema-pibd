package br.ufscar.sistema_universidade.controller;

import br.ufscar.sistema_universidade.model.Laboratorio;
import br.ufscar.sistema_universidade.repository.LaboratorioRepository;
import br.ufscar.sistema_universidade.repository.PredioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/infraestrutura")
public class LaboratorioController {

    private static final String MENSAGEM_CONFLITO_INTEGRIDADE =
            "Nao foi possivel inserir ou realizar a acao. Ja existe um registro com a mesma chave ou com dados conflitantes.";

    private final LaboratorioRepository laboratorioRepository;
    private final PredioRepository predioRepository;

    public LaboratorioController(LaboratorioRepository laboratorioRepository, PredioRepository predioRepository) {
        this.laboratorioRepository = laboratorioRepository;
        this.predioRepository = predioRepository;
    }

    @FunctionalInterface
    private interface OperacaoPersistencia {
        void executar();
    }

    private String executarComTratamentoDeErro(
            String destino,
            String mensagemSucesso,
            RedirectAttributes redirectAttributes,
            OperacaoPersistencia operacao
    ) {
        try {
            operacao.executar();
            redirectAttributes.addFlashAttribute("mensagem", mensagemSucesso);
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("mensagem", MENSAGEM_CONFLITO_INTEGRIDADE);
        }
        return "redirect:" + destino;
    }

    @GetMapping("/laboratorios")
    public String laboratorios(Model model, @RequestParam(required = false) Long editar) {
        model.addAttribute("laboratorios", laboratorioRepository.listarTodos());
        model.addAttribute("laboratorioSelecionado", editar == null ? null : laboratorioRepository.buscarPorId(editar));
        model.addAttribute("predios", predioRepository.listarTodos());
        return "infraestrutura/laboratorio";
    }

    @PostMapping("/laboratorios/salvar")
    public String salvarLaboratorio(
            @RequestParam String numero,
            @RequestParam Integer capacidade,
            @RequestParam(required=false) String categoria,
            @RequestParam(required=false) Long predio,
            @RequestParam String departamentoSetor,
            @RequestParam Integer quantidadeEquipamentos,
            RedirectAttributes redirectAttributes
    ) {
        return executarComTratamentoDeErro(
                "/infraestrutura/laboratorios",
                "Laboratorio salvo com sucesso.",
                redirectAttributes,
                () -> laboratorioRepository.salvar(new Laboratorio(null, numero, categoria, capacidade, predio, departamentoSetor, quantidadeEquipamentos))
        );
    }

    @PostMapping("/laboratorios/editar")
    public String editarLaboratorio(
            @RequestParam Long codigo,
            @RequestParam String numero,
            @RequestParam Integer capacidade,
            @RequestParam(required=false) String categoria,
            @RequestParam(required=false) Long predio,
            @RequestParam String departamentoSetor,
            @RequestParam Integer quantidadeEquipamentos,
            RedirectAttributes redirectAttributes) {
        return executarComTratamentoDeErro(
                "/infraestrutura/laboratorios",
                "Laboratorio atualizado com sucesso.",
                redirectAttributes,
                () -> laboratorioRepository.atualizar(new Laboratorio(codigo, numero, categoria, capacidade, predio, departamentoSetor, quantidadeEquipamentos))
        );
    }

    @PostMapping("/laboratorios/excluir")
    public String excluirLaboratorio(@RequestParam Long codigo, RedirectAttributes redirectAttributes) {
        return executarComTratamentoDeErro(
                "/infraestrutura/laboratorios",
                "Laboratorio excluido com sucesso.",
                redirectAttributes,
                () -> laboratorioRepository.deletarPorId(codigo)
        );
    }
}
