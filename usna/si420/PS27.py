k, m1, m2 = 2, [-1, 1], [-0.9, 1.1]
data = [[1.2, -1.0],
        [-0.7, 2.1],
        [-0.2, -0.07],
        [-2.4, 2.4],
        [1.1, 0.4],
        [-3.6, 0.8],
        [0.6, -1.1],
        [0.01, -0.8],
        [1.0, -0.1],
        [-1.9, 0.9]]

def dist(p1, p2):
    return (p1[0]-p2[0])*(p1[0]-p2[0]) + (p1[1]-p2[1])*(p1[1]-p2[1])

def update(list):
    mean = [0, 0]
    for x in list:
        mean[0] += x[0]
        mean[1] += x[1]
    return [mean[0]/len(list), mean[1]/len(list)]



while True:
    list1, list2 = [], []
    for x in data:
        if dist(x, m1) < dist(x, m2): list1.append(x)
        else: list2.append(x)
    print(f"List 1: {list1}")
    print(f"List 2: {list2}")
    newM1 = update(list1)
    newM2 = update(list2)
    print(f"Updated m1: {newM1}")
    print(f"Updated m2: {newM2}")
    if newM1 == m1 and newM2 == m2: break
    else:
        m1 = newM1
        m2 = newM2