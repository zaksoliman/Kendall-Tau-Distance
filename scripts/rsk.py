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
    """ returns a dictionary with permutations and their P tableau as keys
    """
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

def testAllSubsets(eqClasses, permutation_size):
    permSet_size_is_even = False
    counter = 1
    not_in_same_class = 0
    total_sets_inspected = 0
    total_odd_sets = 0
    total_even_sets = 0
    even_permSets_same_class = 0
    odd_permSets_diff_class = 0

    #total_sets_med_not_in_class = 0
    even_out = open('KnuthClasses_'+ str(permutation_size) +'_pair.txt', mode='w')
    odd_out = open('KnuthClasses_' + str(permutation_size) + '_impair.txt', mode='w')

    for key, permSet in eqClasses.items():
        print('Class: ' + str(counter) + '/' + str(len(eqClasses)))
        even_out.write('\n' + str(counter)+'. Class: ' + key + '\n\n')
        odd_out.write('\n' + str(counter)+'. Class: ' + key + '\n\n')
        powerSet = chain.from_iterable(combinations(permSet, r) for r in range(len(permSet)+1))

        for i, perms in enumerate(powerSet):

            #We want to inspect only sets of size >= 2
            if len(perms) >= 2:
                total_sets_inspected += 1
                mf = MedianFinder(perms)
                mf.findMedian()
                all_med_same_class = True

                if len(perms) % 2 == 0:
                    permSet_size_is_even = True
                    total_even_sets += 1
                else:
                    permSet_size_is_even = False
                    total_odd_sets += 1

                for med in mf.solutions:
                    tableau = RSK(med)

                    if key != str(tableau[0]):
                        #total_sets_med_not_in_class += 1
                        all_med_same_class = False

                        if not permSet_size_is_even:
                            odd_out.write('\n\tSet #'+str(i) +': ' + str(perms) + '\n')
                            odd_out.write('\tSet size:' + str(len(perms)) + '\n\n')

                            odd_out.write('**\n')
                            odd_out.write('\t\tMedian Set: ' + str(mf.solutions)+ '\n')
                            odd_out.write('\t\tSize of Median Set: ' + str(len(mf.solutions))+'\n')
                            odd_out.write('\t\tKendall Tau distance: ' + str(mf.dist_KT)+'\n')
                            odd_out.write('\t\tMedian: ' + str(med)+ '\n')
                            odd_out.write('\t\tTableau of median:' + str(tableau)+'\n\n')
                        elif permSet_size_is_even:
                            even_out.write('\n\tSet #'+str(i) +': ' + str(perms) + '\n')
                            even_out.write('\tSet size:' + str(len(perms)) + '\n\n')

                            even_out.write('**\n')
                            even_out.write('\t\tMedian Set: ' + str(mf.solutions)+ '\n')
                            even_out.write('\t\tSize of Median Set: ' + str(len(mf.solutions))+'\n')
                            even_out.write('\t\tKendall Tau distance: ' + str(mf.dist_KT)+'\n')
                            even_out.write('\t\tMedian: ' + str(med)+ '\n')
                            even_out.write('\t\tTableau of median:' + str(tableau)+'\n\n')

                if all_med_same_class and permSet_size_is_even:
                    even_out.write('\n\tSet #'+ str(i) +': ' + str(perms) + '\n')
                    even_out.write('\tSet size:' + str(len(perms)) + '\n\n')
                    even_out.write('\n')
                    even_out.write('\t\tMedian Set: ' + str(mf.solutions)+ '\n')
                    even_out.write('\t\tSize of Median Set: ' + str(len(mf.solutions))+'\n')
                    even_out.write('\t\tMedian Set in same class: True\n')
                    even_out.write('\t\tKendall Tau distance: ' + str(mf.dist_KT)+'\n')
                    even_permSets_same_class += 1

                elif all_med_same_class and not permSet_size_is_even:
                    odd_out.write('\n\tSet #'+ str(i) +': ' + str(perms) + '\n')
                    odd_out.write('\tSet size:' + str(len(perms)) + '\n\n')
                    odd_out.write('\n')
                    odd_out.write('\t\tMedian Set: ' + str(mf.solutions)+ '\n')
                    odd_out.write('\t\tSize of Median Set: ' + str(len(mf.solutions))+'\n')
                    odd_out.write('\t\tMedian Set in same class: True\n')
                    odd_out.write('\t\tKendall Tau distance: ' + str(mf.dist_KT)+'\n')

                elif not all_med_same_class and permSet_size_is_even:
                    even_out.write('\t\tMedian Set in same class: False\n')

                elif not all_med_same_class and not permSet_size_is_even:
                    odd_out.write('\t\tMedian Set in same class: False\n')
                    odd_permSets_diff_class += 1

                if not all_med_same_class:
                    not_in_same_class += 1

        counter += 1

    even_out.write('\n')
    even_out.write(str(even_permSets_same_class) + ' permutation sets of EVEN size that have a median set that is part of  it\'s Knuth class\n')
    even_out.write(str(odd_permSets_diff_class) + ' permutation sets of ODD size that have a median set that has at least one permutation not in it\'s knuth class \n')
    even_out.write(str(not_in_same_class) + ' permutation sets that have a median set that contains at least one permutation not in it\'s Knuth class \n')
    even_out.write(str(total_sets_inspected) + ' sets have been examined in total\n')
    even_out.write(str(total_even_sets) + ' Even permutaion sets\n')
    even_out.write(str(total_odd_sets) + ' Odd permutaion sets\n')
    even_out.close()

    odd_out.write('\n')
    odd_out.write(str(even_permSets_same_class) + ' permutation sets of EVEN size that have a median set that is part of  it\'s Knuth class\n')
    odd_out.write(str(odd_permSets_diff_class) + ' permutation sets of ODD size that have a median set that has at least one permutation not in it\'s knuth class \n')
    odd_out.write(str(not_in_same_class) + ' permutation sets that have a median set that contains at least one permutation not in it\'s Knuth class \n')
    odd_out.write(str(total_sets_inspected) + ' sets have been examined in total\n')
    odd_out.write(str(total_even_sets) + ' Even permutaion sets\n')
    odd_out.write(str(total_odd_sets) + ' Odd permutaion sets\n')
    odd_out.close()
    return

def RSK_inverse(p, q, output='array', insertion='RSK'):

    from bisect import bisect_left
    # Make a copy of p since this is destructive to it
    p_copy = [row[:] for row in p]

    rev_word = [] # This will be our word in reverse
    d = dict((qij,i) for i, Li in enumerate(q) for qij in Li)
    # d is now a dictionary which assigns to each integer k the
    # number of the row of q containing k.

    use_EG = (insertion == 'EG')

    for i in reversed(list(d.values())): # Delete last entry from i-th row of p_copy
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
    if output == 'array':
        return [list(range(1, len(rev_word)+1)), list(reversed(rev_word))]
    raise ValueError("Invalid output option")

    #upper_row = []
    #lower_row = []
    ##upper_row and lower_row will be the upper and lower rows of the
    ##generalized permutation we get as a result, but both reversed.
    #d = {}
    #for row, Li in enumerate(q):
    #    for col, val in enumerate(Li):
    #        if val in d:
    #            d[val][col] = row
    #        else:
    #            d[val] = {col: row}
    ##d is now a double family such that for every integers k and j,
    ##the value d[k][j] is the row i such that the (i, j)-th cell of
    ##q is filled with k.
    #for value, row_dict in reversed(d.items()):
    #    for i in reversed(row_dict.values()):
    #        x = p_copy[i].pop() # Always the right-most entry
    #        for row in reversed(p_copy[:i]):
    #            y = bisect_left(row,x) - 1
    #            x, row[y] = row[y], x
    #        upper_row.append(value)
    #        lower_row.append(x)

    #if output == 'matrix':
    #    return to_matrix(list(reversed(upper_row)), list(reversed(lower_row)))
    #if output == 'array':
    #    return [list(reversed(upper_row)), list(reversed(lower_row))]
    #if output in ['permutation', 'word']:
    #    raise TypeError("q must be standard to have a %s as valid output"%output)
    #raise ValueError("Invalid output option")

def build_standard_tableaux(shape):
    """ Input: shape as a tuple
        Output: Tuple of all standard"""

    #Start off by building the inital tableau
    num_of_rows = len(shape)
    permutation_size = sum(shape)
    initial_tableau = [list() for x in range(0,num_of_rows)]
    initial_tableau[0].append(1)
    #Holds the tableaux that are built
    tableaux = list()

    def recursive_build(tableau, tableauElems):
        """ tableau is a two dimentional array and tableauElements
            is a set containing all the integers in the tableau under construction"""

        #Base case: the tableau has the correct number of boxes (i.e. size of the permutation)
        if len(tableauElems) == 0: #permutation_size:
            if tableau not in tableaux:
                tableaux.append(list(tableau))
            return

        for row_idx, row in enumerate(tableau):
            #Get element that would be below the element we want to add
            try:
                elem_below = tableau[row_idx-1][len(row)]
            except IndexError:
                elem_below = None

            if row:
                prev_elem = row[-1]
            else:
                prev_elem = 0

            if row is tableau[0]:
                row_below = []
            else:
                row_below = tableau[row_idx-1]

            for new_elem in tableauElems: #range(1, permutation_size+1):
                #if new_elem in tableauElems:
                #    continue

                if row is tableau[0]:
                    if (len(row) < shape[row_idx]) and (prev_elem < new_elem):
                        tableau_copy = [list(row) for row in tableau]
                        tableau_copy[row_idx].append(new_elem)
                        #tableauElems.add(new_elem)
                        recursive_build(list(tableau_copy), set(tableauElems).difference({new_elem}))
                        #tableauElems.remove(new_elem)
                else:
                    if (len(row) < shape[row_idx]) and (elem_below is not None) and (elem_below < new_elem) and (prev_elem < new_elem):
                        tableau_copy = [list(row) for row in tableau]
                        tableau_copy[row_idx].append(new_elem)
                        #tableauElems.add(new_elem)
                        recursive_build(list(tableau_copy), set(tableauElems).difference({new_elem}))
                        #tableauElems.remove(new_elem)


    elements = set(range(2, permutation_size+1))

    recursive_build(initial_tableau, elements)

    return tableaux

def from_tableaux_get_permutations(eq_classes, tableaux_list):
   pass 
    

if __name__ == '__main__':

    permSet = sys.argv[1]
    permSet = literal_eval(permSet)
    tableaux = get_all_tableau(permSet)

    print('\n')

    for key in sorted(tableaux, key=tableaux.get, reverse = True):

        print(key,'-->', tableaux[key])
