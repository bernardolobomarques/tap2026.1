package sobrecarga;

/**
 * Teste de Sobrecarga.
 */
public final class TesteSobrecarga {
    /**
     * Funcao de teste.
     * @param args
     */
    public static void main(final String[] args) {
        Calculadora calculadora = new Calculadora();
        System.out.println(calculadora.somar(1, 2));
        System.out.println(calculadora.somar(1, 2, 1));
        System.out.println(calculadora.somar(1.0, 2.0));

    }
}
