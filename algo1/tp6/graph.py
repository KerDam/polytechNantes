#!/usr/bin/python3
import unittest
import pdb
import copy

def isOriented(AdjacencyMatix):
    for i in range (len(AdjacencyMatix)):
        for y in range(len(AdjacencyMatix[i])):
            if (AdjacencyMatix[i][y] != AdjacencyMatix[y][i]):
                return False
    return True

class Graph():
    def __init__(self, nodes = [], links = []):
        self.nodes = nodes
        self.links = links

    def isLinked(self, x, y):
        """
            :param x: node x
            :param y: node y
            :return: return True if x and y are connected
            :rtype: Boolean
        """
        return self.links[x][y] == 1

    def getListAdjacencies(self):
        compt = 1
        listAdjacencies = []
        for i in range(len(self.links)):
            for y in range(compt):
                if self.links[i][y] == 1:
                    listAdjacencies.append((i,y))
            compt += 1
        return listAdjacencies

    def getMatriceAdjacencies(listAdjacencies, listNodes):
        matrice = Graph.createMatrice(len(listNodes))
        for t in listAdjacencies:
            matrice[t[0]][t[1]], matrice[t[1]][t[0]] = 1, 1
        return matrice

    def createMatrice(length):
        line = [0]*length
        matrice = []
        for i in range(length):
            matrice.append(copy.copy(line))
        return matrice

    def addLink(self, x, y):
        self.links[x][y] = 1
        self.links[y][x] = 1

    def removeLink(self, x, y):
        self.links[x][y] = 0
        self.links[y][x] = 0

    def getAdjacencies(self,x):
        res = []
        for y in range(len(self.links[x])):
            if self.links[x][y] == 1:
                res.append(y)
        return res

    def getNumberOfLinks(self):
        return len(self.getListAdjacencies)

    def


class Test(unittest.TestCase):
    def setUp(self):
        self.graph = Graph([1,2,3], [[1,1,0],
                                     [1,1,1],
                                     [0,1,1]])
    def testisLinked(self):
        self.assertTrue(self.graph.isLinked(0,1))
        self.assertFalse(self.graph.isLinked(0,2))

    def testisOriented(self):
        self.assertTrue(isOriented([[1,2,3],
                                    [2,1,4],
                                    [3,4,1]]))
        self.assertFalse(isOriented([[1,2,5],
                                     [1,2,5],
                                     [1,2,4]]))

    def testGetListAdjacencies(self):
        self.assertEquals(self.graph.getListAdjacencies(), [(0,0),(1,0),(1,1),(2,1),(2,2)])

    def testGetMatriceAdjacencies(self):
        self.assertEquals(Graph.getMatriceAdjacencies([(0,0),(1,0),(1,1),(2,1),(2,2)],[0,1,2]), self.graph.links)

    def testRemoveLink(self):
        self.graph.removeLink(1,2)
        self.assertEquals(self.graph.links,[[1,1,0],[1,1,0],[0,0,1]])

    def testAddLink(self):
        self.graph.addLink(2,0)
        self.assertEquals(self.graph.links,[[1,1,1],[1,1,1],[1,1,1]])

    def testGetAdjacencies(self):
        self.assertEquals(self.graph.getAdjacencies(0),[0,1])

    def testGetNumberLinks(self):
        self.assertEquals(self.graph.getNumberOfLinks(), 5)

if __name__ == "__main__":
    unittest.main()
