package interpreter.command;

import interpreter.expr.Expr;

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
        while(expr.expr().eval()){
            cmds.execute();
        }      
    }
    
}
