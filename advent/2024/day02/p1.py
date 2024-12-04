f = open("input.txt", "r")

total = 0

for line in f:
    arr = [int(x) for x in line.split()]
    inc = arr[0] < arr[1]
    safe = True
    for i in range(1, len(arr)):
        e, p = arr[i], arr[i-1]
        diff = abs(e - p)
        if diff < 1 or diff > 3:
            safe = False
            break
        if inc and e - p < 0 or not inc and e - p > 0:
            safe = False
            break
    if safe:
        total += 1

print(total)