package interpreter.command;

import interpreter.expr.Expr;

public class RepeatCommand extends Command {

    private Expr expr;
    private Command cmds;

    public RepeatCommand(int line, Command cmds, Expr expr) {
        super(line);
        this.cmds = cmds;
        this.expr = expr;
        //TODO Auto-generated constructor stub
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        
    }
    
}
