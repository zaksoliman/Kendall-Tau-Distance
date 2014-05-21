import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

prefix = "RandomPermutationsOddSets("
suffix = ").csv"

for i in range(0,4):
    data = pd.read_csv("../Results/"+prefix+str(i)+suffix)
    #slice to get the data I need
    data = data.ix[:,'|A|':'|M(AUB)B|']
    data.plot(kind='bar', title='Permutation sets' )
    plt.savefig('dist'+str(i)+'.pdf')
