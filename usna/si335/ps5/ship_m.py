import sys

rIn = sys.stdin
memo = {}

def minCost(n):
    if n < 1: return 0
    if n not in memo:
        best = -1
        for x in offers:
            if x <= n:
                cost = offers[n] + minCost(n-x)
                if cost < best or best == -1: best = cost
    return memo[n]

while True:
    line = rIn.readline().split()
    if len(line) == 0: break
    global offers
    offers = {}
    for _ in range(int(line[1])):
        l = rIn.readline().split()
        offers[int(l[0])] = float(l[1])
    targets = [int(x) for x in rIn.readline().split()]
    print(f"{line[0]}:")
    for n in targets:
        print(f"Purchase {n} for ${minCost(n):.2f} Billion")
        memo = {}