a = {1, 2, 3}

for k in a do
    print(k)
end

a = {["oi"] = 1, ["mundo"] = 2, ["bom"] = 3}

print()
for k, v in a do
    print(k .. " " .. v)
end

print()
for i = 0, 5 do 
    print(i)
end


print()
for i = 0, 5, 2 do 
    print(i)
end

print()
while i >= 5 do
    i = i - 1
    print(i)
end

print()
v = "primavera"
while v do
    print(v)
    v = nil
end
