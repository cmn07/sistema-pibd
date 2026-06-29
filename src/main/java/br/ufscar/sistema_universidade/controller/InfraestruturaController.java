package br.ufscar.sistema_universidade.controller;

import br.ufscar.sistema_universidade.model.Campus;
import br.ufscar.sistema_universidade.model.Predio;
import br.ufscar.sistema_universidade.repository.CampusRepository;
import br.ufscar.sistema_universidade.repository.PredioRepository;
import br.ufscar.sistema_universidade.repository.RelatorioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/infraestrutura")
public class InfraestruturaController {

    private static final String MENSAGEM_CONFLITO_INTEGRIDADE =
            "Nao foi possivel inserir ou realizar a acao. Ja existe um registro com a mesma chave ou com dados conflitantes.";

    private final CampusRepository campusRepository;
    private final PredioRepository predioRepository;
    private final RelatorioRepository relatorioRepository;

    public InfraestruturaController(CampusRepository campusRepository, PredioRepository predioRepository, RelatorioRepository relatorioRepository) {
        this.campusRepository = campusRepository;
        this.predioRepository = predioRepository;
        this.relatorioRepository = relatorioRepository;
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

    // ROTAS DE CAMPUS

    @GetMapping("/campus")
    public String campus(Model model, @RequestParam(required = false) Long editar) {
        model.addAttribute("campi", campusRepository.listarTodos());
        model.addAttribute("campusSelecionado", editar == null ? null : campusRepository.buscarPorId(editar));
        return "infraestrutura/campus";
    }

    @PostMapping("/campus/salvar")
    public String salvarCampus(@RequestParam String nome, @RequestParam String cidade, RedirectAttributes redirectAttributes) {
        return executarComTratamentoDeErro("/infraestrutura/campus", "Campus salvo com sucesso.", redirectAttributes,
                () -> campusRepository.salvar(new Campus(null, nome, cidade)));
    }

    @PostMapping("/campus/editar")
    public String editarCampus(@RequestParam Long codigo, @RequestParam String nome, @RequestParam String cidade, RedirectAttributes redirectAttributes) {
        return executarComTratamentoDeErro("/infraestrutura/campus", "Campus atualizado com sucesso.", redirectAttributes,
                () -> campusRepository.atualizar(new Campus(codigo, nome, cidade)));
    }

    @PostMapping("/campus/excluir")
    public String excluirCampus(@RequestParam Long codigo, RedirectAttributes redirectAttributes) {
        return executarComTratamentoDeErro("/infraestrutura/campus", "Campus excluido com sucesso.", redirectAttributes,
                () -> campusRepository.deletarPorId(codigo));
    }

    // ROTAS DE PRÉDIO

    @GetMapping("/predios")
    public String predios(Model model, @RequestParam(required = false) Long editar) {
        model.addAttribute("campi", campusRepository.listarTodos());
        model.addAttribute("predios", predioRepository.listarTodos());
        model.addAttribute("predioSelecionado", editar == null ? null : predioRepository.buscarPorId(editar));
        return "infraestrutura/predios";
    }

    @PostMapping("/predios/salvar")
    public String salvarPredio(@RequestParam String nome, @RequestParam(required = false) String bloco, @RequestParam Long codigoCampus, RedirectAttributes redirectAttributes) {
        return executarComTratamentoDeErro("/infraestrutura/predios", "Predio salvo com sucesso.", redirectAttributes,
                () -> predioRepository.salvar(new Predio(null, nome, bloco, codigoCampus)));
    }

    @PostMapping("/predios/editar")
    public String editarPredio(@RequestParam Long codigo, @RequestParam String nome, @RequestParam(required = false) String bloco, @RequestParam Long codigoCampus, RedirectAttributes redirectAttributes) {
        return executarComTratamentoDeErro("/infraestrutura/predios", "Predio atualizado com sucesso.", redirectAttributes,
                () -> predioRepository.atualizar(new Predio(codigo, nome, bloco, codigoCampus)));
    }

    @PostMapping("/predios/excluir")
    public String excluirPredio(@RequestParam Long codigo, RedirectAttributes redirectAttributes) {
        return executarComTratamentoDeErro("/infraestrutura/predios", "Predio excluido com sucesso.", redirectAttributes,
                () -> predioRepository.deletarPorId(codigo));
    }

    // ROTAS DE RELATÓRIOS

    @GetMapping("/relatorios/salas")
    public String relatorioSalas(Model model) {
        model.addAttribute("relatorio", relatorioRepository.emitirRelatorioInfraestrutura());
        return "infraestrutura/relatorio-salas";
    }

    // RELATÓRIO DE SALAS DISPONÍVEIS POR DATA (Hashimoto)
    @GetMapping("/relatorios/salas-disponiveis")
    public String relatorioSalasDisponiveis(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horarioInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horarioFim,
            Model model) {
        if (data == null) data = LocalDate.now();
        model.addAttribute("salas", relatorioRepository.listarSalasDisponiveis(data, horarioInicio, horarioFim));
        model.addAttribute("dataSelecionada", data);
        model.addAttribute("horarioInicioSelecionado", horarioInicio);
        model.addAttribute("horarioFimSelecionado", horarioFim);
        return "infraestrutura/relatorio-salas-disponiveis";
    }
}