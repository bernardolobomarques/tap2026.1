package cleancode.controler;

import cleancode.exceptions.SistemaException;
import cleancode.model.Db;
import cleancode.model.Item;
import cleancode.model.Pedido;
import cleancode.model.PedidoService;
import cleancode.model.TipoCliente;
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
        while (true) {
            exibirMenu();
            int operacao = lerInteiro("Opcao: ", -1);

            if (operacao == 0) {
                System.out.println("fim");
                return;
            }

            try {
                processarOperacao(operacao);
            } catch (SistemaException exception) {
                System.out.println("erro de negocio: " + exception.getMessage());
                throw exception;
            }
        }
    }

    private void exibirMenu() {
        System.out.println("==== SISTEMA ====");
        System.out.println("1 - Novo pedido");
        System.out.println("2 - Listar pedidos");
        System.out.println("3 - Buscar pedido por id");
        System.out.println("4 - Relatorio");
        System.out.println("5 - Cancelar pedido");
        System.out.println("0 - Sair");
    }

    private void processarOperacao(int operacao) {
        switch (operacao) {
            case 1:
                novoPedido();
                break;
            case 2:
                listarPedidos();
                break;
            case 3:
                buscarPedidos();
                break;
            case 4:
                chamarRelatorio();
                break;
            case 5:
                cancelarPedido();
                break;
            default:
                System.out.println("opcao invalida");
                break;
        }
    }

    public void novoPedido() {
        String nomeCliente = lerTexto("Nome cliente:");
        int tipoInformado = lerInteiro("Tipo cliente (1 comum, 2 premium, 3 vip):", TipoCliente.COMUM);
        List<Item> itens = coletarItensPedido();

        Pedido pedido = pedidoService.criarPedido(nomeCliente, tipoInformado, itens);

        imprimirPedidoCriado(pedido);
    }

    public void listarPedidos() {
        List<Pedido> pedidos = pedidoService.listarPedidos();
        if (pedidos.isEmpty()) {
            System.out.println("sem pedidos");
            return;
        }

        for (Pedido pedido : pedidos) {
            imprimirPedidoDaLista(pedido);
        }
    }

    public void buscarPedidos() {
        int id = lerInteiro("Digite o id:", -1);
        Pedido pedido = pedidoService.buscarPedidoPorId(id);

        System.out.println("Pedido encontrado");
        System.out.println("id: " + pedido.getId());
        System.out.println("cliente: " + pedido.getCliente().getNome());
        System.out.println("status: " + pedido.getStatusDesc());
        System.out.println("total: " + pedido.getTotal());

        double subtotal = pedido.calcularSubtotalItens();
        System.out.println("subtotal calculado novamente: " + subtotal);

        System.out.println("cliente " + pedido.getCliente().getTipoDesc());

        int indiceItem = 1;
        for (Item item : pedido.getItens()) {
            imprimirItemDetalhado(indiceItem, item);
            indiceItem++;
        }
    }

    public void chamarRelatorio() {
        relatorio.gerar(pedidoService.listarPedidos());
    }

    public void cancelarPedido() {
        int id = lerInteiro("Digite id do pedido", -1);
        pedidoService.cancelarPedido(id);
        System.out.println("cancelado");
    }

    private List<Item> coletarItensPedido() {
        List<Item> itens = new ArrayList<>();
        String adicionarMaisItens = "s";
        while (adicionarMaisItens.equalsIgnoreCase("s")) {
            String nomeItem = lerTexto("Nome item:");
            double precoItem = lerDouble("Preco item:", 0);
            int quantidadeItens = lerInteiro("Qtd:", 1);

            Item item = new Item(nomeItem, precoItem, quantidadeItens);
            itens.add(item);

            adicionarMaisItens = lerTexto("Adicionar mais item? s/n");
        }
        return itens;
    }

    private void imprimirPedidoCriado(Pedido pedido) {
        System.out.println("Pedido criado com sucesso");
        System.out.println("Id: " + pedido.getId());
        System.out.println("Cliente: " + pedido.getCliente().getNome());
        System.out.println("Total: " + pedido.getTotal());

        if (pedido.getTotal() > 500) {
            System.out.println("Pedido importante!!!");
        }
    }

    private void imprimirPedidoDaLista(Pedido pedido) {
        System.out.println("---------------");
        System.out.println("id: " + pedido.getId());
        System.out.println("cliente: " + pedido.getCliente().getNome());
        System.out.println("email: " + pedido.getCliente().getEmail());
        System.out.println("tipo: " + pedido.getCliente().getTipoDesc());
        System.out.println("status: " + pedido.getStatusDesc());
        System.out.println("total: " + pedido.getTotal());
        System.out.println("itens:");
        for (Item item : pedido.getItens()) {
            System.out.println(item.getNome() + " - " + item.getQuantidade() + " - " + item.getPrecoUnitario());
        }
    }

    private void imprimirItemDetalhado(int indiceItem, Item item) {
        System.out.println(
                "item " + indiceItem + ": " +
                item.getNome() + " / " +
                item.getQuantidade() + " / " +
                item.getPrecoUnitario());
    }

    private String lerTexto(String mensagem) {
        System.out.println(mensagem);
        return scanner.nextLine();
    }


    private int lerInteiro(String mensagem, int valorPadrao) {
        System.out.println(mensagem);
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException exception) {
            System.out.println("valor invalido, usando padrao " + valorPadrao);
            return valorPadrao;
        }
    }

    private double lerDouble(String mensagem, double valorPadrao) {
        System.out.println(mensagem);
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException exception) {
            System.out.println("valor invalido, usando padrao " + valorPadrao);
            return valorPadrao;
        }
    }
}


