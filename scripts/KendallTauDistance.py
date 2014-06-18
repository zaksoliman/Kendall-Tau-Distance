#!/usr/bin/env python3
from sys import argv
from ast import literal_eval

def inversePermutation(permutation):

    inverse = [x for x in range(len(permutation))]

    for i in range(0,len(permutation)):
        inverse[((permutation[i])-1)] = i

    return inverse

def printAllKendallTauDistance(permutation, permSet):

    distance = 0
    totalDist = 0
    permA = inversePermutation(permutation)

    for p in permSet:

        permB = inversePermutation(p)

        #compute the distance
        for i in range(0,len(p)-1):
            for j in range(i+1, len(permB)):

                if(permA[i] < permA[j] and permB[i] > permB[j]) or \
                  (permA[i] > permA[j] and permB[i] < permB[j]):

                    distance+=1

        print("Distance between " + str(permutation) + " and "
                + str(p) + " is " + str(distance))

        totalDist += distance
        distance = 0

    return totalDist

def slowKendallTauDistance(permutation, permSet):

    distance = 0
    totalDist = 0
    permA = inversePermutation(permutation)

    for p in permSet:

        permB = inversePermutation(p)

        #compute the distance
        for i in range(0,len(p)-1):
            for j in range(i+1, len(permB)):

                if(permA[i] < permA[j] and permB[i] > permB[j]) or \
                  (permA[i] > permA[j] and permB[i] < permB[j]):

                    distance+=1

        print("Distance between " + str(permutation) + " and "
                + str(p) + " is " + str(distance))

        totalDist += distance
        distance = 0

    return totalDist

def fastInversionCount(permutation):
    return _mergeSort(permutation)


def KendallTauDistance(permutation, permSet):
    """ O(nlogn) implementation to compute kendall tau distance

    """
    kt_dist = 0

    for p in permSet:

        dist = 0
        comp_perm = transfomPermutation(permutation, p)
        dist, arr = fastInversionCount(comp_perm)
        #print('Distance between: ', permutation, ' and ', p, ' is ' , dist)

        kt_dist += dist

    return kt_dist

def transfomPermutation(permA, permB):
    """Returns permA(inv_permB)

    """

    inv_permB = inversePermutation(permB)

    result = [x for x in range(len(permA))]

    for i in range(0,len(inv_permB)):
        result[i] = inv_permB[permA[i]-1]

    return result

def merge(y,z,result,mid):

    inv_count = 0
    i = 0
    j = 0

    while i < len(y) and j < len(z):
        if y[i] <= z[j]:
            result.append(y[i])
            i+=1
        else:
             result.append(z[j])
             inv_count = inv_count + (mid-i)
             j+=1

    result += y[i:]
    result += z[j:]

    return inv_count, result

def _mergeSort(arr):

    result = []
    inv_count = 0

    if len(arr) == 1:
        return inv_count, arr

    mid = int (len(arr)/2)
    inv_count, y = _mergeSort(arr[:mid])
    temp_count, z = _mergeSort(arr[mid:])

    inv_count += temp_count

    temp_count, result = merge(y,z,result,mid)

    inv_count += temp_count

    return inv_count, result


if __name__ == '__main__':

    from permutationGenerator import PermutationGenerator

    pg = PermutationGenerator(15)

    permutationSet = pg.generatePermutationSet(9, 7)
    #medianSet = argv[2]

    #permutationSet = literal_eval(permutationSet)
    #medianSet = literal_eval(medianSet)

    for perm in permutationSet:

        print('Slow KT distance algo: ')

        totalDistSlow = slowKendallTauDistance(perm, permutationSet)
        print("D_KT = " + str(totalDistSlow)+ "\n")

        print('Fast algo:')
        totalDistFast = KendallTauDistance(perm, permutationSet)
        print("D_KT = " + str(totalDistFast)+ "\n")
        #print(arr)

        def bubbleSort(arr):

            counter = 0
            for i in range(len(arr)-1):
                for j in range(i+1,len(arr)):

                    if arr[i] > arr[j]:
                        counter += 1

            return counter

        #print('Bubble sort', bubbleSort(permutationSet[0]))
        #count, arr = fastInversionCount(permutationSet[0])
        #print('MergeSort:', count)

