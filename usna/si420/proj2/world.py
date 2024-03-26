import json
import copy

class World:

    def __init__(self, fName):
        with open(fName, 'r') as filehandle:
            d = json.load(filehandle)
        self.rows, self.cols = d["shape"][0], d["shape"][1]
        self.g = d["gamma"]
        self.rl = {}
        for s in d["rl"]:
            self.rl[self.__hash(s[0])] = s[1]
        self.tl, self.bl = d["tl"], d["bl"]
        self.values = [[0] * self.cols for _ in range(self.rows)]
        self.policy = [['N'] * self.cols for _ in range(self.rows)]
        for s in d["tl"] + d["bl"]:
            self.policy[s[0]][s[1]] = '.'
    
    def valueIteration(self, n):
        for x in range(n):
            newValue = copy.deepcopy(self.values)
            for r in range(self.rows):
                for c in range(self.cols):
                    if [r, c] not in self.bl and [r, c] not in self.tl: [self.policy[r][c], newValue[r][c]] = self.__update([r, c])
            self.values = newValue

    def __update(self, square):
        aList = {'N': 0, 'S': 0, 'E': 0, 'W': 0}
        for a in aList.keys():
            aList[a] = self.__qStar(a, square)
        maxA = ['N', aList['N']]
        for a in aList.keys():
            if aList[a] > maxA[1]: maxA = [a, aList[a]]
        return maxA
    
    def __qStar(self, a, square):
        others = ('N', 'S') if a == 'E' or a == 'W' else ('E', 'W')
        return 0.8 * self.__tReward(self.__move(square, a)) + 0.1 * self.__tReward(self.__move(square, others[0])) + 0.1 * self.__tReward(self.__move(square, others[1]))
    
    def __tReward(self, square):
        reward = self.rl[self.__hash(square)] if self.__hash(square) in self.rl else 0
        return reward + self.g * self.values[square[0]][square[1]]
    
    def __move(self, square, a):
        r = square
        if a == 'N': r = [square[0]-1, square[1]]
        if a == 'S': r = [square[0]+1, square[1]]
        if a == 'E': r = [square[0], square[1]+1]
        if a == 'W': r = [square[0], square[1]-1]
        return r if r[0] >= 0 and r[0] < self.rows and r[1] >= 0 and r[1] < self.cols and [r[0], r[1]] not in self.bl else square
            
    def __hash(self, p):
        return p[0] * self.cols + p[1]
    
