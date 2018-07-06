# -*- coding: utf-8 -*-
"""
Created on Fri Apr 21 20:23:08 2017

@author: emad
"""

import random as rand
import sys,os
import argparse


def stylePrint(a):
    for x in a:
        print("\t".join(str(y) for y in x))


# the interval for vector components
# each component x is a integer from [startRange,endRange)
startRange = 0
endRange = 1000000
#dimension of each vector
dim = 3
#number of vectors
input_size = 500

#first arguemnt: input_size
#second argument: dimension
parser = argparse.ArgumentParser(description="Generates an instance of the multidimensional knapsack problem")
parser.add_argument("-s","--input_size", required=True ,type=int, help="The number of item in knapsack")
parser.add_argument("-d","--dim", type=int, required=True, help="The dimension of input")
parser.add_argument("-p","--path", default=".",help="The path to save the output file. Default is the same directory as this script.")
args = parser.parse_args()


labels=['P','W']
labels.extend(["P" for i in range(args.dim-2)])

a = [rand.sample(range(startRange,endRange), args.dim) for i in range(args.input_size)]

os.chdir(args.path)
fileName = "input_{}d_{}p.dat".format(args.dim, args.input_size)
with open(fileName,'w') as f:
    f.write("{} {}\n".format(args.input_size,args.dim))
    f.write(" ".join(labels))
    f.write("\n")
    for x in a:
        lineStr ="\t".join(str(y) for y in x)
        lineStr+="\n"
        f.write(lineStr)



    
