f = open("input.txt", "r")

def process(line):
    arr = line.split()
    target = int(arr[0][:-1])
    arr = [int(x) for x in arr[1:]]
    mask = (1 << len(arr)-1) - 1
    while mask >= 0:
        result = arr[0]
        for i in range(1, len(arr)):
            if mask & (1 << (i-1)) != 0:
                result += arr[i]
            else:
                result *= arr[i]
        if result == target:
            return target
        mask -= 1
    return 0

total = 0

for line in f:
    total += process(line)

print(total)