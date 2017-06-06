# -*- coding: utf-8 -*-
"""
Created on Fri Apr 21 20:23:08 2017

@author: emad
"""

import random as rand
#from statistics import median
#from operator import itemgetter, attrgetter
import sys,os


def stylePrint(a):
    for x in a:
        print("\t".join(str(y) for y in x))



# the interval for vector components
# each component x is a integer from [startRange,endRange)
startRange = -15
endRange = 15




#dimension of each vector
dim = 3

#number of vectors
input_size = 500

#first arguemnt: input_size
#second argument: dimension
if len(sys.argv)>2:
    input_size = int(sys.argv[1])
    dim = int(sys.argv[2])

labels=["P"]
labels.extend([rand.choice(["P","W"]) for i in range(dim-1)])

a = [rand.sample(range(startRange,endRange),dim) for i in range(input_size)]

os.chdir("Inputs/")
fileName = "input_{}d_{}p.dat".format(dim,input_size)
with open(fileName,'w') as f:
    f.write("{} {}\n".format(input_size,dim))
    f.write(" ".join(labels))
    f.write("\n")
    for x in a:
        lineStr ="\t".join(str(y) for y in x)
        lineStr+="\n"
        f.write(lineStr)



    