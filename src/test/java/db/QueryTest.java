package com.advanced.software.engineering.aseproject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import db.Query;


@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryTest {
	
	@Before
	public void setup() {
		
	}
	
	@Test
	public void testCreateSchema() {
		String test1 = "newSchema";
		String result = "CREATE SCHEMA IF NOT EXISTS newSchema";
		String test2= "SchemaNew";
		
		assertTrue(Query.createSchema(test1).equals(result));
		assertFalse(Query.createSchema(test2).equals(result));
	}
	
	@Test
	public void testCreateIndexTable() {
		String test1 = "IndexTable";
		String test2 = "TableIndex";
		String result = "CREATE TABLE IF NOT EXISTS IndexTable(Lookback ARRAY, NameOfDeclaringType varchar(255) NOT NULL, MethodName varchar(255) NOT NULL, ParameterType ARRAY NOT NULL);";
		
		assertTrue(Query.createIndexTable(test1).equals(result));
		assertFalse(Query.createIndexTable(test2).equals(result));
	}

	@Test
	public void testInsertIndexTable() {
		String table = "IndexTable";
		String test1 = "io,except,string;javax.swingJOptionPanel;showMessageDialog;Component,Object";
		String test2 = "io,except;javax.swingJOptionPanel;showMessageDialog;Component,Object";
		String result = "INSERT INTO " + table + " VALUES (('io','except','string'),'javax.swingJOptionPanel','showMessageDialog',('Component','Object'));";
		
		assertTrue(Query.insertIndexTable(test1, table).equals(result));
		assertFalse(Query.insertIndexTable(test2, table).equals(result));
	}
	
	@Test
	public void testUpdateIndexTable() {
		String table = "IndexTable";
		String test1 = "io,except,string;javax.swingJOptionPanel;showMessageDialog;Component,Object;MethodName='getMessage'";
		String test2 = "io,except;javax.swingJOptionPanel;showMessageDialog;Component,Object;MethodName='getMessage'";
		String result = "UPDATE " + table + " SET Lookback=('io','except','string'), NameOfDeclaringType='javax.swingJOptionPanel', MethodName='showMessageDialog', ParameterType=('Component','Object')"
				+ " WHERE MethodName='getMessage';";

		assertTrue(Query.updateIndexTable(test1, table).equals(result));
		assertFalse(Query.updateIndexTable(test2, table).equals(result));
	}
	
	@Test
	public void testDeleteRow() {
		String table = "IndexTable";
		String test1 = "MethodName='getMessage'";
		String test2 = "Lookback=('io','except','string')";
		String result = "DELETE FROM IndexTable WHERE MethodName='getMessage';";

		assertTrue(Query.deleteRow(test1, table).equals(result));
		assertFalse(Query.deleteRow(test2, table).equals(result));
	}
	
	@Test
	public void testDeleteIndexTable() {
		String test1 = "IndexTable";
		String test2 = "TableIndex";
		String result = "DROP TABLE IF EXISTS IndexTable;";
		
		assertTrue(Query.deleteIndexTable(test1).equals(result));
		assertFalse(Query.deleteIndexTable(test2).equals(result));
	}
	
	@Test
	public void testDeleteSchema() {
		String test1 = "newSchema";
		String test2 = "SchemaNew";
		String result = "DROP SCHEMA IF EXISTS newSchema;";

		assertTrue(Query.deleteSchema(test1).equals(result));
		assertFalse(Query.deleteSchema(test2).equals(result));
	}
	
	@Test
	public void testgetIndex() {
		String test1 = "IndexTable";
		String test2 = "TableIndex";
		String result = "SELECT * FROM IndexTable;";

		assertTrue(Query.getIndex(test1).equals(result));
		assertFalse(Query.getIndex(test2).equals(result));
	}
}
