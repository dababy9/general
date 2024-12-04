import re

f = open("input.txt", "r")

l = re.findall("mul[(][0-9]*,[0-9]*[)]", f.read())

total = 0

for item in l:
    nums = re.findall("[0-9][0-9]*", item)
    total += int(nums[0]) * int(nums[1])

print(total)