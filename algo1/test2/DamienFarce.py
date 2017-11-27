import random
import pdb

def saisieEntier(borneMin, borneMax):
    userInput = borneMin - 1 #Pour que le while soit toujours vrai peut importe la borneMin
    while (userInput < borneMin or userInput > borneMax):
        print ("veuillez rentrer votre nombre entre"+ str(borneMin) +" et "+str(borneMax) + ":\n")
        userInput = int(input())
    return userInput

def saisie(borneMin, borneMax):
    userInput = borneMin - 1 #Pour que le while soit toujours vrai peut importe la borneMin
    while (userInput < borneMin or userInput > borneMax):
        userInput = input()
    return userInput

def saisieEntierAllumette(nbAllumette): #S'il reste moins d'allumette que 3 dans le jeu
    if nbAllumette > 3:
        return saisieEntier(1, 3)
    else:
        return saisieEntier(1, nbAllumette)

def jouer2Joueurs(nombreAllumette):
    joueur1Turn = True
    while nombreAllumette > 0:
        if joueur1Turn:
            print("tour du joueur 1, combien d'allumette retirez-vous ? \n") 
            nombreAllumette = saisieEntierAllumette(nombreAllumette) #On ne peut retirez plus d'allummette que nombre d'allummette ou 3
        else:
            print("tour du joueur 2, combien d'allumette retirez-vous ? \n")
            nombreAllumette = saisieEntierAllumette(nombreAllumette)
        joueur1Turn = not joueur1Turn #Changement de joueur
        print("il reste "+str(nombreAllumette))
    if joueur1Turn:
        print("Joueur 1 a gagné")
    else: 
        print("Joueur 2 a gagné")

def jouerOrdiNaif(nombreAllumette):
    joueur1Turn = True
    while nombreAllumette > 0:
        if joueur1Turn:
            print("tour du joueur 1, combien d'allumette retirez-vous ? \n") 
            nombreAllumette = nombreAllumette - saisieEntierAllumette(nombreAllumette) #On ne peut retirez plus d'allummette que nombre d'allummette ou 3
        else:
            if (nombreAllumette - 4 % 4 == 0):
                nombreAllumette = nombreAllumette - 3
            if (nombreAllumette - 3 % 4 == 0):
                nombreAllumette = nombreAllumette - 2 
            if (nombreAllumette - 2 % 4 == 0):
                nombreAllumette = nombreAllumette - 1
            else:
                nombreAllumette = nombreAllumette - random.randint(1,nombreAllumette) % 3 #S'il y a moins de 3 allumette dans le jeux il faut faire un modulo pour éviter de passer en négatif
             
        print("il reste "+str(nombreAllumette))
        joueur1Turn = not joueur1Turn #Changement de joueur

    if not joueur1Turn: #Le jeu est terminé et ce n'est pas à lui de jouer le prochain tour c'est donc lui qui a gagné
        print("Joueur 1 a gagné")
    else: 
        print("l'ordinateur a gagné")



class Liste:
    def __init__(self):
        self.premierMaillon = None #Le premier maillon pour renter dans le liste

    def affichageInverse(self):
        if self.premierMaillon != None:
            return self.premierMaillon.printInv() #Appel de la fonction sur le premier élément de la liste

    def insertionTrie(self, valeur):
        if (self.premierMaillon == None): #Cas où le premier maillon est vide
            self.premierMaillon = Maillon(valeur)
        else:
            maillonCourant = self.premierMaillon
            if valeur < self.premierMaillon.getValeur(): #Cas ou la valeur est inférieur au premier maillon
                nouveauMaillon = Maillon(valeur)
                nouveauMaillon.maillonSuivant = maillonCourant 
                self.premierMaillon = nouveauMaillon
            else:
                while  maillonCourant.getSuivant() != None and maillonCourant.getSuivant().getValeur() < valeur: #Pour tout les autres cas, si le maillon suivant est plus grand que la valeur alors il faut le placer entre le maillon courant et le suivant.
                    maillonCourant = maillonCourant.getSuivant() 
                nouveauMaillon = Maillon(valeur)
                nouveauMaillon.maillonSuivant = maillonCourant.getSuivant()
                maillonCourant.maillonSuivant = nouveauMaillon

    def intersection(self, secondeList):
        maillonCourant = self.premierMaillon
        while (maillonCourant != None): #itération sur toutes les valeurs de la liste 1 
            nbCourant = maillonCourant.getValeur()
            maillonCourantSecondeList = secondeList.premierMaillon
            while (maillonCourantSecondeList != None): #recherche de la valeur i dans le seconde list en itérant sur toutes les valueurs de la seconde liste
                if maillonCourantSecondeList.getValeur() == nbCourant:
                    print (nbCourant)
                maillonCourantSecondeList = maillonCourantSecondeList.getSuivant()
            maillonCourant = maillonCourant.getSuivant()



class Maillon:
    def __init__(self, v):
        self.valeur = v #Valeur du maillon
        self.maillonSuivant = None  #pointeur vers le prochain maillon
    
    def getSuivant(self):
        return self.maillonSuivant

    def getValeur(self):
        return self.valeur

    #Fonction récursive pour l'affichage inverse de la liste
    def printInv(self):
        if self.maillonSuivant == None: #condition d'arrêt 
            return str(self.valeur)
        else:
            return (self.maillonSuivant.printInv() + "  " + str(self.valeur)) #Appel de la même fonction pour le récursivité

def pi():
    plus = True #Pour alterner entre l'addition et la soustraction
    pi = 3 #Valeur de pi a retourner 
    compteur = 2 #compteur pour le diviseur du terme 
    while True:
        temp = 4/(compteur* (compteur+1)* (compteur+2))
        if temp < 10**(-10): #Condition de retour et d'arrêt de la boucle
            return pi 
        else:
            if plus:
                pi = pi + temp
            else: 
                pi = pi - temp 
            compteur += 2 
            plus = not plus #Alternance addition/soustraction


if __name__ == "__main__":
    l = Liste()
    l.insertionTrie(60)
    l.insertionTrie(5)
    l.insertionTrie(4)
    l.insertionTrie(1)
    ll = Liste()
    ll.insertionTrie(60)
    ll.insertionTrie(4)
    ll.insertionTrie(1)
    ll.insertionTrie(8)
    l.intersection(ll)
    print(l.premierMaillon.getValeur())
    print(l.premierMaillon.getSuivant().getSuivant().getSuivant().getValeur())
    print(l.affichageInverse())
    print (pi())
    jouerOrdiNaif(20)
