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

    batchSize = 20
    tries_num = 5

    for permSize in range(10,14):
        for setSize in range(3,5):

            counter = 1

            while(counter <= tries_num):

                filePath = '../Results/scripts/RndPerm_' + str(setSize) + '_' + \
                str(permSize) + '(' + str(counter)  + ')'

                print('Writing to ' + filePath)

                out_csv = open(filePath + '.csv', 'w')
                out_txt = open(filePath + '.txt', 'w')

                out_csv.write('|A|,|M(A)|,|M(A)A|\n')

                batch = getNextBatch(pg,batchSize,setSize,permSize)

                print('Batch: ' + str(batch))

                for permList in batch:

                    print('Permutation Set: ' + str(permList))

                    mf = MedianFinder(permList)
                    mf.findMedian()

                    print('Solutions: ' + str(mf.solutions))
                    permSet = {tuple(elem) for elem in permList}
                    solSet = {tuple(elem) for elem in mf.solutions}

                    intersection = permSet.intersection(solSet)

                    out_txt.write(str(permSet) + '\n')
                    out_txt.write(str(mf.solutions) + 'Distance: '+
                            str(mf.dist_KT) + '\n')

                    out_txt.write(str(intersection) + '\n\n')

                    out_csv.write(str(len(permList)) + ',' +
                            str(len(mf.solutions)) + ',' +
                            str(len(intersection)) + '\n' )
                    counter += 1

        out_csv.close()
        out_txt.close()

