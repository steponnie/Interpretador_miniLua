package interpreter.expr;

import java.util.Scanner;

import interpreter.util.Utils;
import interpreter.value.BooleanValue;
import interpreter.value.NumberValue;
import interpreter.value.StringValue;
import interpreter.value.TableValue;
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
        Value<?> v;
        Value<?> ret = null;
        switch (op) {
            case Neg:
                v = expr.expr();
                ret = negOp(v);
                break;
            case Size:
                v = expr.expr();
                ret = sizeOp(v);
                break;
            case Not:
                v = expr.expr();
                ret = notOp(v);
                break;
            case Read:
                v = expr == null ? null : expr.expr();
                ret = readOp(v);
                break;
            case ToNumber:
                v = expr == null ? null : expr.expr();
                ret = toNumberOp(v);
                break;
            case ToString:
                v = expr == null ? null : expr.expr();
                ret = toStringOp(v);
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

    public Value<?> sizeOp(Value<?> v) {
        Value<?> ret = null;
        if (v instanceof TableValue) {
            TableValue tv = (TableValue) v;
            Double s = Double.valueOf(tv.value().size());

            ret = new NumberValue(s);
        } else {
            Utils.abort(super.getLine());
        }

        return ret;
    }

    public Value<?> notOp(Value<?> v) {
        if (v == null) {
            return new BooleanValue(true);
        }
        else {
            return new BooleanValue(!v.eval());
        }
    }

    public Value<?> readOp(Value<?> v) {
        Value<?> ret = null;
        if (v instanceof StringValue) {
            StringValue sv = (StringValue) v;
            String s = sv.value();

            System.out.print(s);
        } else {
            Utils.abort(super.getLine());
        }

        Scanner scan = new Scanner(System.in); // VERIFICAR ISSO AQUI
        String input = scan.nextLine();
        ret = new StringValue(input);
        return ret;
    }

    public Value<?> toNumberOp(Value<?> v) {
        Value<?> ret = null;
        if (v instanceof NumberValue) {
            NumberValue nv = (NumberValue) v;
            Double d = nv.value();

            ret = new NumberValue(d);
        } else if (v instanceof StringValue) {
            StringValue sv = (StringValue) v;
            String s = sv.value();

            try {
                Double d = Double.valueOf(s);
                ret = new NumberValue(d);
            } catch (Exception e) {
                ret = null;
            }
        } else {
            ret = null;
        }
        return ret;
    }

    public Value<?> toStringOp(Value<?> v) {
        Value<?> ret = null;
        if (v instanceof NumberValue) {
            NumberValue nv = (NumberValue) v;
            Double d = nv.value();

            ret = new StringValue(d.toString());
        } else if (v instanceof StringValue) {
            StringValue sv = (StringValue) v;
            String s = sv.value();

            ret = new StringValue(s);
        } else if (v instanceof BooleanValue) {
            BooleanValue bv = (BooleanValue) v;
            Boolean b = bv.value();

            ret = new StringValue(b.toString());
        } else {
            ret = null;
        }

        return ret;
    }

}
