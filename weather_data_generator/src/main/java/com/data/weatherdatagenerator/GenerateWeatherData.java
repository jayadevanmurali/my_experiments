
package com.data.weatherdatagenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import com.data.weatherdatagenerator.constants.Constants;
import com.data.weatherdatagenerator.utils.Utils;
import com.data.weatherdatagenerator.utils.ValidationUtils;


/**
 * This the main class which will trigger data generation. The generated data will be in the following format.
 * 
 * station_code|date|Conditions|temperature|pressure|humidity
 * 
 * @author Jayadevan M
 *
 */
public class GenerateWeatherData {

	public static void main (String args[]) throws Exception{
		
		boolean isDone = true;
		Properties prop = Utils.loadProperties();
		String weatherData = null;
		
		boolean isValid = ValidationUtils.validateArguments(args,prop);
		
		if(isValid){
			weatherData = generateWeatherData(args[0],args[1],args[2],prop);
			
			if(weatherData!=null && !weatherData.isEmpty())
				isDone = Utils.writeToFile(weatherData, prop.getProperty(Constants.OUTPUT_FILE_NAME));
		}
		
		if(isDone)
			System.exit(0);
		else
			System.exit(1);
			
	}

	/**
	 * This method will generate weather data for the given station code and date range.
	 * 
	 * @param stationCode
	 * @param startDate
	 * @param endDate
	 * @param prop
	 * 
	 * @return isDone
	 * @throws Exception 
	 */
	public static String generateWeatherData(String stationCode,String startDate,String endDate,Properties prop) throws Exception {
		
		StringBuilder data = new StringBuilder();
		String [] timeFact = prop.get(Constants.WEATHER_TEMP_TIME_FACTOR).toString().split(",");
		Calendar startDt = parseDate(startDate,new SimpleDateFormat(prop.getProperty(Constants.INPUT_DATE_FORMAT)));
		Calendar endDt = parseDate(endDate,new SimpleDateFormat(prop.getProperty(Constants.INPUT_DATE_FORMAT)));

		for (Calendar date = startDt; date.before(endDt); date.add(Calendar.DATE, 1))
		{
			for(int i=1;i<4;i++){
				date.set(Calendar.HOUR_OF_DAY, i*6);
				date.set(Calendar.MINUTE, 0);
				data.append(getWeatherData(stationCode,date,Integer.parseInt(timeFact[i-1]),prop));
			}
		}

		System.out.println(data.toString());

		return data.toString();
	}
	
	/**
	 * This method create weather data for a given station code and date
	 * 
	 * @param stationCode
	 * @param date
	 * @param prop
	 * @return
	 */
	public static String getWeatherData(String stationCode, Calendar date, int timeFactor, Properties prop) {
		
		Float temperature,humidity,pressure ;
		String stationGeoDetails=null;
		Map<String,String> refWeatherDataMap= Utils.loadRefrenceWetherdata(prop);
		Map<String,String> refGeoMap= Utils.loadRefrenceGeodata(prop);
		
		
		String[] value = refWeatherDataMap.get(Utils.generateKey(stationCode,String.valueOf(date.get(Calendar.MONTH)+1),
				Constants.WEATHER_MAP_DELIMITER)).split(Constants.WEATHER_MAP_DELIMITER);

		temperature =  getTemperature(value[0],value[1],timeFactor);
		pressure = getPressure(temperature,value[2],value[3]);
		humidity= getHumidity(temperature,value[4],value[5]);
		stationGeoDetails = refGeoMap.get(stationCode);
		
		return generateRow(stationCode,stationGeoDetails,date,
				temperature,pressure,humidity,prop);
	}

	/**
	 * This method will create a row by appending various weather attributes
	 * 
	 * @param stationCode
	 * @param stationGeoDetails
	 * @param date
	 * @param temperature
	 * @param pressure
	 * @param humidity
	 * @param prop
	 * @return
	 */
	public static String generateRow(String stationCode,
			String stationGeoDetails, Calendar date, Float temperature, Float pressure,
			Float humidity, Properties prop) {
		
		SimpleDateFormat sdfout = new SimpleDateFormat(prop.getProperty(Constants.OUTPUT_FILE_DATE_FORMAT));
		StringBuilder row = new StringBuilder();
		
		row.append(stationCode).append(prop.getProperty(Constants.OUTPUT_FILE_DELIM));
		row.append(stationGeoDetails).append(prop.getProperty(Constants.OUTPUT_FILE_DELIM));
		row.append(sdfout.format(date.getTime())).append(prop.getProperty(Constants.OUTPUT_FILE_DELIM));
		row.append(getCondition(temperature,prop)).append(prop.getProperty(Constants.OUTPUT_FILE_DELIM));
		row.append(String.format("%.2f",temperature)).append(prop.getProperty(Constants.OUTPUT_FILE_DELIM));
		row.append(String.format("%.2f",pressure)).append(prop.getProperty(Constants.OUTPUT_FILE_DELIM));
		row.append(String.format("%.2f",humidity)).append(prop.getProperty(Constants.OUTPUT_FILE_LINE_DELIM));
		
		return row.toString();
		
	}

	/**
	 * This method will parse given string date using the given pattern 
	 * @param startDate
	 * @param simpleDateFormat
	 * @return
	 * @throws Exception
	 */
	public static Calendar parseDate(String startDate,
			SimpleDateFormat simpleDateFormat) throws Exception {
		
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(simpleDateFormat.parse(startDate));
		} catch (ParseException e) {
			throw new Exception("Failed to parse date- "+startDate+"Expected format is "+
						simpleDateFormat.toPattern(),e.getCause());
		}
		return cal;

	}

	/**
	 * This method will return weather condition based on temperature
	 * @param randomValue
	 * @return
	 */
	private static String getCondition(Float temperature,Properties prop) {

		if (temperature > Integer.parseInt(prop.getProperty(Constants.WEATHER_SNOW_MIN_TEMP))
				&& temperature <= Integer.parseInt(prop.getProperty(Constants.WEATHER_SNOW_MAX_TEMP)))
			return WeatherConditions.Snow.toString();
		else if(temperature > Integer.parseInt(prop.getProperty(Constants.WEATHER_RAIN_MIN_TEMP))
				&&  temperature <= Integer.parseInt(prop.getProperty(Constants.WEATHER_RAIN_MAX_TEMP)))
			return WeatherConditions.Rain.toString();
		else if (temperature > Integer.parseInt(prop.getProperty(Constants.WEATHER_SUNNY_MIN_TEMP))
				&&  temperature <= Integer.parseInt(prop.getProperty(Constants.WEATHER_SUNNY_MAX_TEMP)))
			return WeatherConditions.Sunny.toString();
		else
			return WeatherConditions.Unknown.toString();

	}

	/**
	 * This method generate random float values from the given min and max values
	 * @param min
	 * @param max
	 * @return
	 */
	private static Float getTemperature(String min, String max,int timeFactor) {

		Random generator = new Random();
		Float tmpMax = new Float(max);
		Float tmpMin = new Float(min);
		return tmpMin + generator.nextInt((tmpMax.intValue()- tmpMin.intValue())*timeFactor/100)+generator.nextFloat();
	}
	
	/**
	 * This method generate pressure value depend on temperature
	 * @param min
	 * @param max
	 * @return
	 */
	private static Float getPressure(Float temp, String min, String max) {
		
		return (Float.parseFloat(max)-Float.parseFloat(min))/temp+Float.parseFloat(min);
	}
	
	/**
	 * This method generate humidity value depend on temperature
	 * @param min
	 * @param max
	 * @return
	 */
	private static Float getHumidity(Float temp, String min, String max) {
		
		return (Float.parseFloat(max)+Float.parseFloat(min)/2)/temp +Float.parseFloat(min);
	}
	
	
	/**
	 * 
	 * Enum for climate conditions
	 */
	public static enum WeatherConditions {
		Snow,Rain,Sunny,Unknown;
	}
}
