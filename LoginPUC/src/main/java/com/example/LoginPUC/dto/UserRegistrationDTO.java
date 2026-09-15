package com.example.LoginPUC.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO que transporta os dados vindos do formulário de registro.
 *
 * As anotações de validação (Bean Validation) garantem que os dados
 * só são aceitos se respeitarem as regras da aplicação.
 */
public class UserRegistrationDTO {

    // Nome completo é obrigatório
    @NotBlank(message = "O nome é obrigatório")
    private String name;

    // Username obrigatório, com tamanho mínimo/máximo
    @NotBlank(message = "O nome de usuário é obrigatório")
    @Size(min = 3, max = 20, message = "O nome de usuário deve ter entre 3 e 20 caracteres")
    private String username;

    // E-mail obrigatório e com formato válido
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Informe um e-mail válido")
    private String email;

    // Senha obrigatória com tamanho mínimo definido pela aplicação
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String password;

    // Confirmação da senha (comparada manualmente no service)
    @NotBlank(message = "A confirmação da senha é obrigatória")
    private String confirmPassword;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}