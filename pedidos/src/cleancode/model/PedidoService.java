package cleancode.model;

import cleancode.exceptions.PedidoJaCanceladoException;
import cleancode.exceptions.ValidacaoException;

import java.util.List;

public class PedidoService {

    private static final double DESCONTO_COMUM = 0.05;
    private static final double DESCONTO_PREMIUM = 0.10;
    private static final double DESCONTO_VIP = 0.15;
    private static final double FRETE_MENOR_100 = 25;
    private static final double FRETE_ENTRE_100_E_300 = 15;

    private final Db db;

    public PedidoService(Db db) {
        this.db = db;
    }

    public Pedido criarPedido(String nomeCliente, int tipoClienteCodigo, List<Item> itens) {
        validarDadosCriacaoPedido(nomeCliente, itens);

        Cliente cliente = criarCliente(nomeCliente, tipoClienteCodigo);
        Pedido pedido = criarPedidoComItens(cliente, itens);
        pedido.setTotal(calcularTotalFinal(pedido));

        db.save(pedido);
        return pedido;
    }

    public List<Pedido> listarPedidos() {
        return db.getAll();
    }

    public Pedido buscarPedidoPorId(int id) {
        return db.getById(id);
    }

    public void cancelarPedido(int id) {
        Pedido pedido = db.getById(id);

        if (pedido.isCancelado()) {
            throw new PedidoJaCanceladoException(id);
        }

        pedido.cancelar();
    }

    private void validarDadosCriacaoPedido(String nomeCliente, List<Item> itens) {
        if (nomeCliente == null || nomeCliente.trim().isEmpty()) {
            throw new ValidacaoException("nome do cliente obrigatorio");
        }

        if (itens == null || itens.isEmpty()) {
            throw new ValidacaoException("pedido deve ter pelo menos um item");
        }
    }

    private Cliente criarCliente(String nomeCliente, int tipoClienteCodigo) {
        int tipoClienteNormalizado = TipoCliente.fromCodigo(tipoClienteCodigo);
        return new Cliente(gerarNovoIdCliente(), nomeCliente, gerarEmailPadrao(nomeCliente), tipoClienteNormalizado);
    }

    private Pedido criarPedidoComItens(Cliente cliente, List<Item> itens) {
        Pedido pedido = new Pedido(gerarNovoIdPedido(), cliente);
        for (Item item : itens) {
            pedido.adicionarItem(item);
        }
        return pedido;
    }

    private double calcularTotalFinal(Pedido pedido) {
        double subtotal = pedido.calcularSubtotalItens();
        double totalComDesconto = aplicarDesconto(pedido.getCliente().getTipoClienteCodigo(), subtotal);
        return aplicarFrete(totalComDesconto);
    }

    private double aplicarDesconto(int tipoClienteCodigo, double total) {
        double desconto = calcularTaxaDesconto(tipoClienteCodigo, total);
        return total - (total * desconto);
    }

    private double calcularTaxaDesconto(int tipoClienteCodigo, double total) {
        if (tipoClienteCodigo == TipoCliente.COMUM && total > 300) {
            return DESCONTO_COMUM;
        }
        if (tipoClienteCodigo == TipoCliente.PREMIUM && total > 200) {
            return DESCONTO_PREMIUM;
        }
        if (tipoClienteCodigo == TipoCliente.VIP) {
            return DESCONTO_VIP;
        }
        return 0;
    }

    private double aplicarFrete(double total) {
        if (total < 100) {
            return total + FRETE_MENOR_100;
        }
        if (total < 300) {
            return total + FRETE_ENTRE_100_E_300;
        }
        return total;
    }

    private int gerarNovoIdPedido() {
        return db.count() + 1;
    }

    private int gerarNovoIdCliente() {
        return db.count() + 1;
    }

    private String gerarEmailPadrao(String nomeCliente) {
        return nomeCliente.replace(" ", "").toLowerCase() + "@email.com";
    }
}

