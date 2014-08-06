#!/usr/bin/env python3

from ast import literal_eval
from bisect import bisect
import sys

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
    return (P,Q)

def get_all_tableau(permSet):

    tableaux = dict()

    for p in permSet:

        tableau = RSK(p)
        tableaux[repr(p)] = tableau

    return tableaux

if __name__ == '__main__':

    permSet = sys.argv[1]
    permSet = literal_eval(permSet)
    tableaux = get_all_tableau(permSet)

    print('\n')

    for key in sorted(tableaux, key=tableaux.get, reverse = True):

        print(key,'-->', tableaux[key])
