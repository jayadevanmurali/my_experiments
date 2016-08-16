
package com.data.weatherdatagenerator.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Properties;

import com.data.weatherdatagenerator.constants.Constants;

/**
 * This class contains validation methods for the input arguments
 * @author Jayadevan M
 *
 */
public class ValidationUtils {
	
	/**
	 * This method will validate input arguments and initiate data generation
	 * @param args
	 * @throws Exception 
	 */
	public static boolean validateArguments(String args[],Properties prop) throws Exception{
		
		boolean isValid=  true;
		
		if(args.length < 3){
			System.err.println("Invalid number of input arguments, Expecting station "
					+ "code,start date, end date");
			isValid = false;
		}else {
			if (!validateStationCode(args[0],prop)){

				System.err.println("Invalid Station Code, Station code should be "+
				prop.getProperty(Constants.STATION_CODE_LENGTH) +" letter IATA code");
				isValid = false;

			}else if (!validateDateFormat(args[1],args[2],prop)){

				System.err.println("Invalid Date Format, Date should be in "+
				prop.getProperty(Constants.INPUT_DATE_FORMAT) +" format ");
				isValid = false;

			}else if (!validateDate(args[1],args[2],prop)){

				System.err.println("Start Date should be less than or equal to end date");
				isValid = false;
			}
		}
		
		return isValid;
	}
	/**
	 * This method validate input date format
	 * @param args
	 * @param prop
	 * @return
	 */
	public static boolean validateDateFormat(String startDt,String endDt,Properties prop) {

		SimpleDateFormat sdf = new SimpleDateFormat(prop.getProperty(Constants.INPUT_DATE_FORMAT));

		sdf.setLenient(false);

		try {

			sdf.parse(startDt);
			sdf.parse(endDt);

		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * This method validate input date
	 * @param args
	 * @param prop
	 * @return
	 */
	public static boolean validateDate(String startDt,String endDt, Properties prop) {

		SimpleDateFormat sdf = new SimpleDateFormat(prop.getProperty(Constants.INPUT_DATE_FORMAT));

		sdf.setLenient(false);

		try {

			if(sdf.parse(endDt).before(sdf.parse(startDt))){
				return false;
			}


		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * This method validate station code
	 * @param args
	 * @param prop
	 * @return
	 */
	public static boolean validateStationCode(String stationCode,Properties prop) {
		
		Map<String,String> refGeoMap= Utils.loadRefrenceGeodata(prop);

		if(stationCode.length()!=Integer.parseInt(prop.getProperty(Constants.STATION_CODE_LENGTH)))
			return false;
		else if (!refGeoMap.containsKey(stationCode))
			return false;
		else
			return true;
	}
}
