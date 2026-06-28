package br.ufscar.sistema_universidade.service;

import br.ufscar.sistema_universidade.model.MaterialAcervo;
import br.ufscar.sistema_universidade.repository.MaterialRepository;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class MaterialService {

    private static final Set<String> TIPOS_MATERIAL = Set.of("LIVRO", "PERIODICO", "OUTROS");

    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public List<MaterialAcervo> listarTodos() {
        return materialRepository.listarTodos();
    }

    public MaterialAcervo buscarPorId(Long codigo) {
        return materialRepository.buscarPorId(codigo);
    }

    public void salvar(MaterialAcervo material) {
        materialRepository.salvar(normalizar(material));
    }

    public void atualizar(MaterialAcervo material) {
        materialRepository.atualizar(normalizar(material));
    }

    public void deletarPorId(Long codigo) {
        materialRepository.deletarPorId(codigo);
    }

    public List<String> listarTiposMaterial() {
        return List.of("LIVRO", "PERIODICO", "OUTROS");
    }

    private MaterialAcervo normalizar(MaterialAcervo material) {
        String tipoMaterial = material.getTipoMaterial() == null ? "" : material.getTipoMaterial().trim().toUpperCase();
        if (!TIPOS_MATERIAL.contains(tipoMaterial)) {
            throw new IllegalArgumentException("Tipo de material invalido.");
        }

        Integer quantidadeCopias = material.getQuantidadeCopias() == null ? 1 : material.getQuantidadeCopias();
        if (quantidadeCopias < 0) {
            throw new IllegalArgumentException("Quantidade de copias nao pode ser negativa.");
        }

        Integer anoPublicacao = material.getAnoPublicacao();
        if (anoPublicacao != null && (anoPublicacao <= 1000 || anoPublicacao > 2100)) {
            throw new IllegalArgumentException("Ano de publicacao invalido.");
        }

        material.setTipoMaterial(tipoMaterial);
        material.setTitulo(trimToNull(material.getTitulo()));
        material.setQuantidadeCopias(quantidadeCopias);
        material.setIdioma(trimToNull(material.getIdioma()));
        material.setCategoria(trimToNull(material.getCategoria()));
        material.setEditora(trimToNull(material.getEditora()));

        if (material.getTitulo() == null) {
            throw new IllegalArgumentException("Titulo e obrigatorio.");
        }

        return material;
    }

    private String trimToNull(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        return valor.trim();
    }
}
