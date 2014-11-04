#!/usr/bin/env python3

from ast import literal_eval
from bisect import bisect
from itertools import permutations, combinations, chain
from medianFinder import MedianFinder
import pprint
import rsk
import sys

def knuthClassesExperiment(permutationSize):
    #n := size of permutations
    n = permutationSize
    #Sn := permutation set of permutations of size n
    Sn = permutations(range(1,n+1))
    knuthClasses = rsk.getKnuthClasses(Sn)

    odd_closed_out = open('Resultats/impair/SAME_permutations_taille_'+ str(n) +'.txt', mode='w')
    odd_out = open('Resultats/impair/permutations_taille_'+ str(n) +'.txt', mode='w')

    even_closed_out = open('Resultats/pair/SAME_permutations_taille_'+ str(n) +'.txt', mode='w')
    even_out =  open('Resultats/pair/permutations_taille_'+ str(n) +'.txt', mode='w')

    stats = open('Resultats/statistiques_permutations_taille_'+ str(n) +'_.txt', mode='w')

    total_odd_sets = 0
    total_even_sets = 0
    total_even_sets_closed = 0
    total_odd_sets_closed = 0
    isEven = False

    for cl, permSet in knuthClasses.items():
        powerSet = chain.from_iterable(combinations(permSet, r) for r in
                range(len(permSet)+1))

        for s in powerSet:
            isClosed = False
            if len(s) < 2:
                continue

            if len(s) % 2 == 0:
                isEven = True
                total_even_sets += 1
            elif len(s) % 2 == 1:
                isEven = False
                total_odd_sets += 1

            mf = MedianFinder(s)
            mf.findMedian()
            medianSet = mf.solutions

            medianClasses = rsk.getKnuthClasses(medianSet)

            if len(medianClasses) == 1 and cl in medianClasses:
                isClosed = True

            if isEven and isClosed:
                total_even_sets_closed += 1
                even_closed_out.write("Ensemble de depart:\n"+ str(s) + '\n')
                even_closed_out.write("Tableau de l'ensemble de depart:\n"+ cl + '\n\n')
                even_closed_out.write("Tableau(x) de l'ensemble de medianes:\n"+ pprint.pformat(medianClasses)+ '\n')
                even_closed_out.write('\n')

            elif isEven and not isClosed:
                even_out.write("Ensemble de depart:\n"+ str(s) + '\n')
                even_out.write("Tableau de l'ensemble de depart:\n"+ cl + '\n\n')
                even_out.write("Tableau(x) de l'ensemble de medianes:\n"+ pprint.pformat(medianClasses)+ '\n')
                even_out.write('\n')

            elif not isEven and isClosed:
                total_odd_sets_closed += 1
                odd_closed_out.write("Ensemble de depart:\n"+ str(s) + '\n')
                odd_closed_out.write("Tableau de l'ensemble de depart:\n"+ cl + '\n\n')
                odd_closed_out.write("Tableau(x) de l'ensemble de medianes:\n"+ pprint.pformat(medianClasses)+ '\n')
                odd_closed_out.write('\n')

            elif not isEven and not isClosed:
                odd_out.write("Ensemble de depart:\n"+ str(s) + '\n')
                odd_out.write("Tableau de l'ensemble de depart:\n"+ cl + '\n\n')
                odd_out.write("Tableau(x) de l'ensemble de medianes:\n"+ pprint.pformat(medianClasses)+ '\n')
                odd_out.write('\n')

    stats.write("Nombre d'ensemble de taille pair: "+ str(total_even_sets) +'\n')
    stats.write("Dont "+ str(total_even_sets_closed) +' partagent le meme tableau P que leurs medianes\n')
    stats.write("\n\n")
    stats.write("Nombre d'ensemble de taille impair: "+ str(total_odd_sets)+'\n')
    stats.write("Dont "+ str(total_odd_sets_closed) +' partagent le meme tableau P que leurs medianes\n')

    odd_closed_out.close()
    odd_out.close()
    even_closed_out.close()
    even_out.close()
    stats.close()

if __name__ == '__main__':

    for i in range(4,7):
        print('Iteration:' + str(i))
        knuthClassesExperiment(i)
