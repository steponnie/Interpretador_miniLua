package interpreter.command;

import interpreter.expr.Expr;
import interpreter.expr.Variable;
import interpreter.util.Utils;
import interpreter.value.NumberValue;
import interpreter.value.Value;

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

        Value<?> expr1_value = expr1.expr();
        Value<?> expr2_value = expr2.expr();
        if (!(expr1_value instanceof NumberValue) && !(expr2_value instanceof NumberValue)) {
            Utils.abort(super.getLine());
        } else {
            if (expr3 == null) {
                var.setValue(expr1_value);
                Variable cont = var;
                while ((double) cont.expr().value() < (double) expr2_value.value()) {
                    cmds.execute();

                    Double v = Double.valueOf((double) cont.expr().value() + 1);
                    NumberValue nv = new NumberValue(v);
                    cont.setValue(nv);
                }
            } else {
                var.setValue(expr1_value);
                Variable cont = var;
                while ((double) cont.expr().value() < (double) expr2_value.value()) {
                    cmds.execute();

                    Double v = Double.valueOf((double) cont.expr().value() + (double) expr3.expr().value());
                    NumberValue nv = new NumberValue(v);
                    cont.setValue(nv);
                }
            }
        }
    }
}
