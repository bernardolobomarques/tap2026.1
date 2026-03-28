package cleancode.desconto;

public class DescontoVip implements IDesconto {

    @Override
    public double aplicar(double subtotal) {
        return subtotal * 0.85;
    }
}
