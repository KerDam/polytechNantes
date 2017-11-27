import pdb

class Pile:

    def __init__(self):
        self.array = []

    def push(self, value):
        self.array.append(value)

    def pop(self):
        temp = self.array[len(self.array)-1]
        self.array = self.array[0:len(self.array)-1]
        return temp 

    def top(self):
        return self.array[len(self.array)-1]

    def isEmpty(self):
        return self.array == [] or self.array == None

def calculPrefix(s):
    splited = s.split(" ")
    numberPile = Pile()
    operatorPile = Pile()
    for item in splited[-1::-1]:
        try:
            numberPile.push(int(item))
        except:
            operatorPile.push(item)
    while not operatorPile.isEmpty():
        operator = operatorPile.pop()
        if operator == "+":
            numberPile.push(numberPile.pop() + numberPile.pop())
        if operator == "-":
            numberPile.push(numberPile.pop() - numberPile.pop())
        if operator == "*":
            numberPile.push(numberPile.pop() * numberPile.pop())
        if operator == "/":
            numberPile.push(numberPile.pop() / numberPile.pop())
    return numberPile.pop()

if __name__ == "__main__":
    print(calculPrefix("+ - 4 * 3 2 / 5 2"))

