package interpreter.expr;

import interpreter.value.Value;

public class AcessExpr extends SetExpr {

    private Expr base;
    private Expr index;

    public AcessExpr(int line, Expr base, Expr index) {
        super(line);
        this.base = base;
        this.index = index;
    }
    
    @Override
    public Value<?> expr() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void setValue(Value<?> value) {
        // TODO Auto-generated method stub
        
    }

    
}
