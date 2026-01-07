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
* PostgreSQL
* MapStruct (DTO ↔ Entity)
* Lombok
* Auditoria JPA (`@CreatedDate`, `@LastModifiedDate`)

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

---

### 4. Atualizar Autor

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

* 422 — Erro de validação
* 409 — Autor duplicado
* 404 — Autor não encontrado

---

### 5. Remover Autor

**DELETE /autores/{id}**

**Sucesso — 204 No Content**

**Erros**

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

---

### 4. Atualizar Livro

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

* 422 — Erro de validação
* 409 — ISBN duplicado
* 404 — Livro não encontrado
* 400 — Autor inexistente

---

### 5. Remover Livro

**DELETE /livros/{id}**

**Sucesso — 204 No Content**

**Erro**

* 404 — Livro não encontrado
