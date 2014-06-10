#!/usr/bin/env python3
from sys import argv
from ast import literal_eval 

def inversePermutation(permutation):

    inverse = [x for x in range(len(permutation))]

    for i in range(0,len(permutation)):
        inverse[((permutation[i])-1)] = i

    return inverse

def KendallTauDistance(permutation, permSet):

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

if __name__ == '__main__':

    permutationSet = argv[1]
    medianSet = argv[2]

    permutationSet = literal_eval(permutationSet)
    medianSet = literal_eval(medianSet)

    for perm in medianSet:
       totalDist = KendallTauDistance(perm, permutationSet)
       print("D_KT = " + str(totalDist)+ "\n")
