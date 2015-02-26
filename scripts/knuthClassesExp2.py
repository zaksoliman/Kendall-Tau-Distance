#!/usr/bin/env python3

from ast import literal_eval
from bisect import bisect
from itertools import permutations, combinations, chain
from medianFinder import MedianFinder
import pprint
import rsk
import sys
import copy

def print_tableau(tableau, out_file):

    if type(tableau) is str:
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

def print_to_file(out_file, pferm_set, initial_class, median_classes):

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

    freturn

def second_print(out_file, perm_set, median_set, closed_class, contains_median):

    out_file.write("A = " + str(perm_set) + "\n")
    out_file.write("M(A) = " + str(median_set) + "\n")
    out_file.write("Ferme sur la classe de knuth: " + str(closed_class) +"\n")
    out_file.write("A contient une mediane: " + str(contains_median) +"\n\n")

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

def adding_blocks_to_tableaux_exp(starting_tableau, tableau, perm_set, out_file):
    #out_file = open('Resultats/impair/experience_sur_tableau'+ '.txt', mode='w')

    powerSet = chain.from_iterable(combinations(perm_set, r) for r in
                range(len(perm_set)+1))

    out_file.write("---------------------------------------------------------------------------\n")
    out_file.write("Tableau de depart:"+"\n")
    print_tableau(starting_tableau, out_file)
    out_file.write("Nouveau Tableau: " + "\n")
    print_tableau(tableau, out_file)
    all_medians = set()

    for s in powerSet:
        if len(s) <= 2 : continue

        mf = MedianFinder(s)
        mf.findMedian()
        median_set = mf.solutions

        closed_class = True
        contains_median = False

        for median in median_set:

            all_medians.add(str(median))
            median_tableau = rsk.RSK(median)

            if median in s:
                contains_median = True

            if median_tableau[0] != tableau:
                closed_class = False

        second_print(out_file, s, median_set, closed_class, contains_median)
        #print("Ensemble de depart: " + str(s))
        #print("Medianes: " + str(medianSet))
        #print('\n')

    out_file.write("Medianes: " + str(all_medians) +"\n\n")

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

def build_next_tableaux(small_tableau, new_block):

    tableaux = list()

    for row_idx, row in enumerate(small_tableau):

        row_below = small_tableau[row_idx-1]

        # we always can add a new block on the first row (of course the new
        # block needs to be an integer greater than the greatest integer in the
        # small_tableau)
        if row == small_tableau[0]:
            row.append(new_block)
            tableaux.append(copy.deepcopy(small_tableau))
            row.pop()
        # To add new block we need to check if the last element of the row
        # below is less than the new block
        elif (row[-1] < new_block) and (len(row_below) > len(row)) and (row_below[-1] < new_block):
            row.append(new_block)
            tableaux.append(copy.deepcopy(small_tableau))
            row.pop()

    # Finally we check if we can add the new block on top of the tableau
    top_row = small_tableau[-1]
    if top_row[0] < new_block:
        small_tableau.append([new_block])
        tableaux.append(copy.deepcopy(small_tableau))
        small_tableau.pop()

    return tableaux

def from_tableaux_get_permutations(eq_classes, tableaux_list):
       pass

def permutation_classes(size):
    """ returns a dictionary with permutations of size 'size' and their P tableau
        as keys """
    Sn = permutations(range(1,size+1))
    knuthClasses = rsk.getKnuthClasses(Sn)

    return knuthClasses

if __name__ == '__main__':

    tab_4 = permutation_classes(4)
    tab_4 = [literal_eval(t) for t in tab_4.keys()]
    out_file = open('Resultats/exp_tableaux'+'.txt', mode='w')

    for small_tableau in tab_4:
        tab_5 = build_next_tableaux(small_tableau, 5)

        for next_tableau in tab_5:
            perm_set = get_permutations_from_tableau(next_tableau)
            adding_blocks_to_tableaux_exp(small_tableau, next_tableau, perm_set, out_file)

    out_file.close()

    #for i in range(4,7):
    #    print('Iteration:' + str(i))
    #    knuthClassesExperiment(i)
