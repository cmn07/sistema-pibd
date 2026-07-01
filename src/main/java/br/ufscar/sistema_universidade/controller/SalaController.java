package br.ufscar.sistema_universidade.controller;

import br.ufscar.sistema_universidade.model.Sala;
import br.ufscar.sistema_universidade.repository.SalaRepository;
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
public class SalaController {

    private static final String MENSAGEM_CONFLITO_INTEGRIDADE =
            "Nao foi possivel inserir ou realizar a acao. Ja existe um registro com a mesma chave ou com dados conflitantes.";

    private final SalaRepository salaRepository;
    private final PredioRepository predioRepository;

    public SalaController(SalaRepository salaRepository, PredioRepository predioRepository) {
        this.salaRepository = salaRepository;
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
            OperacaoPersistencia operacao) {
        try {
            operacao.executar();
            redirectAttributes.addFlashAttribute("mensagem", mensagemSucesso);
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("mensagem", MENSAGEM_CONFLITO_INTEGRIDADE);
        }
        return "redirect:" + destino;
    }

    // ROTAS DE SALA

    @GetMapping("/salas")
    public String salas(Model model, @RequestParam(required = false) Long editar) {
        model.addAttribute("salas", salaRepository.listarTodos());
        model.addAttribute("salaSelecionada", editar == null ? null : salaRepository.buscarPorId(editar));
        model.addAttribute("predios", predioRepository.listarTodos());
        return "infraestrutura/salas";
    }

    @PostMapping("/salas/salvar")
    public String salvarSala(
            @RequestParam String numero,
            @RequestParam Integer capacidade,
            @RequestParam String categoria,
            @RequestParam Long predio,
            RedirectAttributes redirectAttributes) {
        return executarComTratamentoDeErro(
                "/infraestrutura/salas",
                "Sala salva com sucesso.",
                redirectAttributes,
                () -> salaRepository.salvar(new Sala(null, numero, categoria, capacidade, predio))
        );
    }

    @PostMapping("/salas/editar")
    public String editarSala(
            @RequestParam Long codigo,
            @RequestParam String numero,
            @RequestParam Integer capacidade,
            @RequestParam String categoria,
            @RequestParam Long predio,
            RedirectAttributes redirectAttributes) {
        return executarComTratamentoDeErro(
                "/infraestrutura/salas",
                "Sala atualizada com sucesso.",
                redirectAttributes,
                () -> salaRepository.atualizar(new Sala(codigo, numero, categoria, capacidade, predio))
        );
    }

    @PostMapping("/salas/excluir")
    public String excluirSala(@RequestParam Long codigo, RedirectAttributes redirectAttributes) {
        return executarComTratamentoDeErro(
                "/infraestrutura/salas",
                "Sala excluida com sucesso.",
                redirectAttributes,
                () -> salaRepository.deletarPorId(codigo)
        );
    }
}
