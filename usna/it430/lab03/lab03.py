def showpkts(data):
    data = data[24:]
    i = 8
    while i < len(data):
        try:
            length = int.from_bytes(data[i:i+4], 'little')
            i += 8
            packet = data[i:i+length]
            __hexdump(packet)
            i += length + 8
        except IndexError:
            break

def showpkts_Eth(data):
    data = data[24:]
    i = 8
    while i < len(data):
        try:
            length = int.from_bytes(data[i:i+4], 'little')
            i += 8
            packet = data[i:i+length]
            src = packet[0:6]
            dest = packet[6:12]
            t = packet[12:14]
            __printEth(src, "Dst")
            __printEth(dest, "src")
            print(f"Type= {t[0]:02x} {t[1]:02x}")
            packet = packet[14:]
            __hexdump(packet)
            i += length + 8
        except IndexError:
            break

def __hexdump(packet):
    pos = 0
    print("data:")
    while True:
        try:
            for i in range(0, 16):
                print(f"{packet[pos+i]:02x}", end=' ')
                if i == 7:
                    print(" ", end="")
            print("")
            pos += 16
        except IndexError:
            break
    print("\n")

def __printEth(addr, s):
    print(f"{s}-MAC= ", end="")
    for i in range(0, 6):
        if i != 0: print(":", end="")
        print(f"{addr[i]:02x}", end="")
    print("")