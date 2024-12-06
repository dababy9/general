

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
dir = 0

visited = {(row, col)}

while True:
    newRow, newCol = row + dirs[dir][0], col + dirs[dir][1]
    if newRow >= 0 and newCol >= 0 and newRow < len(floor) and newCol < len(floor[0]):
        if floor[newRow][newCol] == '.':
            row, col = newRow, newCol
            visited.add((row, col))
        else:
            dir = (dir + 1) % 4
    else:
        break

print(len(visited))