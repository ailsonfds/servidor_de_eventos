# Servidor de Eventos

## Descrição

Implemente um servidor de comunicação baseada em eventos (ou simplesmente servidor de eventos) que permita

1. [X] Um elemento gerar um evento sobre determinado tópico e publicá-lo no servidor de eventos
+ [X] Elementos cadastrarem, no servidor de eventos, o interesse em determinado(s) tópico(s)
+ [X] Toda vez que um evento for publicado no servidor de eventos, todos os elementos interessados devem receber uma notificação

## Atores e ações

+ Atores:
    + servidor
        + eventos
    + elemento (pessoa, orgão, grupo, etc)
        + tópicos de interesse
    + evento
        + tópico -> mais urgente pois é o único nas especificações
        + descrição
+ Ações
    + gerar evento
    + publicar evento
    + agrupar eventos por tópicos
    + cadastrar interesse em tópicos
    + notificar eventos

## Migration to RESTful API

+ Exemplo de endereço para a aplicação: `http://localhost:8080/live_events`

### GET methods

+ lista de eventos ::= `/`
+ detalhes de evento* ::= `/{event_id}`
+ eventos de um tópico ::= `/{topic}`
+ notificações lidos/não lidos ::= `/{user_id}/notifications`
+ eventos de interesse ::= `/{user_id}/topics`

### POST methods

+ notificação lida/não lida ::= `/{user_id}/notify/{event_id}`
+ novo evento ::= `/`
    + JSON Object
        + name
        + topics
        + description
        + author*
        + postTime
        + releaseTime
    + Template:
    ```json
    {
        "name":"",
        "topics":[],
        "description":"",
        "author":"",
        "end":""
    }
    ```
+ adicionar tópicos de interesse ::= `/topics/{topic}`
+ cadastro de usuario* ::= `/signin`
    + JSON Object
        + name
        + username
        + interestTopics
    + Template:
    ```json
    {
        "name":"",
        "username":"",
        "topics":[]
    }
    ```
