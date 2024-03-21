import sys

try:
    b = open(sys.argv[1], "rb").read()
    pos = 0
    end = False
    while not end:
        print(f"{pos:08x}", end="  ")
        try:
            for i in range(0, 16):
                print(f"{b[pos+i]:02x}", end=" ")
                if i == 7 or i == 15:
                    print(" ", end="")
        except IndexError:
            print("   "*(16 - (len(b)%16)), end=" ")
            end = True
        print("|", end="")
        try:
            for i in range(0, 16):
                char = chr(b[pos+i])
                if not str.isprintable(char):
                    char = "."
                print(char, end="")
        except IndexError:
            pass
        print("|")
        pos += 16
    print(f"{len(b):08x}")

except OSError:
    print(f"hexdump: {sys.argv[1]}: No such file or directory")
    print("hexdump: all input file arguments failed")
    exit(1)