import random

class Board(object):

    #A board is just a 2-d list, plus a location of the blank, for easier move generation.
    def __init__(self):
        self.b = [['b', 1, 2, 3], [4, 5, 6, 7], [8, 9, 10, 11], [12, 13, 14, 15]]
        self.lb = [0, 0]
    
    #Returns a list of places the blank can be moved to.  Note the use of map and filter.  Good tools for AI programming.
    def generateMoves(self):
        delta = [[-1,0],[1,0],[0,-1],[0,1]]
        result = list(map(lambda x: pairAdd(x, self.lb), delta)) 
        result = list(filter(inRange, result))
        return result

    #Takes a move location, and actually changes the board.
    def makeMove(self, m):
        self.b[self.lb[0]][self.lb[1]] = self.b[m[0]][m[1]]
        self.b[m[0]][m[1]] = 'b'
        self.lb = m

    #Mix up the board. Parameter n is amount of random moves to scramble.
    def scramble(self, n, s=2018):
        random.seed(s)
        for i in range(n):
            moves = self.generateMoves()
            self.makeMove(moves[random.randint(0, len(moves)-1)])

    #Overriden methods
    def __eq__(self,other):
        if self.lb == other.lb: return self.b == other.b
        return False
    def __ne__(self,other):
        if self.lb == other.lb: return self.b != other.b
        return True
    def key(self):
        return str(self.b)
    
#---------------------------------
#End of Board class


#Apply a list of moves to the board.
def applyMoves(board, moveList):
    for m in moveList:
        board.makeMove(m)


#Some utility functions.
def pairAdd(a, b):
    return [a[0]+b[0], a[1]+b[1]]

def inRange(p):
    return p[0]>=0 and p[0]<4 and p[1]>=0 and p[1]<4


#The heuristics go here
def misplacedTiles(board):
    final = [['b', 1, 2, 3], [4, 5, 6, 7], [8, 9, 10, 11], [12, 13, 14, 15]]
    numMisplaced = 0
    for i in range(0, 4):
        for j in range(0, 4):
            if final[i][j] != board.b[i][j]: numMisplaced += 1
    return numMisplaced

def manhattanDistance(board):
    totalDistance = 0
    for i in range(0, 4):
        for j in range(0, 4):
            pos = board.b[i][j]
            if pos == 'b': pos = 0
            totalDistance += manhattan([i, j], [pos//4, pos%4])
    return totalDistance


#This is not the actual manhattan distance heuristic, but may be helpful.
def manhattan(a, b):

    #Takes two locations on the board and returns the difference.
    return abs(a[0]-b[0])+abs(a[1]-b[1])