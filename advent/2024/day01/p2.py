f = open("input.txt", "r")
l = []
d = {}
for line in f:
    line_arr = line.split()
    l.append(int(line_arr[0]))
    e = int(line_arr[1])
    if e in d:
        d[e] += 1
    else:
        d[e] = 1

total = 0

for i in l:
    total += i * d[i] if i in d else 0
print(total)