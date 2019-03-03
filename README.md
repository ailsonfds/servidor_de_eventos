# Servidor de Eventos

## Descrição

Implemente um servidor de comunicação baseada em eventos (ou simplesmente servidor de eventos) que permita

1. [ ] Um elemento gerar um evento sobre determinado tópico e publicá-lo no servidor de eventos
+ [ ] Elementos cadastrarem, no servidor de eventos, o interesse em determinado(s) tópico(s)
+ [ ] Toda vez que um evento for publicado no servidor de eventos, todos os elementos interessados devem receber uma notificação

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