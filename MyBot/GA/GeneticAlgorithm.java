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

	private List<Genome> vecGenomes;
	
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
	}
	public void run()
	{
		createStartPopulation();
	}
	public void initMap(Location[][] map,List<Tile> sEnemy,List<Tile> gDefend,Map<Integer,Tile> emptyTile,List<Enemy> enemyList)
	{
		this.map = map;
		this.sEnemy = sEnemy;
		this.gDefend = gDefend;
		this.emptyTile = emptyTile;
		this.enemyList =enemyList;/*假设在towers里面已经计算出了速度*/
	}
	private List<Node>  getBestPath(Tile startPoint,Map<Integer,Integer> tileToCannon)
    {
    	Tile startStatus = startPoint;
    	int minPath = 8092;
    	boolean isPath =false;
    	List<Node> bestPath = new ArrayList<Node>();
    	//System.out.println("getBestPath "+gDefend.size());
    	for(int i=0;i<gDefend.size();i++)
    	{
    		Tile endStatus = gDefend.get(i);
    		System.out.println("startStatus "+startStatus.getRow()+startStatus.getCol());
    		System.out.println("endStatus "+endStatus.getRow()+endStatus.getCol());
    		AstarDoer doer = new AstarDoer(startStatus,endStatus,map,tileToCannon );
    		doer.run();
    		if(doer.hasPath()==true)
    		{
    			System.out.println("this path length "+doer.getLength());
    			if(doer.getLength()<=minPath)
    			{
    				if(doer.getLength()==minPath)
    				{
    					System.out.println("has one same");
    					Node seNode= doer.getEndPath();
    					seNode = seNode.parent;
    					Node preNode=null;
    					for(int b=1;b<bestPath.size();b++)
    					{
    						Node bestNode = bestPath.get(b);
    						System.out.println("bestNode has one same");
    						for(;seNode!=null;seNode=seNode.parent)
    						{   if(seNode.equal(bestNode.status)==true)
    							break;
    							preNode = seNode;	
    						}
    						if(seNode != null)
    						{
    							
    							Node preBNode = bestPath.get(b-1);
    							if(preBNode.priority>preNode.priority)
    							{
    								bestPath.clear();
    								Node newNode = doer.getEndPath();
    								while(newNode!=null)
    								{
    									bestPath.add(newNode);
    									newNode = newNode.parent;
    								}
    								
    							}
    							break;
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
    	System.out.println("MinPath "+ minPath);
    	System.out.println("BestPath");
    	for(Node iter:bestPath)
    	{
    		System.out.print(iter.status.getRow());
    		System.out.print('.');
    		System.out.print(iter.status.getCol());
    		System.out.print("->");
    	}
    	return bestPath;
    }
	private double testRoute(List<Integer> decisions)
	{
		int cost =0;
		int killEnemy = 0;
		int enemySize = enemyList.size();
		double fitness = 0;
		NodeComparator cmpNode = new NodeComparator(); 
		Map<Integer,Integer> tileToCannon = new HashMap<Integer,Integer>();
		for(int i=0;i<decisions.size;i++)
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
		Map<Integer,List<Node>> path = new HashMap<Integer,List<Node>>();
		for(int p=0;p<sEnemy.size();p++)
		{
			List<Node> bestPath = getBestPath(sEnemy.get(p),tileToCannon);
			if(bestPath.isEmpty == true)
			{
				return 0;
			}
			Integer key = new Integer(p);
			List<Node> getPath = path.get(key);
			if(getPath.isEmpty != null)
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
				if(enemyList.get(nE).startPoint.equals(sEnemy.get(startE))==true)
				{
					stEnemy.add(enemyList.get(nE));
				}
			}
			stEnemy.put(stE,stEnemy);
		}
		Map<Integer,Integer> idToHurt = new HashMap<Integer,Integer>();
		for(int c=0;c<decisions.size();c++)
		{
		 /*0-4 none 5-9 rapid 10-14 attack*/
			Integer cannon = decisions.get(c);
			if(cannon.intValue()<5)
			{
				continue;
			}
			Tile tile = emptyTile.get(new Integer(c));
			int towerType =Util.decodeType(cannon.intValue());
			int towerStrength = Util.decodeStrength(cannon.intValue());
			CannonTower cannonTower = new CannonTower(towerType,towerStrength,tile);
			if(map[tile.getRow()][tile.getCol()].getIsTower()==true)
			{
				
				if(map[tile.getRow()][tile.getCol()].getTowerType()==cannonTower.getTowerType())
				{
					if(map[tile.getRow()][tile.getCol()].getTowerStrength()< cannonTower.getTowerStrength())
					{
						cost+= cannonTower.getStrengthenCost(map[tile.getRow()][tile.getCol()].getTowerStrength(),cannonTower.getTowerStrength());
					}
				}
				else
				{
					cost+=cannonTower.getTowerCost();
				}
				
			}
			else
			{/*
				int towerType =Util.decodeType(cannon.intValue());
				int towerStrength = Util.decodeStrength(cannon.intValue());
				CannonTower cannonTower = new CannonTower(towerType,towerStrength,tile);*/
				cost+=cannonTower.getTowerCost();
			}
			for(int sE=0;sE<sEnemy.size();i++)
			{
				List<Node> bestPath =  getBestPath(sEnemy.get(sE),tileToCannon);
				/*PriorityQueue<Node> path = new PriorityQueue<Node>(1000000,cmp );
				for(Node iter:bestPath)
				{
					path.add(iter)
				}*/
				if(cannonTower.checkIsInRange(bestPath)==false)
				{
					continue;
				}
				boolean start =false;
				int leaveNode = 0;
				int arriveNode =0;
				int leaveGvalue = 0;
				int arriveGvalue = 0;
				for(int bPath=0;bPath<bestPath.size();bPath++)
				{
					Node rangeNode = bestPath.get(bPath);
					if(cannonTower.checkIsInRange(rangeNode)
					{
						if(start==false)
						{
							leaveNode = bPath;
							arriveNode= bPath;
							start = true;
						}
						else if(start==true)
						{
							arriveNode = bPath;
							continue;
						}
					}
					else
					{
						if(start == true)
						{		
							start = false;
							if(leaveNode != arriveNode)
							{
								if(leaveNode == 0)
								{
									leaveGvalue =10+bestPath.get(leaveNode).gvalue;
								}
								else
								{
									leaveGvalue = bestPath.get(leaveNode-1).gvalue;
								}
								arriveGvalue = bestPath.get(arriveNode).gvalue;
							}
							else
							{
								if(leaveNode ==0)
								{
									leaveGvalue = 10;
								}
								else
								{
									leaveGvalue = bestPath.get(leaveNode-1).gvalue;
								}
								arriveGvalue = bestPath.get(arriveNode).gvalue;
							}
							Integer keyAttack = new Integer(sE);
							List<Enemy> attackEnemy = startEnemy.get(keyAttack);
							List<Target> targetList = new ArrayList<Target>();
							if(attackEnemy == null)
							{
								System.out.println("attackEnemyList error");
							}
							else
							{
								for(int attackE=0;attackE<attackEnemy.size();attackE++)
								{
									Enemy toAttackE = attackEnemy.get(attackE);
									int arriveTime = toAttackE.getTimeToCome()+arriveGvalue/toAttackE.getSpeed();
									int leaveTime = toAttackE.getTimeToCome()+leaveTime/toAttackE.getSpeed();
									int ID = toAttackE.getEnemyID();
									int life = toAttackE.getLife();
									int timeToCome = toAttackE.getTimeToCome();
				
									Target newTarget = Target(ID,life,timeToCome,arriveTime,leaveTime);
								//	newTarget.setSE(sE);
									targetList.add(newTarget);
								}
								cannonTower.addTargetList(targetList);
							}
						}
					}
				}
				
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
			}
			
		}
		for(int hurtEnemy=0;hurtEnemy<enemyList.size();hurtEnemy++)
		{
			Enemy h_enemy = enemyList.get(hurtEnemy);
			Integer h_enemyID = new Integer(h_enemy.getEnemyID());
			Integer hurt_enemy = idToHurt.get(h_enemyID);
			if(hurt_enemy != null)
			{
				if(hurt_enemy.intValue()>=h_enemy.getLife())
				{
					killEnemy+=1;
				}
			}
		}
		double kill = (double) killEnemy;
		double total = (double) enemySize;
		double totalCost =(double)cost;
		if(cost != 0)
			fitness= (kill/total)*0.6+(1/totalCost)*0.4;
		else
			fitness =0.0;

		return fitness;
	}
	private void createStartPopulation()
	{
		vecGenomes.clear();
		for(int i=0;i<population;i++)
		{
			vecGenomes.add(new Genome(chromoLength,geneLength));
		}
		m_iGeneration		 = 0;
		m_iFittestGenome	 = 0;
		m_dBestFitnessScore  = 0;		
		m_dTotalFitnessScore = 0;
	}
	private void updateFitnessScores()
	{
		m_iFittestGenome		= 0;
		m_dBestFitnessScore		= 0;
		m_dTotalFitnessScore	= 0;

	
	 
		//update the fitness scores and keep a check on fittest so far
		for (int i=0; i<m_iPopSize; ++i)
		{
			//decode each genomes chromosome into a vector of directions
			vector<int> vecDirections = Decode(m_vecGenomes[i].vecBits);

			//get it's fitness score
			m_vecGenomes[i].dFitness = m_BobsMap.TestRoute(vecDirections, TempMemory);

			//update total
			m_dTotalFitnessScore += m_vecGenomes[i].dFitness;

			//if this is the fittest genome found so far, store results
			if (m_vecGenomes[i].dFitness > m_dBestFitnessScore)
			{
				m_dBestFitnessScore = m_vecGenomes[i].dFitness;

				m_iFittestGenome = i;

				m_BobsBrain = TempMemory;

				//Has Bob found the exit?
				if (m_vecGenomes[i].dFitness == 1)
				{
					//is so, stop the run
					m_bBusy = false;
				}
		}

		TempMemory.ResetMemory();
	
	}//next genome
	}
	private void epoch()
	{
		updateFitnessScores();
	}
	class NodeComparator implements Comparator<Node> {  
        @Override  
        public int compare(Node x, Node y) { 
   	       
			return x.gvalue - y.gvalue; 
        }  
    }  
}