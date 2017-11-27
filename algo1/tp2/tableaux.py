def palindrome():
    print ("input your words please")
    userInputList = []
    userInput = input()
    while (userInput != ""):
        userInputList.append(userInput)
        userInput = input()
    for word in userInputList:
        if word == word[len(word)::-1]:
            print(word)


def triABulle(inputList):
    listToSort = inputList
    for i in range(len(listToSort)-1):
        y = i
        while y < len(listToSort)-1 and listToSort[y] > listToSort[y+1]:
            print(listToSort[y])
            temp = listToSort[y+1]
            listToSort[y+1] = listToSort[y]
            listToSort[y] = temp
            y += 1
        y = i
        while y > 0 and listToSort[y] < listToSort[y-1]:
            print(listToSort[y])
            temp = listToSort[y-1]
            listToSort[y-1] = listToSort[y]
            listToSort[y] = temp
            y -= 1
    return listToSort
    
            

if __name__ == "__main__":
    palindrome()


