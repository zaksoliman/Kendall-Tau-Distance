#!/usr/bin/env python3

from  os import listdir
from glob import glob
from re import sub

###########
# Funtion #
###########

def convertFile(filePath):
    f = open(filePath)
    lines = f.readlines()
    f.close()
    f = open(filePath, 'w')


    for index, line in enumerate(lines):
        if line.startswith('{'):
            line = sub('(\d*)\[(\d*)\](\d*)', r'\2',line)
            line = sub('{','[', line)
            line = sub('}',']',line)
            #replace old line with new line
            lines[index] = line
    #Now we write to the file
    for line in lines:
        f.write(line)
    f.close()

########
# MAIN #
########

#Get all the subdirecotries to start converting
directory = "../Resultats/Res/"
dirList = listdir(directory)

for folder in dirList:
    filePaths = directory+folder
    #Get all the txt files
    filePaths = glob(filePaths+"/*.txt")
    for a_file in filePaths:
        print("Now visiting file: " + a_file)
        convertFile(a_file)
