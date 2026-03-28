package cleancode.view;

import cleancode.model.Item;
import cleancode.model.Pedido;

import java.util.List;

public class Relatorio {

    public void gerar(List<Pedido> pedidos) {
        System.out.println("======= RELATORIO =======");

        int total = 0;
        double valorTotal = 0;
        int cancelados = 0;
        int comuns = 0;
        int premium = 0;
        int vip = 0;

        for (Pedido pedido : pedidos) {
            total++;
            valorTotal += pedido.getTotal();

            if (pedido.isCancelado()) cancelados++;

            String tipo = pedido.getCliente().getTipo();
            if (tipo.equals("premium"))      premium++;
            else if (tipo.equals("vip"))     vip++;
            else                             comuns++;

            System.out.println("Pedido " + pedido.getId() + " - " + pedido.getCliente().getNome() + " - R$" + pedido.getTotal() + " - " + pedido.getStatus());
            for (Item item : pedido.getItens()) {
                System.out.println("   item: " + item.getNome() + " qtd:" + item.getQuantidade() + " preco:R$" + item.getPrecoUnitario());
            }
        }

        System.out.println("--------------------");
        System.out.println("qtd pedidos: " + total);
        System.out.println("valor total: R$" + valorTotal);
        System.out.println("cancelados: " + cancelados);
        System.out.println("clientes comuns: " + comuns);
        System.out.println("clientes premium: " + premium);
        System.out.println("clientes vip: " + vip);

        if (total > 0) System.out.println("media: R$" + (valorTotal / total));

        if (valorTotal > 1000)      System.out.println("resultado muito bom");
        else if (valorTotal > 500)  System.out.println("resultado ok");
        else                        System.out.println("resultado fraco");
    }
}
