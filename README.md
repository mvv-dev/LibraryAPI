# LibraryAPI

API REST para gerenciamento de uma livraria, desenvolvida com **Spring Boot** e **PostgreSQL**.

O projeto contempla dois módulos principais:

* **Autores**
* **Livros**

Toda a API segue boas práticas REST, com validações de negócio, códigos HTTP apropriados e padronização de respostas de erro.

---

## Tecnologias

* Java 17
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Security
* JWT (Auth0 Java JWT)
* PostgreSQL
* MapStruct (DTO ↔ Entity)
* Lombok
* Auditoria JPA (`@CreatedDate`, `@LastModifiedDate`)

---

## 🔐 Segurança

A API está protegida com **Spring Security** usando **autenticação local (login/senha)** e **autorização via token JWT (stateless)**.

### Como funciona (visão prática)

1. O usuário envia `login` e `password` em `POST /auth/login`
2. Se as credenciais estiverem corretas, a API retorna um **JWT**
3. Nas rotas protegidas, o cliente envia o token no header:

```http
Authorization: Bearer <token>
```

4. A cada requisição, um **Security Filter** valida o token e popula o `SecurityContext` (usuário + authorities/roles)
5. As permissões (roles) são aplicadas conforme as regras abaixo

### Roles e regras de negócio

Roles existentes:

* `GERENTE`
* `OPERADOR`

Regras:

* **Somente GERENTE** pode: **cadastrar/atualizar/remover Autores** e **cadastrar usuários**
* **GERENTE e OPERADOR** podem: **cadastrar/atualizar/remover Livros**
* **Ambos** podem consultar (GET) Autores e Livros (desde que autenticados)

Resumo por endpoint:

**Autenticação**
- `POST /auth/login` — público
- `POST /auth/register` — **apenas GERENTE** (rota protegida)

**Autores**
- `POST /autores` — **GERENTE**
- `PUT /autores/{id}` — **GERENTE**
- `DELETE /autores/{id}` — **GERENTE**
- `GET /autores` e `GET /autores/{id}` — autenticado (GERENTE/OPERADOR)

**Livros**
- `POST /livros` — **GERENTE/OPERADOR**
- `PUT /livros/{id}` — **GERENTE/OPERADOR**
- `DELETE /livros/{id}` — **GERENTE/OPERADOR**
- `GET /livros` e `GET /livros/{id}` — autenticado (GERENTE/OPERADOR)

---

## Como executar o projeto (local)

### Pré-requisitos

* Java 17+
* Maven
* PostgreSQL rodando localmente

---

# Contratos da API

## Padrão de Erro

```json
{
  "status": 422,
  "mensagem": "Erro de validação",
  "erros": [
    { "campo": "titulo", "mensagem": "Campo obrigatório" }
  ]
}
```

---

# Autenticação

## Login

**POST /auth/login**

```json
{
  "login": "string",
  "password": "string"
}
```

**Sucesso — 200 OK**

```json
{
  "token": "jwt-token"
}
```

**Erros**

* 401 — Credenciais inválidas / usuário inexistente
* 422 — Erro de validação

---

## Cadastro de Usuário

> **Somente GERENTE** pode cadastrar usuários (rota protegida).

**POST /auth/register**

```json
{
  "login": "string",
  "password": "string",
  "role": "GERENTE | OPERADOR"
}
```

**Sucesso — 200 OK**

**Erros**

* 401 — Não autenticado
* 403 — Sem permissão
* 422 — Erro de validação
* 409 — Login já existente

---

## Módulo: Autor

### Regras de Negócio

* Não permitir cadastro de autores duplicados (nome, data de nascimento e nacionalidade)
* Não permitir exclusão de autor que possua livros cadastrados
* Não permitir data de nascimento futura

### Campos

**Campos de Negócio**

* nome *
* dataNascimento *
* nacionalidade *

**Campos de Controle/Auditoria**

* id (UUID)
* dataCadastro
* dataUltimaAtualizacao
* usuarioUltimaAtualizacao

---

### 1. Cadastrar Autor

> **Permissão:** apenas `GERENTE`

**POST /autores**

```json
{
  "nome": "string",
  "dataNascimento": "yyyy-MM-dd",
  "nacionalidade": "string"
}
```

**Sucesso — 201 Created**

* Header: `Location`

```json
{
  "id": "uuid",
  "nome": "string",
  "dataNascimento": "yyyy-MM-dd",
  "nacionalidade": "string",
  "dataCadastro": "yyyy-MM-dd'T'HH:mm:ss"
}
```

**Erros**

* 401 — Não autenticado
* 403 — Sem permissão
* 422 — Erro de validação
* 409 — Autor duplicado

---

### 2. Buscar Autor por ID

**GET /autores/{id}**

**Sucesso — 200 OK**

```json
{
  "id": "uuid",
  "nome": "string",
  "dataNascimento": "yyyy-MM-dd",
  "nacionalidade": "string",
  "dataCadastro": "yyyy-MM-dd'T'HH:mm:ss"
}
```

**Erro**

* 401 — Não autenticado
* 404 — Autor não encontrado

---

### 3. Buscar Autores (Filtros opcionais)

**GET /autores?nome=&nacionalidade=**

**Sucesso — 200 OK**

```json
[
  {
    "id": "uuid",
    "nome": "string",
    "dataNascimento": "yyyy-MM-dd",
    "nacionalidade": "string",
    "dataCadastro": "yyyy-MM-dd'T'HH:mm:ss"
  }
]
```

**Erros**

* 401 — Não autenticado

---

### 4. Atualizar Autor

> **Permissão:** apenas `GERENTE`

**PUT /autores/{id}**

```json
{
  "nome": "string",
  "dataNascimento": "yyyy-MM-dd",
  "nacionalidade": "string"
}
```

**Sucesso — 204 No Content**

**Erros**

* 401 — Não autenticado
* 403 — Sem permissão
* 422 — Erro de validação
* 409 — Autor duplicado
* 404 — Autor não encontrado

---

### 5. Remover Autor

> **Permissão:** apenas `GERENTE`

**DELETE /autores/{id}**

**Sucesso — 204 No Content**

**Erros**

* 401 — Não autenticado
* 403 — Sem permissão
* 404 — Autor não encontrado
* 409 — Autor possui livros cadastrados

---

## Módulo: Livro

### Regras de Negócio

* Não permitir cadastro de livro com ISBN duplicado
* Não permitir cadastro ou atualização com autor inexistente
* Não permitir data de publicação futura
* Gêneros permitidos:

  * FICCAO, FANTASIA, MISTERIO, ROMANCE, BIOGRAFIA, CIENCIA, AVENTURA, TERROR, RELIGIOSO, AUTOAJUDA

### Campos

**Campos de Negócio**

* isbn *
* titulo *
* dataPublicacao *
* genero
* preco
* autor *

**Campos de Controle/Auditoria**

* id (UUID)
* dataCadastro
* dataUltimaAtualizacao
* usuarioUltimaAtualizacao

---

### 1. Cadastrar Livro

> **Permissão:** `GERENTE` e `OPERADOR`

**POST /livros**

```json
{
  "isbn": "string",
  "titulo": "string",
  "dataPublicacao": "yyyy-MM-dd",
  "genero": "ENUM",
  "preco": 99.90,
  "id_autor": "uuid"
}
```

**Sucesso — 201 Created**

* Header: `Location`

```json
{
  "id": "uuid",
  "isbn": "string",
  "titulo": "string",
  "dataPublicacao": "yyyy-MM-dd",
  "genero": "ENUM",
  "preco": 99.90,
  "autor": {
    "nome": "string",
    "dataNascimento": "yyyy-MM-dd",
    "nacionalidade": "string"
  },
  "dataCadastro": "yyyy-MM-dd'T'HH:mm:ss"
}
```

**Erros**

* 401 — Não autenticado
* 403 — Sem permissão
* 422 — Erro de validação
* 409 — ISBN duplicado
* 400 — Autor inexistente

---

### 2. Buscar Livro por ID

**GET /livros/{id}**

**Sucesso — 200 OK**

```json
{
  "id": "uuid",
  "isbn": "string",
  "titulo": "string",
  "dataPublicacao": "yyyy-MM-dd",
  "genero": "ENUM",
  "preco": 99.90,
  "autor": {
    "nome": "string",
    "dataNascimento": "yyyy-MM-dd",
    "nacionalidade": "string"
  },
  "dataCadastro": "yyyy-MM-dd'T'HH:mm:ss"
}
```

**Erro**

* 401 — Não autenticado
* 404 — Livro não encontrado

---

### 3. Buscar Livros (Filtros opcionais)

**GET /livros?isbn=&titulo=&nome_autor=&ano_publicacao=&genero=**

**Sucesso — 200 OK**

```json
[
  {
    "id": "uuid",
    "isbn": "string",
    "titulo": "string",
    "dataPublicacao": "yyyy-MM-dd",
    "genero": "ENUM",
    "preco": 99.90,
    "autor": {
      "nome": "string",
      "dataNascimento": "yyyy-MM-dd",
      "nacionalidade": "string"
    },
    "dataCadastro": "yyyy-MM-dd'T'HH:mm:ss"
  }
]
```

**Erros**

* 401 — Não autenticado

---

### 4. Atualizar Livro

> **Permissão:** `GERENTE` e `OPERADOR`

**PUT /livros/{id}**

```json
{
  "isbn": "string",
  "titulo": "string",
  "dataPublicacao": "yyyy-MM-dd",
  "genero": "ENUM",
  "preco": 99.90,
  "id_autor": "uuid"
}
```

**Sucesso — 204 No Content**

**Erros**

* 401 — Não autenticado
* 403 — Sem permissão
* 422 — Erro de validação
* 409 — ISBN duplicado
* 404 — Livro não encontrado
* 400 — Autor inexistente

---

### 5. Remover Livro

> **Permissão:** `GERENTE` e `OPERADOR`

**DELETE /livros/{id}**

**Sucesso — 204 No Content**

**Erro**

* 401 — Não autenticado
* 403 — Sem permissão
* 404 — Livro não encontrado
