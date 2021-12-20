-- Tabela com os operadores suportados.
ops = { ["+"] = "add", ["-"] = "sub", ["*"] = "mul", ["/"] = "div" }

stats = {}

for symbol, op in ops do
    stats[op] = 0
end 

exp = {8, 6, 8, 6 , 6, 12}

print(ops)
print(stats)
print(exp)

v = read()
v = read()
v = read()