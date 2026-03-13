package sobrescrita;

/**
 * Gato.
 */

public class Gato extends Animal {
    /**
     * faz barulho.
     */
    @Override
    void emitirSom() {
        System.out.println("Au au");
    }
}
