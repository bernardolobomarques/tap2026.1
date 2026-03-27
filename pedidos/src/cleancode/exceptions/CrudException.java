package cleancode.exceptions;

public class CrudException extends SistemaException {
    public CrudException(String mensagem) {
        super(mensagem);
    }

    public CrudException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
