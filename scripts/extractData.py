#!/usr/bin/env python3

from  os import listdir
from os.path import join
from re import sub


def convertFile(directory, file_name):
    filePath = join(directory, file_name)
    f = open(filePath)
    lines = f.readlines()
    f.close()
    f = open(filePath, 'w')

    for index, line in enumerate(lines):
        if line.startswith('['):
            line = sub(r'(\[([\d, \[\]]*)\])[a-zA-Z\s:]*(\[\[[\d, \[\]]*\]\]), [\w]+: (\d)+[,\w: \d]+', r'[\1];\3;\4',line)
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
    print("Now visiting file: " + f)
    convertFile(directory, f)
