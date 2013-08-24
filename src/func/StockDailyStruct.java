package func;

public class StockDailyStruct {

	/* The return data sequence is: 
	 *  0:  Stock name
	 *  1:  Today open price
	 *  2:  Yesterday close price
	 *  3:  Current real time price, usually 10s delay
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
	private String ticker_;
	private String name_;	
	private double ydayClosePrice_;
	private double todayOpenPrice_;
	private double todayHighestPrice_;
	private double todayLowestPrice_;
	private long   todayTotalTradedShares_;
	private double todayTotalTradedNotional_;
	private String todayDate_;
	private String currentTime_;
	private double currentPrice_;
	
	public String toString(){
		String tmpString = "Stock "+ticker_+ ": [\n" +
							"\t\tName:"+name_+"\n"+
							"\t\tOpen:"+todayOpenPrice_+"\n"+
							"\t\tClose:"+ydayClosePrice_+"\n"+
							"\t\tHighest:"+todayHighestPrice_+"\n"+
							"\t\tLowest:"+todayLowestPrice_+"\n"+
							"\t\tCurrent:"+currentPrice_+"\n"+
							"\t\tDate:"+todayDate_+" Time:"+ currentTime_+"\n\t\t]\n";

		return tmpString ;
	}
	public String getTicker_() {
		return ticker_;
	}
	public void setTicker_(String ticker_) {
		this.ticker_ = ticker_;
	}
	public String getName_() {
		return name_;
	}
	public void setName_(String name_) {
		this.name_ = name_;
	}
	public double getYdatClosePrice_() {
		return ydayClosePrice_;
	}
	public void setYdatClosePrice_(double ydatClosePrice_) {
		this.ydayClosePrice_ = ydatClosePrice_;
	}
	public double getTodayOpenPrice_() {
		return todayOpenPrice_;
	}
	public void setTodayOpenPrice_(double todayOpenPrice_) {
		this.todayOpenPrice_ = todayOpenPrice_;
	}
	public double getTodayHighestPrice_() {
		return todayHighestPrice_;
	}
	public void setTodayHighestPrice_(double todayHighestPrice_) {
		this.todayHighestPrice_ = todayHighestPrice_;
	}
	public double getTodayLowestPrice_() {
		return todayLowestPrice_;
	}
	public void setTodayLowestPrice_(double todayLowestPrice_) {
		this.todayLowestPrice_ = todayLowestPrice_;
	}
	public long getTodayTotalTradedShares_() {
		return todayTotalTradedShares_;
	}
	public void setTodayTotalTradedShares_(long todayTotalTradedShares_) {
		this.todayTotalTradedShares_ = todayTotalTradedShares_;
	}
	public double getTodayTotalTradedNotional_() {
		return todayTotalTradedNotional_;
	}
	public void setTodayTotalTradedNotional_(double todayTotalTradedNotional_) {
		this.todayTotalTradedNotional_ = todayTotalTradedNotional_;
	}
	public String getTodayDate_() {
		return todayDate_;
	}
	public void setTodayDate_(String todayDate_) {
		this.todayDate_ = todayDate_;
	}
	public String getCurrentTime_() {
		return currentTime_;
	}
	public void setCurrentTime_(String currentTime_) {
		this.currentTime_ = currentTime_;
	}
	public double getCurrentPrice_() {
		return currentPrice_;
	}
	public void setCurrentPrice_(double currentPrice_) {
		this.currentPrice_ = currentPrice_;
	}
	
	
}
