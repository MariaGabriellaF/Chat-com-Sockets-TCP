# Chat-com-Sockets-TCP em java

Este projeto é uma continuação da **Atividade 1** da disciplina de **Sistemas Distribuídos** (P6/UFPB).  
Na atividade original, foi implementada uma comunicação básica entre cliente e servidor usando Sockets TCP.  
Neste trabalho, o código foi modificado para se tornar um **bate-papo (chat) em linha de comando**, com **suporte a múltiplos usuários**.

---

## ✅ O que foi feito

- O servidor agora aceita **vários clientes simultaneamente**, utilizando **threads**.
- Mensagens de um cliente são **enviadas para todos os outros** (broadcast).
- O **servidor também pode participar da conversa**, digitando mensagens via terminal.
- A comunicação é feita usando **Sockets TCP** com `DataInputStream` e `DataOutputStream`.

---

- `MultiClientServer.java`: Servidor principal. Escuta clientes e faz broadcast das mensagens.
- `ChatClient.java`: Cliente simples que envia e recebe mensagens em tempo real.

---
