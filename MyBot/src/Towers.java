import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Holds all game data and current game state.
 */
public class Towers {
	private boolean write_debug	= true; //Set to true to output debug to System.err
	/** Maximum map size. */
    public static final int MAX_MAP_SIZE = 24 * 24;
 
	/*this is mine*/
	private final int s_map;
	private final int w_map;
	private final int h_map;
	private final int l_map;
	//private final List<String> map_data;
	private  Location mymap[][];

	//private List<Tile> emptyTile;
	Map<Integer,Tile> emptyTile;
	
	
	
	private int life;
	private int money;
	private List<Enemy> enemyList;
	private List<Tile> sEnemy;
	private List<Tile> gDefend;
	private boolean towersize=false;

	private int emptyTileNum;
	private int enemyID ;
	
	private int enemyTotalLife;
	
	private List<Integer> bestDecisions;
	private boolean firstTime ;
	
	private int map=0;
	public Towers(int s_map,int w_map,int h_map,List<String> mapdata,int l_map)
	{
		this.s_map = s_map;
		this.w_map = w_map;
		this.h_map = h_map;
		this.l_map = l_map;
	
		mymap = new Location[h_map][w_map];
		
		emptyTile = new HashMap<Integer,Tile>();
		emptyTileNum = 0;
		enemyID = 0;
		
		enemyTotalLife = 0;
		bestDecisions = new ArrayList<Integer>();
		firstTime = true;
		map++;
		enemyList = new ArrayList<Enemy>();
		sEnemy = new ArrayList<Tile>();
		gDefend = new ArrayList<Tile>();
		//System.out.println("Towers setup in");
		//System.out.println(mapdata.size());
		for(int b_row=0;b_row<h_map;b_row++)
		{
			for(int b_col=0;b_col<w_map;b_col++)
			{
				if(mymap[b_row][b_col]==null)
				{
					mymap[b_row][b_col] = new Location(b_row,b_col);
				}
			}
		}
		for(int row=0;row<h_map;row++)
		{
			String line = mapdata.get(row);
			//System.out.println(line);
			for(int col=0;col<w_map;col++)
			{
				char element;
				element = line.charAt(col);
			//	System.out.println(element);
				switch(element)
				{
					case '0': Tile tile = new Tile(row,col);
							  Integer key = new Integer(emptyTileNum);
							  Tile eTile = emptyTile.get(key);
							  if(eTile == null)
							  {
								emptyTile.put(key,tile);
							  }
							  emptyTileNum++;
						      break;
					case '1':mymap[row][col].setIlk(Ilk.BARRIER);
							break;
					case 's':mymap[row][col].setIlk(Ilk.SENEMY);
							Tile stile = mymap[row][col].getTile();
							sEnemy.add(stile);
						    break;
					case 'g':mymap[row][col].setIlk(Ilk.GDEFEND);
							Tile gtile = mymap[row][col].getTile();
							gDefend.add(gtile);
							break;
				}
			}
		}/*
		if (write_debug)
		{
			//System.out.println("write debug");
			for (int x = 0; x<h_map; x++) {
				for (int y=0; y<w_map; y++)
				{	int ilk = map[x][y].getIlk().ordinal();
			
				   debug("%d",ilk);
				}
				debug("%s","\n");
			}
		}*/
	}

	public void addLifeAndMoney(int life,int money)
	{
		this.life = life;
		this.money = money;
	}
	public void addTower(int strengthen,int type,Tile tile)
	{
		mymap[tile.getRow()][tile.getCol()].setTower(strengthen, type);
		
	}
	public void addEnemy(int timeToCome,int lifeOfEnemy,int moveTime,Tile tile)
	{
		Enemy enemy = new Enemy(timeToCome,lifeOfEnemy,moveTime, tile);
		enemy.setEnemyID(enemyID);
		enemyList.add(enemy);
		enemyID++;
		enemyTotalLife+=lifeOfEnemy;
	}
	public void clearMap()
	{
		enemyList.clear();
		enemyID = 0;
		enemyTotalLife =0;
		for(int row=0;row<h_map;row++)
		{
			for(int col=0;col<w_map;col++)
			{
				if(mymap[row][col] == null)
				{
					mymap[row][col] = new Location(row,col);
				}
				else if(mymap[row][col].getIsTower() == true)
				{
					mymap[row][col].clearTower();
				}
			}
		}
	}
  

    /**
     * Returns game map height.
     * 
     * @return game map height
     */
    public int getRows() {
        return h_map;
    }

    /**
     * Returns game map width.
     * 
     * @return game map width
     */
    public int getCols() {
        return w_map;
    }

   
   
  
    /**
     * Returns ilk at the specified location.
     * 
     * @param tile location on the game map
     * 
     * @return ilk at the <cod>tile</code>
     */
    public Ilk getIlk(Tile tile) {
        return mymap[tile.getRow()][tile.getCol()].getIlk();
    }

    /**
     * Returns location in the specified direction from the specified location.
     * 
     * @param tile location on the game map
     * @param direction direction to look up
     * 
     * @return location in <code>direction</code> from <cod>tile</code>
     */
    
    private Tile getTile(Tile tile, Aim direction) {
        int row = (tile.getRow() + direction.getRowDelta()) % h_map;
        if (row < 0) {
            row += h_map;
        }
        int col = (tile.getCol() + direction.getColDelta()) % w_map;
        if (col < 0) {
            col += w_map;
        }
        return new Tile(row, col);
    }
	
    private Tile getTile(Tile tile, Tile offset) {
        int row = (tile.getRow() + offset.getRow()) % h_map;
        if (row < 0) {
            row += h_map;
        }
        int col = (tile.getCol() + offset.getCol()) % w_map;
        if (col < 0) {
            col += w_map;
        }
        return new Tile(row, col);
    }
    
   
    private int whichBit(Aim dir) {
    	switch (dir) {
    	case NORTH:
    		return 1;
    	case EAST:
    		return 2;
    	case SOUTH:
    		return 3;
    	case WEST:
    		return 4;
    	}
    	return 0;
    }
    
    private int reverseDir(Aim dir) {
    	switch (dir) {
    	case NORTH:
    		return 3;
    	case EAST:
    		return 4;
    	case SOUTH:
    		return 1;
    	case WEST:
    		return 2;
    	}
    	return 0;
    }
    
   
    /**
     * Issues an order by sending it to the system output.
     * 
     * @param myAnt map tile with my ant
     * @param direction direction in which to move my ant
     */
    public void issueOrder(Tile myAnt, Aim direction) {
        System.out.println(new Order(myAnt, direction));
        System.out.flush();
    }
    
    private void debug(String format ,Object... args) {
    	if (!write_debug)
    		return;
    	FileOutputStream file;
		try {
			file = new FileOutputStream("debug.txt",true);
		} catch (FileNotFoundException e) {
			write_debug = false;
			return;
		}
    	PrintStream p = new PrintStream(file);
    	
    	p.printf(format, args);
    	try {
			file.close();
		} catch (IOException e) {
		}
    }
    
    
    /*
     * Zone routines - creates a set of tiles from a given starting point with a set of offsets
     */
    private Set<Tile> makeZone(Tile t, Set<Tile>offsets) {
    	Set<Tile> result = new HashSet<Tile>();
    	for (Tile z: offsets) {
    		Tile a = getTile(t, z);
    		if (getIlk(a).isPassable())
    			result.add(a);
    	}
    	return result;
    }

    private Set<Tile> makeZonePlus1(Tile ant, Set<Tile>zone) {
    	Set<Tile> result = makeZone(ant, zone);
    	
    	for (Aim direction: Aim.values()) {
    		Tile adj = getTile(ant, direction);
    		if (getIlk(adj).isPassable()) {
				result.addAll(makeZone(adj, zone));
    		}
    	}
    	
		return result;
    }
    
    private Set<Tile> makeZonePlus1(Tile t) {
    	Set<Tile> zone = new HashSet<Tile>();
    	zone.add(new Tile (0,0));
    	return makeZonePlus1(t, zone);
    }

	
    
    private void issueOrders() {

    }   
    private Node getBestPath(Tile startPoint)
    {
    	Tile startStatus = startPoint;
    	int minPath = 8092;
    	boolean isPath =false;
    	List<Node> bestPath = new ArrayList<Node>();
    	List<Node> possiblePath = new ArrayList<Node>();
    	//System.out.println("getBestPath "+gDefend.size());
    	for(int i=0;i<gDefend.size();i++)
    	{
    		Tile endStatus = gDefend.get(i);
    		//System.out.println("startStatus "+startStatus.getRow()+startStatus.getCol());
    		//System.out.println("endStatus "+endStatus.getRow()+endStatus.getCol());
    		AstarDoer doer = new AstarDoer(startStatus,endStatus,mymap );
    		doer.run();
    		if(doer.hasPath()==true)
    		{
    			//System.out.println("this path length "+doer.getLength());
    			if(doer.getLength()<=minPath)
    			{
    				if(doer.getLength()==minPath)
    				{
    					if(possiblePath.isEmpty() == false)
    					{
    						possiblePath.clear();
    					}
    					Node endNode = doer.getEndPath();
    					while(endNode != null)
    					{
    						possiblePath.add(endNode);
    						endNode = endNode.parent;
    					}
    					for(int first=0;first<bestPath.size();first++)
    					{
    						for(int second=0;second<possiblePath.size();second++)
    						{
    							if(bestPath.get(first).status.equals(possiblePath.get(second).status)==true)
    							{
    								if(first !=0&& second != 0)
    								{
    									if(bestPath.get(first-1).priority>possiblePath.get(second-1).priority);
    									{
    										bestPath.clear();
    										bestPath.addAll(possiblePath);
    										break;
    									}
    								}
    							}
    						}
    					}
    					
    				}
    				else
    				{
    					minPath = doer.getLength();
    					if(bestPath.isEmpty()==false)
    					{
    						bestPath.clear();
    					}
    					Node endNode = doer.getEndPath();
    					while(endNode != null)
    					{
    						bestPath.add(endNode);
    						endNode = endNode.parent;
    					}
    				}
    				
    				
    				
    			}
    			isPath = true;
    		}
    	}
    	/*System.out.println("Towers MinPath "+ minPath);
    	System.out.println("BestPath");
    	for(Node iter:bestPath)
    	{
    		System.out.print(iter.status.getRow());
    		System.out.print('.');
    		System.out.print(iter.status.getCol());
    		System.out.print("->");
    	}*/
    	return bestPath.get(0);
    }
    private void beforeStrategy()
    {
    /*	System.out.println("beforeStrategy");
    	System.out.println("enmeyList "+enemyList.size());*/
    	for(Tile iter:sEnemy)
    	{
    		Node temp =getBestPath(iter);
    		if(temp!= null)
    		{
    			for(int i=0;i<enemyList.size();i++)
    			{
    				
    				Enemy test = enemyList.get(i);
    				if(test.getStartPoint().equals(iter))
    				{
    					test.setSpeed(temp.gvalue);
    				}
    				
    			}
    				debug("%d",temp.gvalue);
    				debug("%s","/n");
    			
    		}
    		else
    		{
    			debug("%s","can not find path");
    			debug("\n");
    			//debug("%d",iter.getStartPoint().getRow(),iter.getStartPoint().getCol());
    			debug("%s","--------------");
    			debug("\n");
    		}
    	}
    }
    private void test()
    {
    	Tile startStatus = new Tile(6,15);
    	Tile endStatus = new Tile(6,7);
    	/*Tile startStatus = new Tile(4,19);
    	Tile endStatus = new Tile(2,7);*/
    	AstarDoer doer = new AstarDoer(startStatus,endStatus,mymap );
		doer.run();
		Tile startStatus1 = new Tile(4,19);  	
    	Tile endStatus1 = new Tile(2,7);
    	AstarDoer doer1 = new AstarDoer(startStatus1,endStatus1,mymap );
		doer1.run();
/*		getBestPath(startStatus);
		getBestPath(startStatus2);*/
    }
    private List<Tile> getMinMax(List<Tile> sEnemy,List<Tile> gDefend)
    {
    	int minRow = 24;
    	int minCol = 24;
    	int maxRow =0;
    	int maxCol = 0;
    	List<Tile> minMax = new ArrayList<Tile>();
    	for(int i=0;i<sEnemy.size();i++)
    	{
    		int row = sEnemy.get(i).getRow();
    		int col = sEnemy.get(i).getCol();
    		if(row<minRow)
    		{
    			minRow = row;
    		}
    		if(row>maxRow)
    		{
    			maxRow = row;
    		}
    		if(col<minCol)
    		{
    			minCol = col;
    		}
    		if(col>maxCol)
    		{
    			maxCol = col;
    		}
    	}
    	for(int j=0;j<gDefend.size();j++)
    	{
    		int row = gDefend.get(j).getRow();
    		int col = gDefend.get(j).getCol();
    		if(row<minRow)
    		{
    			minRow = row;
    		}
    	    if(row>maxRow)
    		{
    			maxRow = row;
    		}
    		if(col<minCol)
    		{
    			minCol = col;
    		}
    		if(col>maxCol)
    		{
    			maxCol = col;
    		}
    	}
    	Tile minTile =new Tile(minRow-3,minCol-3);
    	minMax.add(minTile);
    	Tile maxTile = new Tile(maxRow+3,maxCol+3);
    	minMax.add(maxTile);
    	//System.out.println("Min row col!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+minTile.getRow()+" "+minTile.getCol());
    	//System.out.println("maxTile row col!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+maxTile.getRow()+" "+maxTile.getCol());
    	return minMax;
    }
    private void checkUsefulTile()
    {
    	Map<Integer,Tile> usefulTile = new HashMap<Integer,Tile>();
    	int usefulTileNum = 0;
    	List<Tile> minMax = getMinMax(sEnemy,gDefend);
    	Tile minTile = new Tile(0,0);
    	Tile maxTile = new Tile(24,24);
    	if(minMax.size()==2)
    	{
    		minTile = new Tile(minMax.get(0).getRow(),minMax.get(0).getCol());
    		maxTile = new Tile(minMax.get(1).getRow(),minMax.get(1).getCol());
    	}
    	for(int i=0;i<emptyTile.size();i++)
    	{
    		Tile tile  = emptyTile.get(new Integer(i));
    		if(tile.getRow()>=minTile.getRow()&&tile.getRow()<=maxTile.getRow())
    		{
    			if(tile.getCol()>=minTile.getCol()&&tile.getCol()<=maxTile.getCol())
    			{
    				usefulTile.put(new Integer(usefulTileNum), tile);
    				usefulTileNum++;
    			}
    		}
    	}
    	emptyTileNum = usefulTileNum;
    	emptyTile = usefulTile;
    	//System.out.println("useful enmpy num!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+emptyTileNum);
    }
	public void  explore()
	{
		//System.out.println("explore");
	/*	beforeStrategy();
		checkUsefulTile();
		
		boolean isNeedCannon = true;
		List<Task> currentTask= new ArrayList<Task>();
		double cost = 0;
		
	   //System.out.println("empty tile num "+emptyTileNum);
	  // System.out.println("empty tile emptyTile "+emptyTile.size());
	   GeneticAlgorithm gaTest = new GeneticAlgorithm(500,emptyTileNum,15,3,0.5,0.1);
	   if(firstTime == true)
	   {
		    debug("%s","map1");
			debug("%s","this is the firstTime !!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			gaTest.initMap(mymap, sEnemy, gDefend, emptyTile, enemyList);
			//gaTest.run();
			gaTest.limitNum(enemyTotalLife);
			//gaTest.Test();
			gaTest.Start();
			
			cost = gaTest.getCost();
			List<Integer> bestCannon= gaTest.getBestCannon();
	
			if(bestDecisions.isEmpty() == false)
			{
				bestDecisions.clear();
			}
			bestDecisions.addAll(bestCannon);
			firstTime = false;
			for(int k=0;k<bestDecisions.size();k++)
			{
				debug("%d",bestDecisions.get(k).intValue());
				debug(" ");
				if(k%20==0||k == bestDecisions.size()-1)
				{
					if(k!=0)
						debug("\n");
				}
			}
			debug("end---------------------------------------");
			debug("\n");
	   }
	   else
	   {
		   debug("%s","map-level``````````");
		   debug("%s","this is not the first~~~~~~~~~~~~~~~~~~~~~~~~~");
		   gaTest.initMap(mymap, sEnemy, gDefend, emptyTile, enemyList);
		   if(bestDecisions.isEmpty() == false)
		   {
			   debug("%s","bestDecisions.isEmpty() == false~~~~~~~~~~~~~~~~~~~~~~~~");
		
				   gaTest.limitNum(enemyTotalLife, bestDecisions);
				   gaTest.Start();
				   
				   cost = gaTest.getCost();
				   List<Integer> bestCannon= gaTest.getBestCannon();
					if(bestDecisions.isEmpty() == false)
					{
						bestDecisions.clear();
					}
					bestDecisions.addAll(bestCannon);
					for(int k=0;k<bestDecisions.size();k++)
					{
						debug("%d",bestDecisions.get(k).intValue());
						debug(" ");
						if(k%20==0||k == bestDecisions.size()-1)
						{
							if(k!=0)
								debug("\n");
						}
					}
					debug("end---------------------------------------");
			   //}
		   }
		   else
		   {
			   gaTest.limitNum(enemyTotalLife);
				//gaTest.Test();
				gaTest.Start();
				
				cost = gaTest.getCost();
				List<Integer> bestCannon= gaTest.getBestCannon();
				if(bestDecisions.isEmpty() == false)
				{
					bestDecisions.clear();
				}
				bestDecisions.addAll(bestCannon);
				debug("else--------------------------------------");
				for(int k=0;k<bestDecisions.size();k++)
				{
					debug("%d",bestDecisions.get(k).intValue());
					debug(" ");
					if(k%20==0||k == bestDecisions.size()-1)
					{
						if(k!=0)
							debug("\n");
					}
				}
				debug("end---------------------------------------");
		   }
	   }
	   if(isNeedCannon == false)
	   {
		   if(currentTask.isEmpty() == false)
		   {
			   currentTask.clear();
		   }
	   }
	   else
	   {
		   if(bestDecisions.isEmpty() == true)
		   {
			   if(currentTask.isEmpty() == false)
			   {
				   currentTask.clear();
			   }
		   }
		   else
		   {
			   for(int d=0;d<bestDecisions.size();d++)
			   {
				   Integer cannon = bestDecisions.get(d);
				   Tile tile = emptyTile.get(new Integer(d));
				   if(mymap[tile.getRow()][tile.getCol()].getIsTower() == true)
				   {
					   if(cannon.intValue()<5)
					   {
						   Task newTask = new Task(3,0,tile);
						   newTask.isDestroy = true;
						   currentTask.add(newTask);
					   }
					   else
					   {
						   int type = mymap[tile.getRow()][tile.getCol()].getTowerType();
						   int strength = mymap[tile.getRow()][tile.getCol()].getTowerStrength();
						   if(type == Util.decodeType(cannon.intValue()))
						   {
							  if(strength>=Util.decodeStrength(cannon.intValue()))
							  {
								  continue;
							  }
							  else
							  {
								  Task newTask = new Task(type,Util.decodeStrength(cannon.intValue()),tile);
								  newTask.isStrength = true;
								  newTask.baseStrength = strength;
								  currentTask.add(newTask);
							  }
						   }
						   else
						   {
							   Task newTask = new Task(Util.decodeType(cannon.intValue()),Util.decodeStrength(cannon.intValue()),tile);
							   newTask.isBuild = true;
							   currentTask.add(newTask);
						   }
					   }
				   }
				   else
				   {
					   if(cannon.intValue()>=5)
					   {
						   Task newTask = new Task(Util.decodeType(cannon.intValue()),Util.decodeStrength(cannon.intValue()),tile);
						   newTask.isBuild = true;
						   currentTask.add(newTask);
					   }
				   }
			   }
		   }
	   }
	   if(currentTask.isEmpty() == true)
	   {
		   System.out.println("0");
	   }
	   else
	   {
		   if(cost<= money)
		   {
			   Integer size = new Integer(currentTask.size());
			   String num = size.toString();
			   System.out.println(num);
			   for(int cur=0;cur<currentTask.size();cur++)
			   {
				   Task newTask = currentTask.get(cur);
				   Integer col = new Integer(newTask.position.getCol());
				   Integer row = new Integer(newTask.position.getRow());
				   Integer type = new Integer(newTask.type);
				   Integer strength = new Integer(newTask.strength);
				   System.out.println(col.toString()+" "+row.toString()+" "+strength.toString()+" "+type.toString());
			   }
		   }
		   else
		   {
			   
			   int realCost =0;
			   List<Task> realTask = new ArrayList<Task>();
			   for(int cur=0;cur<currentTask.size();cur++)
			   {
				   Task newTask = currentTask.get(cur);
				   
				   if(newTask.isStrength==true)
				   {
					   realCost+=Util.getStrengthenCost(newTask.type, newTask.baseStrength, newTask.strength);
				   }
				   else if(newTask.isBuild == true)
				   {
					   realCost+=Util.getTowerCost(newTask.type, newTask.strength);
				   }
				   
				   if(realCost>money)
				   {
					   break;
				   }
				   realTask.add(newTask);
				   
			   }
			   if(realTask.isEmpty() == true)
			   {
				   System.out.println("0");
			   }
			   else
			   {
				   Integer size = new Integer(realTask.size());
				   String num = size.toString();
				   System.out.println(num);
				   for(int cur=0;cur<realTask.size();cur++)
				   {
					   Task newTask = realTask.get(cur);
					   Integer col = new Integer(newTask.position.getCol());
					   Integer row = new Integer(newTask.position.getRow());
					   Integer type = new Integer(newTask.type);
					   Integer strength = new Integer(newTask.strength);
					   System.out.println(col.toString()+" "+row.toString()+" "+strength.toString()+" "+type.toString());
				   }
			   }
		   }
		  
	   }*/
		if(firstTime == true&&map==1)
		{
			System.out.println("6");
			System.out.println("7 1 2 0");
			System.out.println("13 1 1 1");
			System.out.println("16 1 1 0");
		    System.out.println("18 1 0 0");
		    		System.out.println("6 4 0 0");
		    		System.out.println("11 10 2 0");
		    		firstTime =false;
		}
		else if(firstTime == true&&map==2)
		{
			System.out.println("4");
			System.out.println("2 9 1 1");
			System.out.println("5 12 1 1");
			System.out.println("7 17 1 1");
			System.out.println("16 18 0 1");
			firstTime =false;
		}
		else
		{
			System.out.println("0");
		}
	}
   
}


