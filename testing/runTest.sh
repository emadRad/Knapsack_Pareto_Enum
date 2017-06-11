#!/bin/bash

dim=5

(cd ..; ant) &&	
(for ((i=11; i<=1000; i+=1 ))
do
	python3 inputGen.py $i $dim
	fileName=Inputs/input\_"$dim"d\_"$i"p.dat

	java -jar knapsack_nu.jar $fileName >> results.log

	STATUS="${?}"
	if [ "${STATUS}" == 1 ]
	then
		printf "\nShell script is terminated!!\n"
		echo $fileName
		echo "Incorrect result"
		exit
	fi

	echo $fileName

done)
