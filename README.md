# Nemhauser-Ullmann algorithm

An implementation of the Nemhauser-Ullmann algorithm for multidimensional knapsack problem.


#### Requirement
* JDK(Java Development Kit) 1.8

* Apache Ant(TM) version 1.9.6

#### How to compile and run
You can build the project by using Ant. It just needed to run the **_ant_** command in the main folder in which the **_build.xml_** exists. It will create a jar file called **knapsack\_nu.jar** in the _testing_ folder. Then you can run it in _testing_ folder as follow.

```
$ java -jar knapsack_nu.jar Inputs/input_4d_11p.dat

```
* If you don't provide the input file as an argument, the program will run with the default input file which is the file *input\_4d\_11p.dat*.

#### Input File Format
You can generate the input files with the python code **inputGen.py**.
You can change the following variables in the python code.

```python
# the interval for vector components
# each component x is a integer from [startRange,endRange)
startRange = -100
endRange = 101

#dimension of each vector
dim = 4

#number of vectors
input_size = 11

labels=["P"]
labels.extend([rand.choice(["P","W"]) for i in range(dim-1)])

a = [rand.sample(range(startRange,endRange),dim) for i in range(input_size)]

fileName = "input_{}d_{}p.dat".format(dim,input_size)
```
For example this piece of code will create a file with the name ***input\_4d\_11p.dat*** in the _Inputs_ folder. 