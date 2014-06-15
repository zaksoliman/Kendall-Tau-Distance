#! /usr/bin/env python3

from permutationGenerator import PermutationGenerator
import KendallTauDistance as kt
from medianFinder import MedianFinder

def getNextBatch(pg, batchSize, setSize, permSize):
    """ returns list of (batchSize) permutationSets of size (setSize)

    """

    permList = []

    for i in range(batchSize):

        permList.append(pg.generatePermutationSet(setSize, permSize))

    return permList

if __name__ ==  '__main__':

    pgA = PermutationGenerator(20)
    pgB = PermutationGenerator(10)

    batchSize = 50
    tries_num = 5

    for permSize in range(10,15):
        for setSize in range(3,5):

            counter = 1

            while(counter <= tries_num):

                filePath = '../Results/scripts/RndPerm_' + str(setSize) + '_' + \
                str(permSize) + '_' + str(counter)

                print('Writing to ' + filePath)

                out_csv = open(filePath + '.csv', 'w')
                out_txt = open(filePath + '.txt', 'w')

                out_csv.write("n,|M(A)|,|M(A)A|,|M(B)|,|M(B)B|,|M(AUB)|,|M(AUB)A|,|M(AUB)B|,|M(AUB)(M(A)UM(B))|\n")

                batchA = getNextBatch(pgA,batchSize,setSize,permSize)
                batchB = getNextBatch(pgB, batchSize, setSize, permSize)

                #print('Batch: ' + str(batch))

                for index, (permListA, permListB) in enumerate(zip(batchA, batchB)):

                    #print('Permutation Set: ' + str(permList))

                    mfA = MedianFinder(permListA)
                    mfB = MedianFinder(permListB)
                    mfA.findMedian()
                    mfB.findMedian()

                    #print('Solutions: ' + str(mf.solutions))
                    permSetA = {tuple(elem) for elem in permListA}
                    solSetA = {tuple(elem) for elem in mfA.solutions}

                    permSetB = {tuple(elem) for elem in permListB}
                    solSetB = {tuple(elem) for elem in mfB.solutions}

                    solInterPermA = permSetA.intersection(solSetA)
                    solInterPermB = permSetB.intersection(solSetB)

                    AuB = permSetA.union(permSetB)
                    mfUnion = MedianFinder([list(elem) for elem in AuB])
                    mfUnion.findMedian()

                    unionSet = {tuple(elem) for elem in AuB}
                    unionSolSet = {tuple(elem) for elem in mfUnion.solutions}

                    unionSolAB = solSetA.union(solSetB)


                    out_txt.write('('+str(index)+')\n')
                    out_txt.write('A = \n' + str(permSetA) + '\n')
                    out_txt.write('M(A) = \n' + str(mfA.solutions) + '\n'+ 'Distance: '+
                            str(mfA.dist_KT) + '\n')

                    out_txt.write('B = \n' + str(permSetB) + '\n')
                    out_txt.write('M(B) = \n' + str(mfB.solutions) + '\n'+ 'Distance: '+
                            str(mfB.dist_KT) + '\n')

                    out_txt.write('M(A) inter A:\n')
                    out_txt.write(str(solInterPermA) + '\n')
                    out_txt.write('M(B) inter B:\n')
                    out_txt.write(str(solInterPermB) + '\n\n')

                    out_txt.write('AUB = \n' + str(AuB) + '\n')
                    out_txt.write('M(AUB) = \n' + str(mfUnion.solutions) + '\n')
                    out_txt.write('Distance:' + str(mfUnion.dist_KT)+ '\n\n')

                    out_csv.write(str(permSize) + ',' + \
                            str(len(mfA.solutions)) + ',' + \
                            str(len(solInterPermA)) + ',' + \
                            str(len(mfB.solutions)) + ',' + \
                            str(len(solInterPermB)) + ',' + \
                            str(len(mfUnion.solutions)) + ',' + \
                            str(len(permSetA.intersection(unionSolSet))) + ',' + \
                            str(len(permSetB.intersection(unionSolSet))) + ',' + \
                            str(len(unionSolSet.intersection(unionSolAB))) + ',' + \
                            '\n')
                counter += 1

                out_csv.close()
                out_txt.close()

