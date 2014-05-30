#!/usr/bin/env python3

#############################################################################
#                                                                           #
#   This script reads permutation sets and tries to build a median by using #
#   one of the permutation in the set currently explored by checking all    #
#   possible single inversions                                              #
#                                                                           #
#############################################################################

from  os import listdir
from glob import glob
from re import sub
from ast import literal_eval

############
# Funtions #
############

def loadFile(filePath):
    f = open(filePath)
    lines = f.readlines()
    f.close()
    return lines

def getObjectiveValues(lines):

    values = []
    for line in lines:
        if line.startswith('Objective'):
            line = sub('[^\d/.]','',line)
            line = sub(r'\n','',line)
            values.append(int(float(line)))
    return values

def getPermutationSets(lines):

    permSet = []
    for line in lines:
        if line.startswith('['):
            line = sub(r'\n','',line)
            permSet.append(literal_eval(line))
    return permSet

def buildMedian(value, permSet):
    minDist = float('inf') #holds the distance of the closest permutation in permSet
    closestPermutations = []

    #Get the closest permutations
    for permutation in permSet:
        distance = KendallTauDistance(permutation, permSet)
        if distance < minDist:
            minDist = distance
            del closestPermutations[:]
            closestPermutations.append(permutation)
        elif distance == minDist:
            closestPermutations.append(permutation)

    print('DIFFERENCE: ' + str(minDist-value))
    #First Check if the median is actually the closest permutation
    if value == minDist:
        return closestPermutations

    medians = []
    #Try every posible single inversion on the closest permutations
    for permutation in closestPermutations:
        for i in range(0,len(permutation)-1):
            for j in range(i,len(permutation)):
                #swap
                permutation[i], permutation[j] = permutation[j], permutation[i]
                if KendallTauDistance(permutation, permSet) == value:
                    medians.append(list(permutation))
                #swap back to continue
                permutation[i], permutation[j] = permutation[j], permutation[i]

    return medians

def KendallTauDistance(permutation, permSet):
    distance = 0
    totalDist = 0
    permA = list(reversed(permutation))
    for p in permSet:
        permB = list(reversed(p))

        #####################
        # COMPUTE DISTANCE  #
        #####################
        for i in range(0,len(p)-1):
            for j in range(i, len(permB)):
                if((permA[i]<permA[j])and(permB[i]>permB[j])):
                    distance+=1
                if((permA[i]>permA[j]) and (permB[i]<permB[j])):
                    distance+=1
        totalDist += distance
        distance = 0
    return totalDist

#Get all the subdirecotries to start converting
directory = "../Results/"
dirList = listdir(directory)

for folder in dirList:
    filePaths = directory+folder
    #Get all the txt files
    filePaths = glob(filePaths+"/*.txt")
    for a_file in filePaths:
        lines = loadFile(a_file)
        vals = getObjectiveValues(lines)
        permSet = getPermutationSets(lines)

        for index, p in enumerate(permSet):
            med = buildMedian(vals[index],p)
            if not med:
                print('Could not find median of distance 1 from closest permutation')
                print('MEDIAN: ' + str(med))
            else:
                print(str(med))
                print('Distance: ' + str(KendallTauDistance(med[0],permSet)))
