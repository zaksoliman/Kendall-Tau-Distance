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
            inversions = rnd.randrange(0,600)

            for indx in range(inversions):
                i = rnd.randrange(0,size-1)
                permutation[i], permutation[i+1] = permutation[i+1], permutation[i]

            inv = list(reversed(permutation))

            if (permutation not in permList) and (inv not in permList):
                permList.append(permutation)

        return permList

    def knuthGenerator(self,setSize, size):

        permSet = []

        while(len(permSet) != setSize):

            permutation = [x for x in range(1,size+1)]

            for i in range(0,size):
                j = rnd.randrange(i,size)
                permutation[j],permutation[i] = permutation[i],permutation[j]

            inv = list(reversed(permutation))

            if (permutation not in permSet) and (inv not in permSet):
                permSet.append(permutation)

        return permSet

    def getComplement(self, permSet):

        comp = []

        for p in permSet:
            comp.append(list(reversed(p)))

        return comp

if __name__ == '__main__':

    pg = PermutationGenerator()
    ps = pg.generatePermutationSet(5,11)

    print('Set size = ', len(ps))

    #for s in ps:
     #   print(s)
