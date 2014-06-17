#! /usr/bin/env python3

from medianFinder import MedianFinder
from permutationGenerator import PermutationGenerator
from shutil import move
import KendallTauDistance as kt
import multiprocessing as mp
import os

def getNextBatch(pg, batchSize, setSize, permSize):
    """ returns list of (batchSize) permutationSets of size (setSize)

    """

    permList = []

    for i in range(batchSize):

        permList.append(pg.generatePermutationSet(setSize, permSize))

    return permList

def generateData(dist_path, batchSize, batch_num, min_set_size, max_set_size,
        min_perm_size, max_perm_size):

    pgA = PermutationGenerator(20)
    pgB = PermutationGenerator(10)
    #batchSize = 100
    #batch_num = 2
    #min_perm_size = 10
    #min_set_size = 3
    #max_perm_size = 11
    #max_set_size = 3

    done_path = os.path.join(dist_path, 'done')

    for permSize in range(min_perm_size, max_perm_size+1):
        for setSize in range(min_set_size, max_set_size+1):

            counter = 1

            while(counter <= batch_num):

                file_suffix =str(setSize) + '_' + str(permSize) + '_' + str(counter)

                filePath = os.path.join(dist_path, file_suffix)

                #print('Creating ' + filePath)

                out_csv = open(filePath + '.csv', 'w')
                out_txt = open(filePath + '.txt', 'w')

                out_csv.write("n,|M(A)|,D_ktA,|M(A)A|,|M(B)|,D_ktB,|M(B)B|,|M(AUB)|,|M(AUB)A|,|M(AUB)B|,|M(AUB)(M(A)UM(B))|\n")

                batchA = getNextBatch(pgA,batchSize,setSize,permSize)
                batchB = getNextBatch(pgB, batchSize, setSize, permSize)

                #print('Startin to compute medians in ' + filePath)
                for index, (permListA, permListB) in enumerate(zip(batchA, batchB)):

                    #print('Permutation Set: ' + str(permList))
                    #print(str(index+1)+"/"+str(len(batchA)))
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
                            str(mfA.dist_KT) + ',' + \
                            str(len(solInterPermA)) + ',' + \
                            str(len(mfB.solutions)) + ',' + \
                            str(mfB.dist_KT) + ',' + \
                            str(len(solInterPermB)) + ',' + \
                            str(len(mfUnion.solutions)) + ',' + \
                            str(len(permSetA.intersection(unionSolSet))) + ',' + \
                            str(len(permSetB.intersection(unionSolSet))) + ',' + \
                            str(len(unionSolSet.intersection(unionSolAB))) + \
                            '\n')
                counter += 1
                print(filePath + ' DONE!')
                file_name = os.path.basename(filePath)

                file_name_csv = file_name + '.csv'
                file_name_txt = file_name + '.txt'

                move(filePath + '.csv', os.path.join(done_path,file_name_csv))
                move(filePath + '.txt', os.path.join(done_path,file_name_txt))
                out_csv.close()
                out_txt.close()

if __name__ ==  '__main__':

    jobs = []
    dist_path = 'tst/'
    done_path = os.path.join(dist_path, 'done')
    if not os.path.exists(done_path):
        os.makedirs(done_path)

    for i in range(10,15):

        p = mp.Process(target=generateData, args=(dist_path,100, 1, 3, 10, i, i))
        jobs.append(p)
        p.start()

    for p in jobs:

        p.join()
