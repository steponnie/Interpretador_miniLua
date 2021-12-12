package syntatic;

//import java.util.ArrayList;
//import java.util.List;

//import javax.sound.sampled.SourceDataLine;

/*import interpreter.command.BlocksCommand;
import interpreter.command.Command;
import interpreter.command.PrintCommand;
import interpreter.expr.ConstExpr;
import interpreter.expr.Expr;
import interpreter.value.NumberValue;
import interpreter.value.StringValue;
import interpreter.value.Value;*/
import lexical.Lexeme;
import lexical.LexicalAnalysis;
import lexical.TokenType;

public class SyntaticAnalysis {

    private LexicalAnalysis lex;
    private Lexeme current;

    public SyntaticAnalysis(LexicalAnalysis lex) {
        this.lex = lex;
        this.current = lex.nextToken();
    }

    // public Command start() {
    public void start() {
        // BlocksCommand bc = procCode();
        procCode();
        eat(TokenType.END_OF_FILE);

        // return bc;
    }

    private void advance() {
        System.out.println("Advanced (\"" + current.token + "\", " +
                current.type + ")");
        current = lex.nextToken();
    }

    private void eat(TokenType type) {
        System.out.println("Expected (..., " + type + "), found (\"" +
                current.token + "\", " + current.type + ")");
        if (type == current.type) {
            current = lex.nextToken();
        } else {
            showError();
        }
    }

    private void showError() {
        System.out.printf("%02d: ", lex.getLine());

        switch (current.type) {
            case INVALID_TOKEN:
                System.out.printf("Invalid lexeme [%s]\n", current.token);
                break;
            case UNEXPECTED_EOF:
            case END_OF_FILE:
                System.out.printf("Unexpected end of file\n");
                break;
            default:
                System.out.printf("Lexeme not expected [%s]\n", current.token);
                break;
        }

        System.exit(1);
    }

    // <code> ::= { <cmd> }
    // private BlocksCommand procCode() {
    private void procCode() {
        // int line = lex.getLine();
        // List<Command> cmds = new ArrayList<Command>();
        while (current.type == TokenType.IF ||
                current.type == TokenType.WHILE ||
                current.type == TokenType.REPEAT ||
                current.type == TokenType.FOR ||
                current.type == TokenType.PRINT ||
                current.type == TokenType.ID) {
            procCmd();
            // Command cmd = procCmd();
            // cmds.add(cmd);
        }

        // BlocksCommand bc = new BlocksCommand(line, cmds);
        // return bc;
    }

    // <cmd> ::= (<if> | <while> | <repeat> | <for> | <print> | <assign>) [';']
    // private Command procCmd() {
    private void procCmd() {
        // Command cmd = null;
        if (current.type == TokenType.IF) {
            procIf();
        } else if (current.type == TokenType.WHILE) {
            procWhile();
        } else if (current.type == TokenType.REPEAT) {
            procRepeat();
        } else if (current.type == TokenType.FOR) {
            procFor();
        } else if (current.type == TokenType.PRINT) {
            // cmd = procPrint();
            procPrint();
        } else if (current.type == TokenType.ID) {
            procAssign();
        } else {
            showError();
        }

        if (current.type == TokenType.SEMI_COLON)
            advance();

        // return cmd;
    }

    // <if> ::= if <expr> then <code> { elseif <expr> then <code> } [ else <code> ]
    // end
    private void procIf() {
        eat(TokenType.IF);
        procExpr();
        eat(TokenType.THEN);
        procCode();

        while (current.type == TokenType.ELSEIF) {
            advance();
            procExpr();
            eat(TokenType.THEN);
            procCode();
        }

        if (current.type == TokenType.ELSE) {
            advance();
            procCode();
        }

        eat(TokenType.END);
    }

    // <while> ::= while <expr> do <code> end
    private void procWhile() {
        eat(TokenType.WHILE);
        procExpr();
        eat(TokenType.DO);
        procCode();
        eat(TokenType.END);
    }

    // <repeat> ::= repeat <code> until <expr>
    private void procRepeat() {
        eat(TokenType.REPEAT);
        procCode();
        eat(TokenType.UNTIL);
        procExpr();
    }

    // <for> ::= for <name> (('=' <expr> ',' <expr> [',' <expr>]) | ([',' <name>] in
    // <expr>)) do <code> end
    private void procFor() {
        eat(TokenType.FOR);
        procName();
        if (current.type == TokenType.ASSIGN) {
            advance();
            procExpr();
            eat(TokenType.COLON);
            procExpr();
            if (current.type == TokenType.COLON) {
                advance();
                procExpr();
            }
        } else if (current.type == TokenType.COLON || current.type == TokenType.IN) {
            if(current.type == TokenType.COLON){
                advance();
                procName();
            }
            advance();
            procExpr();
        }
        eat(TokenType.DO);
        procCode();
        eat(TokenType.END);
    }

    // <print> ::= print '(' [ <expr> ] ')'
    // private PrintCommand procPrint() {
    private void procPrint() {
        eat(TokenType.PRINT);
        eat(TokenType.OPEN_PAR);
        // Expr expr = null;
        if (current.type == TokenType.OPEN_PAR ||
                current.type == TokenType.SUB ||
                current.type == TokenType.SIZE ||
                current.type == TokenType.NOT ||
                current.type == TokenType.NUMBER ||
                current.type == TokenType.STRING ||
                current.type == TokenType.FALSE ||
                current.type == TokenType.TRUE ||
                current.type == TokenType.NIL ||
                current.type == TokenType.READ ||
                current.type == TokenType.TONUMBER ||
                current.type == TokenType.TOSTRING ||
                current.type == TokenType.OPEN_CUR ||
                current.type == TokenType.ID) {
            // expr = procExpr();
            procExpr();
        }
        // int line = lex.getLine();
        eat(TokenType.CLOSE_PAR);

        // PrintCommand pc = new PrintCommand(line, expr);
        // return pc;
    }

    // <assign> ::= <lvalue> { ',' <lvalue> } '=' <expr> { ',' <expr> }
    private void procAssign() {
        procLValue();
        while (current.type == TokenType.COLON) {
            advance();
            procLValue();
        }
        eat(TokenType.ASSIGN);
        procExpr();
        while (current.type == TokenType.COLON) {
            advance();
            procExpr();
        }
    }

    // <expr> ::= <rel> { (and | or) <rel> }
    // private Expr procExpr() {
    private void procExpr() {
        // Expr expr = procRel();
        procRel();
        while (current.type == TokenType.AND || current.type == TokenType.OR) {
            advance();

            // FIXME: Implement me!
            procRel();
        }

        // return expr;
    }

    // <rel> ::= <concat> [ ('<' | '>' | '<=' | '>=' | '~=' | '==') <concat> ]
    // private Expr procRel() {
    private void procRel() {
        // Expr expr = procConcat();
        procConcat();
        if (current.type == TokenType.LOWER_THAN || current.type == TokenType.GREATER_THAN ||
                current.type == TokenType.LOWER_EQUAL || current.type == TokenType.GREATER_EQUAL ||
                current.type == TokenType.NOT_EQUAL || current.type == TokenType.EQUAL) {
            advance();
            procConcat();
        }
        // FIXME: implement me!

        // return expr;
    }

    // <concat> ::= <arith> { '..' <arith> }
    // private Expr procConcat() {
    private void procConcat() {
        // Expr expr = procArith();
        procArith();
        // FIXME: implement me!
        while (current.type == TokenType.CONCAT) {
            advance();
            procArith();
        }
        // return expr;
    }

    // <arith> ::= <term> { ('+' | '-') <term> }
    // private Expr procArith() {
    private void procArith() {
        // Expr expr = procTerm();
        procTerm();
        while (current.type == TokenType.ADD || current.type == TokenType.SUB) {
            advance();
            procTerm();
        }
        // FIXME: implement me!

        // return expr;
    }

    // <term> ::= <factor> { ('*' | '/' | '%') <factor> }
    // private Expr procTerm() {
    private void procTerm() {
        // Expr expr = procFactor();
        procFactor();
        while (current.type == TokenType.MUL ||
                current.type == TokenType.DIV ||
                current.type == TokenType.MOD) {
            advance();
            procFactor();
        }
        // FIXME: implement me! FEITO

        // return expr;
    }

    // <factor> ::= '(' <expr> ')' | [ '-' | '#' | not ] <rvalue>
    // private Expr procFactor() {
    private void procFactor() {
        // Expr expr = null;
        if (current.type == TokenType.OPEN_PAR) {
            advance();
            procExpr();
            eat(TokenType.CLOSE_PAR);
            // FIXME: implement me!
        } else {
            if (current.type == TokenType.SUB ||
                    current.type == TokenType.SIZE ||
                    current.type == TokenType.NOT) {
                advance();
            }
            // FIXME: implement me!

            // expr = procRValue();
            procRValue();
        }
        // return expr;
    }

    // <lvalue> ::= <name> { '.' <name> | '[' <expr> ']' }
    private void procLValue() {
        procName();
        while (current.type == TokenType.DOT || current.type == TokenType.OPEN_BRA) {
            if (current.type == TokenType.DOT) {
                advance();
                procName();
            } else {
                eat(TokenType.OPEN_BRA);
                procExpr();
                eat(TokenType.CLOSE_BRA);
            }
        }
    }

    // <rvalue> ::= <const> | <function> | <table> | <lvalue>
    // private Expr procRValue() {
    private void procRValue() {
        // Expr expr = null;
        if (current.type == TokenType.NUMBER ||
                current.type == TokenType.STRING ||
                current.type == TokenType.FALSE ||
                current.type == TokenType.TRUE ||
                current.type == TokenType.NIL) {
            // Value<?> v = procConst();
            procConst();
            // int line = lex.getLine();
            // expr = new ConstExpr(line, v);
        } else if (current.type == TokenType.READ ||
                current.type == TokenType.TONUMBER ||
                current.type == TokenType.TOSTRING) {
            procFunction();
        } else if (current.type == TokenType.OPEN_CUR) {
            procTable();
        } else {
            procLValue();
        }

        // return expr;
    }

    // <const> ::= <number> | <string> | false | true | nil
    // private Value<?> procConst() {
    private void procConst() {
        // Value<?> v = null;
        if (current.type == TokenType.NUMBER) {
            // v = procNumber();
            procNumber();
        } else if (current.type == TokenType.STRING) {
            // v = procString();
            procString();
        } else if (current.type == TokenType.FALSE) {
            advance();
        } else if (current.type == TokenType.TRUE) {
            advance();
        } else if (current.type == TokenType.NIL) {
            advance();
        } else {
            showError();
        }

        // return v;
    }

    // <function> ::= (read | tonumber | tostring) '(' [ <expr> ] ')'
    private void procFunction() {
        if (current.type == TokenType.READ || current.type == TokenType.TONUMBER
                || current.type == TokenType.TOSTRING
                ) {
            advance();
        }
        eat(TokenType.OPEN_PAR);
        if (current.type == TokenType.AND || current.type == TokenType.OR || current.type == TokenType.OPEN_PAR ||
            current.type == TokenType.SUB ||
            current.type == TokenType.SIZEc||
            current.type == TokenType.NOT ||
            current.type == TokenType.NUMBER ||
            current.type == TokenType.STRING ||
            current.type == TokenType.FALSE ||
            current.type == TokenType.TRUE ||
            current.type == TokenType.NIL ||
            current.type == TokenType.READ ||
            current.type == TokenType.TONUMBER ||
            current.type == TokenType.TOSTRING ||
            current.type == TokenType.OPEN_CUR ||
            current.type == TokenType.ID) {

            procExpr();

        }
        eat(TokenType.CLOSE_PAR);
    }

    // <table> ::= '{' [ <elem> { ',' <elem> } ] '}'
    private void procTable() {
        eat(TokenType.OPEN_CUR);
        if (current.type == TokenType.OPEN_BRA) {
            procElem();
            while (current.type == TokenType.COLON) {
                advance();
                procElem();
            }
        }
        eat(TokenType.CLOSE_CUR);
    }

    // <elem> ::= [ '[' <expr> ']' '=' ] <expr>
    private void procElem() {
        if (current.type == TokenType.OPEN_BRA) {
            advance();
            procExpr();
            eat(TokenType.CLOSE_BRA);
            eat(TokenType.ASSIGN);
        }
        procExpr();
    }

    private void procName() {
        eat(TokenType.ID);
    }

    // private NumberValue procNumber() {
    private void procNumber() {
        // String tmp = current.token;
        eat(TokenType.NUMBER);

        // Double v = Double.valueOf(tmp);
        // NumberValue nv = new NumberValue(v);
        // return nv;
    }

    // private StringValue procString() {
    private void procString() {
        // String name = current.token;
        eat(TokenType.STRING);

        // StringValue sv = new StringValue(name);
        // return sv;
    }
}