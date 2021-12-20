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
        if (!(l instanceof NumberValue) || !(r instanceof NumberValue)) {
            Utils.abort(super.getLine());
            return null;
        } else {
            NumberValue valorL = (NumberValue) l;
            NumberValue valorR = (NumberValue) r;
            Double res = valorL.value() % valorR.value();
            return new NumberValue(res);
        }
    }

    public Value<?> divOp(Value<?> l, Value<?> r) {
        if (!(l instanceof NumberValue) || !(r instanceof NumberValue)) {
            Utils.abort(super.getLine());
            return null;
        } else {
            NumberValue valorL = (NumberValue) l;
            NumberValue valorR = (NumberValue) r;
            Double res = valorL.value() / valorR.value();
            return new NumberValue(res);
        }
    }

    public Value<?> mulOp(Value<?> l, Value<?> r) {
        if (!(l instanceof NumberValue) || !(r instanceof NumberValue)) {
            Utils.abort(super.getLine());
            return null;

        } else {
            NumberValue valorL = (NumberValue) l;
            NumberValue valorR = (NumberValue) r;
            double res = valorL.value() * valorR.value();
            return new NumberValue(res);
        }
    }

    public Value<?> subOp(Value<?> l, Value<?> r) {
        if (!(l instanceof NumberValue) || !(r instanceof NumberValue)) {
            Utils.abort(super.getLine());
            return null;

        } else {
            NumberValue valorL = (NumberValue) l;
            NumberValue valor2 = (NumberValue) r;
            double res = valorL.value() - valor2.value();
            return new NumberValue(res);
        }
    }

    public Value<?> addOp(Value<?> l, Value<?> r) {
        if (!(l instanceof NumberValue) || !(r instanceof NumberValue)) {
            Utils.abort(super.getLine());
            return null;

        } else {
            NumberValue valor1 = (NumberValue) l;
            NumberValue valor2 = (NumberValue) r;
            double res = valor1.value() + valor2.value();
            return new NumberValue(res);
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
        if (!(l instanceof NumberValue) || !(r instanceof NumberValue)) {
            Utils.abort(super.getLine());
            return null;
        } else {
            NumberValue valor1 = (NumberValue) l;
            NumberValue valor2 = (NumberValue) r;
            if (!(valor1.value() >= valor2.value())) {
                Boolean maiorOuIgual = false;
                return new BooleanValue(maiorOuIgual);
            } else {
                Boolean maiorOuIgual = true;
                return new BooleanValue(maiorOuIgual);
            }
        }
    }

    public Value<?> greaterThanOp(Value<?> l, Value<?> r) {
        if (!(l instanceof NumberValue) || !(r instanceof NumberValue)) {
            Utils.abort(super.getLine());
            return null;
        } else {
            NumberValue valor1 = (NumberValue) l;
            NumberValue valor2 = (NumberValue) r;
            if (!(valor1.value() > valor2.value())) {
                Boolean maior = false;
                return new BooleanValue(maior);
            } else {
                Boolean maior = true;
                return new BooleanValue(maior);
            }
        }
    }

    public Value<?> lowerEqualOp(Value<?> l, Value<?> r) {
        if (!(l instanceof NumberValue) || !(r instanceof NumberValue)) {
            Utils.abort(super.getLine());
            return null;
        } else {
            NumberValue valor1 = (NumberValue) l;
            NumberValue valor2 = (NumberValue) r;
            if (!(valor1.value() <= valor2.value())) {
                Boolean menorOuIgual = false;
                return new BooleanValue(menorOuIgual);
            } else {
                Boolean menorOuIgual = true;
                return new BooleanValue(menorOuIgual);
            }
        }
    }

    public Value<?> lowerThanOp(Value<?> l, Value<?> r) {
        if (!(l instanceof NumberValue) || !(r instanceof NumberValue)) {
            Utils.abort(super.getLine());
            return null;
        } else {
            NumberValue valor1 = (NumberValue) l;
            NumberValue valor2 = (NumberValue) r;
            if (!(valor1.value() < valor2.value())) {
                Boolean menor = false;
                return new BooleanValue(menor);
            } else {
                Boolean menor = true;
                return new BooleanValue(menor);
            }
        }
    }

    public Value<?> notEqualOp(Value<?> l, Value<?> r) {
        if ((l instanceof NumberValue) && (r instanceof NumberValue)) {
            NumberValue valor1 = (NumberValue) l;
            NumberValue valor2 = (NumberValue) r;
            if (valor1.value().equals(valor2.value())) {
                Boolean diferente = false;
                return new BooleanValue(diferente);
            } else {
                Boolean diferente = true;
                return new BooleanValue(diferente);
            }
        } else if ((l instanceof StringValue) && (r instanceof StringValue)) {
            StringValue valor1 = (StringValue) l;
            StringValue valor2 = (StringValue) r;
            if (valor1.value().equals(valor2.value())) {
                Boolean diferente = false;
                return new BooleanValue(diferente);
            } else {
                Boolean diferente = true;
                return new BooleanValue(diferente);
            }
        } else if ((l instanceof BooleanValue) && (r instanceof BooleanValue)) {
            BooleanValue valor1 = (BooleanValue) l;
            BooleanValue valor2 = (BooleanValue) r;
            if (valor1.value().equals(valor2.value())) {
                Boolean diferente = false;
                return new BooleanValue(diferente);
            } else {
                Boolean diferente = true;
                return new BooleanValue(diferente);
            }
        } else if ((l instanceof TableValue) && (r instanceof TableValue)) {
            TableValue valor1 = (TableValue) l;
            TableValue valor2 = (TableValue) r;
            if (valor1.value().equals(valor2.value())) {
                Boolean diferente = false;
                return new BooleanValue(diferente);
            } else {
                Boolean diferente = true;
                return new BooleanValue(diferente);
            }
        } else {
            Utils.abort(super.getLine());
            return null;
        }
    }

    public Value<?> equalOp(Value<?> l, Value<?> r) {
        Value<?> ret = notEqualOp(l, r);
        if (ret instanceof BooleanValue) {
            BooleanValue valor = (BooleanValue) ret;
            Boolean nb = valor.value() == true ? false : true;
            return new BooleanValue(nb);
        }
        else {
            Utils.abort(super.getLine());
            return null;
        }
    }

    public Value<?> orOp(Value<?> l, Value<?> r) {
        if (!(l instanceof BooleanValue) || !(r instanceof BooleanValue)) {
            Utils.abort(super.getLine());
            return null;
        } else {
            BooleanValue valor1 = (BooleanValue) l;
            BooleanValue valor2 = (BooleanValue) r;
            if (valor1.value() || valor2.value()) {
                Boolean ou = true;
                return new BooleanValue(ou);
            } else {
                Boolean ou = false;
                return new BooleanValue(ou);
            }
        }
    }

    public Value<?> andOp(Value<?> l, Value<?> r) {
        if (!(l instanceof BooleanValue) || !(r instanceof BooleanValue)) {
            Utils.abort(super.getLine());
            return null;
        } else {
            BooleanValue valor1 = (BooleanValue) l;
            BooleanValue valor2 = (BooleanValue) r;
            if (valor1.value() && valor2.value()) {
                Boolean e = true;
                return new BooleanValue(e);
            } else {
                Boolean e = false;
                return new BooleanValue(e);
            }
        }
    }
}