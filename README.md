# jokenpo-five

Aplicação para jogar joken po com a seguintes regras:

![regras](/resources/regras.jpg)

## Jogadas

A aplicação serve o recurso ```/match``` para iniciação das jogadas com o formato json através pelo método ```POST```.

Ex: 
```json
{
    "name": "Jogador 1",
    "play": "PEDRA"
}
```

## Listagem de jogadas (play)
* PEDRA
* PAPEL
* TESOURA
* SPOCK
* LAGARTO

## Concluindo rodada

Para concluir a rodada basta chamar o recurso ```/match/play``` com o método ```GET```. Ele trará o ganhador da partida e iniciará por padrão outra partida. Aceitando novamente através do recurso ```/match```
