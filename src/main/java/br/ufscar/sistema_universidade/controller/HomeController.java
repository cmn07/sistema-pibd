package br.ufscar.sistema_universidade.controller;

import br.ufscar.sistema_universidade.model.Administrador;
import br.ufscar.sistema_universidade.model.CredencialAcesso;
import br.ufscar.sistema_universidade.model.Discente;
import br.ufscar.sistema_universidade.model.Funcionario;
import br.ufscar.sistema_universidade.model.Pessoa;
import br.ufscar.sistema_universidade.repository.AdministradorRepository;
import br.ufscar.sistema_universidade.repository.CredencialAcessoRepository;
import br.ufscar.sistema_universidade.repository.DiscenteRepository;
import br.ufscar.sistema_universidade.repository.FuncionarioRepository;
import br.ufscar.sistema_universidade.repository.PessoaRepository;
import br.ufscar.sistema_universidade.repository.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private static final String MENSAGEM_CONFLITO_INTEGRIDADE =
        "Nao foi possivel inserir ou realizar a acao. Ja existe um registro com a mesma chave ou com dados conflitantes.";

    private final PessoaRepository pessoaRepository;
    private final UsuarioRepository usuarioRepository;
    private final DiscenteRepository discenteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final AdministradorRepository administradorRepository;
    private final CredencialAcessoRepository credencialAcessoRepository;
    private final PasswordEncoder passwordEncoder;

    public HomeController(
        PessoaRepository pessoaRepository,
        UsuarioRepository usuarioRepository,
        DiscenteRepository discenteRepository,
        FuncionarioRepository funcionarioRepository,
        AdministradorRepository administradorRepository,
        CredencialAcessoRepository credencialAcessoRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.pessoaRepository = pessoaRepository;
        this.usuarioRepository = usuarioRepository;
        this.discenteRepository = discenteRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.administradorRepository = administradorRepository;
        this.credencialAcessoRepository = credencialAcessoRepository;
        this.passwordEncoder = passwordEncoder;
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

    @GetMapping("/login")
    public String login() {
        return "login";
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
        return executarComTratamentoDeErro(
            "/pessoas",
            "Pessoa salva com sucesso.",
            redirectAttributes,
            () -> pessoaRepository.salvar(new Pessoa(null, nome, cpf, email, telefone))
        );
    }

    @PostMapping("/pessoas/excluir")
    public String excluirPessoa(@RequestParam Long idPessoa, RedirectAttributes redirectAttributes) {
        return executarComTratamentoDeErro(
            "/pessoas",
            "Pessoa excluida com sucesso.",
            redirectAttributes,
            () -> {
                funcionarioRepository.deletarPorId(idPessoa);
                administradorRepository.deletarPorId(idPessoa);
                usuarioRepository.deletarPorId(idPessoa);
                credencialAcessoRepository.deletarPorId(idPessoa);
                pessoaRepository.deletarPorId(idPessoa);
            }
        );
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
        return executarComTratamentoDeErro(
            "/pessoas",
            "Pessoa atualizada com sucesso.",
            redirectAttributes,
            () -> pessoaRepository.atualizar(new Pessoa(idPessoa, nome, cpf, email, telefone))
        );
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
        return executarComTratamentoDeErro(
            "/usuarios",
            "Usuario salvo com sucesso.",
            redirectAttributes,
            () -> usuarioRepository.salvar(idPessoa)
        );
    }

    @PostMapping("/usuarios/excluir")
    public String excluirUsuario(@RequestParam Long idPessoa, RedirectAttributes redirectAttributes) {
        return executarComTratamentoDeErro(
            "/usuarios",
            "Usuario excluido com sucesso.",
            redirectAttributes,
            () -> {
                funcionarioRepository.deletarPorId(idPessoa);
                usuarioRepository.deletarPorId(idPessoa);
            }
        );
    }

    @PostMapping("/usuarios/editar")
    public String editarUsuario(
        @RequestParam Long idPessoaAtual,
        @RequestParam Long idPessoa,
        RedirectAttributes redirectAttributes
    ) {
        return executarComTratamentoDeErro(
            "/usuarios",
            "Usuario atualizado com sucesso.",
            redirectAttributes,
            () -> {
                if (!idPessoaAtual.equals(idPessoa)) {
                    if (!usuarioRepository.existePorId(idPessoa)) {
                        usuarioRepository.deletarPorId(idPessoaAtual);
                        usuarioRepository.salvar(idPessoa);
                    }
                }
            }
        );
    }

    @GetMapping("/credenciais")
    public String credenciais(Model model, @RequestParam(required = false) Long editar) {
        model.addAttribute("pessoas", pessoaRepository.listarTodos());
        model.addAttribute("credenciais", credencialAcessoRepository.listarTodos());
        model.addAttribute("credencialSelecionada", editar == null ? null : credencialAcessoRepository.buscarPorPessoa(editar));
        return "credenciais";
    }

    @PostMapping("/credenciais/salvar")
    public String salvarCredencial(
        @RequestParam Long idPessoa,
        @RequestParam String login,
        @RequestParam String senha,
        @RequestParam(required = false) Boolean ativo,
        RedirectAttributes redirectAttributes
    ) {
        return executarComTratamentoDeErro(
            "/credenciais",
            "Credencial salva com sucesso.",
            redirectAttributes,
            () -> credencialAcessoRepository.salvar(
                new CredencialAcesso(idPessoa, login, passwordEncoder.encode(senha), ativo)
            )
        );
    }

    @PostMapping("/credenciais/editar")
    public String editarCredencial(
        @RequestParam Long idPessoa,
        @RequestParam String login,
        @RequestParam(required = false) String novaSenha,
        @RequestParam(required = false) Boolean ativo,
        RedirectAttributes redirectAttributes
    ) {
        return executarComTratamentoDeErro(
            "/credenciais",
            "Credencial atualizada com sucesso.",
            redirectAttributes,
            () -> {
                credencialAcessoRepository.atualizarDados(idPessoa, login, ativo);
                if (novaSenha != null && !novaSenha.isBlank()) {
                    credencialAcessoRepository.atualizarSenha(idPessoa, passwordEncoder.encode(novaSenha));
                }
            }
        );
    }

    @PostMapping("/credenciais/excluir")
    public String excluirCredencial(@RequestParam Long idPessoa, RedirectAttributes redirectAttributes) {
        return executarComTratamentoDeErro(
            "/credenciais",
            "Credencial excluida com sucesso.",
            redirectAttributes,
            () -> credencialAcessoRepository.deletarPorId(idPessoa)
        );
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
        return executarComTratamentoDeErro(
            "/discentes",
            "Discente salvo com sucesso.",
            redirectAttributes,
            () -> {
                if (!usuarioRepository.existePorId(idPessoa)) {
                    usuarioRepository.salvar(idPessoa);
                }
                discenteRepository.salvar(new Discente(idPessoa, null, null, null, null, ra, curso));
            }
        );
    }

    @PostMapping("/discentes/excluir")
    public String excluirDiscente(@RequestParam Long idPessoa, RedirectAttributes redirectAttributes) {
        return executarComTratamentoDeErro(
            "/discentes",
            "Discente excluido com sucesso.",
            redirectAttributes,
            () -> discenteRepository.deletarPorId(idPessoa)
        );
    }

    @PostMapping("/discentes/editar")
    public String editarDiscente(
        @RequestParam Long idPessoaAtual,
        @RequestParam Long idPessoa,
        @RequestParam String ra,
        @RequestParam String curso,
        RedirectAttributes redirectAttributes
    ) {
        return executarComTratamentoDeErro(
            "/discentes",
            "Discente atualizado com sucesso.",
            redirectAttributes,
            () -> {
                if (!usuarioRepository.existePorId(idPessoa)) {
                    usuarioRepository.salvar(idPessoa);
                }
                discenteRepository.atualizar(idPessoaAtual, new Discente(idPessoa, null, null, null, null, ra, curso));
            }
        );
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
        return executarComTratamentoDeErro(
            "/funcionarios",
            "Funcionario salvo com sucesso.",
            redirectAttributes,
            () -> {
                if (!usuarioRepository.existePorId(idPessoa)) {
                    usuarioRepository.salvar(idPessoa);
                }
                funcionarioRepository.salvar(new Funcionario(idPessoa, null, null, null, null, idFunc, tipoVinculo));
            }
        );
    }

    @PostMapping("/funcionarios/excluir")
    public String excluirFuncionario(@RequestParam Long idPessoa, RedirectAttributes redirectAttributes) {
        return executarComTratamentoDeErro(
            "/funcionarios",
            "Funcionario excluido com sucesso.",
            redirectAttributes,
            () -> funcionarioRepository.deletarPorId(idPessoa)
        );
    }

    @PostMapping("/funcionarios/editar")
    public String editarFuncionario(
        @RequestParam Long idPessoa,
        @RequestParam String idFunc,
        @RequestParam String tipoVinculo,
        RedirectAttributes redirectAttributes
    ) {
        return executarComTratamentoDeErro(
            "/funcionarios",
            "Funcionario atualizado com sucesso.",
            redirectAttributes,
            () -> funcionarioRepository.atualizar(new Funcionario(idPessoa, null, null, null, null, idFunc, tipoVinculo))
        );
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
        return executarComTratamentoDeErro(
            "/administradores",
            "Administrador salvo com sucesso.",
            redirectAttributes,
            () -> administradorRepository.salvar(new Administrador(idPessoa, null, null, null, null, idAdmin, setor))
        );
    }

    @PostMapping("/administradores/excluir")
    public String excluirAdministrador(@RequestParam Long idPessoa, RedirectAttributes redirectAttributes) {
        return executarComTratamentoDeErro(
            "/administradores",
            "Administrador excluido com sucesso.",
            redirectAttributes,
            () -> administradorRepository.deletarPorId(idPessoa)
        );
    }

    @PostMapping("/administradores/editar")
    public String editarAdministrador(
        @RequestParam Long idPessoa,
        @RequestParam String idAdmin,
        @RequestParam(required = false) String setor,
        RedirectAttributes redirectAttributes
    ) {
        return executarComTratamentoDeErro(
            "/administradores",
            "Administrador atualizado com sucesso.",
            redirectAttributes,
            () -> administradorRepository.atualizar(new Administrador(idPessoa, null, null, null, null, idAdmin, setor))
        );
    }
}
