package cleancode.model;

public class Cliente {
    private final int id;
    private final String nome;
    private final String email;
    private final int tipoClienteCodigo;

    public Cliente(int id, String nome, String email, int tipoClienteCodigo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipoClienteCodigo = tipoClienteCodigo;
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

    public int getTipoClienteCodigo() {
        return tipoClienteCodigo;
    }

    public String getTipoDesc() {
        return TipoCliente.getDescricao(tipoClienteCodigo);
    }
}
