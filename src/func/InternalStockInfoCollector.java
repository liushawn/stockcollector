package func;
import java.io.BufferedReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.*;


public class InternalStockInfoCollector implements StockInfoCollector {

	private String stockFileName_;
	private ArrayList<String> stocks_;
	private ArrayList<String> errorStocks_;
	private ArrayList<StockDailyStruct> stockInfoStore_;
	private String market_;
	private Connection conn_; 
	private Statement stmt_ ;
	private CallableStatement cs_ ; 

	public InternalStockInfoCollector(){
		stockFileName_ ="";
		stocks_ = new ArrayList<String>();
		errorStocks_ = new ArrayList<String>(); 
		stockInfoStore_ = new ArrayList<StockDailyStruct>();
		
		if(!initDB()){
			System.out.println("Couldn't initiate DB connection! Exiting ...");
			System.exit(1);
		}
	}

	public String getStockFileName_() {
		return stockFileName_;
	}

	public void setStockFileName_(String stockFileName_) {
		this.stockFileName_ = stockFileName_;
	}
	
	public void setMarket_(String market_) {
		this.market_ = market_;
	}
	
	public String getMarket_() {
		return this.market_ ; 
	}

	/**
	 * read the target stock from file into internal arraylist
	 * return true if succeed
	 */
	@Override
	public boolean readTargetStock() {
		File inputFile = new File(this.stockFileName_); 
		if(!inputFile.exists() || !inputFile.canRead() || inputFile.isDirectory()) return false; 
		
		BufferedReader reader=null;
		try 
		{
			reader = new BufferedReader(new FileReader(inputFile));
			String line=null;
			while( (line=reader.readLine())!= null)
			{
				line= line.toLowerCase();
				System.out.println("Read : " +line);
				this.stocks_.add(line);				
			}		
			reader.close();
		} catch (FileNotFoundException e){			
			e.printStackTrace();
			return false;
		} catch (IOException e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Collect the stock info from Sina interface and store it into StockInfoStore
	 * Return true if it succeed and no Exception
	 */
	@Override
	public boolean collectStockInfo() {
		String httpReq="http://hq.sinajs.cn/list=";
		boolean retStatus = true;
		for( String code : this.stocks_ )
		{
			try {
				HttpURLConnection conn = (HttpURLConnection)(new URL(httpReq + code.toLowerCase())).openConnection();
				conn.connect();
				BufferedReader reader =  new BufferedReader(new InputStreamReader((InputStream)conn.getContent()));
				
				String line;
				StockDailyStruct tmpStruct = new StockDailyStruct(); 
				tmpStruct.setTicker_(code);
				while( (line=reader.readLine()) !=null)
				{
					if(parseQueryReturn(line , tmpStruct)){
						System.out.println("Parsing output for ticker: " + code);
						this.stockInfoStore_.add(tmpStruct);						
					}else{
						System.out.println("Error happened during parse of internet return");
					}
				}
			} catch (IOException e) {				
				e.printStackTrace();
				retStatus= false;
			}		
		}
			
		return retStatus;
	}

	/**
	 * Till this point , the stock info is already stored into stockStore.
	 * Persist the stockStore info into DB stock table 
	 */
	@Override
	public void persistStockInfo() {
		String sql = "{call sp_store_stock_info (?,?,?,?)}" ;
		try{
			for( StockDailyStruct struct : this.stockInfoStore_)
			{
				cs_ = conn_.prepareCall(sql);
				int retValue=-1 ; 
				String ticker = struct.getTicker_() ; 
				System.out.println("Ticker is:" + ticker);
				//Set the market info based on the ticker
				if( ticker.startsWith("sh"))
				{
					cs_.setString(1, ticker);
					cs_.setString(2, "SH");
				}else if(ticker.startsWith("sz"))
				{
					cs_.setString(1, ticker);
					cs_.setString(2, "SZ");
				}
				cs_.setString(3, struct.getName_());
				retValue = cs_.getInt(4);
				cs_.execute();
				cs_.close();
				if(retValue != 0){
					errorStocks_.add(struct.getTicker_());
					System.out.println("Error Happened During Stock Info Persistence");
				}else{
					System.out.println("Stock: "+ struct.getTicker_() + " is persisted!");
				}
			}
		}catch(Exception e){
			System.out.println("DB Exception for persistStockInfo");
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Persist the stockStore info into DB tables 
	 */
	@Override
	public void persistStockDaily() {
		String sql = "{call sp_store_stock_daily (?,?,?,?,?,?,?,?,?)}" ;
		try{
			for( StockDailyStruct struct : this.stockInfoStore_)
			{
				cs_ = conn_.prepareCall(sql);
				int retValue=-1 ; 
				String ticker = struct.getTicker_() ; 
				System.out.println("Ticker is:" + ticker);
				cs_.setString(1, ticker);
				cs_.setDouble(2, struct.getCurrentPrice_());
				cs_.setDouble(3, struct.getTodayOpenPrice_());
				cs_.setDouble(4, struct.getYdatClosePrice_());
				cs_.setDouble(5, struct.getTodayHighestPrice_());
				cs_.setDouble(6, struct.getTodayLowestPrice_());
				cs_.setLong(7, struct.getTodayTotalTradedShares_());
				cs_.setDouble(8, struct.getTodayTotalTradedNotional_());
				cs_.registerOutParameter(9, java.sql.Types.INTEGER);
				System.out.println(cs_);
				cs_.executeUpdate();
				retValue =cs_.getInt(9);
				cs_.close();
				if(retValue != 0){
					errorStocks_.add(struct.getTicker_());
					System.out.println("Error Happened During Stock Daily Price Persistence");
				}else{
					System.out.println("Stock: "+ struct.getTicker_() + " is persisted !");
				}
			}
		}catch(Exception e){
			System.out.println("DB Exception for persistStockInfo");
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	@Override
	public void report() {
		System.out.println("Error Stock Report:");
		for(String tmp : errorStocks_)
		{
			System.out.println("Ticker: " + tmp);
		}
	}
	
	private boolean initDB() 
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/test";
			conn_ = DriverManager.getConnection(url, "root", "root");
			stmt_ = conn_.createStatement();
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true; 		
	}

	/**
	 * Parse the line returned from Sina interface into daily data struct
	 * @param line
	 * @param stockStruct to carry over the info
	 * @return false: when the stock has no return from Sina interface or the format of the return is not expected
	 * @return true: all the return are expected and set the stockStruct properly
	 */
	private boolean parseQueryReturn(String line, StockDailyStruct stockStruct)
	{
		/* The return data sequence is: 
		 *  0:  Stock name
		 *  1:  Today open price
		 *  2:  Yesterday close price
		 *  3:  Current real time price, usually several 10s delay
		 *  4:  Today highest price, as of now
		 *  5:  Today lowest price, as of now
		 *  6:  Best bid price
		 *  7:  Best ask price
		 *  8:  Today's exchanged share number, normally should divided by 100 as that's the lotsize for shares
		 *  9:  Notional value of today's exchange shares: share number x deal price
		 *  10: Bid 1 share number
		 *  11: Bid 1 share price : should be same with #6
		 *  12: Bid 2 share number
		 *  13: Bid 2 share price
		 *  14: Bid 3 share number 
		 *  15: Bid 3 share price
		 *  16: Bid 4 share number
		 *  17: Bid 4 share price
		 *  18: Bid 5 share number
		 *  19: Bid 5 share price
		 *  20: Ask 1 share number
		 *  21: Ask 1 share price : should be same with #7
		 *  22: Ask 2 share number
		 *  23: Ask 2 share price
		 *  24: Ask 3 share number 
		 *  25: Ask 3 share price
		 *  26: Ask 4 share number
		 *  27: Ask 4 share price
		 *  28: Ask 5 share number
		 *  29: Ask 5 share price
		 *  30: Today's Date
		 *  31: Current Time
		 *  32: Unknown, normally 00
		 */
		Pattern p = Pattern.compile("\"(.+)\"");		
		Matcher matcher=p.matcher(line);
		if(matcher.find())
		{
			String[] arr = matcher.group(1).split(",");
			try{
				stockStruct.setName_(arr[0]);
				stockStruct.setTodayOpenPrice_(Double.parseDouble(arr[1]));
				stockStruct.setYdatClosePrice_(Double.parseDouble(arr[2]));
				stockStruct.setCurrentPrice_(Double.parseDouble(arr[3]));
				stockStruct.setTodayHighestPrice_(Double.parseDouble(arr[4]));
				stockStruct.setTodayLowestPrice_(Double.parseDouble(arr[5]));
				stockStruct.setTodayTotalTradedShares_(Long.parseLong(arr[8]));
				stockStruct.setTodayTotalTradedNotional_(Double.parseDouble(arr[9]));
				stockStruct.setTodayDate_(arr[30]);
				stockStruct.setCurrentTime_(arr[31]);
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
		}else{
			System.out.println("No such stock defined in the market: "+stockStruct.getTicker_());
			errorStocks_.add(stockStruct.getTicker_());
			return false;
		}
		return true;
	}
	
	public static void main(String[] args)
	{
		
	}

}
