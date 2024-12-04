f = open("inpu.txt", "r")

a = []

for line in f:
    a.append(list(line)[:-1])

dirs = [
    [('M', (1, 1)), ('A', (2, 2)), ('S', (3, 3))],
    [('M', (1, 0)), ('A', (2, 0)), ('S', (3, 0))],
    [('M', (1, -1)), ('A', (2, -2)), ('S', (3, -3))],
    [('M', (0, -1)), ('A', (0, -2)), ('S', (0, -3))],
    [('M', (-1, -1)), ('A', (-2, -2)), ('S', (-3, -3))],
    [('M', (-1, 0)), ('A', (-2, 0)), ('S', (-3, 0))],
    [('M', (-1, 1)), ('A', (-2, 2)), ('S', (-3, 3))],
    [('M', (0, 1)), ('A', (0, 2)), ('S', (0, 3))]
]

def count(arr, r, c):
    for d in dirs:
        pass

total = 0

for row in range(len(a)):
    for col in range(len(a[0])):
        if(a[row][col] == 'X'):
            total += count(a, row, col)


print(total)