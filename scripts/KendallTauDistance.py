#!/usr/bin/env python3
permutationSet=[[1,6,7,3,13,9,10,8,5,12,4,11,2],[2,11,1,7,10,5,3,6,12,13,4,8,9],[12,2,10,4,8,11,3,7,5,1,6,13,9]]
medianSet =[[10, 11, 1, 7, 2, 5, 3, 6, 12, 13, 4, 8, 9], [3, 11, 1, 7, 10, 5, 2, 6, 12, 13, 4, 8, 9], [12, 11, 1, 7, 10, 5, 3, 6, 2, 13, 4, 8, 9], [8, 11, 1, 7, 10, 5, 3, 6, 12, 13, 4, 2, 9], [2, 10, 1, 7, 11, 5, 3, 6, 12, 13, 4, 8, 9], [2, 3, 1, 7, 10, 5, 11, 6, 12, 13, 4, 8, 9], [2, 12, 1, 7, 10, 5, 3, 6, 11, 13, 4, 8, 9], [2, 4, 1, 7, 10, 5, 3, 6, 12, 13, 11, 8, 9], [2, 8, 1, 7, 10, 5, 3, 6, 12, 13, 4, 11, 9], [2, 11, 3, 7, 10, 5, 1, 6, 12, 13, 4, 8, 9], [2, 11, 1, 7, 10, 3, 5, 6, 12, 13, 4, 8, 9], [2, 11, 1, 7, 10, 6, 3, 5, 12, 13, 4, 8, 9], [2, 11, 1, 7, 10, 12, 3, 6, 5, 13, 4, 8, 9], [2, 11, 1, 7, 10, 8, 3, 6, 12, 13, 4, 5, 9]]

def inversePermutation(permutation):
    inverse = list(range(len(permutation)))

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
                if(((permA[i]<permA[j])and(permB[i]>permB[j])) or
                ((permA[i]>permA[j]) and (permB[i]<permB[j]))):
                    distance+=1
        print("Distance between " + str(permutation) + " and "
                + str(p) + " is " + str(distance))
        totalDist += distance
        distance = 0
    return totalDist

for perm in medianSet:
    totalDist = KendallTauDistance(perm, permutationSet)
    print("D_KT = " + str(totalDist)+ "\n")
