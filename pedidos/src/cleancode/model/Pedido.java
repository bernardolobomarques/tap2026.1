package cleancode.model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private final int id;
    private final Cliente cliente;
    private final List<Item> itens;
    private double total;
    private String status;

    public Pedido(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.itens = new ArrayList<>();
        this.status = "novo";
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

    public String getStatus() {
        return status;
    }

    public boolean isCancelado() {
        return status.equals("cancelado");
    }

    public void cancelar() {
        this.status = "cancelado";
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
