package interpreter.expr;

import interpreter.util.Utils;
import interpreter.value.BooleanValue;
import interpreter.value.NumberValue;
import interpreter.value.StringValue;
import interpreter.value.TableValue;
import interpreter.value.Value;

public class BinaryExpr extends Expr {

    private Expr left;
    private BinaryOp op;
    private Expr right;

    public BinaryExpr(int line, Expr left, BinaryOp op, Expr right) {
        super(line);
        this.left = left;
        this.op = op;
        this.right = right;

    }

    @Override
    public Value<?> expr() {
        Value<?> l = left.expr();
        Value<?> r = right.expr();
        Value<?> ret = null;
        switch (op) {
            case AndOp:
                ret = andOp(l, r);
                break;
            case OrOp:
                ret = orOp(l, r);
                break;
            case EqualOp:
                ret = equalOp(l, r);
                break;
            case NotEqualOp:
                ret = notEqualOp(l, r);
                break;
            case LowerThanOp:
                ret = lowerThanOp(l, r);
                break;
            case LowerEqualOp:
                ret = lowerEqualOp(l, r);
                break;
            case GreaterThanOp:
                ret = greaterThanOp(l, r);
                break;
            case GreaterEqualOp:
                ret = greaterEqualOp(l, r);
                break;
            case ConcatOp:
                ret = concatOp(l, r);
                break;
            case AddOp:
                ret = addOp(l, r);
                break;
            case SubOp:
                ret = subOp(l, r);
                break;
            case MulOp:
                ret = mulOp(l, r);
                break;
            case DivOp:
                ret = divOp(l, r);
                break;
            case ModOp:
                ret = modOp(l, r);
                break;
            default:
                Utils.abort(super.getLine());
        }
        return ret;
    }

    public Value<?> modOp(Value<?> l, Value<?> r) {
        if (l instanceof NumberValue && r instanceof NumberValue) {
            NumberValue valor1 = (NumberValue) l;
            NumberValue valor2 = (NumberValue) r;
            Double res = valor1.value() % valor2.value();
            return new NumberValue(res);
        } else if (l instanceof StringValue && r instanceof NumberValue) {
            StringValue valor1 = (StringValue) l;
            NumberValue valor2 = (NumberValue) r;
            Double res = Double.valueOf(valor1.value()) % valor2.value();
            return new NumberValue(res);
        } else if (l instanceof NumberValue && r instanceof StringValue) {
            NumberValue valor1 = (NumberValue) l;
            StringValue valor2 = (StringValue) r;
            Double res = valor1.value() % Double.valueOf(valor2.value());
            return new NumberValue(res);
        } else {
            Utils.abort(super.getLine());
            return null;
        }
    }

    public Value<?> divOp(Value<?> l, Value<?> r) {
        if (l instanceof NumberValue && r instanceof NumberValue) {
            NumberValue valor1 = (NumberValue) l;
            NumberValue valor2 = (NumberValue) r;
            Double res = valor1.value() / valor2.value();
            return new NumberValue(res);
        } else if (l instanceof StringValue && r instanceof NumberValue) {
            StringValue valor1 = (StringValue) l;
            NumberValue valor2 = (NumberValue) r;
            Double res = Double.valueOf(valor1.value()) / valor2.value();
            return new NumberValue(res);
        } else if (l instanceof NumberValue && r instanceof StringValue) {
            NumberValue valor1 = (NumberValue) l;
            StringValue valor2 = (StringValue) r;
            Double res = valor1.value() / Double.valueOf(valor2.value());
            return new NumberValue(res);
        } else {
            Utils.abort(super.getLine());
            return null;
        }
    }

    public Value<?> mulOp(Value<?> l, Value<?> r) {
        if (l instanceof NumberValue && r instanceof NumberValue) {
            NumberValue valor1 = (NumberValue) l;
            NumberValue valor2 = (NumberValue) r;
            Double res = valor1.value() * valor2.value();
            return new NumberValue(res);
        } else if (l instanceof StringValue && r instanceof NumberValue) {
            StringValue valor1 = (StringValue) l;
            NumberValue valor2 = (NumberValue) r;
            Double res = Double.valueOf(valor1.value()) * valor2.value();
            return new NumberValue(res);
        } else if (l instanceof NumberValue && r instanceof StringValue) {
            NumberValue valor1 = (NumberValue) l;
            StringValue valor2 = (StringValue) r;
            Double res = valor1.value() * Double.valueOf(valor2.value());
            return new NumberValue(res);
        } else {
            Utils.abort(super.getLine());
            return null;
        }
    }

    public Value<?> subOp(Value<?> l, Value<?> r) {
        if (l instanceof NumberValue && r instanceof NumberValue) {
            NumberValue valor1 = (NumberValue) l;
            NumberValue valor2 = (NumberValue) r;
            Double res = valor1.value() - valor2.value();
            return new NumberValue(res);
        } else if (l instanceof StringValue && r instanceof NumberValue) {
            StringValue valor1 = (StringValue) l;
            NumberValue valor2 = (NumberValue) r;
            Double res = Double.valueOf(valor1.value()) - valor2.value();
            return new NumberValue(res);
        } else if (l instanceof NumberValue && r instanceof StringValue) {
            NumberValue valor1 = (NumberValue) l;
            StringValue valor2 = (StringValue) r;
            Double res = valor1.value() - Double.valueOf(valor2.value());
            return new NumberValue(res);
        } else {
            System.out.print(r.getClass());
            Utils.abort(super.getLine());
            return null;
        }
    }

    public Value<?> addOp(Value<?> l, Value<?> r) {
        if (l instanceof NumberValue && r instanceof NumberValue) {
            NumberValue valor1 = (NumberValue) l;
            NumberValue valor2 = (NumberValue) r;
            Double res = valor1.value() + valor2.value();
            return new NumberValue(res);
        } else if (l instanceof StringValue && r instanceof NumberValue) {
            StringValue valor1 = (StringValue) l;
            NumberValue valor2 = (NumberValue) r;
            Double res = Double.valueOf(valor1.value()) + valor2.value();
            return new NumberValue(res);
        } else if (l instanceof NumberValue && r instanceof StringValue) {
            NumberValue valor1 = (NumberValue) l;
            StringValue valor2 = (StringValue) r;
            Double res = valor1.value() + Double.valueOf(valor2.value());
            return new NumberValue(res);
        } else {
            Utils.abort(super.getLine());
            return null;
        }
    }

    public Value<?> concatOp(Value<?> l, Value<?> r) {
        if (!(l instanceof StringValue) || !(r instanceof StringValue)) {
            String valor1 = l.value().toString();
            String valor2 = r.value().toString();
            String frase = valor1 + valor2;
            return new StringValue(frase);
        } else {
            StringValue valor1 = (StringValue) l;
            StringValue valor2 = (StringValue) r;
            String frase = valor1.value() + valor2.value();
            return new StringValue(frase);
        }

    }

    public Value<?> greaterEqualOp(Value<?> l, Value<?> r) {
        if (l instanceof NumberValue && r instanceof NumberValue) {
            NumberValue valor1 = (NumberValue) l;
            NumberValue valor2 = (NumberValue) r;
            if (!(valor1.value() >= valor2.value())) {
                Boolean menor = false;
                return new BooleanValue(menor);
            } else {
                Boolean menor = true;
                return new BooleanValue(menor);
            }
        } else if (l.getClass().equals(r.getClass())) {
            return new BooleanValue(false);
        } else {
            Utils.abort(super.getLine());
            return null;
        }
    }

    public Value<?> greaterThanOp(Value<?> l, Value<?> r) {
        if (l instanceof NumberValue && r instanceof NumberValue) {
            NumberValue valor1 = (NumberValue) l;
            NumberValue valor2 = (NumberValue) r;
            if (!(valor1.value() > valor2.value())) {
                Boolean menor = false;
                return new BooleanValue(menor);
            } else {
                Boolean menor = true;
                return new BooleanValue(menor);
            }
        } else if (l.getClass().equals(r.getClass())) {
            return new BooleanValue(false);
        } else {
            Utils.abort(super.getLine());
            return null;
        }
    }

    public Value<?> lowerEqualOp(Value<?> l, Value<?> r) {
        if (l instanceof NumberValue && r instanceof NumberValue) {
            NumberValue valor1 = (NumberValue) l;
            NumberValue valor2 = (NumberValue) r;
            if (!(valor1.value() <= valor2.value())) {
                Boolean menor = false;
                return new BooleanValue(menor);
            } else {
                Boolean menor = true;
                return new BooleanValue(menor);
            }
        } else if (l.getClass().equals(r.getClass())) {
            return new BooleanValue(false);
        } else {
            Utils.abort(super.getLine());
            return null;
        }
    }

    public Value<?> lowerThanOp(Value<?> l, Value<?> r) {
        if (l instanceof NumberValue && r instanceof NumberValue) {
            NumberValue valor1 = (NumberValue) l;
            NumberValue valor2 = (NumberValue) r;
            if (!(valor1.value() < valor2.value())) {
                Boolean menor = false;
                return new BooleanValue(menor);
            } else {
                Boolean menor = true;
                return new BooleanValue(menor);
            }
        } else if (l == null && r != null) {
            return new BooleanValue(false);
        } else if (r == null && l != null) {
            return new BooleanValue(false);
        } else if (!(l.getClass().equals(r.getClass()))) {
            return new BooleanValue(false);
        } else {
            Utils.abort(super.getLine());
            return null;
        }
    }

    public Value<?> notEqualOp(Value<?> l, Value<?> r) {
        if ((l instanceof NumberValue) && (r instanceof NumberValue)) {
            NumberValue valor1 = (NumberValue) l;
            NumberValue valor2 = (NumberValue) r;
            if (valor1.value().equals(valor2.value())) {
                return new BooleanValue(false);
            } else {
                return new BooleanValue(true);
            }
        } else if ((l instanceof StringValue) && (r instanceof StringValue)) {
            StringValue valor1 = (StringValue) l;
            StringValue valor2 = (StringValue) r;
            if (valor1.value().equals(valor2.value())) {
                return new BooleanValue(false);
            } else {
                return new BooleanValue(true);
            }
        } else if ((l instanceof BooleanValue) && (r instanceof BooleanValue)) {
            BooleanValue valor1 = (BooleanValue) l;
            BooleanValue valor2 = (BooleanValue) r;
            if (valor1.value().equals(valor2.value())) {
                return new BooleanValue(false);
            } else {
                return new BooleanValue(true);
            }
        } else if ((l instanceof TableValue) && (r instanceof TableValue)) {
            TableValue valor1 = (TableValue) l;
            TableValue valor2 = (TableValue) r;
            if (valor1.value().equals(valor2.value())) {
                return new BooleanValue(false);
            } else {
                return new BooleanValue(true);
            }
        } else {
            // Utils.abort(super.getLine());
            return new BooleanValue(false);
        }
    }

    public Value<?> equalOp(Value<?> l, Value<?> r) {
        if ((l instanceof NumberValue) && (r instanceof NumberValue)) {
            NumberValue valor1 = (NumberValue) l;
            NumberValue valor2 = (NumberValue) r;
            if (valor1.value().equals(valor2.value())) {
                return new BooleanValue(true);
            } else {
                return new BooleanValue(false);
            }
        } else if ((l instanceof StringValue) && (r instanceof StringValue)) {
            StringValue valor1 = (StringValue) l;
            StringValue valor2 = (StringValue) r;
            if (valor1.value().equals(valor2.value())) {
                return new BooleanValue(true);
            } else {
                return new BooleanValue(false);
            }
        } else if ((l instanceof BooleanValue) && (r instanceof BooleanValue)) {
            BooleanValue valor1 = (BooleanValue) l;
            BooleanValue valor2 = (BooleanValue) r;
            if (valor1.value().equals(valor2.value())) {
                return new BooleanValue(true);
            } else {
                return new BooleanValue(false);
            }
        } else if ((l instanceof TableValue) && (r instanceof TableValue)) {
            TableValue valor1 = (TableValue) l;
            TableValue valor2 = (TableValue) r;
            if (valor1.value().equals(valor2.value())) {
                return new BooleanValue(true);
            } else {
                return new BooleanValue(false);
            }
        } else {
            // Utils.abort(super.getLine());
            return new BooleanValue(false);
        }
    }

    public Value<?> orOp(Value<?> l, Value<?> r) {
        if (l == null) {
            return r;
        } else if (l instanceof BooleanValue) {
            BooleanValue valor1 = (BooleanValue) l;
            if (valor1.value() == false) {
                return r;
            }
        }
        return l;
    }

    public Value<?> andOp(Value<?> l, Value<?> r) {
        if (l.eval()) {
            return r;
        }
        return l;
    }
}
