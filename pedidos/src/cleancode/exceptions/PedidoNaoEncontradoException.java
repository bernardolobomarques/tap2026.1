package cleancode.exceptions;

public class PedidoNaoEncontradoException extends CrudException {
    public PedidoNaoEncontradoException(int idPedido) {
        super("pedido nao encontrado para id " + idPedido);
    }
}
