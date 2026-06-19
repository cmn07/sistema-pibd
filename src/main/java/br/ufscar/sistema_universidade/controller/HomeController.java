package br.ufscar.sistema_universidade.controller;

import br.ufscar.sistema_universidade.model.Administrador;
import br.ufscar.sistema_universidade.model.Discente;
import br.ufscar.sistema_universidade.model.Funcionario;
import br.ufscar.sistema_universidade.model.Pessoa;
import br.ufscar.sistema_universidade.repository.AdministradorRepository;
import br.ufscar.sistema_universidade.repository.DiscenteRepository;
import br.ufscar.sistema_universidade.repository.FuncionarioRepository;
import br.ufscar.sistema_universidade.repository.PessoaRepository;
import br.ufscar.sistema_universidade.repository.UsuarioRepository;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private final PessoaRepository pessoaRepository;
    private final UsuarioRepository usuarioRepository;
    private final DiscenteRepository discenteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final AdministradorRepository administradorRepository;

    public HomeController(
        PessoaRepository pessoaRepository,
        UsuarioRepository usuarioRepository,
        DiscenteRepository discenteRepository,
        FuncionarioRepository funcionarioRepository,
        AdministradorRepository administradorRepository
    ) {
        this.pessoaRepository = pessoaRepository;
        this.usuarioRepository = usuarioRepository;
        this.discenteRepository = discenteRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.administradorRepository = administradorRepository;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/pessoas")
    public String pessoas(Model model, @RequestParam(required = false) Long editar) {
        model.addAttribute("pessoas", pessoaRepository.listarTodos());
        model.addAttribute("pessoaSelecionada", editar == null ? null : pessoaRepository.buscarPorId(editar));
        return "pessoas";
    }

    @PostMapping("/pessoas/salvar")
    public String salvarPessoa(
        @RequestParam String nome,
        @RequestParam String cpf,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String telefone,
        RedirectAttributes redirectAttributes
    ) {
        pessoaRepository.salvar(new Pessoa(null, nome, cpf, email, telefone));
        redirectAttributes.addFlashAttribute("mensagem", "Pessoa salva com sucesso.");
        return "redirect:/pessoas";
    }

    @PostMapping("/pessoas/excluir")
    public String excluirPessoa(@RequestParam Long idPessoa, RedirectAttributes redirectAttributes) {
        funcionarioRepository.deletarPorId(idPessoa);
        administradorRepository.deletarPorId(idPessoa);
        usuarioRepository.deletarPorId(idPessoa);
        pessoaRepository.deletarPorId(idPessoa);
        redirectAttributes.addFlashAttribute("mensagem", "Pessoa excluida com sucesso.");
        return "redirect:/pessoas";
    }

    @PostMapping("/pessoas/editar")
    public String editarPessoa(
        @RequestParam Long idPessoa,
        @RequestParam String nome,
        @RequestParam String cpf,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String telefone,
        RedirectAttributes redirectAttributes
    ) {
        pessoaRepository.atualizar(new Pessoa(idPessoa, nome, cpf, email, telefone));
        redirectAttributes.addFlashAttribute("mensagem", "Pessoa atualizada com sucesso.");
        return "redirect:/pessoas";
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model, @RequestParam(required = false) Long editar) {
        model.addAttribute("pessoas", pessoaRepository.listarTodos());
        model.addAttribute("usuarios", usuarioRepository.listarTodos());
        model.addAttribute("usuarioSelecionado", editar == null ? null : usuarioRepository.buscarPorId(editar));
        return "usuarios";
    }

    @PostMapping("/usuarios/salvar")
    public String salvarUsuario(@RequestParam Long idPessoa, RedirectAttributes redirectAttributes) {
        usuarioRepository.salvar(idPessoa);
        redirectAttributes.addFlashAttribute("mensagem", "Usuario salvo com sucesso.");
        return "redirect:/usuarios";
    }

    @PostMapping("/usuarios/excluir")
    public String excluirUsuario(@RequestParam Long idPessoa, RedirectAttributes redirectAttributes) {
        funcionarioRepository.deletarPorId(idPessoa);
        usuarioRepository.deletarPorId(idPessoa);
        redirectAttributes.addFlashAttribute("mensagem", "Usuario excluido com sucesso.");
        return "redirect:/usuarios";
    }

    @PostMapping("/usuarios/editar")
    public String editarUsuario(
        @RequestParam Long idPessoaAtual,
        @RequestParam Long idPessoa,
        RedirectAttributes redirectAttributes
    ) {
        if (!idPessoaAtual.equals(idPessoa)) {
            if (!usuarioRepository.existePorId(idPessoa)) {
                usuarioRepository.deletarPorId(idPessoaAtual);
                usuarioRepository.salvar(idPessoa);
            }
        }
        redirectAttributes.addFlashAttribute("mensagem", "Usuario atualizado com sucesso.");
        return "redirect:/usuarios";
    }

    @GetMapping("/discentes")
    public String discentes(Model model, @RequestParam(required = false) Long editar) {
        model.addAttribute("pessoas", pessoaRepository.listarTodos());
        model.addAttribute("discentes", discenteRepository.listarTodos());
        model.addAttribute("discenteSelecionado", editar == null ? null : discenteRepository.buscarPorId(editar));
        return "discentes";
    }

    @PostMapping("/discentes/salvar")
    public String salvarDiscente(
        @RequestParam Long idPessoa,
        @RequestParam String ra,
        @RequestParam String curso,
        RedirectAttributes redirectAttributes
    ) {
        if (!usuarioRepository.existePorId(idPessoa)) {
            usuarioRepository.salvar(idPessoa);
        }
        discenteRepository.salvar(new Discente(idPessoa, null, null, null, null, ra, curso));
        redirectAttributes.addFlashAttribute("mensagem", "Discente salvo com sucesso.");
        return "redirect:/discentes";
    }

    @PostMapping("/discentes/excluir")
    public String excluirDiscente(@RequestParam Long idPessoa, RedirectAttributes redirectAttributes) {
        discenteRepository.deletarPorId(idPessoa);
        redirectAttributes.addFlashAttribute("mensagem", "Discente excluido com sucesso.");
        return "redirect:/discentes";
    }

    @PostMapping("/discentes/editar")
    public String editarDiscente(
        @RequestParam Long idPessoaAtual,
        @RequestParam Long idPessoa,
        @RequestParam String ra,
        @RequestParam String curso,
        RedirectAttributes redirectAttributes
    ) {
        if (!usuarioRepository.existePorId(idPessoa)) {
            usuarioRepository.salvar(idPessoa);
        }
        discenteRepository.atualizar(idPessoaAtual, new Discente(idPessoa, null, null, null, null, ra, curso));
        redirectAttributes.addFlashAttribute("mensagem", "Discente atualizado com sucesso.");
        return "redirect:/discentes";
    }

    @GetMapping("/funcionarios")
    public String funcionarios(Model model, @RequestParam(required = false) Long editar) {
        model.addAttribute("pessoas", pessoaRepository.listarTodos());
        model.addAttribute("funcionarios", funcionarioRepository.listarTodos());
        model.addAttribute("funcionarioSelecionado", editar == null ? null : funcionarioRepository.buscarPorId(editar));
        return "funcionarios";
    }

    @PostMapping("/funcionarios/salvar")
    public String salvarFuncionario(
        @RequestParam Long idPessoa,
        @RequestParam String idFunc,
        @RequestParam String tipoVinculo,
        RedirectAttributes redirectAttributes
    ) {
        if (!usuarioRepository.existePorId(idPessoa)) {
            usuarioRepository.salvar(idPessoa);
        }
        funcionarioRepository.salvar(new Funcionario(idPessoa, null, null, null, null, idFunc, tipoVinculo));
        redirectAttributes.addFlashAttribute("mensagem", "Funcionario salvo com sucesso.");
        return "redirect:/funcionarios";
    }

    @PostMapping("/funcionarios/excluir")
    public String excluirFuncionario(@RequestParam Long idPessoa, RedirectAttributes redirectAttributes) {
        funcionarioRepository.deletarPorId(idPessoa);
        redirectAttributes.addFlashAttribute("mensagem", "Funcionario excluido com sucesso.");
        return "redirect:/funcionarios";
    }

    @PostMapping("/funcionarios/editar")
    public String editarFuncionario(
        @RequestParam Long idPessoa,
        @RequestParam String idFunc,
        @RequestParam String tipoVinculo,
        RedirectAttributes redirectAttributes
    ) {
        funcionarioRepository.atualizar(new Funcionario(idPessoa, null, null, null, null, idFunc, tipoVinculo));
        redirectAttributes.addFlashAttribute("mensagem", "Funcionario atualizado com sucesso.");
        return "redirect:/funcionarios";
    }

    @GetMapping("/administradores")
    public String administradores(Model model, @RequestParam(required = false) Long editar) {
        model.addAttribute("pessoas", pessoaRepository.listarTodos());
        model.addAttribute("administradores", administradorRepository.listarTodos());
        model.addAttribute("administradorSelecionado", editar == null ? null : administradorRepository.buscarPorId(editar));
        return "administradores";
    }

    @PostMapping("/administradores/salvar")
    public String salvarAdministrador(
        @RequestParam Long idPessoa,
        @RequestParam String idAdmin,
        @RequestParam(required = false) String setor,
        RedirectAttributes redirectAttributes
    ) {
        administradorRepository.salvar(new Administrador(idPessoa, null, null, null, null, idAdmin, setor));
        redirectAttributes.addFlashAttribute("mensagem", "Administrador salvo com sucesso.");
        return "redirect:/administradores";
    }

    @PostMapping("/administradores/excluir")
    public String excluirAdministrador(@RequestParam Long idPessoa, RedirectAttributes redirectAttributes) {
        administradorRepository.deletarPorId(idPessoa);
        redirectAttributes.addFlashAttribute("mensagem", "Administrador excluido com sucesso.");
        return "redirect:/administradores";
    }

    @PostMapping("/administradores/editar")
    public String editarAdministrador(
        @RequestParam Long idPessoa,
        @RequestParam String idAdmin,
        @RequestParam(required = false) String setor,
        RedirectAttributes redirectAttributes
    ) {
        administradorRepository.atualizar(new Administrador(idPessoa, null, null, null, null, idAdmin, setor));
        redirectAttributes.addFlashAttribute("mensagem", "Administrador atualizado com sucesso.");
        return "redirect:/administradores";
    }
}
