package lasm;

public class ParseException extends Exception {
    public Token currentToken;
    protected String eol;
    public int[][] expectedTokenSequences;
    private static final long serialVersionUID = 1L;
    public String[] tokenImage;

    public ParseException() {
        this.eol = System.getProperty("line.separator", "\n");
    }

    public ParseException(String message) {
        super(message);
        this.eol = "\n";
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
        this.eol = "\n";
    }

    public ParseException(Token currentTokenVal, int[][] expectedTokenSequencesVal, String[] tokenImageVal) {
        super(ParseException.initialise(currentTokenVal, expectedTokenSequencesVal, tokenImageVal));
        this.eol = System.getProperty("line.separator", "\n");
        this.currentToken = currentTokenVal;
        this.expectedTokenSequences = expectedTokenSequencesVal;
        this.tokenImage = tokenImageVal;
    }

    static String add_escapes(String str) {
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

    private static String initialise(Token currentToken, int[][] expectedTokenSequences, String[] tokenImage) {
        String s = System.getProperty("line.separator", "\n");
        StringBuffer expected = new StringBuffer();
        int maxSize = 0;
        for(int i = 0; i < expectedTokenSequences.length; ++i) {
            if(maxSize < expectedTokenSequences[i].length) {
                maxSize = expectedTokenSequences[i].length;
            }
            for(int j = 0; j < expectedTokenSequences[i].length; ++j) {
                expected.append(tokenImage[expectedTokenSequences[i][j]]).append(' ');
            }
            if(expectedTokenSequences[i][expectedTokenSequences[i].length - 1] != 0) {
                expected.append("...");
            }
            expected.append(s).append("    ");
        }
        String retval = "Encountered \"";
        Token tok = currentToken.next;
        for(int i = 0; i < maxSize; ++i) {
            if(i != 0) {
                retval = retval + " ";
            }
            if(tok.kind == 0) {
                retval = retval + tokenImage[0];
                break;
            }
            retval = retval + " " + tokenImage[tok.kind] + " \"" + ParseException.add_escapes(tok.image) + " \"";
            tok = tok.next;
        }
        String retval = retval + "\" at line " + currentToken.next.beginLine + ", column " + currentToken.next.beginColumn + "." + s;
        return expectedTokenSequences.length == 1 ? retval + "Was expecting:" + s + "    " + expected.toString() : retval + "Was expecting one of:" + s + "    " + expected.toString();
    }
}

