#!/usr/bin/env python3

from ast import literal_eval
from bisect import bisect
from itertools import permutations, combinations, chain
from medianFinder import MedianFinder
import pprint
import rsk
import sys

def print_tableau(tableau, out_file):

    tableau = literal_eval(tableau)
    for row in reversed(tableau):
        out_file.write(str(row) + "\n")

    out_file.write("\n")

    return

def class_count(initial_tableau, medianClasses):

    same_class = 0
    different_class = 0
    initial_tableau = str(initial_tableau)

    for tableau, permutation_set in medianClasses.items():
        if tableau == initial_tableau:
            same_class += len(permutation_set)
        else:
            different_class += len(permutation_set)

    return (same_class, different_class)

def print_to_file(out_file, perm_set, initial_class, median_classes):

    same_class, different_class = class_count(initial_class, median_classes)

    out_file.write("---------------------------------------------------------------------------\n")
    out_file.write("Ensemble de depart:\n"+ str(perm_set) + '\n')
    out_file.write("Tableau de l'ensemble de depart:\n"+ initial_class + '\n\n')

    if different_class == 0:
        out_file.write("Toutes les medianes ont le meme tableau\n")
    else:
        out_file.write(str(same_class) + " medianes ont le meme tableau\n")
        out_file.write(str(different_class) + " medianes ont un tableau different\n")


    out_file.write("Tableau(x) de l'ensemble de medianes:\n")

    for tableau, med_set in median_classes.items():
        out_file.write("Tableau:\n")
        print_tableau(tableau, out_file)
        out_file.write("Ensemble mediane:\n")
        out_file.write(str(med_set)+"\n\n")

    out_file.write('\n')

    return

def knuthClassesExperimentf(permutationSize):

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

    working_classes = open('Resultats/classes/SAME_classes_permutations_taille_'+ str(n) +'.txt', mode='w')
    not_same = open('Resultats/classes/classes_permutations_taille_'+ str(n) +'.txt', mode='w')

    total_odd_sets = 0
    total_even_sets = 0
    total_even_sets_closed = 0
    total_odd_sets_closed = 0
    isEven = False

    working_cases = list()
    bad_cases = list()

    for cl, permSet in knuthClasses.items():
        powerSet = chain.from_iterable(combinations(permSet, r) for r in
                range(len(permSet)+1))

        isEntireClassClosed = True

        for s in powerSet:
            isClosed = False
            if len(s) <= 2:
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
                print_to_file(even_closed_out, s, cl, medianClasses)

            elif isEven and not isClosed:
                isEntireClassClosed = False
                print_to_file(even_out, s, cl, medianClasses)

            elif not isEven and isClosed:
                total_odd_sets_closed += 1
                print_to_file(odd_closed_out, s, cl, medianClasses)

            elif not isEven and not isClosed:
                isEntireClassClosed = False
                print_to_file(odd_out, s, cl, medianClasses)

            if isClosed:
                working_cases.append(list(s))
            elif not isClosed:
                bad_cases.append(list(s))

        if isEntireClassClosed:
            print_tableau(cl,working_classes)

        elif not isEntireClassClosed:
            print_tableau(cl, not_same)

            if working_cases:
                not_same.write("Mais ca marche pour:\n")
                not_same.write(str(len(working_cases))+ " Ensembles de permutations")
                not_same.write("\n\n")

        del working_cases[:]
        del bad_cases[:]

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
    not_same.close()
    working_classes.close()

def adding_blocks_to_tableaux_exp(permSet):

    powerSet = chain.from_iterable(combinations(permSet, r) for r in
                range(len(permSet)+1))

    for s in powerSet:
        if len(s) <= 2 : continue

        mf = MedianFinder(s)
        mf.findMedian()
        medianSet = mf.solutions

        print("Ensemble de depart: " + str(s))
        print("Medianes: " + str(medianSet))
        print('\n')

def get_tableau_shape(tableau):

    shape = list()
    for row in tableau:
        shape.append(len(row))
    return shape

def get_permutations_from_tableau(tableau):

    shape = get_tableau_shape(tableau)
    tableaux = rsk.build_standard_tableaux(shape)

    permSet = list()

    for t in tableaux:

        p = rsk.RSK_inverse(tableau, t)
        permSet.append(p[1])

    return permSet

if __name__ == '__main__':

    for i in range(4,7):
        print('Iteration:' + str(i))
        knuthClassesExperiment(i)
