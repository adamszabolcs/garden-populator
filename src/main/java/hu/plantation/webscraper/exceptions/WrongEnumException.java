package hu.plantation.webscraper.exceptions;

public class WrongEnumException extends Exception {

    public WrongEnumException() {
        super();
    }

    public WrongEnumException(String message) {
        super(message);
    }

    public WrongEnumException(String message, Throwable cause) {
        super(message, cause);
    }

}
