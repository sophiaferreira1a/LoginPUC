# 🔐 LoginPUC

Aplicação web de **autenticação e cadastro de usuários** desenvolvida com **Spring Boot + Thymeleaf + Spring Security** para a disciplina de Desenvolvimento e Integração de Aplicações Web (PUC Minas).

## ✨ Funcionalidades

- 👤 Cadastro de novos usuários com validações (campos obrigatórios, e-mail válido, senha mínima, confirmação de senha, usuário/e-mail duplicados)
- 🔑 Login por **username ou e-mail**, com senha armazenada como hash **BCrypt** (nunca em texto puro)
- 🚪 Logout da sessão
- ⚠️ Mensagens de erro/sucesso nas telas (credenciais inválidas, cadastro concluído, logout, recuperação)
- 📧 **Recuperação de senha por e-mail** (desafio opcional): o sistema gera uma nova senha aleatória, salva o hash no banco e envia a nova senha por e-mail (padrão do projeto `SendEmail`)
- 🎨 Identidade visual própria, responsiva (gradientes, hover, cartão com imagem)

## 🚀 Como executar

Pré-requisitos:

- **Java 25+** instalado
- **Maven 3.9+** (ou use o wrapper `mvnw.cmd` do projeto)

Na pasta do projeto:

```bash
# Linux/macOS
./mvnw spring-boot:run

# Windows
.\mvnw.cmd spring-boot:run
```

Acesse: <http://localhost:8080/login>

> ⚠️ O banco **H2 em memória** é recriado a cada execução: os usuários cadastrados são perdidos ao reiniciar a aplicação. Ideal para demonstração/entrega.

## 🌐 Endpoints

| Método | Endpoint             | Descrição                                           | Autenticação |
| ------ | -------------------- | --------------------------------------------------- | ------------ |
| `GET`  | `/`                  | Redireciona para a tela de login                    | —            |
| `GET`  | `/login`             | Exibe a tela de login                               | Público      |
| `POST` | `/login`             | Processa a autenticação (**Spring Security**)       | Público      |
| `GET`  | `/register`          | Exibe o formulário de cadastro                      | Público      |
| `POST` | `/register`          | Processa o cadastro (validações + BCrypt)           | Público      |
| `GET`  | `/recoverpassword`   | Exibe o formulário de recuperação de senha          | Público      |
| `POST` | `/recoverpassword`   | Gera nova senha, salva o hash e envia por e-mail    | Público      |
| `POST` | `/logout`            | Encerra a sessão                                    | Autenticado  |
| `GET`  | `/h2-console`        | Console do banco H2 (login `sa`, sem senha)         | Público      |

> Todas as demais URLs exigem usuário autenticado (qualquer acesso não autenticado é redirecionado para `/login`).

## 🔑 Configuração do ambiente

### Banco de dados (H2 em memória)

Já configurado em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:loginpuc;DB_CLOSE_DELAY=-1
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```

Para trocar por **MySQL**, substitua por:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/loginpuc
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

### Envio de e-mail (recuperação de senha)

As credenciais **não ficam no código-fonte**. Defina duas **variáveis de ambiente**:

| Variável        | Valor                                           |
| --------------- | ----------------------------------------------- |
| `MAIL_USERNAME` | Seu e-mail Gmail (usado no remetente)           |
| `MAIL_PASSWORD` | App Password do Gmail (16 caracteres)           |

Passos para gerar o App Password:

1. Ative a **Verificação em duas etapas** na sua conta Google.
2. Acesse <https://myaccount.google.com/apppasswords>.
3. Gere uma senha de app para "Correio" e copie os 16 caracteres.
4. Defina as variáveis no seu sistema:

```bash
# Linux/macOS
export MAIL_USERNAME="seuemail@gmail.com"
export MAIL_PASSWORD="aaaa bbbb cccc dddd"

# Windows (PowerShell)
$env:MAIL_USERNAME="seuemail@gmail.com"
$env:MAIL_PASSWORD="aaaa bbbb cccc dddd"
```

> ⚠️ **Nunca publique senhas, tokens ou e-mails reais no GitHub.** O `application.properties` usa `${MAIL_USERNAME:}` e `${MAIL_PASSWORD:}`, permitindo rodar a aplicação sem SMTP (o e-mail falha silenciosamente e a recuperação continua funcionando para demonstração).

### Dissecando o fluxo de recuperação

1. Usuário informa o e-mail em `POST /recoverpassword`.
2. `UserService.recoverPassword()` gera uma senha aleatória e salva o hash BCrypt no banco.
3. `EmailService` envia a nova senha para o e-mail (SMTP Gmail).
4. A mensagem exibida é **genérica** para não revelar se o e-mail existe na base (boa prática de segurança).

## 📁 Estrutura do projeto

```
src/main/java/com/example/LoginPUC
├── application/       LoginPucApplication.java (bootstrap)
├── config/            SecurityConfig.java (Spring Security + BCrypt)
├── controller/        LoginController, RegisterController, RecoverPasswordController
├── dto/               UserRegistrationDTO (validações dos formulários)
├── entity/            User.java (mapeamento JPA)
├── repository/        UserRepository.java (acesso a dados)
└── service/           UserService, CustomUserDetailsService, EmailService

src/main/resources
├── static/
│   ├── css/           login.css, register.css (identidade visual + responsividade)
│   └── images/        bg-puc.jpeg, apc-login-bg.png
└── templates/         login.html, register.html, recoverpassword.html (Thymeleaf)
```

## 🛠️ Tecnologias

- Java 25 · Spring Boot 4.1
- Spring MVC · Thymeleaf
- Spring Security (BCrypt, login form, CSRF)
- Spring Data JPA · H2 (em memória)
- Spring Mail (SMTP Gmail)

## 📄 Licença

Projeto acadêmico — reutilização livre para fins de estudo.