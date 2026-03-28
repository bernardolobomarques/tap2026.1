package cleancode.frete;

public class FreteNormal implements IFrete {

    @Override
    public double calcular(double valorComDesconto) {
        if (valorComDesconto < 100) return 25.0;
        if (valorComDesconto < 300) return 15.0;
        return 0.0;
    }
}
