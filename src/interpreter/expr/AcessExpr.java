package interpreter.expr;

import interpreter.util.Utils;
import interpreter.value.TableValue;
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
        if (base.expr() instanceof TableValue && index.expr() != null) {
            TableValue table = (TableValue) base.expr();
            Value<?> ret = table.value().get(index.expr());
            return ret;
        } else {
            Utils.abort(super.getLine());
            return null;
        }
        
    }

    @Override
    public void setValue(Value<?> value) {
        TableValue table = (TableValue) base.expr();
        if (value == null) {
            table.value().remove(index.expr());
        } else {
            table.value().put(index.expr(), value);
        }
    }
}
