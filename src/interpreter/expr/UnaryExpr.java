package interpreter.expr;

import interpreter.util.Utils;
import interpreter.value.NumberValue;
import interpreter.value.StringValue;
import interpreter.value.Value;

public class UnaryExpr extends Expr {
    
    private Expr expr;
    private UnaryOp op;

    public UnaryExpr(int line, Expr expr, UnaryOp op) {
        super(line);
        this.expr = expr;
        this.op = op;
    }

    @Override
    public Value<?> expr() {
        Value<?> v = expr.expr();

        Value<?> ret = null;
        switch (op) {
            case Neg:
                ret = negOp(v);
                break;
            default:
                Utils.abort(super.getLine());
        }

        return ret;
    }

    public Value<?> negOp(Value<?> v) {
        Value<?> ret = null;
        if (v instanceof NumberValue) {
            NumberValue nv = (NumberValue) v;
            Double d = -nv.value();
            
            ret = new NumberValue(d);
        } else if (v instanceof StringValue) {
            StringValue sv = (StringValue) v;
            String s = sv.value();

            try {
                Double d = -Double.valueOf(s);
                ret = new NumberValue(d);
            } catch (Exception e) {
                Utils.abort(super.getLine());
            }
        } else {
            Utils.abort(super.getLine());
        }

        return ret;
    }

}
