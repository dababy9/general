from copy import copy

class Space:
    def __init__(self, length, val):
        self.l = length
        self.v = val

f = open("input.txt", "r")

space = False
arr = []

for i, n in enumerate([int(x) for x in list(f.readline().strip())]):
    c = '.' if space else i//2
    space = not space
    arr.append(Space(n, c))

e = len(arr) - 1

while e > 0:
    if arr[e].v != '.':
        l = arr[e].l
        for s in range(e):
            if arr[s].v == '.' and arr[s].l >= l:
                if arr[s].l == l:
                    arr[s].v = arr[e].v
                else:
                    arr[s].l -= l
                    arr.insert(s, copy(arr[e]))
                    e += 1
                arr[e].v = '.'
                break
    e -= 1

total, pos = 0, 0

for item in arr:
    for i in range(item.l):
        total += pos*(item.v if item.v != '.' else 0)
        pos += 1

print(total)