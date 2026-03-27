package cleancode.model;

public final class StatusPedido {
    public static final int NOVO = 1;
    public static final int CANCELADO = 2;

    private StatusPedido() {
    }

    public static String getDescricao(int codigo) {
        switch (codigo) {
            case CANCELADO:
                return "CANCELADO";
            default:
                return "NOVO";
        }
    }
}

