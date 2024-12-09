f = open("input.txt", "r")

def recurse(i, result):
    if i == len(arr):
        return result == target
    return recurse(i+1, result + arr[i]) or recurse(i+1, result * arr[i]) or recurse(i+1, int(str(result) + str(arr[i])))

def process(line):
    list = line.split()
    global target
    global arr
    target = int(list[0][:-1])
    arr = [int(x) for x in list[1:]]
    return recurse(1, arr[0])

total = 0

for line in f:
    total += target if process(line) else 0

print(total)