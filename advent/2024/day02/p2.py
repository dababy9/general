import copy

f = open("input.txt", "r")

def safe(arr):
    inc = arr[0] < arr[1]
    for i in range(1, len(arr)):
        e, p = arr[i], arr[i-1]
        diff = abs(e - p)
        if diff < 1 or diff > 3:
            return False
        if inc and e - p < 0 or not inc and e - p > 0:
            return False
    return True

total = 0

for line in f:
    arr = [int(x) for x in line.split()]
    if safe(arr):
        total += 1
    else:
        for i in range(0, len(arr)):
            c = copy.copy(arr)
            c.pop(i)
            if safe(c):
                total += 1
                break

print(total)