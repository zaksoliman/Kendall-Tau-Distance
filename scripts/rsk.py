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

def getEquivalenceClasses(permSet):

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

#TODO: find a better name
def getAllMedianSet(eqClasses):

    for key, permSet in eqClasses.items():

        #print('Class '+key + ':\n')
        powerSet = chain.from_iterable(combinations(permSet, r) for r in range(len(permSet)+1))

        for perms in powerSet:
            if len(perms) >= 3:
                mf = MedianFinder(perms)
                mf.findMedian()
                #print('Set: ' + str(perms))
                #print('Medians: ' + str(mf.solutions))
                #print('RSK Tableau for each median:')

                for med in mf.solutions:
                    tableau = RSK(med)
                    #print(str(med)+'\t'+str(tableau)+'\n')
                    if key == str(tableau[0]):
                        print('SAME CLASS')
                    else:
                        print('***DIFFERENT CLASS***')
                        print('Class: ' + key)
                        print('Set: ' + str(perms))
                        print('Medians: ' + str(mf.solutions))
                        print(str(tableau))
    return

if __name__ == '__main__':

    permSet = sys.argv[1]
    permSet = literal_eval(permSet)
    tableaux = get_all_tableau(permSet)

    print('\n')

    for key in sorted(tableaux, key=tableaux.get, reverse = True):

        print(key,'-->', tableaux[key])
