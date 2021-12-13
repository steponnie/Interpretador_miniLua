package interpreter.command;

import interpreter.expr.Expr;

public class IfCommand extends Command {

    private Expr expr;
    private Command cmds;
    
    public IfCommand(int line, Expr expr, Command cmds) {
        super(line);
        this.expr = expr;
        this.cmds = cmds;
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        
    }
    
}
