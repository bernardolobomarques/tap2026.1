package cleancode.desconto;

public class DescontoPremium implements IDesconto {
    private final double percentual = 0.10;
    private final double minimo = 200;

    @Override
    public double getPercentual() {
        return percentual;
    }

    @Override
    public double getMinimo() {
        return minimo;
    }
}
