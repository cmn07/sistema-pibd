package br.ufscar.sistema_universidade.service;

import br.ufscar.sistema_universidade.model.CredencialAcesso;
import br.ufscar.sistema_universidade.model.UsuarioLogado;
import br.ufscar.sistema_universidade.repository.CredencialAcessoRepository;
import java.util.List;
import java.util.Set;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioAutenticadoService implements UserDetailsService {

    private final CredencialAcessoRepository credencialAcessoRepository;

    public UsuarioAutenticadoService(CredencialAcessoRepository credencialAcessoRepository) {
        this.credencialAcessoRepository = credencialAcessoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        CredencialAcesso credencial = credencialAcessoRepository.buscarPorLogin(login)
            .orElseThrow(() -> new UsernameNotFoundException("Credencial nao encontrada."));

        Set<String> perfis = credencialAcessoRepository.buscarPerfisPorPessoa(credencial.getIdPessoa());
        List<SimpleGrantedAuthority> authorities = perfis.stream()
            .map(SimpleGrantedAuthority::new)
            .toList();

        return new UsuarioLogado(
            credencial.getIdPessoa(),
            credencial.getLogin(),
            credencial.getSenhaHash(),
            Boolean.TRUE.equals(credencial.getAtivo()),
            perfis,
            authorities
        );
    }
}
