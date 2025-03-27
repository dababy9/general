import random
import copy

length = random.randint(2, 50)

target = random.sample(range(1, 100), length)

l1 = copy.copy(target)
l2 = copy.copy(target)

random.shuffle(l1)
random.shuffle(l2)

def swap(i):
    if l1[i] == l2[i]:
        del l1[i]
        del l2[i]

    elif l1[i] > l2[i]:
        if i < len(l1)-1:
            l1[i], l1[i+1] = l1[i+1], l1[i]
        if i > 0:
            l2[i], l2[i-1] = l2[i-1], l2[i]

    else:
        if i > 0:
            l1[i], l1[i-1] = l1[i-1], l1[i]
        if i < len(l1)-1:
            l2[i], l2[i+1] = l2[i+1], l2[i]

def p():
    print(l1)
    print(l2)
    print('---------------')

index = 1
count = 0
while True:
    swap(index)
    count += 1
    if not l1:
        break
    index = (index + 1) % len(l1)

print(f'length: {length}')
print(f'brute force: {int(length*(length+1)/2)}')
print(f'actual: {count}')