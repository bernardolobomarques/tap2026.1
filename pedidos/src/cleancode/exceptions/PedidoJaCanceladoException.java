package cleancode.exceptions;

public class PedidoJaCanceladoException extends CrudException {
    public PedidoJaCanceladoException(int idPedido) {
        super("pedido " + idPedido + " ja esta cancelado");
    }
}
