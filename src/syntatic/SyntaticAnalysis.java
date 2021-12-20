package syntatic;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import interpreter.command.AssignCommand;
import interpreter.command.BlocksCommand;
import interpreter.command.Command;
import interpreter.command.GenericForCommand;
import interpreter.command.IfCommand;
import interpreter.command.NumericForCommand;
import interpreter.command.PrintCommand;
import interpreter.command.RepeatCommand;
import interpreter.command.WhileCommand;
import interpreter.expr.AcessExpr;
import interpreter.expr.BinaryExpr;
import interpreter.expr.BinaryOp;
import interpreter.expr.ConstExpr;
import interpreter.expr.Expr;
import interpreter.expr.SetExpr;
import interpreter.expr.TableEntry;
import interpreter.expr.TableExpr;
import interpreter.expr.UnaryExpr;
import interpreter.expr.UnaryOp;
import interpreter.expr.Variable;
import interpreter.value.BooleanValue;
import interpreter.value.NumberValue;
import interpreter.value.StringValue;
import interpreter.value.Value;
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

    public Command start() {
        Command bc = procCode();
        eat(TokenType.END_OF_FILE);

        return bc;
    }

    private void advance() {
        // System.out.println("Advanced (\"" + current.token + "\", " +
        // current.type + ")");
        current = lex.nextToken();
    }

    private void eat(TokenType type) {
        // System.out.println("Expected (..., " + type + "), found (\"" +
        // current.token + "\", " + current.type + ")");
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
    private BlocksCommand procCode() {
        int line = lex.getLine();
        List<Command> cmds = new ArrayList<Command>();
        while (current.type == TokenType.IF ||
                current.type == TokenType.WHILE ||
                current.type == TokenType.REPEAT ||
                current.type == TokenType.FOR ||
                current.type == TokenType.PRINT ||
                current.type == TokenType.ID) {
            Command cmd = procCmd();
            cmds.add(cmd);
        }

        BlocksCommand bc = new BlocksCommand(line, cmds);
        return bc;
    }

    // <cmd> ::= (<if> | <while> | <repeat> | <for> | <print> | <assign>) [';']
    private Command procCmd() {
        Command cmd = null;
        if (current.type == TokenType.IF) {
            cmd = procIf();
        } else if (current.type == TokenType.WHILE) {
            cmd = procWhile();
        } else if (current.type == TokenType.REPEAT) {
            cmd = procRepeat();
        } else if (current.type == TokenType.FOR) {
            cmd = procFor();
        } else if (current.type == TokenType.PRINT) {
            cmd = procPrint();
        } else if (current.type == TokenType.ID) {
            cmd = procAssign();
        } else {
            showError();
        }

        if (current.type == TokenType.SEMI_COLON)
            advance();

        return cmd;
    }

    // <if> ::= if <expr> then <code> { elseif <expr> then <code> } [ else <code> ]
    // end
    private IfCommand procIf() {
        eat(TokenType.IF);
        int line = lex.getLine();
        Expr expr1 = procExpr();
        eat(TokenType.THEN);
        Command thenCmds = procCode();
        IfCommand ifc = new IfCommand(line, expr1, thenCmds);
        ArrayList<IfCommand> ifs = new ArrayList<>();
        ifs.add(ifc);

        int i = 0;
        while (current.type == TokenType.ELSEIF) {
            advance();
            line = lex.getLine();
            Expr expr2 = procExpr();
            eat(TokenType.THEN);
            thenCmds = procCmd();
            IfCommand elseifc = new IfCommand(line, expr2, thenCmds);
            ifs.get(i).setElseCommands(elseifc);
            ifs.add(elseifc);
            i++;
        }

        if (current.type == TokenType.ELSE) {
            advance();
            Command elseCmds = procCode();
            ifs.get(i).setElseCommands(elseCmds);
        }
        eat(TokenType.END);
        return ifc;
    }

    // <while> ::= while <expr> do <code> end
    private WhileCommand procWhile() {
        eat(TokenType.WHILE);
        int line = lex.getLine();

        Expr expr = procExpr();
        eat(TokenType.DO);
        Command cmds = procCode();
        eat(TokenType.END);

        WhileCommand wc = new WhileCommand(line, expr, cmds);
        return wc;
    }

    // <repeat> ::= repeat <code> until <expr>
    private RepeatCommand procRepeat() {
        eat(TokenType.REPEAT);
        int line = lex.getLine();
        Command cmds = procCode();
        eat(TokenType.UNTIL);
        Expr expr = procExpr();

        RepeatCommand rc = new RepeatCommand(line, cmds, expr);
        return rc;
    }

    // <for> ::= for <name> (('=' <expr> ',' <expr> [',' <expr>]) | ([',' <name>] in
    // <expr>)) do <code> end
    private Command procFor() {
        eat(TokenType.FOR);
        int line = lex.getLine();
        Variable var1 = new Variable(line, current.token);
        Variable var2 = null;
        procName();
        Expr expr1 = null;
        Expr expr2 = null;
        Expr expr3 = null;
        if (current.type == TokenType.ASSIGN) {
            advance();
            expr1 = procExpr();
            eat(TokenType.COLON);
            expr2 = procExpr();
            if (current.type == TokenType.COLON) {
                advance();
                expr3 = procExpr();
            }
        } else if (current.type == TokenType.COLON || current.type == TokenType.IN) {
            if (current.type == TokenType.COLON) {
                advance();
                line = lex.getLine();
                var2 = procName();
            }
            advance();
            expr1 = procExpr();

        }
        eat(TokenType.DO);
        Command cmds = procCode();
        eat(TokenType.END);
        Command fc = null;
        if (var2 == null) {
            fc = new NumericForCommand(line, var1, expr1, expr2, expr3, cmds);
        } else {
            fc = new GenericForCommand(line, var1, var2, expr1, cmds);
        }
        return fc;
    }

    // <print> ::= print '(' [ <expr> ] ')'
    private PrintCommand procPrint() {
        eat(TokenType.PRINT);
        eat(TokenType.OPEN_PAR);
        Expr expr = null;
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
            expr = procExpr();
        }
        int line = lex.getLine();
        eat(TokenType.CLOSE_PAR);

        PrintCommand pc = new PrintCommand(line, expr);
        return pc;
    }

    // <assign> ::= <lvalue> { ',' <lvalue> } '=' <expr> { ',' <expr> }
    private AssignCommand procAssign() {
        Vector<SetExpr> lhs = new Vector<SetExpr>();
        Vector<Expr> rhs = new Vector<Expr>();

        lhs.add(procLValue());

        while (current.type == TokenType.COLON) {
            advance();
            lhs.add(procLValue());
        }

        eat(TokenType.ASSIGN);
        int line = lex.getLine();

        rhs.add(procExpr());
        while (current.type == TokenType.COLON) {
            advance();
            rhs.add(procExpr());
        }

        AssignCommand ac = new AssignCommand(line, lhs, rhs);
        return ac;
    }

    // <expr> ::= <rel> { (and | or) <rel> }
    private Expr procExpr() {
        Expr left = procRel();
        Expr right = null;
        BinaryOp op = null;
        BinaryExpr expr = null;
        int line = lex.getLine();
        while (current.type == TokenType.AND || current.type == TokenType.OR) {
            if (current.type == TokenType.AND) {
                op = BinaryOp.AndOp;
            } else {
                op = BinaryOp.OrOp;
            }
            advance();
            right = procTerm();
            expr = new BinaryExpr(line, left, op, right);
            left = expr;
        }
        return left;
    }

    // <rel> ::= <concat> [ ('<' | '>' | '<=' | '>=' | '~=' | '==') <concat> ]
    private Expr procRel() {
        Expr expr = procConcat();
        Expr right = null;
        BinaryOp op = null;
        int line = lex.getLine();
        if (current.type == TokenType.LOWER_THAN) {
            op = BinaryOp.LowerThanOp;
            advance();
            right = procConcat();
            expr = new BinaryExpr(line, expr, op, right);
        } else if (current.type == TokenType.GREATER_THAN) {
            op = BinaryOp.GreaterThanOp;
            advance();
            right = procConcat();
            expr = new BinaryExpr(line, expr, op, right);
        } else if (current.type == TokenType.LOWER_EQUAL) {
            op = BinaryOp.LowerEqualOp;
            advance();
            right = procConcat();
            expr = new BinaryExpr(line, expr, op, right);
        } else if (current.type == TokenType.GREATER_EQUAL) {
            op = BinaryOp.GreaterEqualOp;
            advance();
            right = procConcat();
            expr = new BinaryExpr(line, expr, op, right);
            ;
        } else if (current.type == TokenType.NOT_EQUAL) {
            op = BinaryOp.NotEqualOp;
            advance();
            right = procConcat();
            expr = new BinaryExpr(line, expr, op, right);
        } else if (current.type == TokenType.EQUAL) {
            op = BinaryOp.EqualOp;
            advance();
            right = procConcat();
            expr = new BinaryExpr(line, expr, op, right);
            ;
        }
        return expr;
    }

    // <concat> ::= <arith> { '..' <arith> }
    private Expr procConcat() {
        Expr left = procArith();
        Expr right = null;
        BinaryOp op = null;
        BinaryExpr expr = null;
        int line = lex.getLine();
        while (current.type == TokenType.CONCAT) {
            op = BinaryOp.ConcatOp;
            advance();
            right = procArith();
            expr = new BinaryExpr(line, left, op, right);
            left = expr;
        }
        return left;
    }

    // <arith> ::= <term> { ('+' | '-') <term> }
    private Expr procArith() {
        Expr left = procTerm();
        Expr right = null;
        BinaryOp op = null;
        BinaryExpr expr = null;
        int line = lex.getLine();
        while (current.type == TokenType.ADD || current.type == TokenType.SUB) {
            if (current.type == TokenType.ADD) {
                op = BinaryOp.AddOp;
            } else {
                op = BinaryOp.SubOp;
            }
            advance();
            right = procTerm();
            expr = new BinaryExpr(line, left, op, right);
            left = expr;
        }
        return left;
    }

    // <term> ::= <factor> { ('*' | '/' | '%') <factor> }
    // factor % factor / factor
    private Expr procTerm() {
        Expr left = procFactor();
        Expr right = null;
        BinaryOp op = null;
        BinaryExpr expr = null;
        int line = lex.getLine();
        while (current.type == TokenType.MUL || current.type == TokenType.DIV || current.type == TokenType.MOD) {
            if (current.type == TokenType.MUL) {
                op = BinaryOp.MulOp;
            } else if (current.type == TokenType.DIV) {
                op = BinaryOp.DivOp;
            } else {
                op = BinaryOp.ModOp;
            }
            advance();
            right = procFactor();
            expr = new BinaryExpr(line, left, op, right);
            left = expr;
        }

        return left;
    }

    // <factor> ::= '(' <expr> ')' | [ '-' | '#' | not ] <rvalue>
    private Expr procFactor() {
        Expr expr = null;
        UnaryExpr unaryExpr = null;
        UnaryOp op = null;
        int line = lex.getLine();
        if (current.type == TokenType.OPEN_PAR) {
            advance();
            expr = procExpr();
            eat(TokenType.CLOSE_PAR);
        } else {
            if (current.type == TokenType.SUB) {
                op = UnaryOp.Neg;
                advance();
                expr = procRValue();
                unaryExpr = new UnaryExpr(line, expr, op);
                return unaryExpr;
            } else if (current.type == TokenType.SIZE) {
                op = UnaryOp.Size;
                advance();
                expr = procRValue();
                unaryExpr = new UnaryExpr(line, expr, op);
                return unaryExpr;
            } else if (current.type == TokenType.NOT) {
                op = UnaryOp.Not;
                advance();
                expr = procRValue();
                unaryExpr = new UnaryExpr(line, expr, op);
                return unaryExpr;
            }
            expr = procRValue();
        }
        return expr;
    }

    // <lvalue> ::= <name> { '.' <name> | '[' <expr> ']' }
    private SetExpr procLValue() {
        SetExpr expr = procName();
        int line = lex.getLine();

        while (current.type == TokenType.DOT || current.type == TokenType.OPEN_BRA) {
            if (current.type == TokenType.DOT) {
                advance();
                Variable index = procName();
                index.setValue(new StringValue(index.getName()));

                AcessExpr acessExpr = new AcessExpr(line, expr, index);
                expr = (AcessExpr) acessExpr;
            } else {
                advance();
                Expr index = procExpr();

                AcessExpr acessExpr = new AcessExpr(line, expr, index);
                expr = (AcessExpr) acessExpr;
                eat(TokenType.CLOSE_BRA);
            }
        }
        return expr;
    }

    // <rvalue> ::= <const> | <function> | <table> | <lvalue>
    private Expr procRValue() {
        Expr expr = null;
        if (current.type == TokenType.NUMBER ||
                current.type == TokenType.STRING ||
                current.type == TokenType.FALSE ||
                current.type == TokenType.TRUE ||
                current.type == TokenType.NIL) {
            Value<?> v = procConst();
            int line = lex.getLine();
            expr = new ConstExpr(line, v);
        } else if (current.type == TokenType.READ ||
                current.type == TokenType.TONUMBER ||
                current.type == TokenType.TOSTRING) {
            expr = procFunction();
        } else if (current.type == TokenType.OPEN_CUR) {
            expr = procTable();
        } else {
            expr = procLValue();
        }
        return expr;
    }

    // <const> ::= <number> | <string> | false | true | nil
    private Value<?> procConst() {
        Value<?> v = null;
        if (current.type == TokenType.NUMBER) {
            v = procNumber();
        } else if (current.type == TokenType.STRING) {
            v = procString();
        } else if (current.type == TokenType.FALSE) {
            v = new BooleanValue(false);
            advance();
        } else if (current.type == TokenType.TRUE) {
            v = new BooleanValue(true);
            advance();
        } else if (current.type == TokenType.NIL) {
            v = null;
            advance();
        } else {
            showError();
        }

        return v;
    }

    // <function> ::= (read | tonumber | tostring) '(' [ <expr> ] ')'
    private Expr procFunction() {
        Expr expr = null;
        UnaryOp op = null;
        int line = lex.getLine();
        if (current.type == TokenType.READ) {
            op = UnaryOp.Read;
            advance();
        } else if (current.type == TokenType.TONUMBER) {
            op = UnaryOp.ToNumber;
            advance();
        } else if (current.type == TokenType.TOSTRING) {
            op = UnaryOp.ToString;
            advance();
        }
        eat(TokenType.OPEN_PAR);
        if (current.type == TokenType.AND || current.type == TokenType.OR || current.type == TokenType.OPEN_PAR ||
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

            expr = procExpr();

        }
        eat(TokenType.CLOSE_PAR);
        UnaryExpr unaryExpr = new UnaryExpr(line, expr, op);
        return unaryExpr;
    }

    // <table> ::= '{' [ <elem> { ',' <elem> } ] '}'
    private TableExpr procTable() {
        TableEntry elem = new TableEntry();
        eat(TokenType.OPEN_CUR);
        int line = lex.getLine();
        TableExpr expr = new TableExpr(line);
        if (current.type == TokenType.OPEN_BRA || current.type == TokenType.AND ||
                current.type == TokenType.OR || current.type == TokenType.OPEN_PAR ||
                current.type == TokenType.SUB || current.type == TokenType.SIZE ||
                current.type == TokenType.NOT || current.type == TokenType.NUMBER ||
                current.type == TokenType.STRING || current.type == TokenType.FALSE ||
                current.type == TokenType.TRUE || current.type == TokenType.NIL ||
                current.type == TokenType.READ || current.type == TokenType.TONUMBER ||
                current.type == TokenType.TOSTRING || current.type == TokenType.OPEN_CUR ||
                current.type == TokenType.ID) {
            elem = procElem();
            expr.addEntry(elem.key, elem.value);
            while (current.type == TokenType.COLON) {
                advance();
                elem = procElem();
                expr.addEntry(elem.key, elem.value);
            }
        }
        eat(TokenType.CLOSE_CUR);
        return expr;
    }

    // <elem> ::= [ '[' <expr> ']' '=' ] <expr>
    private TableEntry procElem() {
        TableEntry elem = new TableEntry();
        elem.key = null;
        if (current.type == TokenType.OPEN_BRA) {
            advance();
            elem.key = procExpr();
            eat(TokenType.CLOSE_BRA);
            eat(TokenType.ASSIGN);
        }
        elem.value = procExpr();
        return elem;
    }

    private Variable procName() {
        String name = current.token;
        eat(TokenType.ID);
        int line = lex.getLine();
        Variable var = new Variable(line, name);
        return var;
    }

    private NumberValue procNumber() {
        String tmp = current.token;
        eat(TokenType.NUMBER);

        Double v = Double.valueOf(tmp);
        NumberValue nv = new NumberValue(v);
        return nv;
    }

    private StringValue procString() {
        String name = current.token;
        eat(TokenType.STRING);

        StringValue sv = new StringValue(name);
        return sv;
    }
}