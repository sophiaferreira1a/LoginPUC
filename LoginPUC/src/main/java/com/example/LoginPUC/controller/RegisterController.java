package com.example.LoginPUC.controller;

import com.example.LoginPUC.dto.UserRegistrationDTO;
import com.example.LoginPUC.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller do cadastro de novos usuários.
 */
@Controller
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /register : exibe o formulário de cadastro.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // Objeto vazio vinculado ao formulário Thymeleaf (th:object)
        model.addAttribute("registrationForm", new UserRegistrationDTO());
        return "register";
    }

    /**
     * POST /register : processa os dados do formulário.
     *
     * Fluxo:
     * 1. Validações das anotações (@NotBlank, @Email, @Size) - via BindingResult.
     * 2. Validações de negócio (duplicidade, senhas incompatíveis) - via UserService.
     * 3. Sucesso: redireciona para o login com mensagem de confirmação.
     */
    @PostMapping("/register")
    public String processRegistration(
            @Valid @ModelAttribute("registrationForm") UserRegistrationDTO form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        // 1. Campos inválidos: retorna ao formulário exibindo os erros
        if (bindingResult.hasErrors()) {
            return "register";
        }

        // 2. Regras de negócio: retorna null em caso de sucesso
        String error = userService.registerUser(form);
        if (error != null) {
            model.addAttribute("registrationError", error);
            return "register";
        }

        // 3. Cadastro OK: vai para o login informando o registro concluído
        redirectAttributes.addFlashAttribute("registered", true);
        return "redirect:/login";
    }
}