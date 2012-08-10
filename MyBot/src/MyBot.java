import java.io.IOException;
import java.util.Random;

/**
 * Starter bot implementation.
 */
public class MyBot extends Bot {
    /**
     * Main method executed by the game engine for starting the bot.
     * 
     * @param args command line arguments
     * 
     * @throws IOException if an I/O error occurs
     */
    public static void main(String[] args) throws IOException {
        new MyBot().readSystem();
        
      
    }
    
    /**
     * For every ant check every direction in fixed order (N, E, S, W) and move it if the tile is
     * passable.
     */
    @Override
    public void doTurn() {
       // Ants ants = getAnts();
       // ants.explore();
    /*	System.out.println("7");
		System.out.println("8 1 0 0");
		System.out.println("8 3 0 0");
		System.out.println("7 1 0 0");
		System.out.println("6 1 0 0");
		System.out.println("6 2 0 0");
		System.out.println("6 3 0 0");
		System.out.println("7 3 0 0");*/
    	//System.out.println("doTurn");
      Towers towers = getTowers();
	  towers.explore();
    }
}
