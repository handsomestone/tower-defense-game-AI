/**
 * Represents type of tile on the game map.
 */
public enum Ilk {

    /** Unseen tile - status unknown. */
//    UNKNOWN,
    
    /** Water tile. */
 //   WATER,
    
    /** Food tile. */
 //   FOOD,
    
    /** Land tile. */
 //   LAND;
   
        /** empty tile */
    EMPTY,
    
    /** barrier tile. */
    BARRIER,
    
    /** enemy born tile. */
    SENEMY,
    
    /** defend tile. */
    GDEFEND;
	
	
    /**
     * Checks if this type of tile is passable, which means it is not a water tile.
     * 
     * @return <code>true</code> if this is not a water tile, <code>false</code> otherwise
     */
    public boolean isPassable() {
    	if(ordinal() == EMPTY.ordinal())
    	{
			return true;
		}
		return false;
      /*  return ordinal() > WATER.ordinal();*/
    }
   public boolean isDefend(){
	   if(ordinal() == GDEFEND.ordinal())
	   {
		   return true;
	   }
	   return false;
   }
    /**
     * Checks if this type of tile is unoccupied, which means it is a land tile or a dead ant tile.
     * 
     * @return <code>true</code> if this is a land tile or a dead ant tile, <code>false</code>
     *         otherwise
     */
     /*
    public boolean isUnoccupied() {
        return this == LAND;
    }*/
}
