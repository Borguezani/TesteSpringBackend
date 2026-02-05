# Teste Spring Backend

API REST desenvolvida com Spring Boot 3.2.0 para gerenciamento de clientes com autenticação JWT, validação de CPF e consulta de CEP.

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.2.0**
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring Validation
- **H2 Database** (banco em memória)
- **JWT** (autenticação via tokens)
- **Swagger/OpenAPI** (documentação)
- **Lombok**
- **Docker & Docker Compose**
- **Maven**

## 📋 Funcionalidades

- ✅ Sistema de autenticação com JWT (Access Token + Refresh Token)
- ✅ CRUD completo de clientes
- ✅ Validação de CPF
- ✅ Consulta de endereço por CEP
- ✅ Documentação interativa com Swagger
- ✅ Console H2 para visualizar banco de dados
- ✅ Tratamento global de exceções
- ✅ CORS configurado

## 🔧 Pré-requisitos

Escolha uma das opções:

### Opção 1: Com Docker (Recomendado)
- Docker
- Docker Compose

### Opção 2: Sem Docker
- Java JDK 17+
- Maven 3.6+

## 🐳 Executando com Docker

### 1. Construir e iniciar o container

```bash
docker-compose up --build
```

Ou em background:
```bash
docker-compose up -d
```

### 2. Acessar a aplicação

A aplicação estará disponível em: `http://localhost:8080`

### 3. Parar o container

```bash
docker-compose down
```

### Comandos úteis

```bash
# Ver logs
docker-compose logs -f

# Reiniciar o container
docker-compose restart

# Remover containers e volumes
docker-compose down -v
```

## 💻 Executando sem Docker

### 1. Clonar o repositório

```bash
cd /caminho/do/projeto
```

### 2. Compilar o projeto

```bash
./mvnw clean package
```

Ou no Windows:
```bash
mvnw.cmd clean package
```

### 3. Executar a aplicação

```bash
./mvnw spring-boot:run
```

Ou executar o JAR diretamente:
```bash
java -jar target/teste-spring-app-0.0.1-SNAPSHOT.jar
```

### 4. Configurar variáveis de ambiente (opcional)

```bash
export JWT_SECRET=sua-chave-secreta
export JWT_EXPIRATION=3600000
export JWT_REFRESH_EXPIRATION=86400000

./mvnw spring-boot:run
```

## 📚 Acessando a Documentação

### Swagger UI
Interface interativa para testar os endpoints:
```
http://localhost:8080/swagger-ui.html
```

### OpenAPI Docs (JSON)
```
http://localhost:8080/v3/api-docs
```

### Console H2
Acesse o banco de dados H2:
```
http://localhost:8080/h2-console
```

**Configurações de conexão:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (deixar em branco)

## 🔑 Endpoints da API

### Autenticação (`/api/auth`)

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/api/auth/register` | Registrar novo usuário | Não |
| POST | `/api/auth/login` | Fazer login | Não |
| POST | `/api/auth/refresh` | Renovar access token | Não |
| POST | `/api/auth/logout` | Fazer logout | Não |

### Clientes (`/api/clientes`)

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/api/clientes` | Criar novo cliente | Sim |
| GET | `/api/clientes` | Listar todos os clientes | Sim |
| GET | `/api/clientes/{id}` | Buscar cliente por ID | Sim |
| PUT | `/api/clientes/{id}` | Atualizar cliente | Sim |
| DELETE | `/api/clientes/{id}` | Deletar cliente | Sim |

### CEP (`/api/cep`)

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| GET | `/api/cep/{cep}` | Buscar endereço por CEP | Sim |

## 📝 Exemplos de Uso

### 1. Registrar um novo usuário

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario",
    "email": "usuario@email.com",
    "password": "senha123"
  }'
```

**Resposta:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
  "type": "Bearer",
  "username": "usuario",
  "email": "usuario@email.com"
}
```

### 2. Fazer login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario",
    "password": "senha123"
  }'
```

### 3. Criar um cliente (requer autenticação)

```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_ACCESS_TOKEN" \
  -d '{
    "nome": "João Silva",
    "cpf": "12345678901",
    "email": "joao@email.com",
    "telefone": "11999999999",
    "endereco": {
      "cep": "01310-100",
      "logradouro": "Av. Paulista",
      "numero": "1000",
      "complemento": "Apto 101",
      "bairro": "Bela Vista",
      "cidade": "São Paulo",
      "estado": "SP"
    }
  }'
```

### 4. Listar todos os clientes

```bash
curl -X GET http://localhost:8080/api/clientes \
  -H "Authorization: Bearer SEU_ACCESS_TOKEN"
```

### 5. Buscar endereço por CEP

```bash
curl -X GET http://localhost:8080/api/cep/01310-100 \
  -H "Authorization: Bearer SEU_ACCESS_TOKEN"
```

### 6. Renovar access token

```bash
curl -X POST "http://localhost:8080/api/auth/refresh?refreshToken=SEU_REFRESH_TOKEN"
```

### 7. Fazer logout

```bash
curl -X POST "http://localhost:8080/api/auth/logout?refreshToken=SEU_REFRESH_TOKEN"
```

## 🔐 Autenticação JWT

A API utiliza JWT (JSON Web Tokens) para autenticação:

1. **Registre** ou faça **login** para obter um `accessToken` e `refreshToken`
2. Inclua o `accessToken` no header de todas as requisições protegidas:
   ```
   Authorization: Bearer SEU_ACCESS_TOKEN
   ```
3. Quando o `accessToken` expirar (1 hora por padrão), use o `refreshToken` para obter um novo
4. O `refreshToken` expira em 24 horas por padrão

## ⚙️ Configurações

As principais configurações estão no arquivo [application.properties](src/main/resources/application.properties):

```properties
# Servidor
server.port=8080

# Banco de dados H2
spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.enabled=true

# JWT
jwt.secret=minha-chave-secreta-super-segura-para-jwt-tokens-dev
jwt.expiration=3600000          # 1 hora em ms
jwt.refresh-expiration=86400000 # 24 horas em ms

# Swagger
springdoc.swagger-ui.path=/swagger-ui.html
```

### Variáveis de Ambiente

Você pode sobrescrever as configurações usando variáveis de ambiente:

- `JWT_SECRET` - Chave secreta para assinar os tokens JWT
- `JWT_EXPIRATION` - Tempo de expiração do access token (em ms)
- `JWT_REFRESH_EXPIRATION` - Tempo de expiração do refresh token (em ms)
- `SPRING_DATASOURCE_URL` - URL do banco de dados

## 🧪 Testes

### Executar testes

```bash
./mvnw test
```

### Executar com cobertura

```bash
./mvnw clean test
```

## 📦 Build para Produção

### Gerar JAR

```bash
./mvnw clean package -DskipTests
```

O arquivo JAR será gerado em: `target/teste-spring-app-0.0.1-SNAPSHOT.jar`

### Executar JAR

```bash
java -jar target/teste-spring-app-0.0.1-SNAPSHOT.jar
```

### Build Docker

```bash
docker build -t teste-spring-app .
docker run -p 8080:8080 teste-spring-app
```

## 🛠️ Desenvolvimento

### Estrutura do Projeto

```
src/main/java/com/example/teste_spring_app/
├── config/              # Configurações (Security, CORS, Swagger)
├── controller/          # Controllers REST
├── dto/                 # Data Transfer Objects
├── entity/              # Entidades JPA
├── exception/           # Tratamento de exceções
├── repository/          # Repositórios JPA
├── security/            # Filtros e utilitários de segurança
├── service/             # Lógica de negócio
└── util/                # Utilitários (validadores)
```

### Hot Reload (Spring DevTools)

Para habilitar hot reload durante o desenvolvimento, adicione a dependência do Spring DevTools no [pom.xml](pom.xml):

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <optional>true</optional>
</dependency>
```

## 🐛 Troubleshooting

### Porta 8080 já está em uso

```bash
# Linux/Mac - Encontrar processo usando a porta
lsof -i :8080

# Windows
netstat -ano | findstr :8080

# Mudar a porta no application.properties
server.port=8081
```

### Erro de permissão no mvnw (Linux/Mac)

```bash
chmod +x mvnw
```

### Docker não inicia

```bash
# Verificar logs
docker-compose logs

# Limpar containers e volumes
docker-compose down -v
docker system prune -a
```

## 📄 Licença

Este projeto é um teste/exemplo para fins educacionais.

## 👤 Autor

Desenvolvido para demonstração de API REST com Spring Boot.

---

## 📌 Links Úteis

- [Documentação Spring Boot](https://spring.io/projects/spring-boot)
- [Documentação Swagger](https://swagger.io/docs/)
- [JWT.io](https://jwt.io/)
- [H2 Database](https://www.h2database.com/)
