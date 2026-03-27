package cleancode.model;

public final class TipoCliente {
    public static final int COMUM = 1;
    public static final int PREMIUM = 2;
    public static final int VIP = 3;

    private TipoCliente() {
    }

    public static int fromCodigo(int codigo) {
        switch (codigo) {
            case PREMIUM:
            case VIP:
                return codigo;
            default:
                return COMUM;
        }
    }

    public static String getDescricao(int codigo) {
        switch (codigo) {
            case PREMIUM:
                return "premium";
            case VIP:
                return "vip";
            default:
                return "comum";
        }
    }
}

