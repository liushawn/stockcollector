package func;

import java.io.*;

public class FileParser {

	public static void main(String[] args) {
		String filename= "ShenzhenAStocks";
		BufferedReader reader=null;
		String outputfile = filename+".new";		
		try 
		{
			PrintWriter writer = new PrintWriter(outputfile, "UTF-8");
			reader = new BufferedReader(new FileReader(filename));
			String line=null;
			while( (line=reader.readLine())!= null)
			{
				String[] tmpArr = line.split("\t");
				writer.println("sz"+tmpArr[0].trim());
			}
			System.out.println("Done!");
			reader.close();
			writer.close();
		} catch (FileNotFoundException e){			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}

}
