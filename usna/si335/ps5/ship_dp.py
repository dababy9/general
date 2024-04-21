import sys

rIn = sys.stdin

#create and return dynamic programming table that computes minimum costs up to 'maxShips' ships
def DP(maxShips):

    #create indexed list of offers to correlate to table
    l = list(offers.keys())

    #create empty table of 0s
    t = [[0]*maxShips for _ in range(len(offers))]

    #nested for loop for each cell, left to right, top to bottom
    for x in range(len(offers)):

        #key is number of ships that the current offer is for, cost is the cost of that offer
        key = l[x]

        for y in range(maxShips):

            #cost is the cost of the current offer
            cost = offers[key]

            #if there are any more ships to buy, grab minimum cost of that problem from table
            if y - key >= 0:
                cost += t[x][y-key]

            #set the cell value to minimum cost (a previous table entry or current computed cost)
            if x > 0 and t[x-1][y] < cost: t[x][y] = t[x-1][y]
            else: t[x][y] = cost

    #return table
    return t


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

    #dynamic programming table
    table = DP(max(targets))

    #print results
    print(f"{line[0]}:")
    for n in targets:
        print(f"Purchase {n} for ${table[-1][n-1]:.2f} Billion")

