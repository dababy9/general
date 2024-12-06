f = open("input.txt", "r")

grid = { (x, y): c for y, l in enumerate(f.readlines()) 
                   for x, c in enumerate(l.strip()) }
                   
position = next((xy) for xy, c in grid.items() if c == '^')
direction = (0, -1)
grid[position] = '.'

visits = set()

while(True):

    visits.add(position)

    x, y = position
    dx, dy = direction
    next_pos = (x+dx, y+dy)
    next_field = grid.get(next_pos, "")

    if next_field == '.':
        position = next_pos
    elif next_field == '#':
        direction = (-dy, dx)
    else:
        break

print(len(visits))