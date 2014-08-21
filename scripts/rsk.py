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
                #fout.write('\tSet #'+str(i) +': ' + str(perms) + '\n')
                #fout.write('\tSet size:' + str(len(perms)) + '\n\n')
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

                        if not permSet_size_is_even:
                            fout.write('\tSet #'+str(i) +': ' + str(perms) + '\n')
                            fout.write('\tSet size:' + str(len(perms)) + '\n\n')

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

def RSK_inverse(p, q, output='array', insertion='RSK'):

    if p.shape() != q.shape():
        raise ValueError("p(=%s) and q(=%s) must have the same shape"%(p, q))
    from sage.combinat.tableau import SemistandardTableaux
    if p not in SemistandardTableaux():
        raise ValueError("p(=%s) must be a semistandard tableau"%p)

    from bisect import bisect_left
    # Make a copy of p since this is destructive to it
    p_copy = [row[:] for row in p]

    if q.is_standard():
        rev_word = [] # This will be our word in reverse
        d = dict((qij,i) for i, Li in enumerate(q) for qij in Li)
        # d is now a dictionary which assigns to each integer k the
        # number of the row of q containing k.

        use_EG = (insertion == 'EG')

        for i in reversed(d.values()): # Delete last entry from i-th row of p_copy
            x = p_copy[i].pop() # Always the right-most entry
            for row in reversed(p_copy[:i]):
                y_pos = bisect_left(row,x) - 1
                if use_EG and row[y_pos] == x - 1 and y_pos < len(row)-1 and row[y_pos+1] == x:
                    # Nothing to do except decrement x by 1.
                    # (Case 1 on p. 74 of Edelman-Greene [EG1987]_.)
                    x -= 1
                else:
                    # switch x and y
                    x, row[y_pos] = row[y_pos], x
            rev_word.append(x)

        if use_EG:
            return list(reversed(rev_word))
        if output == 'word':
            from sage.combinat.words.word import Word
            return Word(reversed(rev_word))
        if output == 'matrix':
            return to_matrix(list(range(1, len(rev_word)+1)), list(reversed(rev_word)))
        if output == 'array':
            return [list(range(1, len(rev_word)+1)), list(reversed(rev_word))]
        if output == 'permutation':
            if not p.is_standard():
                raise TypeError("p must be standard to have a valid permutation as output")
            from sage.combinat.permutation import Permutation
            return Permutation(reversed(rev_word))
        raise ValueError("Invalid output option")

    # Checks
    if insertion != 'RSK':
        raise NotImplementedError("Only RSK is implemented for non-standard Q")
    if q not in SemistandardTableaux():
        raise ValueError("q(=%s) must be a semistandard tableau"%q)

    upper_row = []
    lower_row = []
    #upper_row and lower_row will be the upper and lower rows of the
    #generalized permutation we get as a result, but both reversed.
    d = {}
    for row, Li in enumerate(q):
        for col, val in enumerate(Li):
            if val in d:
                d[val][col] = row
            else:
                d[val] = {col: row}
    #d is now a double family such that for every integers k and j,
    #the value d[k][j] is the row i such that the (i, j)-th cell of
    #q is filled with k.
    for value, row_dict in reversed(d.items()):
        for i in reversed(row_dict.values()):
            x = p_copy[i].pop() # Always the right-most entry
            for row in reversed(p_copy[:i]):
                y = bisect_left(row,x) - 1
                x, row[y] = row[y], x
            upper_row.append(value)
            lower_row.append(x)

    if output == 'matrix':
        return to_matrix(list(reversed(upper_row)), list(reversed(lower_row)))
    if output == 'array':
        return [list(reversed(upper_row)), list(reversed(lower_row))]
    if output in ['permutation', 'word']:
        raise TypeError("q must be standard to have a %s as valid output"%output)
    raise ValueError("Invalid output option")


if __name__ == '__main__':

    permSet = sys.argv[1]
    permSet = literal_eval(permSet)
    tableaux = get_all_tableau(permSet)

    print('\n')

    for key in sorted(tableaux, key=tableaux.get, reverse = True):

        print(key,'-->', tableaux[key])
