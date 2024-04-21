import sys

rIn = sys.stdin

#memo table
memo = {}

#find minimum cost of buying n ships (using global variable 'offers')
def minCost(n):

    #cost of buying 0 ships is 0
    if n < 1: return 0

    if n not in memo:

        #look at all choices and save cost of cheapest choice
        best = -1
        for x in offers:

            #total cost is cost of choice + cost of rest of the ships
            cost = offers[x] + minCost(n-x)
            if cost < best or best == -1: best = cost

        #memoize result
        memo[n] = best
    return memo[n]

while True:

    #read in first line, end loop if there is no more input
    line = rIn.readline().split()
    if len(line) == 0: break

    #global variable holding all possible offers to be used in recursive method
    global offers
    offers = {}

    #loop through all offers
    for _ in range(int(line[1])):
        l = rIn.readline().split()
        offers[int(l[0])] = float(l[1])

    #list of target numbers of ships
    targets = [int(x) for x in rIn.readline().split()]

    #print results
    print(f"{line[0]}:")
    for n in targets:
        print(f"Purchase {n} for ${minCost(n):.2f} Billion")
        memo = {}