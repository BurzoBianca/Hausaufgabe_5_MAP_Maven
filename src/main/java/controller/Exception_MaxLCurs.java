package controller;

/**
 * Exceptie pe care o folosim atunci cand lista de studenti inscrisi a unui curs atinge capacitatea MAX
 */
public class Exception_MaxLCurs extends RuntimeException{

    public Exception_MaxLCurs(String message) {
        super(message);
    }
}
