#!/bin/bash

dim=4

DATE=`date +%Y-%m-%d:%H:%M:%S`

printf "\nInputSize,Dim,Time(ms),AvgIterTime,ParetoSize,AvgSize,MaxSize\n" >> results.log
printf "\n$DATE\n" | tee -a testRun.log

(cd ..; ant | tee -a testing/testRun.log) &&	
(for ((i=30; i<=40; i+=1 ))
do
	python3 inputGen.py $i $dim
	fileName=Inputs/input\_"$dim"d\_"$i"p.dat

	java -jar knapsack_nu.jar $fileName >> results.log

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
