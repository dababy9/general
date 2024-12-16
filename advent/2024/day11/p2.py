f = open("input.txt", "r")

arr = [int(x) for x in f.readline().split(" ")]

memo = {}
    
def count_stones(stone, blinks_left):
    if blinks_left == 0:
        return 1
    if (stone, blinks_left) in memo:
        return memo[(stone, blinks_left)]
    if stone == 0:
        result = count_stones(1, blinks_left - 1)
    elif len(str(stone)) % 2 == 0:
        num_str = str(stone)
        mid = len(num_str) // 2
        left = int(num_str[:mid])
        right = int(num_str[mid:])
        result = count_stones(left, blinks_left - 1) + count_stones(right, blinks_left - 1)
    else:
        new_stone = stone*2024
        result = count_stones(new_stone, blinks_left - 1)
    memo[(stone, blinks_left)] = result
    return result

total_stones = sum(count_stones(stone, 75) for stone in arr)

print(total_stones)