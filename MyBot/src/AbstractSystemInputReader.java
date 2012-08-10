import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Handles system input stream reading.
 */
public abstract class AbstractSystemInputReader {
    /**
     * Reads system input stream line by line. All characters are converted to lower case and each
     * line is passed for processing to {@link #processLine(String)} method.
     * 
     * @throws IOException if an I/O error occurs
     */
	 private void debug(String format, Object... args) {
	    	
	    	FileOutputStream file;
			try {
				file = new FileOutputStream("debug.txt",true);
			} catch (FileNotFoundException e) {
				
				return;
			}
	    	PrintStream p = new PrintStream(file);
	    	
	    	p.printf(format, args);
	    	try {
				file.close();
			} catch (IOException e) {
			}
	    }
	public void readSystem() throws IOException
	{
		Scanner scan  = new Scanner(System.in);
		String buf=scan.nextLine();
		int map,level;
		boolean  stop = false;
		map = Integer.parseInt(buf);
		for(int i=0;i<map;i++){
			while(true){
				String tmp=scan.nextLine();
				processLineSetup(tmp);
				if(tmp.equals("END")){
					level = Integer.parseInt(buf);
				//	System.out.println("main piu");
					break;
				}else{
					buf=tmp;
				}
			}
			for(int j=0;j<level;j++){
				stop =false;
				while(true){
					buf=scan.nextLine();
					stop = processLineUpdate(buf);
				//	System.out.println("stop"+stop);
					if(stop == true){
						//System.out.println("0");
					//	System.out.println("stop == true");
						break;
					}
				}
			}
		}
	}
	/*
    public void readSystemInput() throws IOException {
        StringBuilder line = new StringBuilder();
        int c;
        while ((c = System.in.read()) >= 0) {
            if (c == '\r' || c == '\n') {
            	debug("%5.5f ", line);
            	processLine(line.toString());
                
                line.setLength(0);
            } else {
                line = line.append((char)c);
            }
        }
    }*/
    
    /**
     * Process a line read out by {@link #readSystemInput()} method in a way defined by subclass
     * implementation.
     * 
     * @param line single, trimmed line of system input
     */
    //public abstract void processLine(String line);
    public abstract boolean processLineUpdate(String line);
    public abstract void processLineSetup(String line) ;
}
