
package com.data.weatherdatagenerator.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.data.weatherdatagenerator.constants.Constants;

/**
 * This class contains helper methods for weather data generation
 * @author Jayadevan M
 *
 */
public class Utils {

	/**
	 * This method load application properties from file - weather.properties
	 * @return properties
	 * @throws Exception 
	 */
	public static Properties loadProperties() throws Exception{

		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(Constants.APPLICATION_PROPERTY_FILE));
		} catch (FileNotFoundException e) {
			throw new Exception("The config file - conf/datagenerator.properties is missing",e.getCause());
		}catch (IOException e) {
			throw new Exception("Failed to read config file - conf/datagenerator.properties",e.getCause());
		}
		return properties;

	}

	/**
	 * This method will load historical weather data into a hashmap. The input file should be a comma delimited text file.
	 * The file should contains following columns station_code,month,min_temperature,max_temperature,min_pressure,max_pressure,
	 * min_humidity,max_humidity
	 * 
	 * The key of the map will be <station_code>_<month>
	 * The value of the map will be <min_temperature>_<max_temperature>_<min_pressure>_<max_pressure>_<min_humidity>_<max_humidity>
	 * 
	 * @param filename
	 * @param prop
	 * @return weatherMap
	 */
	public static Map<String,String> loadRefrenceWetherdata(Properties prop){

		Map<String,String> weatherMap = new HashMap<String,String>();
		BufferedReader br = null;
		String line = "";

		try {

			br = new BufferedReader(new FileReader(prop.getProperty(Constants.WEATHER_REF_FILE_NAME)));
			br.readLine();
			String[] wetherRefData;

			while ((line = br.readLine()) != null) {

				wetherRefData = line.split(prop.getProperty(Constants.WEATHER_REF_FILE_DELIMTER));
				weatherMap.put(generateKey(wetherRefData[0],wetherRefData[1],Constants.WEATHER_MAP_DELIMITER),
						generateValue(wetherRefData,Constants.WEATHER_MAP_DELIMITER));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return weatherMap;

	}

	/**
	 * This method will generate map key.
	 * The key of the map will be <station_code>_<month>
	 * 
	 * @param wetherRefData
	 * @return
	 */
	public static String generateKey(String stationCode,String month, String delimiter) {

		return stationCode+delimiter+month;
	}

	/**
	 * This method will generate map value
	 * The value of the map will be <min_temperature>_<max_temperature>_<min_pressure>_<max_pressure>_<min_humidity>_<max_humidity>
	 * 
	 * @param wetherRefData
	 * @return
	 */
	public static String generateValue(String[] wetherRefData,String delimiter) {

		StringBuilder sb = new StringBuilder();

		sb.append(wetherRefData[2]).append(delimiter);
		sb.append(wetherRefData[3]).append(delimiter);
		sb.append(wetherRefData[4]).append(delimiter);
		sb.append(wetherRefData[5]).append(delimiter);
		sb.append(wetherRefData[6]).append(delimiter);
		sb.append(wetherRefData[6]);

		return sb.toString();
	}

	/**
	 * This method will write the generated weather data into a output file.
	 * 
	 * @param content
	 * @param filename
	 */
	public static boolean writeToFile(String content,String filename){

		FileOutputStream fop = null;
		File file;
		boolean isOK = true;
		try {

			file = new File(filename);
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			fop.write(content.getBytes());
			fop.flush();
			fop.close();

		} catch (IOException e) {
			e.printStackTrace();
			isOK = false;
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				isOK = false;
			}
		}

		return isOK;
	}
	
	/**
	 * This method will load stations geographical details into hasmap. The input file should be a comma delimited text file.
	 * The file should contains following columns station_code,latitude,longitude,height
	 *
	 * 
	 * The key of the map will be <station_code>
	 * The value of the map will be <latitude>,<longitude>,<height>
	 * 
	 * @param filename
	 * @param prop
	 * @return weatherMap
	 */
	public static Map<String,String> loadRefrenceGeodata(Properties prop){

		Map<String,String> geoMap = new HashMap<String,String>();
		BufferedReader br = null;
		String line = "";

		try {

			br = new BufferedReader(new FileReader(prop.getProperty(Constants.GEO_REF_FILE_NAME)));
			br.readLine();
			String[] geoData;
			StringBuilder geoBldr;

			while ((line = br.readLine()) != null) {
				geoBldr = new StringBuilder();
				geoData = line.split(prop.getProperty(Constants.GEO_REF_FILE_DELIMTER));
				geoBldr.append(geoData[1]).append(Constants.GEO_MAP_DELIMITER).append(geoData[2]).
				append(Constants.GEO_MAP_DELIMITER).append(geoData[3]);
				geoMap.put(geoData[0],geoBldr.toString());
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return geoMap;

	}
}
