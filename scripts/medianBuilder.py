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
            values.append(int(float(line)))
    return values

def getPermutationSets(lines):

    permSet = []
    for line in lines:
        if line.startswith('['):
            permSet.append(literal_eval(line))
    return permSet

def buildMedian(values, permSet):
    #TODO

#Get all the subdirecotries to start converting
directory = "../Results/Res/"
dirList = listdir(directory)

for folder in dirList:
    filePaths = directory+folder
    #Get all the txt files
    filePaths = glob(filePaths+"/*.txt")
    for a_file in filePaths:
        lines = loadFile(a_file)
        vals = getObjectiveValues(lines)
        permSet = getPermutationSets(lines)
