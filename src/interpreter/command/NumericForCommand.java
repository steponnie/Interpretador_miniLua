package interpreter.command;

import interpreter.expr.Expr;
import interpreter.expr.Variable;
import interpreter.util.Utils;
import interpreter.value.NumberValue;

public class NumericForCommand extends Command {

    private Variable var;
    private Expr expr1;
    private Expr expr2;
    private Expr expr3;
    private Command cmds;

    public NumericForCommand(int line, Variable var, Expr expr1, Expr expr2, Expr expr3, Command cmds) {
        super(line);
        this.var = var;
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.expr3 = expr3;
        this.cmds = cmds;
    }

    public NumericForCommand(int line, Variable var, Expr expr1, Expr expr2, Command cmds) {
        super(line);
        this.var = var;
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.expr3 = null;
        this.cmds = cmds;
    }

    @Override
    public void execute() {
        if (!(expr1.expr() instanceof NumberValue) || !(expr2.expr() instanceof NumberValue)) {
            Utils.abort(super.getLine());
        } else {
            if (expr3 == null) {
                var.setValue(expr1.expr());
                while ((double) var.expr().value() <= (double) expr2.expr().value()) {
                    cmds.execute();

                    Double d = Double.valueOf((double) var.expr().value() + 1);
                    NumberValue nv = new NumberValue(d);
                    var.setValue(nv);
                }
            } else {
                var.setValue(expr1.expr());
                while ((double) var.expr().value() <= (double) expr2.expr().value()) {
                    cmds.execute();

                    Double d = Double.valueOf((double) var.expr().value() + (double) expr3.expr().value());
                    NumberValue nv = new NumberValue(d);
                    var.setValue(nv);
                }
            }
        }
    }
}
