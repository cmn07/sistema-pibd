package br.ufscar.sistema_universidade.model;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UsuarioLogado implements UserDetails {

    private final Long idPessoa;
    private final String login;
    private final String senhaHash;
    private final boolean ativo;
    private final Set<String> perfis;
    private final Collection<? extends GrantedAuthority> authorities;

    public UsuarioLogado(Long idPessoa, String login, String senhaHash, boolean ativo,
                         Set<String> perfis, Collection<? extends GrantedAuthority> authorities) {
        this.idPessoa = idPessoa;
        this.login = login;
        this.senhaHash = senhaHash;
        this.ativo = ativo;
        this.perfis = perfis;
        this.authorities = authorities;
    }

    public Long getIdPessoa() {
        return idPessoa;
    }

    public String getLogin() {
        return login;
    }

    public Set<String> getPerfis() {
        return perfis;
    }

    public String getPapeisAtuantes() {
        List<String> papeis = new java.util.ArrayList<>();
        if (temPerfil("ROLE_ADMIN_MASTER")) {
            papeis.add("Administrador master");
        }
        if (temPerfil("ROLE_ADMIN_OPERACIONAL")) {
            papeis.add("Administrador operacional");
        }
        if (temPerfil("ROLE_FUNCIONARIO")) {
            papeis.add("Funcionario");
        }
        if (temPerfil("ROLE_DISCENTE")) {
            papeis.add("Discente");
        }
        if (temPerfil("ROLE_ADMINISTRADOR")) {
            papeis.add("Administrador");
        }
        if (temPerfil("ROLE_USUARIO")) {
            papeis.add("Usuario");
        }
        return papeis.isEmpty() ? "Sem papel definido" : String.join(", ", papeis);
    }

    public boolean temPerfil(String perfil) {
        return perfis.contains(perfil);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return senhaHash;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }
}
