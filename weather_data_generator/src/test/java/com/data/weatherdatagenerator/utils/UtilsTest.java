package com.data.weatherdatagenerator.utils;

import java.util.Map;
import java.util.Properties;
import java.io.File;

import com.data.weatherdatagenerator.utils.Utils;

import junit.framework.TestCase;
/**
 * Test case for Utils class
 * @author Jayadevan M
 *
 */
public class UtilsTest extends TestCase {

	/**
	 * Test for possible keys in weather reference data map. This method will assert various
	 * combinations of key
	 * @throws Exception 
	 */
	public void testKeyLoadRefrenceWeatherdata() throws Exception
	{
		Properties prp = Utils.loadProperties();
		Map<String,String> refDataMap = Utils.loadRefrenceWetherdata(prp);
		assertTrue(refDataMap.containsKey("SYD_10"));
		assertFalse(refDataMap.containsKey("SYD|15"));
		assertFalse(refDataMap.containsKey("SYD_15"));

	}

	/**
	 * Test for possible values in weather reference data map. This method will assert various
	 * combinations of value
	 * @throws Exception 
	 */
	public void testValueLoadRefrenceWeatherdata() throws Exception
	{
		Properties prp = Utils.loadProperties();
		Map<String,String> refDataMap = Utils.loadRefrenceWetherdata(prp);
		assertTrue(refDataMap.get("SYD_10").split("_").length==6);
		assertTrue(refDataMap.get("SYD|15")==null);
		assertTrue(refDataMap.get("SYD_15")==null);

	}

	/**
	 * Test for possible keys in geo reference data map. This method will assert various
	 * combinations of key
	 * @throws Exception 
	 */
	public void testKeyLoadRefrenceGeodata() throws Exception
	{
		Properties prp = Utils.loadProperties();
		Map<String,String> refDataMap = Utils.loadRefrenceGeodata(prp);
		assertTrue(refDataMap.containsKey("ADL"));
		assertFalse(refDataMap.containsKey("xxxxx"));
	}

	/**
	 * Test for possible value in geo reference data map. This method will assert various
	 * combinations of value
	 * @throws Exception 
	 */
	public void testValueLoadRefrenceGeodata() throws Exception
	{
		Properties prp = Utils.loadProperties();
		Map<String,String> refDataMap = Utils.loadRefrenceGeodata(prp);
		assertTrue(refDataMap.get("ADL").split(",").length==3);
		assertTrue(refDataMap.get("xxxxx") == null);

	}

	/**
	 * Test for writetoFile method
	 */
	public void testWriteToFile()
	{
		String message = "Hello World";
		//Blank file path
		assertFalse(Utils.writeToFile(message ,""));

		//Wrong path
		assertFalse(Utils.writeToFile(message ,"x://file.csv"));

		//Valid file
		assertTrue(Utils.writeToFile(message ,"file.csv"));
		
		new File("file.csv").delete();
	}

}
