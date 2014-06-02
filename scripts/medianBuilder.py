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

log = open('log.txt','w')

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

    permSets = []
    for line in lines:
        if line.startswith('['):
            line = sub(r'\n','',line)
            permSets.append(literal_eval(line))
    return permSets

#Builds the medians and writes results to a file
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
    log.write('minDist = ' + str(minDist)+'\n')
    log.write('Value= ' + str(value)+'\n')
    diff = minDist-value
    log.write('DIFFERENCE: ' + str(diff) + '\n')
    #First Check if the median is actually the closest permutation
    if value == minDist:
        return closestPermutations, diff

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

    return medians, diff

def inversePermutation(permutation):
    if not permutation:
        return None
    inverse = list(range(len(permutation)))

    for i in range(0,len(permutation)):
        inverse[((permutation[i])-1)] = i

    return inverse

def KendallTauDistance(permutation, permSet):
    distance = 0
    totalDist = 0
    permA =inversePermutation(permutation)
    for p in permSet:
        permB = inversePermutation(p)

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
directory = "../Resultats/Res/"
dirList = listdir(directory)

#Holds the biggest difference value in all the files
maxDiff = 0
for folder in dirList:
    filePaths = directory+folder
    #Get all the txt files
    filePaths = glob(filePaths+"/*.txt")
    for a_file in filePaths:
        print("Reading the file: " + a_file)
        log.write("Reading the file: " + a_file+'\n')
        lines = loadFile(a_file)
        vals = getObjectiveValues(lines)
        permSets = getPermutationSets(lines)
        #Holds the biggest difference value found in a_file
        maxDiff_file = 0
        for index, p in enumerate(permSets):
            med,diff = buildMedian(vals[index],p)
            if diff > maxDiff_file:
                maxDiff_file = diff
            if not med:
                log.write('Could not find median of distance 1 from closest permutation\n')
                log.write('MEDIAN: ' + str(med)+'\n')
            else:
                log.write(str(med)+'\n')
                log.write('Distance: ' + str(KendallTauDistance(med[0],p))+'\n')
        log.write("Maximum distance difference in " + a_file + ": " + str(maxDiff_file)+'\n')
    if maxDiff_file > maxDiff:
        maxDiff = maxDiff_file

log.write("Maximum distance difference: " + str(maxDiff)+'\n')
