f = open("input.txt", "r")

antinodes = set()
nodes = dict()
bounds = (0, 0)

def in_bounds(p):
    return p[0] >= 0 and p[1] >= 0 and p[0] <= bounds[0] and p[1] <= bounds[1]

for row, line in enumerate(f):
    for col, char in enumerate(list(line.strip())):
        bounds = (row, col)
        if char != '.':
            if char in nodes:
                nodes[char].append((row, col))
            else:
                nodes[char] = [(row, col)]

for arr in nodes.values():
    for i in range(len(arr)-1):
        for j in range(i+1, len(arr)):
            p1, p2 = arr[i], arr[j]
            delta = (p2[0] - p1[0], p2[1] - p1[1])
            antinodes.add(p1)
            while in_bounds(p2):
                antinodes.add(p2)
                p2 = (p2[0] + delta[0], p2[1] + delta[1])
            p2 = p1
            while in_bounds(p2):
                antinodes.add(p2)
                p2 = (p2[0] - delta[0], p2[1] - delta[1])

print(len(antinodes))