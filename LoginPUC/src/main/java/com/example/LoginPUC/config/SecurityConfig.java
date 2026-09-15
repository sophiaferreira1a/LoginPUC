package com.example.LoginPUC.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração do Spring Security.
 *
 * Define quais endpoints são públicos e quais são protegidos, além de
 * configurar o formulário de login, o logout e o encoder de senhas (BCrypt).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Regras de autorização por URL
                .authorizeHttpRequests(auth -> auth
                        // Páginas públicas (não exigem autenticação)
                        .requestMatchers(
                                "/login", "/register", "/recoverpassword",
                                "/css/**", "/js/**", "/images/**",
                                "/h2-console/**", "/error"
                        ).permitAll()
                        // Qualquer outra URL exige usuário autenticado
                        .anyRequest().authenticated()
                )
                // Permite abrir o console do H2 dentro de um frame (necessário)
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                // Formulário de login gerenciado pelo próprio Spring Security
                .formLogin(form -> form
                        .loginPage("/login")                  // GET: minha tela personalizada
                        .loginProcessingUrl("/login")         // POST: processado pelo framework
                        .defaultSuccessUrl("/login?success", true) // após login OK
                        .failureUrl("/login?error")           // após falha de credencial
                        .permitAll()
                )
                // Logout padrão via POST /logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    /**
     * Encoder de senhas: BCrypt (criptografia de mão única).
     * As senhas nunca são salvas em texto puro no banco.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}