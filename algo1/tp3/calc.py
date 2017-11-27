import pdb
import random
def fac(x):
    if x == 0:
        return 1
    else:
        return x * fac(x-1)

def pgcd(a,b):
    if b == 0:
        return a
    else:
        return pgcd(b, a % b)

#def search1(sortedList, valueToFind, markerInf, markerSup):
#    if (marker >= 0 and marker < len(sortedList)):
#        if sortedList[marker] == valueToFind:
#            return marker
#        else:
#            if sortedList[marker] < valueToFind:
#                return search(sortedList, valueToFInd, markerInf, markerInf+(markerSup - markerInf)//2)
#                return search(sortedList, valueToFind, markerInf, markerInf+((len(sortedList)-marker)//2))
#            elif sortedList[marker] > valueToFind:
#                return search(sortedList, valueToFind, markerSup-marker//2, markerSup)
#                return search(sortedList, valueToFind, markerInf
#    else:
#        return -1

#Ne fonctionne pas si la valeur n'est pas dans la liste
def search(sortedList, valueToFind, markerInf, markerSup):
    marker = (markerSup + markerInf ) // 2 #The next indice to try
    #marker = markerInf + ((markerSup + markerInf ) // 2) #The next indice to try
    if sortedList[marker] == valueToFind :
        return marker
    else:
        if sortedList[marker] > valueToFind:
            return search(sortedList, valueToFind, markerInf, marker)
        elif sortedList[marker] < valueToFind:
            return search(sortedList, valueToFind, marker, markerSup)
        else:
            return -1

def nbChiffres(n):
    if n < 1:
        return 0
    else:
        return 1 + nbChiffres(n // 10)

def nbChiffresBase(n, b):
    if n < 1:
        return 0
    else:
        return 1 + nbChiffresBase(n/b,b)

def convert(n,b):
    if n < 1:
        return ""
    else:
        return convert(n//b,b) + str(n%b)

def convertInv(n,b):
    if n < 1:
        return ""
    else:
        return str(n%b) + convertInv(n//b,b) 

#def quickSort(listToSort):
#    if len(listToSort) < 1:
#        return []
#    else:
#        marker = listToSort[0]
#        listLower = []
#        listUp = []
#        for i in listToSort:
#            if i < marker:
#                listLower.append(i)
#            elif i > marker:
#                listUp.append(i)
#        return quickSort(listLower) + [marker] + quickSort(listUp)

#def quickSort(l):
#    if len(l) <= 1: #If there is only 1 element in the list it is sorted
#        return l
#    else:
#        marker = l[0]
#        cursorEnd = len(l) - 1
#        indiceMarker = 1
#        for i in range(len(l)):
#            if l[i] >= marker:
#                changed = False
#                for y in range(i,len(l)):
#                    if l[y] < marker and not changed:
#                        temp = l[i]
#                        l[i] = l[y]
#                        l[y] = temp
#                        indiceMarker += 1
#                        changed = True
#        if indiceMarker < 2 and len(l) > 1 and marker > l[1] :
#            [l[len(l)-1]] + l[1:len(l)-2] + [marker]
#            indiceMarker = len(l) - 1
#        print(l)
#        print(indiceMarker)
#        pdb.set_trace()
#        return quickSort(l[0:indiceMarker]) + quickSort(l[indiceMarker::])
def quickSort(l):
    if len(l) <= 1:
        return l
    else:
        pivot = l[0]
        i = 1
        j = len(l) - 1
        while True:
            while i <= j and l[i] <= pivot:
                i += 1
            while i <= j and l[j] >= pivot:
                j -= 1
            if i >= j:
                j +=1
                break
            l[i], l[j] = l[j], l[i]
        #l[0], l[j] = l[j], l[0]
        print(l)
        pdb.set_trace()
        return quickSort(l[1:j]) + [pivot] + quickSort(l[j::])


        #for i in range(len(listToSort)-1): #Iterate through the list
        #    if i < markerEnd: #The list is sorted no need to continue
        #        if listToSort[i] >= marker: #If the element is higher than the marker then shit it with a element lower from the end of the list 
        #            y = markerEnd
        #            while listToSort[y] > marker and y > i:
        #                y -= 1
        #            if y >= 1 and listToSort[i] > listToSort[y]:
        #                temp = listToSort[i]
        #                listToSort[i] = listToSort[y]
        #                listToSort[y] = temp
        #                markerEnd = y
        #                if temp == marker:
        #                    compteur = y
        #                compteur +=1
        #print(listToSort)
        #pdb.set_trace()
        #return quickSort(listToSort[0:compteur]) + quickSort(listToSort[compteur::])


if __name__ == "__main__":
   # randomGeneratedList = [random.randint(0,1000) for r in range(10)] 
   # print("what do you want to find")
   # randomGeneratedList.sort()
   # print(randomGeneratedList)
   # numberToFind = input()
   # print(search(randomGeneratedList,int(numberToFind),0,9))
   # print(randomGeneratedList)
   print(nbChiffres(17328))
   print(nbChiffresBase(129, 2))





