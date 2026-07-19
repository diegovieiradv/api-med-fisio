# API Med Fisio (Voll.med)

API RESTful para gerenciamento de clinica medica - projeto Voll.med do Oracle Next Education (ONE).

## Descricao

API desenvolvida durante o curso de Spring Boot da Alura/ONE. Gerencia medicos, pacientes e agendamento de consultas com validacoes de negocio, autenticacao JWT e documentacao OpenAPI.

## Funcionalidades

- Autenticacao JWT com expiracao de 2 horas
- CRUD de Medicos com especialidades (Ortopedia, Cardiologia, Ginecologia, Dermatologia)
- CRUD de Pacientes com validacao de CPF
- Agendamento e cancelamento de consultas
- Validacao de horario de funcionamento (segunda a sabado, 7h-18h)
- Validacao de antecedencia minima (30 min para agendar, 24h para cancelar)
- Verificacao de disponibilidade de medico/paciente
- Endereco via API ViaCEP
- Documentacao Swagger/OpenAPI

## Tecnologias

- **Linguagem:** Java 17
- **Framework:** Spring Boot 3.0.0
- **Banco de Dados:** MySQL (Flyway para migrations)
- **Seguranca:** Spring Security + JWT
- **Validacao:** Jakarta Bean Validation + Validadores customizados (@CPF, @Cep)
- **Monitoramento:** Spring Boot Actuator
- **Build:** Maven

## Como Rodar

```bash
# Crie o banco de dados MySQL
CREATE DATABASE med_fisio;

# Execute
./mvnw spring-boot:run

# Ou com Maven global
mvn spring-boot:run
```

## Endpoints

| Metodo | Rota | Descricao |
|--------|------|-----------|
| POST | /login | Autenticacao JWT |
| POST | /usuarios | Cadastrar usuario |
| GET/POST/PUT/DELETE | /medicos | CRUD medicos |
| GET/POST/PUT/DELETE | /pacientes | CRUD pacientes |
| POST | /consultas | Agendar consulta |
| DELETE | /consultas | Cancelar consulta |
| GET | /swagger-ui.html | Documentacao API |

## Arquitetura

```
src/main/java/api/med/fisio/
├── controller/          # Controllers REST
├── domain/              # Entidades, repositorios, servicos
│   ├── medico/
│   ├── paciente/
│   ├── consulta/
│   │   └── validacoes/  # Validadores de agendamento/cancelamento
│   ├── endereco/
│   └── usuario/
└── infra/
    ├── security/        # JWT, filtros de seguranca
    ├── exception/       # Tratamento global de erros
    └── springdoc/       # Configuracao OpenAPI
```

## Licenca

MIT License - Diego Vieira
