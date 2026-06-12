# Guia de Estudo — API Copa do Mundo (AP2)

## O que essa API faz

Gerencia informações da FIFA World Cup. Três entidades principais:

- **Seleção**: país participante com técnico e ranking FIFA
- **Jogador**: atleta vinculado a uma seleção
- **Partida**: jogo entre duas seleções, com data, estádio e placar

---

## Estrutura de pastas — o que cada uma faz

```
entity/       →  representa a tabela no banco. Uma classe = uma tabela.
repository/   →  faz o SQL. Você nunca escreve SELECT/INSERT, o Spring faz.
service/      →  regra de negócio. Ex: "para criar jogador, preciso verificar se a seleção existe".
controller/   →  recebe a requisição HTTP e devolve a resposta. Sem lógica aqui.
dto/          →  objetos de entrada da API (o que o cliente manda no body).
builder/      →  padrão Builder para construção de Partida.
config/       →  configurações do Spring (Swagger/OpenAPI).
exception/    →  tratamento de erros padronizado.
```

**Fluxo de uma requisição:**
```
Cliente HTTP → Controller → Service → Repository → Banco de dados
                         ←          ←             ←
```

---

## As 3 entidades e seus relacionamentos

### Selecao.java
```java
String nomePais       // nome do país
String tecnico        // nome do técnico
Integer rankingFifa   // posição no ranking

@OneToMany            // uma Seleção tem MUITOS Jogadores
List<Jogador> jogadores;

@ManyToMany(mappedBy = "selecoes")  // uma Seleção participa de MUITAS Partidas
List<Partida> partidas;
```

### Jogador.java
```java
String nome
Integer numeroCamisa
String posicao
Integer idade

@ManyToOne            // muitos Jogadores pertencem a UMA Seleção
Selecao selecao;
```

### Partida.java
```java
LocalDate data
String estadio
String fase
String placar

@ManyToMany           // uma Partida tem MUITAS Seleções (normalmente duas)
@JoinTable(name = "partida_selecao")
List<Selecao> selecoes;
```

**No banco de dados isso vira 4 tabelas:**
```
selecoes              jogadores             partidas          partida_selecao
--------              ---------             --------          ---------------
id (PK)               id (PK)               id (PK)           partida_id (FK)
nome_pais             nome                  data              selecao_id (FK)
tecnico               numero_camisa         estadio
ranking_fifa          posicao               fase
                      idade                 placar
                      selecao_id (FK) ──────────────────► selecoes.id
```

---

## Lombok — por que não tem getter/setter no código

Lombok gera código em tempo de compilação automaticamente.

| Anotação | O que gera |
|---|---|
| `@Data` | getter, setter, equals, hashCode, toString |
| `@Builder` | padrão builder: `Selecao.builder().nomePais("Brasil").build()` |
| `@NoArgsConstructor` | construtor vazio (JPA exige) |
| `@AllArgsConstructor` | construtor com todos os campos |
| `@RequiredArgsConstructor` | construtor só com campos `final` (usado nos Services) |

---

## SOLID — como está aplicado aqui

### S — Single Responsibility (cada classe faz uma coisa)
- `Selecao.java` só mapeia o banco. Não tem lógica.
- `SelecaoService.java` só tem regra de negócio.
- `SelecaoController.java` só recebe HTTP e devolve resposta.

### O — Open/Closed (aberto para extensão, fechado para modificação)
Se precisar de um `SelecaoServiceV2`, cria nova classe implementando `ISelecaoService` sem mexer no código existente.

### L — Liskov Substitution
`SelecaoService` implementa `ISelecaoService`. Qualquer lugar que use `ISelecaoService` pode receber `SelecaoService` sem quebrar.

### I — Interface Segregation (interfaces específicas)
Existem 3 interfaces separadas: `ISelecaoService`, `IJogadorService`, `IPartidaService`. Cada uma tem só os métodos do seu contexto.

### D — Dependency Inversion (dependa de abstrações)
```java
// Controller depende da INTERFACE, não da implementação concreta
private final ISelecaoService service;  // ← correto (abstração)

// Se fosse assim, estaria errado:
private final SelecaoService service;   // ← acoplamento direto
```

---

## Padrões de Projeto aplicados

### Builder (GoF — Criacional)
```java
// PartidaBuilder — construção fluente com validação
Partida partida = new PartidaBuilder()
    .naData(LocalDate.of(2026, 6, 15))
    .noEstadio("Maracanã")
    .naFase("Fase de Grupos")
    .comPlacar("2 x 1")
    .build();  // valida campos obrigatórios antes de criar
```
Separar a construção do objeto da sua representação. O `build()` garante que campos obrigatórios foram informados.

### Strategy (GoF — Comportamental)
```java
// As interfaces IService são o padrão Strategy
public interface ISelecaoService {
    List<Selecao> listarTodos();
    Selecao buscarPorId(Long id);
    // ...
}

// O controller não sabe qual implementação está usando
public class SelecaoController {
    private final ISelecaoService service; // estratégia intercambiável
}
```

### Repository (Padrão Arquitetural)
```java
// Você define a interface — Spring gera a implementação completa
public interface SelecaoRepository extends JpaRepository<Selecao, Long> {
    Optional<Selecao> findByNomePais(String nomePais); // SQL gerado automaticamente
}
```

### Singleton (GoF — Criacional)
Todo `@Service`, `@Repository` e `@RestController` é um Singleton. O Spring cria uma única instância e injeta onde necessário. Você nunca escreve `new SelecaoService()`.

---

## Injeção de Dependência — como o Spring conecta tudo

```java
@RestController
@RequiredArgsConstructor              // Lombok gera construtor com campos final
public class SelecaoController {

    private final ISelecaoService service; // Spring injeta automaticamente
}
```

O Spring vê que `SelecaoController` precisa de `ISelecaoService`, encontra `SelecaoService` (que implementa essa interface), e injeta. Zero `new`.

---

## Endpoints disponíveis

### Seleções
```
GET    /selecoes              → listar todas
GET    /selecoes/{id}         → buscar por id
POST   /selecoes              → cadastrar (body: SelecaoRequest)
PUT    /selecoes/{id}         → atualizar
DELETE /selecoes/{id}         → remover
```

### Jogadores
```
GET    /jogadores                    → listar todos
GET    /jogadores/selecao/{id}       → listar jogadores de uma seleção
GET    /jogadores/{id}               → buscar por id
POST   /jogadores/selecao/{id}       → cadastrar jogador na seleção
PUT    /jogadores/{id}               → atualizar
DELETE /jogadores/{id}               → remover
```

### Partidas
```
GET    /partidas                          → listar todas
GET    /partidas/{id}                     → buscar por id
POST   /partidas                          → cadastrar
PUT    /partidas/{id}                     → atualizar
POST   /partidas/{id}/selecoes/{id}       → adicionar seleção à partida
DELETE /partidas/{id}/selecoes/{id}       → remover seleção da partida
DELETE /partidas/{id}                     → remover
```

---

## Testando pelo Swagger

Acesse: `http://localhost:8080/swagger-ui/index.html` (local) ou `https://sua-url.railway.app/swagger-ui/index.html`

Os campos já vêm pré-preenchidos com exemplos reais. Fluxo de teste completo:

1. `POST /selecoes` → criar Brasil (`nomePais: "Brasil"`, `tecnico: "Dorival Júnior"`, `rankingFifa: 5`)
2. `POST /selecoes` → criar Argentina (`nomePais: "Argentina"`, `tecnico: "Lionel Scaloni"`, `rankingFifa: 1`)
3. `POST /jogadores/selecao/1` → criar Vinicius Jr. na seleção 1
4. `POST /partidas` → criar partida (`data: "2026-06-15"`, `estadio: "Maracanã"`, `fase: "Fase de Grupos"`)
5. `POST /partidas/1/selecoes/1` → adicionar Brasil à partida
6. `POST /partidas/1/selecoes/2` → adicionar Argentina à partida
7. `GET /jogadores/selecao/1` → ver jogadores do Brasil
8. `GET /partidas/1` → ver partida com as duas seleções

---

## Perguntas que o professor pode fazer

**"O que é JPA?"**
JPA (Jakarta Persistence API) é uma especificação para mapear classes Java em tabelas do banco. Com `@Entity` você diz "essa classe é uma tabela" e o Hibernate cuida do SQL.

**"Por que usar interface no Service se só tem uma implementação?"**
SOLID — Dependency Inversion. O Controller depende de um contrato (interface), não de uma classe concreta. Facilita testes e extensão futura sem alterar o código existente.

**"O que é `@RestController`?"**
É `@Controller` + `@ResponseBody`. Todos os métodos retornam JSON direto, não uma página HTML.

**"Como funciona o `@ManyToMany`?"**
O JPA cria automaticamente a tabela de junção `partida_selecao` com duas colunas: `partida_id` e `selecao_id`. Cada linha representa uma seleção participando de uma partida.

**"O que é Lombok?"**
Biblioteca que gera código em tempo de compilação. `@Data` gera getters/setters, `@Builder` gera o padrão Builder, `@RequiredArgsConstructor` gera construtor — eliminando código repetitivo.

**"Explique o padrão Builder."**
Builder separa a construção de um objeto complexo da sua representação. O `PartidaBuilder` permite construir uma `Partida` passo a passo com API fluente (`.naData(...).noEstadio(...).build()`), validando os campos obrigatórios no `build()` antes de criar o objeto.

**"Explique o padrão Strategy."**
Strategy define uma família de algoritmos (ou comportamentos), encapsula cada um em uma interface e os torna intercambiáveis. Aqui, `ISelecaoService`, `IJogadorService` e `IPartidaService` são as estratégias — os controllers dependem das interfaces, não das implementações, permitindo trocar o comportamento sem alterar quem usa.

**"O que é `ddl-auto=update`?"**
O Hibernate olha as entidades e cria/atualiza as tabelas no banco automaticamente ao iniciar. Em produção real usaria migrations, mas para desenvolvimento é suficiente.

**"O que é Swagger?"**
Swagger (OpenAPI) gera documentação interativa da API automaticamente a partir das anotações no código. Permite visualizar e testar todos os endpoints diretamente pelo navegador.
