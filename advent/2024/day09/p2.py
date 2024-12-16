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

for e in range(len(arr)-1, 0, -1):
    if arr[e].v != '.':
         

arr = [x for x in arr if x != '.']

total = 0

for i, n in enumerate(arr):
    total += i*n

print(total)