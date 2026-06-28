package br.ufscar.sistema_universidade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlaceholderController {

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("titulo", "Administracao Geral");
        model.addAttribute("descricao", "Area reservada para funcionalidades administrativas transversais.");
        return "placeholder";
    }
}
