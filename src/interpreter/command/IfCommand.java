package interpreter.command;

import interpreter.expr.Expr;

public class IfCommand extends Command {

    private Expr expr;
    private Command cmds;
    private Command elseCmds;
    
    public IfCommand(int line, Expr expr, Command cmds) {
        super(line);
        this.expr = expr;
        this.cmds = cmds;
    }

    @Override
    public void execute() {
        if (expr.expr().eval()) {
            cmds.execute();
        } else if (this.elseCmds != null){
            elseCmds.execute();
        }
        
    }

    public void setElseCommands(Command elseCmds) {
        this.elseCmds = elseCmds;
    }
    
}
