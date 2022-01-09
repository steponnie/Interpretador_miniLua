-- read the matrix.
dim = tonumber(read("Entre com a dimensao: "))
matrix = {}
r = 0;
while r < dim do
  matrix[r] = {}

  c = 0;
  while c < dim do
    matrix[r][c] = tonumber(read("Entre com valor na posicao " .. (r+1) .. "x" .. (c+1) .. ": ")) or 0
    c = c + 1
  end

  r = r + 1
end

-- show the matrix.
print("{");
for r = 0, dim-1 do
  print("  [" .. r .. "] = {")
  for c = 0, dim-1 do
    print("    [" .. c .. "] = " .. matrix[r][c])
  end
  print("  }");
end

-- calculate the first part of the determinant.
det1 = 0
c = 0
while c < dim do
  j = c
  tmp = 1

  i = 0;
  while i < dim do
    tmp = tmp * matrix[i][j]
    j = (j + 1) % dim
    i = i + 1
  end
  det1 = det1 + tmp

  c = c + 1
end

-- calculate the second part of the determinant.
det2 = 0
c = 0
while c < dim do
  j = c
  tmp = 1

  i = 0
  while i < dim do
    tmp = tmp * matrix[i][j]
    j = (j - 1) % dim
    if j < 0 then
      j = j + dim
    end

    i = i + 1
  end
  det2 = det2 + tmp

  c = c + 1
end

-- print the determinant.
print("} = " .. (det1 - det2));
