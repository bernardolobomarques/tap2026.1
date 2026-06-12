# Guia de Estudo — AP2: API Copa do Mundo

## Visão Geral do Sistema

**O que é?** Uma API REST que gerencia dados da FIFA World Cup: seleções, jogadores e partidas.

**Como funciona?** O cliente (Postman, Swagger, front-end) manda requisições HTTP. A API processa e persiste no banco MySQL.

```
[Cliente / Swagger] → HTTP → [Spring Boot API] → [MySQL]
                    ←       ←                  ←
```

---

## Estrutura de Pacotes (mostre essa pasta pro professor)

```
com.ibmec.api/
├── config/         → configurações (Swagger)
├── controller/     → recebe requisições HTTP, devolve resposta
├── service/        → regras de negócio
├── repository/     → acesso ao banco de dados
├── entity/         → classes que viram tabelas no banco
├── dto/            → objetos que o cliente manda no body
├── builder/        → padrão Builder para criar Partida
└── exception/      → tratamento de erros
```

**O que falar:** *"Seguimos a arquitetura em camadas. Cada pacote tem uma responsabilidade única — isso é o princípio S do SOLID."*

---

## As 3 Entidades

### Selecao.java
```java
@Entity
@Table(name = "selecoes")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Selecao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomePais;   // ex: "Brasil"
    private String tecnico;    // ex: "Dorival Júnior"
    private Integer rankingFifa; // ex: 5

    @OneToMany(mappedBy = "selecao")  // 1:N → uma seleção tem muitos jogadores
    private List<Jogador> jogadores;

    @ManyToMany(mappedBy = "selecoes") // N:N → uma seleção participa de muitas partidas
    private List<Partida> partidas;
}
```

### Jogador.java
```java
@Entity
@Table(name = "jogadores")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Jogador {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;          // ex: "Vinicius Jr."
    private Integer numeroCamisa; // ex: 7
    private String posicao;       // ex: "Atacante"
    private Integer idade;        // ex: 24

    @ManyToOne                    // N:1 → muitos jogadores pertencem a uma seleção
    @JoinColumn(name = "selecao_id")
    private Selecao selecao;
}
```

### Partida.java
```java
@Entity
@Table(name = "partidas")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Partida {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;    // ex: 2026-06-15
    private String estadio;    // ex: "Maracanã"
    private String fase;       // ex: "Fase de Grupos"
    private String placar;     // ex: "2 x 1"

    @ManyToMany                // N:N → uma partida tem duas seleções
    @JoinTable(
        name = "partida_selecao",        // tabela de junção criada automaticamente
        joinColumns = @JoinColumn(name = "partida_id"),
        inverseJoinColumns = @JoinColumn(name = "selecao_id")
    )
    private List<Selecao> selecoes;
}
```

---

## Banco de Dados — 4 Tabelas Criadas Automaticamente

```
selecoes              jogadores             partidas          partida_selecao
--------              ---------             --------          ---------------
id (PK)               id (PK)               id (PK)           partida_id  (FK)
nome_pais             nome                  data              selecao_id  (FK)
tecnico               numero_camisa         estadio
ranking_fifa          posicao               fase
                      idade                 placar
                      selecao_id (FK)──►selecoes.id
```

O Hibernate cria essas tabelas automaticamente pela config `spring.jpa.hibernate.ddl-auto=update`. Você não escreve nenhum `CREATE TABLE`.

---

## Repositories — Spring faz o SQL por você

```java
// Você só escreve a interface — Spring gera todo o SQL automaticamente
public interface SelecaoRepository extends JpaRepository<Selecao, Long> {
    Optional<Selecao> findByNomePais(String nomePais); // gera: SELECT * FROM selecoes WHERE nome_pais = ?
}

public interface JogadorRepository extends JpaRepository<Jogador, Long> {
    List<Jogador> findBySelecaoId(Long selecaoId); // gera: SELECT * FROM jogadores WHERE selecao_id = ?
}
```

**O que falar:** *"Usamos o padrão Repository. O Spring Data JPA implementa automaticamente os métodos com base no nome — `findBySelecaoId` vira um SELECT com WHERE."*

---

## Services — Onde ficam as regras de negócio

Cada entidade tem uma **interface** e uma **implementação**. Isso é o princípio D do SOLID (Dependency Inversion).

```java
// Interface define o CONTRATO (o que o service faz)
public interface ISelecaoService {
    List<Selecao> listarTodos();
    Selecao buscarPorId(Long id);
    Selecao salvar(Selecao selecao);
    Selecao atualizar(Long id, Selecao selecao);
    void deletar(Long id);
}

// Implementação define o COMO
@Service
@RequiredArgsConstructor
public class SelecaoService implements ISelecaoService {

    private final SelecaoRepository repository;

    @Override
    public Selecao buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seleção", id));
        // Se não achar, lança exceção → controller devolve 404 automaticamente
    }

    @Override
    public Selecao atualizar(Long id, Selecao dados) {
        Selecao existente = buscarPorId(id);  // valida se existe
        existente.setNomePais(dados.getNomePais());
        existente.setTecnico(dados.getTecnico());
        existente.setRankingFifa(dados.getRankingFifa());
        return repository.save(existente);
    }
    // ...
}
```

**JogadorService** tem uma dependência a mais:
```java
@Service
@RequiredArgsConstructor
public class JogadorService implements IJogadorService {

    private final JogadorRepository repository;
    private final ISelecaoService selecaoService; // depende da INTERFACE, não da classe concreta

    @Override
    public Jogador salvar(Long selecaoId, Jogador jogador) {
        Selecao selecao = selecaoService.buscarPorId(selecaoId); // valida se seleção existe
        jogador.setSelecao(selecao);
        return repository.save(jogador);
    }
}
```

---

## Controllers — Recebem HTTP, chamam Service, devolvem resposta

```java
@RestController
@RequestMapping("/selecoes")
@RequiredArgsConstructor
@Tag(name = "Seleções") // anotação Swagger
public class SelecaoController {

    private final ISelecaoService service; // depende da INTERFACE (SOLID - DIP)

    @GetMapping
    @Operation(summary = "Listar todas as seleções")
    public List<Selecao> listar() {
        return service.listarTodos();
    }

    @PostMapping
    @Operation(summary = "Cadastrar nova seleção")
    public ResponseEntity<Selecao> criar(@Valid @RequestBody SelecaoRequest request) {
        Selecao selecao = Selecao.builder()
                .nomePais(request.getNomePais())
                .tecnico(request.getTecnico())
                .rankingFifa(request.getRankingFifa())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(selecao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build(); // HTTP 204
    }
}
```

---

## Lombok — elimina código repetitivo

Sem Lombok, `Selecao.java` teria +50 linhas extras de getters/setters. Com Lombok:

| Anotação | O que gera |
|---|---|
| `@Data` | todos os getters, setters, equals, hashCode, toString |
| `@Builder` | `Selecao.builder().nomePais("Brasil").build()` |
| `@NoArgsConstructor` | construtor vazio — **obrigatório para JPA** |
| `@AllArgsConstructor` | construtor com todos os campos |
| `@RequiredArgsConstructor` | construtor só com campos `final` (injeção no Spring) |

---

## DTOs — o que o cliente manda no body

```java
@Data
public class SelecaoRequest {
    @NotBlank(message = "Nome do país é obrigatório")
    @Schema(example = "Brasil")      // aparece pré-preenchido no Swagger
    private String nomePais;

    @NotBlank(message = "Nome do técnico é obrigatório")
    @Schema(example = "Dorival Júnior")
    private String tecnico;

    @NotNull @Positive
    @Schema(example = "5")
    private Integer rankingFifa;
}
```

**Por que DTO em vez de mandar a entidade direto?**
Separa o que o cliente pode enviar do que existe no banco. Exemplo: o cliente não manda o `id` (gerado pelo banco) nem a lista de jogadores.

---

## Padrão Builder — PartidaBuilder.java

```java
// Padrão de Projeto: Builder (GoF — Criacional)
public class PartidaBuilder {
    private LocalDate data;
    private String estadio;
    private String fase;
    private String placar;

    public PartidaBuilder naData(LocalDate data)      { this.data = data;       return this; }
    public PartidaBuilder noEstadio(String estadio)   { this.estadio = estadio; return this; }
    public PartidaBuilder naFase(String fase)         { this.fase = fase;       return this; }
    public PartidaBuilder comPlacar(String placar)    { this.placar = placar;   return this; }

    public Partida build() {
        // valida antes de criar — campos obrigatórios
        if (data == null)              throw new IllegalArgumentException("Data obrigatória");
        if (estadio == null || estadio.isBlank()) throw new IllegalArgumentException("Estádio obrigatório");
        if (fase == null || fase.isBlank())       throw new IllegalArgumentException("Fase obrigatória");

        return Partida.builder()
                .data(data).estadio(estadio).fase(fase).placar(placar)
                .build();
    }
}
```

Usado no controller:
```java
Partida partida = new PartidaBuilder()
    .naData(request.getData())
    .noEstadio(request.getEstadio())
    .naFase(request.getFase())
    .comPlacar(request.getPlacar())
    .build();
```

**O que falar:** *"O Builder permite construir objetos complexos passo a passo, com API legível e validação centralizada antes da criação."*

---

## SOLID na prática — resumo rápido

| Letra | Princípio | Onde está no código |
|---|---|---|
| **S** | Single Responsibility | Cada classe faz uma coisa: entity mapeia banco, service tem lógica, controller faz HTTP |
| **O** | Open/Closed | Novo comportamento = nova implementação de `IService`, sem mudar o controller |
| **L** | Liskov Substitution | `SelecaoService` substitui `ISelecaoService` em qualquer lugar sem quebrar |
| **I** | Interface Segregation | 3 interfaces separadas em vez de uma com 15 métodos misturados |
| **D** | Dependency Inversion | Controllers dependem de `ISelecaoService`, não de `SelecaoService` |

---

## Design Patterns — os 4 usados

### 1. Builder (GoF Criacional)
- **Onde:** `PartidaBuilder.java`
- **O que faz:** Constrói `Partida` passo a passo com API fluente

### 2. Strategy (GoF Comportamental)
- **Onde:** `ISelecaoService`, `IJogadorService`, `IPartidaService`
- **O que faz:** Define contratos intercambiáveis — o controller não sabe qual implementação usa

### 3. Repository (Padrão Arquitetural)
- **Onde:** `SelecaoRepository`, `JogadorRepository`, `PartidaRepository`
- **O que faz:** Abstrai o acesso ao banco — Spring gera o SQL automaticamente

### 4. Singleton (GoF Criacional)
- **Onde:** todos os `@Service` e `@Repository`
- **O que faz:** Spring mantém uma única instância de cada bean e injeta onde precisar

---

## Todos os Endpoints

### POST /selecoes — Cadastrar seleção
```json
Body:
{
  "nomePais": "Brasil",
  "tecnico": "Dorival Júnior",
  "rankingFifa": 5
}
Resposta: 201 Created
```

### GET /selecoes — Listar todas
```json
Resposta: 200 OK
[
  { "id": 1, "nomePais": "Brasil", "tecnico": "Dorival Júnior", "rankingFifa": 5 },
  { "id": 2, "nomePais": "Argentina", "tecnico": "Lionel Scaloni", "rankingFifa": 1 }
]
```

### POST /jogadores/selecao/1 — Cadastrar jogador na seleção 1
```json
Body:
{
  "nome": "Vinicius Jr.",
  "numeroCamisa": 7,
  "posicao": "Atacante",
  "idade": 24
}
Resposta: 201 Created
```

### GET /jogadores/selecao/1 — Jogadores do Brasil
```json
Resposta: 200 OK
[
  { "id": 1, "nome": "Vinicius Jr.", "numeroCamisa": 7, "posicao": "Atacante", "idade": 24 }
]
```

### POST /partidas — Cadastrar partida
```json
Body:
{
  "data": "2026-06-15",
  "estadio": "Maracanã",
  "fase": "Fase de Grupos",
  "placar": null
}
Resposta: 201 Created
```

### POST /partidas/1/selecoes/1 — Adicionar Brasil à partida
```
Sem body — só os IDs na URL
Resposta: 200 OK — partida com Brasil incluído
```

### PUT /partidas/1 — Atualizar placar após o jogo
```json
Body:
{
  "data": "2026-06-15",
  "estadio": "Maracanã",
  "fase": "Fase de Grupos",
  "placar": "2 x 1"
}
Resposta: 200 OK
```

---

## Tratamento de Erros

```java
// ResourceNotFoundException → HTTP 404
// Disparado quando: buscarPorId(99) e id 99 não existe

// MethodArgumentNotValidException → HTTP 400
// Disparado quando: body sem campo obrigatório (@NotBlank, @NotNull)

// GlobalExceptionHandler captura tudo e devolve JSON padronizado:
{
  "timestamp": "2026-06-15T10:30:00",
  "status": 404,
  "erro": "Seleção não encontrada com id: 99"
}
```

---

## Swagger — O que mostrar para o professor

URL: `http://localhost:8080/swagger-ui/index.html`

1. A página mostra os 3 grupos de endpoints: **Seleções**, **Jogadores**, **Partidas**
2. Cada endpoint tem descrição (`@Operation`) e códigos de resposta (`@ApiResponse`)
3. Ao clicar em **Try it out** → campos já vêm preenchidos com exemplos reais
4. A descrição da API lista os 4 Design Patterns usados

---

## Roteiro para a Apresentação

**1. Mostrar o Swagger** — abrir no navegador, explicar que a documentação é gerada automaticamente pelo código

**2. Fazer um cadastro ao vivo** — criar Brasil, criar Argentina, criar um jogador, criar uma partida e adicionar as duas seleções

**3. Mostrar o código em camadas** — abrir `SelecaoController.java` → `ISelecaoService.java` → `SelecaoService.java` → `SelecaoRepository.java` → `Selecao.java` e explicar o fluxo

**4. Explicar SOLID** — mostrar que o controller usa `ISelecaoService` (interface) e não `SelecaoService` (classe concreta)

**5. Mostrar os padrões de projeto** — abrir `PartidaBuilder.java` para o Builder, mostrar as interfaces para o Strategy

**6. Mostrar o banco** — se tiver H2 local, abrir `/h2-console` e mostrar as 4 tabelas criadas automaticamente

---

## Perguntas Esperadas e Respostas

**"O que é Spring Boot?"**
Framework Java que configura automaticamente tudo que o projeto precisa (servidor Tomcat, conexão com banco, serialização JSON) sem precisar configurar manualmente. Você foca na regra de negócio.

**"O que é JPA/Hibernate?"**
JPA é uma especificação para persistência de dados em Java. Hibernate é a implementação. Com `@Entity` você mapeia uma classe para uma tabela — o Hibernate gera o SQL de criação e manipulação automaticamente.

**"Por que usar interface em vez de chamar a classe diretamente?"**
É o princípio D do SOLID — Dependency Inversion. O controller não deve depender de detalhes de implementação, só de abstrações. Se amanhã precisar trocar `SelecaoService` por outra implementação, o controller não precisa mudar.

**"O que é o padrão Builder?"**
Padrão criacional que separa a construção de um objeto complexo da sua representação. O `PartidaBuilder` permite criar uma `Partida` passo a passo com API legível, e valida os campos obrigatórios no `build()` antes de criar o objeto.

**"O que é o padrão Strategy?"**
Padrão comportamental que define uma família de algoritmos (interfaces `IService`), encapsula cada um e os torna intercambiáveis. O controller não sabe qual implementação está rodando — só conhece o contrato.

**"O que é REST?"**
Estilo arquitetural para APIs web. Cada recurso tem uma URL (`/selecoes`, `/jogadores`). As operações usam verbos HTTP: GET (leitura), POST (criação), PUT (atualização), DELETE (remoção). As respostas retornam JSON com códigos HTTP adequados (200, 201, 204, 404).

**"O que é Lombok?"**
Biblioteca que usa anotações para gerar código em tempo de compilação. `@Data` gera getters/setters/equals/hashCode, `@Builder` gera o padrão Builder, eliminando dezenas de linhas de código repetitivo.

**"Como funciona o relacionamento N:N entre Partida e Seleção?"**
O JPA cria automaticamente uma tabela de junção `partida_selecao` com duas colunas de chave estrangeira: `partida_id` e `selecao_id`. Cada linha representa uma seleção participando de uma partida. Para adicionar Brasil (id=1) à partida (id=1), chama `POST /partidas/1/selecoes/1`.

**"O que é Swagger/OpenAPI?"**
Swagger gera documentação interativa da API diretamente do código. Com as anotações `@Tag`, `@Operation` e `@Schema`, o Springdoc cria automaticamente uma interface web onde é possível visualizar e testar todos os endpoints sem precisar do Postman.
