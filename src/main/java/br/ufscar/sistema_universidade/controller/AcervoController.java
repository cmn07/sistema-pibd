package br.ufscar.sistema_universidade.controller;

import br.ufscar.sistema_universidade.model.MaterialAcervo;
import br.ufscar.sistema_universidade.service.MaterialService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/acervo")
public class AcervoController {

    private static final String MENSAGEM_CONFLITO_INTEGRIDADE =
            "Nao foi possivel inserir ou realizar a acao. Verifique campos obrigatorios, valores duplicados ou vinculos existentes.";

    private final MaterialService materialService;

    public AcervoController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @FunctionalInterface
    private interface OperacaoPersistencia {
        void executar();
    }

    private String executarComTratamentoDeErro(
            String mensagemSucesso,
            RedirectAttributes redirectAttributes,
            OperacaoPersistencia operacao
    ) {
        try {
            operacao.executar();
            redirectAttributes.addFlashAttribute("mensagem", mensagemSucesso);
        } catch (IllegalArgumentException | DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("mensagem", MENSAGEM_CONFLITO_INTEGRIDADE);
        }
        return "redirect:/acervo";
    }

    @GetMapping
    public String acervo(Model model) {
        model.addAttribute("materiais", materialService.listarTodos());
        return "acervo";
    }

    @GetMapping("/novo")
    public String novoMaterial(Model model) {
        model.addAttribute("tiposMaterial", materialService.listarTiposMaterial());
        model.addAttribute("materialSelecionado", new MaterialAcervo());
        model.addAttribute("edicao", false);
        return "acervo-form";
    }

    @GetMapping("/editar")
    public String editarMaterial(Model model, @RequestParam Long codigo, RedirectAttributes redirectAttributes) {
        MaterialAcervo material = materialService.buscarPorId(codigo);
        if (material == null) {
            redirectAttributes.addFlashAttribute("mensagem", "Material nao encontrado.");
            return "redirect:/acervo";
        }
        model.addAttribute("tiposMaterial", materialService.listarTiposMaterial());
        model.addAttribute("materialSelecionado", material);
        model.addAttribute("edicao", true);
        return "acervo-form";
    }

    @PostMapping("/salvar")
    public String salvarMaterial(
            @RequestParam String tipoMaterial,
            @RequestParam String titulo,
            @RequestParam(required = false, defaultValue = "1") Integer quantidadeCopias,
            @RequestParam(required = false) Integer anoPublicacao,
            @RequestParam(required = false) String idioma,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String editora,
            RedirectAttributes redirectAttributes
    ) {
        return executarComTratamentoDeErro(
                "Material salvo com sucesso.",
                redirectAttributes,
                () -> materialService.salvar(new MaterialAcervo(
                        null,
                        tipoMaterial,
                        titulo,
                        quantidadeCopias,
                        anoPublicacao,
                        idioma,
                        categoria,
                        editora
                ))
        );
    }

    @PostMapping("/editar")
    public String editarMaterial(
            @RequestParam Long codigo,
            @RequestParam String tipoMaterial,
            @RequestParam String titulo,
            @RequestParam(required = false, defaultValue = "1") Integer quantidadeCopias,
            @RequestParam(required = false) Integer anoPublicacao,
            @RequestParam(required = false) String idioma,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String editora,
            RedirectAttributes redirectAttributes
    ) {
        return executarComTratamentoDeErro(
                "Material atualizado com sucesso.",
                redirectAttributes,
                () -> materialService.atualizar(new MaterialAcervo(
                        codigo,
                        tipoMaterial,
                        titulo,
                        quantidadeCopias,
                        anoPublicacao,
                        idioma,
                        categoria,
                        editora
                ))
        );
    }

    @PostMapping("/excluir")
    public String excluirMaterial(@RequestParam Long codigo, RedirectAttributes redirectAttributes) {
        return executarComTratamentoDeErro(
                "Material excluido com sucesso.",
                redirectAttributes,
                () -> materialService.deletarPorId(codigo)
        );
    }
}
