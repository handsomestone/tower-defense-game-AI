import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class GeneticAlgorithm
{
	private final int population;
	private final int chromoLength;
	private final int geneLength;
	private final int generationNum;
	private final double crossRate;
	private final double mutateRate;

	private Location map[][];
	private List<Tile> sEnemy;
	private List<Tile> gDefend;
	Map<Integer,Tile> emptyTile;
	private List<Enemy> enemyList;

	private int m_iGeneration;
	private int m_iFittestGenome;
	private double m_dBestFitnessScore;
	private double m_dTotalFitnessScore;
	private List<Integer> m_dBestDecisions;
	
	private double m_cost=1000000;
	private double m_dbestCost = 1000000;
	private double m_kill;

	private RangeStartComparator cmp = new RangeStartComparator();

	private List<Genome> vecGenomes;
	
	private Map<Integer,Boolean> resetCannonList;
	
	public GeneticAlgorithm(int population,int chromoLength,int geneLength,
		int generationNum,double crossRate,double mutateRate)
	{
		this.population = population;
		this.chromoLength = chromoLength;
		this.geneLength = geneLength;
		this.generationNum = generationNum;
		this.crossRate = crossRate;
		this.mutateRate = mutateRate;
		vecGenomes = new ArrayList<Genome>();
		m_dBestDecisions = new ArrayList<Integer>();
		resetCannonList = new HashMap<Integer,Boolean>();
		m_kill =0;
	}
	public void run()
	{
		//createStartPopulation();
	}
	public void limitNum(int enemyTotalLife,List<Integer> lastDecisions)
	{
		createStartPopulation(enemyTotalLife,lastDecisions);
	}
	public void limitNum(int enemyTotalLife)
	{
		createStartPopulation(enemyTotalLife);
	}
	public void initMap(Location[][] map,List<Tile> sEnemy,List<Tile> gDefend,Map<Integer,Tile> emptyTile,List<Enemy> enemyList)
	{
		this.map = map;
		this.sEnemy = sEnemy;
		this.gDefend = gDefend;
		this.emptyTile = emptyTile;
		this.enemyList =enemyList;/*假设在towers里面已经计算出了速度*/
	}
	private int isInBestPath(Node newNode,List<Node> path)
	{
		if(path == null)
			return 0;
		for(int i=1;i<path.size();i++)
		{
			if(newNode.status.equals(path.get(i))==true)
				return i;
		}
		return 0;
	}
	private List<Node>  getBestPath(Tile startPoint,Map<Integer,Integer> tileToCannon)
    {
    	Tile startStatus = startPoint;
    	int minPath = 8092;
    	boolean isPath =false;
    	List<Node> bestPath = new ArrayList<Node>();
    	List<Node> possiblePath = new ArrayList<Node>();
    	//List<Node> possiblePath = new ArrayList<Node>();
    	//System.out.println("getBestPath "+gDefend.size());
    	for(int i=0;i<gDefend.size();i++)
    	{
    		Tile endStatus = gDefend.get(i);
    		//System.out.println("startStatus "+startStatus.getRow()+startStatus.getCol());
    		//System.out.println("endStatus "+endStatus.getRow()+endStatus.getCol());
    		AstarDoer doer = new AstarDoer(startStatus,endStatus,map,tileToCannon );
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
    	/*System.out.println("MinPath "+ minPath);
    	System.out.println("BestPath");
    	if(bestPath!=null)
    	{
	    	for(Node iter:bestPath)
	    	{
	    		System.out.print(iter.status.getRow());
	    		System.out.print('.');
	    		System.out.print(iter.status.getCol());
	    		System.out.print("->");
	    	}
    	}*/
    	return bestPath;
    }
	public void Test()
	{
		Genome tGenome = new Genome(chromoLength);
		List<Integer> test= tGenome.getGenome();
		double f = testRoute(test);
		//System.out.println("double f"+f);
	}
	private List<Node> reverse(List<Node> path)
	{
		List<Node> reverseNode = new ArrayList<Node>();
		int length = path.size();
		length = length-1;
		for(;length>=0;length--)
		{
			Node newNode = path.get(length);
			reverseNode.add(newNode);
		}
		return reverseNode;
	}
	private boolean isHasTower(CannonTower cannon,List<CannonTower> cannonList)
	{
		for(int k=0;k<cannonList.size();k++)
		{
			CannonTower oldCannon = cannonList.get(k);
			if(oldCannon.getPos().equals(cannon.getPos())==true)
			{
				if(oldCannon.getTowerType()==cannon.getTowerType())
				{
					if(oldCannon.getTowerStrength()==cannon.getTowerStrength())
					{
						return true;
					}
				}
			}
		}
		return false;
		
	}
	private void debug(String format ,Object... args) {
    	
    	FileOutputStream file;
		try {
			file = new FileOutputStream("debug-shi.txt",true);
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
	public boolean isNeedCompute(List<Integer> lastDecisions)
	{
		double fitness = testRoute(lastDecisions);
		if(this.m_kill == this.enemyList.size())
		{
			return false;
		}
		return true;
	}
	private double testRoute(List<Integer> decisions)
	{
		
		int cost =0;
		int killEnemy = 0;
		int enemySize = enemyList.size();
		double fitness = 0;
		
		resetCannonList.clear();
		
		Map<Integer,Integer> tileToCannon = new HashMap<Integer,Integer>();
		for(int i=0;i<decisions.size();i++)
		{
			Integer tileDecision = decisions.get(i);
			Tile tile = emptyTile.get(new Integer(i));
			int key = 0;
			int row = tile.getRow();
			int col = tile.getCol();
			key = row<<6|col;	
			Integer rowCol = new Integer(key);
			tileToCannon.put(rowCol,tileDecision);
		}
		/*
		for(int d=0;d<decisions.size();d++)
		{
			Tile td = emptyTile.get(new Integer(d));
			System.out.println("id "+d+"row "+td.getRow()+"col "+td.getCol());
			
		}*/
		Map<Integer,List<Node>> path = new HashMap<Integer,List<Node>>();
		for(int p=0;p<sEnemy.size();p++)
		{
			List<Node> bestPath = getBestPath(sEnemy.get(p),tileToCannon);
			if(bestPath.isEmpty() == true||bestPath==null)
			{
				return 0;
			}
			Integer key = new Integer(p);
			List<Node> getPath = path.get(key);
			if(getPath != null)
			{
				getPath.clear();
				getPath.addAll(bestPath);
			}
			else
			{
				path.put(key,bestPath);
			}
		}
		Map<Integer,List<Enemy>> startEnemy = new HashMap<Integer,List<Enemy>>();
		for(int startE=0;startE<sEnemy.size();startE++)
		{
			Integer stE = new Integer(startE);
			List<Enemy> stEnemy = startEnemy.get(stE);
			if(stEnemy==null)
			{
				stEnemy = new ArrayList<Enemy>();
				
			}
			else
			{
				stEnemy.clear();
			}
			for(int nE =0;nE<enemyList.size();nE++)
			{
				if(enemyList.get(nE).getStartPoint().equals(sEnemy.get(startE))==true)
				{
					//Enemy newEnemy = new Enemy(enemyList.get)
					stEnemy.add(enemyList.get(nE));
				}
			}
			startEnemy.put(stE,stEnemy);
		}
		//List<CannonTower> hasRegCostTower = new ArrayList<CannonTower>();
		//List<CannonTower> hasAttackTower = new ArrayList<CannonTower>();
		Map<Integer,Integer> idToHurt = new HashMap<Integer,Integer>();
		for(int costI=0;costI<decisions.size();costI++)
		{
			Integer cannonC = decisions.get(costI);
			Boolean reset= resetCannonList.get(cannonC);
			if(reset == null)
			{
				Boolean resetCannon = new Boolean(false);
				resetCannonList.put(cannonC, resetCannon);
			}
			else
			{
				Boolean resetCannon = new Boolean(false);
				resetCannonList.put(cannonC, resetCannon);
			}
			if(cannonC.intValue()<5)
				continue;
			Tile tileC = emptyTile.get(new Integer(costI));
			int towerType = Util.decodeType(cannonC.intValue());
			int towerStrength = Util.decodeStrength(cannonC.intValue());
			if(map[tileC.getRow()][tileC.getCol()].getIsTower()==true)
			{
				
				if(map[tileC.getRow()][tileC.getCol()].getTowerType()==towerType)
				{
					if(map[tileC.getRow()][tileC.getCol()].getTowerStrength()< towerStrength)
					{
						//if(isHasTower(cannonTower,hasRegCostTower) == false)
						//{
							cost+= Util.getStrengthenCost(towerType, map[tileC.getRow()][tileC.getCol()].getTowerStrength(), towerStrength);
							//hasRegCostTower.add(cannonTower);
						//}
						
					}
				}
				else
				{
					//if(isHasTower(cannonTower,hasRegCostTower) == false)
					//{
					//	System.out.println("cannon Tower"+ cannonTower.getPos().getRow()+" "+cannonTower.getPos().getCol());
						cost+=Util.getTowerCost(towerType, towerStrength);
					//	hasRegCostTower.add(cannonTower);
						//System.out.println("cannon Tower hasRegCostTower.add(cannonTower);");
					//}
					
				}
				
			}
			else
			{
				
					cost+=Util.getTowerCost(towerType, towerStrength);
			
				
				
			}
		}
		//List<Integer> resetCannon = new ArrayList<Integer>();
		List<Tile> attackTower = new ArrayList<Tile>();
		for(int sE =0;sE<sEnemy.size();sE++)
		{
		  	PriorityQueue<RangeStart> cannonRange= new PriorityQueue<RangeStart> (10000,cmp); 
			for(int c=0;c<decisions.size();c++)
			{
				Integer tileDecision = decisions.get(c);
				Integer ID = new Integer(c);
				Tile tile = emptyTile.get(new Integer(c));
				RangeStart range = new RangeStart(tile,sEnemy.get(sE),tileDecision,ID);
				cannonRange.add(range);
			}
			while(cannonRange.isEmpty()==false)
			{
				RangeStart newRange = cannonRange.poll();
				
				
				
				Integer cannon = newRange.decide;
				if(cannon.intValue()<5)
				{
					continue;
				}
				boolean isGoOut = false;
				Tile tile = newRange.posOfCannon;
				for(int k=0;k<attackTower.size();k++)
				{
					if(tile != null)
					{
						if(tile.equals(attackTower.get(k))==true)
						{
							isGoOut = true;
						}
					}
				}
				if(isGoOut == true)
				{
					//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					continue;
				}
				
				
				
				int towerType = Util.decodeType(cannon.intValue());
				int towerStrength = Util.decodeStrength(cannon.intValue());
				CannonTower cannonTower = new CannonTower(towerType,towerStrength,tile);
				//System.out.println("hasRegCostTower cost+===" +hasRegCostTower.size());
				
				/*if(isHasTower(cannonTower,hasAttackTower)==true)
				{
					continue;
				}*/
				//System.out.println("after isHasTower");
				boolean start =false;
				int leaveNode = 0;
				int arriveNode =0;
				int leaveGvalue = 0;
				int arriveGvalue = 0;
				int attackTimes =0;
				
				for(int scE=0;scE<sEnemy.size();scE++)
				{
					//System.out.println("int scE=0;scE<sEnemy.size()"+scE);
					List<Node> bestPath = path.get(new Integer(scE));
					if(cannonTower.checkIsInRange(bestPath)==false)
					{
						continue;
					}
					attackTimes++;
					start =false;
					leaveNode = 0;
					arriveNode =0;
					leaveGvalue = 0;
					arriveGvalue = 0;
					List<Node> reversePath = reverse(bestPath);
					for(int bPath=0;bPath<reversePath.size();bPath++)
					{
						Node bNode = reversePath.get(bPath);
						if(cannonTower.checkIsInRange(bNode))
						{
							if(start == false)
							{
								start = true;
								arriveNode = bPath;
								leaveNode = bPath;
							}
							else
							{
								leaveNode = bPath;
								continue;
							}
						}
						else
						{
							if(start == true)
							{
								start = false;
								if(arriveNode != leaveNode)
								{
									if(leaveNode==(reversePath.size()-1))
									{
										leaveGvalue = reversePath.get(leaveNode).gvalue;
									}
									else
									{
										leaveGvalue = reversePath.get(leaveNode+1).gvalue;
									}
									arriveGvalue = reversePath.get(arriveNode).gvalue;
								}
								else
								{
									if(leaveNode == (reversePath.size()-1))
									{
										leaveGvalue = reversePath.get(leaveNode).gvalue;
									}
									else
									{
										leaveGvalue = reversePath.get(leaveNode+1).gvalue;
									}
									arriveGvalue = reversePath.get(arriveNode).gvalue;
								}
								Integer keyAttack = new Integer(scE);
								List<Enemy> attackEnemy = startEnemy.get(keyAttack);
								List<Target> targetList = new ArrayList<Target>();
								if(attackEnemy == null)
								{
									//System.out.println("attackEnemyList error");
								}
								else
								{
									for(int attackE=0;attackE<attackEnemy.size();attackE++)
									{
										Enemy toAttackE = attackEnemy.get(attackE);
										float arriveTime =  (toAttackE.getTimeToCome()+arriveGvalue/toAttackE.getSpeed());
										float leaveTime =  (toAttackE.getTimeToCome()+leaveGvalue/toAttackE.getSpeed());
										int ID = toAttackE.getEnemyID();
										int life = toAttackE.getLife();
										int timeToCome = toAttackE.getTimeToCome();
										/*System.out.println("cannon tower "+cannonTower.getPos().getRow()+" "+cannonTower.getPos().getCol());
										System.out.println("attack enemy arriveGvalue "+arriveGvalue);
										System.out.println("attack enemy leaveGvalue "+leaveGvalue);
										System.out.println("attack enemy speed "+toAttackE.getSpeed());
										System.out.println("attack enemy Id "+toAttackE.getEnemyID());
										System.out.println("attack enemy timeToCome "+toAttackE.getTimeToCome());
										*/
										Target newTarget = new Target(ID,life,timeToCome,arriveTime,leaveTime);
									//	newTarget.setSE(sE);
										targetList.add(newTarget);
									}
									cannonTower.addTargetList(targetList);
								}
							}
						}
					}
					if(start == true)
					{
						start = false;
						if(arriveNode != leaveNode)
						{
							if(leaveNode==(reversePath.size()-1))
							{
								leaveGvalue = reversePath.get(leaveNode).gvalue;
							}
							else
							{
								leaveGvalue = reversePath.get(leaveNode+1).gvalue;
							}
							arriveGvalue = reversePath.get(arriveNode).gvalue;
						}
						else
						{
							if(leaveNode == (reversePath.size()-1))
							{
								leaveGvalue = reversePath.get(leaveNode).gvalue;
							}
							else
							{
								leaveGvalue = reversePath.get(leaveNode+1).gvalue;
							}
							arriveGvalue = reversePath.get(arriveNode).gvalue;
						}
						Integer keyAttack = new Integer(scE);
						List<Enemy> attackEnemy = startEnemy.get(keyAttack);
						List<Target> targetList = new ArrayList<Target>();
						if(attackEnemy == null)
						{
							//System.out.println("attackEnemyList null");
						}
						else
						{
							for(int attackE=0;attackE<attackEnemy.size();attackE++)
							{
								Enemy toAttackE = attackEnemy.get(attackE);
								float arriveTime =  (toAttackE.getTimeToCome()+arriveGvalue/toAttackE.getSpeed());
								float leaveTime =  (toAttackE.getTimeToCome()+leaveGvalue/toAttackE.getSpeed());
								int ID = toAttackE.getEnemyID();
								int life = toAttackE.getLife();
								int timeToCome = toAttackE.getTimeToCome();
			
								Target newTarget = new Target(ID,life,timeToCome,arriveTime,leaveTime);
							//	newTarget.setSE(sE);
								targetList.add(newTarget);
							}
							cannonTower.addTargetList(targetList);
						}
					}
				}
				if(attackTimes>=2)
				{
					attackTower.add(tile);
					//hasAttackTower.add(cannonTower);
				}
				if(cannonTower.isAttack == true)
				{
					List<Target> attacked = cannonTower.Attack();
					for(int attackIndex=0;attackIndex<attacked.size();attackIndex++)
					{
						Integer ID = new Integer(attacked.get(attackIndex).getID());
						Integer hurtLife = idToHurt.get(ID);
						if(hurtLife==null)
						{
							hurtLife = new Integer(attacked.get(attackIndex).getHurtLife());
							idToHurt.put(ID,hurtLife);
						}
						else
						{
							int hurt = hurtLife.intValue();
							hurt = hurt+attacked.get(attackIndex).getHurtLife();
							hurtLife = new Integer(hurt);
							idToHurt.put(ID,hurtLife);
						}
					}
				//	System.out.println("after cannonTower.isAttack"); 
					for(int sdE=0;sdE<sEnemy.size();sdE++)
					{
						//System.out.println("after in cannonTower.isAttack"); 
						Integer index = new Integer(sdE);
						
						List<Enemy> sdEList = startEnemy.get(index);
						
						if(sdEList == null)
						{
						//	System.out.println("NUll get startEnemy");
							continue;
						}
						else
						{
							if(sdEList.isEmpty() == false)
							{
								Iterator<Enemy> iter = sdEList.iterator();
								while(iter.hasNext())
								{
									boolean remove =false;
									Enemy iterE = iter.next();
									Integer h_enemyID = new Integer(iterE.getEnemyID());
									Integer hurt_enemy = idToHurt.get(h_enemyID);
									if(hurt_enemy!=null)
									{
										if(hurt_enemy.intValue()>=iterE.getLife())
										{
											killEnemy+=1;
											remove = true;
										}
										else
										{
											//iterE.subLife(hurt_enemy.intValue());
										}
									}
									if(remove==true)
									{
										iter.remove();
									}
								}
								
							}
						}
						
					}
					Boolean resetAttack =  resetCannonList.get(newRange.cannonID);
					if(resetAttack ==null)
					{
						resetAttack = new Boolean(true);
						resetCannonList.put(newRange.cannonID, resetAttack);
						//System.out.println("put !!!end cannonTower.isAttack"); 
					}
					else
					{
						resetAttack = new Boolean(true);
						resetCannonList.put(newRange.cannonID, resetAttack);
						//System.out.println("put !!!end cannonTower.isAttack"); 
					}
				//	System.out.println("end cannonTower.isAttack"); 
				}
				
				
			}
		}
	
		double kill = (double) killEnemy;
		double total = (double) enemySize;
		double totalCost =(double)cost;
		
			m_cost =totalCost;
			m_kill = kill;
		
		m_kill = kill;
		//System.out.println("cost "+totalCost+"kill "+kill+"totalE "+total);
		double logCost = Math.log10(totalCost);
		if(cost != 0)
			fitness= (kill/total)*0.8+(1/logCost)*0.2*(kill/total);
		else
			fitness =0.0;
		debug("%f",kill);
		debug("\n");
		debug("%f",totalCost);
		debug("\n");
		debug("%f",fitness);
		debug("\n");
		//System.out.println("fitness +cost+kill"+fitness+"+"+cost+"+"+kill);
		return fitness;
	}
	private void createStartPopulation(int enemyTotalLife,List<Integer> lastDecisions)
	{
		vecGenomes.clear();
		for(int i=0;i<(population-1);i++)
		{
			if(i<200)
			{
				vecGenomes.add(new Genome(chromoLength,geneLength-7,enemyTotalLife));
			}
			else if(i>=200&&i<550)
			{
				vecGenomes.add(new Genome(chromoLength,geneLength-3,enemyTotalLife));
			}
			else
			{
				vecGenomes.add(new Genome(chromoLength,geneLength,enemyTotalLife));
			}
			
		}
		if(lastDecisions.isEmpty() == false)
		{
			Genome lastGenome = new Genome(lastDecisions,geneLength,enemyTotalLife);
			vecGenomes.add(lastGenome);
		}
		else
		{
			vecGenomes.add(new Genome(chromoLength,geneLength,enemyTotalLife));
		}
		
		m_iGeneration		 = 0;
		m_iFittestGenome	 = 0;
		m_dBestFitnessScore  = 0;		
		m_dTotalFitnessScore = 0;
		m_cost = 1000000;
		
	}
	private void createStartPopulation(int enemyTotalLife)
	{
		vecGenomes.clear();
		for(int i=0;i<population;i++)
		{
			if(i<200)
			{
				vecGenomes.add(new Genome(chromoLength,geneLength-7,enemyTotalLife));
			}
			else if(i>=200&&i<550)
			{
				vecGenomes.add(new Genome(chromoLength,geneLength-3,enemyTotalLife));
			}
			else
			{
				vecGenomes.add(new Genome(chromoLength,geneLength,enemyTotalLife));
			}
			
		}
		m_iGeneration		 = 0;
		m_iFittestGenome	 = 0;
		m_dBestFitnessScore  = 0;		
		m_dTotalFitnessScore = 0;
		m_cost = 1000000;
		//Genome genome = vecGenomes.get(0);
		//List<Integer> tVec =genome.vecBits;
     /*	for(int k=0;k<population;k++)
     	{
     		List<Integer> tVec = vecGenomes.get(k).vecBits;
     		for(int i=0;i<tVec.size();i++)
    		{
    			System.out.print(tVec.get(i).intValue());
    			System.out.print(' ');
    			if(i%20 ==0)
    				System.out.println();
    		}
     	}*/
		
	}
	private void updateFitnessScores()
	{
		m_iFittestGenome		= 0;
		//m_dBestFitnessScore		= 0;//根据测试发现有可能第一代为最优解，因此fitnessscore取所有最优解
		m_dTotalFitnessScore	= 0;
		m_cost=1000000;
		for(int i=0;i<population;i++)
		{
			List<Integer> vecCannons = vecGenomes.get(i).getGenome();
			double fitness = testRoute(vecCannons);
			
			if(fitness > m_dBestFitnessScore)
			{
				m_dbestCost = this.m_cost;
				debug("fitness > m_dBestFitnessScore  ---------------------");
				if(m_dBestDecisions.isEmpty() == false)
				{
					m_dBestDecisions.clear();
				}
				m_dBestDecisions.addAll(vecCannons);
				for(int t=0;t<m_dBestDecisions.size();t++)
				{
					debug("%d",m_dBestDecisions.get(t).intValue());
					debug(" ");
					if(t%20 ==0||t==(m_dBestDecisions.size()-1))
					{
						if(t!=0)
						debug("/n");
					}
				}
			}
			if(resetCannonList != null)
			{
				vecGenomes.get(i).resetGenome(resetCannonList);
				//System.out.println("resetCannonLIst.size  ~~~~~~~~~"+resetCannonList.size());
				if(fitness > m_dBestFitnessScore)
				{
					debug("after reset this vecGenomes ---------------------");
					List<Integer> vec = vecGenomes.get(i).vecBits;
					for(int f=0;f<vec.size();f++)
					{
						debug("%d",vec.get(f).intValue());
						debug(" ");
						if(f%20 ==0||f==(vec.size()-1))
						{
							debug("/n");
						}
					}
					debug("END reset this vecGenomes ---------------------");
				}
			}
			vecGenomes.get(i).setFitness(fitness);
			m_dTotalFitnessScore+=fitness;
			if(fitness>m_dBestFitnessScore)
			{
				m_dBestFitnessScore = fitness;
				m_iFittestGenome = i;
			}
		}
	/*	System.out.println("update fitness~~~~~~~~~~~~~~~~~~");
		System.out.println("update fitness~~~~best scorre~~~~~~~~~~"+m_dBestFitnessScore);
		System.out.println("update fitness~~~~best m_iFittestGenome~~~~~~~~~~"+m_iFittestGenome);
		List<Integer> best = vecGenomes.get(m_iFittestGenome).vecBits;
		for(int i=0;i<best.size();i++)
		{
			System.out.print(best.get(i).intValue());
			System.out.print(" ");
			if(i%20 ==0)
				System.out.println();
		}*/
		//	vector<Integer> vecDirections = Decode(m_vecGenomes[i].vecBits);

		
		//	m_vecGenomes[0].dFitness = m_BobsMap.TestRoute(vecDirections, TempMemory);

	
	
	}
	private Genome RouletteWheelSelection()
	{
		double fSlice = Util.RandFloat() * m_dTotalFitnessScore;
		double cfTotal = 0.0;
		int selectGenome =0;
		for(int i=0;i<population;i++)
		{
			cfTotal += vecGenomes.get(i).getFitness();
			if(cfTotal > fSlice)
			{
				selectGenome = i;
				break;
			}
		}
		return vecGenomes.get(selectGenome);
	}
	private void Mutate(List<Integer> vecBits)
	{
		for(int curBit=0;curBit<vecBits.size();curBit++)
		{
			if(Util.RandFloat()<mutateRate)
			{
			/*	int cannon = Util.RandInt(geneLength);
				while(cannon == vecBits.get(curBit).intValue())
				{
					cannon = Util.RandInt(geneLength);
				}*/
				int cannon = vecBits.get(curBit).intValue();
				if(cannon == 0)
				{
					cannon = Util.RandInt(geneLength);
				}
				else
				{
					cannon = Util.RandInt(cannon-1);
				}
				Integer nowCannon = new Integer(cannon);
				vecBits.set(curBit, nowCannon);
			}
		}
	}
	private void CrossOver(List<Integer> mum,List<Integer> dad,List<Integer> baby1,List<Integer> baby2)
	{
		if((Util.RandFloat()>crossRate)||(mum.equals(dad)))
		{
			baby1.addAll(mum);
			baby2.addAll(dad);
			return;
		}
		int cp = Util.RandInt(chromoLength);
		baby1.clear();
		baby2.clear();
		for(int i=0;i<cp;i++)
		{
			baby1.add(mum.get(i));
			baby2.add(dad.get(i));
		}
		for(int j=cp;j<mum.size();j++)
		{
			baby1.add(dad.get(j));
			baby2.add(mum.get(j));
		}
	}
	private void epoch()
	{
	
		updateFitnessScores();
		int NewBabies = 0;
		m_iGeneration++;
		if(m_iGeneration == generationNum)
		{
			return;
		}
		//System.out.println("m_iGeneration!!!!! "+m_iGeneration);
		List<Genome> vecBabyGenomes = new ArrayList<Genome>();
		while(NewBabies <population)
		{
			Genome mum = RouletteWheelSelection();
			Genome dad = RouletteWheelSelection();
			Genome baby1 = new Genome();
			Genome baby2 = new Genome();
			CrossOver(mum.vecBits,dad.vecBits,baby1.vecBits,baby2.vecBits);
			Mutate(baby1.vecBits);
			Mutate(baby2.vecBits);
			vecBabyGenomes.add(baby1);
			vecBabyGenomes.add(baby2);
			NewBabies+=2;
		}
		vecGenomes = vecBabyGenomes;
		
	}
	public List<Integer> getBestCannon()
	{
		return this.m_dBestDecisions;
	}
	public double getCost()
	{
		return this.m_dbestCost;
	}
	public void Start()
	{
		while(m_iGeneration<generationNum)
		{
			//System.out.println("m_iGeneration +++++!!!!"+m_iGeneration);
			epoch();
		}
	/*	System.out.println("m_iGeneration +++++!!!!"+m_iGeneration);
		System.out.println("best fitness " + m_dBestFitnessScore);
		System.out.println("Total fitness " + m_dTotalFitnessScore);
		System.out.println("Generation "+ m_iGeneration );
		System.out.println("best din vec "+ m_iFittestGenome);
		System.out.println("min cost in vec "+m_dbestCost);
		System.out.println("min cost in kill "+m_kill);
		Genome best =vecGenomes.get(m_iFittestGenome);
		for(int i=0;i<best.vecBits.size();i++)
		{
			System.out.print(best.vecBits.get(i).intValue());
			System.out.print(" ");
			if(i%20 ==0)
				System.out.println();
		}*/
	}
	
	class NodeComparator implements Comparator<Node> {  
        @Override  
        public int compare(Node x, Node y) { 
   	       
			return x.gvalue - y.gvalue; 
        }  
    }  
	class RangeStartComparator implements Comparator<RangeStart>{

		public int compare(RangeStart x,RangeStart y)
		{
			return x.getDistanceFromStart()-y.getDistanceFromStart();
		}
	}
	class RangeStart
	{
		public Tile posOfCannon;
		Tile startTile;
		//public int distanceFromStart;
		public Integer decide;
		public Integer cannonID;
		public RangeStart(Tile posOfCannon,Tile startTile,Integer decide,Integer ID)
		{
			this.posOfCannon = posOfCannon;
			this.startTile = startTile;
			this.decide = decide;
			this.cannonID =ID;
		}
		public int getDistanceFromStart()
		{
			int rowDis = posOfCannon.getRow() - startTile.getRow();
			int colDis = posOfCannon.getCol() - startTile.getCol();
			return rowDis*rowDis+colDis*colDis;
		}
	}
}