#! /usr/bin/env python3

from permutationGenerator import PermutationGenerator
from medianFinder import MedianFinder
import os
from shutil import move

def generate_data_with_union(dist_path, batchSize, set_sizeA, set_sizeB,
        min_perm_size, max_perm_size):

    pgA = PermutationGenerator(20)
    pgB = PermutationGenerator(20)
    done_path = os.path.join(dist_path, 'done')

    for permSize in range(min_perm_size, max_perm_size+1):

        file_suffix = str(set_sizeA) + '_' + str(set_sizeB) + '_' + str(permSize)
        filePath = os.path.join(dist_path, file_suffix)
        print('Creating ' + filePath)
        out_csv = open(filePath + '.csv', 'w')
        out_txt = open(filePath + '.txt', 'w')
        out_csv.write("n,|A|,|B|,|AB|,|M(A)|,D_ktA,|M(A)A|,|M(B)|,D_ktB,|M(B)B|,|M(AUB)|,D_ktaub,|M(AUB)A|,|M(AUB)B|,|M(AUB)(M(A)UM(B))|\n")

        #GENERATE BATCHES
        batchA = getNextBatch(pgA, batchSize, set_sizeA, permSize)
        batchB = getNextBatch(pgB, batchSize, set_sizeB, permSize)

        print('Starting to compute medians in ' + filePath)
        for index, (permListA, permListB) in enumerate(zip(batchA, batchB)):

            print('Progress of ' + filePath + ' ' + str(index+1)  + '/' + str(len(batchA)) )
            mfA = MedianFinder(permListA)
            mfB = MedianFinder(permListB)
            mfA.findMedian()
            mfB.findMedian()

            #BUILD SETS
            permSetA = {tuple(elem) for elem in permListA}
            solSetA = {tuple(elem) for elem in mfA.solutions}
            permSetB = {tuple(elem) for elem in permListB}
            ABset = permSetA.intersection(permSetB)
            solSetB = {tuple(elem) for elem in mfB.solutions}
            solInterPermA = permSetA.intersection(solSetA)
            solInterPermB = permSetB.intersection(solSetB)
            AuB = permSetA.union(permSetB)
            mfUnion = MedianFinder([list(elem) for elem in AuB])
            mfUnion.findMedian()
            unionSet = {tuple(elem) for elem in AuB}
            unionSolSet = {tuple(elem) for elem in mfUnion.solutions}
            unionSolAB = solSetA.union(solSetB)

            #OUTPUT
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
            out_txt.write('AB = \n' + str(ABset) + '\n')
            out_txt.write('AUB = \n' + str(AuB) + '\n')
            out_txt.write('M(AUB) = \n' + str(mfUnion.solutions) + '\n')
            out_txt.write('Distance:' + str(mfUnion.dist_KT)+ '\n\n')
            out_csv.write(str(permSize) + ',' + \
                    str(set_sizeA) + ',' + \
                    str(set_sizeB) + ',' + \
                    str(len(ABset)) + ',' + \
                    str(len(mfA.solutions)) + ',' + \
                    str(mfA.dist_KT) + ',' + \
                    str(len(solInterPermA)) + ',' + \
                    str(len(mfB.solutions)) + ',' + \
                    str(mfB.dist_KT) + ',' + \
                    str(len(solInterPermB)) + ',' + \
                    str(len(mfUnion.solutions)) + ',' + \
                    str(mfUnion.dist_KT) + ',' + \
                    str(len(permSetA.intersection(unionSolSet))) + ',' + \
                    str(len(permSetB.intersection(unionSolSet))) + ',' + \
                    str(len(unionSolSet.intersection(unionSolAB))) + \
                    '\n')

        print(filePath + ' DONE!')
        file_name = os.path.basename(filePath)
        file_name_csv = file_name + '.csv'
        file_name_txt = file_name + '.txt'
        move(filePath + '.csv', os.path.join(done_path,file_name_csv))
        move(filePath + '.txt', os.path.join(done_path,file_name_txt))
        out_csv.close()
        out_txt.close()

def getNextBatch(pg, batchSize, setSize, permSize):
    """ returns list of (batchSize) permutationSets of size (setSize)

    """

    permList = []

    for i in range(batchSize):

        permList.append(pg.generatePermutationSet(setSize, permSize))

    return permList

if __name__ == '__main__':

    generate_data_with_union('res', 100, 3, 2, 4, 6)
