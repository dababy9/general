f = open("input.txt", "r")

space = False
arr = []

for i, n in enumerate([int(x) for x in list(f.readline().strip())]):
    c = '.' if space else i//2
    space = not space
    for j in range(n):
        arr.append(c)

s, e = 0, len(arr)-1

while s < e:
    while arr[s] != '.':
        s += 1
    while arr[e] == '.':
        e -= 1
    arr[s], arr[e] = arr[e], arr[s]

arr = [x for x in arr if x != '.']

total = 0

for i, n in enumerate(arr):
    total += i*n

print(total)