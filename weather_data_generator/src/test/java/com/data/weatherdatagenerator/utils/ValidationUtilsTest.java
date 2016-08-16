package com.data.weatherdatagenerator.utils;

import java.util.Properties;

import junit.framework.TestCase;

public class ValidationUtilsTest extends TestCase {
	
	/**
	 * Test for input arguments
	 * @throws Exception 
	 */
	public void testGenerateData() throws Exception 
	{
		//Test wrong number of input arguments
		Properties prp = Utils.loadProperties();
		String[] args = new String[1];
		boolean result = ValidationUtils.validateArguments(args,prp);

		assertEquals(false, result);

		// Test wrong format of input date
		String[] args1 = new String[3];
		args1[0]="SYD";
		args1[1]="2016-01-01";
		args1[2]="2016-01-01";	 
		result = ValidationUtils.validateArguments(args1,prp);

		assertEquals(false, result);

		//Test end date less than start date
		args1[0]="SYD";
		args1[1]="01/01/2016";
		args1[2]="01/01/2013";	 
		result = ValidationUtils.validateArguments(args1,prp);

		assertEquals(false, result);

		//Test valid method call
		args1[0]="SYD";
		args1[1]="01/01/2016";
		args1[2]="10/01/2016";	 
		result = ValidationUtils.validateArguments(args1,prp);

		assertEquals(true, result);
	}

}
