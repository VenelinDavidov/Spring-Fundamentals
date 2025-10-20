package main.exception;

public class SpellNotAvailableException extends RuntimeException {


    public SpellNotAvailableException(String message) {
        super (message);
    }
}
