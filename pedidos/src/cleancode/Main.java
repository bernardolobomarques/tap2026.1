package cleancode;

import cleancode.controler.Sistema;
import cleancode.exceptions.SistemaException;

public class Main {
    public static void main(String[] args) {
        Sistema sistema = new Sistema();
        try {
            sistema.executarMenu();
        } catch (SistemaException exception) {
            System.out.println("sistema encerrado por erro: " + exception.getMessage());
        }
    }
}
