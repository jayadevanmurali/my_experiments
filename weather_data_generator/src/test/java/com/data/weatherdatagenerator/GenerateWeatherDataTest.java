package com.data.weatherdatagenerator;

import java.text.SimpleDateFormat;
import java.util.Properties;

import junit.framework.TestCase;

import com.data.weatherdatagenerator.constants.Constants;
import com.data.weatherdatagenerator.utils.Utils;
/**
 * Test case for GenerateWeatherData
 * @author Jayadevan M
 *
 */
public class GenerateWeatherDataTest extends TestCase {

	/**
	 * Test for generate weather data & content of generated data
	 * @throws Exception 
	 */
	public void testGenerateWeatherData() throws Exception
	{
		Properties prop = Utils.loadProperties();
		String resultWeatherData =GenerateWeatherData.generateWeatherData("SYD", "01/01/2016", "02/01/2016", prop);

		assertTrue(resultWeatherData.split("\n").length==3);

		String resultStringArray[] =resultWeatherData.split("\n");

		String row = resultStringArray[0];
		
		assertTrue(row.split("\\|").length==7);

	}
	
	/**
	 * Test for get weather conditions
	 * @throws Exception
	 */
	public void testGetCondition() throws Exception
	{
		Properties prop = Utils.loadProperties();
		assertTrue(GenerateWeatherData.getCondition(21.0F, prop).equalsIgnoreCase("sunny"));
		assertFalse(GenerateWeatherData.getCondition(21.0F, prop).equalsIgnoreCase("Rain"));
		assertTrue(GenerateWeatherData.getCondition(18.0F, prop).equalsIgnoreCase("Rain"));
		assertTrue(GenerateWeatherData.getCondition(05.0F, prop).equalsIgnoreCase("Snow"));
		assertTrue(GenerateWeatherData.getCondition(50.0F, prop).equalsIgnoreCase("Unknown"));
	}
	
	/**
	 * Test for parse date
	 * @throws Exception
	 */
	public void testParseDate() throws Exception
	{
		Properties prop = Utils.loadProperties();
		Exception exp = new Exception();
		
		assertTrue(GenerateWeatherData.parseDate("02/04/2016", new SimpleDateFormat(prop.getProperty(Constants.INPUT_DATE_FORMAT)))
				instanceof java.util.Calendar);
		
		try{
			GenerateWeatherData.parseDate("30-30-16", new SimpleDateFormat(prop.getProperty(Constants.INPUT_DATE_FORMAT)));
		}catch (Exception e){
			exp=e;
		}
		assertTrue(exp.getMessage().contains("Failed to parse date-"));
	}

}
