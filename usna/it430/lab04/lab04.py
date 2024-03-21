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
            frame = data[i:i+length]
            print(f"Dst-MAC= {__ethToString(frame[0:6])}")
            print(f"Src-MAC= {__ethToString(frame[6:12])}")
            packet = frame[14:]
            __hexdump(packet)
            i += length + 8
        except IndexError:
            break

def showpkts_IP(data):
    data = data[24:]
    i = 8
    while i < len(data):
        try:
            length = int.from_bytes(data[i:i+4], 'little')
            i += 8
            frame = data[i:i+length]
            print(f"Dst-MAC= {__ethToString(frame[0:6])}")
            print(f"Src-MAC= {__ethToString(frame[6:12])}")
            packet = frame[14:]
            ihl = packet[0] & 15
            print(f"IHL= {ihl}")
            pLength = int.from_bytes(packet[2:4], 'big')
            print(f"Total Length= {pLength}")
            print(f"Src-IP= {__ipToString(packet[12:16])}")
            print(f"Dst-IP= {__ipToString(packet[16:20])}")
            segment = packet[ihl*4:pLength]
            __hexdump(segment)
            i += length + 8
        except IndexError:
            break

def showpkts_TCP(data, addr1, addr2):
    data = data[24:]
    i = 8
    while i < len(data):
        try:
            length = int.from_bytes(data[i:i+4], 'little')
            i += 8
            packet = data[i+14:i+length]
            pLength = int.from_bytes(packet[2:4], 'big')
            addrs = [__ipToString(packet[12:16]), __ipToString(packet[16:20])]
            if packet[9] == 6 and addr1 in addrs and addr2 in addrs:
                segment = packet[(packet[0] & 15)*4:pLength]
                payload = segment[(segment[12] >> 4)*4:]
                if len(payload) != 0:
                    print(f"{addrs[0]}({int.from_bytes(segment[0:2], 'big')}) ->  {addrs[1]}({int.from_bytes(segment[2:4], 'big')}) :")
                    print(f"\t{payload}")
            i += length + 8
        except IndexError:
            break

def __hexdump(packet):
    pos = 0
    lastIndex = 0
    print("data:")
    while True:
        try:
            for i in range(0, 16):
                print(f"{packet[pos+i]:02x}", end=' ')
                lastIndex = i
                if i == 7:
                    print(" ", end="")
            print("")
            pos += 16
        except IndexError:
            break
    if lastIndex != 15: print("\n")
    else: print("")

def __ethToString(addr):
    result = ""
    for i in range(0, 6):
        if i != 0: result += ":"
        result += addr[i:i+1].hex()
    return result

def __ipToString(addr):
    result = ""
    for i in range(0, 4):
        if i != 0: result += "."
        result += str(addr[i])
    return result
