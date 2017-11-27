import pdb

class Tree:
    def __init__(self, node):
        self.root = node

    def root(self):
        return self.root 

    def isEmpty(self):
        return self.root.getL() == None and self.root.getR() == None and self.root.getValue() == None

    def printTree(self):
        self.root.printNode()

    def depth(self):
        return self.root.depth()

    def nbNode(self):
        return self.root.nbNode()

    def sum(self):
        return self.root.sum()

    def inc(self):
        return self.root.inc()

    def hierarchy(self):
        return self.root.hierarchy()
    
    def insert(self, item):
        self.root.insert(item)

    def insertBis(self, item):
        current = self.root
        if current.value >= item:
            while (current.nodeL != None and current.value >= item and current.nodeL.value >= item):
                current = current.nodeL 
            current.nodeL = Node(item, current.nodeL, None)
        else: 
            while (current.nodeR != None and current.value <= item and current.nodeR.value <= item):
                current = current.nodeR
            current.nodeR = Node(item, None, current.nodeR)

    def search(self, item):
        return self.root.search(item)

    def searchBis(self, item):
        current = self.root
        if current.value >= item:
            while (current.nodeL != None and current.value >= item and current.nodeL.value >= item):
                current = current.nodeL 
            return current.value == item 
        else: 
            while (current.nodeR != None and current.value <= item and current.nodeR.value <= item):
                current = current.nodeR
            return current.value == item 

    def toString(self):
        return self.root.toString()

    def calc(self):
        return self.root.calc()

    def getParent(self, node):
        return self.root.getParent(node)

                
    def createCalcTree(self, s):
        finished = False
        i = 0
        firstNode = (None, None, None)
        while not finished:
            if s[i] == "+" or s[i] == "-" or s[i] == "*"
                if currentNode.value == None:
                    currentNode.value = s[i]
                else:
                    if currentNode.
                    currentNode.Node(s[i], None, None)
                    currentNode = currentNode.NodeL
            elif s[i].isdigit():
                if currentNode.nodeL == None:
                    currentNode.nodeL = int(s[i])
                elif currentNode.nodeR == None:
                    currentNode.nodeR = int(s[i])


class Node:
    def __init__(self, value=None, nodeL=None , nodeR=None):
        self.value = value
        self.nodeL = nodeL
        self.nodeR = nodeR

    def setL(self, nodeL):
        self.nodeL = nodeL

    def setR(self, nodeR):
        self.nodeR = nodeR

    def setValue(self, val):
        self.value = val

    def getValue(self):
        return self.value

    def getL(self):
        return self.nodeL

    def getR(self):
        return self.nodeR

    def getParent(self, node):
        if self.nodeL == node or self.nodeR == node:
            return self
        else:
            if self.nodeL != None:
                self.nodeL.getParent(node)
            if self.nodeR != None:
                self.nodeR.getParent(node) 

    def printNode(self):
        if self.value != None:
            print(str(self.value) + "\n")
        if self.getL() != tern():
            self.getL().printNode()
        if self.getR() != tern():
            self.getR().printNode()

    def depth(self):
        if self.nodeL == None and self.nodeR == None and self.value != None:
            return 1
        else:
            lenghtL, lenghtR = 0, 0
            if self.nodeL != None:
                lenghtL = self.nodeL.depth()
            if self.nodeR != None:
                lenghtR = self.nodeR.depth()
            if lenghtR > lenghtL:
                return 1 + lenghtR
            else:
                return 1 + lenghtL 

    def nbNode(self):
        if self.nodeL == None and self.nodeR == None and self.value != None:
            return 1
        else:
            nbNodeL, nbNodeR = 0, 0
            if self.nodeL != None:
                nbNodeL = self.nodeL.nbNode()
            if self.nodeR != None:
                nbNodeR = self.nodeR.nbNode()
            return 1 + nbNodeR + nbNodeL

    def sum(self): 
        if self.nodeL == None and self.nodeR == None and self.value != None:
            return self.value
        else:
            sumL, sumR= 0, 0
            if self.nodeL != None:
                sumL = self.nodeL.sum()
            if self.nodeR != None:
                sumR = self.nodeR.sum()
            return self.value + sumL + sumR

    def inc(self): 
        if self.nodeL == None and self.nodeR == None and self.value != None:
            self.value += 1 
        else:
            self.value += 1 
            if self.nodeL != None:
                self.nodeL.inc()
            if self.nodeR != None:
                self.nodeR.inc()

    def hierarchy(self):
        if self.nodeL == None and self.nodeR == None and self.value != None:
            return True
        else:
            boolL, boolR = True, True
            if self.nodeL != None:
                boolL = self.value >= self.nodeL.getValue() and self.nodeL.hierarchy()
            if self.nodeR != None:
                boolR = self.value >= self.nodeR.getValue() and self.nodeR.hierarchy() 
            return boolL and boolR 
       
    def insert(self, item):
        #inférieur courant et suivant
        if (self.value > item and (self.nodeL != None and self.nodeL.value > item)):
            self.nodeL.insert(item)
        #inférieur courant et sup suivant
        if (self.value > item and (self.nodeL == None or self.nodeL.value < item)):
            self.nodeL = Node(item, None, self.nodeL)
        #sup courant et suivant
        if (self.value < item and (self.nodeR != None and self.nodeR.value < item)):
            self.nodeR.insert(item)
        #sup courant et inf suivant
        if (self.value < item and (self.nodeR == None or self.nodeR.value > item)):
            self.nodeR = Node(item, self.nodeR, None)

    def search(self, item):
        if self.value == item:
            return True
        elif self.value > item and self.nodeL != None:
            return self.nodeL.search(item)
        elif self.value < item and self.nodeR != None:
            return self.nodeR.search(item)
        else: return False

    def toString(self):
        leftSide, rightSide = "", ""
        if self.nodeL != None:
            leftSide = self.nodeL.toString()
        if self.nodeR != None:
            rightSide = self.nodeR.toString()
        return leftSide + " " + str(self.value) + " " + rightSide

    def calc(self):
        if self.value == "+":
            return self.nodeL.calc() + self.nodeR.calc()
        elif self.value == "-":
            return self.nodeL.calc() - self.nodeR.calc()
        elif self.value == "*":
            return self.nodeL.calc() * self.nodeR.calc()
        elif self.nodeL == None and self.nodeR == None:
            return self.value

    def createCalcTree(self, s):
        for i in range(len(s)):
            if s[i] == "+" or s[i] == "-" or s[i] == "*":
                if self.value == None:
                    self.value = s[i]
                elif self.nodeL == None:
                    self.nodeL = s[i]
                elif self.nodeR == None:
                    self.nodeR = s[i]
                else:
                    self.getParent().createCalcTree(s[i:len(s)])

def tern():
    return None

def sortByTree(l):
    t = Tree(Node(l[0], tern(), tern()))
    for i in l[1::]:
        t.insertBis(i)
    return t.toString()




if __name__ == "__main__":
    tree = Tree(Node(10, tern(), tern()))
    tree.insert(1)
    tree.insert(15)
    tree.insertBis(6)
    tree.insertBis(7)
    tree.printTree()
    print(tree.search(7)) 
    print(tree.search(20)) 
    print(tree.searchBis(7)) 
    print(tree.searchBis(20)) 
    print(tree.toString())
    print(sortByTree([1,-5,60,74,2,-56]))
    treeC = Tree(Node("+", Node("-",Node(4, tern(),tern()), Node(2,tern(),tern())), Node("*",Node(3, tern(), tern()),Node(5,tern(),tern()))))
    print(treeC.calc())
