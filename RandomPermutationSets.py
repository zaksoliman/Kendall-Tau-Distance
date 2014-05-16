import numpy as np
import pandas as pd
import matplotlib.pyplot as plt


data = pd.read_csv("RandomPermutationsOddSets.csv")

data.plot(kind='bar')

plt.savefig('dist.pdf')
