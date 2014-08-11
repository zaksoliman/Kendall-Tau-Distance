import medianFinder as mf1
import medianFinder2 as mf2
from permutationGenerator import PermutationGenerator
import copy

pg = PermutationGenerator(15)

#ps = [[10, 3, 7, 4, 6, 2, 1, 8, 5, 11, 12, 9], [2, 1, 7, 5, 9, 6, 12, 10, 3, 4, 8, 11], [9, 8, 12, 4, 7, 5, 11, 1, 6, 2, 10, 3], [3, 11, 5, 9, 7, 6, 12, 4, 1, 2, 10, 8], [5, 12, 9, 11, 8, 10, 6, 1, 7, 2, 3, 4]]
#
#
#m1 = mf1.MedianFinder(ps)
#m2 = mf2.MedianFinder(ps)
#m1.findMedian()
#m2.findMedian()


for i in range(1):
    ps = [[9, 12, 11, 5, 8, 1, 2, 6, 4, 7, 3, 10], [11, 8, 4, 3, 10, 12, 6, 9, 5, 7, 1, 2], [3, 10, 2, 6, 1, 11, 5, 7, 4, 12, 8, 9], [8, 10, 2, 1, 4, 12, 6, 7, 9, 5, 11, 3], [4, 3, 2, 7, 1, 6, 10, 8, 11, 9, 12, 5]]
    #pg.generatePermutationSet(5,12)
    m1 = mf1.MedianFinder(copy.deepcopy(ps))
    m2 = mf2.MedianFinder(copy.deepcopy(ps))
    m1.findMedian()
    m2.findMedian()

#for i in range(20):
#    ps = pg.generatePermutationSet(4,14)
#    mf = mfinder.MedianFinder(ps)
#    mf2 = mfinder2.MedianFinder(ps)

#    if mf.solutions == mf2.solutions:
#        print('OK!')
#    else:
#        print('***PROBLEM***')
#        print(ps)
