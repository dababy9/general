def noFruit(name):
    print(f"Error! {name} not found!")   

file = input("Filename: ")
try:
    file = open(file)
except OSError:
    print("File not found!")
    exit(1)

lines = file.readlines()
prices = {}
amounts = {}
for line in lines:
    l = line.split()
    prices[l[0]] = float(l[1])
    amounts[l[0]] = 0

while True:
    inp = input("command: ").split()
    if inp[0] == "add":
        if inp[3] in prices:
            amounts[inp[3]] += float(inp[1])
        else:
            noFruit(inp[3])
    elif inp[0] == "price":
        if inp[1] in prices:
            print(f"{inp[1]} are ${prices[inp[1]]} per pound")
        else:
            noFruit(inp[1])
    elif inp[0] == "checkout":
        break
    else:
        print("Error! unknown command!")

total = 0
for fruit in amounts:
    total += amounts[fruit] * prices[fruit]
print(f"total is ${total}")

file.close()