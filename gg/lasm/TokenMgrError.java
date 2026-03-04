package lasm;

public class TokenMgrError extends Error {
    static final int INVALID_LEXICAL_STATE = 2;
    static final int LEXICAL_ERROR = 0;
    static final int LOOP_DETECTED = 3;
    static final int STATIC_LEXER_ERROR = 1;
    int errorCode;
    int errorColumn;
    int errorLine;
    private static final long serialVersionUID = 1L;

    public TokenMgrError() {
    }

    public TokenMgrError(String message, int reason) {
        super(message);
        this.errorCode = reason;
    }

    public TokenMgrError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar, int reason) {
        this(TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar), reason);
        this.errorLine = errorLine;
        this.errorColumn = errorColumn;
    }

    protected static String LexicalError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar) {
        StringBuilder stringBuilder0 = new StringBuilder("Lexical error at line ").append(errorLine).append(", column ").append(errorColumn).append(".  Encountered: ");
        return EOFSeen ? stringBuilder0.append("<EOF> ").append("after : \"").append(TokenMgrError.addEscapes(errorAfter)).append("\"").toString() : stringBuilder0.append("\"" + TokenMgrError.addEscapes(String.valueOf(curChar)) + "\"" + " (" + ((int)curChar) + "), ").append("after : \"").append(TokenMgrError.addEscapes(errorAfter)).append("\"").toString();
    }

    protected static final String addEscapes(String str) {
        StringBuffer retval = new StringBuffer();
        for(int i = 0; i < str.length(); ++i) {
            switch(str.charAt(i)) {
                case 0: {
                    break;
                }
                case 8: {
                    retval.append("\\b");
                    break;
                }
                case 9: {
                    retval.append("\\t");
                    break;
                }
                case 10: {
                    retval.append("\\n");
                    break;
                }
                case 12: {
                    retval.append("\\f");
                    break;
                }
                case 13: {
                    retval.append("\\r");
                    break;
                }
                case 34: {
                    retval.append("\\\"");
                    break;
                }
                case 39: {
                    retval.append("\\\'");
                    break;
                }
                case 92: {
                    retval.append("\\\\");
                    break;
                }
                default: {
                    int v1 = str.charAt(i);
                    if(v1 >= 0x20 && v1 <= 0x7E) {
                        retval.append(((char)v1));
                    }
                    else {
                        String s1 = "0000" + Integer.toString(v1, 16);
                        retval.append("\\u" + s1.substring(s1.length() - 4, s1.length()));
                    }
                }
            }
        }
        return retval.toString();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

