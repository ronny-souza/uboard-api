# Uboard – Backend

Backend da aplicação **Uboard**, responsável por autenticação, sincronização com GitLab, gerenciamento de organizações, tarefas assíncronas e sessões de **Scrum Poker** com WebSocket.

---

## 🧰 Tecnologias Utilizadas

- Java 21
- Spring Boot 3
- Spring Security + OAuth2 (Keycloak)
- Spring Data JPA + PostgreSQL
- RabbitMQ (tarefas assíncronas)
- WebSocket (votação em tempo real)
- Quartz Scheduler (sincronizações automáticas)
- HashiCorp Vault (armazenamento seguro de tokens)
- Docker Compose (serviços auxiliares)

---

## 📦 Funcionalidades

- Integração com provedores Git (GitLab, GitHub futuramente)
- Cadastro e gerenciamento de credenciais com escopos e tokens
- Criação de organizações vinculadas a credenciais
- Sincronização de milestones e issues
- Sincronização automática via Quartz + RabbitMQ
- Geração e controle de tarefas assíncronas
- Sessões de Scrum Poker com WebSocket
- APIs com filtros dinâmicos (Specifications) e paginação

---

## 🔐 Segurança

- Autenticação via Keycloak (OAuth2)
- Tokens sensíveis armazenados no Vault
- Acesso protegido por roles

---

## ⚙️ Requisitos

- Java 21
- Maven 3.9+
- Docker (para Keycloak, Vault, RabbitMQ, PostgreSQL)

---

## ▶️ Como executar

### 1. Clone o projeto

```bash
git clone https://github.com/seu-usuario/uboard-backend.git
cd uboard-backend
```

### 2. Configure o ambiente

Edite o arquivo `application.yml` com as variáveis corretas de conexão com:
- Keycloak
- Vault
- RabbitMQ
- PostgreSQL

### 3. Suba os serviços auxiliares (para desenvolvimento)

```bash
docker-compose -f infra/docker-compose.dev.yml up
```

### 4. Execute o backend

```bash
./mvnw spring-boot:run
```

---

## 🧪 Testes

```bash
./mvnw test
```
---

## 📄 Licença

Este projeto é proprietário. Consulte o arquivo [`LICENSE`](../LICENSE) para mais informações.
