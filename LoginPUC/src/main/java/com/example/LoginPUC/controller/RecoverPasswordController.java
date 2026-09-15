package com.example.LoginPUC.controller;

import com.example.LoginPUC.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller da recuperação de senha.
 *
 * Desafio opcional da atividade: o usuário informa o e-mail, o sistema
 * gera uma nova senha aleatória, salva o hash no banco e envia a nova
 * senha por e-mail (padrão do projeto SendEmail).
 */
@Controller
public class RecoverPasswordController {

    private final UserService userService;

    public RecoverPasswordController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /recoverpassword : exibe o formulário de recuperação.
     */
    @GetMapping("/recoverpassword")
    public String showRecoverForm() {
        return "recoverpassword";
    }

    /**
     * POST /recoverpassword : processa a solicitação.
     *
     * Boa prática de segurança: a mensagem é genérica, para não revelar
     * se um e-mail está ou não cadastrado na base.
     */
    @PostMapping("/recoverpassword")
    public String processRecover(@RequestParam("email") String email, Model model) {
        String newPassword = userService.recoverPassword(email);

        if (newPassword != null) {
            model.addAttribute("message",
                    "Uma nova senha foi enviada para o seu e-mail. Acesse sua caixa de entrada.");
        } else {
            model.addAttribute("message",
                    "Se este e-mail estiver cadastrado, uma nova senha será enviada para você.");
        }

        return "recoverpassword";
    }
}