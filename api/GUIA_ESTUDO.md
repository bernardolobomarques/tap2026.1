# Guia de Estudo — API REST Java

## O que essa API faz

É uma loja simples. Três conceitos:
- **Categoria**: agrupa produtos (ex: "Eletrônicos")
- **Produto**: pertence a uma categoria, pode estar em vários pedidos
- **Pedido**: feito por um cliente, contém vários produtos

---

## Estrutura de pastas — o que cada uma faz

```
entity/       →  representa a tabela do banco. Uma classe = uma tabela.
repository/   →  faz o SQL. Você nunca escreve SELECT/INSERT/UPDATE, o Spring faz.
service/      →  regra de negócio. Ex: "para criar um produto, preciso verificar se a categoria existe".
controller/   →  recebe a requisição HTTP e devolve a resposta. Não tem lógica aqui.
exception/    →  trata erros de forma padronizada.
```

**Fluxo de uma requisição:**
```
Cliente HTTP → Controller → Service → Repository → Banco de dados
                         ←         ←            ←
```

---

## As 3 entidades e seus relacionamentos

### Categoria.java
```java
@OneToMany(mappedBy = "categoria")   // uma categoria tem MUITOS produtos
private List<Produto> produtos;
```

### Produto.java
```java
@ManyToOne                           // muitos produtos pertencem a UMA categoria
private Categoria categoria;

@ManyToMany(mappedBy = "produtos")   // um produto pode estar em MUITOS pedidos
private List<Pedido> pedidos;
```

### Pedido.java
```java
@ManyToMany                          // um pedido tem MUITOS produtos
@JoinTable(name = "pedido_produto")  // cria tabela de junção automaticamente
private List<Produto> produtos;
```

**No banco de dados isso vira 4 tabelas:**
```
categorias          produtos              pedidos        pedido_produto
----------          --------              -------        -------------
id                  id                    id             pedido_id
nome                nome                  cliente        produto_id
descricao           descricao             data_criacao
                    preco
                    categoria_id  ←── chave estrangeira para categorias
```

---

## Lombok — por que não tem getter/setter no código

Lombok é uma biblioteca que gera código em tempo de compilação.

| Anotação | O que gera |
|---|---|
| `@Data` | getter, setter, equals, hashCode, toString |
| `@Builder` | padrão builder: `Categoria.builder().nome("x").build()` |
| `@NoArgsConstructor` | construtor sem argumentos (JPA exige isso) |
| `@AllArgsConstructor` | construtor com todos os campos |
| `@RequiredArgsConstructor` | construtor só com campos `final` (usado nos Services) |

**Sem Lombok o código seria o dobro do tamanho com getters/setters manuais.**

---

## SOLID — como está aplicado aqui

### S — Single Responsibility (cada classe faz uma coisa)
- `Categoria.java` só mapeia o banco. Não tem lógica.
- `CategoriaService.java` só tem regra de negócio.
- `CategoriaController.java` só recebe HTTP e devolve resposta.

### O — Open/Closed (aberto para extensão, fechado para modificação)
Se o professor pedir um segundo tipo de serviço (ex: `CategoriaServicePremium`), você cria uma nova classe implementando `ICategoriaService` sem mexer em nenhum código existente.

### L — Liskov Substitution
`CategoriaService` implementa `ICategoriaService`. Em qualquer lugar que usar `ICategoriaService`, pode substituir por `CategoriaService` sem quebrar nada.

### I — Interface Segregation (interfaces específicas)
Existem 3 interfaces separadas: `ICategoriaService`, `IProdutoService`, `IPedidoService`.
Se fosse uma interface só com 15 métodos, qualquer implementação seria obrigada a ter todos eles.

### D — Dependency Inversion (dependa de abstrações)
```java
// Controller depende da INTERFACE, não da implementação concreta
private final ICategoriaService service;  // ← correto

// Se fosse assim, estaria errado:
private final CategoriaService service;   // ← acoplamento direto
```

---

## Padrões de Projeto aplicados

### Repository Pattern
```java
// Você define a interface:
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {}

// O Spring gera a implementação completa automaticamente.
// Você nunca escreve: "SELECT * FROM categorias WHERE id = ?"
```

### Strategy Pattern
As interfaces `IService` são o padrão Strategy. O Controller não sabe qual implementação está usando — ele só chama `service.listarTodos()`. Você pode trocar a implementação sem mudar o Controller.

### Singleton Pattern
Todo `@Service`, `@Repository` e `@RestController` é um Singleton gerenciado pelo Spring. O Spring cria uma única instância de cada um e injeta onde necessário.

---

## Injeção de Dependência — como o Spring conecta tudo

```java
@RestController
@RequiredArgsConstructor              // Lombok gera construtor com campos final
public class CategoriaController {

    private final ICategoriaService service;  // Spring injeta automaticamente
}
```

O Spring vê que `CategoriaController` precisa de `ICategoriaService`, procura qual classe implementa essa interface (`CategoriaService`), cria ela e injeta. Você não escreve `new CategoriaService()` em lugar nenhum.

---

## Endpoints disponíveis

### Categorias
```
GET    /categorias           → lista todas
GET    /categorias/1         → busca por id
POST   /categorias           → cria nova
PUT    /categorias/1         → atualiza
DELETE /categorias/1         → deleta
```

### Produtos
```
GET    /produtos                        → lista todos
GET    /produtos/categoria/1            → lista por categoria
GET    /produtos/1                      → busca por id
POST   /produtos/categoria/1            → cria produto na categoria 1
PUT    /produtos/1                      → atualiza
DELETE /produtos/1                      → deleta
```

### Pedidos
```
GET    /pedidos                         → lista todos
GET    /pedidos/1                       → busca por id
POST   /pedidos                         → cria pedido
POST   /pedidos/1/produtos/2            → adiciona produto 2 ao pedido 1
DELETE /pedidos/1/produtos/2            → remove produto 2 do pedido 1
DELETE /pedidos/1                       → deleta pedido
```

---

## Exemplos de requisição (para testar no Postman)

**Criar categoria:**
```json
POST /categorias
{
  "nome": "Eletrônicos",
  "descricao": "Produtos eletrônicos em geral"
}
```

**Criar produto na categoria 1:**
```json
POST /produtos/categoria/1
{
  "nome": "Notebook",
  "descricao": "Notebook 15 polegadas",
  "preco": 3500.00
}
```

**Criar pedido:**
```json
POST /pedidos
{
  "cliente": "João Silva"
}
```

**Adicionar produto 1 ao pedido 1:**
```
POST /pedidos/1/produtos/1
(sem body)
```

---

## Perguntas que o professor pode fazer

**"O que é JPA?"**
JPA (Jakarta Persistence API) é uma especificação para mapear classes Java em tabelas do banco. O Hibernate é a implementação mais usada. Com `@Entity` você diz "essa classe é uma tabela" e o JPA cuida do SQL.

**"Por que usar interface no Service se só tem uma implementação?"**
Por SOLID (Dependency Inversion). O Controller não depende de uma classe concreta, depende de um contrato. Facilita testes (você pode criar um mock da interface) e extensão futura.

**"O que é `@RestController`?"**
É `@Controller` + `@ResponseBody`. Significa que todos os métodos retornam dados JSON direto, não uma página HTML.

**"O que faz `@RequestMapping("/categorias")`?"**
Define o prefixo da URL para todos os endpoints da classe. Junto com `@GetMapping`, `@PostMapping` etc. mapeia cada método a uma rota HTTP.

**"O que é `ddl-auto=update`?"**
O Hibernate olha suas entidades e cria/atualiza as tabelas no banco automaticamente. Em produção real usaria `validate` ou migrations manuais, mas para desenvolvimento/prova `update` é suficiente.

**"Como funciona o `@ManyToMany`?"**
O JPA cria automaticamente uma tabela de junção (`pedido_produto`) com duas colunas: `pedido_id` e `produto_id`. Cada linha representa um produto dentro de um pedido.

**"O que é Lombok?"**
Uma biblioteca de geração de código. As anotações como `@Data` e `@Builder` instruem o compilador a gerar getters, setters e construtores automaticamente, eliminando boilerplate.

---

## Como adaptar para o tema da prova

1. Renomear as 3 entidades na pasta `entity/` e nos `repository/`, `service/`, `controller/`
2. Trocar os campos dentro das entidades
3. Manter toda a estrutura igual

**Exemplo — tema "Biblioteca":**

| Original | Adaptado |
|---|---|
| `Categoria` | `Autor` |
| `Produto` | `Livro` |
| `Pedido` | `Emprestimo` |
| `preco` em Produto | `anoPublicacao` em Livro |
| `cliente` em Pedido | `nomeAluno` em Emprestimo |

Os relacionamentos continuam iguais: Autor tem muitos Livros (1:N), Emprestimo tem muitos Livros (N:N).
