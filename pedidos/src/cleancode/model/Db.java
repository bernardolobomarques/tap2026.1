package cleancode.model;

import cleancode.exceptions.PedidoNaoEncontradoException;
import cleancode.exceptions.ValidacaoException;

import java.util.ArrayList;
import java.util.List;

public class Db {

    private final List<Pedido> pedidos = new ArrayList<>();

    public void save(Pedido pedido) {
        if (pedido == null) {
            throw new ValidacaoException("pedido invalido para salvar");
        }
        pedidos.add(pedido);
    }

    public List<Pedido> getAll() {
        return pedidos;
    }

    public Pedido getById(int id) {
        for (Pedido pedido : pedidos) {
            if (pedido.getId() == id) {
                return pedido;
            }
        }
        throw new PedidoNaoEncontradoException(id);
    }

    public int count() {
        return pedidos.size();
    }
}
