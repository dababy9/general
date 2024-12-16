def dfs(node, graph, values):
    count = 0
    fringe = [node]
    while len(fringe) > 0:
        curr = fringe.pop()
        for neighbor in graph[curr]:
            if values[neighbor[0]][neighbor[1]] == 9:
                count += 1
            else:
                fringe.append(neighbor)
    return count
                
f = open("input.txt", "r")

grid = []

for line in f:
    grid.append([int(x) for x in list(line.strip())])

trailheads = []
g = {}

dirs = [(0, 1), (1, 0), (0, -1), (-1, 0)]

for r, row in enumerate(grid):
    for c, item in enumerate(row):
        g[(r, c)] = []
        for dr, dc in dirs:
                rr, cc = r+dr, c+dc
                if rr >= 0 and cc >= 0 and rr < len(grid) and cc < len(row) and grid[rr][cc] - grid[r][c] == 1:
                    g[(r, c)].append((rr, cc))
        if item == 0:
            trailheads.append((r, c))

total = 0

for start in trailheads:
    total += dfs(start, g, grid)

print(total)