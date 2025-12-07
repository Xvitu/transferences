### Transferences
Uma aplicação responsável por gerenciar transferências financeiras entre usuários.

### Requisitos
- Java 21
- Docker
- Gradle

### Dependencias

A aplicação utiliza um conjunto de dependências do ecossistema Spring e bibliotecas adicionais para garantir validação, comunicação reativa, mensageria e tolerância a falhas.

### **Spring Boot Starters**
- **spring-boot-starter-web**  
  Fornece suporte para criação de APIs REST síncronas (Spring MVC).

- **spring-boot-starter-webflux**  
  Utilizado para chamadas externas reativas via `WebClient` e processamento não-bloqueante.

- **spring-boot-starter-data-jpa**  
  Permite integração com bancos de dados relacionais usando JPA/Hibernate.

- **spring-boot-starter-validation**  
  Inclui o Bean Validation (Jakarta Validation) para validação de DTOs e entidades.

- **spring-boot-starter-amqp**  
  Suporte para integração com RabbitMQ (publisher, listener, queues).

### **Database**

- **postgresql:42.7.3**  
  Driver JDBC para conexão com PostgreSQL.
- **flyway**
  Rodar as migration do projeto

### **Resiliência**

- **resilience4j-spring-boot3**  
  Fornece circuit breaker, retry, rate limiter, bulkhead e fallback integrados ao Spring Boot 3+.

- **resilience4j-reactor**  
  Extensão do Resilience4j para integração com Reactor (Mono/Flux).

### **Testes**

- **reactor-test**  
  Ferramentas para testes de fluxos reativos.

- **spring-boot-starter-webmvc-test**  
  Suporte para testes unitários de controllers MVC.

- **junit-platform-launcher**  
  Runtime necessário para execução da plataforma JUnit 5.

### Estrutura do projeto
```
📦 transferences/
├── 📁 src
│   ├── 📁 main
│   │   └── 📁 java
│   │       ├── 🧠 domain -> Contém as regras de negócio e entidades (ex: cálculo de imposto).
│   │       ├── 🚀 application -> Orquestra as operações (o fluxo de cálculo).
│   │       ├── 🪟 boundary -> Interface de entrada/saída (recebe o JSON, entrega o resultado).
│   │       └── 🏗️ infrastructure -> Implementações externas (como a serialização e persistência de dados).
│   └── 📁 test
├── ⚙️ build.gradle
├── 🐳 Dockerfile
├── 🐳 docker-compose.yml
└── 📄 README.md
```

### Arquitetura

![arquitetura.png](arquitetura.png)

O objetivo dessa arquitetura é segregar as responsabilidades nos principais pontos do fluxo: API, processamento de transferencia e notificação.

Essa separação facilita:
- Escalabilidade horizontal, permitindo aumentar apenas os componentes mais exigidos.
- Resiliência, já que falhas no processamento não impactam diretamente a API.
- Evolução, permitindo trocar serviços externos ou estratégias sem afetar o restante da aplicação.

### Rodando localmente

1. **Subindo as dependências (Postgres + RabbitMQ)**
- O projeto inclui um docker-compose.yml preparado para subir os serviços necessários.
```
docker compose up -d
```
Para verificar os serviços:
- RabbitMQ Management: http://localhost:15672
- Postgres: disponível em localhost:5432

2. **Build da aplicação**

- Compile o projeto usando o Gradle Wrapper:
```
./gradlew clean build
```
- Obs: O docker-compose também contém a aplicação, mas para o uso no dia a dia recomendo rodar via Gradle, pois é mais dinâmico.
4. **Executando a aplicação**
- Para rodar diretamente via Gradle:
````
./gradlew bootRun
````

### Rodando os testes
Para rodar a stack de testes execute o comando:
```
./gradlew test
```