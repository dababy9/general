# test.py
import pr   # the name of the module you have to write in this practicum

filename = input("Filename: ") 
data = open(filename, "rb").read()
pr.showpkts_ARP(data)