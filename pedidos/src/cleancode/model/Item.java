package cleancode.model;

public class Item {
    private final String nome;
    private final double precoUnitario;
    private final int quantidade;

    public Item(String nome, double precoUnitario, int quantidade) {
        this.nome = nome;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double calcularSubtotal() {
        return precoUnitario * quantidade;
    }
}
