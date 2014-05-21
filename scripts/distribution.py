import pandas as pd
import matplotlib as plt

permutation3 = pd.read_csv('Results/Permutation_3.csv')
permutation4 = pd.read_csv('Results/Permutation_4.csv')
permutation5 =  pd.read_csv('Results/Permutation_5.csv')

permutation3[' Median set size'].hist()
plt.savefig('dist_permutation_3.pdf')
#do the rest
