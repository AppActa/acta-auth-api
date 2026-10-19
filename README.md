# 🪪 ACTA Auth API

API de identidade do ACTA para apoiar o ciclo **PDCA** (*Plan, Do, Check, Act*), validando sessões Firebase e conduzindo onboarding, ativação e convites de acesso.

## 📌 Visão geral

A `acta-auth-api` reúne os fluxos de autenticação e identidade usados pelo ecossistema de gestão ACTA. Ela valida Firebase ID Tokens, relaciona uma identidade Firebase ao perfil ACTA correspondente e mantém os cadastros relacionais de empresas, usuários, colaboradores e convites.

O cliente realiza o login pelo Firebase Authentication e envia o Firebase ID Token no cabeçalho `Authorization`. A API não oferece login local nem emite tokens de sessão próprios. Uma identidade Firebase válida pode ainda não ter perfil ACTA; nesse caso, `GET /api/v1/me` informa `CADASTRO_NAO_INICIADO`.

## ✨ Capacidades

| Domínio | Capacidades |
| --- | --- |
| Identidade | Valida Firebase ID Tokens e revogação; consulta o estado do perfil e associa Firebase UID a usuário ACTA. |
| Onboarding | Verifica o CNPJ e orienta o cadastro; cria uma empresa e seu primeiro gestor quando a empresa ainda não está cadastrada. |
| Convites | Cadastra colaboradores pendentes, envia convites por e-mail, reenvia convites pendentes e ativa o cadastro com o código recebido. |
| Saúde | Verifica a conexão com PostgreSQL e informa o estado da API e do banco. |

## 🛠️ Tecnologias

| Tecnologia | Uso |
| --- | --- |
| Java 17 | Linguagem e versão de compilação declarada no Maven. |
| Spring Boot 4.1.1 | Aplicação REST e gerenciamento das versões das dependências Spring. |
| Spring Security | Filtro stateless de autenticação e autorização. |
| Spring Data JPA | Persistência relacional das entidades. |
| PostgreSQL | Banco relacional; o repositório não fixa a versão do servidor. |
| Firebase Admin SDK 9.10.0 | Validação de ID Tokens e consulta de revogação. |
| MapStruct 1.6.3 | Mapeamento entre entidades e DTOs. |
| Springdoc OpenAPI 3.1.0 | Especificação OpenAPI e Swagger UI. |
| Maven Wrapper | Execução dos comandos Maven sem exigir uma instalação local do Maven. |

## ✅ Pré-requisitos e configuração

- JDK 17.
- PostgreSQL acessível e com estrutura compatível com as entidades; o Hibernate usa `ddl-auto=validate` e não cria nem atualiza tabelas.
- Projeto Firebase e credenciais de servidor disponíveis por *Application Default Credentials* (ADC).
- Para criar ou reenviar convites, credenciais e endereço de remetente válidos na API Brevo.

Configure os valores no ambiente do processo que iniciará a aplicação. O Spring Boot não carrega arquivos `.env` automaticamente.

| Variável | Obrigatória | Uso |
| --- | --- | --- |
| `DB_URL` | Sim | URL JDBC do PostgreSQL, por exemplo `jdbc:postgresql://localhost:5432/acta_auth`. |
| `DB_USER` | Sim | Usuário do banco. |
| `DB_PASSWORD` | Sim | Senha do banco. |
| `FIREBASE_PROJECT_ID` | Sim | Identificador do projeto Firebase usado para validar tokens. |
| `CORS_ALLOWED_ORIGINS` | Não | Origens permitidas separadas por vírgula. O padrão é `http://localhost:5173,http://localhost:3000`. |
| `BREVO_API_KEY` | Para envio de convites | Chave usada no cabeçalho `api-key` das chamadas à API Brevo. |
| `BREVO_SENDER_EMAIL` | Para envio de convites | E-mail remetente informado à Brevo. |
| `EXTERNAL_URL` | Não | Base usada para formar a URL do logotipo no e-mail. O padrão é `http://localhost:8080`. |
| `GOOGLE_APPLICATION_CREDENTIALS` | Conforme o ambiente | Caminho para um arquivo de credenciais de servidor quando o ambiente não fornece ADC automaticamente. Não é uma propriedade da aplicação; é reconhecida pelas bibliotecas Google. |

`DB_URL`, `DB_USER`, `DB_PASSWORD` e `FIREBASE_PROJECT_ID` são exigidos pela configuração atual. `BREVO_API_KEY` e `BREVO_SENDER_EMAIL` têm valor padrão vazio, mas precisam estar configurados para o envio de convites funcionar. `EXTERNAL_URL` só compõe o endereço do logotipo no e-mail.

### Banco de dados

O arquivo [`script.sql`](src/main/resources/sql/script.sql) cria os schemas `public`, `pdca` e `auditoria` e suas tabelas. **Atenção: as primeiras instruções removem os três schemas com `DROP SCHEMA ... CASCADE`, apagando os objetos e os dados neles contidos. Execute o comando abaixo somente em um banco descartável ou após confirmar que os dados podem ser destruídos; não o use em bancos que contenham dados a preservar.**

```powershell
psql -h localhost -U "<USUARIO_POSTGRES>" -d "<BANCO_DESCARTAVEL>" -f src/main/resources/sql/script.sql
```

Após preparar a estrutura, `spring.jpa.hibernate.ddl-auto=validate` faz o Hibernate comparar o mapeamento das entidades com o schema existente. Essa configuração não executa o script e não corrige diferenças automaticamente.

### Firebase e e-mail

O SDK Firebase Admin inicializa com `GoogleCredentials.getApplicationDefault()`. Em ambientes Google Cloud, associe uma identidade de serviço com as permissões necessárias; fora deles, disponibilize credenciais por um mecanismo seguro do ambiente ou monte um arquivo fora do repositório e defina `GOOGLE_APPLICATION_CREDENTIALS` com seu caminho. Não inclua chaves no código, no `.env` versionado, na imagem ou nos logs.

O envio de convite chama `https://api.brevo.com/v3/smtp/email` e renderiza [`convite-usuario.html`](src/main/resources/templates/email/convite-usuario.html). `EXTERNAL_URL` é usado para montar o endereço público do logotipo (`/images/acta-logo.png`).

## 🚀 Execução local

No PowerShell, defina a configuração na sessão atual e inicie a aplicação:

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/<BANCO>"
$env:DB_USER = "<USUARIO_POSTGRES>"
$env:DB_PASSWORD = "<SENHA_LOCAL>"
$env:FIREBASE_PROJECT_ID = "<PROJETO_FIREBASE>"
$env:GOOGLE_APPLICATION_CREDENTIALS = "<CAMINHO_SEGURO_DA_CREDENCIAL>"

.\mvnw.cmd spring-boot:run
```

As variáveis `BREVO_API_KEY` e `BREVO_SENDER_EMAIL` também devem ser definidas antes de iniciar a aplicação se você for testar o envio de convites. Para desenvolvimento local, `EXTERNAL_URL` pode ser omitida; o padrão é `http://localhost:8080`. A API escuta na porta `8080`, configurada em [`application.properties`](src/main/resources/application.properties).

## 📚 Documentação e autenticação

Com a API em execução:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI: `http://localhost:8080/v3/api-docs`
- Saúde: `GET /api/v1/health`, sem autenticação.

### Regras de acesso

As rotas de aplicação usam `Authorization: Bearer <FIREBASE_ID_TOKEN>`. O filtro valida assinatura, projeto, validade e revogação do token. Custom token, refresh token, senha e Firebase UID não substituem o Firebase ID Token.

`GET /api/v1/me`, `POST /api/v1/auth/ativar` e as duas rotas de onboarding exigem uma identidade Firebase válida, mas aceitam usuários ainda sem perfil ACTA. O endpoint `/me` retorna `estadoPerfil: "CADASTRO_NAO_INICIADO"` quando não encontra perfil e `"PERFIL_CRIADO"` quando o encontra.

As operações de convite passam pelo fluxo autenticado com perfil ACTA. O filtro exige usuário e empresa ativos e, quando houver colaborador associado, colaborador ativo. Criar ou reenviar convite exige ainda o papel `ADMIN` e que o colaborador ou usuário esteja na empresa do administrador.

## 🔌 Endpoints

| Método e rota | Acesso adicional | Resposta de sucesso |
| --- | --- | --- |
| `GET /api/v1/health` | Público | `200`; `503` quando o banco está indisponível. |
| `GET /api/v1/me` | Firebase ID Token; perfil ACTA não obrigatório | `200`, com o estado da identidade/perfil. |
| `POST /api/v1/auth/ativar` | Firebase ID Token e e-mail verificado correspondente ao convite | `200`, com o perfil ativado. |
| `POST /api/v1/onboarding/inicio` | Firebase ID Token, e-mail verificado e dados do gestor/CNPJ | `200`, com o resultado da consulta e a orientação para a próxima etapa. |
| `POST /api/v1/onboarding/gestor` | Firebase ID Token, e-mail verificado correspondente ao gestor informado | `201`, quando a empresa e o gestor são cadastrados. |
| `POST /api/v1/empresas/me/convites` | Perfil ativo e papel `ADMIN` da empresa | `201`, com os dados do colaborador convidado. |
| `POST /api/v1/colaborador/{idColaborador}/convite` | Perfil ativo, papel `ADMIN` e colaborador pendente da empresa | `204`, sem corpo. |

`/onboarding/inicio` verifica se o CNPJ já possui cadastro. Se já existir uma empresa, a resposta orienta a pessoa a pedir convite a um ADMIN ativo; o endpoint não cria outro cadastro para essa empresa. `POST /api/v1/onboarding/gestor` cria empresa, usuário ADMIN e colaborador inicial quando os dados passam pelas validações.

### Exemplos

Consulta de saúde:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/health"
```

Consulta do perfil associado à identidade Firebase:

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8080/api/v1/me" `
  -Headers @{ Authorization = "Bearer <FIREBASE_ID_TOKEN>" }
```

Início do onboarding, com CNPJ e gestor correspondentes ao e-mail verificado no Firebase:

```powershell
$body = @{
  cnpj = "<CNPJ_COM_14_DIGITOS>"
  gestor = @{
    cpf = "<CPF_COM_11_DIGITOS>"
    nome = "<NOME_DO_GESTOR>"
    cargo = "<CARGO>"
    area = "<AREA>"
    dataNascimento = "<AAAA-MM-DD>"
    dataContratacao = "<AAAA-MM-DD>"
    email = "<EMAIL_VERIFICADO_NO_FIREBASE>"
  }
} | ConvertTo-Json -Depth 4

Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:8080/api/v1/onboarding/inicio" `
  -Headers @{ Authorization = "Bearer <FIREBASE_ID_TOKEN>" } `
  -ContentType "application/json" `
  -Body $body
```

Os placeholders devem ser substituídos por valores válidos do ambiente. O endpoint `/onboarding/gestor` recebe `gestor`, `empresa` e `endereco`; a definição dos campos e enums está no esquema OpenAPI.

## ✉️ Fluxo de convites

Ao convidar um colaborador, um administrador informa os dados validados do colaborador e seu e-mail de acesso. A API cria ou localiza os registros pendentes, gera um código alfanumérico maiúsculo de seis caracteres com `SecureRandom`, persiste somente o hash SHA-256 do código e envia o convite pela Brevo. O código expira em 30 minutos.

Para ativar o cadastro, a pessoa autentica-se pelo Firebase, verifica o e-mail e envia o código em `POST /api/v1/auth/ativar` no corpo `{ "token": "<CODIGO_DO_CONVITE>" }`. A API confere hash, validade, status pendente e correspondência do e-mail; em caso válido, vincula o Firebase UID e ativa usuário, colaborador e convite. O reenvio revoga convites pendentes ainda válidos antes de gerar e enviar outro código.

## ⚠️ Erros

As respostas de erro seguem o formato:

```json
{
  "mensagens": ["Descrição do erro"],
  "httpStatus": 400,
  "timestamp": "2026-10-19T10:35:00"
}
```

| Código | Uso |
| --- | --- |
| `400` | Corpo, parâmetro ou validação inválidos. |
| `401` | Firebase ID Token ausente ou inválido nas rotas protegidas. |
| `403` | Identidade sem acesso ao perfil ou operação solicitada. |
| `404` | Rota ou recurso não encontrado. |
| `405` | Método HTTP não permitido para a rota. |
| `409` | Conflito de dados, como duplicidade ou restrição do banco. |
| `415` | Tipo de conteúdo não suportado. |
| `422` | Regra de negócio não atendida; inclui falha ao enviar e-mail ou carregar o template. |
| `500` | Erro interno inesperado. |
| `503` | Banco indisponível no health check. |

## 🏗️ Arquitetura e estrutura

A aplicação separa os endpoints HTTP, regras de negócio, acesso JPA e mapeamento de DTOs. A validação Firebase ocorre no filtro antes da autorização das rotas protegidas; a autorização adicional de papel e escopo da empresa é aplicada nos serviços.

```text
src/main/java/br/com/acta/
├── common/       # segurança, Firebase, cliente Brevo, configuração e erros
├── controller/   # endpoints REST
├── dto/          # contratos de entrada e saída
├── entity/       # entidades e enums JPA
├── mapper/       # conversões MapStruct
├── repository/   # consultas e persistência JPA
└── service/      # onboarding, identidade, convites e saúde
```

O workflow [`validacao.yml`](.github/workflows/validacao.yml) é acionado em pull requests para `main` e em pushes para `main`. Ele inicia PostgreSQL 16 temporário, executa `script.sql` nesse banco e chama `./mvnw ... clean verify`. Esse comando do workflow executa testes disponíveis no checkout do workflow; a inspeção deste README não executou testes.

O workflow [`build-image.yml`](.github/workflows/build-image.yml) é acionado em pushes para `main`, constrói a imagem usando o `Dockerfile` da raiz, publica-a no GHCR e atualiza `apps/auth-api.yaml` no `acta-platform` com o SHA publicado. A disponibilidade da imagem depende de o workflow concluir e de a credencial de plataforma estar configurada.

## 🤝 Links e autoria

- [Repositório](https://github.com/AppActa/acta-auth-api) · [Licença MIT](LICENSE) · `acta.institutojef@gmail.com`
- Contribuições: use *issues* e *pull requests*; há um [PULL_REQUEST_TEMPLATE.md](PULL_REQUEST_TEMPLATE.md).