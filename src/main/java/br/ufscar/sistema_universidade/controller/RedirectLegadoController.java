package br.ufscar.sistema_universidade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectLegadoController {

    @GetMapping("/campus")
    public String campus() {
        return "redirect:/infraestrutura/campus";
    }

    @GetMapping("/predios")
    public String predios() {
        return "redirect:/infraestrutura/predios";
    }

    @GetMapping("/relatorios/salas")
    public String relatorioSalas() {
        return "redirect:/infraestrutura/relatorios/salas";
    }
}

