package func;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

	private void storeStockIntoDB(){
		System.out.println("Setting up for InternalStockInfoCollectorTest");
		//String fileName = "ShanghaiAStocks";
		//String fileName = "ShanghaiBStocks"; 
		String fileName = "ShenzhenAStocks"; 
		
		InternalStockInfoCollector inst_ = new InternalStockInfoCollector();
		inst_.setStockFileName_(fileName);
		
		inst_.readTargetStock();
		inst_.collectStockInfo();
		inst_.persistStockInfo();
		inst_.report();
	}
	
	
	private void storeStockDailyToDB(String fileName){

		//String fileName = "TargetStocks"; 
		
		InternalStockInfoCollector inst_ = new InternalStockInfoCollector();
		inst_.setStockFileName_(fileName);
		
		inst_.readTargetStock();
		inst_.collectStockInfo();
		inst_.persistStockDaily();
		inst_.report();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ArrayList<String> stockFiles = new ArrayList<String>();
		stockFiles.add("ShanghaiAStocks");
		stockFiles.add("ShenzhenAStocks");
		
		Main inst = new Main() ;
		for( String file : stockFiles){
			inst.storeStockDailyToDB(file);
		}
	}

}
