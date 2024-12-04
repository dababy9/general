import re

f = open("input.txt", "r")

pattern_list = [
    (r"mul\([0-9]*,[0-9]*\)", 'MUL'),
    (r"don't\(\)", 'DONT'),
    (r"do\(\)", 'DO')
]

patterns = [(re.compile(pattern), ty) for pattern, ty in pattern_list]

s = f.read()

tokens = []
pos = 0

while pos < len(s):
    match = None
    for regex, ty in patterns:
        match = regex.match(s, pos)
        if match:
            value = match.group(0)
            tokens.append((ty, value))
            pos = match.end(0)
            break
    if not match:
        pos += 1

total = 0
allow_mul = True

for ty, pattern in tokens:
    if ty == 'DO':
        allow_mul = True
    elif ty == 'DONT':
        allow_mul = False
    elif allow_mul:
        nums = re.findall("[0-9][0-9]*", pattern)
        total += int(nums[0]) * int(nums[1])

print(total)