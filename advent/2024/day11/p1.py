f = open("input.txt", "r")

arr = [int(x) for x in f.readline().split(" ")]

for _ in range(25):
    newArr = []
    for n in arr:
        s = str(n)
        if n == 0:
            newArr.append(1)
        elif len(s) % 2 == 0:
            newArr.append(int(s[:len(s)//2]))
            newArr.append(int(s[len(s)//2:]))
        else:
            newArr.append(n*2024)
    arr = newArr

print(len(arr))