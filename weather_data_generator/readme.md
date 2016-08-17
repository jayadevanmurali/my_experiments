## Weather Data Generator:

### Requirment

Create a toy model of the environment (taking into account things like atmosphere, topography,geography, oceanography, or similar) that evolves over time. Then take measurements at various locations (ie weather stations), and then have your program emit that data, as in the following:


SYD|-33.86,151.21,39|2015-12-23T05:02:12Z|Rain|+12.5|1004.3|97

MEL|-37.83,144.98,7|2015-12-24T15:30:55Z|Snow|-5.3|998.4|55

ADL|-34.92,138.62,48|2016-01-03T12:35:37Z|Sunny|+39.4|1114.1|12

### Input File Details

There are two types of input files.

1. Historical weather data file - This is a comma seperated file. This file will contains following feilds-station_code,month,min_temperature,max_temperature,min_pressure,max_pressure,min_humidity,max_humidity
2. Weather Station geography data file - This is comma seperated file. This fill will conatins follwing feilds- station_code,latitude,longitude,elevation

The end user can specify the file path in application property file - datagenerator.properties.

### Miscellaneous  Informations

All application parameters like historical weather file name, output file name, input date formats,weather_snow_min_temp etc can be configured in datagenerator.properties.

The target platform for this application is Linux and Windows.

## How to build:

Step 1. Clone project from github.(url -https://github.com/jayadevanmurali/my_experiments.git)

	git clone https://github.com/jayadevanmurali/my_experiments.git

Step 2. Go to project home directory by executing following command- 

	cd my_experiments/weather_data_generator/

Step 3. Execute the following command - 

	mvn clean package

This command will generate weatherdatagenerator-0.0.1.tar.gz file which contains deliverables. This file include following sub directories
	
	-bin 		-  Contains the shell/bash scripts for invoking the application
		
	-lib 		-  Contains executable jar file
	
	-conf   	-  Contains datagenerator.properties with default values
	
	-resources 	-  Contains sample reference data (historical weather data file & station geography data file)
	

## How to run the application:

Step 1. Copy the weatherdatagenerator-0.0.1.tar.gz from target directory to required server.

Step 2.Un-zip the weatherdatagenerator-0.0.1.tar.gz file generated in maven build. Example

	mkdir weatherdatagenerator
	tar -zxvf weatherdatagenerator-0.0.1.tar.gz -C weatherdatagenerator
			
Step 2. Go to bin directory and execute shell script by invoking following command-
	
	cd weatherdatagenerator/bin
	sh generate_weatherdata_init.sh <stationcode> <startdate> <endate>

## How to run Junit test case alone : 

Step 1. Go to project home directory
	
	cd path_to_project_home

Step 2. Execute the following command
	
	mvn test

## Preconditions for Maven build,test & run:

1. Java should be installed and configured environment variable JAVA_HOME. We can set the environment variable by invoking following command- 

	export JAVA_HOME=path_to_java_home
	
2. Maven should be installed and configured
