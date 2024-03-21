from Board import *
from PriQue import *
from collections import deque
import copy

def aStar(b, heuristic):
    s = State(copy.deepcopy(b), None, 0, heuristic(b))
    moves, nodesExpanded = deque(), 0
    parents, goalBoard = {}, Board()
    openB, closedB = PriQue(), set()
    while s.b != goalBoard:
        closedB.add(s.b.key())
        nodesExpanded += 1
        for x in s.b.generateMoves():
            newB = copy.deepcopy(s.b)
            newB.makeMove(x)
            if newB.key() in closedB: continue
            if openB.add(newB, x, s.g+1, heuristic(newB)): parents[newB.key()] = s
        s = openB.min()
    while s.b.key() in parents:
        moves.appendleft(s.prevMove)
        s = parents[s.b.key()]
    return list(moves), nodesExpanded
        