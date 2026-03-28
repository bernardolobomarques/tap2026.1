package cleancode.frete;

public class FreteGratis implements IFrete {
    private final double valor = 0.0;

    @Override
    public double getValor() {
        return valor;
    }
}
