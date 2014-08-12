#!/usr/bin/env python3

from ast import literal_eval
from bisect import bisect
import sys
from itertools import permutations, combinations, chain
from medianFinder import MedianFinder

def RSK(p):
    '''Given a permutation p, spit out a pair of Young tableaux'''
    P = []; Q = []
    def insert(m, n=0):
        '''Insert m into P, then place n in Q at the same place'''
        for r in range(len(P)):
            if m > P[r][-1]:
                P[r].append(m); Q[r].append(n)
                return
            c = bisect(P[r], m)
            P[r][c],m = m,P[r][c]
        P.append([m])
        Q.append([n])

    for i in range(len(p)):
        insert(int(p[i]), i+1)

    #P = tuple(tuple(elem) for elem in P)
    #Q = tuple(tuple(elem) for elem in Q)
    return (P,Q)

def getKnuthClasses(permSet):
    classes = dict()
    for p in permSet:
        tableau = RSK(p)
        tableau = str(tableau[0])
        if tableau not in classes:
            classes[tableau] = list()
            classes[tableau].append(p)
        else:
            classes[tableau].append(p)
    return classes

def testAllSubsets(eqClasses):
    permSet_size_is_even = False
    counter = 1
    not_in_same_class = 0
    total_sets_inspected = 0
    total_odd_sets = 0
    total_even_sets = 0
    even_permSets_same_class = 0
    odd_permSets_diff_class = 0

    #total_sets_med_not_in_class = 0
    fout = open('KnuthClasses_6.txt', mode='w')
    for key, permSet in eqClasses.items():
        print('Class: ' + str(counter) + '/' + str(len(eqClasses)))
        fout.write(str(counter)+'. Class: ' + key + '\n\n')
        powerSet = chain.from_iterable(combinations(permSet, r) for r in range(len(permSet)+1))

        for i, perms in enumerate(powerSet):
            if len(perms) >= 2:
                total_sets_inspected += 1
                mf = MedianFinder(perms)
                mf.findMedian()
                fout.write('\tSet #'+str(i) +': ' + str(perms) + '\n')
                fout.write('\tSet size:' + str(len(perms)) + '\n\n')
                all_med_same_class = True

                if len(perms) % 2 == 0:
                    permSet_size_is_even = True
                    total_even_sets += 1
                else:
                    permSet_size_is_even = False
                    total_odd_sets += 1

                for med in mf.solutions:
                    tableau = RSK(med)
                    if key == str(tableau[0]):
                        pass
                    else:
                        #total_sets_med_not_in_class += 1
                        all_med_same_class = False
                        fout.write('**\n')
                        fout.write('\tMedian Set: ' + str(mf.solutions)+ '\n')
                        fout.write('\tSize of Median Set: ' + str(len(mf.solutions))+'\n')
                        fout.write('\tKendall Tau distance: ' + str(mf.dist_KT)+'\n')
                        fout.write('\tMedian: ' + str(med)+ '\n')
                        fout.write('\tTableau of median:' + str(tableau)+'\n\n')

                if all_med_same_class and permSet_size_is_even:
                    even_permSets_same_class += 1
                if not permSet_size_is_even and not all_med_same_class:
                    odd_permSets_diff_class += 1
                if not all_med_same_class:
                    not_in_same_class += 1
        counter += 1

    fout.write(str(even_permSets_same_class) + ' permutation sets of EVEN size that have a median set that is part of  it\'s Knuth class\n')
    fout.write(str(odd_permSets_diff_class) + ' permutation sets of ODD size that have a median set that has at least one permutation not in it\'s knuth class \n')
    fout.write(str(not_in_same_class) + ' permutation sets that have a median set that contains at least one permutation not in it\'s Knuth class \n')
    fout.write(str(total_sets_inspected) + ' sets have been examined in total\n')
    fout.write(str(total_even_sets) + ' Even permutaion sets\n')
    fout.write(str(total_odd_sets) + ' Odd permutaion sets\n')
    #fout.write(str(total_sets_med_not_in_class) + ' Sets for which the median set is not in the same class\n')
    fout.close()
    return

if __name__ == '__main__':

    permSet = sys.argv[1]
    permSet = literal_eval(permSet)
    tableaux = get_all_tableau(permSet)

    print('\n')

    for key in sorted(tableaux, key=tableaux.get, reverse = True):

        print(key,'-->', tableaux[key])
