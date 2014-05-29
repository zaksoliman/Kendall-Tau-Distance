import ast

#holds the solution set (medians)
solutions = []
#holds the permutation set for which we want to find it's median(s)
permSet = []

def getKendallTauDist(currentSolution, permSet):
    #TODO

#builds initial constraints from the given permutation set
def buildConstraints(permSet):
    #TODO

#Builds and returns the initial instances from which we start building our
#solution
def getInitialInstances():
    #TODO

#Finds the median by starting from the given initial solution set
def findMedBT(initialSolution):
    #TODO

#Loops through all the inital solutions and calls findMedBT
def findMedian():
    startingInstances = getInitialInstances()
    for potentialSolution in startingInstances:
        findMedBT(potentialSolution)

