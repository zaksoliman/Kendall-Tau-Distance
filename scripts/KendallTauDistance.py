#!/usr/bin/env python3
permutationSet = [[4, 1, 2, 3], [4, 1, 3, 2], [4, 2, 1, 3], [4, 2, 3, 1], [4, 3, 1, 2], [4, 3, 2, 1]]

medianSet = [[4, 1, 2, 3], [4, 1, 3, 2], [4, 2, 1, 3], [4, 2, 3, 1], [4, 3, 1, 2], [4, 3, 2, 1]]

def KendallTauDistance(permutation, permSet):
    distance = 0
    totalDist = 0
    permA = list(reversed(permutation))
    for p in permSet:
        permB = list(reversed(p))
        #compute the distance
        for i in range(0,len(p)-1):
            for j in range(i, len(permB)):
                if((permA[i]<permA[j])and(permB[i]>permB[j])):
                    distance+=1
                if((permA[i]>permA[j]) and (permB[i]<permB[j])):
                    distance+=1
        print("Distance between " + str(permutation) + " and "
                + str(p) + " is " + str(distance))
        totalDist += distance
        distance = 0
    return totalDist

for perm in medianSet:
    totalDist = KendallTauDistance(perm, permutationSet)
    print("D_KT = " + str(totalDist)+ "\n")
