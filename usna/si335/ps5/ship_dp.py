import sys

rIn = sys.stdin

def DP(maxShips):
    l = list(offers.keys())
    t = [[0]*maxShips for _ in range(len(offers))]
    for x in range(len(offers)):
        for y in range(maxShips):
            key = l[x]
            cost = offers[key]
            if y - key >= 0:
                cost += t[x][y-key]
            if x > 0 and t[x-1][y] < cost: t[x][y] = t[x-1][y]
            else: t[x][y] = cost
    return t


while True:
    line = rIn.readline().split()
    if len(line) == 0: break
    global offers
    offers = {}
    for _ in range(int(line[1])):
        l = rIn.readline().split()
        offers[int(l[0])] = float(l[1])
    targets = [int(x) for x in rIn.readline().split()]
    table = DP(max(targets))
    print(f"{line[0]}:")
    for n in targets:
        print(f"Purchase {n} for ${table[-1][n-1]:.2f} Billion")

