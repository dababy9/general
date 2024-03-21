from Board import *

class State:
    def __init__(self, b, m, g, h):
        self.b = b
        self.prevMove = m
        self.cost = g+h
        self.g = g

    def key(self):
        return self.b.key()

class PriQue:
    def __init__(self):
        self.uList = []
        self.hTable = {}

    def add(self, b, m, g, h):
        bHash = b.key()
        if bHash not in self.hTable:
            s = State(b, m, g, h)
            self.hTable[bHash] = s
            self.uList.append(s)
            return True
        else:
            s = self.hTable[bHash]
            if g+h < s.cost:
                s.prevMove = m
                s.cost = g+h
                s.g = g
                return True
        return False
    
    def min(self):
        if len(self.uList) == 0: return None
        result = self.uList[0]
        for state in self.uList:
            if state.cost < result.cost: result = state
        self.uList.remove(result)
        return result