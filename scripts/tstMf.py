import medianFinder as mfinder
import medianFinder2 as mfinder2
from permutationGenerator import PermutationGenerator

pg = PermutationGenerator(15)

for i in range(20):
    ps = pg.generatePermutationSet(4,14)
    mf = mfinder.MedianFinder(ps)
    mf2 = mfinder2.MedianFinder(ps)

    if mf.solutions == mf2.solutions:
        print('OK!')
    else:
        print('***PROBLEM***')
        print(ps)
