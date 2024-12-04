f = open("i.txt", "r")
l1 = []
l2 = []
for line in f:
    line_arr = line.split()
    l1.append(int(line_arr[0]))
    l2.append(int(line_arr[1]))

l1.sort()
l2.sort()

total = 0

for i1, i2 in zip(l1, l2):
    total += abs(i1 - i2)

print(total)