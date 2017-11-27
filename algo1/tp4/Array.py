import pdb
class Array:
    def __init__(self):
        self.l = []

    def insert(self, item, place):
        if len(self.l) == 0:
            self.l.append(item)
        else:
            self.l.append(None)
            for i in range(len(self.l)-1, place, -1):
                self.l[i] = self.l[i-1]
            self.l[place] = item

    def search(self, item):
        for i in range(len(self.l)):
            if self.l[i] == item:
                return i
        return -1

def concat(array1, array2):
    for item in array2:
        array1.append(item)
    return array1

if __name__ == "__main__":
    a1 = Array()
    a1.insert(1,0)
    a1.insert(2,0)
    a1.insert(3,0)
    a1.insert(4,0)
    print(a1.search(3))
    print(a1.l)



