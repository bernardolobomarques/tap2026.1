package cleancode.desconto;

public interface IDesconto {
    double getPercentual();
    double getMinimo();

    default double aplicar(double subtotal) {
        if (subtotal >= getMinimo()) {
            return subtotal - (subtotal * getPercentual());
        }
        return subtotal;
    }
}
