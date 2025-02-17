f = open("input.txt", "r")

m = {}

total = 0

reading_rules = True
for line in f:
    line = line.strip()
    if reading_rules:
        try:
            left, right = line.split('|')
            if left not in m:
                m[left] = [right]
            else:
                m[left].append(right)
        except:
            reading_rules = False
    else:
        line = line.split(",")
        valid = True
        for i in range(len(line)):
            num = line[i]
            for j in range(i):
                try:
                    if line[j] in m[line[i]]:
                        valid = False
                        break
                except:
                    pass
        if valid:
            total += int(line[len(line)//2])

print(total)