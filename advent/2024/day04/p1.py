f = open("input.txt", "r")

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
    cnt = 0
    for d in dirs:
        matched = True
        for char, p in d:
            new_r, new_c = r+p[0], c+p[1]
            if new_r < 0 or new_c < 0 or new_r >= len(arr) or new_c >= len(arr[r]):
                matched = False
                break
            if arr[r+p[0]][c+p[1]] != char:
                matched = False
                break
        if matched:
            cnt += 1
    return cnt

total = 0

for row in range(len(a)):
    for col in range(len(a[0])):
        if(a[row][col] == 'X'):
            total += count(a, row, col)

print(total)