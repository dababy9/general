import sys
import random
import copy

class Mid:
    def __init__(self, line):
        self.co = int(line[0])
        self.name = line[1]

def readInput():
    return [Mid(line.split()) for line in sys.stdin.read().strip().split('\n')]

def specialSort(arr, map):
    l = len(arr)
    for i in range(0, l-1):
        if arr[i].co == map[i]: continue
        best = i+1
        for j in range(i+1, l):
            if arr[j].co == map[i]:
                best = j
                if arr[i].co == map[j]: break
        arr[i], arr[best] = arr[best], arr[i]
        print(f"{arr[i].name} {arr[best].name}")

def generateRanges(arr):
    lengths = {}
    for i in arr:
        if i.co in lengths: lengths[i.co] += 1
        else: lengths[i.co] = 1
    bestOrder, bestMatches = naiveOrder(lengths, arr)
    for i in range(0, 1000):
        trialOrder = copy.copy(bestOrder)
        random.shuffle(trialOrder)
        trialMatches = matches(trialOrder, arr, lengths)
        if trialMatches > bestMatches: bestOrder, bestMatches = trialOrder, trialMatches
    result = []
    for i in bestOrder:
        for j in range(lengths[i]): result.append(i)
    return result

def naiveOrder(lengths, arr):
    choices, index, result, totalMatches = list(lengths.keys()), 0, [], 0
    while len(choices) > 0:
        bestChoice, bestMatches, bestRatio = None, -1, -1
        for c in choices:
            trialMatches = count(arr[index:index+lengths[c]], c)
            trialRatio = trialMatches/lengths[c]
            if trialRatio > bestRatio: bestChoice, bestMatches, bestRatio = c, trialMatches, trialRatio
        result.append(bestChoice)
        index += lengths[bestChoice]
        totalMatches += bestMatches
        choices.remove(bestChoice)
    return result, totalMatches

def matches(list, arr, lengths):
    result, index = 0, 0
    for i in list:
        result += count(arr[index:index+lengths[i]], i)
        index += lengths[i]
    return result

def count(arr, co):
    return sum(map(lambda i : i.co == co, arr))

arr = readInput()
map = generateRanges(arr)
specialSort(arr, map)