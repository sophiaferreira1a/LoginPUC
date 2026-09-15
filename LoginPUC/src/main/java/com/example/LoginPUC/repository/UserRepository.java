package com.example.LoginPUC.repository;

import com.example.LoginPUC.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório responsável pelo acesso a dados da entidade User.
 *
 * O Spring Data JPA gera automaticamente a implementação dos métodos
 * declarados aqui, seguindo a convenção de nomenclatura.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // Busca pelo nome de usuário exato (usado na autenticação)
    Optional<User> findByUsername(String username);

    // Busca pelo e-mail exato (também aceito no login e na recuperação de senha)
    Optional<User> findByEmail(String email);

    // Validações de duplicidade no cadastro:
    // retornam true se já existir um usuário/email igual na base
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}