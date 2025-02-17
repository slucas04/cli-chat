# CLI Chat 💬

Este projeto implementa um **chat via terminal** utilizando **Java** e **sockets**, permitindo a comunicação entre múltiplos clientes em um servidor central. O sistema suporta mensagens públicas, mensagens privadas e comandos especiais para interação. Implementado com intuito de estudar **sockets**, **threads** e aplicações com **command line interface**.

---

## 🛠️ Tecnologias utilizadas

- **Java:** Linguagem de programação utilizada para implementar o chat.
- **Sockets:** Permite conexão e troca de mensagens entre clientes e servidor.
- **Threads:** Garante o funcionamento simultâneo de múltiplos clientes.
- **JANSI:** Biblioteca usada para colorir os nomes dos usuários no terminal.

---

## ⬆️ Funcionamento

### Teste com quatro clientes conectados à porta 12345
  ![funcionamento](https://github.com/user-attachments/assets/12077d04-0009-4019-9cda-db574fb8f2f7)

---

## 🚀 Principais funcionalidades

1. **Servidor (`ServidorChat.java`)**
   - Inicia na porta `12345` e aguarda conexões de clientes.
   - Para cada cliente que se conecta, cria uma nova thread com `ClienteService`.
   - Exibe no terminal quando novos clientes entram ou saem.

2. **Cliente (`ClienteChat.java`)**
   - Conecta-se ao servidor e permite enviar mensagens.
   - Garante que o nome do usuário seja único.
   - Exibe mensagens recebidas de outros clientes em tempo real.
   - Suporte a mensagens coloridas com JANSI.

3. **Gerenciamento de clientes (`ClienteService.java`)**
   - Atribui um nome de usuário e uma cor aleatória para cada cliente.
   - Mantém uma lista de clientes conectados para envio de mensagens.
   - Implementa comandos especiais:
     - `/privado <usuario> <mensagem>`: Envia uma mensagem privada.
     - `/sair`: Desconecta o cliente do chat.
     - `/help`: Exibe a lista de comandos disponíveis.

---

## ▶️ Como executar o projeto

### 1️⃣ Iniciar o servidor:
```
javac ServidorChat.java ClienteService.java
java ServidorChat
```

### 2️⃣ Iniciar um cliente:
```
mvn exec:java
```
(Repita para abrir múltiplos clientes e testar a interação.)

Sinta-se à vontade para explorar o código, contribuir e sugerir melhorias! 😊
