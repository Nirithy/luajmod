package lasm;

public class LasmParseException extends ParseException {
    public LasmParseException(Token t, String message) {
        super(message);
        this.currentToken = t;
    }

    public LasmParseException(Token t, String message, Throwable cause) {
        super(message, cause);
        this.currentToken = t;
    }
}

