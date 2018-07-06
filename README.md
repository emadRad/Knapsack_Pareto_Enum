# Pareto set enumerator

The multidimensional knapsack problem can be solved using the Nemhauser-Ullmann[^1] algorithm. This algorithm generate a sequence of Pareto sets( set of Pareto optimal solutions)  and the running time depends on the size of Pareto sets. The smoothed analysis[^2] of this algorithm is based on the size of Pareto sets.  The main part of this algorithm is to compute the Pareto set. For this task we have implemented the multidimensional divide and conquer[^3] and Fast linear expected-time[^4] (FLET) algorithms. Finally we use this implementation to enumerate the Pareto sets and store some statistics.


#### Requirement
* JDK(Java Development Kit) 1.8

* Apache Ant(TM) version 1.9.6

#### How to compile and run
You can build the project by using Ant. It just needed to run the **_ant_** command in the main folder in which the **_build.xml_** exists. It will create a jar file called **knapsackParetoEnum.jar** in the _testing_ folder and the main folder as well. Then you can run it in _testing_ folder as follow.

```bash
$ java -jar knapsackParetoEnum.jar -f Inputs/input_4d_11p.dat -m single -a mdc
Using 'mdc' algorithm for computing Pareto set.
Input size: 11 Dimension: 4
Total runtime: 59 ms
Average iteration time: 5
Average size of Pareto set: 45
Max size of Pareto set: 105
Min size of Pareto set: 3
Average size of input for maxima finding: 71
Max size of input for maxima finding:: 181
```
In Nemhauser

#### Input File Format

You can generate the input files with the python code **inputGen.py**.
The python script can be executed according to the following manual.

```bash
$ python3 inputGen.py -h
usage: inputGen.py [-h] -s INPUT_SIZE -d DIM [-p PATH]

Generates an instance of the multidimensional knapsack problem

optional arguments:
  -h, --help            show this help message and exit
  -s INPUT_SIZE, --input_size INPUT_SIZE
                        The number of item in knapsack
  -d DIM, --dim DIM     The dimension of input
  -p PATH, --path PATH  The path to save the output file. Default is the same
                        directory as this script.
```
For example, you can generate an instance in the same directory(the default path) as follows:

```bash
$ python3 inputGen.py -s 640 -d 4
```

  This command creates a file named **input\_4d\_640p.dat **  in the same directory as the **inputGen.py** script.



[^1]: Discrete dynamic programming and capital allocation. George L. Nemhauser and Zev Ullmann
[^2]: Smoothed analysis of algorithms: Daniel A. Spielman and Shang-Hua Teng
[^3]: Multidimensional Divide-and-conquer: Jon Louis Bentley.
[^4]: Fast linear expected-time algorithms for computing maxima and convex hulls: Jon L. Bentley, Kenneth L. Clarkson, and David B. Levine.