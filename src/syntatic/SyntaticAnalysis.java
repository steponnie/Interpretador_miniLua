package syntatic;

import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;
import java.util.List;

import javax.sound.sampled.SourceDataLine;

import interpreter.command.AssignCommand;
import interpreter.command.BlocksCommand;
import interpreter.command.Command;
import interpreter.command.GenericForCommand;
import interpreter.command.IfCommand;
import interpreter.command.NumericForCommand;
import interpreter.command.PrintCommand;
import interpreter.command.RepeatCommand;
import interpreter.command.WhileCommand;
import interpreter.expr.ConstExpr;
import interpreter.expr.Expr;
import interpreter.expr.SetExpr;
import interpreter.expr.Variable;
import interpreter.value.BooleanValue;
import interpreter.value.NumberValue;
import interpreter.value.StringValue;
import interpreter.value.TableValue;
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
        BlocksCommand bc = procCode();
        eat(TokenType.END_OF_FILE);

        return bc;
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
            cmd  = procIf();
        } else if (current.type == TokenType.WHILE) {
            cmd = procWhile();
        } else if (current.type == TokenType.REPEAT) {
            cmd = procRepeat();
        } else if (current.type == TokenType.FOR) {
            cmd  = procFor();
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

    // <if> ::= if <expr> then <code> { elseif <expr> then <code> } [ else <code> ] end
    private IfCommand procIf() {
        eat(TokenType.IF);
      	int line = lex.getLine();
        Expr expr = procExpr();
        eat(TokenType.THEN);
        Command thenCmds = procCode();
      
        while (current.type == TokenType.ELSEIF) {
            advance();
            procExpr();
            eat(TokenType.THEN);
            procCode();
        }     	
			
        if (current.type == TokenType.ELSE) {
            advance();
            Command elseCmds = procCode();
          	setElseCommands(elseCmds);
        }

        eat(TokenType.END);
      
      	IfCommand ifc =  new IfCommand(line, expr, thenCmds);
      	return ifc;
    }

    private void setElseCommands(Command elseCmds) {
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
    // <for> ::= for <name> (('=' <expr> ',' <expr> [',' <expr>]) | ([',' <name>] in <expr>)) do <code> end
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
            if(current.type == TokenType.COLON){
                advance();
                line = lex.getLine();
                var2 = new Variable(line, current.token);
                procName();
            }
            advance();
            expr1 = procExpr();
          	
        }
        eat(TokenType.DO);
        Command cmds = procCode();
        eat(TokenType.END);
      
      	if (var2 == null){
          NumericForCommand fc = new NumericForCommand(line, var1, expr1, expr2, expr3, cmds);
        } else {
          GenericForCommand fc = new GenericForCommand(line, var1, var2, expr1, cmds);
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
        Expr expr = procRel();
        procRel();
        while (current.type == TokenType.AND || current.type == TokenType.OR) {
            advance();

            // FIXME: Implement me!
            procRel();
        }

        return expr;
    }

    // <rel> ::= <concat> [ ('<' | '>' | '<=' | '>=' | '~=' | '==') <concat> ]
    private Expr procRel() {
        Expr expr = procConcat();
        procConcat();
        if (current.type == TokenType.LOWER_THAN || current.type == TokenType.GREATER_THAN ||
                current.type == TokenType.LOWER_EQUAL || current.type == TokenType.GREATER_EQUAL ||
                current.type == TokenType.NOT_EQUAL || current.type == TokenType.EQUAL) {
            advance();
            procConcat();
        }
        // FIXME: implement me!

        return expr;
    }

    // <concat> ::= <arith> { '..' <arith> }
    private Expr procConcat() {
        Expr expr = procArith();
        procArith();
        // FIXME: implement me!
        while (current.type == TokenType.CONCAT) {
            advance();
            procArith();
        }
        return expr;
    }

    // <arith> ::= <term> { ('+' | '-') <term> }
    private Expr procArith() {
        Expr expr = procTerm();
        procTerm();
        while (current.type == TokenType.ADD || current.type == TokenType.SUB) {
            advance();
            procTerm();
        }
        // FIXME: implement me!

        return expr;
    }

    // <term> ::= <factor> { ('*' | '/' | '%') <factor> }
    private Expr procTerm() {
        Expr expr = procFactor();
        procFactor();
        while (current.type == TokenType.MUL ||
                current.type == TokenType.DIV ||
                current.type == TokenType.MOD) {
            advance();
            procFactor();
        }
        // FIXME: implement me! FEITO

        return expr;
    }

    // <factor> ::= '(' <expr> ')' | [ '-' | '#' | not ] <rvalue>
    private Expr procFactor() {
        Expr expr = null;
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

            expr = procRValue();
        }
        return expr;
    }

    // <lvalue> ::= <name> { '.' <name> | '[' <expr> ']' }
    private SetExpr procLValue() {
        SetExpr expr = procName();
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
        // FIX ME!!!!!
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
            procFunction();
        } else if (current.type == TokenType.OPEN_CUR) {
            procTable();
        } else {
            procLValue();
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
    private void procFunction() {
        if (current.type == TokenType.READ || current.type == TokenType.TONUMBER
                || current.type == TokenType.TOSTRING
                ) {
            advance();
        }
        eat(TokenType.OPEN_PAR);
        if (current.type == TokenType.AND || current.type == TokenType.OR || current.type == TokenType.OPEN_PAR ||
            current.type == TokenType.SUB ||
            current.type == TokenType.SIZE||
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
    private TableValue procTable() {
        eat(TokenType.OPEN_CUR);
      	int line = lex.getLine();
        if (current.type == TokenType.OPEN_BRA) {      	
            Expr expr = procElem();          	
            while (current.type == TokenType.COLON) {
                advance();
                procElem();
            }
        }
        eat(TokenType.CLOSE_CUR);
        return null;
      
    }

    // <elem> ::= [ '[' <expr> ']' '=' ] <expr>
    private Expr procElem() {
        if (current.type == TokenType.OPEN_BRA) {
            advance();
            procExpr();
            eat(TokenType.CLOSE_BRA);
            eat(TokenType.ASSIGN);
        }
        procExpr();
        return null;
    }

    private Variable procName() { 
      	String name = current.token;
        eat(TokenType.ID);
      	
       int line = lex.getLine();
       Variable v = new Variable(line, name);
       return v;
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