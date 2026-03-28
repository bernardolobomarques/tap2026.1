package cleancode.desconto;

public class DescontoComum implements IDesconto {
    private final double percentual = 0.05;
    private final double minimo = 300;

    @Override
    public double getPercentual() {
        return percentual;
    }

    @Override
    public double getMinimo() {
        return minimo;
    }
}
