f = open("input.txt", "r")

grid = { (x, y): c for y, l in enumerate(f.readlines()) 
                   for x, c in enumerate(l.strip()) }
                   
start_pos = next((xy) for xy, c in grid.items() if c == '^')
start_direction = (0, -1)
grid[start_pos] = '.'

def walk(g, in_simulation = False):
    obstacle_placements = set()
    visits = dict()
    position = start_pos
    direction = start_direction
    while(True):
        if direction in visits.get(position, []):
            raise OverflowError("Infinite loop detected")

        visits[position] = visits.get(position, []) + [direction]

        x, y = position
        dx, dy = direction
        next_pos = (x+dx, y+dy)
        next_field = g.get(next_pos, "")

        if next_field == '.':
            if not in_simulation and next_pos not in obstacle_placements:
                try:
                    walk({**g, **{next_pos: '#'}}, True)
                except OverflowError:
                    obstacle_placements.add(next_pos)

            position = next_pos
        elif next_field == '#':
            direction = (-dy, dx)
        else:
            return obstacle_placements

print(len(walk(grid)))