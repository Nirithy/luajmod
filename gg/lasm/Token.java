package lasm;

import java.io.Serializable;

public class Token implements Serializable {
    public int beginColumn;
    public int beginLine;
    public int endColumn;
    public int endLine;
    public String image;
    public int kind;
    public Token next;
    private static final long serialVersionUID = 1L;
    public Token specialToken;

    public Token() {
    }

    public Token(int kind) {
        this(kind, null);
    }

    public Token(int kind, String image) {
        this.kind = kind;
        this.image = image;
    }

    public Object getValue() {
        return null;
    }

    public static Token newToken(int ofKind) {
        return Token.newToken(ofKind, null);
    }

    public static Token newToken(int ofKind, String image) {
        return new Token(ofKind, image);
    }

    @Override
    public String toString() {
        return this.image;
    }
}

