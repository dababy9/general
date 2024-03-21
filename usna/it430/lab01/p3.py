def pr(a):
    print(f"dec: {a:d}")
    print(f"hex: 0x{a:02x}")
    print(f"bin: {a:08b}")

def zero_out_top(a, n):
    num = (2**(8-n))-1
    return a&num

def set_one_at(a, i):
    num = 128
    num >>= i
    return a|num

def set_zero_at(a, i):
    num = set_one_at(0, i) ^ 255
    return a&num