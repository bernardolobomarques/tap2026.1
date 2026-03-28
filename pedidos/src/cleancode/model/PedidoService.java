package cleancode.model;

import cleancode.desconto.IDesconto;
import cleancode.desconto.DescontoComum;
import cleancode.desconto.DescontoPremium;
import cleancode.desconto.DescontoVip;
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

    public Pedido criarPedido(String nomeCliente, int tipoClienteCodigo, List<Item> itens) {
        validarDados(nomeCliente, itens);

        Cliente cliente = criarCliente(nomeCliente, tipoClienteCodigo);
        Pedido pedido = montarPedido(cliente, itens);
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

    private Cliente criarCliente(String nomeCliente, int tipoClienteCodigo) {
        int tipo = TipoCliente.fromCodigo(tipoClienteCodigo);
        String email = nomeCliente.replace(" ", "").toLowerCase() + "@email.com";
        return new Cliente(db.count() + 1, nomeCliente, email, tipo);
    }

    private Pedido montarPedido(Cliente cliente, List<Item> itens) {
        Pedido pedido = new Pedido(db.count() + 1, cliente);
        for (Item item : itens) {
            pedido.adicionarItem(item);
        }
        return pedido;
    }

    private double calcularTotal(Pedido pedido) {
        double subtotal = pedido.calcularSubtotalItens();

        // cria desconto pela interface — qualquer filho de IDesconto serve
        IDesconto desconto = getDesconto(pedido.getCliente().getTipoClienteCodigo());
        double valorComDesconto = desconto.aplicar(subtotal);

        // cria frete pela interface — qualquer filho de IFrete serve
        IFrete frete = getFrete(pedido.getCliente().getTipoClienteCodigo());
        return valorComDesconto + frete.calcular(valorComDesconto);
    }

    private IDesconto getDesconto(int tipoCodigo) {
        if (tipoCodigo == TipoCliente.PREMIUM) return new DescontoPremium();
        if (tipoCodigo == TipoCliente.VIP) return new DescontoVip();
        return new DescontoComum();
    }

    private IFrete getFrete(int tipoCodigo) {
        if (tipoCodigo == TipoCliente.VIP) return new FreteGratis();
        return new FreteNormal();
    }
}
