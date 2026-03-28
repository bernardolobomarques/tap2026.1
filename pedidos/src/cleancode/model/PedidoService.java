package cleancode.model;

import cleancode.desconto.DescontoComum;
import cleancode.desconto.DescontoPremium;
import cleancode.desconto.DescontoVip;
import cleancode.desconto.IDesconto;
import cleancode.exceptions.PedidoJaCanceladoException;
import cleancode.exceptions.ValidacaoException;
import cleancode.frete.FreteGratis;
import cleancode.frete.FreteNormal;
import cleancode.frete.IFrete;

import java.util.List;

public class PedidoService {

    private final Db db;

    public PedidoService(Db db) {
        this.db = db;
    }

    public Pedido criarPedido(String nomeCliente, String tipo, List<Item> itens) {
        validarDados(nomeCliente, itens);

        String tipoNormalizado = normalizarTipo(tipo);
        String email = nomeCliente.replace(" ", "").toLowerCase() + "@email.com";
        Cliente cliente = new Cliente(db.count() + 1, nomeCliente, email, tipoNormalizado);

        Pedido pedido = new Pedido(db.count() + 1, cliente);
        for (Item item : itens) {
            pedido.adicionarItem(item);
        }

        pedido.setTotal(calcularTotal(pedido));
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

    private void validarDados(String nomeCliente, List<Item> itens) {
        if (nomeCliente == null || nomeCliente.trim().isEmpty()) {
            throw new ValidacaoException("nome do cliente obrigatorio");
        }
        if (itens == null || itens.isEmpty()) {
            throw new ValidacaoException("pedido deve ter pelo menos um item");
        }
    }

    private String normalizarTipo(String tipo) {
        if (tipo == null) return "comum";
        switch (tipo.toLowerCase()) {
            case "premium": return "premium";
            case "vip":     return "vip";
            default:        return "comum";
        }
    }

    private double calcularTotal(Pedido pedido) {
        double subtotal = pedido.calcularSubtotalItens();

        // objeto criado pela interface - aceita qualquer filho de IDesconto
        IDesconto desconto = getDesconto(pedido.getCliente().getTipo());
        double valorComDesconto = desconto.aplicar(subtotal);

        // objeto criado pela interface - aceita qualquer filho de IFrete
        IFrete frete = getFrete(pedido.getCliente().getTipo());
        return valorComDesconto + frete.calcular(valorComDesconto);
    }

    private IDesconto getDesconto(String tipo) {
        if (tipo.equals("premium")) return new DescontoPremium();
        if (tipo.equals("vip"))     return new DescontoVip();
        return new DescontoComum();
    }

    private IFrete getFrete(String tipo) {
        if (tipo.equals("vip")) return new FreteGratis();
        return new FreteNormal();
    }
}
