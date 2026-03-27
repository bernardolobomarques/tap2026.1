package cleancode.model;

import cleancode.exceptions.PedidoNaoEncontradoException;
import cleancode.exceptions.ValidacaoException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Db {

    private final List<Pedido> pedidosSalvos = new ArrayList<>();

    public void save(Pedido pedido) {
        validarPedidoParaSalvar(pedido);
        pedidosSalvos.add(pedido);
        System.out.println("salvou no banco");
    }

    private void validarPedidoParaSalvar(Pedido pedido) {
        if (pedido == null) {
            throw new ValidacaoException("pedido invalido para salvar");
        }
    }

    public List<Pedido> getAll() {
        return Collections.unmodifiableList(pedidosSalvos);
    }

    public Pedido getById(int id) {
        if (id <= 0) {
            throw new ValidacaoException("id do pedido deve ser maior que zero");
        }

        for (Pedido pedido : pedidosSalvos) {
            if (pedido.getId() == id) {
                return pedido;
            }
        }

        throw new PedidoNaoEncontradoException(id);
    }

    public int count() {
        return pedidosSalvos.size();
    }
}
