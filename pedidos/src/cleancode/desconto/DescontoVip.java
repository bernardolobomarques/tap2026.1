package cleancode.desconto;

public class DescontoVip implements IDesconto {
    private final double percentual = 0.15;
    private final double minimo = 0; // vip sempre tem desconto

    @Override
    public double getPercentual() {
        return percentual;
    }

    @Override
    public double getMinimo() {
        return minimo;
    }
}
