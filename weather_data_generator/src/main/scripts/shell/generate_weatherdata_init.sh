#!/bin/bash
#This script initiate weather data generation.

if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ]
then
        echo "Wrong Syntax, Usage: generate_weatherdata_init.sh <stationcode> <startdate> <endate>"
        echo "stationcode should be three letter IATA code"
else	
		#TODO Need to implement logscanner utility
		cd ../
		java -cp lib/weatherdatagenerator-0.0.1.jar com.data.weatherdatagenerator.GenerateWeatherData $1 $2 $3
		
		EXIT_CODE=$?
		
		if [ $EXIT_CODE != 0 ]
		then
			echo "Job Failed"
			exit 1
		else
			echo "Job completed sucessflly"
			exit 0
		fi
fi
