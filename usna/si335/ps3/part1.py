import sys

class Mid:
    def __init__(self, line):
        self.co = int(line[0])
        self.name = line[1]

def selectionSort(list):
    l = len(list)
    for i in range(0, l-1):
        min = i
        for j in range(i+1, l):
            if list[j].co < list[min].co: min = j
        if i != min:
            list[i], list[min] = list[min], list[i]
            print(f"{list[i].name} {list[min].name}")


def readInput():
    return [Mid(line.split()) for line in sys.stdin.read().split('\n')]

selectionSort(readInput())