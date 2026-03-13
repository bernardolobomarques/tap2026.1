package sobrescrita;

/**
 * Cachorro.
 */
public class Cachorro extends Animal {
    /**
     * sim.
     */
    @Override
    void emitirSom() {
        System.out.println("Au au");
    }
}
