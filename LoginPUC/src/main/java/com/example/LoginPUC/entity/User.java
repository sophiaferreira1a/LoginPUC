package com.example.LoginPUC.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade que representa um usuário da aplicação.
 *
 * A senha é armazenada como hash BCrypt (nunca em texto puro),
 * conforme a boa prática de segurança solicitada na atividade.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome completo exibido na interface
    private String name;

    // Nome de usuário único, usado no login
    @Column(nullable = false, unique = true)
    private String username;

    // E-mail único, também aceito no login
    @Column(nullable = false, unique = true)
    private String email;

    // Hash BCrypt da senha (nunca armazenar senha em texto puro!)
    @Column(nullable = false)
    private String password;

    // Papel do usuário ("USER").
    // Deixamos como String para simplificar; poderia ser um Enum.
    @Column(nullable = false)
    private String role;

    // Construtor padrão exigido pela JPA
    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}