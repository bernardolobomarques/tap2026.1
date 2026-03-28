package cleancode.controler;

import cleancode.exceptions.SistemaException;
import cleancode.model.Db;
import cleancode.model.Item;
import cleancode.model.Pedido;
import cleancode.model.PedidoService;
import cleancode.view.Relatorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Sistema {

    private final Scanner scanner = new Scanner(System.in);
    private final PedidoService pedidoService;
    private final Relatorio relatorio;

    public Sistema() {
        Db db = new Db();
        this.pedidoService = new PedidoService(db);
        this.relatorio = new Relatorio();
    }

    public void executarMenu() {
        int operacao = -1;

        while (operacao != 0) {
            System.out.println("==== SISTEMA ====");
            System.out.println("1 - Novo pedido");
            System.out.println("2 - Listar pedidos");
            System.out.println("3 - Buscar pedido por id");
            System.out.println("4 - Relatorio");
            System.out.println("5 - Cancelar pedido");
            System.out.println("0 - Sair");
            System.out.print("Opcao: ");
            operacao = lerInteiro();

            try {
                switch (operacao) {
                    case 1: novoPedido();      break;
                    case 2: listarPedidos();   break;
                    case 3: buscarPedidos();   break;
                    case 4: relatorio.gerar(pedidoService.listarPedidos()); break;
                    case 5: cancelarPedido();  break;
                    case 0: System.out.println("Encerrando..."); break;
                    default: System.out.println("Opcao invalida"); break;
                }
            } catch (SistemaException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    public void novoPedido() {
        System.out.println("Nome cliente:");
        String nomeCliente = scanner.nextLine();

        System.out.println("Tipo cliente (1 comum, 2 premium, 3 vip):");
        int tipoNum = lerInteiro();
        String tipo = tipoNum == 2 ? "premium" : tipoNum == 3 ? "vip" : "comum";

        List<Item> itens = new ArrayList<>();
        String continua = "s";
        while (continua.equalsIgnoreCase("s")) {
            System.out.println("Nome item:");
            String nomeItem = scanner.nextLine();
            System.out.println("Preco:");
            double preco = lerDouble();
            System.out.println("Qtd:");
            int qtd = lerInteiro();
            itens.add(new Item(nomeItem, preco, qtd));
            System.out.println("Adicionar mais item? s/n");
            continua = scanner.nextLine();
        }

        Pedido pedido = pedidoService.criarPedido(nomeCliente, tipo, itens);
        System.out.println("Pedido criado com sucesso");
        System.out.println("Id: " + pedido.getId());
        System.out.println("Cliente: " + pedido.getCliente().getNome());
        System.out.println("Total: " + pedido.getTotal());

        if (pedido.getTotal() > 500) {
            System.out.println("Pedido importante!!!");
        }
    }

    public void listarPedidos() {
        List<Pedido> pedidos = pedidoService.listarPedidos();
        if (pedidos.isEmpty()) {
            System.out.println("sem pedidos");
            return;
        }
        for (Pedido pedido : pedidos) {
            System.out.println("---------------");
            System.out.println("id: " + pedido.getId());
            System.out.println("cliente: " + pedido.getCliente().getNome());
            System.out.println("email: " + pedido.getCliente().getEmail());
            System.out.println("tipo: " + pedido.getCliente().getTipo());
            System.out.println("status: " + pedido.getStatus());
            System.out.println("total: " + pedido.getTotal());
            System.out.println("itens:");
            for (Item item : pedido.getItens()) {
                System.out.println(item.getNome() + " - " + item.getQuantidade() + "un - R$" + item.getPrecoUnitario());
            }
        }
    }

    public void buscarPedidos() {
        System.out.println("Digite o id:");
        int id = lerInteiro();
        Pedido pedido = pedidoService.buscarPedidoPorId(id);

        System.out.println("Pedido encontrado");
        System.out.println("id: " + pedido.getId());
        System.out.println("cliente: " + pedido.getCliente().getNome());
        System.out.println("tipo: " + pedido.getCliente().getTipo());
        System.out.println("status: " + pedido.getStatus());
        System.out.println("subtotal: " + pedido.calcularSubtotalItens());
        System.out.println("total: " + pedido.getTotal());

        int contador = 1;
        for (Item item : pedido.getItens()) {
            System.out.println("item " + contador + ": " + item.getNome() + " - " + item.getQuantidade() + "un - R$" + item.getPrecoUnitario());
            contador++;
        }
    }

    public void cancelarPedido() {
        System.out.println("Digite id do pedido:");
        int id = lerInteiro();
        pedidoService.cancelarPedido(id);
        System.out.println("cancelado");
    }

    private int lerInteiro() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Insira um numero valido");
            return -1;
        }
    }

    private double lerDouble() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Valor invalido");
            return 0;
        }
    }
}
