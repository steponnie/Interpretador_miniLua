package interpreter.command;

import interpreter.expr.Expr;
import interpreter.value.Value;

public class RepeatCommand extends Command {

    private Expr expr;
    private Command cmds;

    public RepeatCommand(int line, Command cmds, Expr expr) {
        super(line);
        this.cmds = cmds;
        this.expr = expr;
    }

    @Override
    public void execute() {
        Value<?> v;
        while((v = expr.expr()) != null && !(v.eval())){
            cmds.execute();
        }      
    }
    
}
