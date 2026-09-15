package com.example.LoginPUC.service;

import com.example.LoginPUC.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Integração com o Spring Security.
 *
 * Informa ao framework como carregar um usuário a partir da base de dados
 * durante o processo de autenticação (login).
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Chamado pelo Spring Security quando o usuário tenta efetuar login.
     * Aceita username OU e-mail como identificador.
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userService.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + usernameOrEmail));

        // Converte a entidade do banco para o UserDetails entendido pelo Spring Security.
        // A authority tem prefixo ROLE_ (convenção do Spring Security).
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // hash BCrypt armazenado no banco
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())))
                .build();
    }
}