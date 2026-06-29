package br.ufscar.sistema_universidade.controller;

import br.ufscar.sistema_universidade.model.Campus;
import br.ufscar.sistema_universidade.model.Predio;
import br.ufscar.sistema_universidade.model.Sala;
import br.ufscar.sistema_universidade.repository.CampusRepository;
import br.ufscar.sistema_universidade.repository.PredioRepository;
import br.ufscar.sistema_universidade.repository.SalaRepository;
import br.ufscar.sistema_universidade.repository.RelatorioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/infraestrutura")
public class InfraestruturaVisualizacaoController {
    private final CampusRepository campusRepo;
    private final PredioRepository predioRepo;
    private final SalaRepository salaRepo;
    private final RelatorioRepository relatorioRepository;

    public InfraestruturaVisualizacaoController(CampusRepository campusRepo, PredioRepository predioRepo, SalaRepository salaRepo, RelatorioRepository relatorioRepository) {
        this.campusRepo = campusRepo;
        this.predioRepo = predioRepo;
        this.salaRepo = salaRepo;
        this.relatorioRepository = relatorioRepository;
    }

    @GetMapping("/visualizar")
    public String visualizarInfra(Model model) {
        List<Campus> campi = campusRepo.listarTodos();
        List<Predio> predios = predioRepo.listarTodos();
        List<Sala> salas = salaRepo.listarTodos();
        model.addAttribute("campi", campi);
        model.addAttribute("predios", predios);
        model.addAttribute("salas", salas);
        return "infraestrutura/visualizacao";
    }

    @GetMapping("/relatorio-critico")
    public String relatorioCritico(Model model) {
        model.addAttribute("relatorioCritico", relatorioRepository.emitirRelatorioInfraestruturaCritica());
        return "infraestrutura/relatorio-critico";
    }
}
