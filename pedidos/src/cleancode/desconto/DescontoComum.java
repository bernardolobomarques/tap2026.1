package cleancode.desconto;

public class DescontoComum implements IDesconto {

    @Override
    public double aplicar(double subtotal) {
        if (subtotal > 300) {
            return subtotal * 0.95;
        }
        return subtotal;
    }
}
