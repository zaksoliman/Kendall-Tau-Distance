#! /usr/bin/env python3
from math import factorial
from random import shuffle, Random

def perm_given_index(alist, apermindex):
    alist = alist[:]
    for i in range(len(alist)-1):
        apermindex, j = divmod(apermindex, len(alist)-i)
        alist[i], alist[i+j] = alist[i+j], alist[i]
    return alist

perm = list(range(1,13))

indx = list(range(factorial(len(perm))))

indx = shuffle(indx)

for i in indx[:10]:
    print(perm_given_index(perm, i))
