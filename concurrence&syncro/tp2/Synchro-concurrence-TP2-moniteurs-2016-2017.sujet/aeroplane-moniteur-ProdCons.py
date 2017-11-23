
#
#   Simulation d'une usine d'assemblage d'aeroplanes
#


#   L'usine est composée d'un ensemble de chaines séquentielles de montages
#   qui sont autonomes et fonctionnent en parallèle:
#   - 1 chaine de production de carlingues
#   - 1 chaine de production d'ailes
#   - 1 chaine de production de roues
#   - 1 chaine de production de moteurs
#   - 1 chaine d'assemblage 1 x carlingue + 2 x ailes
#   - 1 chaine d'assemblage 1 x 1xcarlingue+2xailes + 3 x roues
#   - 1 chaine d'assemblage de l'aeroplane complet : 1xcarlingue+2xailes+3xroues+2xmoteurs
#

#
#   Proposez une synchronisation des chaines de montage à l'aide de tampons producteurs consommateurs
#

import multiprocessing
import threading
import time
import random
from tampon_fifo import Tampon_fifo, tampon_est_plein, tampon_est_vide
# A décommenter pour importer le tampon producteur consommateur protégé par moniteu
from producteur_consommateur_moniteur import MoniteurProdCons, moniteur_deposer, moniteur_retirer
# tprint = print
# A décommenter pour synchroniser l'affichage tprint()
from tprint import tprint



class UsineAeroplane :
    def __init__(self, tailleMaxTamponsChaines=2):
        # Les chaines d'assemblage
        self.chaines = ["aile", "roue", "carlingue", "moteur", "carlingue1Ailes2", "carlingue1Ailes2Roues3", "aeroplane"]
        self.tailleMaxTamponsChaines=tailleMaxTamponsChaines
        # A completer: definir les tampons PC (bornés à tailleMax) entre les chaines servant à stocker les productions de chaque chaine
        # ...
        self.tamponAile = MoniteurProdCons(Tampon_fifo(tailleMaxTamponsChaines))
        self.tamponRoue = MoniteurProdCons(Tampon_fifo(tailleMaxTamponsChaines))
        self.tamponCarlingue = MoniteurProdCons(Tampon_fifo(tailleMaxTamponsChaines))
        self.tamponMoteur = MoniteurProdCons(Tampon_fifo(tailleMaxTamponsChaines))
        self.tamponCarlingueAile = MoniteurProdCons(Tampon_fifo(tailleMaxTamponsChaines))
        self.tamponCarlingueAileRoue = MoniteurProdCons(Tampon_fifo(tailleMaxTamponsChaines))
        self.tamponAeroplne = MoniteurProdCons(Tampon_fifo(5))


nbAvionsPrevus=5
tailleMaxTamponsChaines=2
usine = UsineAeroplane(tailleMaxTamponsChaines)

def carlingue():
    for i in range(nbAvionsPrevus):
        tprint( 'Une carlingue est achevée ({})'.format(i+1))
        # A completer: deposer dans le tampon des carlingues
        moniteur_deposer(usine.tamponCarlingue,"carlingue","1")

def aile() :
    for i in range(nbAvionsPrevus*2):
        tprint( 'Une Aile est achevée ({})'.format(i+1))
        # A completer: deposer dans le tampon des ailes
        moniteur_deposer(usine.tamponAile,"aile","1")

def roue():
    for i in range(nbAvionsPrevus*3):
        tprint( 'Une roue est achevée ({})'.format(i+1))
        # A completer: deposer dans le tampon des roues
        moniteur_deposer(usine.tamponRoue,"roue","1")


def moteur():
    for i in range(nbAvionsPrevus*2):
        tprint( 'Un moteur est achevé ({})'.format(i+1))
        # A completer: deposer dans le tampon des moteurs
        moniteur_deposer(usine.tamponMoteur,"moteur","1")



def carlingue1Ailes2():
    for i in range(nbAvionsPrevus):
        # A completer: retirer 1 carlingue et 2 ailes des tampons
        moniteur_retirer(usine.tamponAile,"aile")
        moniteur_retirer(usine.tamponCarlingue,"carlingue")
        moniteur_retirer(usine.tamponAile,"aile")
        tprint( 'Un assemblage 1 carlingue avec 2 ailes est achevé ({})'.format(i+1))
        # A completer: deposer dans le tampon des carlingue1Ailes2
        moniteur_deposer(usine.tamponCarlingueAile,"carlingueAile","1")


def carlingue1Ailes2Roues3():
    for i in range(nbAvionsPrevus):
        # A completer: retirer 1 carlingue1Ailes2 et 3 roues des tampons
        moniteur_retirer(usine.tamponRoue,"roue")
        moniteur_retirer(usine.tamponRoue,"roue")
        moniteur_retirer(usine.tamponCarlingueAile,"carlingueAile")
        moniteur_retirer(usine.tamponRoue,"roue")
        tprint( 'Un assemblage 1 carlingue et 2 ailes avec 3 roue est achevé ({})'.format(i+1))
        # A completer: deposer dans le tampon des carlingue1Ailes2Roues3
        moniteur_deposer(usine.tamponCarlingueAileRoue, "carlingueAileRoue","1")

def aeroplane():
    for i in range(nbAvionsPrevus):
        moniteur_retirer(usine.tamponMoteur,"moteur")
        moniteur_retirer(usine.tamponCarlingueAileRoue,"carlingueAileRoue")
        moniteur_retirer(usine.tamponMoteur,"moteur")
        # A completer: retirer 1 carlingue1Ailes2Roues3 et 2 moteurs des tampons
        tprint( 'Un aeroplane est achevé ({})'.format(i+1))
        # A completer: deposer dans le tampon des aeroplane
        moniteur_deposer(usine.tamponAeroplne,"avion","1")

#
#  Test de l'usine d'assemblage d'aeroplanes
#

tprint('Demarrage de l\'usine')

# Création des Thread de chaine de production
threads=dict()
for chaine in [aile, roue, carlingue, moteur, carlingue1Ailes2, carlingue1Ailes2Roues3, aeroplane] :
    print( chaine.__name__ )
    threads[chaine]=threading.Thread(target=chaine)

# Démarrage des threads
for t in threads.values() :
    t.start()

# Attente de terminaison des threads
for t in threads.values() :
    t.join()

#tprint( 'Etat usine : ' + etat_usine ( usine ) )
tprint('Arret de l\'usine')
