package cleancode.view;

import cleancode.model.Item;
import cleancode.model.Pedido;
import cleancode.model.TipoCliente;

import java.util.List;

public class Relatorio {

    private static final int INDICE_COMUM = 0;
    private static final int INDICE_PREMIUM = 1;
    private static final int INDICE_VIP = 2;

    public void gerar(List<Pedido> pedidos) {
        System.out.println("======= RELATORIO =======");

        int quantidadePedidos = 0;
        double valorTotal = 0;
        int cancelados = 0;
        int[] clientesPorTipo = new int[3];

        for (Pedido pedido : pedidos) {
            quantidadePedidos++;
            valorTotal += pedido.getTotal();

            if (pedido.isCancelado()) {
                cancelados++;
            }

            contarTipoCliente(clientesPorTipo, pedido.getCliente().getTipoClienteCodigo());
            imprimirPedido(pedido);
        }

        imprimirRodapeResumo(
                quantidadePedidos,
                valorTotal,
                cancelados,
                clientesPorTipo[INDICE_COMUM],
                clientesPorTipo[INDICE_PREMIUM],
                clientesPorTipo[INDICE_VIP]);
    }

    private void contarTipoCliente(int[] clientesPorTipo, int tipoClienteCodigo) {
        if (tipoClienteCodigo == TipoCliente.PREMIUM) {
            clientesPorTipo[INDICE_PREMIUM]++;
            return;
        }
        if (tipoClienteCodigo == TipoCliente.VIP) {
            clientesPorTipo[INDICE_VIP]++;
            return;
        }
        clientesPorTipo[INDICE_COMUM]++;
    }

    private void imprimirPedido(Pedido pedido) {
        System.out.println(
                "Pedido " + pedido.getId() +
                " - " + pedido.getCliente().getNome() +
                " - " + pedido.getTotal() +
                " - " + pedido.getStatusDesc());

        for (Item item : pedido.getItens()) {
            System.out.println(
                    "   item: " + item.getNome() +
                    " qtd:" + item.getQuantidade() +
                    " preco:" + item.getPrecoUnitario());
        }
    }


    private void imprimirRodapeResumo(
            int quantidadePedidos,
            double valorTotal,
            int cancelados,
            int clientesComuns,
            int clientesPremium,
            int clientesVip) {
        System.out.println("--------------------");
        System.out.println("qtd pedidos: " + quantidadePedidos);
        System.out.println("valor total: " + valorTotal);
        System.out.println("cancelados: " + cancelados);
        System.out.println("clientes comuns: " + clientesComuns);
        System.out.println("clientes premium: " + clientesPremium);
        System.out.println("clientes vip: " + clientesVip);

        if (quantidadePedidos > 0) {
            System.out.println("media: " + (valorTotal / quantidadePedidos));
        } else {
            System.out.println("media: 0");
        }

        if (valorTotal > 1000) {
            System.out.println("resultado muito bom");
        } else if (valorTotal > 500) {
            System.out.println("resultado ok");
        } else {
            System.out.println("resultado fraco");
        }
    }
}
