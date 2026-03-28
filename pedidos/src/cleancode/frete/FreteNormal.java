package cleancode.frete;

public class FreteNormal implements IFrete {
    private final double valor;

    public FreteNormal(double subtotal) {
        if (subtotal < 100) {
            this.valor = 25.0;
        } else if (subtotal < 300) {
            this.valor = 15.0;
        } else {
            this.valor = 0.0;
        }
    }

    @Override
    public double getValor() {
        return valor;
    }
}
