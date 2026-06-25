package br.ufscar.sistema_universidade.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**", "/images/**").permitAll()
                .requestMatchers("/pessoas", "/pessoas/**", "/usuarios", "/usuarios/**", "/credenciais", "/credenciais/**", "/funcionarios", "/funcionarios/**", "/discentes", "/discentes/**", "/administradores", "/administradores/**").hasRole("ADMIN_MASTER")
                .requestMatchers("/estrutura").hasAnyRole("USUARIO", "ADMIN_MASTER")
                .requestMatchers("/infraestrutura/**", "/campus/**", "/predios/**", "/relatorios/**").hasRole("ADMIN_MASTER")
                .requestMatchers("/admin", "/admin/**").hasRole("ADMIN_MASTER")
                .requestMatchers(HttpMethod.POST, "/reservas/editar", "/reservas/excluir").hasRole("ADMIN_OPERACIONAL")
                .requestMatchers("/reservas", "/reservas/nova", "/reservas/salvar").hasAnyRole("FUNCIONARIO", "ADMIN_OPERACIONAL")
                .requestMatchers("/acervo", "/acervo/**", "/emprestimos", "/emprestimos/**", "/pendencias", "/pendencias/**").hasAnyRole("USUARIO", "ADMIN_OPERACIONAL")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
