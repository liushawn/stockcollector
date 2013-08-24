package test;


import static org.junit.Assert.*;
//import InternalStockInfoCollector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import func.InternalStockInfoCollector;

public class InternalStockInfoCollectorTest {

	private InternalStockInfoCollector inst_ ; 
	
	@Before
	public void setUp() throws Exception {
		System.out.println("Setting up for InternalStockInfoCollectorTest");
		String fileName = "TargetStocks"; 
		inst_ = new InternalStockInfoCollector();
		inst_.setStockFileName_(fileName);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		String str = "SH55555";
		
		System.out.println("Hello, JUnit " + str.toLowerCase() + "!");
	}

	@Test
	public void testReadTargetStock()
	{
		assertTrue(inst_.readTargetStock()) ; 
	}
	
	@Test
	public void testCollectStockInfo()
	{
		inst_.readTargetStock();
		assertTrue(inst_.collectStockInfo());
	}
	
	@Test
	public void testPersistStockInfo()
	{
		inst_.readTargetStock();
		inst_.collectStockInfo();
		inst_.persistStockInfo();
	}
	
	@Test
	public void testPersistStockDaily()
	{
		inst_.readTargetStock();
		inst_.collectStockInfo();
		inst_.persistStockInfo();
	}
}
