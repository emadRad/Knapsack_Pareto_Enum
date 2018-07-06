#!/bin/bash

dim=3

DATE=`date +%Y-%m-%d:%H:%M:%S`

printf "\nInputSize,Time(ns),Dim,RepeatNum,AvgIterTime,AvgParetoSize,MaxParetoSize,MinParetoSize,AvgSize,MaxSize\n" >> results.log
printf "\n$DATE\n" | tee -a testRun.log

repeatNum=10

(cd ..; ant | tee -a testing/testRun.log) &&
(for ((i=10; i<=50; i+=1 ))
do
	timeSum=0
	for (( j=0; j<=$repeatNum ; j++ ))
	do
		python3 inputGen.py -s $i -d $dim -p Inputs/
		fileName=Inputs/input\_"$dim"d\_"$i"p.dat

        
		output=$(java -jar knapsackParetoEnum.jar -f $fileName -m expr) 
		
		# arr	
    # 0 : TotalTime
    # 1 : AvgIterTime, average time of each iteration
    # 2 : AvgParetoSize, average size of paretoSize
    # 3 : MaxParetoSize
    # 4 : MinParetoSize
    # 5 : AvgSize, average size each unionSet
    # 6 : MaxSize, max size of unionSet
		IFS=" " read -ra arr <<< "$output"
			
		#echo "${arr[0]}" >> results.log

		let "timeSum = $timeSum + ${arr[0]}"

		STATUS="${?}"
		if [ "${STATUS}" == 1 ]
		then
			printf "\nShell script is terminated!!\n" | tee -a  testRun.log
			echo $fileName | tee -a testRun.log
			echo "Incorrect result" | tee -a testRun.log
			exit
		fi
	
	done

	echo $fileName | tee -a testRun.log
	
	let "avgTime = $timeSum/$repeatNum "
	
	printf "$i,$avgTime,$dim,$repeatNum,${arr[1]},${arr[2]},${arr[3]},${arr[4]},${arr[5]},${arr[6]}\n" >> results.log

done)
