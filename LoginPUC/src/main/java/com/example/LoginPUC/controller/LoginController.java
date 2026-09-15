package com.example.LoginPUC.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller da tela de login.
 *
 * Apenas o GET /login é mapeado aqui: o POST /login (a autenticação em si)
 * é processado pelo próprio Spring Security, conforme configurado em SecurityConfig.
 */
@Controller
public class LoginController {

    /**
     * GET / : redireciona para a tela de login (evita erro 404 na raiz).
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    /**
     * GET /login : exibe a tela de login.
     *
     * Se o usuário já estiver autenticado (ex.: acabou de logar), a própria
     * tela mostra o painel "logado" com o botão de logout.
     */
    @GetMapping("/login")
    public String login(Authentication authentication, Model model) {
        // Parâmetros de mensagem adicionados via RedirectAttributes ou query string
        // (ex.: erro, logout, sucesso, registro concluído) são lidos pelo Thymeleaf.

        if (authentication != null && authentication.isAuthenticated()) {
            // Expõe o nome do usuário autenticado para a view
            model.addAttribute("username", authentication.getName());
        }
        return "login";
    }
}