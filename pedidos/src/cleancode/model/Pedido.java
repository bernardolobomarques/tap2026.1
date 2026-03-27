package cleancode.model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private final int id;
    private final Cliente cliente;
    private final List<Item> itens;
    private double total;
    private int statusCodigo;

    public Pedido(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.itens = new ArrayList<>();
        this.statusCodigo = StatusPedido.NOVO;
        this.total = 0;
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<Item> getItens() {
        return itens;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatusDesc() {
        return StatusPedido.getDescricao(statusCodigo);
    }

    public boolean isCancelado() {
        return statusCodigo == StatusPedido.CANCELADO;
    }

    public void cancelar() {
        this.statusCodigo = StatusPedido.CANCELADO;
    }

    public void adicionarItem(Item item) {
        itens.add(item);
    }

    public double calcularSubtotalItens() {
        double subtotal = 0;
        for (Item item : itens) {
            subtotal += item.calcularSubtotal();
        }
        return subtotal;
    }
}
