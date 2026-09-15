package com.example.LoginPUC.service;

import com.example.LoginPUC.dto.UserRegistrationDTO;
import com.example.LoginPUC.entity.User;
import com.example.LoginPUC.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

/**
 * Regras de negócio relacionadas ao cadastro e à recuperação de senha.
 *
 * Centraliza aqui toda a lógica que envolve o repositório, mantendo
 * os controllers enxutos.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /**
     * Registra um novo usuário no banco.
     *
     * Valida se as senhas coincidem, se não existe username/email
     * duplicado e, só então, salva a senha como hash BCrypt.
     *
     * @return mensagem de erro, ou null se o cadastro foi concluído com sucesso.
     */
    public String registerUser(UserRegistrationDTO dto) {
        // 1. Senha e confirmação devem ser idênticas
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            return "As senhas não coincidem. Verifique e tente novamente.";
        }

        // 2. Username não pode estar em uso
        if (userRepository.existsByUsername(dto.getUsername().trim())) {
            return "Este nome de usuário já está em uso. Escolha outro.";
        }

        // 3. E-mail não pode estar em uso
        if (userRepository.existsByEmail(dto.getEmail().trim())) {
            return "Este e-mail já está cadastrado. Faça login ou recupere sua senha.";
        }

        // 4. Monta a entidade e criptografa a senha com BCrypt
        User user = new User();
        user.setName(dto.getName().trim());
        user.setUsername(dto.getUsername().trim());
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("USER");

        userRepository.save(user);
        return null;
    }

    /**
     * Busca um usuário pelo username OU pelo e-mail.
     * Utilizado pela autenticação do Spring Security.
     */
    public Optional<User> findByUsernameOrEmail(String identifier) {
        Optional<User> byUsername = userRepository.findByUsername(identifier);
        if (byUsername.isPresent()) {
            return byUsername;
        }
        return userRepository.findByEmail(identifier);
    }

    /**
     * Recuperação de senha: gera uma nova senha aleatória, salva o hash
     * no banco e envia a nova senha para o e-mail do usuário.
     *
     * @return a nova senha em texto puro (para o e-mail), ou null se o
     *         e-mail não existir na base.
     */
    public String recoverPassword(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email.trim().toLowerCase());
        if (userOpt.isEmpty()) {
            return null;
        }

        // Gera uma senha temporária aleatória e já guarda o hash no banco
        String newPassword = generateRandomPassword(10);
        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Envia a senha temporária por e-mail (padrão SendEmail)
        emailService.sendEmail(
                user.getEmail(),
                "Recuperação de senha - LoginPUC",
                "Olá " + user.getName() + ",\n\nSua nova senha temporária é: " + newPassword
                        + "\nUse-a para entrar e depois altere no seu perfil.\n\nAtt,\nLoginPUC"
        );
        return newPassword;
    }

    /**
     * Gera uma senha aleatória com caracteres alfanuméricos.
     */
    private String generateRandomPassword(int length) {
        // Caracteres que evitam ambiguidade (0/O, 1/l/I)
        String charset = "ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(charset.charAt(random.nextInt(charset.length())));
        }
        return sb.toString();
    }
}