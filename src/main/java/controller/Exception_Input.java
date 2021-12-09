package controller;


/**
 * Exceptie pe care o folosim atunci cand instanta pusa in RegistrationSystem nu exista
 * sau din cauza acestora nu se pot efectua metodele apelate
 */
public class Exception_Input extends Exception{

    public Exception_Input(String message){super(message);}
}
