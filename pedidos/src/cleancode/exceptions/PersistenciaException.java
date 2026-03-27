package cleancode.exceptions;

public class PersistenciaException extends CrudException {
    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
