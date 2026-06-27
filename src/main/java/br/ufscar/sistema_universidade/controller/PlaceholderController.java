package br.ufscar.sistema_universidade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlaceholderController {

    @GetMapping("/acervo")
    public String acervo(Model model) {
        model.addAttribute("titulo", "Acervo");
        model.addAttribute("descricao", "Area reservada para a funcionalidade de materiais, livros, periodicos, outros e autores.");
        return "placeholder";
    }

    @GetMapping("/emprestimos")
    public String emprestimos(Model model) {
        model.addAttribute("titulo", "Emprestimos");
        model.addAttribute("descricao", "Area reservada para emprestimos de materiais do acervo.");
        return "placeholder";
    }

    @GetMapping("/pendencias")
    public String pendencias(Model model) {
        model.addAttribute("titulo", "Pendencias");
        model.addAttribute("descricao", "Area reservada para consulta e gestao de pendencias vinculadas a emprestimos.");
        return "placeholder";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("titulo", "Administracao Geral");
        model.addAttribute("descricao", "Area reservada para funcionalidades administrativas transversais.");
        return "placeholder";
    }
}
