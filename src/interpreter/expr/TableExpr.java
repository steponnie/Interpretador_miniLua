package interpreter.expr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interpreter.value.TableValue;
import interpreter.value.Value;

public class TableExpr extends Expr{

    private List<TableEntry> table;

    public TableExpr(int line) {
        super(line);
        this.table = new ArrayList<TableEntry>();
    }

    public void addEntry(Expr key, Expr value){
        TableEntry entry = new TableEntry();
        entry.key = key;
        entry.value = value;
        this.table.add(entry);
    }

    @Override
    public Value<?> expr() {
        Map<Value<?>, Value<?>> var = new HashMap<Value<?>, Value<?>>();
        for(TableEntry entry : table)
            var.put(entry.key.expr(), entry.value.expr());
        TableValue nt = new TableValue(var);
        return nt;
    }
}