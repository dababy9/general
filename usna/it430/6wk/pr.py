def showpkts_ARP(data):
    data = data[24:]
    f = True
    i = 8
    while i < len(data):
        length = int.from_bytes(data[i:i+4], 'little')
        i += 8
        frame = data[i:i+length]
        if int.from_bytes(frame[12:14], 'big') == 2054:
            if f: f = False
            else: print("")
            print("===")
            print(f"Dst-MAC= {__ethToString(frame[0:6])}")
            print(f"Src-MAC= {__ethToString(frame[6:12])}")
            packet = frame[14:]
            print(f"Opcode= {int.from_bytes(packet[6:8], 'big')}")
            __printARP(packet[8:18], "Sender")
            __printARP(packet[18:28], "Target")
            print("===")
        i += length + 8



def __printARP(data, name):
    print(f"{name}-HW-Addr= {__ethToString(data[0:6])}")
    print(f"{name}-Prot-Addr= {__ipToString(data[6:10])}")



def __ipToString(addr):
    result = ""
    for i in range(0, 4):
        if i != 0: result += "."
        result += str(addr[i])
    return result



def __ethToString(addr):
    result = ""
    for i in range(0, 6):
        if i != 0: result += ":"
        result += addr[i:i+1].hex()
    return result