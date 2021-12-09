package controller;

/**
 * Exceptie pe care o folosim atunci cand limita de credite este atinsa
 */

public class Exception_LimitECTS extends RuntimeException{

    private final int id;

    public Exception_LimitECTS(String message, int id) {
        super(message);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
