#!/usr/bin/env python3
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

prefix = "RandomPermutationSets_"
suffix = ".csv"

for i in range(3,8):
    data = pd.read_csv("../Results/"+prefix+str(i)+suffix)
    #slice to get the data I need
    data = data.ix[:,'|M(A)|':'|M(B)B|']
    data.plot(kind='bar', title='Permutation sets', figsize=(20,10) )
    plt.savefig("distribution_" + str(i)  +".pdf")
