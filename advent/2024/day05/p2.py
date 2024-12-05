f = open("input.txt", "r")

m = {}
wrong = []

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
                    m[line[i]] = []
        if not valid:
            wrong.append(line)
            
for line in wrong:
    for i in range(len(line)-1, 0, -1):
        for j in range(i):
            if [x for x in m[line[j]] if x in line[0:i+1]] == []:
                line[i], line[j] = line[j], line[i]
                break
    total += int(line[len(line)//2])
print(total)