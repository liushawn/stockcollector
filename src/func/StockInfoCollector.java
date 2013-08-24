package func;

public interface StockInfoCollector {

	boolean readTargetStock(); 
	boolean collectStockInfo();
	void persistStockInfo();
	void persistStockDaily();
	void report();
}
