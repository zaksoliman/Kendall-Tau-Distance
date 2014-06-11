#! /usr/bin/env python3
from math import factorial
import random as rnd

class PermutationGenerator:

    def __init__(self):

        rnd.seed(20)

    def generatePermutationsSet(self, numOfPermutations, size):
        """ Generates a set of (num) permutations of size (size)

        """

        permutation = list(range(1,size+1))

        #rnd.seed(20)

        #Randomly choose a number of inversions
        inversions = rnd.randrange(0,50)

        permSet = set()

        while(len(permSet) < numOfPermutations):

            for indx in range(inversions):
                i = rnd.randrange(0,size-1)
                permutation[i], permutation[i+1] = permutation[i+1], permutation[i]

            permSet.add(tuple(permutation))

        return [list(elem) for elem in permSet]

if __name__ == '__main__':

    ps = generatePermutationsSet(50, 13)

    print('Set size = ', len(ps))

    for s in ps:
        print(s)
