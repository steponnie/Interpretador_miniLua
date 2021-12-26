package interpreter.expr;

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
        TableValue table = (TableValue) base.expr();
        Value<?> ret = table.value().get(index.expr());
        return ret;
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
