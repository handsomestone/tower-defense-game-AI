import java.util.List;

/**
 * Provides basic game state handling.
 */
public abstract class Bot extends AbstractSystemInputParser {
    //private Ants ants;
	private Towers towers;
    
    /**
     * {@inheritDoc}
     */
    @Override
    /*
    public void setup(int loadTime, int turnTime, int rows, int cols, int turns, int viewRadius2,
            int attackRadius2, int spawnRadius2, long seed, int players) {
        setAnts(new Ants(loadTime, turnTime, rows, cols, turns, viewRadius2, attackRadius2,
            spawnRadius2, seed, players));
    }*/

	public void setup(int s_map,int w_map,int h_map,List<String> mapdata,int l_map)
	{
    	//System.out.println("Bot setup in");
		setTowers(new Towers(s_map,w_map,h_map,mapdata,l_map));
	}
    /**
     * Returns game state information.
     * 
     * @return game state information
     */
     /*
    public Ants getAnts() {
        return ants;
    }*/
	public Towers getTowers(){
		return towers;
	}
    
    /**
     * Sets game state information.
     * 
     * @param ants game state information to be set
     */
     /*
    protected void setAnts(Ants ants) {
        this.ants = ants;
    }*/
    protected void setTowers(Towers towers)
    {
		this.towers = towers;
	}
    /**
     * {@inheritDoc}
     */
    @Override
    /*
    public void beforeUpdate() {
        ants.setTurnStartTime(System.currentTimeMillis());
        ants.clearMap();
    }
	*/
	public void beforeUpdate()
	{
		towers.clearMap();
	}
	public void addLifeAndMoney(int life,int money)
	{
		towers.addLifeAndMoney(life,money);
	}

	public void addTower(int Xt,int Yt,int strengthen,int type)
	{
		towers.addTower( strengthen, type,new Tile(Yt,Xt));
	}
	public void addEnemy(int Xe,int Ye,int timeToCome,int lifeOfEnemy,int moveTime)
	{
		towers.addEnemy(timeToCome, lifeOfEnemy,moveTime,new Tile(Ye,Xe));
	}
    /**
     * {@inheritDoc}
     */
     /*
    @Override
    public void addWater(int row, int col) {
        ants.update(Ilk.WATER, new Tile(row, col));
    }
    */
    /**
     * {@inheritDoc}
     */
     /*
    @Override
    public void addAnt(int row, int col, int owner) {
        ants.addAnt(owner, new Tile(row, col));
    }*/
    
    /**
     * {@inheritDoc}
     */
     /*
    @Override
    public void addFood(int row, int col) {
        ants.update(Ilk.FOOD, new Tile(row, col));
    }
    */
    /**
     * {@inheritDoc}
     */
     /*
    @Override
    public void removeAnt(int row, int col, int owner) {
    	ants.removeAnt(owner, new Tile(row, col));
    }
    */
    /**
     * {@inheritDoc}
     */
     /*
    @Override
    public void addHill(int row, int col, int owner) {
        ants.updateHills(owner, new Tile(row, col));
    }
    */
    /**
     * {@inheritDoc}
     */
     /*
    @Override
    public void afterUpdate() {
        ants.setVision();
    }*/
}
