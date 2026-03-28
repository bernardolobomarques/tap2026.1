package cleancode.desconto;

public class DescontoPremium implements IDesconto {

    @Override
    public double aplicar(double subtotal) {
        if (subtotal > 200) {
            return subtotal * 0.90;
        }
        return subtotal * 0.97;
    }
}
