#! /usr/bin/env python3
import ast
import math
import KendallTauDistance as kt


class MedianFinder:

    def __init__(self, permSet):

        #holds the solution set (medians)
        self.solutions = []
        #holds the permutation set for which we want to find it's median(s)
        self.permSet = []
        #hold the min kendall tau distance found thus far
        self.dist_KT = float('inf')
        #Holds the size of the permutations
        self.permSize = 0
        #Holds element we can add to the rigth or to the left of the integer i
        self.elem_right = []
        self.elem_left = []

        self.permSet = permSet
        self.permSize = len(permSet[0])
        self.elem_right = [set() for x in range(self.permSize+1)]
        self.elem_left = [set() for x in range(self.permSize+1)]

        self.buildConstraints()
        self.buildInitialInstances()

        self.inverse = False

        #Check of the mirror set has better constraints
        self.checkConstraints()

        self.elem_right[0] = {x for x in range(1,self.permSize+1)}

    def checkConstraints(self):

        #2D array that counts the number of sets of size j in elem_right and elem_left
        # i = 0 --> counts for elem_left
        # i = 1 --> counts for elem_right
        count = [[0 for x in range(self.permSize)] for x in range(2)]
        #check if the mirror set has better constraints
        for sl, sr in zip(self.elem_left, self.elem_right):
            if len(sl) == self.permSize or len(sr) == self.permSize:
                continue

            count[0][len(sl)] += 1
            count[1][len(sr)] += 1

        for size in range(self.permSize):

            if count[0][size] < count[1][size]:
                #then it is better to find the median of the inverse
                self.inverse = True
                #swap them
                self.elem_right, self.elem_left = self.elem_left, self.elem_right
                break;
            elif count[0][size] > count[1][size]:
                #nothing needs to be done
                break;

        if self.inverse:
           for p in self.permSet:
               p.reverse()

    def buildConstraints(self):
        """ builds initial constraints from the given permutation set

        """

        #counts the number of times the int i is before j in all permutations in permSet
        counterMatrix = [[0 for x in range(self.permSize+1)] for x in range(self.permSize+1)]

        #Start by counting
        for perm in self.permSet:
            for i in range(self.permSize-1):
                for j in range(i+1, self.permSize):
                    counterMatrix[perm[i]][perm[j]]+=1

        majority = math.ceil(len(self.permSet)/2.0)
        for i in range(len(counterMatrix)):
            for j in range(len(counterMatrix)):
                if counterMatrix[i][j] >= majority:
                    #Then we have j to the right of i in a majority of cases
                    self.elem_right[i].add(j)
                    #and i to the left of j
                    self.elem_left[j].add(i)
        return

    def buildInitialInstances(self):
        """ Builds and returns the initial instances from which we start building our solution

        """

        size = len(self.elem_left)
        partialSol = []
        initialInstances=[]

        #Find (if it exists) the element that will be the first element of a median
        for i in range(1,size):
            if not self.elem_left[i]:
                #We found the first element of a median
                partialSol.append(i)
                #We start constructing this initial instance
                #Continue adding elements to the partial solution until its no longer possible
                done = False
                while(not done):
                    lastElementAdded = partialSol[len(partialSol)-1]
                    done = True
                    for s in self.elem_left:
                        if (len(s) == 1) and (lastElementAdded in s):
                            partialSol.append(self.elem_left.index(s))
                            done=False
                            break

                initialInstances.append(partialSol[:])
                del partialSol[:]

        return initialInstances

    def isValid(self, candidate):
        """ Checks validity

        """
        valid = True

        lastElement = 0
        if candidate:
            lastElement = candidate[len(candidate)-1]

            #If we can't add anymore elements we can't continue
            if not self.elem_right[lastElement]:
                valid = False
        return valid

    def findMedBT(self, potentialSolution):
        """"  Finds the median by starting from the given initial solution set

        """
        #CASE 1: We have a potential solution if it is the correct size
        if len(potentialSolution) == self.permSize:
            currentDist = kt.KendallTauDistance(potentialSolution, self.permSet)
            #Check if it's a better solution
            if currentDist < self.dist_KT:
                self.dist_KT = currentDist
                del self.solutions[:]
                self.solutions.append(list(potentialSolution))
                return
            #Check if it is as good as the solutions we found
            elif currentDist == self.dist_KT:
                self.solutions.append(list(potentialSolution))
                return
            #If we reach here then the potential solution is not an optimal one
            else:
                return

        #CASE 2: Check if the current partial solution is valid
        elif (not self.isValid(potentialSolution)):
            return

        #holds the last element added to the current potential solution permutation
        lastElement = 0
        #Find the elements that we can add to the current solution and recurse
        if potentialSolution:
            lastElement = potentialSolution[len(potentialSolution)-1]
        else:
            lastElement = 0

        for elem in self.elem_right[lastElement]:
            if elem not in potentialSolution:
                potentialSolution.append(elem)
                self.findMedBT(potentialSolution)
                if potentialSolution:
                    potentialSolution.pop()

    def findMedian(self):
        """ Loops through all the inital solutions and calls findMedBT

        """

        startingInstances = self.buildInitialInstances()

        if not startingInstances:
            startingInstances = [[]]

        for potentialSolution in startingInstances:
            self.findMedBT(potentialSolution)

        if self.inverse:
            for s in self.solutions:
                s.reverse()


if __name__ == '__main__':

    permSet=[[9, 4, 8, 2, 3, 7, 5, 6, 13, 12, 11, 1, 10], [2, 3, 4, 1, 8, 6, 5, 7, 9, 11, 12, 10, 13], [4, 2, 3, 8, 1, 7, 9, 13, 5, 6, 11, 12, 10]]
    mf = MedianFinder(permSet)
    mf.findMedian()
    print(mf.solutions)
