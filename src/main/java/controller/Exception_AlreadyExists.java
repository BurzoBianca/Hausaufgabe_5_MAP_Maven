package controller;

/**
 * Exceptie pe care o folosim daca ceea ce cautam/adaugam exista deja in lista noastra
 */
public class Exception_AlreadyExists extends RuntimeException{

    public Exception_AlreadyExists(String message) {
        super(message);
    }
}
