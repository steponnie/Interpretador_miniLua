package interpreter.command;


import interpreter.expr.Expr;
import interpreter.expr.Variable;
import interpreter.util.Utils;
import interpreter.value.TableValue;
import interpreter.value.Value;

public class GenericForCommand extends Command {

    private Variable var1;
    private Variable var2;
    private Expr expr;
    private Command cmds;

    public GenericForCommand(int line, Variable var1, Variable var2, Expr expr, Command cmds) {
        super(line);
        this.var1 = var1;
        this.var2 = var2;
        this.expr = expr;
        this.cmds = cmds;
    }

    public GenericForCommand(int line, Variable var1, Expr expr, Command cmds) {
        super(line);
        this.var1 = var1;
        this.var2 = null;
        this.expr = expr;
        this.cmds = cmds;
    }

    @Override
    public void execute() {
        if ((expr.expr()) == null && !(expr.expr() instanceof TableValue)) {
            Utils.abort(super.getLine());
        }
        TableValue tv = (TableValue) expr.expr();
        for(Value<?> key : tv.value().keySet()){
            this.var1.setValue(key);
            if(var2 != null){
                this.var2.setValue(tv.value().get(key));
            }
            cmds.execute();
        }
        // FIX ME!!!
        
    }
}
