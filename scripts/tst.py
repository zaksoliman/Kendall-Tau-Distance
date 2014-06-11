#! /usr/bin/env python3

from permutationGenerator import PermutationGenerator
import KendallTauDistance as kt
from medianFinder import MedianFinder

def getNextBatch(pg, batchSize, setSize, permSize):
    """ returns list of (batchSize) permutationSets of size (setSize)

    """

    permList = []

    for i in range(batchSize):

        permList.append(pg.generatePermutationsSet(setSize, permSize))

    return permList

if __name__ ==  '__main__':

    pg = PermutationGenerator()

    batch = getNextBatch(pg, 4,3,13)
    batch2 = getNextBatch(pg,4,3,13)

    for a,b in zip(batch,batch2):
        print(a)
        print(b)
