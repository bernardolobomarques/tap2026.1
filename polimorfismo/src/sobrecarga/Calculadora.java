/**
 * Arquivo de Calculadora, Final
 */


package sobrecarga;

public final class Calculadora {
    /**
     * Somar numero inteiro.
     * @param a
     * @param b
     * @return int a + b
     */
    public  int somar(final int a, final  int b) {
        return a + b;
    }

    /**
     * Somar 3 inteiros.
     * @param a
     * @param b
     * @param c
     * @return int a + b + c
     */
    public int somar(final int a, final  int b, final  int c) {
        return a + b + c;
    }

    /**
     * Somar numero flutuante.
     * @param a
     * @param b
     * @return double
     */
    public double somar(final double a, final  double b) {
        return a + b;
    }

}
