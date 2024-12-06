import copy

f = open("input.txt", "r")

floor = []

for line in f:
    floor.append(list(line.strip()))

row, col = 0, 0

for r in range(len(floor)):
    for c in range(len(floor[r])):
        if(floor[r][c] == '^'):
            row, col = r, c
            floor[r][c] = '.'
            break

dirs = [(-1, 0), (0, 1), (1, 0), (0, -1)]

def hasLoop(arr, r, c, dir, visited):
    while True:
        newRow, newCol = r + dirs[dir][0], c + dirs[dir][1]
        if newRow >= 0 and newCol >= 0 and newRow < len(floor) and newCol < len(arr[0]):
            if arr[newRow][newCol] == '.':
                r, c = newRow, newCol
                if (r, c) in visited:
                    if dir in visited[(r, c)]:
                        return True
                    visited[(r, c)].add(dir)
                else:
                    visited[(r, c)] = {dir}
            else:
                dir = (dir + 1) % 4
        else:
            return False

d = 0
total = 0

v = {(row, col): {0}}

while True:
    newRow, newCol = row + dirs[d][0], col + dirs[d][1]
    if newRow >= 0 and newCol >= 0 and newRow < len(floor) and newCol < len(floor[0]):
        if floor[newRow][newCol] == '.':
            newFloor = copy.deepcopy(floor)
            newFloor[newRow][newCol] = '#'
            if hasLoop(newFloor, row, col, d, copy.deepcopy(v)):
                total += 1
            row, col = newRow, newCol
            if (row, col) in v:
                v[(row, col)].add(d)
            else:
                v[(row, col)] = {d}
        else:
            d = (d + 1) % 4
    else:
        break

print(total)