#! /usr/bin/env python3
from math import factorial
import random as rnd

class PermutationGenerator:

    def __init__(self, seed):

        rnd.seed(seed)

    def generatePermutationSet(self, numOfPermutations, size):
        """ Generates a set of (num) permutations of size (size)

        """

        identity = list(range(1,size+1))

        #rnd.seed(20)


        permList = []

        while(len(permList) < numOfPermutations):
            permutation = identity[:]
            #Randomly choose a number of inversions
            inversions = rnd.randrange(0,50)

            for indx in range(inversions):
                i = rnd.randrange(0,size-1)
                permutation[i], permutation[i+1] = permutation[i+1], permutation[i]

            if permutation not in permList:
                permList.append(permutation)

        return permList

if __name__ == '__main__':

    pg = PermutationGenerator()
    ps = pg.generatePermutationSet(5,11)

    print('Set size = ', len(ps))

    for s in ps:
        print(s)
