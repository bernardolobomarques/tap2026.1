package cleancode.model;

public class Cliente {
    private final int id;
    private final String nome;
    private final String email;
    private final String tipo; // "comum", "premium", "vip"

    public Cliente(int id, String nome, String email, String tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTipo() {
        return tipo;
    }
}
