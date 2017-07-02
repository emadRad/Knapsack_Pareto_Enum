#!/bin/bash

dim=5

DATE=`date +%Y-%m-%d:%H:%M:%S`

printf "\n$DATE\n" | tee -a testRun.log

(cd ..; ant | tee -a testRun.log) &&	
(for ((i=11; i<=1000; i+=1 ))
do
	python3 inputGen.py $i $dim
	fileName=Inputs/input\_"$dim"d\_"$i"p.dat

	java -Xms512m -Xmx3g -jar knapsack_nu.jar $fileName >> results.log

	STATUS="${?}"
	if [ "${STATUS}" == 1 ]
	then
		printf "\nShell script is terminated!!\n" | tee -a  testRun.log
		echo $fileName | tee -a testRun.log
		echo "Incorrect result" | tee -a testRun.log
		exit
	fi

	echo $fileName | tee -a testRun.log

done)
