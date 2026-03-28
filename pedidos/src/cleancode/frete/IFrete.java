package cleancode.frete;

public interface IFrete {
    double getValor();

    default double calcular(double valorComDesconto) {
        return valorComDesconto + getValor();
    }
}
