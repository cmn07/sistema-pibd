package br.ufscar.sistema_universidade.controller;

import br.ufscar.sistema_universidade.model.Campus;
import br.ufscar.sistema_universidade.model.Predio;
import br.ufscar.sistema_universidade.model.Sala;
import br.ufscar.sistema_universidade.repository.CampusRepository;
import br.ufscar.sistema_universidade.repository.PredioRepository;
import br.ufscar.sistema_universidade.repository.SalaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class InfraestruturaVisualizacaoController {
    private final CampusRepository campusRepo;
    private final PredioRepository predioRepo;
    private final SalaRepository salaRepo;

    public InfraestruturaVisualizacaoController(CampusRepository campusRepo, PredioRepository predioRepo, SalaRepository salaRepo) {
        this.campusRepo = campusRepo;
        this.predioRepo = predioRepo;
        this.salaRepo = salaRepo;
    }

    @GetMapping("/infraestrutura/visualizar")
    public String visualizarInfra(Model model) {
        List<Campus> campi = campusRepo.listarTodos();
        List<Predio> predios = predioRepo.listarTodos();
        List<Sala> salas = salaRepo.listarTodos();
        model.addAttribute("campi", campi);
        model.addAttribute("predios", predios);
        model.addAttribute("salas", salas);
        return "infraestrutura/visualizacao";
    }
}
