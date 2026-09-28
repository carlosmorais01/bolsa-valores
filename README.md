# stocknotify

API que simula um sistema de notificação de bolsa de valores para uma empresa que opera na **NASDAQ** e na **BOVESPA**.

## O problema

- Sempre que uma bolsa **sobe**, todos os clientes da empresa devem ser notificados.
- Sempre que uma bolsa **cai**, somente os clientes **premium** devem ser notificados.
- NASDAQ e BOVESPA têm subsistemas remotos próprios, com formatos de dados diferentes.
- A integração entre esses subsistemas e o domínio comum deve ser baseada em um padrão de projeto.
- Algum ponto da solução deve ser configurável em tempo de execução, sem reiniciar a aplicação.

## Padrões de projeto usados

### Observer — quem é notificado

`model/observer/`

- `Subject` — contrato de quem pode ser inscrito/desinscrito (`subscribe`, `unsubscribe`, `isSubscribed`).
- `Observer` — contrato de quem recebe notificações (`update`).
- `Bolsa` — o *Subject* concreto (classe abstrata). Mantém a lista de clientes inscritos e decide, a cada evento, quem deve ser notificado.
- `Cliente` — o *Observer* concreto. Guarda o histórico de notificações recebidas.

### Strategy — a regra de quem é notificado em cada direção

`model/strategy/`

- `Strategy` — contrato (`deveNotificar(Observer o)`).
- `PoliticaNotificarTodos` — sempre retorna `true` (usada por padrão na alta).
- `PoliticaNotificarSomentePremium` — só retorna `true` se o observer for `CientePremium` e `ehPremium()` (usada por padrão na baixa).

`Bolsa` mantém um `Map<Direcao, Strategy>` e consulta a estratégia certa antes de notificar cada cliente — é esse mapa que permite trocar a regra em runtime.

### Adapter — integração com cada bolsa

`adapter/nasdaq/` e `adapter/bovespa/`

Cada bolsa expõe um formato de evento diferente (simulando protocolos remotos distintos):

| | NASDAQ | BOVESPA |
|---|---|---|
| Formato recebido | preço antigo e novo | variação percentual direta |
| DTO | `NasdaqEventoRequest` | `BovespaEventoRequest` |
| Callback interno | `OuvinteNasdaq` | `RetornoBovespa` |
| Sistema remoto simulado | `SistemaRemotoNasdaq` | `SistemaRemotoBovespa` |
| Adapter | `AdaptadorBolsaNasdaq` | `AdaptadorBolsaBovespa` |

`AdaptadorBolsaNasdaq extends Bolsa implements OuvinteNasdaq` (o mesmo vale para a Bovespa): a classe **é** uma `Bolsa` (Observer) e ao mesmo tempo **adapta** o callback específico daquele sistema remoto, convertendo `precoAntigo/precoNovo` ou `variacaoPercentual` para o `EventoMercado` comum antes de publicar para os observers.

## Configuração em tempo de execução

```
PUT /api/bolsas/{bolsaId}/politica
{ "direcao": "ALTA", "tipo": "SOMENTE_PREMIUM" }
```

Troca a `Strategy` usada por uma bolsa para uma direção específica (`ALTA`/`BAIXA`), sem reiniciar a aplicação. `TipoPolitica` (`TODOS` ou `SOMENTE_PREMIUM`) decide qual implementação concreta de `Strategy` é instanciada.

## Estrutura de pacotes

```
com.ao.depress.stocknotify/
  controller/     REST controllers
  service/        ClienteService, BolsaService, BolsaRegistry
  repository/     ClienteRepository (interface) + InMemoryClienteRepository
  adapter/        Adapter pattern: nasdaq/ e bovespa/
  model/          Direcao, EventoMercado, Notificacao
    observer/     Observer pattern
    strategy/     Strategy pattern
  dto/            Request/response da API
  exception/      Exceções de domínio + tratamento global (404)
  config/         Configuração Spring (wiring dos beans)
```

## Rodando

```
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`. Documentação interativa (Swagger UI) em `http://localhost:8080/swagger-ui/index.html`.

## Endpoints

| Verbo | Rota | O que faz |
|---|---|---|
| `POST` | `/api/bolsas/nasdaq/eventos` | Recebe evento de variação de preço da NASDAQ |
| `POST` | `/api/bolsas/bovespa/eventos` | Recebe evento de variação percentual da BOVESPA |
| `GET` | `/api/bolsas` | Lista as bolsas registradas (id + nome) |
| `POST` | `/api/bolsas/{bolsaId}/clientes/{clienteId}` | Inscreve um cliente numa bolsa |
| `DELETE` | `/api/bolsas/{bolsaId}/clientes/{clienteId}` | Desinscreve um cliente de uma bolsa |
| `PUT` | `/api/bolsas/{bolsaId}/politica` | Troca a política de notificação em runtime |
| `POST` | `/api/clientes` | Cadastra um cliente (já inscrito em todas as bolsas) |
| `GET` | `/api/clientes` | Lista os clientes cadastrados |
| `GET` | `/api/clientes/{id}/notificacoes` | Notificações recebidas por um cliente |
| `GET` | `/api/clientes/{id}/bolsas` | Bolsas em que um cliente está inscrito |

## Exemplo de ponta a ponta

```bash
# cadastra um cliente comum e um premium (ambos ficam inscritos em nasdaq e bovespa)
curl -X POST localhost:8080/api/clientes -H 'Content-Type: application/json' \
  -d '{"nome":"Carlos","premium":false}'
curl -X POST localhost:8080/api/clientes -H 'Content-Type: application/json' \
  -d '{"nome":"Ana","premium":true}'

# NASDAQ sobe -> os dois recebem notificação
curl -X POST localhost:8080/api/bolsas/nasdaq/eventos -H 'Content-Type: application/json' \
  -d '{"simbolo":"AAPL","precoAntigo":150.0,"precoNovo":155.0}'

# BOVESPA cai -> só a Ana (premium) recebe
curl -X POST localhost:8080/api/bolsas/bovespa/eventos -H 'Content-Type: application/json' \
  -d '{"ativo":"PETR4","variacaoPercentual":-1.2}'

# reconfigura em runtime: NASDAQ passa a notificar só premium mesmo na alta
curl -X PUT localhost:8080/api/bolsas/{bolsaId-da-nasdaq}/politica -H 'Content-Type: application/json' \
  -d '{"direcao":"ALTA","tipo":"SOMENTE_PREMIUM"}'
```
