package com.advanced.software.engineering.aseproject;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import db.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InitializeTest {
	
	private DBConnection dbConnection;
	private Initialize init;
	private String testTable = "testTable";


	@Before
	public void createDatabase() {
		dbConnection = new DBConnection("jdbc:h2:tcp://localhost/~/test","sa","");
		init = new Initialize("test",testTable);
	}
	
	@Test
	public void testInsert() {
		String test1 = "io,except,string;javax.swingJOptionPanel;showMessageDialog;Component,Object";
		String expectedResult = "[io, except, string]-javax.swingJOptionPanel-showMessageDialog-[Component, Object]";
		
		init.insertIntoIndexTable(test1, testTable);

		String result= init.getIndexTable(testTable);
		assertTrue(result.equals(expectedResult));

	}
	
	@Test
	public void testDeleteRow() {
		String insert = "io,except,string;javax.swing;showMessageDialog;Component,Object";
		String test1 = "NameOfDeclaringType='javax.swing'";
		String expectedResult = "";
		
		init.insertIntoIndexTable(insert, testTable);
		init.deleteRow(test1, testTable);
		
		String result= init.getIndexTable(testTable);
		assertTrue(result.equals(expectedResult));
		
	}
	
	@Test
	public void testUpdate() {
		String insert = "io,except,string;javax.swing;showMessageDialog;Component,Object";
		String test1 = "io,except,string;javax.swingJ;showMessageDialog;Component,Object;NameOfDeclaringType='javax.swing'";
		String test2 = "io,except,string;javax.swingJ;showMessage;Component,Object;MethodName='showMessageDialog'";
		String test3 = "io,except,string;javax.swingJ;showMessage;Component;ParameterType=('Component','Object')";
		String test4 = "io,string;javax.swingJ;showMessage;Component;Lookback=('io','except','string')";
		String expectedResult1= "[io, except, string]-javax.swingJ-showMessageDialog-[Component, Object]";
		String expectedResult2= "[io, except, string]-javax.swingJ-showMessage-[Component, Object]";
		String expectedResult3= "[io, except, string]-javax.swingJ-showMessage-[Component]";
		String expectedResult4= "[io, string]-javax.swingJ-showMessage-[Component]";
		String result = "";
		
		init.insertIntoIndexTable(insert, testTable);
		
		init.updateIndexTable(test1, testTable);
		result= init.getIndexTable(testTable);
		assertTrue(result.equals(expectedResult1));
		
		init.updateIndexTable(test2, testTable);
		result= init.getIndexTable(testTable);
		assertTrue(result.equals(expectedResult2));
		
		init.updateIndexTable(test3, testTable);
		result= init.getIndexTable(testTable);
		assertTrue(result.equals(expectedResult3));
		
		init.updateIndexTable(test4, testTable);
		result= init.getIndexTable(testTable);
		assertTrue(result.equals(expectedResult4));

	}
	

	@After
	public void shutdownDatabase() {
		init.deleteIndexTable(testTable);
		init.deleteSchema("test");
		dbConnection.closeConnection();
	}

}