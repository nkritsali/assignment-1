import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Stack;


public class StockSpan {

	/**
	 * @param args
	 */

	public ArrayList<String> simpleStockSpan (ArrayList<String> date, ArrayList<Double> quote) {
//		Input: quote, an array with n stock price quotes
//		Output: span, an array with n stock price spans		

		int k;
        boolean span_end;

		ArrayList<String> span = new ArrayList<String>();
			
		for (int i=0 ; i<date.size() ; i++){
			k=1;
			span_end = false;
			while ((i-k>=0) && !span_end){
				if (quote.get(i-k) <= quote.get(i)){
					k+=1;
				}
				else{
					span_end = true;
				}
			}
			span.add(i, date.get(i)+","+k);
		}

		return span;
	}
	

	
	public ArrayList<String> stackStockSpan (ArrayList<String> date, ArrayList<Double> quote) {
//		Input: quote, an array with n stock price quotes
//		Output: span, an array with n stock price spans		
//		Data: s, a stack
		
		ArrayList<String> span = new ArrayList<String>();
		Stack<Integer> s = new Stack<Integer>();
		
		s.push(0);
		span.add(0, date.get(0)+","+1);
		for (int i=1 ; i<date.size() ; i++){
			while (!s.empty() && quote.get(s.peek()) <= quote.get(i)){
				s.pop();
			}	
			if (s.empty()){
				span.add(i, date.get(i)+","+(i+1));
			}
			else{
				span.add(i, date.get(i)+","+(i-s.peek()));
			}
			s.push(i);
		}
		
		return span;
	}
	
	
	public static void main(String[] args) throws IOException {
        
        if (args.length == 2 || args.length == 4){ 
        	
        	int stockDatesCounter = 0;
        	String scan;
    		StockSpan stockSpan = new StockSpan();

    		ArrayList<String> date = new ArrayList<String>();
    		ArrayList<Double> quote = new ArrayList<Double>();
    		ArrayList<String> spanResults = new ArrayList<String>();

    		FileReader file = new FileReader(args[1]);
            BufferedReader br = new BufferedReader(file);

            while((scan = br.readLine()) != null)
            {
//        		System.out.println(scan);	// in order to check if lines are read in a correct way
            	if (scan.contains("-")){
            		date.add(stockDatesCounter, scan.split(",")[0]); 
            		quote.add(stockDatesCounter, Double.valueOf(scan.split(",")[1])); 
            		stockDatesCounter++;
            	}
            }
            br.close();

        	
        	
			if (args[0].trim().equals("-n")){
				spanResults = stockSpan.simpleStockSpan(date,quote);
			}
			else if (args[0].trim().equals("-s")){
				spanResults = stockSpan.stackStockSpan(date,quote);			
			}
			else if (args[0].trim().equals("-b")){
				long startTime = System.currentTimeMillis();
				for (int i=0 ; i<100 ; i++){
					stockSpan.simpleStockSpan(date,quote);							
				}
				long stopTime = System.currentTimeMillis();
				System.out.println("Naive implementation took: "+(stopTime-startTime)+" millis");
				
				startTime = System.currentTimeMillis();
				for (int i=0 ; i<100 ; i++){
					stockSpan.stackStockSpan(date,quote);							
				}
				stopTime = System.currentTimeMillis();
				System.out.println("Stack implementation took: "+(stopTime-startTime)+" millis");
				
			}
			else {
				System.err.println("The first argument must be '-n','-s' or '-b'. You typed something else. Try again...");
			}
			
			if (args.length==2){
				if (args[0].trim().equals("-n") || args[0].trim().equals("-s")){
					for (int i=0 ; i<spanResults.size() ; i++)
						System.out.println(spanResults.get(i));
				}
			}
			else if (args[2].trim().equals(">")){
			    try {
			        File outputFile = new File(args[3]);
			        FileOutputStream is = new FileOutputStream(outputFile);
			        OutputStreamWriter osw = new OutputStreamWriter(is);    
			        Writer w = new BufferedWriter(osw);
			    	for (int i=0 ; i<spanResults.size() ; i++)
			    		w.write(spanResults.get(i)+"\n");
			        w.close();
			    } catch (IOException e) {
			        System.err.println("Problem writing to the file "+args[3]);
			    }
		        System.out.println("Writing to the file "+args[3]+" is DONE!");
			}
			else {
				System.err.println("You have typed wrong the two last arguments for the output. They should must be ' > output.csv'. Try again...");
			}
		}
		else {			
			System.err.println("THIS PROGRAM WAS DEVELOPPED IN ECLIPSE. IT HAS TWO ARGUMENTS: <TYPE> <CSV FILE>. EDIT IT FROM THE RUN CONFIGURATION");
			System.err.println("You haved typed a wrong number of arguments. You should type something like:\n '-n DJIA.csv' or \n '-n DJIA.csv > output.csv' in the Run Configuration of Eclipse. Try again...");
		}
	}
}
