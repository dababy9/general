from astar import *
from Board import *
from PriQue import *

if __name__ == "__main__":
    n = int(input("Enter scramble size: ")) 
    b1 = Board()
    b1.scramble(n)
    b2 = Board()
    p , n1  = aStar(b1, misplacedTiles)
    print(len(p))
    print(n1)
    applyMoves(b1,p)
    print(b1==b2)
    b1 = Board()
    b1.scramble(n)
    p , n2  = aStar(b1, manhattanDistance)
    print(len(p))
    print(n2)
    applyMoves(b1,p)
    print(b1==b2)
    print(n1>=n2)