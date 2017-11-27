import pdb

class List:
    def __init__(self):
        self.firstElem = None
        self.lastElem = None

    def insert(self, item, place):
        if self.firstElem == None:
            self.firstElem = Elem(item)
        else:
            currentElement = self.firstElem
            for i in range(place-1):
                currentElement = currentElement.getNext() 
            pdb.set_trace()
            currentElement.setNext(Elem(item,currentElement, currentElement.getNext()))

    def concat(self, secondList):
        currentElement = self.firstElem
        while (currentElement.nextElement != None):
            currentElement = currentElement.nextElement
        currentElement.nextElement = secondList.firstElem

    def search(self, item):
        currentElement = self.firstElem
        while (currentElement.nextElement != None and currentElement.value != item):
            currentElement = currentElement.nextElement
        return currentElement.value == item 
    
    def toString(self):
        stringToReturn = ""
        currentElement = self.firstElem
        while (currentElement.nextElement != None):
            stringToReturn += str(currentElement.value) + " "
            currentElement = currentElement.nextElement
        return stringToReturn + str(currentElement.value)


class Elem:
    def __init__(self, value=None, previous=None, nextE=None):
        self.nextElement = nextE
        self.previousElement = previous
        self.value = value

#    def __init__(self, value):
#        self.nextElement = None
#        self.previousElement = None
#        self.value = value
#
#    def __init__(self, value, previous):
#        self.previousElement = previous
#        self.nextElement = None 
#        self.value = value
#
#    def __init__(self, value, previous, nextE):
#        self.nextElement = nextE
#        self.previousElement = previous
#        self.value = value

    def getNext(self):
        return self.nextElement

    def getPrevious(self):
        return self.previousElement

    def setNext(self, nextElem):
        self.nextElement = nextElem

    def setPrevious(self, previousElem):
        self.previousElement = previousElem


if __name__ == "__main__":
    l1 = List()
    l1.insert(1,0)
    l1.insert(2,1)
    l1.insert(4,0)
    print(l1.toString())
