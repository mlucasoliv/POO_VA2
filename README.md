# POO2 - Sistema de Usuários e Transações

Projeto de **Programação Orientada a Objetos (POO)** em Java, com persistência de dados em **PostgreSQL**, que gerencia usuários e transações de tokens.

---

## Integrantes do Grupo

1. Marcelo Lucas de Oliveira Lima
2. Jullyane Maria Dué de Souza
3. Rafael Silver
4. Pedro Tavares

---

## 💡 Descrição

Este projeto tem como objetivo criar um sistema simples de cadastro de usuários, controle de tokens e registro de transações, utilizando conceitos de **POO**, **DAO** e **JDBC**.

Funcionalidades principais:

- Cadastrar, listar e consultar usuários.
- Creditar, debitar e transferir tokens entre usuários.
- Persistência de dados em banco PostgreSQL.

---

## ⚙ Pré-requisitos

- Java 22
- PostgreSQL instalado e rodando
- Driver JDBC do PostgreSQL (`postgresql-42.7.8.jar` ou similar)

---

## 🛠 Configuração do banco de dados

### 1. Crie o banco de dados:

`CREATE DATABASE database;`

 ### 2. Crie as tabelas necessárias:
   
    -- Tabela de usuários
    CREATE TABLE usuario (
        id SERIAL PRIMARY KEY,
        nome VARCHAR(100) NOT NULL,
        tipo VARCHAR(50) NOT NULL
    );

    -- Tabela de tokens
    CREATE TABLE token (
        id SERIAL PRIMARY KEY,
        saldo NUMERIC(10,2) DEFAULT 0,
        usuario_id INT NOT NULL,
        FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
    ); 

    -- Tabela de transações
    CREATE TABLE transacao (
        id SERIAL PRIMARY KEY,
        valor NUMERIC(10,2) NOT NULL,
        datahora TIMESTAMP NOT NULL DEFAULT NOW(),
        token_id INT NOT NULL,
        FOREIGN KEY (token_id) REFERENCES token(id) ON DELETE CASCADE
    );

### 3. Configure a classe Conexao.java com usuário, senha e nome do banco:

    private static final String URL = "jdbc:postgresql://localhost:5432/database";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

## 🚀 Executando o projeto

- Compilando os arquivos:

    javac -cp ".;caminho/do/postgresql-42.7.8.jar" src/**/*.java

- Executando o projeto:

    java -cp ".;caminho/do/postgresql-42.7.8.jar" app.Main

