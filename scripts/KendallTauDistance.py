#!/usr/bin/env python3
permutationSet=[[1,10,7,9,12,8,6,2,11,4,13,3,5],[4,7,10,5,11,6,9,3,8,12,13,2,1],[6,9,3,11,12,10,4,2,8,1,13,7,5],[8,4,9,12,1,5,2,3,7,6,13,11,10],[8,4,10,6,13,7,9,1,3,2,12,5,11],[8,7,10,12,11,9,2,3,4,1,13,6,5],[10,9,1,2,8,7,5,13,11,4,6,12,3],[12,1,5,9,8,10,13,3,11,2,4,6,7],[13,2,7,8,4,5,9,6,3,1,12,11,10]] 
medianSet =[[1,10,7,9,12,8,6,2,11,4,13,3,5],[4,7,10,5,11,6,9,3,8,12,13,2,1],[6,9,3,11,12,10,4,2,8,1,13,7,5],[8,4,9,12,1,5,2,3,7,6,13,11,10],[8,4,10,6,13,7,9,1,3,2,12,5,11],[8,7,10,12,11,9,2,3,4,1,13,6,5],[10,9,1,2,8,7,5,13,11,4,6,12,3],[12,1,5,9,8,10,13,3,11,2,4,6,7],[13,2,7,8,4,5,9,6,3,1,12,11,10]]
#TODO: Fix the algo
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
