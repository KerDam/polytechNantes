import numpy as np
import matplotlib.pyplot as plt

voy = np.matrix('0.94 0.01 0.02; 0.02 0.96 0.05; 0.04 0.03 0.93')
cities = np.matrix('150000 ; 30000000; 210000')

angers = []
nantes = []
rennes = []

#populate the list with population each year
def populationAtN(n, voy, cities):
	for i in range(n):
		angers.append(cities.tolist()[0][0])
		nantes.append(cities.tolist()[1][0])
		rennes.append(cities.tolist()[2][0])
		cities = voy.dot(cities)

#output a list of the difference between the year (n+1) - n
def generationDifferenceList(l):
	ll = []
	for i in range(1, len(l)):
		ll.append(l[i] - l[i-1])
	return ll

			

#Ploting
populationAtN(100, voy, cities)
plt.subplot(211)
plt.plot(generationDifferenceList(angers), label="angers")
plt.plot(generationDifferenceList(nantes), label="nantes")
plt.plot(generationDifferenceList(rennes), label="rennes")
plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,
           ncol=2, mode="expand", borderaxespad=0.)

plt.subplot(212)
plt.plot(angers, label="angers")
plt.plot(nantes, label="nantes")
plt.plot(rennes, label="rennes")
plt.show()


