#!/usr/bin/env python3

from  os import listdir
from os.path import join
from re import sub


def convertFile(filePath):
    filePath = filePath
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
directory = "../Results/Big_Results"
fileList =  listdir(directory)

for f in fileList:
    filePaths = join(directory, f)
    #Get all the txt files
    for a_file in filePaths:
        print("Now visiting file: " + a_file)
        convertFile(a_file)
